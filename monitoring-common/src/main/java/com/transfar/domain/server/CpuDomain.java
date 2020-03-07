package com.transfar.domain.server;

import java.util.List;

import com.transfar.common.SuperBean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * CPU信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午2:28:02
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class CpuDomain extends SuperBean {

	/**
	 * cpu总数
	 */
	private int cpuNum;
	/**
	 * cpu信息
	 */
	private List<CpuInfoDomain> cpuList;

	@Data
	@NoArgsConstructor
	@Accessors(chain = true)
	@EqualsAndHashCode(callSuper = true)
	public static class CpuInfoDomain extends SuperBean {

		/**
		 * CPU频率（MHz）
		 */
		String cpuMhz;
		/**
		 * CPU剩余率
		 */
		String cpuIdle;
		/**
		 * CPU使用率
		 */
		String cpuCombined;

		@Override
		public String toString() {
			return "CpuInfoVo [CPU频率（MHz）=" + cpuMhz + ", CPU剩余率=" + cpuIdle + ", CPU使用率=" + cpuCombined + "]";
		}

	}

}
