package com.transfar.domain;

import com.transfar.common.SuperBean;
import com.transfar.constant.AlarmLevelEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Alarm extends SuperBean {

	/**
	 * 告警内容
	 */
	private String msg;

	/**
	 * 告警级别
	 */
	private AlarmLevelEnum level;

}
