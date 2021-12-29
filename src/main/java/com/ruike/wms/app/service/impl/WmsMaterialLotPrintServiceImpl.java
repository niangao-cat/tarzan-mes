package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO;
import com.ruike.wms.app.service.WmsMaterialLotPrintService;
import com.ruike.wms.domain.vo.WmsMaterialLotPrintVO;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialLotPrintMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.file.FileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * 条码打印服务实现
 *
 * @author penglin.sui@hand-china.com 2020-07-29 20:25
 */
@Service
@Slf4j
public class WmsMaterialLotPrintServiceImpl implements WmsMaterialLotPrintService {

    @Autowired
    private WmsMaterialLotPrintMapper wmsMaterialLotPrintMapper;
    @Autowired
    FileClient fileClient;

    private String bucketName = "material-lot-print-pdf-file";

    /**
     * 单个条码打印
     *
     * @author penglin.sui@hand-china.com 2020-07-29 21:17
     */
    @Override
    public String singlePrint(Long tenantId, WmsMaterialLotPrintVO wmsMaterialLotPrintVo) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String dateTime = System.currentTimeMillis() + "";
        String docNumber = wmsMaterialLotPrintVo.getMaterialLotCode();
        String uuid = UUID.randomUUID().toString();
        // 生成条形码
        String barcodePath = basePath + "/" + uuid + "barcode.png";
        File barcodeImageFile = new File(barcodePath);
        try {
            CommonBarcodeUtil.generateToFile(docNumber, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //生成二维码
        String qrcodePath = basePath + "/" + uuid + "qrcode.png";
//        String content = "RK * " + wmsMaterialLotPrintVo.getMaterialLotCode() + " * " + wmsMaterialLotPrintVo.getMaterialCode() + " * " + wmsMaterialLotPrintVo.getMaterialVersion() + " * "
//                + wmsMaterialLotPrintVo.getSupplierCode() + " * " + wmsMaterialLotPrintVo.getQty() + " * " + wmsMaterialLotPrintVo.getSupplierLot() + " * " + wmsMaterialLotPrintVo.getCreationDate()
//                + " * " + wmsMaterialLotPrintVo.getOutMaterialLotCode();
        //V20201006 modify by penglin.sui for kang.wang 二维码值存储条码号
        String content = wmsMaterialLotPrintVo.getMaterialLotCode();
        try {
            CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //生成图标
        String ruikePath = basePath + "/" + "img/raycus.png";
        log.info("============================ruikePath============================:" + ruikePath);
        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        imgMap.put("ruikeImage", ruikePath);
        imgMap.put("barcodeImage", barcodePath);
        imgMap.put("qrcodeImage", qrcodePath);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        formMap.put("materialLotCode", wmsMaterialLotPrintVo.getMaterialLotCode());
        formMap.put("supplierName", wmsMaterialLotPrintVo.getSupplierName());
        formMap.put("materialCode", wmsMaterialLotPrintVo.getMaterialCode());
        formMap.put("materialVersion", wmsMaterialLotPrintVo.getMaterialVersion());
        formMap.put("materialName", wmsMaterialLotPrintVo.getMaterialName());
        formMap.put("qty", wmsMaterialLotPrintVo.getQty());
        formMap.put("uomName", wmsMaterialLotPrintVo.getUomName());
        formMap.put("lot", wmsMaterialLotPrintVo.getLot());
        formMap.put("supplierLot", wmsMaterialLotPrintVo.getSupplierLot());
        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        param.put("formMap", formMap);
        param.put("imgMap", imgMap);
        //生成PDF
        String pdfPath = basePath + "/" + uuid + ".pdf";
        try {
            log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, param);
            CommonPdfTemplateUtil.multiplePage(basePath + "/material_lot_print_template.pdf", pdfPath, Collections.singletonList(param));
            log.info("<==== 生成PDF完成！{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.generatePDFFile Error", e);
            throw new MtException(e.getMessage());
        }

        //将文件转化成流进行输出
        File pdfFile = new File(pdfPath);
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        try{
//            //设置相应参数
//            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
//            response.setHeader("Content-Disposition","attachment;filename=" + uuid + ".pdf");
//            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
//            if(org.apache.commons.lang.StringUtils.isNotEmpty(encoding)){
//                response.setCharacterEncoding(encoding);
//            }
//
//            //将文件转化成流进行输出
//            bis = new BufferedInputStream(new FileInputStream(pdfPath));
//            bos = new BufferedOutputStream(response.getOutputStream());
//            byte[] buff = new byte[2048];
//            int bytesRead;
//            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//                bos.write(buff, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.outputPDFFile Error", e);
//            throw new MtException(e.getMessage());
//        } finally {
//            try {
//                if (bis != null){
//                    bis.close();
//                }
//                if (bos != null){
//                    bos.close();
//                }
//            } catch (IOException e) {
//                log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.closeIO Error", e);
//            }
//        }

        //将文件转化成流进行输出
        String outPutStream = WmsCommonUtils.getStringByFile(pdfPath);
        //删除临时文件
        if (!barcodeImageFile.delete()) {
            log.info("<==== WmsMaterialLotPrintServiceImpl.singlePrint.deleteBarcodeImageFile Failed: {}", barcodePath);
        }
        String d = DateUtil.date2String(new Date(), "yyyyMMdd");
        String filePath = fileClient.uploadFile(tenantId, bucketName, "pages" + "/" + d,
                uuid + ".pdf", "application/pdf", FileUtil.File2byte(pdfFile));
        filePath = fileClient.getSignedUrl(tenantId, bucketName, null, filePath);
        return filePath;
    }

    /**
     * 条码打印s
     *
     * @author penglin.sui@hand-china.com 2020-07-30 9:37
     */
    @Override
    public void multiplePrint(Long tenantId, List<String> materialLotIds, HttpServletResponse response) throws Exception {
        //获取数据
        List<WmsMaterialLotPrintVO> wmsMaterialLotPrintVOs = queryPrintMaterialLot(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(wmsMaterialLotPrintVOs)) {
            return;
        }

        List<String> filePathList = new ArrayList();
        String outStream = "";
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String dateTime = System.currentTimeMillis() + "";
        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        ;
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String ruikePath = basePath + "/" + "img/raycus.png";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();
        //循环需要打印的数据，单个打印
        for (WmsMaterialLotPrintVO wmsMaterialLotPrintVo : wmsMaterialLotPrintVOs
        ) {
            docNumber = wmsMaterialLotPrintVo.getMaterialLotCode();
            // 生成条形码
            barcodePath = basePath + "/" + uuid + "_" + docNumber + "_barcode.png";
            File barcodeImageFile = new File(barcodePath);
            barcodeImageFileList.add(barcodeImageFile);
            try {
                //CommonBarcodeUtil.generateToFile(docNumber, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
                CommonBarcodeUtil.generateCode128ToFile(docNumber, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
                log.info("<====生成条形码完成！{}", barcodePath);
            } catch (Exception e) {
                log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.generateToFile Error", e);
                throw new MtException(e.getMessage());
            }

            //生成二维码
            qrcodePath = basePath + "/" + uuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            barcodeImageFileList.add(qrcodeImageFile);

//            List<String> paramList = new ArrayList<>();
//            paramList.add(wmsMaterialLotPrintVo.getMaterialLotCode());
//            paramList.add(wmsMaterialLotPrintVo.getMaterialCode());
//            paramList.add(wmsMaterialLotPrintVo.getMaterialVersion());
//            paramList.add(wmsMaterialLotPrintVo.getSupplierCode());
//            paramList.add(wmsMaterialLotPrintVo.getQty());
//            paramList.add(wmsMaterialLotPrintVo.getSupplierLot());
//            paramList.add(wmsMaterialLotPrintVo.getCreationDate());
//            paramList.add(wmsMaterialLotPrintVo.getOutMaterialLotCode());
//            content = this.appendContent("RK", paramList);
            content = wmsMaterialLotPrintVo.getMaterialLotCode();

            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====生成二维码完成！{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("ruikeImage", ruikePath);
            imgMap.put("barcodeImage", barcodePath);
            imgMap.put("qrcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            formMap.put("materialLotCode", wmsMaterialLotPrintVo.getMaterialLotCode());
            formMap.put("supplierName", wmsMaterialLotPrintVo.getSupplierName());
            formMap.put("materialCode", wmsMaterialLotPrintVo.getMaterialCode());
            formMap.put("materialVersion", wmsMaterialLotPrintVo.getMaterialVersion());
            formMap.put("materialName", wmsMaterialLotPrintVo.getMaterialName());
            formMap.put("qty", wmsMaterialLotPrintVo.getQty());
            formMap.put("uomName", wmsMaterialLotPrintVo.getUomName());
            formMap.put("lot", wmsMaterialLotPrintVo.getLot());
            formMap.put("supplierLot", wmsMaterialLotPrintVo.getSupplierLot());
            formMap.put("createDate", wmsMaterialLotPrintVo.getCreationDate());
            Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            param.put("formMap", formMap);
            param.put("imgMap", imgMap);
            dataList.add(param);
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/material_lot_print_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.closeIO Error", e);
            }
        }

        //将文件转化成流进行输出
        //String outPutStream = WmsCommonUtils.getStringByFile(pdfPath);
        /*String d = DateUtil.date2String(new Date(), "yyyyMMdd");
        String filePath = fileClient.uploadFile(tenantId, bucketName, "pages" + "/" + d,
                pdfFileName, "application/pdf", FileUtil.File2byte(pdfFile));
        filePath = fileClient.getSignedUrl(tenantId,bucketName,null,filePath);
        filePathList.add(filePath);*/

        //删除临时文件
        for (File file : barcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== WmsMaterialLotPrintServiceImpl.multiplePrint.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== WmsMaterialLotPrintServiceImpl.multiplePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== WmsMaterialLotPrintServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }

    }

    /**
     * 条码打印-2
     *
     * @author penglin.sui@hand-china.com 2020-07-30 9:37
     */
    @Override
    public void print(Long tenantId, String type, List<WmsMaterialLotQryResultDTO> wmsMaterialLotQryResultDTOList, HttpServletResponse response) {
        //获取数据
        if (org.springframework.util.CollectionUtils.isEmpty(wmsMaterialLotQryResultDTOList)) {
            return;
        }

        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        String pdfName = "";
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

        WmsMaterialLotQryResultDTO wmsMaterialLotQryResultDTO = new WmsMaterialLotQryResultDTO();
        for (int i = 0; i < wmsMaterialLotQryResultDTOList.size(); i++) {
            wmsMaterialLotQryResultDTO = wmsMaterialLotQryResultDTOList.get(i);
            if (StringUtils.equals(type, "1")) {
                // 条形码
                barcodePath = getBarcodePath(basePath, uuid, wmsMaterialLotQryResultDTO.getMaterialLotCode(), barcodeImageFileList);
                int num = i % 2;
                imgMap.put("barcodeImage" + num, barcodePath);
                pdfName = "/wms_material_lot_print_type_1_template.pdf";
                formMap.put("materialLotCode" + num, wmsMaterialLotQryResultDTO.getMaterialLotCode());
                formMap.put("materialCode" + num, wmsMaterialLotQryResultDTO.getMaterialCode());
                formMap.put("materialName" + num, wmsMaterialLotQryResultDTO.getMaterialName());
                formMap.put("primaryUomQty" + num, wmsMaterialLotQryResultDTO.getPrimaryUomQty());
                formMap.put("productDate" + num, wmsMaterialLotQryResultDTO.getProductDate());
                if (num == 1 || i == wmsMaterialLotQryResultDTOList.size() - 1) {
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                }
            }
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsMaterialLotPrintServiceImpl.print.multiplePage Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.print.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== WmsMaterialLotPrintServiceImpl.print.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== WmsMaterialLotPrintServiceImpl.print.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== WmsMaterialLotPrintServiceImpl.print.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== WmsMaterialLotPrintServiceImpl.print.pdfFile Failed: {}", barcodePath);
        }
    }

    /**
     * 生成条形码
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @return
     */
    private String getBarcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        // 生成条形码
        String barcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(materialLotCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.print.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }

        return barcodePath;
    }

    private String appendContent(String header, List<String> paramList) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(header);
        for (String str : paramList) {
            if (StringUtils.isBlank(str)) {
                str = "";
            }
            sbf.append("*").append(str);
        }
        return sbf.toString();
    }

    /**
     * 查询条码信息
     *
     * @author penglin.sui@hand-china.com 2020/07/29 20:11
     */
    @Override
    public List<WmsMaterialLotPrintVO> queryPrintMaterialLot(Long tenantId, List<String> materialLotIds) {
        List<WmsMaterialLotPrintVO> wmsMaterialLotPrintVOList = wmsMaterialLotPrintMapper.materialLotPrint(tenantId, materialLotIds);
        return wmsMaterialLotPrintVOList;
    }
}
