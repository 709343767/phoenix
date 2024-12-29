package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.plug.core.EnumPoolingHttpClient;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorHttpDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * HTTP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:05
 */
@Service
public class HttpServiceImpl extends ServiceImpl<IMonitorHttpDao, MonitorHttp> implements IHttpService {

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param method          请求方法
     * @param urlTarget       目标URL
     * @param contentType     媒体类型
     * @param headerParameter 请求头参数
     * @param bodyParameter   请求头参数
     * @return HTTP状态码
     * @author 皮锋
     * @custom.date 2022/10/10 20:09
     */
    @Override
    public String testMonitorHttp(String method, String urlTarget, String contentType, String headerParameter, String bodyParameter) {
        Map<String, Object> map;
        // HTTP线程池工具类
        EnumPoolingHttpClient httpClient = EnumPoolingHttpClient.getInstance();
        // GET请求
        if (StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), method)) {
            map = httpClient.sendHttpGet(urlTarget, headerParameter);
        }
        // POST请求
        else if (StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), method)) {
            map = httpClient.sendHttpPost(urlTarget, contentType, headerParameter, bodyParameter);
        } else {
            return ResultMsgConstants.FAILURE;
        }
        // 状态码
        int statusCode = Integer.parseInt(String.valueOf(map.get("statusCode")));
        // 响应时间
        long avgTime = Long.parseLong(String.valueOf(map.get("avgTime")));
        // 从数据库获取HTTP信息
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorHttp::getHostnameSource, NetUtils.getLocalIp());
        lambdaQueryWrapper.eq(MonitorHttp::getUrlTarget, urlTarget);
        MonitorHttp monitorHttp = this.getOne(lambdaQueryWrapper);
        // 如果数据库中有此信息，则更新
        if (monitorHttp != null) {
            monitorHttp.setStatus(statusCode);
            monitorHttp.setAvgTime(avgTime);
            monitorHttp.setUpdateTime(new Date());
            if (statusCode != HttpStatus.SC_OK) {
                // 异常信息
                String excMessage = map.get("excMessage") != null ? String.valueOf(map.get("excMessage")) : null;
                monitorHttp.setExcMessage(excMessage);
            }
            // 更新数据库
            this.updateById(monitorHttp);
        }
        // 状态码
        return String.valueOf(statusCode);
    }

}
