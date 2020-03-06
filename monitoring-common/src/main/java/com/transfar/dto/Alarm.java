package com.transfar.dto;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import com.transfar.common.InstanceBean;
import com.transfar.util.StrUtils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控告警信息传输层对象
 * </p>
 * 用来定义监控告警信息的数据格式
 *
 * @author 皮锋
 * @custom.date 2020/3/3 10:01
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Alarm extends InstanceBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8198453286095801334L;

	/**
	 * ID
	 */
	private String id = StrUtils.getUUID();

	/**
	 * 告警时间
	 */
	private Date alarmTime;

	/**
	 * 告警内容
	 */
	private String msg;

	/**
	 * 告警级别
	 */
	private String level;

	/**
	 * 方法
	 */
	private Method method;

}
