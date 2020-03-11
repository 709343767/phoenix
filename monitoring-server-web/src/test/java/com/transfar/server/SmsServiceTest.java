package com.transfar.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 测试短信服务
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 上午11:18:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SmsServiceTest {

    /*
     * 短信服务接口
     */
    // @Autowired
    //private ISmsService smsService;

    /**
     * <p>
     * 测试调用创发的短信接口发送短信
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020年3月10日 下午12:11:05
     */
    @Test
    public void testSendSmsByTransfarApi() {
        //TransfarSms sms = TransfarSms.builder().identity("监控项目开发者").type("测试").content("测试短信接口！").phone("18807473672")
        //		.build();
        //String result = this.smsService.sendSmsByTransfarApi(sms);
        //log.info("短信发送结果：{}", result);
    }

}
