package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.controller.v1.HmeProductionPrintDTO;
import com.ruike.hme.api.dto.HmeProductionPrintDTO2;
import com.ruike.hme.app.service.HmeProductionPrintService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeProductionPrintMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.hme.infra.util.ZebraUtils;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.order.api.dto.MtEoDTO5;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description 打印实现类
 *
 * @author wengang.qiang 2021/09/23 18:41
 */
@Service
@Slf4j
public class HmeProductionPrintServiceImpl implements HmeProductionPrintService {

    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final HmeProductionPrintMapper hmeProductionPrintMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final LovAdapter lovAdapter;
    private final MtEoRepository mtEoRepository;

    public HmeProductionPrintServiceImpl(MtWorkOrderRepository mtWorkOrderRepository,
                                         HmeProductionPrintMapper hmeProductionPrintMapper, MtErrorMessageRepository mtErrorMessageRepository, LovAdapter lovAdapter, MtEoRepository mtEoRepository) {
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.hmeProductionPrintMapper = hmeProductionPrintMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.lovAdapter = lovAdapter;
        this.mtEoRepository = mtEoRepository;
    }

    @Override
    public void boxPrints(Long tenantId, HmeProductionPrintVO printVO) {
        MtWorkOrder mtWorkOrder = new MtWorkOrder();
        mtWorkOrder.setWorkOrderNum(printVO.getWorkOrderId());
        MtWorkOrder workOrder = mtWorkOrderRepository.selectOne(mtWorkOrder);
        //设置版本号
        printVO.setVersion(workOrder.getProductionVersion());
        List<HmeProductionPrintAttr> hmeProductionPrintAttrList = hmeProductionPrintMapper.queryAttrByWorkcellId(tenantId, printVO.getWorkcellId());
        Map<String, String> hmeProductionPrintAttrMap = hmeProductionPrintAttrList.stream().collect(Collectors.toMap(HmeProductionPrintAttr::getAttrName, HmeProductionPrintAttr::getAttrValue));
        String printerIp = hmeProductionPrintAttrMap.getOrDefault("PRINTER_IP", "");
        if (StringUtils.isBlank(printerIp)) {
            throw new MtException("HME_PRODUCTION_PRINT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_PRODUCTION_PRINT_001", "HME"));
        }
        //获取端口
        String printerPort = hmeProductionPrintAttrMap.getOrDefault("PRINTER_PORT", "");
        if (StringUtils.isBlank(printerPort)) {
            throw new MtException("HME_PRODUCTION_PRINT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_PRODUCTION_PRINT_001", "HME"));
        }
        int printerIntPort = Integer.parseInt(printerPort);
        String zpl = this.createZpl(printVO);
        ZebraUtils.printZplByNetWorkTimeOut(zpl, printerIp, printerIntPort, 8000);
    }

    @Override
    public void boxPrintSecond(Long tenantId, HmeProductionPrintSecondVO hmeProductionPrintSecondVO) {
        MtWorkOrder mtWorkOrder = new MtWorkOrder();
        mtWorkOrder.setWorkOrderNum(hmeProductionPrintSecondVO.getWorkOrderId());
        MtWorkOrder workOrder = mtWorkOrderRepository.selectOne(mtWorkOrder);
        //设置版本号
        hmeProductionPrintSecondVO.setVersion(workOrder.getProductionVersion());
        List<HmeProductionPrintAttr> hmeProductionPrintAttrList = hmeProductionPrintMapper.queryAttrByWorkcellId(tenantId, hmeProductionPrintSecondVO.getWorkcellId());
        Map<String, String> hmeProductionPrintAttrMap = hmeProductionPrintAttrList.stream().collect(Collectors.toMap(HmeProductionPrintAttr::getAttrName, HmeProductionPrintAttr::getAttrValue));
        String printerIp = hmeProductionPrintAttrMap.getOrDefault("PRINTER_IP", "");
        if (StringUtils.isBlank(printerIp)) {
            throw new MtException("HME_PRODUCTION_PRINT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_PRODUCTION_PRINT_001", "HME"));
        }
        //获取端口
        String printerPort = hmeProductionPrintAttrMap.getOrDefault("PRINTER_PORT", "");
        if (StringUtils.isBlank(printerPort)) {
            throw new MtException("HME_PRODUCTION_PRINT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_PRODUCTION_PRINT_001", "HME"));
        }
        int printerIntPort = Integer.parseInt(printerPort);
        String zplSecond = this.createZplSecond(hmeProductionPrintSecondVO);
        ZebraUtils.printZplByNetWorkTimeOut(zplSecond, printerIp, printerIntPort, 8000);
    }

    @Override
    public List<HmeProductionPrintVO10> multiTemplateQuery(Long tenantId, HmeProductionPrintDTO dto) {
        List<HmeProductionPrintVO10> resultList = new ArrayList<>();
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            //参数${1}为空,请检查!
            throw new MtException("HME_WO_INPUT_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_WO_INPUT_0001", "HME", "工单"));
        }
        List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                .select(MtWorkOrder.FIELD_PRODUCTION_VERSION)
                .andWhere(Sqls.custom()
                        .andEqualTo(MtWorkOrder.FIELD_WORK_ORDER_ID, dto.getWorkOrderId()))
                .build());
        if (CollectionUtils.isEmpty(mtWorkOrderList)) {
            //工单${1}不存在,请检查!
            throw new MtException("HME_CHIP_DATA_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_CHIP_DATA_0001", "HME", dto.getWorkOrderId()));
        }

        String snNum = StringUtils.isBlank(dto.getSnNum()) ? "" : dto.getSnNum();
        dto.setSnNum(snNum);
        HmeProductionPrintVO10 hmeProductionPrintVO10 = new HmeProductionPrintVO10();
        hmeProductionPrintVO10.setSnNum(snNum);
        hmeProductionPrintVO10.setSn1("/");
        hmeProductionPrintVO10.setSn2("/");
        hmeProductionPrintVO10.setPrintNumber(1L);
        resultList.add(hmeProductionPrintVO10);

        String versionNumber = StringUtils.isBlank(mtWorkOrderList.get(0).getProductionVersion()) ? "" : mtWorkOrderList.get(0).getProductionVersion();
        dto.setVersion(versionNumber);
        String subCode = StringUtils.isBlank(dto.getSubCode()) ? "" : dto.getSubCode();
        dto.setSubCode(subCode);
        String scanBarCode = StringUtils.isBlank(dto.getScanBarCode()) ? "" : dto.getScanBarCode();
        dto.setScanBarCode(scanBarCode);
        dto.setScanBarCodeList(Arrays.asList(scanBarCode.split(",")));
        List<HmeProductionPrintDTO> productionPrintDTOList = hmeProductionPrintMapper.queryScanInfo(tenantId, dto);
        dto.setScanPrintDtoList(productionPrintDTOList);
        if (CollectionUtils.isNotEmpty(productionPrintDTOList)) {
            dto.setSapMaterial(productionPrintDTOList.get(0).getSapMaterial());
        }
        for (HmeProductionPrintDTO printDto : dto.getScanPrintDtoList()){
            HmeProductionPrintVO10 hmeProductionPrintVO102 = new HmeProductionPrintVO10();
            hmeProductionPrintVO102.setSnNum(snNum);
            hmeProductionPrintVO102.setSn1(printDto.getSubCode());
            hmeProductionPrintVO102.setSn2(printDto.getScanBarCode());
            hmeProductionPrintVO102.setPrintNumber(1L);
            resultList.add(hmeProductionPrintVO102);
        }
        return resultList;
    }

    private String createZplSecond(HmeProductionPrintSecondVO hmeProductionPrintSecondVO) {
        String zpl = "^XA\n" +
                "^CW1,E:MSSUNG24.FNT\n" +
                "^SEE:GB.DAT\n" +
                "^CI28\n" +
                "^FO150,5^BQN,2,5^FDMM,A" + hmeProductionPrintSecondVO.getSubCode() + "^FS\n" +
                "^FO55,130^BY2,2,4,50^BCN,30,Y,N,N,A^FD" + hmeProductionPrintSecondVO.getSubCode() + "^FS\n" +
                "^FO135,190^A1N^CF0,12,12^FDversion:" + hmeProductionPrintSecondVO.getVersion() + "^FS\n" +
                "^FO100,215^A1N^CF0,12,12^FDcombineNum:" + hmeProductionPrintSecondVO.getSapMaterial() + "^FS\n" +
                "^FO55,250^BY2,2,4,50^BCN,30,Y,N,N,A^FD" + hmeProductionPrintSecondVO.getScanBarCode() + "^FS\n" +
                "^XZ";
        return zpl;
    }

    private String createZpl(HmeProductionPrintVO printVO) {
        String zpl = "^XA\n" +
                "^CW1,E:MSSUNG24.FNT\n" +
                "^SEE:GB.DAT\n" +
                "^CI28\n" +
                "^FO145,20^BQN,2,5^FDMM,A" + printVO.getSnNum() + "^FS\n" +
                "^FO50,160^BY2,2,4^BCN,30,Y,N,N,A^FD" + printVO.getSnNum() + "^FS\n" +
                "^FO80,299^A1N^CF0,13,20^FDsn:" + printVO.getSnNum() + "^FS\n" +
                "^FO80,259^A1N^CF0,13,20^FDversion:" + printVO.getVersion() + "^FS\n" +
                "^FO80,288^A1N^CF0,10,10^FDcombineNum:" + printVO.getSapMaterial() + "^FS\n" +
                "^XZ";
        return zpl;
    }

    private HmeProductionPrintVO2 multiTemplateCreate(HmeProductionPrintDTO dto) {
        HmeProductionPrintVO2 resultVO = new HmeProductionPrintVO2();
        List<Map<String, Object>> dataList = new ArrayList<>(1);
        String uuid = dto.getUuid();
        String barcodePath = dto.getBasePath() + "/" + uuid + "_" + dto.getSnNum() + "_barcode.png";
        String qrcodePath = dto.getBasePath() + "/" + uuid + "_" + dto.getSnNum() + "_qrcode.png";
        List<File> fileList = new ArrayList<>(2);
        // 生成条形码
        File barcodeImageFile = new File(barcodePath);
        fileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(dto.getSnNum(), CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //生成二维码
        File qrcodeImageFile = new File(qrcodePath);
        fileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(dto.getSnNum(), qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        imgMap.put("barcode", barcodePath);
        imgMap.put("qrcode", qrcodePath);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        formMap.put("snNum", dto.getSnNum());
        formMap.put("versionNumber", dto.getVersion());
        formMap.put("materialCode", dto.getSapMaterial());
        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        param.put("formMap", formMap);
        param.put("imgMap", imgMap);
        dataList.add(param);
        resultVO.setDataList(dataList);
        resultVO.setFileList(fileList);
        return resultVO;
    }

    private HmeProductionPrintVO2 multiTemplateCreate2(HmeProductionPrintDTO dto) {
        HmeProductionPrintVO2 resultVO = new HmeProductionPrintVO2();
        List<Map<String, Object>> dataList = new ArrayList<>();
        String uuid = dto.getUuid();
        String barcodePath = dto.getBasePath() + "/" + uuid + "_" + dto.getSnNum() + "_barcode2.png";
        String qrcodePath = dto.getBasePath() + "/" + uuid + "_" + dto.getSnNum() + "_qrcode2.png";
        List<File> fileList = new ArrayList<>();
        // 生成条形码
        File barcode2ImageFile = new File(barcodePath);
        fileList.add(barcode2ImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(dto.getSnNum(), CommonBarcodeUtil.IMG_TYPE_PNG, barcode2ImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeProductionPrintServiceImpl.multiTemplateCreate2.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //生成二维码
        File qrcodeImageFile = new File(qrcodePath);
        fileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(dto.getSnNum(), qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeProductionPrintServiceImpl.multiTemplateCreate2.encode Error", e);
            throw new MtException(e.getMessage());
        }

        for (HmeProductionPrintDTO printDto : dto.getScanPrintDtoList()
        ) {
            String barcode3Path = dto.getBasePath() + "/" + uuid + "_" + printDto.getScanBarCode() + "_barcode3.png";
            // 生成条形码
            File barcode3ImageFile = new File(barcode3Path);
            fileList.add(barcode3ImageFile);
            try {
                CommonBarcodeUtil.generateCode128ToFile(printDto.getScanBarCode(), CommonBarcodeUtil.IMG_TYPE_PNG, barcode3ImageFile, 10);
                log.info("<====生成条形码完成！{}", barcode3Path);
            } catch (Exception e) {
                log.error("<==== HmeProductionPrintServiceImpl.multiTemplateCreate2.generateToFile Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(3);
            imgMap.put("barcode", barcodePath);
            imgMap.put("qrcode", qrcodePath);
            imgMap.put("barcode2", barcode3Path);
            Map<String, Object> formMap = new HashMap<>(4);
            formMap.put("sn1", printDto.getSubCode());
            formMap.put("versionNumber", dto.getVersion());
            formMap.put("materialCode", dto.getSapMaterial());
            formMap.put("sn2", printDto.getScanBarCode());
            Map<String, Object> param = new HashMap<>(2);
            param.put("formMap", formMap);
            param.put("imgMap", imgMap);
            dataList.add(param);
        }
        resultVO.setDataList(dataList);
        resultVO.setFileList(fileList);
        return resultVO;
    }

    private void multiTemplatePrintsCommon(Long tenantId, HmeProductionPrintDTO dto, HttpServletResponse response) {
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            //参数${1}为空,请检查!
            throw new MtException("HME_WO_INPUT_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_WO_INPUT_0001", "HME", "工单"));
        }
        if (Objects.isNull(dto.getPrintNumber())) {
            dto.setPrintNumber(1L);
        }
        List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                .select(MtWorkOrder.FIELD_PRODUCTION_VERSION)
                .andWhere(Sqls.custom()
                        .andEqualTo(MtWorkOrder.FIELD_WORK_ORDER_ID, dto.getWorkOrderId()))
                .build());
        if (CollectionUtils.isEmpty(mtWorkOrderList)) {
            //工单${1}不存在,请检查!
            throw new MtException("HME_CHIP_DATA_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_CHIP_DATA_0001", "HME", dto.getWorkOrderId()));
        }

        String snNum = StringUtils.isBlank(dto.getSnNum()) ? "" : dto.getSnNum();
        dto.setSnNum(snNum);
        String versionNumber = StringUtils.isBlank(mtWorkOrderList.get(0).getProductionVersion()) ? "" : mtWorkOrderList.get(0).getProductionVersion();
        dto.setVersion(versionNumber);
        String subCode = StringUtils.isBlank(dto.getSubCode()) ? "" : dto.getSubCode();
        dto.setSubCode(subCode);
        String scanBarCode = StringUtils.isBlank(dto.getScanBarCode()) ? "" : dto.getScanBarCode();
        dto.setScanBarCode(scanBarCode);
        dto.setScanBarCodeList(Arrays.asList(scanBarCode.split(",")));
        List<HmeProductionPrintDTO> productionPrintDTOList = hmeProductionPrintMapper.queryScanInfo(tenantId, dto);
        dto.setScanPrintDtoList(productionPrintDTOList);
        if (CollectionUtils.isNotEmpty(productionPrintDTOList)) {
            dto.setSapMaterial(productionPrintDTOList.get(0).getSapMaterial());
        }

        String uuid = UUID.randomUUID().toString();
        dto.setUuid(uuid);
        String pdfFileName = uuid + ".pdf";

        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);

        String basePath;
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
        dto.setBasePath(basePath);
        String pdfPath = basePath + "/" + pdfFileName;

        Map<String, java.util.List<Map<String, Object>>> templateDataList = new HashMap<>();
        List<File> fileList = new ArrayList<>();
        if ("0".equals(dto.getPrintType()) || "1".equals(dto.getPrintType())) {
            HmeProductionPrintVO2 productionPrint1VO2 = multiTemplateCreate(dto);
            List<Map<String, Object>> dataList = productionPrint1VO2.getDataList();
            if (CollectionUtils.isNotEmpty(dataList)) {
                templateDataList.put(basePath + "/hme_sitein_print_template.pdf", dataList);
            }
            if (CollectionUtils.isNotEmpty(productionPrint1VO2.getFileList())) {
                fileList.addAll(productionPrint1VO2.getFileList());
            }
        }

        if ("0".equals(dto.getPrintType()) || "2".equals(dto.getPrintType())) {
            HmeProductionPrintVO2 productionPrint2VO2 = multiTemplateCreate2(dto);
            List<Map<String, Object>> dataList2 = productionPrint2VO2.getDataList();
            if (CollectionUtils.isNotEmpty(dataList2)) {
                templateDataList.put(basePath + "/hme_scan_barcode_print_template.pdf", dataList2);
            }
            if (CollectionUtils.isNotEmpty(productionPrint2VO2.getFileList())) {
                fileList.addAll(productionPrint2VO2.getFileList());
            }
        }

        if (templateDataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", templateDataList.size());
                CommonPdfTemplateUtil.multipleTemplatePage(pdfPath, templateDataList, dto.getPrintNumber());
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
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
                if (StringUtils.isNotEmpty(encoding)) {
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
                log.error("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.outputPDFFile Error", e);
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
                    log.error("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.closeIO Error", e);
                }
            }

            if (!pdfFile.delete()) {
                log.info("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.pdfFile Failed: {}");
            }
        }

        //删除临时文件
        for (File file : fileList) {
            if (!file.delete()) {
                log.info("<==== HmeProductionPrintServiceImpl.multiTemplatePrintsCommon.deleteFile Failed: {}");
            }
        }
    }

    @Override
    public void multiTemplatePrints(Long tenantId, HmeProductionPrintDTO dto, HttpServletResponse response) {
        multiTemplatePrintsCommon(tenantId, dto, response);
    }

    @Override
    public HmeProductionPrintVO9 internalCodeQuery(Long tenantId, List<MtEoDTO5> dtoList) {
        //执行作业铭牌打印查询校验
        List<HmeProductionPrintVO8> pdfDataList = eoNameplatePrintVerify(tenantId, dtoList);
        //2021-11-03 edit by chaonan.hu for peng.zhao 增加返修SN的情况
        List<HmeProductionPrintVO3> eoInternalCodeList = new ArrayList<>();
        //判断勾选的eo中是否存在返修SN
        List<String> repairSnList = dtoList.stream().filter(item -> StringUtils.isNotBlank(item.getRepairSn())).map(MtEoDTO5::getRepairSn).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(repairSnList)){
            //根据返修SN查询AF_FLAG等于Y的返修SN
            List<String> repairSns = hmeProductionPrintMapper.afFlagYMaterialLotQuery(tenantId, repairSnList);
            if(CollectionUtils.isNotEmpty(repairSns)){
                //如果存在AF_FLAG等于Y的返修SN，则这些EO数据的内部识别码全部为空,同时更新PDF界面数据的serialNumber为物料编码+返修SN
                List<MtEoDTO5> repairSnEoList = dtoList.stream().filter(item -> repairSns.contains(item.getRepairSn())).collect(Collectors.toList());
                for (MtEoDTO5 mtEoDTO5:repairSnEoList) {
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                    hmeProductionPrintVO3.setEoId(mtEoDTO5.getEoId());
                    hmeProductionPrintVO3.setEoNum(mtEoDTO5.getRepairSn());
                    eoInternalCodeList.add(hmeProductionPrintVO3);
                    List<HmeProductionPrintVO8> singlePdfDataList = pdfDataList.stream().filter(item -> item.getEoId().equals(mtEoDTO5.getEoId())).collect(Collectors.toList());
                    singlePdfDataList.get(0).setSerialNumber(mtEoDTO5.getMaterialCode() + " " + mtEoDTO5.getRepairSn());
                }
                dtoList.removeIf(item -> repairSns.contains(item.getRepairSn()));
            }
        }
        if(CollectionUtils.isNotEmpty(dtoList)){
            List<String> eoIdList = dtoList.stream().map(MtEoDTO5::getEoId).collect(Collectors.toList());
            //根据eoId查询hme_eo_job_sn的jobId
            List<HmeEoJobSn> hmeEoJobSnList = hmeProductionPrintMapper.jobIdQueryByEoId(tenantId, eoIdList);
            List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
            //迭代查询整机所有投料数据
            List<HmeProductionPrintVO4> materialQtyInfoList = assembleDataQuery(tenantId, dtoList);
            //取值集HME.IDENTIFYING_CODE_TAG维护的值(数据项编码),进而查询到数据项ID
            List<LovValueDTO> identifyingCodeTagList = lovAdapter.queryLovValue("HME.IDENTIFYING_CODE_TAG", tenantId);
            List<String> tagCodeList = identifyingCodeTagList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            List<String> tagIdList = hmeProductionPrintMapper.getTagIdByTagCode(tenantId, tagCodeList);
            //根据jobId和tagId查询表hme_eo_job_data_record的result
            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeProductionPrintMapper.eoJobDataRecordQuery(tenantId, jobIdList, tagIdList);
            //查询AC
            HmeProductionPrintVO6 hmeProductionPrintVO6 = acQuery(tenantId, dtoList, materialQtyInfoList);
            //查询DC
            dcQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询泵浦源
            pumpQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询输出光缆
            opticalCableQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询航插型号
            aerPlugModelQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询上位机版本
            hostComVerQuery(tenantId, dtoList, hmeProductionPrintVO6, hmeEoJobSnList, hmeEoJobDataRecordList);
            //查询主控板型号
            mainControlModQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询主控板程序
            mainControlProgramQuery(tenantId, dtoList, hmeProductionPrintVO6, hmeEoJobSnList, hmeEoJobDataRecordList);
            //查询光模控制板型号
            optModeControlModel(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询光模控制板程序
            optModeControlProgramQuery(tenantId, dtoList, hmeProductionPrintVO6, hmeEoJobSnList, hmeEoJobDataRecordList);
            //查询合束控制板型号
            comBundleControlModelQuery(tenantId, dtoList, hmeProductionPrintVO6, materialQtyInfoList);
            //查询合束控制板程序
            comBundleControlProgramQuery(tenantId, dtoList, hmeProductionPrintVO6, hmeEoJobSnList, hmeEoJobDataRecordList);
            eoInternalCodeList.addAll(hmeProductionPrintVO6.getResultList());
        }
        HmeProductionPrintVO9 result = new HmeProductionPrintVO9();
        result.setPdfDataList(pdfDataList);
        result.setEoInternalCodeList(eoInternalCodeList);
        return result;
    }

    @Override
    public List<HmeProductionPrintVO4> assembleDataQuery(Long tenantId, List<MtEoDTO5> dtoList) {
        List<String> identificationList = dtoList.stream().map(MtEoDTO5::getEoIdentification).collect(Collectors.toList());
        //eoMaterialLotCodeMap 存储的是eo下的所有投料条码
        Map<String, List<String>> eoMaterialLotCodeMap = new HashMap<>(dtoList.size());
        int i = 0;
        List<HmeProductionPrintVO7> identificationMaterialLotIdList = new ArrayList<>();
        //allHmeProductionPrintVO7List 存储的是所有投料条码与其对应的投料物料
        List<HmeProductionPrintVO7> allHmeProductionPrintVO7List = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(identificationList)) {
            //根据eo标识查询表hme_eo_job_material的material_id +material_lot_code
            List<HmeProductionPrintVO7> hmeProductionPrintVO7List = hmeProductionPrintMapper.eoJobMaterialQueryByIdentification(tenantId, identificationList);
            identificationList = hmeProductionPrintVO7List.stream().map(HmeProductionPrintVO7::getMaterialLotCode).collect(Collectors.toList());
            allHmeProductionPrintVO7List.addAll(hmeProductionPrintVO7List);
            //记录下每个eo下的所有投料条码关系
            if (i == 0) {
                for (MtEoDTO5 mtEo : dtoList) {
                    List<String> singleMaterialLotCodeList = hmeProductionPrintVO7List.stream().filter(item -> item.getIdentification().equals(mtEo.getEoIdentification())).map(HmeProductionPrintVO7::getMaterialLotCode).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(singleMaterialLotCodeList)) {
                        eoMaterialLotCodeMap.put(mtEo.getEoId(), singleMaterialLotCodeList);
                    }
                }
            } else {
                for (MtEoDTO5 mtEo : dtoList) {
                    List<String> alreadyAssembleMaterialLotCodeList = eoMaterialLotCodeMap.get(mtEo.getEoId());
                    if (CollectionUtils.isNotEmpty(alreadyAssembleMaterialLotCodeList)) {
                        List<String> singleMaterialLotCodeList = hmeProductionPrintVO7List.stream().filter(item -> alreadyAssembleMaterialLotCodeList.contains(item.getIdentification())).map(HmeProductionPrintVO7::getMaterialLotCode).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleMaterialLotCodeList)) {
                            alreadyAssembleMaterialLotCodeList.addAll(singleMaterialLotCodeList);
                            eoMaterialLotCodeMap.put(mtEo.getEoId(), alreadyAssembleMaterialLotCodeList);
                        }
                    }
                }
            }
            i++;
        }
        List<HmeProductionPrintVO4> materialQtyInfoList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<String> assembleMaterialLotCodeList = eoMaterialLotCodeMap.get(mtEo.getEoId());
            if (CollectionUtils.isNotEmpty(assembleMaterialLotCodeList)) {
                List<HmeProductionPrintVO7> singleHmeProductionPrintVO7List = allHmeProductionPrintVO7List.stream().filter(item -> assembleMaterialLotCodeList.contains(item.getMaterialLotCode())).collect(Collectors.toList());
                List<String> singleMaterialIdList = singleHmeProductionPrintVO7List.stream().map(HmeProductionPrintVO7::getMaterialId).distinct().collect(Collectors.toList());
                for (String singleMaterialId : singleMaterialIdList) {
                    HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                    hmeProductionPrintVO4.setEoId(mtEo.getEoId());
                    hmeProductionPrintVO4.setMaterialId(singleMaterialId);
                    long count = singleHmeProductionPrintVO7List.stream().filter(item -> singleMaterialId.equals(item.getMaterialId())).count();
                    hmeProductionPrintVO4.setQty(new BigDecimal(count));
                    materialQtyInfoList.add(hmeProductionPrintVO4);
                }
            }
        }
        return materialQtyInfoList;
    }

    @Override
    public HmeProductionPrintVO6 acQuery(Long tenantId, List<MtEoDTO5> dtoList,
                                         List<HmeProductionPrintVO4> materialQtyInfoList) {
        HmeProductionPrintVO6 result = new HmeProductionPrintVO6();
        List<HmeProductionPrintVO3> resultList = new ArrayList<>();
        List<String> eoIdList = dtoList.stream().map(MtEoDTO5::getEoId).collect(Collectors.toList());
        List<String> identificationList = dtoList.stream().map(MtEoDTO5::getEoIdentification).collect(Collectors.toList());
        result.setIdentificationList(identificationList);
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        //根据EOID查询质量文件头表 这样后续其他字段就不用重复查询此部分数据
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = hmeProductionPrintMapper.quantityAnalyzeDocQuery(tenantId, eoIdList);
        result.setQuantityAnalyzeDocList(quantityAnalyzeDocList);
        //根据type=AC查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "AC");
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ40", "TQ79");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        //selfControlEoIdList 存储的是需进行自制查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(singleQuantityAnalyzeLineResultList)) {
                //如果当前eo没有在质量文件头行表找到数据，则进行自制查询逻辑
                selfControlEoIdList.add(mtEo.getEoId());
                continue;
            }
            long singleQuantityAnalyzeDoccount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
            if (singleQuantityAnalyzeDoccount > 1) {
                //如果当前eo在质量文件头行表找到多条头，则AC赋值为ZY
                HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                hmeProductionPrintVO3.setAc("ZY");
                resultList.add(hmeProductionPrintVO3);
                continue;
            }
            HmeProductionPrintVO5 singleQuantityAnalyzeLineResult = singleQuantityAnalyzeLineResultList.get(0);
            String resultStr = singleQuantityAnalyzeLineResult.getResult();
            String[] materialCodeArray = resultStr.split("/");
            List<String> materialCodeList = Arrays.asList(materialCodeArray);
            List<String> distinctMaterialCodeList = materialCodeList.stream().distinct().collect(Collectors.toList());
            //singleQuantityAnalyzeLineList 存储的是第一种情况在质量文件行表找到的物料信息
            List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, distinctMaterialCodeList);
            List<HmeProductionPrintVO4> singleQuantityAnalyzeLineList = new ArrayList<>();
            for (MtMaterial mtMaterial : mtMaterialList) {
                HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                hmeProductionPrintVO4.setMaterialId(mtMaterial.getMaterialId());
                long count = materialCodeList.stream().filter(item -> item.equals(mtMaterial.getMaterialCode())).count();
                hmeProductionPrintVO4.setQty(BigDecimal.valueOf(count));
                singleQuantityAnalyzeLineList.add(hmeProductionPrintVO4);
            }
            //singleNameplatePrintRelLineList 存储的是符合匹配要求的打印模板行数据
            List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
            //打印模板行数据根据头ID分组，如果该头下的所有行物料以及数量在质量文件行表找到的物料信息数据中均存在则代表该头符合匹配要求
            Map<String, List<HmeProductionPrintVO4>> singleNameplatePrintRelLineMap = hmeNameplatePrintRelLineList.stream().collect(Collectors.groupingBy(HmeProductionPrintVO4::getNameplateHeaderId));
            for (Map.Entry<String, List<HmeProductionPrintVO4>> entry : singleNameplatePrintRelLineMap.entrySet()) {
                List<HmeProductionPrintVO4> value = entry.getValue();
                List<HmeProductionPrintVO4> valueCopy = new ArrayList<>();
                valueCopy.addAll(value);
                value.removeAll(singleQuantityAnalyzeLineList);
                if (CollectionUtils.isEmpty(value)) {
                    singleNameplatePrintRelLineList.addAll(valueCopy);
                }
            }
            if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                if (singleNameplatePrintRelLineList.size() == 1) {
                    //如果匹配结果中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                        hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                        hmeProductionPrintVO3.setAc(identifyingCodeList.get(0));
                        resultList.add(hmeProductionPrintVO3);
                    } else {
                        //如果在多个identifyingCode下，则写入ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                        hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                        hmeProductionPrintVO3.setAc("ZZ");
                        resultList.add(hmeProductionPrintVO3);
                    }
                } else if (singleNameplatePrintRelLineList.size() == 2) {
                    //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                        hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                        hmeProductionPrintVO3.setAc(identifyingCodeList.get(0));
                        resultList.add(hmeProductionPrintVO3);
                    } else {
                        //如果在多个identifyingCode下，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                        hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                        hmeProductionPrintVO3.setAc("ZZ");
                        resultList.add(hmeProductionPrintVO3);
                    }
                } else {
                    //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                    hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                    hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                    hmeProductionPrintVO3.setAc("ZY");
                    resultList.add(hmeProductionPrintVO3);
                }
            } else {
                //如果交集中没有数据，则需进行自制逻辑查询
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        //自制逻辑查询
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //singleMaterialQtyInfoList 存储的是该eo对应的所有整机投料数据
                List<HmeProductionPrintVO4> singleMaterialQtyInfoList = new ArrayList<>();
                singleMaterialQtyInfoList.addAll(materialQtyInfoList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()));
                //2021-10-22 09:57 edit by chaonan.hu for hui.gu
                //因为AC的匹配要求由最初定的物料+数量匹配改为只由物料匹配，在改动代码最小的情况下，基础数据维护功能中将AC类型的打印模板行数量前台只能维护为1，后端代码只需将当前eo所对应的整机投料数据中的数量手动覆盖为1即可
                if(CollectionUtils.isNotEmpty(singleMaterialQtyInfoList)){
                    List<HmeProductionPrintVO4> singleMaterialQtyInfoListCopy = new ArrayList<>();
                    for (HmeProductionPrintVO4 hmeProductionPrintVO4:singleMaterialQtyInfoList) {
                        HmeProductionPrintVO4 hmeProductionPrintVO4Copy = new HmeProductionPrintVO4();
                        BeanCopierUtil.copy(hmeProductionPrintVO4, hmeProductionPrintVO4Copy);
                        hmeProductionPrintVO4Copy.setQty(BigDecimal.valueOf(1));
                        singleMaterialQtyInfoListCopy.add(hmeProductionPrintVO4Copy);
                    }
                    singleMaterialQtyInfoList = singleMaterialQtyInfoListCopy;
                }
                //singleNameplatePrintRelLineList 存储的是符合匹配要求的打印模板行数据
                List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                //打印模板行数据根据头ID分组，如果该头下的所有行物料以及数量在eo的整机投料数据中均存在则代表该头符合匹配要求
                Map<String, List<HmeProductionPrintVO4>> singleNameplatePrintRelLineMap = hmeNameplatePrintRelLineList.stream().collect(Collectors.groupingBy(HmeProductionPrintVO4::getNameplateHeaderId));
                for (Map.Entry<String, List<HmeProductionPrintVO4>> entry : singleNameplatePrintRelLineMap.entrySet()) {
                    List<HmeProductionPrintVO4> value = entry.getValue();
                    List<HmeProductionPrintVO4> valueCopy = new ArrayList<>();
                    valueCopy.addAll(value);
                    value.removeAll(singleMaterialQtyInfoList);
                    if (CollectionUtils.isEmpty(value)) {
                        singleNameplatePrintRelLineList.addAll(valueCopy);
                    }
                }
                if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                    if (singleNameplatePrintRelLineList.size() == 1) {
                        //如果匹配结果中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                            hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                            hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                            hmeProductionPrintVO3.setAc(identifyingCodeList.get(0));
                            resultList.add(hmeProductionPrintVO3);
                        } else {
                            //如果在多个identifyingCode下，则写入ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                            hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                            hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                            hmeProductionPrintVO3.setAc("ZZ");
                            resultList.add(hmeProductionPrintVO3);
                        }
                    } else if (singleNameplatePrintRelLineList.size() == 2) {
                        //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                            hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                            hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                            hmeProductionPrintVO3.setAc(identifyingCodeList.get(0));
                            resultList.add(hmeProductionPrintVO3);
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                            hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                            hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                            hmeProductionPrintVO3.setAc("ZZ");
                            resultList.add(hmeProductionPrintVO3);
                        }
                    } else {
                        //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                        hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                        hmeProductionPrintVO3.setAc("ZY");
                        resultList.add(hmeProductionPrintVO3);
                    }
                } else {
                    //如果交集中没有数据，则写入默认值ZZ
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = new HmeProductionPrintVO3();
                    hmeProductionPrintVO3.setEoId(mtEo.getEoId());
                    hmeProductionPrintVO3.setEoNum(mtEo.getEoNum());
                    hmeProductionPrintVO3.setAc("ZZ");
                    resultList.add(hmeProductionPrintVO3);
                }
            }
        }
        result.setResultList(resultList);
        return result;
    }

    @Override
    public HmeProductionPrintVO6 dcQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                         List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ41", "TQ80");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据type=DC查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "DC");
        //selfControlEoIdList 存储的是需进行自制查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(singleQuantityAnalyzeLineResultList)) {
                //如果当前eo没有在质量文件头行表找到数据，则进行自制查询逻辑
                selfControlEoIdList.add(mtEo.getEoId());
                continue;
            }
            long singleQuantityAnalyzeDoccount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
            if (singleQuantityAnalyzeDoccount > 1) {
                //如果当前eo在质量文件头行表找到多条头，则AC赋值为ZY
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setDc("ZY");
                continue;
            }
            HmeProductionPrintVO5 singleQuantityAnalyzeLineResult = singleQuantityAnalyzeLineResultList.get(0);
            String resultStr = singleQuantityAnalyzeLineResult.getResult();
            String[] materialCodeArray = resultStr.split("/");
            List<String> materialCodeList = Arrays.asList(materialCodeArray);
            //singleQuantityAnalyzeLineList 存储的是第一种情况在质量文件行表找到的物料信息
            List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, materialCodeList);
            List<HmeProductionPrintVO4> singleQuantityAnalyzeLineList = new ArrayList<>();
            for (String materialCode : materialCodeList) {
                long count = singleQuantityAnalyzeLineList.stream().filter(item -> item.getMaterialCode().equals(materialCode)).count();
                if (count > 0) {
                    continue;
                }
                HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                List<MtMaterial> singleMaterial = mtMaterialList.stream().filter(item -> item.getMaterialCode().equals(materialCode)).collect(Collectors.toList());
                hmeProductionPrintVO4.setMaterialId(singleMaterial.get(0).getMaterialId());
                hmeProductionPrintVO4.setMaterialCode(singleMaterial.get(0).getMaterialCode());
                hmeProductionPrintVO4.setQty(BigDecimal.valueOf(1));
                singleQuantityAnalyzeLineList.add(hmeProductionPrintVO4);
            }
            //singleNameplatePrintRelLineList 存储的是符合匹配要求的打印模板行数据
            List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
            //打印模板行数据根据头ID分组，如果该头下的所有行物料以及数量在质量文件行表找到的物料信息数据中均存在则代表该头符合匹配要求
            Map<String, List<HmeProductionPrintVO4>> singleNameplatePrintRelLineMap = hmeNameplatePrintRelLineList.stream().collect(Collectors.groupingBy(HmeProductionPrintVO4::getNameplateHeaderId));
            for (Map.Entry<String, List<HmeProductionPrintVO4>> entry : singleNameplatePrintRelLineMap.entrySet()) {
                List<HmeProductionPrintVO4> value = entry.getValue();
                List<HmeProductionPrintVO4> valueCopy = new ArrayList<>();
                valueCopy.addAll(value);
                value.removeAll(singleQuantityAnalyzeLineList);
                if (CollectionUtils.isEmpty(value)) {
                    singleNameplatePrintRelLineList.addAll(valueCopy);
                }
            }
            if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                if (singleNameplatePrintRelLineList.size() == 1) {
                    //如果匹配结果中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setDc(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setDc("ZZ");
                    }
                } else if (singleNameplatePrintRelLineList.size() == 2) {
                    //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setDc(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setDc("ZZ");
                    }
                } else {
                    //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setDc("ZY");
                }
            } else {
                //如果交集中没有数据，则需进行自制逻辑查询
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        //自制逻辑查询
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //singleMaterialQtyInfoList 存储的是该eo对应的所有整机投料数据
                List<HmeProductionPrintVO4> singleMaterialQtyInfoList = new ArrayList<>();
                singleMaterialQtyInfoList.addAll(materialQtyInfoList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()));
                //2021-10-22 09:57 edit by chaonan.hu for hui.gu
                //因为DC的匹配要求由最初定的物料+数量匹配改为只由物料匹配，在改动代码最小的情况下，基础数据维护功能中将AC类型的打印模板行数量前台只能维护为1，后端代码只需将当前eo所对应的整机投料数据中的数量手动覆盖为1即可
                if(CollectionUtils.isNotEmpty(singleMaterialQtyInfoList)){
                    List<HmeProductionPrintVO4> singleMaterialQtyInfoListCopy = new ArrayList<>();
                    for (HmeProductionPrintVO4 hmeProductionPrintVO4:singleMaterialQtyInfoList) {
                        HmeProductionPrintVO4 hmeProductionPrintVO4Copy = new HmeProductionPrintVO4();
                        BeanCopierUtil.copy(hmeProductionPrintVO4, hmeProductionPrintVO4Copy);
                        hmeProductionPrintVO4Copy.setQty(BigDecimal.valueOf(1));
                        singleMaterialQtyInfoListCopy.add(hmeProductionPrintVO4Copy);
                    }
                    singleMaterialQtyInfoList = singleMaterialQtyInfoListCopy;
                }
                //singleNameplatePrintRelLineList 存储的是符合匹配要求的打印模板行数据
                List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                //打印模板行数据根据头ID分组，如果该头下的所有行物料以及数量在eo的整机投料数据中均存在则代表该头符合匹配要求
                Map<String, List<HmeProductionPrintVO4>> singleNameplatePrintRelLineMap = hmeNameplatePrintRelLineList.stream().collect(Collectors.groupingBy(HmeProductionPrintVO4::getNameplateHeaderId));
                for (Map.Entry<String, List<HmeProductionPrintVO4>> entry : singleNameplatePrintRelLineMap.entrySet()) {
                    List<HmeProductionPrintVO4> value = entry.getValue();
                    List<HmeProductionPrintVO4> valueCopy = new ArrayList<>();
                    valueCopy.addAll(value);
                    value.removeAll(singleMaterialQtyInfoList);
                    if (CollectionUtils.isEmpty(value)) {
                        singleNameplatePrintRelLineList.addAll(valueCopy);
                    }
                }
                if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                    if (singleNameplatePrintRelLineList.size() == 1) {
                        //如果匹配结果中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setDc(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setDc("ZZ");
                        }
                    } else if (singleNameplatePrintRelLineList.size() == 2) {
                        //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setDc(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setDc("ZZ");
                        }
                    } else {
                        //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setDc("ZY");
                    }
                } else {
                    //如果交集中没有数据，则写入默认值ZZ
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setDc("ZZ");
                }
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 pumpQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                           List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据type=PUMP查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "PUMP");
        //取值集HME.PUMP_COMB_CODE维护的值(物料编码)
        List<LovValueDTO> pumpCombCodeList = lovAdapter.queryLovValue("HME.PUMP_COMB_CODE", tenantId);
        List<String> pumpMaterialCodeList = pumpCombCodeList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, pumpMaterialCodeList);
        List<String> materialIdList = mtMaterialList.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList());
        for (MtEoDTO5 mtEo : dtoList) {
            //根据materialId筛选集合materialQtyInfoList中的数据
            List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                    .filter(item -> item.getEoId().equals(mtEo.getEoId())
                            && materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(singleMaterialQtyInfoList)) {
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setPump("ZZ");
                continue;
            }
            //从中筛选出符合要求的pump类型的头 nameplateHeaderIdList 存储的符合要求的pump类型的头
            List<String> nameplateHeaderIdList = new ArrayList<>();
            List<String> singleMaterialIdList = singleMaterialQtyInfoList.stream().map(HmeProductionPrintVO4::getMaterialId).distinct().collect(Collectors.toList());
            BigDecimal qtySum = singleMaterialQtyInfoList.stream().collect(CollectorsUtil.summingBigDecimal(item -> item.getQty()));
            Map<String, List<HmeProductionPrintVO4>> hmeNameplatePrintRelLineMap = hmeNameplatePrintRelLineList.stream().collect(Collectors.groupingBy(HmeProductionPrintVO4::getNameplateHeaderId));
            for (Map.Entry<String, List<HmeProductionPrintVO4>> entry : hmeNameplatePrintRelLineMap.entrySet()) {
                List<HmeProductionPrintVO4> value = entry.getValue();
                //该头下的物料个数必须要与singleMaterialQtyInfoList的物料个数相等
                List<String> lineMaterialList = value.stream().map(HmeProductionPrintVO4::getMaterialId).distinct().collect(Collectors.toList());
                if (lineMaterialList.size() != singleMaterialIdList.size()) {
                    continue;
                }
                //该头下的物料个数必须要与singleMaterialQtyInfoList的物料完全一致
                lineMaterialList.removeAll(singleMaterialIdList);
                if (CollectionUtils.isNotEmpty(lineMaterialList)) {
                    continue;
                }
                //该头下的qty之和必须要与singleMaterialQtyInfoList的qty之和相等
                BigDecimal lineQtySum = value.stream().collect(CollectorsUtil.summingBigDecimal(item -> item.getQty()));
                if (qtySum.compareTo(lineQtySum) != 0) {
                    continue;
                }
                nameplateHeaderIdList.add(entry.getKey());
            }
            if (CollectionUtils.isEmpty(nameplateHeaderIdList)) {
                //如果没有符合匹配要求的头，则写入ZZ
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setPump("ZZ");
            } else {
                if (nameplateHeaderIdList.size() == 1) {
                    //如果只有一个符合要求的头，则写入identifying_code
                    List<HmeProductionPrintVO4> singleHmeNameplatePrintRelLineList = hmeNameplatePrintRelLineList.stream().filter(item -> nameplateHeaderIdList.contains(item.getNameplateHeaderId())).collect(Collectors.toList());
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setPump(singleHmeNameplatePrintRelLineList.get(0).getIdentifyingCode());
                } else {
                    //如果有多个符合要求的头，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setPump("ZZ");
                }
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 opticalCableQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                   List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据type=OPTICAL_CABLE查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "OPTICAL_CABLE");
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
            singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
            //上面两个集合取交集
            List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                    .filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            singleNameplatePrintRelLineList.retainAll(singleMaterialQtyInfoList);
            if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                if (singleNameplatePrintRelLineList.size() == 1) {
                    //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOpticalCable(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOpticalCable("ZZ");
                    }
                } else if (singleNameplatePrintRelLineList.size() == 2) {
                    //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOpticalCable(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOpticalCable("ZZ");
                    }
                } else {
                    //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setOpticalCable("ZY");
                }
            } else {
                //如果交集中没有数据，则写入默认值ZZ
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setOpticalCable("ZZ");
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 aerPlugModelQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                   List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据type=AER_PLUG_MODEL查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "AER_PLUG_MODEL");
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
            singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
            List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                    .filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            //上面两个集合取交集
            singleNameplatePrintRelLineList.retainAll(singleMaterialQtyInfoList);
            if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                if (singleNameplatePrintRelLineList.size() == 1) {
                    //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setAerPlugModel(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setAerPlugModel("ZZ");
                    }
                } else if (singleNameplatePrintRelLineList.size() == 2) {
                    //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                    List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                    if (identifyingCodeList.size() == 1) {
                        //如果只在一个identifyingCode下，则直接赋值
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setAerPlugModel(identifyingCodeList.get(0));
                    } else {
                        //如果在多个identifyingCode下，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setAerPlugModel("ZZ");
                    }
                } else {
                    //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setAerPlugModel("ZY");
                }
            } else {
                //如果交集中没有数据，则写入默认值ZZ
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setAerPlugModel("ZZ");
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 hostComVerQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                 List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ45", "TQ84");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件头，则直接赋值
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setHostComVer(singleQuantityAnalyzeLineResultList.get(0).getResult());
                } else {
                    //如果找到一个质量文件头，则赋值ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setHostComVer("ZY");
                }
            } else {
                //如果找不到质量文件头，则走自制查询逻辑
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //2021-11-03 09:52 edit by chaonan.hu for peng.zhao 如果找不到对应的数据采集项结果，则赋值默认值
                String hostComVer = "ZZ";
                List<String> singleJobIdList = hmeEoJobSnList.stream().filter(item -> mtEo.getEoId().equals(item.getEoId())).map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleJobIdList)){
                    List<HmeEoJobDataRecord> singleHmeEoJobDataRecordList = hmeEoJobDataRecordList.stream().filter(item -> singleJobIdList.contains(item.getJobId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleHmeEoJobDataRecordList) && StringUtils.isNotBlank(singleHmeEoJobDataRecordList.get(0).getResult())){
                        String result = singleHmeEoJobDataRecordList.get(0).getResult();
                        String[] resultArray = result.split("-");
                        hostComVer = resultArray[0];
                    }
                }
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setHostComVer(hostComVer);
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 mainControlModQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                     List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ46", "TQ85");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //selfControlEoIdList 存储的是需进行自制查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        //根据type=MAIN_CONTROL_MOD查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "MAIN_CONTROL_MOD");
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件头，则取其result作为物料编码
                    List<String> materialCodeList = new ArrayList<>();
                    materialCodeList.add(singleQuantityAnalyzeLineResultList.get(0).getResult());
                    List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, materialCodeList);
                    //singleQuantityAnalyzeMaterialList 存储的是从质量文件头行表取出的物料，数量默认1
                    List<HmeProductionPrintVO4> singleQuantityAnalyzeMaterialList = new ArrayList<>();
                    for (MtMaterial mtMaterial : mtMaterialList) {
                        HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                        hmeProductionPrintVO4.setMaterialId(mtMaterial.getMaterialId());
                        hmeProductionPrintVO4.setQty(BigDecimal.valueOf(1));
                        singleQuantityAnalyzeMaterialList.add(hmeProductionPrintVO4);
                    }
                    List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                    singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                    //上面两个集合取交集
                    singleNameplatePrintRelLineList.retainAll(singleQuantityAnalyzeMaterialList);
                    if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                        if (singleNameplatePrintRelLineList.size() == 1) {
                            //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setMainControlMod(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setMainControlMod("ZZ");
                            }
                        } else if (singleNameplatePrintRelLineList.size() == 2) {
                            //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setMainControlMod(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入默认值ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setMainControlMod("ZZ");
                            }
                        } else {
                            //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setMainControlMod("ZY");
                        }
                    } else {
                        //如果交集中没有数据，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setMainControlMod("ZZ");
                    }
                } else {
                    //如果找到多个质量文件头，则赋值ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setMainControlMod("ZY");
                }
            } else {
                //如果找不到质量文件头，则走自制查询逻辑
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        //自制逻辑查询
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                        .filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                //上面两个集合取交集
                singleNameplatePrintRelLineList.retainAll(singleMaterialQtyInfoList);
                if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                    if (singleNameplatePrintRelLineList.size() == 1) {
                        //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setMainControlMod(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setMainControlMod("ZZ");
                        }
                    } else if (singleNameplatePrintRelLineList.size() == 2) {
                        //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setMainControlMod(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setMainControlMod("ZZ");
                        }
                    } else {
                        //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setMainControlMod("ZY");
                    }
                } else {
                    //如果交集中没有数据，则写入默认值ZZ
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setMainControlMod("ZZ");
                }
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 mainControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                         List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ47", "TQ86");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }

        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件行表结果，则直接赋值result
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setMainControlProgram(singleQuantityAnalyzeLineResultList.get(0).getResult());
                } else {
                    //如果找到多个质量文件行表结果，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setMainControlProgram("ZY");
                }
            } else {
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //2021-11-03 09:52 edit by chaonan.hu for peng.zhao 如果找不到对应的数据采集项结果，则赋值默认值
                String mainControlProgram = "ZZ";
                List<String> singleJobIdList = hmeEoJobSnList.stream().filter(item -> mtEo.getEoId().equals(item.getEoId())).map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleJobIdList)){
                    List<HmeEoJobDataRecord> singleHmeEoJobDataRecordList = hmeEoJobDataRecordList.stream().filter(item -> singleJobIdList.contains(item.getJobId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleHmeEoJobDataRecordList) && StringUtils.isNotBlank(singleHmeEoJobDataRecordList.get(0).getResult())){
                        String result = singleHmeEoJobDataRecordList.get(0).getResult();
                        String[] resultArray = result.split("-");
                        mainControlProgram = resultArray[1];
                    }
                }
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setMainControlProgram(mainControlProgram);
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 optModeControlModel(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ48", "TQ87");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        //根据type=OPT_MODE_CONTROL_MODEL查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "OPT_MODE_CONTROL_MODEL");

        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件头，则取其result作为物料编码
                    List<String> materialCodeList = new ArrayList<>();
                    materialCodeList.add(singleQuantityAnalyzeLineResultList.get(0).getResult());
                    List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, materialCodeList);
                    //singleQuantityAnalyzeMaterialList 存储的是从质量文件头行表取出的物料，数量默认1
                    List<HmeProductionPrintVO4> singleQuantityAnalyzeMaterialList = new ArrayList<>();
                    for (MtMaterial mtMaterial : mtMaterialList) {
                        HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                        hmeProductionPrintVO4.setMaterialId(mtMaterial.getMaterialId());
                        hmeProductionPrintVO4.setQty(BigDecimal.valueOf(1));
                        singleQuantityAnalyzeMaterialList.add(hmeProductionPrintVO4);
                    }
                    List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                    singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                    //上面两个集合取交集
                    singleNameplatePrintRelLineList.retainAll(singleQuantityAnalyzeMaterialList);
                    if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                        if (singleNameplatePrintRelLineList.size() == 1) {
                            //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setOptModeControlModel(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入默认值ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                            }
                        } else if (singleNameplatePrintRelLineList.size() == 2) {
                            //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setOptModeControlModel(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入默认值ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                            }
                        } else {
                            //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setOptModeControlModel("ZY");
                        }
                    } else {
                        //如果交集中没有数据，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                    }
                } else {
                    //如果找到多个质量文件行表结果，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setOptModeControlModel("ZY");
                }
            } else {
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }

        //自制逻辑查询
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                        .filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                //上面两个集合取交集
                singleNameplatePrintRelLineList.retainAll(singleMaterialQtyInfoList);
                if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                    if (singleNameplatePrintRelLineList.size() == 1) {
                        //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setOptModeControlModel(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                        }
                    } else if (singleNameplatePrintRelLineList.size() == 2) {
                        //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setOptModeControlModel(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                        }
                    } else {
                        //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setOptModeControlModel("ZY");
                    }
                } else {
                    //如果交集中没有数据，则写入默认值ZZ
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setOptModeControlModel("ZZ");
                }
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 optModeControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ49", "TQ88");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }

        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件行表结果，则直接赋值result
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setOptModeControlProgram(singleQuantityAnalyzeLineResultList.get(0).getResult());
                } else {
                    //如果找到多个质量文件行表结果，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setOptModeControlProgram("ZY");
                }
            } else {
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //2021-11-03 09:52 edit by chaonan.hu for peng.zhao 如果找不到对应的数据采集项结果，则赋值默认值
                String optModeControlProgram = "ZZ";
                List<String> singleJobIdList = hmeEoJobSnList.stream().filter(item -> mtEo.getEoId().equals(item.getEoId())).map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleJobIdList)){
                    List<HmeEoJobDataRecord> singleHmeEoJobDataRecordList = hmeEoJobDataRecordList.stream().filter(item -> singleJobIdList.contains(item.getJobId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleHmeEoJobDataRecordList) && StringUtils.isNotBlank(singleHmeEoJobDataRecordList.get(0).getResult())){
                        String result = singleHmeEoJobDataRecordList.get(0).getResult();
                        String[] resultArray = result.split("-");
                        optModeControlProgram = resultArray[2];
                    }
                }
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setOptModeControlProgram(optModeControlProgram);
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 comBundleControlModelQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeProductionPrintVO4> materialQtyInfoList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ50", "TQ89");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }
        //根据type=COM_BUNDLE_CONTROL_MODEL查询表hme_nameplate_print_rel_line中的物料和数量
        List<HmeProductionPrintVO4> hmeNameplatePrintRelLineList = hmeProductionPrintMapper.nameplatePrintRelLineQueryByType(tenantId, "COM_BUNDLE_CONTROL_MODEL");

        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件头，则取其result作为物料编码
                    List<String> materialCodeList = new ArrayList<>();
                    materialCodeList.add(singleQuantityAnalyzeLineResultList.get(0).getResult());
                    List<MtMaterial> mtMaterialList = hmeProductionPrintMapper.materialIdQueryByCode(tenantId, materialCodeList);
                    //singleQuantityAnalyzeMaterialList 存储的是从质量文件头行表取出的物料，数量默认1
                    List<HmeProductionPrintVO4> singleQuantityAnalyzeMaterialList = new ArrayList<>();
                    for (MtMaterial mtMaterial : mtMaterialList) {
                        HmeProductionPrintVO4 hmeProductionPrintVO4 = new HmeProductionPrintVO4();
                        hmeProductionPrintVO4.setMaterialId(mtMaterial.getMaterialId());
                        hmeProductionPrintVO4.setQty(BigDecimal.valueOf(1));
                        singleQuantityAnalyzeMaterialList.add(hmeProductionPrintVO4);
                    }
                    List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                    singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                    //上面两个集合取交集
                    singleNameplatePrintRelLineList.retainAll(singleQuantityAnalyzeMaterialList);
                    if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                        if (singleNameplatePrintRelLineList.size() == 1) {
                            //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setComBundleControlModel(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入默认值ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                            }
                        } else if (singleNameplatePrintRelLineList.size() == 2) {
                            //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                            List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                            if (identifyingCodeList.size() == 1) {
                                //如果只在一个identifyingCode下，则直接赋值
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setComBundleControlModel(identifyingCodeList.get(0));
                            } else {
                                //如果在多个identifyingCode下，则写入默认值ZZ
                                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                                hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                            }
                        } else {
                            //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setComBundleControlModel("ZY");
                        }
                    } else {
                        //如果交集中没有数据，则写入默认值ZZ
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                    }
                } else {
                    //如果找到多个质量文件行表结果，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setComBundleControlModel("ZY");
                }
            } else {
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }

        //自制逻辑查询
        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                List<HmeProductionPrintVO4> singleNameplatePrintRelLineList = new ArrayList<>();
                singleNameplatePrintRelLineList.addAll(hmeNameplatePrintRelLineList);
                List<HmeProductionPrintVO4> singleMaterialQtyInfoList = materialQtyInfoList.stream()
                        .filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                //上面两个集合取交集
                singleNameplatePrintRelLineList.retainAll(singleMaterialQtyInfoList);
                if (CollectionUtils.isNotEmpty(singleNameplatePrintRelLineList)) {
                    if (singleNameplatePrintRelLineList.size() == 1) {
                        //如果交集中只有一个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setComBundleControlModel(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                        }
                    } else if (singleNameplatePrintRelLineList.size() == 2) {
                        //如果交集中有两个物料+数量的组合，则判断这个组合是否在一个铭牌打印内部识别码对应关系头下
                        List<String> identifyingCodeList = singleNameplatePrintRelLineList.stream().map(HmeProductionPrintVO4::getIdentifyingCode).distinct().collect(Collectors.toList());
                        if (identifyingCodeList.size() == 1) {
                            //如果只在一个identifyingCode下，则直接赋值
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setComBundleControlModel(identifyingCodeList.get(0));
                        } else {
                            //如果在多个identifyingCode下，则写入默认值ZZ
                            HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                            hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                        }
                    } else {
                        //如果交集中有三个及以上物料+数量的组合，则AC赋值为ZY
                        HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                        hmeProductionPrintVO3.setComBundleControlModel("ZY");
                    }
                } else {
                    //如果交集中没有数据，则写入默认值ZZ
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setComBundleControlModel("ZZ");
                }
            }
        }
        return dto;
    }

    @Override
    public HmeProductionPrintVO6 comBundleControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList) {
        List<HmeProductionPrintVO5> quantityAnalyzeDocList = dto.getQuantityAnalyzeDocList();
        List<HmeProductionPrintVO3> resultList = dto.getResultList();
        //根据EOID查询质量文件行表结果
        List<HmeProductionPrintVO5> quantityAnalyzeLineResultList = new ArrayList<>();
        List<String> docIdList = quantityAnalyzeDocList.stream().map(HmeProductionPrintVO5::getQaDocId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docIdList)) {
            List<HmeProductionPrintVO5> lineResultList = hmeProductionPrintMapper.quantityAnalyzeLineQuery(tenantId, docIdList, "TQ51", "TQ90");
            for (MtEoDTO5 mtEo : dtoList) {
                List<HmeProductionPrintVO5> singleDocList = quantityAnalyzeDocList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
                for (HmeProductionPrintVO5 singleDoc : singleDocList) {
                    List<HmeProductionPrintVO5> singleLineResultList = lineResultList.stream().filter(item -> singleDoc.getQaDocId().equals(item.getQaDocId())).collect(Collectors.toList());
                    for (HmeProductionPrintVO5 singleLineResult : singleLineResultList) {
                        HmeProductionPrintVO5 hmeProductionPrintVO5 = new HmeProductionPrintVO5();
                        hmeProductionPrintVO5.setEoId(mtEo.getEoId());
                        hmeProductionPrintVO5.setQaDocId(singleDoc.getQaDocId());
                        hmeProductionPrintVO5.setResult(singleLineResult.getResult());
                        quantityAnalyzeLineResultList.add(hmeProductionPrintVO5);
                    }
                }
            }
        }

        //selfControlEoIdList 存储的是需要进行自制逻辑查询的eo
        List<String> selfControlEoIdList = new ArrayList<>();
        for (MtEoDTO5 mtEo : dtoList) {
            List<HmeProductionPrintVO5> singleQuantityAnalyzeLineResultList = quantityAnalyzeLineResultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(singleQuantityAnalyzeLineResultList)) {
                long docCount = singleQuantityAnalyzeLineResultList.stream().map(HmeProductionPrintVO5::getQaDocId).distinct().count();
                if (docCount == 1) {
                    //如果找到一个质量文件行表结果，则直接赋值result
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setComBundleControlProgram(singleQuantityAnalyzeLineResultList.get(0).getResult());
                } else {
                    //如果找到多个质量文件行表结果，则写入ZY
                    HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                    hmeProductionPrintVO3.setComBundleControlProgram("ZY");
                }
            } else {
                selfControlEoIdList.add(mtEo.getEoId());
            }
        }

        if (CollectionUtils.isNotEmpty(selfControlEoIdList)) {
            List<MtEoDTO5> selfControlEoList = dtoList.stream().filter(item -> selfControlEoIdList.contains(item.getEoId())).collect(Collectors.toList());
            for (MtEoDTO5 mtEo : selfControlEoList) {
                //2021-11-03 09:52 edit by chaonan.hu for peng.zhao 如果找不到对应的数据采集项结果，则赋值默认值
                String comBundleControlProgram = "ZZ";
                List<String> singleJobIdList = hmeEoJobSnList.stream().filter(item -> mtEo.getEoId().equals(item.getEoId())).map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleJobIdList)){
                    List<HmeEoJobDataRecord> singleHmeEoJobDataRecordList = hmeEoJobDataRecordList.stream().filter(item -> singleJobIdList.contains(item.getJobId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleHmeEoJobDataRecordList) && StringUtils.isNotBlank(singleHmeEoJobDataRecordList.get(0).getResult())){
                        String result = singleHmeEoJobDataRecordList.get(0).getResult();
                        String[] resultArray = result.split("-");
                        comBundleControlProgram = resultArray[3];
                    }
                }
                //业务指明，以上数据一定会找到，最终且只会找到一条result
                HmeProductionPrintVO3 hmeProductionPrintVO3 = resultList.stream().filter(item -> item.getEoId().equals(mtEo.getEoId())).collect(Collectors.toList()).get(0);
                hmeProductionPrintVO3.setComBundleControlProgram(comBundleControlProgram);
            }
        }
        return dto;
    }

    @Override
    public void eoNameplatePrint(Long tenantId, HmeProductionPrintVO9 dto, HttpServletResponse response) {
        List<HmeProductionPrintVO8> pdfDataList = eoNameplatePrintQuery(tenantId, dto);
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

        for (int i = 0; i < pdfDataList.size(); i++) {
            HmeProductionPrintVO8 pdfData = pdfDataList.get(i);
            pdfName = "/hme_mt_eo_print_template14.pdf";
            String modelName = pdfData.getModelName();
            String serialNumber = pdfData.getSerialNumber();
            // 二维码
            qrcodePath = getQrcodePath(basePath, uuid, serialNumber, barcodeImageFileList, i);
            imgMap.put("qrcodeImage0", qrcodePath);
            imgMap.put("qrcodeImage1", qrcodePath);
            formMap.put("serialNumber0", serialNumber);
            formMap.put("serialNumber1", serialNumber);
            formMap.put("modelName0", modelName);
            formMap.put("modelName1", modelName);
            formMap.put("netWeight", pdfData.getNetWeight());
            formMap.put("size", pdfData.getSize());
            formMap.put("powerSupply", pdfData.getPowerSupply());
            formMap.put("internalCode", pdfData.getInternalCode());
            Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            param.put("formMap", formMap);
            param.put("imgMap", imgMap);
            dataList.add(param);
            imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        }

        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeProductionPrintService.eoNameplatePrint Error", e);
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
            log.error("<==== HmeProductionPrintService.eoNameplatePrint Error", e);
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
                log.error("<==== HmeProductionPrintService.eoNameplatePrint.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeProductionPrintService.eoNameplatePrint.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeProductionPrintService.eoNameplatePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeProductionPrintService.eoNameplatePrint.pdfFile Failed: {}", barcodePath);
        }
    }

    @Override
    public List<HmeProductionPrintVO8> eoNameplatePrintQuery(Long tenantId, HmeProductionPrintVO9 dto) {
        List<HmeProductionPrintVO8> pdfDataList = dto.getPdfDataList();
        List<HmeProductionPrintVO3> eoInternalCodeList = dto.getEoInternalCodeList();
        for (HmeProductionPrintVO8 pdfData : pdfDataList) {
            List<HmeProductionPrintVO3> singleEoInternalCode = eoInternalCodeList.stream().filter(item -> item.getEoId().equals(pdfData.getEoId())).collect(Collectors.toList());
            HmeProductionPrintVO3 hmeProductionPrintVO3 = singleEoInternalCode.get(0);
            StringBuilder internalCode = new StringBuilder();
            internalCode.append(hmeProductionPrintVO3.getAc());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getDc());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getPump());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getOpticalCable());
            internalCode.append("-");
            internalCode.append(hmeProductionPrintVO3.getAerPlugModel());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getHostComVer());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getMainControlMod());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getMainControlProgram());
            internalCode.append("-");
            internalCode.append(hmeProductionPrintVO3.getOptModeControlModel());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getOptModeControlProgram());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getComBundleControlModel());
            internalCode.append(" ");
            internalCode.append(hmeProductionPrintVO3.getComBundleControlProgram());
            pdfData.setInternalCode(internalCode.toString());
        }
        return pdfDataList;
    }

    @Override
    public List<HmeProductionPrintVO8> eoNameplatePrintVerify(Long tenantId, List<MtEoDTO5> eoList) {
        MtEo singleEo = mtEoRepository.selectByPrimaryKey(eoList.get(0).getEoId());
        String siteId = singleEo.getSiteId();
        //根据物料ID、站点ID查询物料站点扩展表attribute5所对应的扩展值
        List<String> materialIdList = eoList.stream().map(MtEoDTO5::getMaterialId).distinct().collect(Collectors.toList());
        List<HmeProductionPrintVO7> attribute5List = hmeProductionPrintMapper.materialSiteAttrQuery(tenantId, siteId, materialIdList);
        //根据物料编码查询在值集HME.MACHINE_MODEL中维护的值、含义、描述
        List<LovValueDTO> machineModelList = lovAdapter.queryLovValue("HME.MACHINE_MODEL", tenantId);
        //封装pdf界面所需数据
        List<HmeProductionPrintVO8> pdfDataList = new ArrayList<>();
        for (MtEoDTO5 mtEo : eoList) {
            HmeProductionPrintVO8 hmeProductionPrintVO8 = new HmeProductionPrintVO8();
            hmeProductionPrintVO8.setEoId(mtEo.getEoId());
            List<HmeProductionPrintVO7> modelNameList = attribute5List.stream().filter(item -> item.getMaterialId().equals(mtEo.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(modelNameList) || StringUtils.isBlank(modelNameList.get(0).getAttribute5())) {
                //物料【${1}】未维护机型，请维护！
                throw new MtException("HME_NAMEPLATE_PRINT_REL_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NAMEPLATE_PRINT_REL_002", "HME", mtEo.getMaterialCode()));
            }
            hmeProductionPrintVO8.setModelName(modelNameList.get(0).getAttribute5());
            hmeProductionPrintVO8.setSerialNumber(mtEo.getMaterialCode() + " " + mtEo.getEoIdentification());
            List<LovValueDTO> machineModel = machineModelList.stream().filter(item -> item.getValue().equals(mtEo.getMaterialCode())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(machineModel) || StringUtils.isBlank(machineModel.get(0).getDescription())
                    || StringUtils.isBlank(machineModel.get(0).getMeaning()) || StringUtils.isBlank(machineModel.get(0).getTag())) {
                //物料【${1}】在值集HME.MACHINE_MODEL中，含义、标记或描述存在空值，请维护！
                throw new MtException("HME_NAMEPLATE_PRINT_REL_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NAMEPLATE_PRINT_REL_004", "HME", mtEo.getMaterialCode()));
            }
            hmeProductionPrintVO8.setPowerSupply(machineModel.get(0).getDescription());
            hmeProductionPrintVO8.setNetWeight(machineModel.get(0).getMeaning());
            hmeProductionPrintVO8.setSize(machineModel.get(0).getTag());
            pdfDataList.add(hmeProductionPrintVO8);
        }
        return pdfDataList;
    }

    private String getQrcodePath(String basePath, String uuid, String serialNumber, List<File> barcodeImageFileList, int i) {
        String qrcodePath = basePath + "/" + uuid + "_" + serialNumber + i + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        barcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(serialNumber, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        return qrcodePath;
    }
}
