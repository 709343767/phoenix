package com.gitee.pifeng.monitoring.ui.core;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import org.apache.poi.ss.usermodel.*;

/**
 * <p>
 * 自定义POI Excel导出样式
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/5/20 20:49
 */
public class CustomExcelExportStyler extends ExcelExportStylerDefaultImpl {

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param workbook {@link Workbook}
     * @author 皮锋
     * @custom.date 2021/5/20 20:50
     */
    public CustomExcelExportStyler(Workbook workbook) {
        super(workbook);
    }

    /**
     * <p>
     * 设置Title样式
     * </p>
     *
     * @param color 颜色
     * @return {@link CellStyle}
     * @author 皮锋
     * @custom.date 2021/5/20 20:51
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setWrapText(true);
        // 设置背景颜色（淡绿色）
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        // 设置边框颜色
        titleStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        Font font = workbook.createFont();
        //字体（粗体）
        font.setBold(true);
        titleStyle.setFont(font);
        return titleStyle;
    }

    /**
     * <p>
     * 设置Header样式
     * </p>
     *
     * @param color 颜色
     * @return {@link CellStyle}
     * @author 皮锋
     * @custom.date 2021/5/20 20:52
     */
    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        //字体（粗体）
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置背景颜色（淡绿色）
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        // 设置边框颜色
        headerStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return headerStyle;
    }

    /**
     * <p>
     * 设置间隔行样式
     * </p>
     *
     * @param workbook {@link Workbook}
     * @param isWarp   是否换行
     * @return {@link CellStyle}
     * @author 皮锋
     * @custom.date 2021/5/20 20:55
     */
    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        style.setWrapText(true);
        return style;
    }

    /**
     * <p>
     * 设置单行样式
     * </p>
     *
     * @param workbook {@link Workbook}
     * @param isWarp   是否换行
     * @return {@link CellStyle}
     * @author 皮锋
     * @custom.date 2021/5/20 20:56
     */
    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        style.setWrapText(true);
        return style;
    }

}
