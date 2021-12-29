package com.ruike.wms.infra.util;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:01
 */
@Slf4j
public class FileUtils {

    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String path) {
        OutputStream out = null;
        try {
            // 弹出下载框，并处理中文
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(fileName, "utf-8"));
            // 下载
            out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path);
            byte[] bytes = new byte[4096];
            int size = is.read(bytes);
            while (size > 0) {
                out.write(bytes, 0, size);
                size = is.read(bytes);
            }
            out.flush();
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadWorkbook(HSSFWorkbook wk, HttpServletRequest request, HttpServletResponse response, String fileName) {
        String path = request.getSession().getServletContext().getRealPath("") + "/" + fileName;
        FileOutputStream name = null;
        try {
            name = new FileOutputStream(path);
            wk.write(name);
            wk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadFile(request, response, fileName, path);
    }

    public static void downloadWorkbook(XSSFWorkbook wk, HttpServletRequest request, HttpServletResponse response, String fileName) {
        String path = request.getSession().getServletContext().getRealPath("") + "/" + fileName;
        FileOutputStream name = null;
        try {
            name = new FileOutputStream(path);
            wk.write(name);
            wk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadFile(request, response, fileName, path);
    }

    public static Workbook downloadExcel(String filePath){
        HttpURLConnection conn = null;
        InputStream is = null;
        Workbook workbook = null;
        try {
            URL url=new URL(filePath);
            //2 构建连接对象
            conn=(HttpURLConnection)url.openConnection();//一定是这种类型
            conn.setReadTimeout(3000);//设置客户端连接超时间隔，如果超过指定时间  服务端还没有响应 就不要等了
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 设置字符编码
            conn.setRequestProperty("Charset", "UTF-8");
            log.info("文件路径：" + filePath + " conn.getResponseCode()：" + conn.getResponseMessage());
            //判断服务端正常的反馈是否已经到达了 客户端
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //获得网络字节输入流对象
                is = conn.getInputStream();// 操作文件

                workbook = WorkbookFactory.create(is);

                // 关闭流
                is.close();

                log.info("文件路径：" + filePath + "下载成功");
            }
        } catch (MalformedURLException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new CommonException("文件" + filePath + "下载失败！请检查文件服务器连接【MalformedURLException】");
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new CommonException("文件" + filePath + "下载失败！请检查文件服务器连接【IOException】");
        }finally{
            if(is != null){
                // 关闭流
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                    throw new CommonException("文件" + filePath + "关闭流失败！请检查文件服务器连接【IOException】");
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        return workbook;
    }
}
