package com.ruike.wms.infra.barcode;

import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * PDF生成
 *
 * @author admin
 * @date 2019/11/21 18:56
 */
public class CommonPDFUtils {

    private static String fontBasePath = CommonPDFUtils.class.getResource("/").getPath() + "templates/";

    /**
     * HTML转换PDF
     *
     * @param htmlFile html页面文件
     * @param output 输出流
     * @param imageBaseUrl 图片url
     * @date 2019/12/31 10:19
     * @author weihua.liao
     */
    public static void htmlFileToPdfStream(File htmlFile, OutputStream output,
                                           File imageBaseUrl) throws Throwable {
        if (htmlFile == null) {
            throw new RuntimeException("htmlFile is null");
        }
        if (output == null) {
            throw new RuntimeException("output is null");
        }

        // 生成ITextRenderer实例
        ITextRenderer renderer = new ITextRenderer();
        String fileUrl = htmlFile.getAbsolutePath();
        String imageUrl = "D://shanpu//images//";
        // 关联html页面
        renderer.setDocument(fileUrl);
        // 设置获取图片的基本路径
        renderer.getSharedContext().setBaseURL(imageUrl);
        // 设置字体路径，必须和html设置一致
        ITextFontResolver fontResolver = renderer.getFontResolver();


        fontResolver.addFont(fontBasePath + "msyh.ttc", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        fontResolver.addFont(fontBasePath + "msyhl.ttc", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        fontResolver.addFont(fontBasePath + "msyhbd.ttc", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(output);
    }

    /***
     *  静态页面转文件
     * @param htmlFile html页面文件
     * @param pdfFile pdf文件
     * @param imageBaseUrl 图片url
     * @date 2019/12/31 10:20
     * @author weihua.liao
     */
    public static void htmlFileToPdfFile(File htmlFile, File pdfFile,
                                         File imageBaseUrl) throws Throwable {
        try (OutputStream output = new FileOutputStream(pdfFile)) {
            htmlFileToPdfStream(htmlFile, output, imageBaseUrl);
        }
    }

}
