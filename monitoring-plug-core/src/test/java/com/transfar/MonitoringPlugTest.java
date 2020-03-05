package com.transfar;

import org.junit.Test;

import com.transfar.core.MonitoringPlug;

/**
 * <p>
 * 测试监控客户端
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:43:38
 */
public class MonitoringPlugTest {

	@Test
	public void testStart() {
		MonitoringPlug.start();
	}
	
	public static void main(String[] args) {
		MonitoringPlug.start();
	}

}
