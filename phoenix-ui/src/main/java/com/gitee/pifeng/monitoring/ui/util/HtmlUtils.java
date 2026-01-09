package com.gitee.pifeng.monitoring.ui.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * <p>
 * HTML工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2026/1/9 14:00
 */
public class HtmlUtils {

    /**
     * 用于临时替换合法 {@code <br>} 标签的内部占位符，该字符串必须确保在正常输入中几乎不可能出现，以避免与真实内容冲突，
     * 在 HTML 转义完成后，此占位符会被还原为 {@code <br>}。
     */
    private static final String BR_PLACEHOLDER = "%%BR_PLACEHOLDER%%";

    /**
     * 匹配 {@code <br>、<br/>、<br />} 等（不区分大小写，允许空格）。支持的格式包括但不限于：
     * {@code <br>}, {@code <br/>}, {@code <br />}, {@code <BR>}, {@code < bR  />} 等。
     * 使用 {@link Pattern#CASE_INSENSITIVE} 标志实现大小写不敏感，并允许标签内存在任意数量的空白字符（如空格、制表符）。
     */
    private static final Pattern BR_PATTERN = Pattern.compile("<\\s*br\\s*/?>", Pattern.CASE_INSENSITIVE);

    /**
     * <p>
     * 对输入字符串进行 HTML 转义，以防止 XSS 攻击，但保留换行语义，允许 {@code <br>、<br/>、<br />} 等形式的换行标签。
     * </p>
     *
     * @param input 待处理的原始字符串，可能包含 HTML 特殊字符或 {@code <br>} 换行标签
     * @return 转义后的安全字符串，其中除 {@code <br>} 外的所有 HTML 标签均被转义为纯文本；若输入为 {@code null} 或空字符串，则返回空字符串 {@code ""}
     * @author 皮锋
     * @custom.date 2026/1/9 14:06
     */
    public static String escapeHtmlButAllowBr(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        // 1. 将所有合法的 <br> 变体 替换为占位符
        String temp = BR_PATTERN.matcher(input).replaceAll(BR_PLACEHOLDER);
        // 2. 对整个字符串进行 HTML 转义
        String escaped = org.springframework.web.util.HtmlUtils.htmlEscape(temp);
        // 3. 将占位符还原为 <br>
        return escaped.replace(BR_PLACEHOLDER, "<br>");
    }

}