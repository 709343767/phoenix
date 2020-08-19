package com.imby.agent.business.server.service.impl;

import com.imby.agent.constant.Urlconstants;
import com.imby.agent.business.server.service.IHttpService;
import com.imby.agent.business.server.service.IJvmService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.JvmPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的Java虚拟机信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:18
 */
@Service
public class JvmServiceImpl implements IJvmService {

    /**
     * 跟服务端相关的HTTP服务接口
     */
    @Autowired
    private IHttpService httpService;

    /**
     * <p>
     * 给服务端发Java虚拟机信息包
     * </p>
     *
     * @param jvmPackage Java虚拟机信息包
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    @Override
    public BaseResponsePackage sendServerPackage(JvmPackage jvmPackage) throws Exception {
        return this.httpService.sendHttpPost(jvmPackage.toJsonString(), Urlconstants.JVM_URL);
    }
}
