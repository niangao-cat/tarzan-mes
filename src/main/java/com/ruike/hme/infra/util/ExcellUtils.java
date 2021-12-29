package com.ruike.hme.infra.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/6 17:31
 */
public class ExcellUtils {

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map<String, CellStyle> createStyles(HSSFWorkbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        //居中
        CellStyle center = wb.createCellStyle();
        center.setAlignment(HorizontalAlignment.CENTER);
        center.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("center", center);

        // 在制标题样式
        CellStyle subTitle = wb.createCellStyle();
        subTitle.setAlignment(HorizontalAlignment.CENTER);
        subTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        subTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        subTitle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styles.put("subTitle", subTitle);
        // 在制运行样式
        CellStyle runCell = wb.createCellStyle();
        runCell.setAlignment(HorizontalAlignment.CENTER);
        runCell.setVerticalAlignment(VerticalAlignment.CENTER);
        runCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        runCell.setFillForegroundColor(IndexedColors.TAN.getIndex());
        styles.put("runCell", runCell);
        // 在制库存样式
        CellStyle finishCell = wb.createCellStyle();
        finishCell.setAlignment(HorizontalAlignment.CENTER);
        finishCell.setVerticalAlignment(VerticalAlignment.CENTER);
        finishCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        finishCell.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        styles.put("finishCell", finishCell);
        return styles;
    }
}
