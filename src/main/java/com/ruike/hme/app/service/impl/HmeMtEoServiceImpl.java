package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeMtEoService;
import com.ruike.hme.domain.vo.HmeEoVO3;
import com.ruike.hme.infra.mapper.HmeSnBindEoMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname HmeMtEoServiceImpl
 * @Description 执行作业管理-打印
 * @Date 2020/9/22 15:35
 * @Created by yaoyapeng
 */
@Slf4j
@Service
public class HmeMtEoServiceImpl implements HmeMtEoService {

    private final static String REDIS_EO_PRINT = "tarzan:eo-print:";

    private final static String HASH_KEY = "EO_PRINT_LAST_WRITE";

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeSnBindEoMapper hmeSnBindEoMapper;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    /**
     * @param tenantId
     * @param type
     * @param hmeEoVO3List
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @user yaoyapeng
     */
    @Override
    public void print(Long tenantId, String type, List<HmeEoVO3> hmeEoVO3List, HttpServletResponse response) {
        //获取数据
        if (CollectionUtils.isEmpty(hmeEoVO3List)) {
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
        //循环需要打印的数据，单个打印
        int index = 0;
        // 每一行打印条码个数
        int codeNum = 8;
        List<LovValueDTO> productPowerList = lovAdapter.queryLovValue("HME.PRODUCT_POWER", tenantId);
        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        //循环需要打印的数据，单个打印
        for (int i = 0; i < hmeEoVO3List.size(); i++) {
            HmeEoVO3 hmeEoVO3 = hmeEoVO3List.get(i);
            if (StringUtils.equals(type, "1")) {
                // 条形码
                barcodePath = getBarcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("barcodeImage" + index, barcodePath);
                pdfName = "/hme_mt_eo_print_barcode_template.pdf";
                formMap.put("materialLotCode" + index, hmeEoVO3.getEoNum());
                index++;
                if (index > codeNum - 1 || i == hmeEoVO3List.size() - 1) {
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    index = 0;
                    imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                }
            } else if (StringUtils.equals(type, "2")) {
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                int num = i % 2;
                imgMap.put("qrcodeImage" + num, qrcodePath);
                pdfName = "/hme_mt_eo_print_qrcode_template2.pdf";
                formMap.put("eoNum" + num, hmeEoVO3.getEoNum());
                formMap.put("materialCode" + num, hmeEoVO3.getMaterialCode());
                formMap.put("materialName" + num, hmeEoVO3.getMaterialName());
                formMap.put("workOrder" + num, hmeEoVO3.getWorkOrder());
                formMap.put("version" + num, hmeEoVO3.getVersion());
                if (num == 1 || i == hmeEoVO3List.size() - 1) {
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                }
            } else if (StringUtils.equals(type, "3")) {
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                // 需要打两张纸
                int num = i % 2;
                pdfName = "/hme_mt_eo_print_qrcode_template3.pdf";
                // 同一张纸，两个条码
                // 步长
                int increaseNum = num == 0 ? 0 : 1;
                for (int j = 0; j < 2; j++) {
                    formMap.put("eoNum" + (num + j + increaseNum), hmeEoVO3.getEoNum());
                    imgMap.put("qrcodeImage" + ((num + j + increaseNum)), qrcodePath);
                }
                if (num == 1 || i == hmeEoVO3List.size() - 1) {
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                }
            } else if (StringUtils.equals(type, "4")) {
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                int num = i % 2;
                imgMap.put("qrcodeImage" + num, qrcodePath);
                pdfName = "/hme_mt_eo_print_qrcode_template4.pdf";
                formMap.put("eoNum" + num, hmeEoVO3.getEoNum());
                formMap.put("materialCode" + num, hmeEoVO3.getMaterialCode());
                formMap.put("materialName" + num, hmeEoVO3.getMaterialName());
                formMap.put("workOrder" + num, hmeEoVO3.getWorkOrder());
                formMap.put("version" + num, hmeEoVO3.getVersion());
                formMap.put("soNum" + num, hmeEoVO3.getSoNum());
                if (num == 1 || i == hmeEoVO3List.size() - 1) {
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                }
            } else if (org.apache.commons.lang3.StringUtils.equalsAny(type, "5", "6", "7")) {
                // 二维码
                barcodePath = getBarcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                imgMap.put("barcodeImage", barcodePath);
                if (StringUtils.equals(type, "5")) {
                    pdfName = "/hme_mt_eo_print_template1.pdf";
                } else if (StringUtils.equals(type, "6")) {
                    pdfName = "/hme_mt_eo_print_template2.pdf";
                } else if (StringUtils.equals(type, "7")) {
                    pdfName = "/hme_mt_eo_print_template3.pdf";
                    formMap.put("printOption", hmeEoVO3.getPrintOptionValue());
                }
                formMap.put("eoNum", hmeEoVO3.getEoNum());
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            } else if (org.apache.commons.lang3.StringUtils.equalsAny(type, "8", "9")) {
                // 条形码
                barcodePath = getBarcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                imgMap.put("barcodeImage", barcodePath);
                String prodVersion = "版本：";
                MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
                    setTenantId(tenantId);
                    setIdentification(hmeEoVO3.getEoNum());
                }});
                if (mtEo != null) {
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
                    prodVersion += mtWorkOrder.getProductionVersion();
                }
                if (StringUtils.equals(type, "8")) {
                    pdfName = "/hme_mt_eo_print_template4.pdf";
                    formMap.put("prodVersion", prodVersion);
                } else if (StringUtils.equals(type, "9")) {
                    pdfName = "/hme_mt_eo_print_template5.pdf";
                    formMap.put("prodVersion", prodVersion);
                }
                formMap.put("eoNum", hmeEoVO3.getEoNum());
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            } else if (org.apache.commons.lang3.StringUtils.equalsAny(type, "11", "12")){
                String modelName = hmeSnBindEoMapper.queryModelName(tenantId, defaultSiteId, hmeEoVO3.getMaterialCode());
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                if (StringUtils.equals(type, "11")) {
                    pdfName = "/hme_mt_eo_print_template11.pdf";
                    Optional<LovValueDTO> firstOpt = productPowerList.stream().filter(lov -> StringUtils.equals(hmeEoVO3.getMaterialCode(), lov.getValue())).findFirst();
                    formMap.put("powerSupply", firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
                } else if (StringUtils.equals(type, "12")) {
                    pdfName = "/hme_mt_eo_print_template12.pdf";
                }
                formMap.put("eoNum", hmeEoVO3.getEoNum());
                formMap.put("materialCode", hmeEoVO3.getMaterialCode());
                formMap.put("modelName", modelName);
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            } else if (StringUtils.equals(type, "13")) {
                String modelName = hmeSnBindEoMapper.queryModelName(tenantId, defaultSiteId, hmeEoVO3.getMaterialCode());
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                pdfName = "/hme_mt_eo_print_template13.pdf";
                formMap.put("eoNum", hmeEoVO3.getEoNum());
                formMap.put("materialCode", hmeEoVO3.getMaterialCode());
                formMap.put("modelName", modelName);
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            } else if (StringUtils.equals(type, "10")) {
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, hmeEoVO3.getEoNum(), barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                pdfName = "/hme_mt_eo_print_qrcode_template10.pdf";
                formMap.put("eoNum", hmeEoVO3.getEoNum());
                formMap.put("materialCode", hmeEoVO3.getMaterialCode());
                formMap.put("materialName", hmeEoVO3.getMaterialName());
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            }
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeMtEoServiceImpl.print.multiplePage Error", e);
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
            log.error("<==== HmeMtEoServiceImpl.print.outputPDFFile Error", e);
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
                log.error("<==== HmeMtEoServiceImpl.print.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeMtEoServiceImpl.print.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeMtEoServiceImpl.print.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeMtEoServiceImpl.print.pdfFile Failed: {}", barcodePath);
        }
    }

    @Override
    public List<HmeEoVO3> printCheck(Long tenantId, List<HmeEoVO3> hmeEoVO3List) {
        for (HmeEoVO3 hmeEoVO3 : hmeEoVO3List) {
            MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
                setTenantId(tenantId);
                setIdentification(hmeEoVO3.getEoNum());
            }});
            if (mtEo == null) {
                throw new MtException("HME_EO_TM_01", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_TM_01", "HME"));
            }
        }
        return hmeEoVO3List;
    }

    /**
     * 生成二维码
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @return
     */
    private String getQrcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        String qrcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        barcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(materialLotCode, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        return qrcodePath;
    }

    /**
     * 生成条形码
     *
     * @param basePath
     * @param uuid
     * @param eoNum
     * @param barcodeImageFileList
     * @return
     */
    private String getBarcodePath(String basePath, String uuid, String eoNum, List<File> barcodeImageFileList) {
        // 生成条形码
        String barcodePath = basePath + "/" + uuid + "_" + eoNum + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(eoNum, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeMtEoServiceImpl.singlePrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        return barcodePath;
    }

    /**
     * 更新缓存信息
     *
     * @param tenantId
     * @param value
     * @author jiangling.zheng@hand-china.com 2020/12/11 15:08
     * @return void
     */
    private void updateRedisCache(Long tenantId, String value) {
        String cacheKey = REDIS_EO_PRINT + ":" + tenantId;
        String updateValue = redisHelper.toJson(value);
        redisHelper.hshPut(cacheKey, HASH_KEY, updateValue);
    }
}
