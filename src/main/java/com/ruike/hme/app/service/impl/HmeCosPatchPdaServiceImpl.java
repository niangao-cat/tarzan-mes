package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeCosPatchPdaService;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeCosPatchPdaRepository;
import com.ruike.hme.domain.repository.HmeEoJobLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeCosPatchPdaMapper;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.OK;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.mdm.infra.constant.MdmConstant.LocatorCategory.INVENTORY;

/**
 * COS????????????????????????
 *
 * @author chaonan.hu@hand-china.com 2020-08-24 16:25:28
 **/
@Service
@Slf4j
public class HmeCosPatchPdaServiceImpl implements HmeCosPatchPdaService {

    @Autowired
    private HmeCosPatchPdaRepository hmeCosPatchPdaRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public HmeCosPatchPdaVO noSiteOutDataQuery(Long tenantId, HmeCosPatchPdaDTO7 dto) {
        return hmeCosPatchPdaRepository.noSiteOutDataQuery(tenantId, dto);
    }

    @Override
    public HmeCosPatchPdaVO10 scanBarcode(Long tenantId, HmeCosPatchPdaDTO dto) {
        return hmeCosPatchPdaRepository.scanBarcode(tenantId, dto);
    }

    @Override
    public List<HmeCosPatchPdaDTO2> delete(Long tenantId, List<HmeCosPatchPdaDTO2> dtoList) {
        hmeCosPatchPdaRepository.delete(tenantId, dtoList);
        return dtoList;
    }

    @Override
    public List<HmeCosPatchPdaDTO3> siteIn(Long tenantId, List<HmeCosPatchPdaDTO3> dtoList) {
        hmeCosPatchPdaRepository.siteIn(tenantId, dtoList);
        return dtoList;
    }

    @Override
    public HmeCosPatchPdaVO3 siteOut(Long tenantId, HmeCosPatchPdaDTO4 dto) {
        //2020-10-03 edit by chaonan.hu for zhenyong.ban ???????????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getWkcLineId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", dto.getWkcLineId()));
        }
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
            //?????????????????????????????????????????????{????????????????????????????????????????????????????????????}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
//        //2020-09-20 10:27 add by chaonan.hu for zhenyong.ban ?????????????????????????????????????????????
//        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
//        if(StringUtils.isEmpty(mtWorkOrder.getLocatorId())){
//            throw new MtException("HME_EO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_011", "HME"));
//        }
//        //2020-09-22 15:46 add by chaonan.hu for zhenyong.ban ??????????????????????????????????????????????????????
//        List<MtModLocator> subLocatorList = mtModLocatorRepository.select(new MtModLocator() {{
//            setTenantId(tenantId);
//            setParentLocatorId(mtWorkOrder.getLocatorId());
//            setLocatorType("DEFAULT_STORAGE");
//        }});
//        if(CollectionUtils.isEmpty(subLocatorList)){
//            throw new MtException("HME_EO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_011", "HME"));
//        }
        if (StringUtils.isEmpty(dto.getLot())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????"));
        }
        return hmeCosPatchPdaRepository.siteOut(tenantId, dto, defaultStorageLocatorIdList.get(0));
    }

    @Override
    public HmeCosPatchPdaVO3 query(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        return hmeCosPatchPdaRepository.query(tenantId, dto);
    }

    @Override
    public List<String> print(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        if (CollectionUtils.isEmpty(dto.getJobIdList())) {
            throw new MtException("HME_COS_PATCH_PDA_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0007", "HME"));
        }
        return hmeCosPatchPdaRepository.print(tenantId, dto);
    }

    @Override
    public void printPdf(Long tenantId, List<String> materialLotIds, HttpServletResponse response) {
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
                    throw new MtException("make-dir-failed", "???????????????????????????!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }

        //??????API???????????????????????????
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "???????????????"));
        }
        String uuid = UUID.randomUUID().toString();

        //????????????????????????
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<File> imageFileList = new ArrayList<>();
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        // ???????????????????????????
        Map<String, MtWorkOrder> barcodeWorkOrderMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            // ????????????????????????????????????
            MtExtendVO1 dto = new MtExtendVO1();
            dto.setKeyIdList(materialLotIds);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("WORK_ORDER_ID");
            dto.setAttrs(Collections.singletonList(mtExtendVO5));
            dto.setTableName("mt_material_lot_attr");
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, dto);

            Map<String, MtWorkOrder> mtWorkOrderMap = new HashMap<>();
            List<String> workOrderIds = attrVO1List.stream().map(MtExtendAttrVO1::getAttrValue).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(workOrderIds)) {
                List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                        .select(MtWorkOrder.FIELD_WORK_ORDER_ID, MtWorkOrder.FIELD_WORK_ORDER_NUM).andWhere(Sqls.custom()
                                .andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                                .andIn(MtWorkOrder.FIELD_WORK_ORDER_ID, workOrderIds)).build());
                mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, Function.identity()));
            }

            Map<String, MtWorkOrder> finalMtWorkOrderMap = mtWorkOrderMap;
            attrVO1List.stream().forEach(attr -> {
                if (StringUtils.isNotBlank(attr.getAttrValue())) {
                    MtWorkOrder mtWorkOrder = finalMtWorkOrderMap.getOrDefault(attr.getAttrValue(), new MtWorkOrder());
                    barcodeWorkOrderMap.put(attr.getKeyId(), mtWorkOrder);
                }
            });
        }
        for (int i = 0; i < mtMaterialLotList.size(); i++) {
            MtMaterialLot mtMaterialLot = mtMaterialLotList.get(i);
            MtWorkOrder mtWorkOrder = barcodeWorkOrderMap.getOrDefault(mtMaterialLot.getMaterialLotId(), new MtWorkOrder());
            String workOrderNum = mtWorkOrder.getWorkOrderNum();

            //???????????????
            String barcodePath = basePath + "/" + uuid + "_" + mtMaterialLot.getMaterialLotId() + ".png";
            File imageFile = new File(barcodePath);
            try {
                CommonBarcodeUtil.generateCode128ToFile(mtMaterialLot.getMaterialLotCode(), CommonBarcodeUtil.IMG_TYPE_PNG, imageFile, 10);
                log.info("<====????????????????????????{}", barcodePath);

                //??????????????????
                //CommonBarcodeUtil.repaintPictureToCenter(imageFile, 387, 64);
            } catch (Exception e) {
                log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.generateImageFile Error", e);
                throw new MtException("Exception", e.getMessage());
            }
            int num = i % 2;
            //????????????
            imgMap.put("barcodeImage" + num, barcodePath);
            formMap.put("barcode" + num, mtMaterialLot.getMaterialLotCode());
            formMap.put("workOrderNum" + num, workOrderNum);
            if (num == 1 || i == mtMaterialLotList.size() - 1) {
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            }
            imageFileList.add(imageFile);
        }

        //??????PDF
        String pdfPath = basePath + "/" + uuid + ".pdf";
        try {
            log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList);
            CommonPdfTemplateUtil.multiplePage(basePath + "/cos_tp_print_template.pdf", pdfPath, dataList);
            log.info("<==== ??????PDF?????????{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.generatePDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
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
            log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.outputPDFFile Error", e);
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
                log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.closeIO Error", e);
            }
        }

        //??????????????????
        for (File imageFile : imageFileList) {
            if (!imageFile.delete()) {
                log.info("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.deleteImageFile Failed: {}", imageFile);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.deletePDFFile Failed: {}", pdfPath);
        }
    }

    @Override
    public HmeCosPatchPdaVO4 bandingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto) {
        if (StringUtils.isEmpty(dto.getWkcLineId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", dto.getWkcLineId()));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        String operationId = dto.getOperationIdList().get(0);
        HmeCosPatchPdaVO4 result = new HmeCosPatchPdaVO4();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        //????????????????????????
        if (mtMaterialLot == null || !YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", dto.getMaterialLotCode()));
        }
        //?????????????????????OK
        if (!OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("HME_TIME_PROCESS_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0005", "HME", mtMaterialLot.getQualityStatus()));
        }
        //??????+????????????????????????????????????????????????????????????
        HmeEoJobLotMaterial hmeEoJobLotMaterial = hmeEoJobLotMaterialRepository.selectOne(new HmeEoJobLotMaterial() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getWorkcellId());
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }});
        if (hmeEoJobLotMaterial != null) {
            result.setFlag(1L);
            return result;
        }
        //2020-09-25 add by chaonan.hu for zhenyong.ban
        //??????????????????????????????????????????????????????????????????INVENTORY
        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        if (StringUtils.isNotEmpty(mtMaterialLot.getLocatorId())) {
            if (!INVENTORY.equals(mtModLocator.getLocatorCategory())) {
                throw new MtException("HME_COS_PATCH_PDA_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0013", "HME", mtModLocator.getLocatorCode()));
            }
        } else {
            throw new MtException("HME_COS_PATCH_PDA_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0014", "HME"));
        }
        //2020-10-03 09:56 edit by chaonan.hu for zhenyong.ban ????????????????????????????????????????????????????????????????????????
        //????????????{??????????????????${1}????????????????????????????????????????????????,?????????}
//        //???????????????????????????????????????????????????
//        String issuedLocatorId = hmeCosPatchPdaMapper.getIssuedLocatorId(tenantId, dto.getWorkOrderId());
//        if(!mtMaterialLot.getLocatorId().equals(issuedLocatorId)){
//            throw new MtException("HME_COS_PATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_COS_PATCH_0001", "HME",mtMaterialLot.getLocatorId(),issuedLocatorId));
//        }
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
            //?????????????????????????????????????????????{????????????????????????????????????????????????????????????}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
        if (!defaultStorageLocatorIdList.contains(mtMaterialLot.getLocatorId())) {
            //{??????????????????${1}????????????????????????????????????????????????,?????????}
            throw new MtException("HME_EO_JOB_SN_089", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_089", "HME", mtModLocator.getLocatorCode()));
        }

        //????????????????????????????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        String routerOperationId = null;
        MtBomComponent mtBomComponent2 = null;
        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            //??????ROUTER_ID?????????ID?????????????????????mt_router_step???
            //??????????????????????????????mt_router_operation?????????routerStepId??????????????????ID
            List<MtRouterOperation> mtRouterOperations = hmeCosPatchPdaMapper.routerOperationQuery(tenantId, mtWorkOrder.getRouterId());
            for (MtRouterOperation mtRouterOperation : mtRouterOperations) {
                if (operationId.equals(mtRouterOperation.getOperationId())) {
                    routerOperationId = mtRouterOperation.getRouterOperationId();
                    break;
                }
            }
            if (StringUtils.isNotEmpty(routerOperationId)) {
                //??????BOM_COMPONENT_ID
                List<MtBomComponent> mtBomComponents = hmeCosPatchPdaMapper.bomComponentQuery(tenantId, routerOperationId);
                for (MtBomComponent mtBomComponent : mtBomComponents) {
                    //2020-10-15 edit by zhenyong.ban ??????BOM_COMPONENT_ID??????????????????????????????????????????????????????????????????
                    List<String> materialIdList = new ArrayList<>();
                    materialIdList.add(mtBomComponent.getMaterialId());
                    //??????BOM??????ID?????????????????????
                    List<String> woSubstituteMaterialList = hmeCosPatchPdaMapper.getWorkOrderSubstituteMaterial(tenantId, mtBomComponent.getBomComponentId());
                    materialIdList.addAll(woSubstituteMaterialList);
                    //2020-12-03 edit by chaonan.hu for zhenyong.ban ?????????????????????????????????
                    //??????BOM????????????ID+??????ID?????????????????????
                    List<String> globalSubstituteMaterialList = hmeCosPatchPdaMapper.getGlobalSubstituteMaterial(tenantId, mtBomComponent.getMaterialId(), mtMaterialLot.getSiteId());
                    if(globalSubstituteMaterialList.contains(mtBomComponent.getMaterialId()) && mtBomComponent.getQty() > 0){
                        //??????Bom?????????????????????????????????????????????????????????Bom??????????????????0????????????????????????????????????
                        globalSubstituteMaterialList.remove(mtBomComponent.getMaterialId());
                    }
                    materialIdList.addAll(globalSubstituteMaterialList);
                    if(materialIdList.contains(mtMaterialLot.getMaterialId())){
                        //?????????????????????????????????????????????
                        if(woSubstituteMaterialList.contains(mtMaterialLot.getMaterialId()) &&
                                globalSubstituteMaterialList.contains(mtMaterialLot.getMaterialId())){
                            //????????????????????????????????????????????????????????????????????????{??????????????????????????????????????????????????????}
                            throw new MtException("HME_COS_PATCH_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_COS_PATCH_0005", "HME", mtMaterialLot.getMaterialLotCode()));
                        }
                        mtBomComponent2 = mtBomComponentRepository.selectByPrimaryKey(mtBomComponent.getBomComponentId());
                        break;
                    }
                }
            }
        }
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        if (mtBomComponent2 == null) {
            throw new MtException("HME_COS_PATCH_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_0002", "HME", mtMaterial.getMaterialCode()));
        }
        //??????????????????????????????
        hmeCosPatchPdaRepository.bandingWorkcell(tenantId, dto, mtMaterialLot.getMaterialId(),
                mtMaterialLot.getMaterialLotId(), mtBomComponent2.getQty());
        //??????????????????
        result.setFlag(2L);
        result.setMaterialId(mtMaterialLot.getMaterialId());
        result.setMaterialCode(mtMaterial.getMaterialCode());
        result.setMaterialName(mtMaterial.getMaterialName());
        result.setQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
        result.setUomCode(mtUom.getUomCode());
        result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        result.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        return result;
    }

    @Override
    public HmeCosPatchPdaDTO6 unBindingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto) {
        return hmeCosPatchPdaRepository.unBindingWorkcell(tenantId, dto);
    }

    @Override
    public List<HmeCosPatchPdaVO4> bandingMaterialQuery(Long tenantId, String workcellId) {
        return hmeCosPatchPdaRepository.bandingMaterialQuery(tenantId, workcellId);
    }

    @Override
    public HmeCosPatchPdaVO3 materialLotRecall(Long tenantId, HmeCosPatchPdaVO3 dto) {
        List<HmeCosPatchPdaVO2> materialLotCodeList = dto.getMaterialLotCodeList();
        for (HmeCosPatchPdaVO2 hmeCosPatchPdaVO2:materialLotCodeList) {
            //?????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeCosPatchPdaVO2.getMaterialLotId());
            if(!"Y".equals(mtMaterialLot.getEnableFlag())){
                throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_001", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            //?????????????????????
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(hmeCosPatchPdaVO2.getJobId());
            if(Objects.isNull(hmeEoJobSn) || Objects.nonNull(hmeEoJobSn.getSiteOutDate())){
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", mtMaterialLot.getMaterialLotCode()));
            }
        }
        hmeCosPatchPdaRepository.materialLotRecall(tenantId, materialLotCodeList);
        return dto;
    }

    @Override
    public HmeCosPatchPdaDTO5 printRecall(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        return hmeCosPatchPdaRepository.printRecall(tenantId, dto);
    }
}
