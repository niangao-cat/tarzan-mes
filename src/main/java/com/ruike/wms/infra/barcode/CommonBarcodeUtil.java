package com.ruike.wms.infra.barcode;


import lombok.extern.slf4j.Slf4j;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import sun.misc.BASE64Encoder;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Classname qrCodeUtil
 * @Description 二维码生成工具类
 * @Date 2019/11/21 16:01
 * @Author weihua.liao
 */
@Slf4j
public class CommonBarcodeUtil {

    public static final String IMG_TYPE_PNG="image/png";

    public static final String IMG_TYPE_GIF="image/gif";

    public static final String IMG_TYPE_JPEG="image/jpeg";

    public static final double DEFAULT_HEIGHT = 5;

    /**
     * @Description BASE64加密
      * @param msg
     * @param imgType
     * @return java.lang.String
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static String generateToBase64(String msg,String imgType) throws IOException {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generateToStream(msg,imgType, ous, DEFAULT_HEIGHT);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(ous.toByteArray());
    }

    /**
     * @Description 生成文件
      * @param msg
     * @param imgType
     * @param file
     * @return java.io.File
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static File generateToFile(String msg,String imgType, File file)
            throws IOException {
        return generateToFile(msg, imgType, file, DEFAULT_HEIGHT);
    }

    /**
     * @Description 生成文件
     * @param msg
     * @param imgType
     * @param file
     * @return java.io.File
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static File generateToFile(String msg,String imgType, File file, double height)
            throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            generateToStream(msg,imgType, out, height);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return file;
    }


    /**
     * @Description 生成文件
     * @param msg
     * @param imgType
     * @param file
     * @return java.io.File
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static File generateCode128ToFile(String msg,String imgType, File file, double height)
            throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            generateCode128ToStream(msg,imgType, out, height);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return file;
    }

    /**
     * @Description 字节转换
      * @param msg
     * @param imgType
     * @return byte[]
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static byte[] generateToByte(String msg,String imgType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            generateToStream(msg,imgType, out, DEFAULT_HEIGHT);
            return out.toByteArray();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * @Description 流转换
     * @param msg
     * @param imgType
     * @param out
     * @return void
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static void generateToStream(String msg, String imgType,OutputStream out, double height)
            throws IOException {
        if (msg == null || out == null) {
            return;
        }
        Code39Bean bean = new Code39Bean();

        // 精细度
        int dpi = 150;
        // module宽度
        double moduleWidth = UnitConv.in2mm(1.0f / dpi);
        bean.setModuleWidth(moduleWidth);
        // 不显示文字
        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        bean.setHeight(height);
        bean.setWideFactor(3);
        bean.doQuietZone(false);

        // 输出到流
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, imgType,
                dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        // 生成条形码
        bean.generateBarcode(canvas, msg);
        // 结束绘制
        canvas.finish();
    }

    /**
     * @Description 流转换
     * @param msg
     * @param imgType
     * @param out
     * @return void
     * @Date 2019/12/31 10:14
     * @Author weihua.liao
     */
    public static void generateCode128ToStream(String msg, String imgType,OutputStream out, double height)
            throws IOException {
        if (msg == null || out == null) {
            return;
        }
        Code128Bean bean = new Code128Bean();
        // 分辨率
        int dpi = 100;
        // module宽度
        double moduleWidth = UnitConv.in2mm(1.0f / dpi);
        // 设置两侧是否留白
        bean.doQuietZone(false);
        // 设置条形码高度和宽度
        bean.setBarHeight(height);
        bean.setModuleWidth(moduleWidth);
        bean.setHeight(height);
        // 设置文本位置（包括是否显示）
        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        // 输出到流
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, imgType,
                dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        // 生成条形码
        bean.generateBarcode(canvas, msg);
        // 结束绘制
        canvas.finish();
    }

    /**
     *
     * @Description 将条码按照指定宽高居中，并替换原图片文件，条码大于设置的宽高则不操作
     *
     * @author yuchao.wang
     * @date 2020/9/2 18:47
     * @param imageFile 图片
     * @param imageWidth 目标宽度
     * @param imageHeight 目标高度
     * @return void
     *
     */
    public static void repaintPictureToCenter(File imageFile, int imageWidth, int imageHeight){
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try{
            //获取源图片并确定横X/Y轴起始绘制位置,让图片居中显示在底板上
            BufferedImage buffer = ImageIO.read(imageFile);
            int x = (imageWidth - buffer.getWidth()) / 2;
            int y = (imageHeight - buffer.getHeight()) / 2;

            //如果图片尺寸大于底板尺寸则不作操作，由于是一维码，这里不校验高度
            if(x <= 0){
                log.warn("<==== CommonBarcodeUtil.repaintPictureToCenter 图片尺寸[{},{}]超过模板[{},{}]",
                        buffer.getWidth(), buffer.getHeight(), imageWidth, imageHeight);
                return;
            }

            //创建底板图片，白色背景
            g.setBackground(Color.WHITE);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, imageWidth, imageHeight);

            //图片绘制到底板上
            g.drawImage(buffer, x, y, buffer.getWidth(), buffer.getHeight(), null);

            //重新输出图片
            String extensionName = imageFile.getName().substring(imageFile.getName().lastIndexOf(".")+1);
            ImageIO.write(image, extensionName, imageFile);
        } catch (Exception e){
            log.error("<==== CommonBarcodeUtil.repaintPictureToCenter Error:{} {}", e.getMessage(), e);
        } finally {
            try {
                if(g != null){
                    g.dispose();
                }
            } catch (Exception e) {
                log.error("<==== CommonBarcodeUtil.repaintPictureToCenter.Graphics2D Error", e);
            }
        }
    }

}
