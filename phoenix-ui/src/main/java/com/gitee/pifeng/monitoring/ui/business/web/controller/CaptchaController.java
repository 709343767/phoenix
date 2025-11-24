package com.gitee.pifeng.monitoring.ui.business.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import com.gitee.pifeng.monitoring.ui.constant.CaptchaConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * 图形验证码
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/21 13:54
 */
@Slf4j
@Controller
@Tag(name = "图形验证码")
public class CaptchaController {

    /**
     * <p>
     * 获取图形验证码
     * </p>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2023/3/21 14:14
     */
    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha.png")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置内容类型
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        // 禁用缓存
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 38, 4, 2);
        // 自定义验证码内容为四则运算方式
        captcha.setGenerator(new MathGenerator(1, false));
        // 生成code
        captcha.createCode();
        // 获取session对象
        HttpSession session = request.getSession();
        // 替换 captcha，所以先清理旧的
        session.removeAttribute(CaptchaConstants.CAPTCHA);
        session.removeAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME);
        // 将验证码对象设置到session
        session.setAttribute(CaptchaConstants.CAPTCHA, captcha);
        // 设置图形验证码过期时间为1分钟
        session.setAttribute(CaptchaConstants.CAPTCHA_EXPIRE_TIME, LocalDateTime.now().plusMinutes(1));
        // 获取响应输出流
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 将图片验证码写到响应输出流
            captcha.write(outputStream);
            // 推送并关闭响应输出流
            outputStream.flush();
        }
    }

}
