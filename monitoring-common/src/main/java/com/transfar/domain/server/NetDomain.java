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
 * 网卡信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午3:28:03
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class NetDomain extends SuperBean {

	/**
	 * 网卡总数
	 */
	private int netNum;
	/**
	 * 网卡信息
	 */
	private List<NetInterfaceConfigDomain> netList;

	@Data
	@NoArgsConstructor
	@Accessors(chain = true)
	@EqualsAndHashCode(callSuper = true)
	public static class NetInterfaceConfigDomain extends SuperBean {
		/**
		 * 网卡名字
		 */
		String name;
		/**
		 * 网卡类型
		 */
		String type;
		/**
		 * 网卡地址
		 */
		String address;
		/**
		 * 子网掩码
		 */
		String mask;
		/**
		 * 广播地址
		 */
		String broadcast;

		@Override
		public String toString() {
			return "NetInterfaceConfigVo [网卡名字=" + name + ", 网卡类型=" + type + ", 网卡地址=" + address + ", 子网掩码=" + mask
					+ ", 广播地址=" + broadcast + "]";
		}

	}

}
