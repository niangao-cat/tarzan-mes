package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 泵浦源组合关系表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
@Service
@Slf4j
public class HmeEoJobPumpCombServiceImpl implements HmeEoJobPumpCombService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobPumpCombRepository hmeEoJobPumpCombRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeEoJobPumpCombMapper hmeEoJobPumpCombMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobSnBatchService hmeEoJobSnBatchService;
    @Autowired
    private HmeEoJobSnSingleService hmeEoJobSnSingleService;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private HmeEoJobFirstProcessService hmeEoJobFirstProcessService;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Override
    public HmeEoJobPumpCombVO scanWorkOrder(Long tenantId, HmeEoJobPumpCombDTO dto) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getWorkOrderId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工单"));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //校验工单物料是否有对应的筛选规则
        Long filterRuleHeaderCount = hmeEoJobPumpCombMapper.countPumpFilterRuleHeaderByMaterial(tenantId, mtWorkOrder.getMaterialId());
        if(filterRuleHeaderCount == 0){
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
            throw new MtException("HME_EO_JOB_SN_210", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_210", "HME", mtMaterial.getMaterialCode()));
        }
        //根据工单查询SN
        String snNum = hmeEoJobPumpCombRepository.getSnByWorkOrder(tenantId, mtWorkOrder, dto.getWorkcellId());
        if(StringUtils.isBlank(snNum)){
            //若查不到SN，则报错工单无可操作SN
            throw new MtException("HME_EO_JOB_SN_211", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_211", "HME", mtWorkOrder.getWorkOrderNum()));
        }
        //返回结果
        HmeEoJobPumpCombVO result = new HmeEoJobPumpCombVO();
        result.setSnNum(snNum);
        return result;
    }

    @Override
    public void siteInPrint(Long tenantId, HmeEoJobPumpCombDTO2 dto, HttpServletResponse response) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
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
        String snNum = StringUtils.isBlank(dto.getSnNum()) ? "" : dto.getSnNum();
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();
        // 生成条形码
        barcodePath = basePath + "/" + uuid + "_" + snNum + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(snNum, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //生成二维码
        qrcodePath = basePath + "/" + uuid + "_" + snNum + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        qrcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(snNum, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        imgMap.put("barcode", barcodePath);
        imgMap.put("qrcode", qrcodePath);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        formMap.put("snNum", snNum);
        String versionNumber = StringUtils.isBlank(mtWorkOrder.getProductionVersion()) ? "" : mtWorkOrder.getProductionVersion();
        formMap.put("versionNumber", versionNumber);
        String materialCode = StringUtils.isBlank(dto.getSapMaterial()) ? "" : dto.getSapMaterial();
        formMap.put("materialCode", materialCode);
        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        param.put("formMap", formMap);
        param.put("imgMap", imgMap);
        dataList.add(param);
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_sitein_print_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generatePDFFile Error", e);
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
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.outputPDFFile Error", e);
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
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.closeIO Error", e);
            }
        }
        //删除临时文件
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.pdfFile Failed: {}", barcodePath);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchDTO2 dto) {
        HmeEoJobSnBatchVO14 resultVO = new HmeEoJobSnBatchVO14();
        //校验
        HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = this.releaseScanValidate(tenantId,dto);
        resultVO.setComponent(hmeEoJobSnBatchVO8.getComponent());
        MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //2021-11-09 14:20 edit by chaonan.hu for wenxin.zhang 根据物料批ID查询表hme_eo_job_pump_comb中的数据
            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.eoJobPumpCombQueryByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if(Objects.nonNull(hmeEoJobPumpComb)){
                //如果能找到组合物料批，且snLineList中materialLotId不等于组合物料批CombMaterialLotId则报错
                if(CollectionUtils.isNotEmpty(dto.getSnLineList())
                        && !hmeEoJobPumpComb.getCombMaterialLotId().equals(dto.getSnLineList().get(0).getMaterialLotId())){
                    //当前条码已绑定其他SN号【${1}】
                    MtMaterialLot combMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobPumpComb.getCombMaterialLotId());
                    throw new MtException("HME_EO_JOB_SN_076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_076","HME", combMaterialLot.getMaterialLotCode()));
                }
            }
            resultVO.setDeleteFlag(hmeEoJobSnBatchVO8.getDeleteFlag());
            List<HmeEoJobSnBatchVO6> deleteMaterialLotList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
                deleteMaterialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(hmeEoJobSnBatchVO8.getJobMaterialId()))
                        .collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(deleteMaterialLotList)) {
                deleteMaterialLotList.forEach(item -> {
                    item.setDeleteFlag(resultVO.getDeleteFlag());
                });
            }else{
                //当前扫描条码已绑定其它进站的SN
                HmeEoJobSnBatchVO6 deleteMaterialLot = new HmeEoJobSnBatchVO6();
                deleteMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                deleteMaterialLot.setMaterialType(hmeEoJobSnBatchVO8.getMaterialType());
                deleteMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                deleteMaterialLot.setDeleteFlag(hmeEoJobSnBatchVO8.getDeleteFlag());
                deleteMaterialLotList.add(deleteMaterialLot);
                resultVO.getComponent().setMaterialLotList(deleteMaterialLotList);
            }
            return resultVO;
        }
        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        if(Objects.isNull(mtModLocator)){
            //未查询到区域库位
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012",WmsConstant.ConstantValue.WMS, mtMaterialLot.getLocatorId()));
        }
        HmeEoJobSnBatchVO6 singleMaterialLot = new HmeEoJobSnBatchVO6();
        singleMaterialLot.setMaterialType(hmeEoJobSnBatchVO8.getMaterialType());
        singleMaterialLot.setWorkCellId(dto.getWorkcellId());
        singleMaterialLot.setMaterialId(mtMaterialLot.getMaterialId());
        singleMaterialLot.setIsReleased(HmeConstants.ConstantValue.ONE);
        singleMaterialLot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        singleMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        singleMaterialLot.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
        singleMaterialLot.setDeleteFlag(HmeConstants.ConstantValue.NO);
        singleMaterialLot.setLocatorId(mtMaterialLot.getLocatorId());
        singleMaterialLot.setLocatorCode(mtModLocator.getLocatorCode());
        singleMaterialLot.setLot(mtMaterialLot.getLot());
        singleMaterialLot.setSiteId(dto.getSiteId());
        singleMaterialLot.setEnableFlag(mtMaterialLot.getEnableFlag());
        singleMaterialLot.setFreezeFlag(mtMaterialLot.getFreezeFlag());
        singleMaterialLot.setStocktakeFlag(mtMaterialLot.getStocktakeFlag());
        List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
        boolean insertFlag = false;
        if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())){
                //新增
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setTenantId(tenantId);
                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobMaterial.setReleaseQty(BigDecimal.ONE);
                hmeEoJobMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobMaterial.setJobId(dto.getSnLineList().get(0).getJobId());
                hmeEoJobMaterial.setEoId(dto.getSnLineList().get(0).getEoId());
                hmeEoJobMaterial.setBomComponentId(hmeEoJobSnBatchVO8.getComponent().getBomComponentId());
                hmeEoJobMaterialRepository.insertSelective(hmeEoJobMaterial);
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                singleMaterialLot.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
                singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                insertFlag = true;
            }else{
                //更新
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
            //泵浦源工序作业平台独有的逻辑
            HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobPumpCombRepository.releaseScan(tenantId, hmeEoJobSnBatchVO8);
            resultVO.setPrintFlag(hmeEoJobSnBatchVO14.getPrintFlag());
            resultVO.setSubCode(hmeEoJobSnBatchVO14.getSubCode());
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())) {
                //新增
                hmeEoJobLotMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobLotMaterial.setTenantId(tenantId);
                hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobLotMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobLotMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobLotMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobLotMaterialRepository.insertSelective(hmeEoJobLotMaterial);
                singleMaterialLot.setJobMaterialId(hmeEoJobLotMaterial.getJobMaterialId());
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                insertFlag = true;
            }else{
                //更新
                hmeEoJobLotMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobLotMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
            String availableTime = hmeEoJobSnCommonService.getAvailableTime(tenantId,mtMaterialLot.getSiteId(),mtMaterialLot.getMaterialId());
            String deadLineDate = hmeEoJobSnCommonService.getDeadLineDate(tenantId,availableTime,mtMaterialLot.getMaterialLotId());
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())) {
                //新增
                hmeEoJobTimeMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobTimeMaterial.setTenantId(tenantId);
                hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobTimeMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobTimeMaterial.setWorkDate(new Date(System.currentTimeMillis()));
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                hmeEoJobTimeMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobTimeMaterialRepository.insertSelective(hmeEoJobTimeMaterial);
                singleMaterialLot.setJobMaterialId(hmeEoJobTimeMaterial.getJobMaterialId());
                singleMaterialLot.setDeadLineDate(deadLineDate);
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                insertFlag = true;
            }else{
                //更新
                hmeEoJobTimeMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                hmeEoJobTimeMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
        }
        if(insertFlag){
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
            }else {
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }else{
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                materialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(hmeEoJobSnBatchVO8.getJobMaterialId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(materialLotList)){
                    materialLotList.get(0).setIsReleased(HmeConstants.ConstantValue.ONE);
                }else{
                    singleMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                    if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())) {
                        singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                    }
                    resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
                }
            }else{
                singleMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())) {
                    singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                }
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }

        //条码排序
        if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
            List<HmeEoJobSnBatchVO6> materialLotList2 = resultVO.getComponent().getMaterialLotList().stream()
                    .sorted(Comparator.comparing(HmeEoJobSnBatchVO6::getCreationDate)).collect(Collectors.toList());
            resultVO.getComponent().setMaterialLotList(materialLotList2);
            materialLotList2.forEach(item -> item.setLineNumber(resultVO.getComponent().getLineNumber()));
        }

        if(StringUtils.isNotBlank(mtMaterialLot.getCurrentContainerId())){
            // 卸载容器
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(mtMaterialLot.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(mtMaterialLot.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }
        resultVO.setDeleteFlag(HmeConstants.ConstantValue.NO);
        return resultVO;
    }

    @Override
    public HmeEoJobSnBatchVO8 releaseScanValidate(Long tenantId, HmeEoJobSnBatchDTO2 dto) {
        HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = new HmeEoJobSnBatchVO8();
        if(CollectionUtils.isEmpty(dto.getSnLineList())){
            //请先选择SN条码
            throw new MtException("HME_EO_JOB_SN_127", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_127", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            // 扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        hmeEoJobSnBatchVO8.setMtMaterialLot(mtMaterialLot);
        if(Objects.isNull(mtMaterialLot)){
            //扫描条码不存在
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //条码${1}已失效,请检查!
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // 条码号【${1}】不为OK状态,请核实所录入条码
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点冻结标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())){
            // 物料批${1}已被冻结,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点停用标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            // 物料批${1}正在被盘点,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //查询物料类型
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());

        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(componentList)){
            // 请先查询投料组件清单
            throw new MtException("HME_EO_JOB_SN_128", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_128", "HME"));
        }
        hmeEoJobSnBatchVO8.setComponent(componentList.get(0));
        hmeEoJobSnBatchVO8.setMaterialType(materialType);
        if(HmeConstants.MaterialTypeCode.SN.equals(materialType)){

            if(dto.getSnLineList().size() != HmeConstants.ConstantValue.ONE){
                //选中多个SN时不能对序列号物料投料
                throw new MtException("HME_EO_JOB_SN_126", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_126", "HME"));
            }

            if(BigDecimal.ONE.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) != 0){
                //序列号物料条码数量必须是1
                throw new MtException("HME_EO_JOB_SN_125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_125", "HME"));
            }

            if (dto.getSnLineList().get(0).getSnNum().equals(dto.getMaterialLotCode())) {
                // 升级的序列号编码不可与进站序列号一致，请检查录入数据
                throw new MtException("HME_EO_JOB_SN_047", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_047", "HME"));
            }

            List<HmeEoJobSnBatchVO9> materialLotCodeList = hmeEoJobMaterialMapper.selectMaterialLotBindMaterialLot2(tenantId,dto.getMaterialLotCode());
            if(CollectionUtils.isNotEmpty(materialLotCodeList)){
                List<HmeEoJobSnBatchVO9> existJobMaterialList2 = materialLotCodeList.stream().filter(item -> !item.getJobId().equals(dto.getSnLineList().get(0).getJobId()))
                        .collect(Collectors.toList());
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                String jobMaterialId = null;
                String currBindSnNum = null;
                Date creationDate = null;
                if(CollectionUtils.isNotEmpty(existJobMaterialList2)){
                    jobMaterialId = existJobMaterialList2.get(0).getJobMaterialId();
                    creationDate = existJobMaterialList2.get(0).getCreationDate();
                    currBindSnNum = existJobMaterialList2.get(0).getMaterialLotCode();
                }else {
                    jobMaterialId = materialLotCodeList.get(0).getJobMaterialId();
                    creationDate = materialLotCodeList.get(0).getCreationDate();
                    currBindSnNum = materialLotCodeList.get(0).getMaterialLotCode();
                }
                hmeEoJobSnBatchVO8.setJobMaterialId(jobMaterialId);
                hmeEoJobSnBatchVO8.setCreationDate(creationDate);
                hmeEoJobSnBatchVO8.getComponent().setCurrBindSnNum(currBindSnNum);
            }else {
                HmeEoJobMaterial notBindJobMaterial = hmeEoJobMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getSnLineList().get(0).getJobId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobMaterial.getCreationDate());
                }
            }
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getSnLineList().get(0).getJobId());
            hmeEoJobSnBatchVO8.setHmeEoJobSn(hmeEoJobSn);
            //校验组合物料是否维护了筛选规则
            HmeEoJobPumpCombVO2 hmeEoJobPumpCombVO2 = hmeEoJobPumpCombMapper.pumpFilterRuleHeaderByMaterial(tenantId, hmeEoJobSn.getMaterialLotId());
            if(Objects.isNull(hmeEoJobPumpCombVO2)){
                throw new MtException("HME_EO_JOB_SN_220", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_220", "HME"));
            }
            hmeEoJobSnBatchVO8.setMaterialId(hmeEoJobPumpCombVO2.getMaterialId());
            hmeEoJobSnBatchVO8.setQty(hmeEoJobPumpCombVO2.getQty().longValue());
            //2021-09-07 17:15 add by chaonan.hu for wenxin.zhang 校验扫描的条码是否位于同一筛选组合
            hmeEoJobPumpCombRepository.samePumpSelectionComposeVerify(tenantId, hmeEoJobSn, mtMaterialLot);
            // 20211019 add by sanfeng.zhang for wenxin.zhang 校验采集项结果
            this.validateDataRecordResult(tenantId, dto, mtMaterialLot);
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobLotMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //当前条码已与工位【${1}】绑定
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                hmeEoJobSnBatchVO8.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
                hmeEoJobSnBatchVO8.setCreationDate(workcellCodeList.get(0).getCreationDate());
            }else{
                HmeEoJobLotMaterial notBindJobLotMaterial = hmeEoJobLotMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobLotMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobLotMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobLotMaterial.getCreationDate());
                }
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobTimeMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //当前条码已与工位【${1}】绑定
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                hmeEoJobSnBatchVO8.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
                hmeEoJobSnBatchVO8.setCreationDate(workcellCodeList.get(0).getCreationDate());
            }else{
                HmeEoJobTimeMaterial notBindJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobTimeMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobTimeMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobTimeMaterial.getCreationDate());
                }
            }
        }
        //如果走删除逻辑，直接返回
        if(StringUtils.isNotBlank(hmeEoJobSnBatchVO8.getDeleteFlag())){
            if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
                return hmeEoJobSnBatchVO8;
            }
        }

        //工位登陆时不允许绑定
        if(dto.getSnLineList().size() == 0 && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //请扫描${1}
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME","SN"));
        }

        //库位校验
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //V20210219 modify by penglin.sui for hui.ma 虚拟件-校验条码是否是预装条码
        if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(componentList.get(0).getVirtualFlag())){
            int count = hmeEoJobSnMapper.selectCountByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, mtMaterialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
            if(count != 1){
                //组件物料是虚拟件,投料条码必须是预装条码,请检查!
                throw new MtException("HME_EO_JOB_SN_185", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_185", "HME"));
            }
        }

        //查询BOM扩展属性
        List<String> bomComponentIdList = new ArrayList<>();
        bomComponentIdList.add(componentList.get(0).getBomComponentId());
        List<String> bomAttrNameList = new ArrayList<>();
        bomAttrNameList.add("lineAttribute11");
        bomAttrNameList.add("lineAttribute7");
        bomAttrNameList.add("lineAttribute8");
        List<MtExtendAttrVO1> bomExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID"
                ,bomComponentIdList,bomAttrNameList);
        Map<String,String> bomExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(bomExtendAttrVO1List)){
            bomExtendAttrMap = bomExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        //查询条码扩展属性
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(mtMaterialLot.getMaterialLotId());
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add("MATERIAL_VERSION");
        materialLotAttrNameList.add("SO_NUM");
        materialLotAttrNameList.add("SO_LINE_NUM");
        materialLotAttrNameList.add("MF_FLAG");
        materialLotAttrNameList.add("DEADLINE_DATE");
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        //查询工单扩展属性
        List<String> workOrderIdList = new ArrayList<>();
        workOrderIdList.add(dto.getSnLineList().get(0).getWorkOrderId());
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add("attribute1");
        woAttrNameList.add("attribute7");
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_work_order_attr","WORK_ORDER_ID"
                ,workOrderIdList,woAttrNameList);
        Map<String,String> woExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(woExtendAttrVO1List)){
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        String specialInvFlag = bomExtendAttrMap.getOrDefault("lineAttribute11","");

        if("E".equals(specialInvFlag)){
            //销售订单校验
            String woSoNum = woExtendAttrMap.getOrDefault("attribute1","");
            String woSoLineNum = woExtendAttrMap.getOrDefault("attribute7","");
            String materialLotSoNum = materialLotExtendAttrMap.getOrDefault("SO_NUM","");
            String materialLotSoLineNum = materialLotExtendAttrMap.getOrDefault("SO_LINE_NUM","");
            if(!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)){
                //工单销售订单【${1}】与条码销售订单【${2}】不匹配,请检查
                throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum,materialLotSoNum + "-" + materialLotSoLineNum));
            }
        }

        //在制品校验
        if(HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //当前物料仍为在制品,尚未加工完成,无法进行投料
            throw new MtException("HME_EO_JOB_SN_117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_117", "HME"));
        }

        //获取条码扩展表信息
        String productionVersion = materialLotExtendAttrMap.getOrDefault("MATERIAL_VERSION","");

        //获取组件扩展表信息
        String bomVersion = bomExtendAttrMap.getOrDefault("lineAttribute7","");

        if(StringUtils.isNotBlank(bomVersion)) {
            if (!productionVersion.equals(bomVersion)) {
                //条码物料版本【${1}】与组件需求版本【${2}】不一致
                throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_065", "HME", productionVersion, bomVersion));
            }
        }

        hmeEoJobSnBatchVO8.setProductionVersion(productionVersion);
        hmeEoJobSnBatchVO8.setVirtualFlag(bomExtendAttrMap.getOrDefault("lineAttribute8","N"));
        hmeEoJobSnBatchVO8.setDeadLineDate(materialLotExtendAttrMap.getOrDefault("DEADLINE_DATE",""));
        return hmeEoJobSnBatchVO8;
    }

    private void validateDataRecordResult (Long tenantId, HmeEoJobSnBatchDTO2 dto, MtMaterialLot mtMaterialLot) {
        //根据物料查询有效的泵浦源筛选规则行 过滤点计算类型,找不到则报错筛选规则没有维护筛选条件
        List<HmeEoJobPumpCombVO3> pumpCombVO3s = hmeEoJobPumpCombMapper.qureyPumpFilterRuleLineByMaterial(tenantId, dto.getSnLineList().get(0).getSnMaterialId());
        List<HmeEoJobPumpCombVO3> hmeEoJobPumpCombVO3List = pumpCombVO3s.stream().filter(comb -> !"CALCULATION".equals(comb.getCalculateType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hmeEoJobPumpCombVO3List)) {
            throw new MtException("HME_EO_JOB_SN_225", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_225", "HME"));
        }
        List<String> tagIdList = hmeEoJobPumpCombVO3List.stream().map(HmeEoJobPumpCombVO3::getTagId).distinct().collect(Collectors.toList());

        List<String> materialLotIdList = Collections.singletonList(mtMaterialLot.getMaterialLotId());
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "泵浦源物料批集合"));
        }
        //查询值集HME_PUMP_SOURCE_WKC下维护的值（即是工艺编码）,进而查询出工序下的工位
        List<LovValueDTO> processCodeLov = lovAdapter.queryLovValue("HME_PUMP_SOURCE_WKC", tenantId);
        if (CollectionUtils.isEmpty(processCodeLov)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "值集HME_PUMP_SOURCE_WKC"));
        }
        List<String> processCodeList = processCodeLov.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        List<String> workcellIdList = hmeEoJobPumpCombMapper.getWorkcellByOperation(tenantId, processCodeList);
        if (CollectionUtils.isEmpty(workcellIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "值集HME_PUMP_SOURCE_WKC下的工位"));
        }
        //根据workcell_id+泵浦源物料批查询hme_eo_job_sn的workcell_id+泵浦源物料批+jobId+工位的上层工序+最后更新时间
        Long startDate = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO4> hmeEoJobPumpCombVO4List = hmeEoJobPumpCombMapper.getJobId(tenantId, workcellIdList, materialLotIdList);
        log.info("=================================>getJobId耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobPumpCombVO4List)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> jobIdList = new ArrayList<>();
        //查询到的数据根据工序+物料批分组，每组下取更新时间最近的那笔jobId
        Map<String, List<HmeEoJobPumpCombVO4>> jobIdMap = hmeEoJobPumpCombVO4List.stream().collect(Collectors.groupingBy((item -> {
            return item.getProcessId() + "#" + item.getMaterialLotId();
        })));
        for (Map.Entry<String, List<HmeEoJobPumpCombVO4>> map : jobIdMap.entrySet()) {
            List<HmeEoJobPumpCombVO4> sortedValue = map.getValue().stream().sorted(Comparator.comparing(HmeEoJobPumpCombVO4::getLastUpdateDate).reversed()).collect(Collectors.toList());
            jobIdList.add(sortedValue.get(0).getJobId());
        }
        //根据jobId+tagId查询数据采集项
        Long startDate2 = System.currentTimeMillis();
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobPumpCombMapper.queryDataRecordByJobTag(tenantId, jobIdList, tagIdList);
        log.info("=================================>queryDataRecordByJobTag耗时："+(System.currentTimeMillis() - startDate2)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobDataRecordList)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> tagIdList2 = hmeEoJobDataRecordList.stream().map(HmeEoJobDataRecord::getTagId).distinct().collect(Collectors.toList());
        //如果之前找到的筛选规则行表的tagId集合和现在找到的数据采集项表的tagId集合不完全相同则报错
        if (!tagIdList.stream().sorted().collect(Collectors.joining())
                .equals(tagIdList2.stream().sorted().collect(Collectors.joining()))) {
            throw new MtException("HME_EO_JOB_SN_227", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_227", "HME"));
        }
        // 采集项结果没有值 进行报错
        Optional<HmeEoJobDataRecord> firstOpt = hmeEoJobDataRecordList.stream().filter(record -> StringUtils.isBlank(record.getResult())).findFirst();
        if (firstOpt.isPresent()) {
            MtTag mtTag = mtTagRepository.selectByPrimaryKey(firstOpt.get().getTagId());
            throw new MtException("HME_EO_JOB_SN_195", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_195", "HME", dto.getMaterialLotCode(), mtTag != null ? mtTag.getTagCode() : ""));
        }
        //2021-10-25 15:47 edit by choanan.hu for hui.gu 增加单个符合类型和多个符合类型校验
        //进行单个符合类型校验
        Long startDate4 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> singleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "SINGLE".equals(item.getCalculateType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(singleRuleLineList)){
            //只要找到的result符合一个单个符合类型的筛选规则行的最小值与最大值即可
            boolean singleErrorFlag = true;
            //单个符合类型的筛选规则行的tagid都是同一个
            String tagId = singleRuleLineList.get(0).getTagId();
            List<HmeEoJobDataRecord> singleHmeEoJobDataRecord = hmeEoJobDataRecordList.stream().filter(item -> tagId.equals(item.getTagId())).collect(Collectors.toList());
            String resultStr = singleHmeEoJobDataRecord.get(0).getResult();
            BigDecimal result = new BigDecimal(resultStr);
            for (HmeEoJobPumpCombVO3 singleRuleLine:singleRuleLineList) {
                BigDecimal maxValue = singleRuleLine.getMaxValue();
                BigDecimal minValue = singleRuleLine.getMinValue();
                if(Objects.nonNull(minValue) && Objects.nonNull(maxValue)){
                    if(result.compareTo(minValue) >= 0 && result.compareTo(maxValue) <= 0){
                        singleErrorFlag = false;
                        break;
                    }
                }else if(Objects.nonNull(minValue) && Objects.isNull(maxValue)){
                    if(result.compareTo(minValue) >= 0){
                        singleErrorFlag = false;
                        break;
                    }
                }else if(Objects.isNull(minValue) && Objects.nonNull(maxValue)){
                    if(result.compareTo(maxValue) <= 0){
                        singleErrorFlag = false;
                        break;
                    }
                }
            }
            if(singleErrorFlag){
                MtTag mtTag = mtTagRepository.selectByPrimaryKey(tagId);
                throw new MtException("HME_EO_JOB_SN_256", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_256", "HME", mtTag.getTagCode()));
            }
        }
        log.info("=================================>进行单个符合类型校验耗时："+(System.currentTimeMillis() - startDate4)+ "ms");
        //进行多个符合类型校验
        Long startDate6 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> multipleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "MULTIPLE".equals(item.getCalculateType())).collect(Collectors.toList());
        for (HmeEoJobPumpCombVO3 multipleRuleLine:multipleRuleLineList) {
            BigDecimal minValue = multipleRuleLine.getMinValue();
            BigDecimal maxValue = multipleRuleLine.getMaxValue();
            //根据多个符合类型的筛选规则行的tagId取到所有的result，必须每个result都位于当前遍历的筛选规则行的最小值与最大值之间
            List<String> multipleResultList = hmeEoJobDataRecordList.stream()
                    .filter(item -> multipleRuleLine.getTagId().equals(item.getTagId()))
                    .map(HmeEoJobDataRecord::getResult).collect(Collectors.toList());
            List<BigDecimal> resultBigDecimalList = new ArrayList<>();
            try{
                for (String result:multipleResultList) {
                    resultBigDecimalList.add(new BigDecimal(result));
                }
            }catch (Exception ex){
                throw new MtException("进行多个符合类型校验时,数据采集项"+ multipleRuleLine.getTagId()+"的结果不为数字");
            }
            for (BigDecimal resultBigDecimal:resultBigDecimalList) {
                if(Objects.nonNull(minValue)){
                    if(resultBigDecimal.compareTo(minValue) < 0){
                        //报错规则类型为“多个符合的”数据项【${1}】,结果【${2}】小于下限【${3}】。
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_254", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_254", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), minValue.toString()));
                    }
                }
                if(Objects.nonNull(maxValue)){
                    if(resultBigDecimal.compareTo(maxValue) > 0){
                        //报错规则类型为“多个符合的”数据项【${1}】,结果【${2}】大于上限【${3}】。
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_255", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_255", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), maxValue.toString()));
                    }
                }
            }
        }
        log.info("=================================>进行多个符合类型校验耗时："+(System.currentTimeMillis() - startDate6)+ "ms");
    }

    @Override
    public void subBarcodePrint(Long tenantId, HmeEoJobPumpCombDTO3 dto, HttpServletResponse response) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
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
        String barcodePath2 = "";
        String qrcodePath2 = "";
        String subCode = StringUtils.isBlank(dto.getSubCode()) ? "" : dto.getSubCode();
        String scanBarCode = StringUtils.isBlank(dto.getScanBarCode()) ? "" : dto.getScanBarCode();
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();
        // 生成条形码
        barcodePath = basePath + "/" + uuid + "_" + subCode + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        barcodePath2 = basePath + "/" + uuid + "_" + scanBarCode + "_barcode.png";
        File barcodeImageFile2 = new File(barcodePath2);
        barcodeImageFileList.add(barcodeImageFile2);
        try {
            CommonBarcodeUtil.generateCode128ToFile(subCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        try {
            CommonBarcodeUtil.generateCode128ToFile(scanBarCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile2, 10);
            log.info("<====生成条形码完成！{}", barcodePath2);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //生成二维码
        qrcodePath = basePath + "/" + uuid + "_" + subCode + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        qrcodeImageFileList.add(qrcodeImageFile);
        qrcodePath2 = basePath + "/" + uuid + "_" + scanBarCode + "_qrcode.png";
        File qrcodeImageFile2 = new File(qrcodePath2);
        qrcodeImageFileList.add(qrcodeImageFile2);
        try {
            CommonQRCodeUtil.encode(subCode, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        try {
            CommonQRCodeUtil.encode(scanBarCode, qrcodePath2, qrcodePath2, true);
            log.info("<====生成二维码完成！{}", qrcodePath2);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        imgMap.put("barcode1", barcodePath);
        imgMap.put("qrcode1", qrcodePath);
        imgMap.put("barcode2", barcodePath2);
        imgMap.put("qrcode2", qrcodePath2);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        formMap.put("sn1", subCode);
        formMap.put("sn2", scanBarCode);
        String versionNumber = StringUtils.isBlank(mtWorkOrder.getProductionVersion()) ? "" : mtWorkOrder.getProductionVersion();
        formMap.put("versionNumber", versionNumber);
        String materialCode = StringUtils.isBlank(dto.getSapMaterial()) ? "" : dto.getSapMaterial();
        formMap.put("combineNumber", materialCode);
        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        param.put("formMap", formMap);
        param.put("imgMap", imgMap);
        dataList.add(param);
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_scan_barcode_print_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generatePDFFile Error", e);
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
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.outputPDFFile Error", e);
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
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.closeIO Error", e);
            }
        }
        //删除临时文件
//        for (File file : barcodeImageFileList) {
//            if (!file.delete()) {
//                log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.deleteBarcodeImageFile Failed: {}", barcodePath);
//            }
//        }
//        for (File file : qrcodeImageFileList) {
//            if (!file.delete()) {
//                log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.qrcodeImageFileList Failed: {}", barcodePath);
//            }
//        }
//        if (!pdfFile.delete()) {
//            log.info("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.pdfFile Failed: {}", barcodePath);
//        }
    }

    @Override
    public HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        dto.setIsFirstProcess(HmeConstants.ConstantValue.YES);
        dto.setIsPumpProcess(HmeConstants.ConstantValue.YES);
        return hmeEoJobSnBatchService.releaseBack(tenantId , dto);
    }

    @Override
    public HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        dto.setIsPumpProcess(HmeConstants.ConstantValue.YES);
        return hmeEoJobSnSingleService.outSiteScan(tenantId, dto);
    }

    @Override
    public HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId, HmeEoJobSnBatchVO4 dto) {
        dto.setIsPumpProcess(HmeConstants.ConstantValue.YES);
        return hmeEoJobSnBatchService.deleteMaterial(tenantId, dto);
    }

    @Override
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getSnNum());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if(Objects.isNull(mtMaterialLot) || StringUtils.isBlank(mtMaterialLot.getMaterialLotId())){
            //扫描条码为空,请确认
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        //根据物料批ID在表hme_eo_job_sn取最新的进站时间（site_in_date）的进站记录
        String workcellId = hmeEoJobPumpCombMapper.getLastSiteInDateWorkcellIdBySn(tenantId, mtMaterialLot.getMaterialLotId());
        String message = null;
        if(StringUtils.isNotBlank(workcellId) && !workcellId.equals(dto.getWorkcellId())){
            //如果有记录且找到的工位ID与登录工位的ID不一致，则根据工单重新查询SN,将snNum赋值为新的SN,并弹窗消息
            HmeEoJobPumpCombDTO hmeEoJobPumpCombDTO = new HmeEoJobPumpCombDTO();
            hmeEoJobPumpCombDTO.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobPumpCombDTO.setWorkcellId(dto.getWorkcellId());
            HmeEoJobPumpCombVO hmeEoJobPumpCombVO = scanWorkOrder(tenantId, hmeEoJobPumpCombDTO);
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_257", "HME", dto.getSnNum(), mtModWorkcell.getWorkcellCode(), hmeEoJobPumpCombVO.getSnNum());
            dto.setSnNum(hmeEoJobPumpCombVO.getSnNum());
        }
        // 20211027 add by sanfeng.zhang for hui.gu 对条码进行枷锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("泵浦源工序作业平台");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.SN_NUM);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(dto.getSnNum());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        HmeEoJobSnVO hmeEoJobSnVO = new HmeEoJobSnVO();
        try {
            //调用原先的进站方法
            hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, dto);
            if(StringUtils.isNotBlank(message)){
                hmeEoJobSnVO.setMessage(message);
            }
        } catch (Exception e){
            throw new CommonException(e.getMessage());
        } finally {
            //解锁
            hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
        }
        return hmeEoJobSnVO;
    }

    /**
     * 对一个数组进行排列组合 如[1,2,3]得到[1,2,3] [1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]
     *
     * @param array 传入的数组
     * @param stack 栈对象
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 09:20:20
     * @return java.util.List<java.util.List<java.math.BigDecimal>>
     */
    public static void perm(BigDecimal[] array, Stack<BigDecimal> stack, List<List<BigDecimal>> permSingleResultBigList) {
        if(array.length <= 0) {
            //进入了叶子节点，输出栈中内容
            List<BigDecimal> singleResult = new LinkedList<>(stack);
            permSingleResultBigList.add(singleResult);
        } else {
            for (int i = 0; i < array.length; i++) {
                //tmepArray是一个临时数组，用于就是Ri
                //eg：1，2，3的全排列，先取出1，那么这时tempArray中就是2，3
                BigDecimal[] tempArray = new BigDecimal[array.length-1];
                System.arraycopy(array,0,tempArray,0,i);
                System.arraycopy(array,i+1,tempArray,i,array.length-i-1);
                stack.push(array[i]);
                perm(tempArray,stack, permSingleResultBigList);
                stack.pop();
            }
        }
    }
}
