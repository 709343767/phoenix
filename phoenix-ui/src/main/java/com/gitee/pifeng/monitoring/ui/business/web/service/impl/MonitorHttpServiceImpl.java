package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.util.LayUiUtils;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorHttpDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorHttpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeHttpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorHttpVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.UiPackageConstructor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * HTTP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
@Service
public class MonitorHttpServiceImpl extends ServiceImpl<IMonitorHttpDao, MonitorHttp> implements IMonitorHttpService {

    /**
     * UI端包构造器
     */
    @Autowired
    private UiPackageConstructor uiPackageConstructor;

    /**
     * 实时监控服务类
     */
    @Autowired
    private IMonitorRealtimeMonitoringService monitorRealtimeMonitoringService;

    /**
     * HTTP信息历史记录数据访问对象
     */
    @Autowired
    private IMonitorHttpHistoryDao monitorHttpHistoryDao;

    /**
     * <p>
     * 获取HTTP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param status         状态
     * @param monitorEnv     监控环境
     * @param monitorGroup   监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/4/11 10:51
     */
    @Override
    public Page<MonitorHttpVo> getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget,
                                                  String method, Integer status, String monitorEnv, String monitorGroup) {
        // 查询数据库
        IPage<MonitorHttp> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(hostnameSource)) {
            lambdaQueryWrapper.like(MonitorHttp::getHostnameSource, hostnameSource);
        }
        if (StringUtils.isNotBlank(urlTarget)) {
            lambdaQueryWrapper.like(MonitorHttp::getUrlTarget, urlTarget);
        }
        if (StringUtils.isNotBlank(method)) {
            lambdaQueryWrapper.eq(MonitorHttp::getMethod, method);
        }
        if (null != status) {
            // -1 用来表示状态未知
            if (-1 == status) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorHttp::getStatus));
            }
            // -2 表示其它
            else if (-2 == status) {
                lambdaQueryWrapper.and(wrapper -> wrapper
                        .ne(MonitorHttp::getStatus, 200)
                        .ne(MonitorHttp::getStatus, 500)
                        .ne(MonitorHttp::getStatus, 404)
                        .ne(MonitorHttp::getStatus, 403)
                        .ne(MonitorHttp::getStatus, 400)
                        .isNotNull(MonitorHttp::getStatus));
            } else {
                lambdaQueryWrapper.eq(MonitorHttp::getStatus, status);
            }
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            lambdaQueryWrapper.eq(MonitorHttp::getMonitorEnv, monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            lambdaQueryWrapper.eq(MonitorHttp::getMonitorGroup, monitorGroup);
        }
        IPage<MonitorHttp> monitorHttpPage = this.baseMapper.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorHttp> monitorHttps = monitorHttpPage.getRecords();
        // 转换成HTTP信息表现层对象
        List<MonitorHttpVo> monitorHttpVos = Lists.newLinkedList();
        for (MonitorHttp monitorHttp : monitorHttps) {
            MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
            monitorHttpVos.add(monitorHttpVo);
        }
        // 设置返回对象
        Page<MonitorHttpVo> monitorHttpVoPage = new Page<>();
        monitorHttpVoPage.setRecords(monitorHttpVos);
        monitorHttpVoPage.setTotal(monitorHttpPage.getTotal());
        return monitorHttpVoPage;
    }

    /**
     * <p>
     * 删除HTTP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo deleteMonitorHttp(List<Long> ids) {
        // 删除HTTP历史记录表
        LambdaUpdateWrapper<MonitorHttpHistory> monitorHttpHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorHttpHistoryLambdaUpdateWrapper.in(MonitorHttpHistory::getHttpId, ids);
        this.monitorHttpHistoryDao.delete(monitorHttpHistoryLambdaUpdateWrapper);
        // 删除HTTP信息表
        LambdaUpdateWrapper<MonitorHttp> monitorHttpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorHttpLambdaUpdateWrapper.in(MonitorHttp::getId, ids);
        this.baseMapper.delete(monitorHttpLambdaUpdateWrapper);
        // 注意：删除实时监控信息，这个不要忘记了
        this.monitorRealtimeMonitoringService.delete(MonitorTypeEnums.HTTP4SERVICE, null, ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 添加HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    @Override
    public LayUiAdminResultVo addMonitorHttp(MonitorHttpVo monitorHttpVo) {
        // 根据目标url，查询数据库中是否已经存在此目标url的记录
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorHttp::getUrlTarget, monitorHttpVo.getUrlTarget());
        MonitorHttp dbMonitorHttp = this.baseMapper.selectOne(lambdaQueryWrapper);
        if (dbMonitorHttp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorHttp monitorHttp = monitorHttpVo.convertTo();
        monitorHttp.setInsertTime(new Date());
        monitorHttp.setOfflineCount(0);
        if (StringUtils.isBlank(monitorHttpVo.getMonitorEnv())) {
            monitorHttp.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorHttpVo.getMonitorGroup())) {
            monitorHttp.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorHttpVo.getIsEnableMonitor())) {
            monitorHttp.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorHttpVo.getIsEnableAlarm())) {
            monitorHttp.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        int result = this.baseMapper.insert(monitorHttp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    @Override
    public LayUiAdminResultVo editMonitorHttp(MonitorHttpVo monitorHttpVo) {
        // 根据目标目标url，查询数据库中是否已经存在此目标url的记录
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorHttp::getId, monitorHttpVo.getId());
        lambdaQueryWrapper.eq(MonitorHttp::getUrlTarget, monitorHttpVo.getUrlTarget());
        MonitorHttp dbMonitorHttp = this.baseMapper.selectOne(lambdaQueryWrapper);
        if (dbMonitorHttp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorHttp monitorHttp = monitorHttpVo.convertTo();
        monitorHttp.setUpdateTime(new Date());
        if (StringUtils.isBlank(monitorHttpVo.getMonitorEnv())) {
            monitorHttp.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorHttpVo.getMonitorGroup())) {
            monitorHttp.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorHttpVo.getIsEnableMonitor())) {
            monitorHttp.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorHttpVo.getIsEnableAlarm())) {
            monitorHttp.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        int result = this.baseMapper.updateById(monitorHttp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Override
    public LayUiAdminResultVo setIsEnableMonitor(Long id, String isEnableMonitor) {
        LambdaUpdateWrapper<MonitorHttp> httpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        httpLambdaUpdateWrapper.eq(MonitorHttp::getId, id);
        // 设置更新字段
        httpLambdaUpdateWrapper.set(MonitorHttp::getIsEnableMonitor, isEnableMonitor);
        // 如果不监控，状态设置为未知
        if (StringUtils.equals(isEnableMonitor, ZeroOrOneConstants.ZERO)) {
            httpLambdaUpdateWrapper.set(MonitorHttp::getStatus, null);
        }
        this.baseMapper.update(null, httpLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Override
    public LayUiAdminResultVo setIsEnableAlarm(Long id, String isEnableAlarm) {
        LambdaUpdateWrapper<MonitorHttp> httpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        httpLambdaUpdateWrapper.eq(MonitorHttp::getId, id);
        // 设置更新字段
        httpLambdaUpdateWrapper.set(MonitorHttp::getIsEnableAlarm, isEnableAlarm);
        this.baseMapper.update(null, httpLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取home页的HTTP信息
     * </p>
     *
     * @return home页的HTTP信息表现层对象
     * @author 皮锋
     * @custom.date 2022/4/23 13:23
     */
    @Override
    public HomeHttpVo getHomeHttpInfo() {
        // HTTP正常率统计
        Map<String, Object> map = this.baseMapper.getHttpNormalRateStatistics();
        return HomeHttpVo.builder()
                .httpSum(NumberUtil.parseInt(map.get("httpSum").toString()))
                .httpConnectSum(NumberUtil.parseInt(map.get("httpConnectSum").toString()))
                .httpDisconnectSum(NumberUtil.parseInt(map.get("httpDisconnectSum").toString()))
                .httpUnsentSum(NumberUtil.parseInt(map.get("httpUnsentSum").toString()))
                .httpConnectRate(NumberUtil.round(map.get("httpConnectRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：HTTP连通性
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 22:23
     */
    @Override
    public LayUiAdminResultVo testMonitorHttp(MonitorHttpVo monitorHttpVo) throws IOException {
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("method", monitorHttpVo.getMethod());
        extraMsg.put("urlTarget", monitorHttpVo.getUrlTarget());
        // 媒体类型
        String contentType = monitorHttpVo.getContentType();
        extraMsg.put("contentType", contentType);
        // 请求头参数（LayUiTable数据中，过滤出选中的数据）
        String headerParameter = LayUiUtils.filterCheckedWithLayUiTable(monitorHttpVo.getHeaderParameter());
        extraMsg.put("headerParameter", headerParameter);
        String bodyParameter = monitorHttpVo.getBodyParameter();
        if (StringUtils.isNotBlank(bodyParameter)) {
            JSONObject bodyParameterJson = JSON.parseObject(bodyParameter);
            if (bodyParameterJson != null) {
                if (StringUtils.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE, contentType)) {
                    JSONArray bodyApplicationFormUrlencodedParameterJsons = bodyParameterJson.getJSONArray("bodyApplicationFormUrlencodedParameter");
                    // LayUiTable数据中，过滤出选中的数据
                    bodyParameter = LayUiUtils.filterCheckedWithLayUiTable(bodyApplicationFormUrlencodedParameterJsons.toJSONString());
                } else if (StringUtils.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE, contentType)) {
                    bodyParameter = String.valueOf(bodyParameterJson.get("bodyApplicationJsonParameter"));
                }
            }
        }
        extraMsg.put("bodyParameter", bodyParameter);
        BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.TEST_MONITOR_HTTP_URL, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        // 200：成功
        String code = "200";
        if (StringUtils.equals(msg, code)) {
            msg = WebResponseConstants.SUCCESS;
        }
        return LayUiAdminResultVo.ok(msg);
    }

    /**
     * <p>
     * 根据主键ID获取HTTP信息
     * </p>
     *
     * @param id 主键ID
     * @return HTTP信息表现层对象
     * @author 皮锋
     * @custom.date 2024/09/27 21:34
     */
    @Override
    public MonitorHttpVo getMonitorHttpVoById(Long id) {
        MonitorHttp monitorHttp = this.baseMapper.selectById(id);
        MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
        // 请求头参数
        String headerParameter = monitorHttp.getHeaderParameter();
        headerParameter = LayUiUtils.jsonArrayStr2ArrayStrWithLayUiTable(headerParameter, true);
        headerParameter = StringUtils.length(headerParameter) > 2 ? JSONUtil.formatJsonStr(headerParameter) : headerParameter;
        // 媒体类型
        String contentType = monitorHttp.getContentType();
        // 请求体参数
        String bodyParameter = monitorHttp.getBodyParameter();
        if (StringUtils.isNotBlank(bodyParameter)) {
            JSONObject bodyParameterJson = JSON.parseObject(bodyParameter);
            if (StringUtils.equalsAnyIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE, contentType)) {
                JSONArray bodyApplicationFormUrlencodedParameterJsons = bodyParameterJson.getJSONArray("bodyApplicationFormUrlencodedParameter");
                String bodyApplicationFormUrlencodedParameterJsonStr = bodyApplicationFormUrlencodedParameterJsons.toJSONString();
                bodyParameter = JSONUtil.formatJsonStr(LayUiUtils.jsonArrayStr2ArrayStrWithLayUiTable(bodyApplicationFormUrlencodedParameterJsonStr, true));
            } else if (StringUtils.equalsAnyIgnoreCase(MediaType.APPLICATION_JSON_VALUE, contentType)) {
                bodyParameter = String.valueOf(bodyParameterJson.get("bodyApplicationJsonParameter"));
            }
        }
        // 设置请求参数
        monitorHttpVo.setHeaderParameter(headerParameter);
        monitorHttpVo.setBodyParameter(bodyParameter);
        return monitorHttpVo;
    }

}
