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
 * COS贴片平台应用服务
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
        //2020-10-03 edit by chaonan.hu for zhenyong.ban 注释掉工单上的默认完工库位下的子库位的校验
        //增加当前工位的工段或产线下维护的默认存储库位的校验
        if (StringUtils.isEmpty(dto.getWkcLineId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", dto.getWkcLineId()));
        }
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
            //如果未找到默认存储库位，则报错{当前工位的工段或产线下未维护默认存储库位}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
//        //2020-09-20 10:27 add by chaonan.hu for zhenyong.ban 增加工单完工库位是否有值的校验
//        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
//        if(StringUtils.isEmpty(mtWorkOrder.getLocatorId())){
//            throw new MtException("HME_EO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_011", "HME"));
//        }
//        //2020-09-22 15:46 add by chaonan.hu for zhenyong.ban 增加根据工单完工库位查询子库位的校验
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
                    "HME_NC_0006", "HME", "批次号"));
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
                    throw new MtException("make-dir-failed", "创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }

        //调用API获取物料批相关信息
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "物料批信息"));
        }
        String uuid = UUID.randomUUID().toString();

        //循环物料进行打印
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<File> imageFileList = new ArrayList<>();
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        // 批量获取条码工单号
        Map<String, MtWorkOrder> barcodeWorkOrderMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            // 批量获取条码对应工单信息
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

            //生成条形码
            String barcodePath = basePath + "/" + uuid + "_" + mtMaterialLot.getMaterialLotId() + ".png";
            File imageFile = new File(barcodePath);
            try {
                CommonBarcodeUtil.generateCode128ToFile(mtMaterialLot.getMaterialLotCode(), CommonBarcodeUtil.IMG_TYPE_PNG, imageFile, 10);
                log.info("<====生成条形码完成！{}", barcodePath);

                //设置条码居中
                //CommonBarcodeUtil.repaintPictureToCenter(imageFile, 387, 64);
            } catch (Exception e) {
                log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.generateImageFile Error", e);
                throw new MtException("Exception", e.getMessage());
            }
            int num = i % 2;
            //组装参数
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

        //生成PDF
        String pdfPath = basePath + "/" + uuid + ".pdf";
        try {
            log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList);
            CommonPdfTemplateUtil.multiplePage(basePath + "/cos_tp_print_template.pdf", pdfPath, dataList);
            log.info("<==== 生成PDF完成！{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== HmeCosPatchPdaServiceImpl.getPrintPdfUrl.generatePDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
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

        //删除临时文件
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
                    "HME_NC_0006", "HME", "工单"));
        }
        String operationId = dto.getOperationIdList().get(0);
        HmeCosPatchPdaVO4 result = new HmeCosPatchPdaVO4();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        //条码存在，且有效
        if (mtMaterialLot == null || !YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", dto.getMaterialLotCode()));
        }
        //条码质量状态为OK
        if (!OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("HME_TIME_PROCESS_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0005", "HME", mtMaterialLot.getQualityStatus()));
        }
        //工位+条码唯一，存在数据时，前台确认是否要解绑
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
        //扫描投入条码时，校验条码库位的库位类别是否是INVENTORY
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
        //2020-10-03 09:56 edit by chaonan.hu for zhenyong.ban 取消原先条码库位等于工单产线的默认发料库位的校验
        //增加校验{扫描条码库位${1}与工段或产线下默认存储库位不一致,请核实}
//        //条码库位等于工单产线的默认发料库位
//        String issuedLocatorId = hmeCosPatchPdaMapper.getIssuedLocatorId(tenantId, dto.getWorkOrderId());
//        if(!mtMaterialLot.getLocatorId().equals(issuedLocatorId)){
//            throw new MtException("HME_COS_PATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_COS_PATCH_0001", "HME",mtMaterialLot.getLocatorId(),issuedLocatorId));
//        }
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
            //如果未找到默认存储库位，则报错{当前工位的工段或产线下未维护默认存储库位}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
        if (!defaultStorageLocatorIdList.contains(mtMaterialLot.getLocatorId())) {
            //{扫描条码库位${1}与工段或产线下默认存储库位不一致,请核实}
            throw new MtException("HME_EO_JOB_SN_089", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_089", "HME", mtModLocator.getLocatorCode()));
        }

        //条码物料是工单的工序组件
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        String routerOperationId = null;
        MtBomComponent mtBomComponent2 = null;
        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            //根据ROUTER_ID和工艺ID通过工艺步骤表mt_router_step及
            //工艺步骤与工艺关系表mt_router_operation可获取routerStepId工艺路线步骤ID
            List<MtRouterOperation> mtRouterOperations = hmeCosPatchPdaMapper.routerOperationQuery(tenantId, mtWorkOrder.getRouterId());
            for (MtRouterOperation mtRouterOperation : mtRouterOperations) {
                if (operationId.equals(mtRouterOperation.getOperationId())) {
                    routerOperationId = mtRouterOperation.getRouterOperationId();
                    break;
                }
            }
            if (StringUtils.isNotEmpty(routerOperationId)) {
                //获取BOM_COMPONENT_ID
                List<MtBomComponent> mtBomComponents = hmeCosPatchPdaMapper.bomComponentQuery(tenantId, routerOperationId);
                for (MtBomComponent mtBomComponent : mtBomComponents) {
                    //2020-10-15 edit by zhenyong.ban 获取BOM_COMPONENT_ID的逻辑中增加工单替代料、全局替代料的校验逻辑
                    List<String> materialIdList = new ArrayList<>();
                    materialIdList.add(mtBomComponent.getMaterialId());
                    //根据BOM组件ID查询工单替代料
                    List<String> woSubstituteMaterialList = hmeCosPatchPdaMapper.getWorkOrderSubstituteMaterial(tenantId, mtBomComponent.getBomComponentId());
                    materialIdList.addAll(woSubstituteMaterialList);
                    //2020-12-03 edit by chaonan.hu for zhenyong.ban 查询全局替代料逻辑修改
                    //根据BOM组件物料ID+站点ID查询全局替代料
                    List<String> globalSubstituteMaterialList = hmeCosPatchPdaMapper.getGlobalSubstituteMaterial(tenantId, mtBomComponent.getMaterialId(), mtMaterialLot.getSiteId());
                    if(globalSubstituteMaterialList.contains(mtBomComponent.getMaterialId()) && mtBomComponent.getQty() > 0){
                        //如果Bom组件物料在上面查出来的全局替代料中，但Bom组件数量大于0，则此物料不为全局替代料
                        globalSubstituteMaterialList.remove(mtBomComponent.getMaterialId());
                    }
                    materialIdList.addAll(globalSubstituteMaterialList);
                    if(materialIdList.contains(mtMaterialLot.getMaterialId())){
                        //如果条码物料是组件物料或替代料
                        if(woSubstituteMaterialList.contains(mtMaterialLot.getMaterialId()) &&
                                globalSubstituteMaterialList.contains(mtMaterialLot.getMaterialId())){
                            //如果条码物料同时为工单替代料和全局替代料，则报错{物料不能同时为工单替代料和全局替代料}
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
        //绑定工位与条码的关系
        hmeCosPatchPdaRepository.bandingWorkcell(tenantId, dto, mtMaterialLot.getMaterialId(),
                mtMaterialLot.getMaterialLotId(), mtBomComponent2.getQty());
        //封装返回结果
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
            //条码有效性校验
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeCosPatchPdaVO2.getMaterialLotId());
            if(!"Y".equals(mtMaterialLot.getEnableFlag())){
                throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_001", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            //工位未出站校验
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
