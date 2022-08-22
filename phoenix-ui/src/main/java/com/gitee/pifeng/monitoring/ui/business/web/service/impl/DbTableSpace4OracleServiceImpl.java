package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbTableSpace4OracleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbTableSpaceAll4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbTableSpaceFile4OracleVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.core.PackageConstructor;
import com.google.common.collect.Lists;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * Oracle数据库表空间服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:11
 */
@Service
public class DbTableSpace4OracleServiceImpl implements IDbTableSpace4OracleService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * <p>
     * 获取表空间列表(按文件)
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    @Override
    public Page<DbTableSpaceFile4OracleVo> getTableSpaceListFile(Long current, Long size, Long id) throws IOException, NetException, SigarException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        //用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = monitorDb.getPassword();
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("url", url);
        extraMsg.put("username", username);
        extraMsg.put("password", password);
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.ORACLE_GET_TABLESPACE_LIST_FILE, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        List<Entity> entityList = JSON.parseArray(msg, Entity.class);
        // 解析数据
        List<DbTableSpaceFile4OracleVo> dbTableSpace4OracleVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            Long fileId = entity.getLong("FILEID");
            String fileName = entity.getStr("FILENAME", StandardCharsets.UTF_8);
            String tablespaceName = entity.getStr("TABLESPACENAME", StandardCharsets.UTF_8);
            Long totalTemp = entity.getLong("TOTAL");
            String total = totalTemp == null ? null : DataSizeUtil.format(totalTemp);
            Long usedTemp = entity.getLong("USED");
            String used = usedTemp == null ? null : DataSizeUtil.format(usedTemp);
            Long freeTemp = entity.getLong("FREE");
            String free = freeTemp == null ? null : DataSizeUtil.format(freeTemp);
            Double freeperTemp = entity.getDouble("FREEPER");
            Double freePer = freeperTemp == null ? null : NumberUtil.round(freeperTemp, 4).doubleValue();
            Double usedPer = freePer == null ? null : NumberUtil.round(100D - freePer, 4).doubleValue();
            DbTableSpaceFile4OracleVo dbTableSpace4OracleVo = DbTableSpaceFile4OracleVo.builder()
                    .fileId(fileId)
                    .fileName(fileName)
                    .tablespaceName(tablespaceName)
                    .total(total)
                    .used(used)
                    .free(free)
                    .usedPer(usedPer)
                    .freePer(freePer)
                    .build();
            dbTableSpace4OracleVos.add(dbTableSpace4OracleVo);
        }
        // 按文件ID排序
        dbTableSpace4OracleVos.sort(Comparator.comparing(DbTableSpaceFile4OracleVo::getFileId));
        // 设置返回对象
        Page<DbTableSpaceFile4OracleVo> dbTableSpace4OracleVoPage = new Page<>();
        dbTableSpace4OracleVoPage.setRecords(dbTableSpace4OracleVos);
        dbTableSpace4OracleVoPage.setTotal(dbTableSpace4OracleVos.size());
        dbTableSpace4OracleVoPage.setCurrent(current);
        dbTableSpace4OracleVoPage.setSize(size);
        return dbTableSpace4OracleVoPage;
    }

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    @Override
    public Page<DbTableSpaceAll4OracleVo> getTableSpaceListAll(Long current, Long size, Long id) throws NetException, SigarException, IOException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        //用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = monitorDb.getPassword();
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("url", url);
        extraMsg.put("username", username);
        extraMsg.put("password", password);
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.ORACLE_GET_TABLESPACE_LIST_ALL, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        List<Entity> entityList = JSON.parseArray(msg, Entity.class);
        // 解析数据
        List<DbTableSpaceAll4OracleVo> dbTableSpace4OracleVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            String tablespaceName = entity.getStr("TABLESPACENAME", StandardCharsets.UTF_8);
            String total = DataSizeUtil.format(entity.getLong("TOTAL"));
            String used = DataSizeUtil.format(entity.getLong("USED"));
            String free = DataSizeUtil.format(entity.getLong("FREE"));
            double freeRate = NumberUtil.round(entity.getDouble("FREERATE"), 4).doubleValue();
            double usedRate = NumberUtil.round(entity.getDouble("USEDRATE"), 4).doubleValue();
            DbTableSpaceAll4OracleVo dbTableSpace4OracleVo = DbTableSpaceAll4OracleVo.builder()
                    .tablespaceName(tablespaceName)
                    .total(total)
                    .used(used)
                    .free(free)
                    .freeRate(freeRate)
                    .usedRate(usedRate)
                    .build();
            dbTableSpace4OracleVos.add(dbTableSpace4OracleVo);
        }
        // 按使用率排序
        dbTableSpace4OracleVos.sort(Comparator.comparing(DbTableSpaceAll4OracleVo::getUsedRate).reversed());
        // 设置返回对象
        Page<DbTableSpaceAll4OracleVo> dbTableSpace4OracleVoPage = new Page<>();
        dbTableSpace4OracleVoPage.setRecords(dbTableSpace4OracleVos);
        dbTableSpace4OracleVoPage.setTotal(dbTableSpace4OracleVos.size());
        dbTableSpace4OracleVoPage.setCurrent(current);
        dbTableSpace4OracleVoPage.setSize(size);
        return dbTableSpace4OracleVoPage;
    }

}
