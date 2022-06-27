package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbInfo4RedisService;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.core.PackageConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 * Redis数据库信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:46
 */
@Service
public class DbInfo4RedisServiceImpl implements IDbInfo4RedisService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * <p>
     * 获取Redis信息
     * </p>
     *
     * @param id 数据库ID
     * @return Redis信息
     * @throws IOException    IO异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2021/10/16 20:48
     */
    @Override
    public String getRedisInfo(Long id) throws SigarException, IOException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        String[] address = StringUtils.split(url, ":");
        // 主机
        String host = address[0];
        // 端口
        int port = Integer.parseInt(address[1]);
        // 密码
        String password = monitorDb.getPassword();
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("host", host);
        extraMsg.put("port", port);
        extraMsg.put("password", password);
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.REDIS_GET_REDIS_INFO, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        return result.getMsg();
    }
}
