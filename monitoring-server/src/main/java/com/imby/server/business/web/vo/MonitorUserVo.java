package com.imby.server.business.web.vo;

import com.google.common.base.Converter;
import com.imby.server.business.web.entity.MonitorUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 监控用户表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/8 8:45
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监控用户表现层对象")
public class MonitorUserVo {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * <p>
     * MonitorUserVo转MonitorUser
     * </p>
     *
     * @return {@link MonitorUser}
     * @author 皮锋
     * @custom.date 2020/7/8 9:20
     */
    public MonitorUser convertToMonitorUser() {
        MonitorUserVoConvert monitorUserVoConvert = new MonitorUserVoConvert();
        return monitorUserVoConvert.convert(this);
    }

    /**
     * <p>
     * MonitorUser转MonitorUserVo
     * </p>
     *
     * @param monitorUser {@link MonitorUser}
     * @return {@link MonitorUserVo}
     * @author 皮锋
     * @custom.date 2020/7/8 9:22
     */
    public MonitorUserVo convertFor(MonitorUser monitorUser) {
        MonitorUserVoConvert monitorUserVoConvert = new MonitorUserVoConvert();
        return monitorUserVoConvert.reverse().convert(monitorUser);
    }


    /**
     * <p>
     * MonitorUserVo转换器
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年1月20日 下午4:35:20
     */
    private static class MonitorUserVoConvert extends Converter<MonitorUserVo, MonitorUser> {

        @Override
        protected MonitorUser doForward(MonitorUserVo monitorUserVo) {
            MonitorUser monitorUser = MonitorUser.builder().build();
            BeanUtils.copyProperties(monitorUserVo, monitorUser);
            return monitorUser;
        }

        @Override
        protected MonitorUserVo doBackward(MonitorUser monitorUser) {
            MonitorUserVo monitorUserVo = MonitorUserVo.builder().build();
            BeanUtils.copyProperties(monitorUser, monitorUserVo);
            return monitorUserVo;
        }
    }

}
