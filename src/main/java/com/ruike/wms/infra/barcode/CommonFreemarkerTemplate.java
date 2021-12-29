package com.ruike.wms.infra.barcode;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * @Classname FreemarkerTemplate
 * @Description 模板解析
 * @Date 2019/11/21 18:54
 * @Author admin
 */
public class CommonFreemarkerTemplate {

    private final Configuration configuration = new Configuration(
            Configuration.VERSION_2_3_23);
    private String charset;

    /**
     * @Description 指定编码
      * @param charset
     * @return
     * @Date 2019/12/31 10:17
     * @Author weihua.liao
     */
    public CommonFreemarkerTemplate(String charset) {
        this.charset = charset;
        configuration.setEncoding(Locale.CHINA, charset);
        //处理空值为空字符串
        configuration.setClassicCompatible(true);
    }

    /**
     * @Description 设定模板路径
      * @param resourceLoaderClass
     * @param basePackagePath
     * @return void
     * @Date 2019/12/31 10:17
     * @Author weihua.liao
     */
    public void setTemplateClassPath(Class resourceLoaderClass,
                                     String basePackagePath) {
        configuration.setClassForTemplateLoading(resourceLoaderClass,
                basePackagePath);
    }

    /**
     * @Description 指定实际目录
      * @param templatePath
     * @return void
     * @Date 2019/12/31 10:18
     * @Author weihua.liao
     */
    public void setTemplateDirectoryPath(String templatePath)
            throws IOException {
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
    }

    /**
     * @Description 处理流
      * @param templateFileName
     * @param dataMap
     * @param writer
     * @return void
     * @Date 2019/12/31 10:18
     * @Author weihua.liao
     */
    public void processToStream(String templateFileName,
                                Map<String, Object> dataMap, Writer writer) throws Throwable {
        Template template = configuration.getTemplate(templateFileName);
        template.process(dataMap, writer);
    }

    /**
     * @Description 处理文件
      * @param templateFileName
     * @param dataMap
     * @param outFile
     * @return void
     * @Date 2019/12/31 10:18
     * @Author weihua.liao
     */
    public void processToFile(String templateFileName,
                              Map<String, Object> dataMap, File outFile) throws Throwable {
        Writer writer = new OutputStreamWriter(new FileOutputStream(outFile),
                charset);
        try {
            processToStream(templateFileName, dataMap, writer);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * @Description 处理字符串
      * @param templateFileName
     * @param dataMap
     * @return java.lang.String
     * @Date 2019/12/31 10:19
     * @Author weihua.liao
     */
    public String processToString(String templateFileName,
                                  Map<String, Object> dataMap) throws Throwable {
        Writer writer = new StringWriter(2048);
        try {
            processToStream(templateFileName, dataMap, writer);
            return writer.toString();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
