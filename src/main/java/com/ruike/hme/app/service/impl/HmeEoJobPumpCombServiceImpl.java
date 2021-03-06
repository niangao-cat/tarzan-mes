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
 * ????????????????????????????????????????????????
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
                    "HME_NC_0006", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getWorkOrderId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //????????????????????????????????????????????????
        Long filterRuleHeaderCount = hmeEoJobPumpCombMapper.countPumpFilterRuleHeaderByMaterial(tenantId, mtWorkOrder.getMaterialId());
        if(filterRuleHeaderCount == 0){
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
            throw new MtException("HME_EO_JOB_SN_210", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_210", "HME", mtMaterial.getMaterialCode()));
        }
        //??????????????????SN
        String snNum = hmeEoJobPumpCombRepository.getSnByWorkOrder(tenantId, mtWorkOrder, dto.getWorkcellId());
        if(StringUtils.isBlank(snNum)){
            //????????????SN??????????????????????????????SN
            throw new MtException("HME_EO_JOB_SN_211", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_211", "HME", mtWorkOrder.getWorkOrderNum()));
        }
        //????????????
        HmeEoJobPumpCombVO result = new HmeEoJobPumpCombVO();
        result.setSnNum(snNum);
        return result;
    }

    @Override
    public void siteInPrint(Long tenantId, HmeEoJobPumpCombDTO2 dto, HttpServletResponse response) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("???????????????????????????!");
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
        // ???????????????
        barcodePath = basePath + "/" + uuid + "_" + snNum + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(snNum, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====????????????????????????{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //???????????????
        qrcodePath = basePath + "/" + uuid + "_" + snNum + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        qrcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(snNum, qrcodePath, qrcodePath, true);
            log.info("<====????????????????????????{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //????????????
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
            //??????PDF
            try {
                log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_sitein_print_template.pdf", pdfPath, dataList);
                log.info("<==== ??????PDF?????????{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
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
        //??????????????????
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
        //??????
        HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = this.releaseScanValidate(tenantId,dto);
        resultVO.setComponent(hmeEoJobSnBatchVO8.getComponent());
        MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //2021-11-09 14:20 edit by chaonan.hu for wenxin.zhang ???????????????ID?????????hme_eo_job_pump_comb????????????
            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.eoJobPumpCombQueryByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if(Objects.nonNull(hmeEoJobPumpComb)){
                //????????????????????????????????????snLineList???materialLotId????????????????????????CombMaterialLotId?????????
                if(CollectionUtils.isNotEmpty(dto.getSnLineList())
                        && !hmeEoJobPumpComb.getCombMaterialLotId().equals(dto.getSnLineList().get(0).getMaterialLotId())){
                    //???????????????????????????SN??????${1}???
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
                //??????????????????????????????????????????SN
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
            //????????????????????????
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
                //??????
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
                //??????
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
            //??????????????????????????????????????????
            HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobPumpCombRepository.releaseScan(tenantId, hmeEoJobSnBatchVO8);
            resultVO.setPrintFlag(hmeEoJobSnBatchVO14.getPrintFlag());
            resultVO.setSubCode(hmeEoJobSnBatchVO14.getSubCode());
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())) {
                //??????
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
                //??????
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
                //??????
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
                //??????
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

        //????????????
        if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
            List<HmeEoJobSnBatchVO6> materialLotList2 = resultVO.getComponent().getMaterialLotList().stream()
                    .sorted(Comparator.comparing(HmeEoJobSnBatchVO6::getCreationDate)).collect(Collectors.toList());
            resultVO.getComponent().setMaterialLotList(materialLotList2);
            materialLotList2.forEach(item -> item.setLineNumber(resultVO.getComponent().getLineNumber()));
        }

        if(StringUtils.isNotBlank(mtMaterialLot.getCurrentContainerId())){
            // ????????????
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
            //????????????SN??????
            throw new MtException("HME_EO_JOB_SN_127", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_127", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            // ??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        hmeEoJobSnBatchVO8.setMtMaterialLot(mtMaterialLot);
        if(Objects.isNull(mtMaterialLot)){
            //?????????????????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //??????${1}?????????,?????????!
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // ????????????${1}?????????OK??????,????????????????????????
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma ??????????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())){
            // ?????????${1}????????????,??????????????????????????????!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma ??????????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            // ?????????${1}???????????????,??????????????????????????????!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //??????????????????
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());

        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(componentList)){
            // ??????????????????????????????
            throw new MtException("HME_EO_JOB_SN_128", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_128", "HME"));
        }
        hmeEoJobSnBatchVO8.setComponent(componentList.get(0));
        hmeEoJobSnBatchVO8.setMaterialType(materialType);
        if(HmeConstants.MaterialTypeCode.SN.equals(materialType)){

            if(dto.getSnLineList().size() != HmeConstants.ConstantValue.ONE){
                //????????????SN?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_126", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_126", "HME"));
            }

            if(BigDecimal.ONE.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) != 0){
                //????????????????????????????????????1
                throw new MtException("HME_EO_JOB_SN_125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_125", "HME"));
            }

            if (dto.getSnLineList().get(0).getSnNum().equals(dto.getMaterialLotCode())) {
                // ??????????????????????????????????????????????????????????????????????????????
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
            //?????????????????????????????????????????????
            HmeEoJobPumpCombVO2 hmeEoJobPumpCombVO2 = hmeEoJobPumpCombMapper.pumpFilterRuleHeaderByMaterial(tenantId, hmeEoJobSn.getMaterialLotId());
            if(Objects.isNull(hmeEoJobPumpCombVO2)){
                throw new MtException("HME_EO_JOB_SN_220", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_220", "HME"));
            }
            hmeEoJobSnBatchVO8.setMaterialId(hmeEoJobPumpCombVO2.getMaterialId());
            hmeEoJobSnBatchVO8.setQty(hmeEoJobPumpCombVO2.getQty().longValue());
            //2021-09-07 17:15 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????
            hmeEoJobPumpCombRepository.samePumpSelectionComposeVerify(tenantId, hmeEoJobSn, mtMaterialLot);
            // 20211019 add by sanfeng.zhang for wenxin.zhang ?????????????????????
            this.validateDataRecordResult(tenantId, dto, mtMaterialLot);
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobLotMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //???????????????????????????${1}?????????
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
                    //???????????????????????????${1}?????????
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
        //????????????????????????????????????
        if(StringUtils.isNotBlank(hmeEoJobSnBatchVO8.getDeleteFlag())){
            if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
                return hmeEoJobSnBatchVO8;
            }
        }

        //??????????????????????????????
        if(dto.getSnLineList().size() == 0 && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //?????????${1}
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME","SN"));
        }

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //V20210219 modify by penglin.sui for hui.ma ?????????-?????????????????????????????????
        if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(componentList.get(0).getVirtualFlag())){
            int count = hmeEoJobSnMapper.selectCountByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, mtMaterialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
            if(count != 1){
                //????????????????????????,?????????????????????????????????,?????????!
                throw new MtException("HME_EO_JOB_SN_185", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_185", "HME"));
            }
        }

        //??????BOM????????????
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

        //????????????????????????
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

        //????????????????????????
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
            //??????????????????
            String woSoNum = woExtendAttrMap.getOrDefault("attribute1","");
            String woSoLineNum = woExtendAttrMap.getOrDefault("attribute7","");
            String materialLotSoNum = materialLotExtendAttrMap.getOrDefault("SO_NUM","");
            String materialLotSoLineNum = materialLotExtendAttrMap.getOrDefault("SO_LINE_NUM","");
            if(!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)){
                //?????????????????????${1}???????????????????????????${2}????????????,?????????
                throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum,materialLotSoNum + "-" + materialLotSoLineNum));
            }
        }

        //???????????????
        if(HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //???????????????????????????,??????????????????,??????????????????
            throw new MtException("HME_EO_JOB_SN_117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_117", "HME"));
        }

        //???????????????????????????
        String productionVersion = materialLotExtendAttrMap.getOrDefault("MATERIAL_VERSION","");

        //???????????????????????????
        String bomVersion = bomExtendAttrMap.getOrDefault("lineAttribute7","");

        if(StringUtils.isNotBlank(bomVersion)) {
            if (!productionVersion.equals(bomVersion)) {
                //?????????????????????${1}???????????????????????????${2}????????????
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
        //??????????????????????????????????????????????????? ?????????????????????,??????????????????????????????????????????????????????
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
                    "HME_NC_0006", "HME", "????????????????????????"));
        }
        //????????????HME_PUMP_SOURCE_WKC???????????????????????????????????????,?????????????????????????????????
        List<LovValueDTO> processCodeLov = lovAdapter.queryLovValue("HME_PUMP_SOURCE_WKC", tenantId);
        if (CollectionUtils.isEmpty(processCodeLov)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????HME_PUMP_SOURCE_WKC"));
        }
        List<String> processCodeList = processCodeLov.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        List<String> workcellIdList = hmeEoJobPumpCombMapper.getWorkcellByOperation(tenantId, processCodeList);
        if (CollectionUtils.isEmpty(workcellIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????HME_PUMP_SOURCE_WKC????????????"));
        }
        //??????workcell_id+????????????????????????hme_eo_job_sn???workcell_id+??????????????????+jobId+?????????????????????+??????????????????
        Long startDate = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO4> hmeEoJobPumpCombVO4List = hmeEoJobPumpCombMapper.getJobId(tenantId, workcellIdList, materialLotIdList);
        log.info("=================================>getJobId?????????"+(System.currentTimeMillis() - startDate)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobPumpCombVO4List)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> jobIdList = new ArrayList<>();
        //??????????????????????????????+?????????????????????????????????????????????????????????jobId
        Map<String, List<HmeEoJobPumpCombVO4>> jobIdMap = hmeEoJobPumpCombVO4List.stream().collect(Collectors.groupingBy((item -> {
            return item.getProcessId() + "#" + item.getMaterialLotId();
        })));
        for (Map.Entry<String, List<HmeEoJobPumpCombVO4>> map : jobIdMap.entrySet()) {
            List<HmeEoJobPumpCombVO4> sortedValue = map.getValue().stream().sorted(Comparator.comparing(HmeEoJobPumpCombVO4::getLastUpdateDate).reversed()).collect(Collectors.toList());
            jobIdList.add(sortedValue.get(0).getJobId());
        }
        //??????jobId+tagId?????????????????????
        Long startDate2 = System.currentTimeMillis();
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobPumpCombMapper.queryDataRecordByJobTag(tenantId, jobIdList, tagIdList);
        log.info("=================================>queryDataRecordByJobTag?????????"+(System.currentTimeMillis() - startDate2)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobDataRecordList)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> tagIdList2 = hmeEoJobDataRecordList.stream().map(HmeEoJobDataRecord::getTagId).distinct().collect(Collectors.toList());
        //??????????????????????????????????????????tagId?????????????????????????????????????????????tagId??????????????????????????????
        if (!tagIdList.stream().sorted().collect(Collectors.joining())
                .equals(tagIdList2.stream().sorted().collect(Collectors.joining()))) {
            throw new MtException("HME_EO_JOB_SN_227", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_227", "HME"));
        }
        // ???????????????????????? ????????????
        Optional<HmeEoJobDataRecord> firstOpt = hmeEoJobDataRecordList.stream().filter(record -> StringUtils.isBlank(record.getResult())).findFirst();
        if (firstOpt.isPresent()) {
            MtTag mtTag = mtTagRepository.selectByPrimaryKey(firstOpt.get().getTagId());
            throw new MtException("HME_EO_JOB_SN_195", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_195", "HME", dto.getMaterialLotCode(), mtTag != null ? mtTag.getTagCode() : ""));
        }
        //2021-10-25 15:47 edit by choanan.hu for hui.gu ???????????????????????????????????????????????????
        //??????????????????????????????
        Long startDate4 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> singleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "SINGLE".equals(item.getCalculateType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(singleRuleLineList)){
            //???????????????result??????????????????????????????????????????????????????????????????????????????
            boolean singleErrorFlag = true;
            //???????????????????????????????????????tagid???????????????
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
        log.info("=================================>???????????????????????????????????????"+(System.currentTimeMillis() - startDate4)+ "ms");
        //??????????????????????????????
        Long startDate6 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> multipleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "MULTIPLE".equals(item.getCalculateType())).collect(Collectors.toList());
        for (HmeEoJobPumpCombVO3 multipleRuleLine:multipleRuleLineList) {
            BigDecimal minValue = multipleRuleLine.getMinValue();
            BigDecimal maxValue = multipleRuleLine.getMaxValue();
            //?????????????????????????????????????????????tagId???????????????result???????????????result?????????????????????????????????????????????????????????????????????
            List<String> multipleResultList = hmeEoJobDataRecordList.stream()
                    .filter(item -> multipleRuleLine.getTagId().equals(item.getTagId()))
                    .map(HmeEoJobDataRecord::getResult).collect(Collectors.toList());
            List<BigDecimal> resultBigDecimalList = new ArrayList<>();
            try{
                for (String result:multipleResultList) {
                    resultBigDecimalList.add(new BigDecimal(result));
                }
            }catch (Exception ex){
                throw new MtException("?????????????????????????????????,???????????????"+ multipleRuleLine.getTagId()+"?????????????????????");
            }
            for (BigDecimal resultBigDecimal:resultBigDecimalList) {
                if(Objects.nonNull(minValue)){
                    if(resultBigDecimal.compareTo(minValue) < 0){
                        //??????????????????????????????????????????????????????${1}???,?????????${2}??????????????????${3}??????
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_254", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_254", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), minValue.toString()));
                    }
                }
                if(Objects.nonNull(maxValue)){
                    if(resultBigDecimal.compareTo(maxValue) > 0){
                        //??????????????????????????????????????????????????????${1}???,?????????${2}??????????????????${3}??????
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_255", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_255", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), maxValue.toString()));
                    }
                }
            }
        }
        log.info("=================================>???????????????????????????????????????"+(System.currentTimeMillis() - startDate6)+ "ms");
    }

    @Override
    public void subBarcodePrint(Long tenantId, HmeEoJobPumpCombDTO3 dto, HttpServletResponse response) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("???????????????????????????!");
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
        // ???????????????
        barcodePath = basePath + "/" + uuid + "_" + subCode + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        barcodePath2 = basePath + "/" + uuid + "_" + scanBarCode + "_barcode.png";
        File barcodeImageFile2 = new File(barcodePath2);
        barcodeImageFileList.add(barcodeImageFile2);
        try {
            CommonBarcodeUtil.generateCode128ToFile(subCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====????????????????????????{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        try {
            CommonBarcodeUtil.generateCode128ToFile(scanBarCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile2, 10);
            log.info("<====????????????????????????{}", barcodePath2);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }
        //???????????????
        qrcodePath = basePath + "/" + uuid + "_" + subCode + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        qrcodeImageFileList.add(qrcodeImageFile);
        qrcodePath2 = basePath + "/" + uuid + "_" + scanBarCode + "_qrcode.png";
        File qrcodeImageFile2 = new File(qrcodePath2);
        qrcodeImageFileList.add(qrcodeImageFile2);
        try {
            CommonQRCodeUtil.encode(subCode, qrcodePath, qrcodePath, true);
            log.info("<====????????????????????????{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        try {
            CommonQRCodeUtil.encode(scanBarCode, qrcodePath2, qrcodePath2, true);
            log.info("<====????????????????????????{}", qrcodePath2);
        } catch (Exception e) {
            log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        //????????????
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
            //??????PDF
            try {
                log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_scan_barcode_print_template.pdf", pdfPath, dataList);
                log.info("<==== ??????PDF?????????{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEoJobPumpCombServiceImpl.siteInPrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
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
        //??????????????????
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
        // ????????????????????????
        if (StringUtils.isBlank(dto.getSnNum())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getSnNum());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if(Objects.isNull(mtMaterialLot) || StringUtils.isBlank(mtMaterialLot.getMaterialLotId())){
            //??????????????????,?????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        //???????????????ID??????hme_eo_job_sn???????????????????????????site_in_date??????????????????
        String workcellId = hmeEoJobPumpCombMapper.getLastSiteInDateWorkcellIdBySn(tenantId, mtMaterialLot.getMaterialLotId());
        String message = null;
        if(StringUtils.isNotBlank(workcellId) && !workcellId.equals(dto.getWorkcellId())){
            //?????????????????????????????????ID??????????????????ID???????????????????????????????????????SN,???snNum???????????????SN,???????????????
            HmeEoJobPumpCombDTO hmeEoJobPumpCombDTO = new HmeEoJobPumpCombDTO();
            hmeEoJobPumpCombDTO.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobPumpCombDTO.setWorkcellId(dto.getWorkcellId());
            HmeEoJobPumpCombVO hmeEoJobPumpCombVO = scanWorkOrder(tenantId, hmeEoJobPumpCombDTO);
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_257", "HME", dto.getSnNum(), mtModWorkcell.getWorkcellCode(), hmeEoJobPumpCombVO.getSnNum());
            dto.setSnNum(hmeEoJobPumpCombVO.getSnNum());
        }
        // 20211027 add by sanfeng.zhang for hui.gu ?????????????????????
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("???????????????????????????");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.SN_NUM);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(dto.getSnNum());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //??????
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        HmeEoJobSnVO hmeEoJobSnVO = new HmeEoJobSnVO();
        try {
            //???????????????????????????
            hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, dto);
            if(StringUtils.isNotBlank(message)){
                hmeEoJobSnVO.setMessage(message);
            }
        } catch (Exception e){
            throw new CommonException(e.getMessage());
        } finally {
            //??????
            hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
        }
        return hmeEoJobSnVO;
    }

    /**
     * ????????????????????????????????? ???[1,2,3]??????[1,2,3] [1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]
     *
     * @param array ???????????????
     * @param stack ?????????
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 09:20:20
     * @return java.util.List<java.util.List<java.math.BigDecimal>>
     */
    public static void perm(BigDecimal[] array, Stack<BigDecimal> stack, List<List<BigDecimal>> permSingleResultBigList) {
        if(array.length <= 0) {
            //??????????????????????????????????????????
            List<BigDecimal> singleResult = new LinkedList<>(stack);
            permSingleResultBigList.add(singleResult);
        } else {
            for (int i = 0; i < array.length; i++) {
                //tmepArray????????????????????????????????????Ri
                //eg???1???2???3????????????????????????1???????????????tempArray?????????2???3
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
