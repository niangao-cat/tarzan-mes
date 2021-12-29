package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosRetestMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.app.service.WmsMaterialLotPrintService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO19;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 13:52
 */
@Component
public class HmeCosRetestRepositoryImpl implements HmeCosRetestRepository {

    @Autowired
    private HmeCosRetestMapper hmeCosRetestMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private WmsMaterialLotPrintService wmsMaterialLotPrintService;
    @Autowired
    private HmeRetestImportDataRepository hmeRetestImportDataRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;
    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;
    @Autowired
    private HmeCosTestSelectCancleRepository hmeCosTestSelectCancleRepository;


    @Override
    @ProcessLovValue
    public HmeCosRetestVO2 scanMaterialLot(Long tenantId, HmeCosRetestVO dto) {
        //校验参数
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "退料条码"));
        }
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "工单ID"));
        }
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        // 校验条码和工位对应的线边仓货位是否一致
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 根据工位 找线边仓货位
        List<String> locatorList = hmeCosRetestMapper.queryLineSideLocator(tenantId, dto.getWorkcellId(), defaultSiteId);
        if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_007", "HME"));
        }
        if (!StringUtils.equals(locatorList.get(0), materialLot.getLocatorId())) {
            throw new MtException("HME_COS_BARCODE_RETEST_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_017", "HME"));
        }
        HmeCosRetestVO2 retestVO2 = new HmeCosRetestVO2();
        //校验条码
        verifyMaterialLot(tenantId, materialLot, dto, retestVO2);
        retestVO2.setMaterialLotId(materialLot.getMaterialLotId());
        retestVO2.setMaterialLotCode(materialLot.getMaterialLotCode());
        retestVO2.setPrimaryUomQty(materialLot.getPrimaryUomQty());
        retestVO2.setLot(materialLot.getLot());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());
        if (mtMaterial != null) {
            retestVO2.setMaterialCode(mtMaterial.getMaterialCode());
            retestVO2.setMaterialName(mtMaterial.getMaterialName());
            retestVO2.setMaterialId(mtMaterial.getMaterialId());
        }
        return retestVO2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cosRetestFeed(Long tenantId, HmeCosRetestVO3 dto) {
        if (CollectionUtils.isEmpty(dto.getScanBarcodeList())) {
            throw new MtException("HME_COS_BARCODE_RETEST_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_005", "HME"));
        }
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "RETEST_WORK_ORDER_INPUT");
        // 创建事件
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("RETEST_WORK_ORDER_INPUT");
            setEventRequestId(eventRequestId);
        }});
        // 获取当前用户
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails()) && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        //批量获取EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", dto.getScanBarcodeList().size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", dto.getScanBarcodeList().size());
        Integer eoJobSnIndex = 0;
        // 当前时间
        Date currentDate = new Date();
        // 查询工单信息
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_006", "HME"));
        }
        // 校验工单状态是否为下达作业 不为则报错
        if (!StringUtils.equals(mtWorkOrder.getStatus(), HmeConstants.StatusCode.EORELEASED)) {
            throw new MtException("HME_COS_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_057", "HME"));
        }
        // 查询产线信息
        MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 根据工位 找线边仓货位
        List<String> locatorList = hmeCosRetestMapper.queryLineSideLocator(tenantId, dto.getWorkcellId(), defaultSiteId);
        if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_007", "HME"));
        }
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        // 工位找工序
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
            setOrganizationType("WORKCELL");
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // 工序找工艺
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())).build());
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList) || operationWkcDispatchRelList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_008", "HME"));
        }
        MtRouterOperation routerOperation = hmeCosRetestMapper.queryRouteOperation(tenantId, mtWorkOrder.getRouterId(), operationWkcDispatchRelList.get(0).getOperationId());
        if (routerOperation == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_009", "HME"));
        }
        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(dto.getMaterialId());
        }});
        String bomComponentId = "";
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                    .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
            }
        }
        // 总需求数量
        BigDecimal totalDemand = BigDecimal.ZERO;
        // 投料数量
        BigDecimal feedDemand = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_bom_component_attr");
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("lineAttribute5");
            attrList.add(attrOne);
            mtExtendVO1.setAttrs(attrList);
            mtExtendVO1.setKeyIdList(Collections.singletonList(bomComponentId));
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                totalDemand = BigDecimal.valueOf(Double.valueOf(extendAttrVO1List.get(0).getAttrValue()));
            }
        }
        // 事务类型
        List<WmsTransactionType> wmsTransactionTypeList = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class).andWhere(Sqls.custom()
                .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, Arrays.asList(new String[]{"HME_WO_ISSUE", "HME_WO_ISSUE_EXT"}))).build());

        List<String> materialLotIdList = dto.getScanBarcodeList().stream().map(HmeCosRetestVO2::getMaterialLotId).distinct().collect(Collectors.toList());
        Map<String, List<MtExtendAttrVO1>> attrMap = new HashMap<>();
        Map<String, List<MtMaterialLot>> mtMaterialLotMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_material_lot_attr");
            mtExtendVO1.setKeyIdList(materialLotIdList);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 cosTypeAttr = new MtExtendVO5();
            cosTypeAttr.setAttrName("COS_TYPE");
            attrs.add(cosTypeAttr);
            MtExtendVO5 waferAttr = new MtExtendVO5();
            waferAttr.setAttrName("WAFER_NUM");
            attrs.add(waferAttr);
            mtExtendVO1.setAttrs(attrs);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(attrVO1List)) {
                attrMap = attrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + attr.getAttrName()));
            }
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
            if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                mtMaterialLotMap = mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getMaterialLotId));
            }
        }
        for (HmeCosRetestVO2 retestVO2 : dto.getScanBarcodeList()) {
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotMap.get(retestVO2.getMaterialLotId());
            MtMaterialLot mtMaterialLot = CollectionUtils.isNotEmpty(mtMaterialLots) ? mtMaterialLots.get(0) : null;
            // 更新条码为在制品
            MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
            commonExtendVO6.setKeyId(retestVO2.getMaterialLotId());
            List<MtCommonExtendVO5> attrs = new ArrayList<>();
            MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
            mtCommonExtendVO5.setAttrName("MF_FLAG");
            mtCommonExtendVO5.setAttrValue(HmeConstants.ConstantValue.YES);
            attrs.add(mtCommonExtendVO5);
            MtCommonExtendVO5 workOrderAttr = new MtCommonExtendVO5();
            workOrderAttr.setAttrName("WORK_ORDER_ID");
            workOrderAttr.setAttrValue(dto.getWorkOrderId());
            attrs.add(workOrderAttr);
            MtCommonExtendVO5 crsAttr = new MtCommonExtendVO5();
            crsAttr.setAttrName("CURRENT_ROUTER_STEP");
            crsAttr.setAttrValue(routerOperation.getRouterStepId());
            attrs.add(crsAttr);
            // wafer 没有值时 取装载表的
            List<MtExtendAttrVO1> waferList = attrMap.get(mtMaterialLot.getMaterialLotId() + "WAFER_NUM");
            String waferNum = CollectionUtils.isNotEmpty(waferList) ? waferList.get(0).getAttrValue() : "";
            if (StringUtils.isBlank(waferNum)) {
                String wafer = hmeCosRetestMapper.queryLastMaterialLotLoad(tenantId, retestVO2.getMaterialLotId());
                MtCommonExtendVO5 waferAttr = new MtCommonExtendVO5();
                waferAttr.setAttrName("WAFER_NUM");
                waferAttr.setAttrValue(wafer);
                attrs.add(waferAttr);

                waferNum = wafer;
            }
            commonExtendVO6.setAttrs(attrs);
            attrPropertyList.add(commonExtendVO6);

            // 记录hme_eo_job_sn表
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(currentDate);
            hmeEoJobSn.setSiteOutDate(currentDate);
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobSn.setOperationId(dto.getOperationId());
            hmeEoJobSn.setMaterialLotId(retestVO2.getMaterialLotId());
            hmeEoJobSn.setSnQty(BigDecimal.valueOf(retestVO2.getPrimaryUomQty()));
            hmeEoJobSn.setJobId(eoJobSnIdList.get(eoJobSnIndex));
            hmeEoJobSn.setCid(Long.parseLong(eoJobSnCidList.get(eoJobSnIndex++)));
            hmeEoJobSn.setObjectVersionNumber(1L);
            hmeEoJobSn.setCreatedBy(userId);
            hmeEoJobSn.setSnMaterialId(retestVO2.getMaterialId());
            hmeEoJobSn.setCreationDate(currentDate);
            hmeEoJobSn.setLastUpdatedBy(userId);
            hmeEoJobSn.setLastUpdateDate(currentDate);
            hmeEoJobSn.setJobType("COS_STOCK_RETEST");
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            Integer eoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, dto.getWorkcellId(), null, mtMaterialLot.getMaterialLotId(), "N", "COS_STOCK_RETEST", dto.getOperationId());
            hmeEoJobSn.setEoStepNum(eoStepNum + 1);
            hmeEoJobSn.setReworkFlag("N");
            List<MtExtendAttrVO1> cosTypeList = attrMap.get(mtMaterialLot.getMaterialLotId() + "COS_TYPE");

            hmeEoJobSn.setAttribute3(CollectionUtils.isNotEmpty(cosTypeList) ? cosTypeList.get(0).getAttrValue() : "");
            hmeEoJobSn.setAttribute5(waferNum);
            hmeEoJobSn.setAttribute6(String.valueOf(mtMaterialLot.getPrimaryUomQty()));
            hmeEoJobSnList.add(hmeEoJobSn);

            // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
            List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                setTenantId(tenantId);
                setMaterialLotId(retestVO2.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(loadList)) {
                loadList.forEach(load -> {
                    HmeCosRetestVO5 hmeCosRetestVO5 = new HmeCosRetestVO5();
                    BeanUtils.copyProperties(dto,hmeCosRetestVO5);
                    createLoadJob(tenantId, load, hmeCosRetestVO5, "COS_STOCK_RETEST");
                });
            }

            // 存在两笔事务的 临时的集合
            List<WmsObjectTransactionRequestVO> transactionList = new ArrayList<>();
            // 查询投料数量
            if (StringUtils.isNotBlank(bomComponentId)) {
                List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, dto.getMaterialId())
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, bomComponentId)
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                if (totalScrappedQty != null) {
                    feedDemand = BigDecimal.valueOf(totalScrappedQty);
                }
            }
            if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(retestVO2.getPrimaryUomQty())) >= 0) {
                // 工单组件总需求数量-工单组件投料数量>=条码数量  计划内投料事务 事务数量：条码数量
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
                transactionRequestVO.setTransactionQty(BigDecimal.valueOf(retestVO2.getPrimaryUomQty()));
                Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
                if (hmeWoIssueOpt.isPresent()) {
                    transactionRequestVO.setMoveType(hmeWoIssueOpt.get().getMoveType());
                }
                //行号
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                transactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                //bomReserveNum
                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_bom_component_attr");
                extendVO.setKeyId(bomComponentId);
                extendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    transactionRequestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                }
                transactionList.add(transactionRequestVO);
            } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) > 0 && totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(retestVO2.getPrimaryUomQty())) < 0) {
                // 0<工单组件总需求数量-工单组件投料数量<条码数量 做两笔事务
                // 计划内投料 事务数量:工单组件总需求数量-工单组件投料数量
                WmsObjectTransactionRequestVO woIssueTransaction = new WmsObjectTransactionRequestVO();
                woIssueTransaction.setTransactionTypeCode("HME_WO_ISSUE");
                woIssueTransaction.setTransactionQty(totalDemand.subtract(feedDemand));
                Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
                if (hmeWoIssueOpt.isPresent()) {
                    woIssueTransaction.setMoveType(hmeWoIssueOpt.get().getMoveType());
                }
                //行号
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                woIssueTransaction.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                //bomReserveNum
                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_bom_component_attr");
                extendVO.setKeyId(bomComponentId);
                extendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    woIssueTransaction.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                }

                // 计划外投料 事务数量:条码数量-（工单组件总需求数量-工单组件投料数量）
                WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
                woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(retestVO2.getPrimaryUomQty()).subtract(totalDemand).add(feedDemand));
                Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
                if (hmeWoIssueExtOpt.isPresent()) {
                    woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
                }
                transactionList.add(woIssueExtTrans);
            } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) <= 0) {
                // 工单组件总需求数量-工单组件投料数量<=0  计划外投料事务 事务数量：条码数量
                WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
                woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(retestVO2.getPrimaryUomQty()));
                Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
                if (hmeWoIssueExtOpt.isPresent()) {
                    woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
                }
                transactionList.add(woIssueExtTrans);
            }

            for (WmsObjectTransactionRequestVO requestVO : transactionList) {
                // 记录计划内/计划外投料事务
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                transactionRequestVO.setMoveType(requestVO.getMoveType());
                transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(retestVO2.getMaterialLotId());
                transactionRequestVO.setMaterialId(retestVO2.getMaterialId());
                transactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                transactionRequestVO.setTransactionTime(currentDate);
                transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorList.get(0));
                transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                transactionRequestVO.setLocatorId(locatorList.get(0));
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : null);
                transactionRequestVO.setRemark("COS复测投料");
                transactionRequestVOList.add(transactionRequestVO);
            }

            // 调用API{woComponentAssemble} 更新/新增工单组件装配实绩
            MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
            if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
                mtWoComponentActualVO1.setAssembleExcessFlag("N");
            } else {
                mtWoComponentActualVO1.setAssembleExcessFlag("Y");
            }
            if (StringUtils.isNotBlank(bomComponentId)) {
                mtWoComponentActualVO1.setBomComponentId(bomComponentId);
            }
            mtWoComponentActualVO1.setEventRequestId(eventRequestId);
            mtWoComponentActualVO1.setMaterialId(retestVO2.getMaterialId());
            mtWoComponentActualVO1.setOperationId(dto.getOperationId());
            mtWoComponentActualVO1.setParentEventId(eventId);
            mtWoComponentActualVO1.setRouterStepId(routerOperation.getRouterStepId());
            mtWoComponentActualVO1.setTrxAssembleQty(retestVO2.getPrimaryUomQty());
            mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
            mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

            // 调用api{onhandQtyUpdateProcess}扣减组件线边库存
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
            mtInvOnhandQuantityVO9.setMaterialId(retestVO2.getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(locatorList.get(0));
            mtInvOnhandQuantityVO9.setLotCode(retestVO2.getLot());
            mtInvOnhandQuantityVO9.setChangeQuantity(retestVO2.getPrimaryUomQty());
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        }
        // 批量更新条码扩展字段
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        // 批量插入EoJobSn
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            hmeEoJobSnRepository.myBatchInsert(hmeEoJobSnList);
        }
        if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
        }
    }

    @Override
    public HmeCosRetestVO4 overCosNumQuery(Long tenantId, HmeCosRetestVO4 hmeCosRetestVO4) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosRetestVO4.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_006", "HME"));
        }
        // 工位找工序
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(hmeCosRetestVO4.getWorkcellId());
            setOrganizationType("WORKCELL");
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // 工序找工艺
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())).build());
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList) || operationWkcDispatchRelList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_008", "HME"));
        }
        MtRouterOperation routerOperation = hmeCosRetestMapper.queryRouteOperation(tenantId, mtWorkOrder.getRouterId(), operationWkcDispatchRelList.get(0).getOperationId());
        if (routerOperation == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_009", "HME"));
        }
        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(hmeCosRetestVO4.getMaterialId());
        }});
        String bomComponentId = "";
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                    .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
            }
        }
        if (StringUtils.isBlank(bomComponentId)) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeCosRetestVO4.getMaterialId());
            throw new MtException("HME_COS_BARCODE_RETEST_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_011", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : ""));
        }
        List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, hmeCosRetestVO4.getMaterialId())
                .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, bomComponentId)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
        Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
        BigDecimal scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
        BigDecimal woQty = mtWorkOrder.getQty() != null ? BigDecimal.valueOf(mtWorkOrder.getQty()) : BigDecimal.ZERO;
        hmeCosRetestVO4.setRemainingQty(woQty.subtract(scrappedQty));
        return hmeCosRetestVO4;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosRetestVO6> queryCosTypeByContainerType(Long tenantId, String containerTypeCode, String operationId) {
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
            setTenantId(tenantId);
            setContainerTypeCode(containerTypeCode);
        }});
        if (mtContainerType == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_010", "HME"));
        }
        if (StringUtils.isBlank(operationId)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        List<HmeContainerCapacity> hmeContainerCapacityList = hmeContainerCapacityRepository.select(new HmeContainerCapacity() {{
            setTenantId(tenantId);
            setContainerTypeId(mtContainerType.getContainerTypeId());
            setOperationId(operationId);
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        List<HmeCosRetestVO6> hmeCosRetestVO6List = hmeContainerCapacityList.stream().filter(dto -> dto.getCapacity().compareTo(1L) == 0).map(hcc -> {
            HmeCosRetestVO6 hmeCosRetestVO6 = new HmeCosRetestVO6();
            hmeCosRetestVO6.setContainerTypeId(hcc.getContainerTypeId());
            hmeCosRetestVO6.setCosType(hcc.getCosType());
            return hmeCosRetestVO6;
        }).collect(Collectors.toList());
        return hmeCosRetestVO6List;
    }

    @Override
    public HmeCosRetestVO2 scrapScanMaterialLot(Long tenantId, HmeCosRetestVO dto) {
        //校验参数
        // 工单
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "工单ID"));
        }
        // 来料条码
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "退料条码"));
        }
        // 校验条码是否存在
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        HmeCosRetestVO2 retestVO2 = new HmeCosRetestVO2();
        //校验条码
        verifyMaterialLot(tenantId, materialLot, dto, retestVO2);
        retestVO2.setMaterialLotId(materialLot.getMaterialLotId());
        retestVO2.setMaterialLotCode(materialLot.getMaterialLotCode());
        retestVO2.setPrimaryUomQty(materialLot.getPrimaryUomQty());
        return retestVO2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosRetestVO5 cosScrapSplit(Long tenantId, HmeCosRetestVO5 hmeCosRetestVO5) {
        // 校验：抬头字段WAFER、容器类型、COS类型以及条码清单内条码个数字段必输校验，若未输入，提示对应字段未输入报错！
        this.checkNonEmpty(tenantId, hmeCosRetestVO5);
        // 复测投料事件请求，调用API：eventRequestCreate
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "RETEST_WORK_ORDER_INPUT");
        // 复测投料事件，调用API：eventCreate
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("RETEST_WORK_ORDER_INPUT");
            setEventRequestId(eventRequestId);
        }});
        // 更新来料条码，调用API：materialLotUpdate
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, hmeCosRetestVO5.getSourceLotCode());
        // 拆分的总数量
        Double totalCosNum = hmeCosRetestVO5.getTargetList().stream().map(HmeCosRetestVO7::getCosNum).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
        // 变化后数量 = 原数量-拆分数量 小于或等于0 置为失效
        BigDecimal changeQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalCosNum));
        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
            setMaterialLotCode(hmeCosRetestVO5.getSourceLotCode());
            setEventId(eventId);
            setPrimaryUomQty(changeQty.doubleValue());
            setEnableFlag(changeQty.compareTo(BigDecimal.ZERO) > 0 ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
        }}, "N");
        // 查询工单
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosRetestVO5.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_006", "HME"));
        }
        // 校验工单状态是否为下达作业 不为则报错
        if (!StringUtils.equals(mtWorkOrder.getStatus(), HmeConstants.StatusCode.EORELEASED)) {
            throw new MtException("HME_COS_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_057", "HME"));
        }
        // 生成来料记录
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setSiteId(mtWorkOrder.getSiteId());
        hmeCosOperationRecord.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
            setTenantId(tenantId);
            setContainerTypeCode(hmeCosRetestVO5.getContainerTypeCode());
        }});
        hmeCosOperationRecord.setContainerTypeId(mtContainerType != null ? mtContainerType.getContainerTypeId() : "");
        hmeCosOperationRecord.setCosType(hmeCosRetestVO5.getCosType());
        hmeCosOperationRecord.setAverageWavelength(hmeCosRetestVO5.getAverageWavelength());
        hmeCosOperationRecord.setType(hmeCosRetestVO5.getType());
        hmeCosOperationRecord.setLotNo(hmeCosRetestVO5.getLotNo());
        hmeCosOperationRecord.setWafer(hmeCosRetestVO5.getWafer());
        hmeCosOperationRecord.setRemark(hmeCosRetestVO5.getRemark());
        hmeCosOperationRecord.setJobBatch(hmeCosRetestVO5.getJobBatch());
        hmeCosOperationRecord.setCosNum(totalCosNum.longValue());
        hmeCosOperationRecord.setOperationId(hmeCosRetestVO5.getOperationId());
        hmeCosOperationRecord.setWorkcellId(hmeCosRetestVO5.getWorkcellId());
        hmeCosOperationRecord.setMaterialId(mtMaterialLot.getMaterialId());
        hmeCosOperationRecord.setTenantId(tenantId);
        hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

        // 查询容器容量
        HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityRepository.selectOne(new HmeContainerCapacity() {{
            setTenantId(tenantId);
            setContainerTypeId(mtContainerType.getContainerTypeId());
            setOperationId(hmeCosRetestVO5.getOperationId());
            setCosType(hmeCosRetestVO5.getCosType());
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        // 容器容量
        BigDecimal capacity = BigDecimal.valueOf(hmeContainerCapacity.getLineNum()).multiply(BigDecimal.valueOf(hmeContainerCapacity.getColumnNum())).multiply(BigDecimal.valueOf(hmeContainerCapacity.getCapacity()));

        // 工位找工序
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(hmeCosRetestVO5.getWorkcellId());
            setOrganizationType("WORKCELL");
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // 工序找工艺
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())).build());
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList) || operationWkcDispatchRelList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_008", "HME"));
        }
        MtRouterOperation routerOperation = hmeCosRetestMapper.queryRouteOperation(tenantId, mtWorkOrder.getRouterId(), operationWkcDispatchRelList.get(0).getOperationId());
        if (routerOperation == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_009", "HME"));
        }
        // 拆分生成条码号
        hmeCosRetestVO5.setSiteId(mtWorkOrder.getSiteId());
        hmeCosRetestVO5.setProdLineId(mtWorkOrder.getProductionLineId());
        List<HmeCosRetestVO7> hmeCosRetestVO7List = this.splitBarcode(tenantId, mtMaterialLot, hmeCosRetestVO5.getTargetList(), hmeCosRetestVO5, hmeCosOperationRecord, hmeContainerCapacity, routerOperation, capacity,  eventId);

        // 事务类型
        List<WmsTransactionType> wmsTransactionTypeList = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class).andWhere(Sqls.custom()
                .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, Arrays.asList(new String[]{"HME_WO_ISSUE", "HME_WO_ISSUE_EXT"}))).build());

        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(hmeCosRetestVO5.getMaterialId());
        }});
        String bomComponentId = "";
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                    .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
            }
        }
        // 总需求数量
        BigDecimal totalDemand = BigDecimal.ZERO;
        // 投料数量
        BigDecimal feedDemand = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_bom_component_attr");
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("lineAttribute5");
            attrList.add(attrOne);
            mtExtendVO1.setAttrs(attrList);
            mtExtendVO1.setKeyIdList(Collections.singletonList(bomComponentId));
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                totalDemand = BigDecimal.valueOf(Double.valueOf(extendAttrVO1List.get(0).getAttrValue()));
            }
        }
        // 查询投料数量
        if (StringUtils.isNotBlank(bomComponentId)) {
            List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, hmeCosRetestVO5.getMaterialId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, bomComponentId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
            Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            if (totalScrappedQty != null) {
                feedDemand = BigDecimal.valueOf(totalScrappedQty);
            }
        }
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 根据工位 找线边仓货位
        List<String> locatorList = hmeCosRetestMapper.queryLineSideLocator(tenantId, hmeCosRetestVO5.getWorkcellId(), defaultSiteId);
        if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_007", "HME"));
        }
        // 来料条码记录计划内/计划外投料事务
        this.createTransaction(tenantId, wmsTransactionTypeList, totalDemand, feedDemand, mtMaterialLot, bomComponentId, mtWorkOrder, locatorList.get(0), totalCosNum, eventId);

        // 调用API{woComponentAssemble} 更新/新增工单组件装配实绩
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            mtWoComponentActualVO1.setAssembleExcessFlag("N");
        } else {
            mtWoComponentActualVO1.setAssembleExcessFlag("Y");
        }
        if (StringUtils.isNotBlank(bomComponentId)) {
            mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        }
        mtWoComponentActualVO1.setEventRequestId(eventRequestId);
        mtWoComponentActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
        mtWoComponentActualVO1.setOperationId(hmeCosRetestVO5.getOperationId());
        mtWoComponentActualVO1.setParentEventId(eventId);
        mtWoComponentActualVO1.setRouterStepId(routerOperation.getRouterStepId());
        mtWoComponentActualVO1.setTrxAssembleQty(totalCosNum);
        mtWoComponentActualVO1.setWorkcellId(hmeCosRetestVO5.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(hmeCosRetestVO5.getWorkOrderId());
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
        hmeCosRetestVO5.setTargetList(hmeCosRetestVO7List);

        // 调用api{onhandQtyUpdateProcess} 基于来料条码扣减组件线边库存
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
        mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
        mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
        mtInvOnhandQuantityVO9.setChangeQuantity(totalCosNum);
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        return hmeCosRetestVO5;
    }

    @Override
    public HmeCosRetestVO8 backFactoryScanMaterialLot(Long tenantId, HmeCosRetestVO dto) {
        HmeCosRetestVO8 hmeCosRetestVO8 = new HmeCosRetestVO8();
        //校验条码是否存在
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (mtMaterialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        // 校验条码
        verifyBackFactoryMaterialLot(tenantId, mtMaterialLot, dto.getWorkOrderId());
        hmeCosRetestVO8.setSourceMaterialLotId(mtMaterialLot.getMaterialLotId());
        hmeCosRetestVO8.setSourceMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        hmeCosRetestVO8.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());

        List<HmeCosRetestVO10> hmeCosRetestVO10List = hmeCosRetestMapper.queryReturnMaterialLotCodeList(tenantId, mtMaterialLot.getMaterialLotId());
        if (CollectionUtils.isEmpty(hmeCosRetestVO10List)) {
            throw new MtException("HME_COS_BARCODE_RETEST_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_015", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 退料条码清单
        hmeCosRetestVO8.setReturnMaterialLotList(hmeCosRetestVO10List);
        // 投料条码信息
        hmeCosRetestVO8.setFeelMaterialLotList(hmeCosRetestMapper.queryFeelMaterialLotCodeList(tenantId, mtMaterialLot.getMaterialLotId()));
        return hmeCosRetestVO8;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosRetestVO9 cosBackFactorySplit(Long tenantId, HmeCosRetestVO9 dto) {
        // 校验参数
        // 工单
        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "workOrderId"));
        }
        // WAFER
        if (StringUtils.isBlank(dto.getWafer())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "WAFER"));
        }
        // 容器类型
        if (StringUtils.isBlank(dto.getContainerTypeCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "容器类型"));
        }
        // COS类型
        if (StringUtils.isBlank(dto.getCosType())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "COS类型"));
        }
        // 来料条码
        if (StringUtils.isBlank(dto.getSourceMaterialLotCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "来料条码"));
        }
        // 复测投料事件请求，调用API：eventRequestCreate
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "RETEST_WORK_ORDER_INPUT");
        // 复测投料事件，调用API：eventCreate
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("RETEST_WORK_ORDER_INPUT");
            setEventRequestId(eventRequestId);
        }});

        MtMaterialLot sourceMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getSourceMaterialLotCode());
        // 拆分的总数量
        Double totalCosNum = dto.getReturnMaterialLotList().stream().map(HmeCosRetestVO10::getPrimaryUomQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
        // 变化后数量 = 原数量-拆分数量 小于或等于0 置为失效
        BigDecimal changeQty = BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalCosNum));
        // 更新来料条码，调用API：materialLotUpdate
        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
            setMaterialLotCode(sourceMaterialLot.getMaterialLotCode());
            setEventId(eventId);
            setPrimaryUomQty(changeQty.doubleValue());
            setEnableFlag(changeQty.compareTo(BigDecimal.ZERO) > 0 ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
        }}, "N");
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_006", "HME"));
        }
        // 校验工单状态是否为下达作业 不为则报错
        if (!StringUtils.equals(mtWorkOrder.getStatus(), HmeConstants.StatusCode.EORELEASED)) {
            throw new MtException("HME_COS_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_057", "HME"));
        }
        // 工位找工序
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
            setOrganizationType("WORKCELL");
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // 工序找工艺
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())).build());
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList) || operationWkcDispatchRelList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_008", "HME"));
        }
        MtRouterOperation routerOperation = hmeCosRetestMapper.queryRouteOperation(tenantId, mtWorkOrder.getRouterId(), operationWkcDispatchRelList.get(0).getOperationId());
        if (routerOperation == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_009", "HME"));
        }
        // 更新条码清单有效性、装载信息及扩展字段
        this.updateReturnMaterialLotCode(tenantId, sourceMaterialLot.getMaterialLotId(), dto.getReturnMaterialLotList(), dto, routerOperation, eventId);
        // 事务类型
        List<WmsTransactionType> wmsTransactionTypeList = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class).andWhere(Sqls.custom()
                .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, Arrays.asList(new String[]{"HME_WO_ISSUE", "HME_WO_ISSUE_EXT"}))).build());

        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(sourceMaterialLot.getMaterialId());
        }});
        String bomComponentId = "";
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                    .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
            }
        }
        // 总需求数量
        BigDecimal totalDemand = BigDecimal.ZERO;
        // 投料数量
        BigDecimal feedDemand = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_bom_component_attr");
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("lineAttribute5");
            attrList.add(attrOne);
            mtExtendVO1.setAttrs(attrList);
            mtExtendVO1.setKeyIdList(Collections.singletonList(bomComponentId));
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                totalDemand = BigDecimal.valueOf(Double.valueOf(extendAttrVO1List.get(0).getAttrValue()));
            }
        }
        // 查询投料数量
        if (StringUtils.isNotBlank(bomComponentId)) {
            List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, sourceMaterialLot.getMaterialId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, bomComponentId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
            Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            if (totalScrappedQty != null) {
                feedDemand = BigDecimal.valueOf(totalScrappedQty);
            }
        }
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 根据工位 找线边仓货位
        List<String> locatorList = hmeCosRetestMapper.queryLineSideLocator(tenantId, dto.getWorkcellId(), defaultSiteId);
        if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
            throw new MtException("HME_COS_BARCODE_RETEST_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_007", "HME"));
        }
        // 来料条码记录计划内/计划外投料事务
        this.createBackFactoryTransaction(tenantId, wmsTransactionTypeList, totalDemand, feedDemand, sourceMaterialLot, bomComponentId, mtWorkOrder, locatorList.get(0), totalCosNum, eventId);

        // 调用API{woComponentAssemble} 退料条码更新对应装配实绩
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            mtWoComponentActualVO1.setAssembleExcessFlag("N");
        } else {
            mtWoComponentActualVO1.setAssembleExcessFlag("Y");
        }
        if (StringUtils.isNotBlank(bomComponentId)) {
            mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        }
        mtWoComponentActualVO1.setEventRequestId(eventRequestId);
        mtWoComponentActualVO1.setMaterialId(sourceMaterialLot.getMaterialId());
        mtWoComponentActualVO1.setOperationId(dto.getOperationId());
        mtWoComponentActualVO1.setParentEventId(eventId);
        mtWoComponentActualVO1.setRouterStepId(routerOperation.getRouterStepId());
        mtWoComponentActualVO1.setTrxAssembleQty(sourceMaterialLot.getPrimaryUomQty());
        mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

        // 调用api{onhandQtyUpdateProcess} 基于来料条码扣减组件线边库存
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
        mtInvOnhandQuantityVO9.setMaterialId(sourceMaterialLot.getMaterialId());
        mtInvOnhandQuantityVO9.setLocatorId(sourceMaterialLot.getLocatorId());
        mtInvOnhandQuantityVO9.setLotCode(sourceMaterialLot.getLot());
        mtInvOnhandQuantityVO9.setChangeQuantity(sourceMaterialLot.getPrimaryUomQty());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        // 基于投料条码做处理
        if (CollectionUtils.isNotEmpty(dto.getFeelMaterialLotList())) {
            List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
            for (HmeCosRetestVO10 hmeCosRetestVO10 : dto.getFeelMaterialLotList()) {
                // 查询投料的条码的bom组件
                List<MtBomComponent> feelBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                    setBomId(mtWorkOrder.getBomId());
                    setMaterialId(hmeCosRetestVO10.getMaterialId());
                }});

                String feelBomComponentId = "";
                if (CollectionUtils.isNotEmpty(feelBomComponentList)) {
                    List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                            .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                            .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, feelBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
                    if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                        feelBomComponentId = routerOperationComponentList.get(0).getBomComponentId();
                    }
                }
                // 投料条码总需求数量
                BigDecimal feelTotalDemand = BigDecimal.ZERO;
                // 投料数量
                BigDecimal feedQty = BigDecimal.ZERO;
                if (StringUtils.isNotBlank(feelBomComponentId)) {
                    MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_bom_component_attr");
                    List<MtExtendVO5> attrList = new ArrayList<>();
                    MtExtendVO5 attrOne = new MtExtendVO5();
                    attrOne.setAttrName("lineAttribute5");
                    attrList.add(attrOne);
                    mtExtendVO1.setAttrs(attrList);
                    mtExtendVO1.setKeyIdList(Collections.singletonList(feelBomComponentId));
                    List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                        feelTotalDemand = BigDecimal.valueOf(Double.valueOf(extendAttrVO1List.get(0).getAttrValue()));
                    }
                }

                // 查询投料数量
                if (StringUtils.isNotBlank(feelBomComponentId)) {
                    List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, hmeCosRetestVO10.getMaterialId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, feelBomComponentId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
                    Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                    if (totalScrappedQty != null) {
                        feedQty = BigDecimal.valueOf(totalScrappedQty);
                    }
                }
                MtMaterialLot feelMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeCosRetestVO10.getMaterialLotId());
                // 投料条码记录计划内/计划外投料事务
                this.createBackFactoryTransaction(tenantId, wmsTransactionTypeList, feelTotalDemand, feedQty, feelMaterialLot, feelBomComponentId, mtWorkOrder, locatorList.get(0), feelMaterialLot.getPrimaryUomQty(), eventId);

                // 基于投料条码扣减组件线边库存
                MtInvOnhandQuantityVO9 feelInvOnhandQuantity = new MtInvOnhandQuantityVO9();
                feelInvOnhandQuantity.setSiteId(mtWorkOrder.getSiteId());
                feelInvOnhandQuantity.setMaterialId(feelMaterialLot.getMaterialId());
                feelInvOnhandQuantity.setLocatorId(feelMaterialLot.getLocatorId());
                feelInvOnhandQuantity.setLotCode(feelMaterialLot.getLot());
                feelInvOnhandQuantity.setChangeQuantity(feelMaterialLot.getPrimaryUomQty());
                feelInvOnhandQuantity.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, feelInvOnhandQuantity);

                // 调用API{woComponentAssemble} 投料条码更新对应装配实绩
                MtWoComponentActualVO1 feelWoComponentActual = new MtWoComponentActualVO1();
                if (CollectionUtils.isNotEmpty(feelBomComponentList)) {
                    feelWoComponentActual.setAssembleExcessFlag("N");
                } else {
                    feelWoComponentActual.setAssembleExcessFlag("Y");
                }
                if (StringUtils.isNotBlank(feelBomComponentId)) {
                    feelWoComponentActual.setBomComponentId(feelBomComponentId);
                }
                feelWoComponentActual.setEventRequestId(eventRequestId);
                feelWoComponentActual.setMaterialId(hmeCosRetestVO10.getMaterialId());
                feelWoComponentActual.setOperationId(dto.getOperationId());
                feelWoComponentActual.setParentEventId(eventId);
                feelWoComponentActual.setRouterStepId(routerOperation.getRouterStepId());
                feelWoComponentActual.setTrxAssembleQty(hmeCosRetestVO10.getPrimaryUomQty());
                feelWoComponentActual.setWorkcellId(dto.getWorkcellId());
                feelWoComponentActual.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, feelWoComponentActual);

                // 更新投料条码为失效
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(hmeCosRetestVO10.getMaterialLotId());
                mtMaterialLotVO20.setMaterialLotCode(hmeCosRetestVO10.getMaterialLotCode());
                mtMaterialLotVO20.setPrimaryUomQty(0D);
                mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
            }
            // 批量更新投料条码
            if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
            }
        }
        return dto;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosRetestImportVO3> cosRetestImportHeaderDataList(Long tenantId, HmeCosRetestImportVO2 dto, PageRequest pageRequest) {
        Page<HmeCosRetestImportVO3> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosRetestMapper.cosRetestImportHeaderDataList(tenantId, dto));
        List<Long> createByList = pageObj.getContent().stream().map(HmeCosRetestImportVO3::getCreateBy).filter(Objects::nonNull).map(c -> {return Long.valueOf(c);}).collect(Collectors.toList());
        List<Long> operatorList = pageObj.getContent().stream().map(HmeCosRetestImportVO3::getOperator).filter(Objects::nonNull).map(o -> {return Long.valueOf(o);}).collect(Collectors.toList());
        List<Long> userIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(createByList)) {
            userIdList.addAll(createByList);
        }
        if (CollectionUtils.isNotEmpty(operatorList)) {
            userIdList.addAll(operatorList);
        }
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        // 查询目标条码
        List<String> targetBarcodeList = pageObj.getContent().stream().map(HmeCosRetestImportVO3::getTargetBarcode).filter(Objects::nonNull).collect(Collectors.toList());
        Map<String, List<MtMaterialLot>> targetBarcodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(targetBarcodeList)) {
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, targetBarcodeList);
            targetBarcodeMap = materialLotList.stream().collect(Collectors.groupingBy(barcode -> barcode.getMaterialLotCode()));
        }
        for (HmeCosRetestImportVO3 hmeCosRetestImportVO3 : pageObj.getContent()) {
            // 导入人
            if (StringUtils.isNotBlank(hmeCosRetestImportVO3.getCreateBy())) {
                MtUserInfo userInfo = userInfoMap.get(Long.valueOf(hmeCosRetestImportVO3.getCreateBy()));
                hmeCosRetestImportVO3.setCreateByName(userInfo != null ? userInfo.getRealName() : "");
            }
            // 操作人
            if (StringUtils.isNotBlank(hmeCosRetestImportVO3.getOperator())) {
                MtUserInfo userInfo = userInfoMap.get(Long.valueOf(hmeCosRetestImportVO3.getOperator()));
                hmeCosRetestImportVO3.setOperatorName(userInfo != null ? userInfo.getRealName() : "");
            }
            if (StringUtils.isNotBlank(hmeCosRetestImportVO3.getTargetBarcode())) {
                List<MtMaterialLot> materialLotList = targetBarcodeMap.get(hmeCosRetestImportVO3.getTargetBarcode());
                if (CollectionUtils.isNotEmpty(materialLotList)) {
                    hmeCosRetestImportVO3.setTargetBarcodeId(materialLotList.get(0).getMaterialLotId());
                }
            }
        }
        return pageObj;
    }

    @Override
    public Page<HmeRetestImportData> cosRetestImportLineList(Long tenantId, String retestImportDataId, PageRequest pageRequest) {
        HmeRetestImportData retestImportData = hmeRetestImportDataRepository.selectByPrimaryKey(retestImportDataId);
        if (retestImportData == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_016", "HME"));
        }
        Page<HmeRetestImportData> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosRetestMapper.cosRetestImportLineList(tenantId, retestImportData.getTargetBarcode()));
        return pageObj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cosRetestPrint(Long tenantId, List<String> retestImportDataIdList, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(retestImportDataIdList)) {
            throw new MtException("HME_COS_BARCODE_RETEST_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_016", "HME"));
        }
        List<HmeRetestImportData> retestImportDataList = hmeRetestImportDataRepository.selectByCondition(Condition.builder(HmeRetestImportData.class).andWhere(Sqls.custom()
                .andIn(HmeRetestImportData.FIELD_RETEST_IMPORT_DATA_ID, retestImportDataIdList)).build());

        if (CollectionUtils.isEmpty(retestImportDataList)) {
            throw new MtException("HME_COS_BARCODE_RETEST_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_016", "HME"));
        }
        // 更新打印状态
        this.updateCosRetestPrintFlag(tenantId, retestImportDataList);

        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, retestImportDataList.stream().map(HmeRetestImportData::getTargetBarcode).filter(Objects::nonNull).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        List<String> materialLotIdList = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        // 打印条码
        try {
            wmsMaterialLotPrintService.multiplePrint(tenantId, materialLotIdList, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
    }

    private void updateCosRetestPrintFlag(Long tenantId, List<HmeRetestImportData> retestImportDataList) {
        List<String> targetBarcodeList = retestImportDataList.stream().map(HmeRetestImportData::getTargetBarcode).filter(Objects::nonNull).collect(Collectors.toList());
        // 更新目标条码相同数据打印状态
        hmeCosRetestMapper.updateCosRetestPrintFlag(tenantId, targetBarcodeList);
    }

    private void createBackFactoryTransaction(Long tenantId, List<WmsTransactionType> wmsTransactionTypeList, BigDecimal totalDemand, BigDecimal feedDemand, MtMaterialLot sourceMaterialLot, String bomComponentId, MtWorkOrder mtWorkOrder, String locatorId, Double totalCosNum, String eventId) {
        List<WmsObjectTransactionRequestVO> transactionList = new ArrayList<>();
        if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty())) >= 0) {
            // 工单组件总需求数量-工单组件投料数量>=来料条码数量  计划内投料事务 事务数量：来料条码数量
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
            transactionRequestVO.setTransactionQty(BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                transactionRequestVO.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            //行号
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            transactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            //bomReserveNum
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_bom_component_attr");
            extendVO.setKeyId(bomComponentId);
            extendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                transactionRequestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
            }
            transactionList.add(transactionRequestVO);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) > 0 && totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(totalCosNum)) < 0) {
            // 0<工单组件总需求数量-工单组件投料数量<来料条码数量 做两笔事务
            // 计划内投料 事务数量:工单组件总需求数量-工单组件投料数量
            WmsObjectTransactionRequestVO woIssueTransaction = new WmsObjectTransactionRequestVO();
            woIssueTransaction.setTransactionTypeCode("HME_WO_ISSUE");
            woIssueTransaction.setTransactionQty(totalDemand.subtract(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                woIssueTransaction.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            //行号
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            woIssueTransaction.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            //bomReserveNum
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_bom_component_attr");
            extendVO.setKeyId(bomComponentId);
            extendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                woIssueTransaction.setBomReserveNum(extendAttrList.get(0).getAttrValue());
            }

            // 计划外投料 事务数量:来料条码数量-（工单组件总需求数量-工单组件投料数量）
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()).subtract(totalDemand).add(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) <= 0) {
            // 工单组件总需求数量-工单组件投料数量<=0  计划外投料事务 事务数量：来料条码数量
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        }
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        // 当前时间
        Date currentDate = CommonUtils.currentTimeGet();
        // 查询产线信息
        MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());

        for (WmsObjectTransactionRequestVO requestVO : transactionList) {
            // 记录计划内/计划外投料事务
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
            transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
            transactionRequestVO.setMoveType(requestVO.getMoveType());
            transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
            transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
            transactionRequestVO.setEventId(eventId);
            transactionRequestVO.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            transactionRequestVO.setMaterialId(sourceMaterialLot.getMaterialId());
            transactionRequestVO.setLotNumber(sourceMaterialLot.getLot());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
            transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
            transactionRequestVO.setTransactionTime(currentDate);
            transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
            transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
            transactionRequestVO.setLocatorId(locatorId);
            transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : null);
            transactionRequestVO.setRemark("COS返厂复测投料");
            transactionRequestVOList.add(transactionRequestVO);
        }

        if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
        }
    }

    private void updateReturnMaterialLotCode(Long tenantId, String sourceMaterialLotId, List<HmeCosRetestVO10> returnMaterialLotList, HmeCosRetestVO9 hmeCosRetestV08, MtRouterOperation routerOperation, String eventId) {
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();

        List<HmeMaterialLotLoad> materialLotLoadList = new ArrayList<>();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        Date currentDate = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        //批量获取EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", returnMaterialLotList.size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", returnMaterialLotList.size());
        Integer eoJobSnIndex = 0;
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        for (HmeCosRetestVO10 hmeCosRetestVO10 : returnMaterialLotList) {
            // 记录hme_eo_job_sn表
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(currentDate);
            hmeEoJobSn.setSiteOutDate(currentDate);
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setWorkcellId(hmeCosRetestV08.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(hmeCosRetestV08.getWorkOrderId());
            hmeEoJobSn.setOperationId(hmeCosRetestV08.getOperationId());
            hmeEoJobSn.setMaterialLotId(hmeCosRetestVO10.getMaterialLotId());
            hmeEoJobSn.setSnQty(BigDecimal.valueOf(hmeCosRetestVO10.getPrimaryUomQty()));
            hmeEoJobSn.setJobId(eoJobSnIdList.get(eoJobSnIndex));
            hmeEoJobSn.setCid(Long.parseLong(eoJobSnCidList.get(eoJobSnIndex++)));
            hmeEoJobSn.setObjectVersionNumber(1L);
            hmeEoJobSn.setCreatedBy(userId);
            hmeEoJobSn.setSnMaterialId(hmeCosRetestVO10.getMaterialId());
            hmeEoJobSn.setCreationDate(currentDate);
            hmeEoJobSn.setLastUpdatedBy(userId);
            hmeEoJobSn.setLastUpdateDate(currentDate);
            hmeEoJobSn.setJobType("COS_RECALL_RETEST");
            hmeEoJobSn.setShiftId(hmeCosRetestV08.getWkcShiftId());
            Integer eoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeCosRetestV08.getWorkcellId(), null, hmeCosRetestVO10.getMaterialLotId(), "N", "COS_RECALL_RETEST", hmeCosRetestV08.getOperationId());
            hmeEoJobSn.setEoStepNum(eoStepNum + 1);
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setAttribute3(hmeCosRetestV08.getCosType());
            hmeEoJobSn.setAttribute5(hmeCosRetestV08.getWafer());
            hmeEoJobSn.setAttribute6(String.valueOf(hmeCosRetestVO10.getPrimaryUomQty()));
            hmeEoJobSnList.add(hmeEoJobSn);

            // 更新条码有效性
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(hmeCosRetestVO10.getMaterialLotId());
            mtMaterialLotVO20.setMaterialLotCode(hmeCosRetestVO10.getMaterialLotCode());
            mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
            mtMaterialLotVO20.setPrimaryUomQty(hmeCosRetestVO10.getPrimaryUomQty());
            mtMaterialLotVO20List.add(mtMaterialLotVO20);

            // 查询装载信息Attribute3将工单存入
            List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                setTenantId(tenantId);
                setMaterialLotId(hmeCosRetestVO10.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(loadList)) {
                List<HmeMaterialLotLoad> resultLoadList = loadList.stream().map(load -> {
                    load.setAttribute3(hmeCosRetestV08.getWorkOrderId());

                    // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                    HmeCosRetestVO5 hmeCosRetestVO5 = new HmeCosRetestVO5();
                    BeanUtils.copyProperties(hmeCosRetestV08,hmeCosRetestVO5);
                    createLoadJob(tenantId, load, hmeCosRetestVO5, "COS_RECALL_RETEST");

                    return load;
                }).collect(Collectors.toList());
                materialLotLoadList.addAll(resultLoadList);
            }

            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(hmeCosRetestVO10.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            MtCommonExtendVO5 attr1 = new MtCommonExtendVO5();
            attr1.setAttrName("WORK_ORDER_ID");
            attr1.setAttrValue(hmeCosRetestV08.getWorkOrderId());
            attrList.add(attr1);
            MtCommonExtendVO5 attr2 = new MtCommonExtendVO5();
            attr2.setAttrName("WORKING_LOT");
            attr2.setAttrValue(hmeCosRetestV08.getJobBatch());
            attrList.add(attr2);
            MtCommonExtendVO5 attr3 = new MtCommonExtendVO5();
            attr3.setAttrName("WAFER_NUM");
            attr3.setAttrValue(hmeCosRetestV08.getWafer());
            attrList.add(attr3);
            MtCommonExtendVO5 attr4 = new MtCommonExtendVO5();
            attr4.setAttrName("TYPE");
            attr4.setAttrValue(hmeCosRetestV08.getType());
            attrList.add(attr4);
            MtCommonExtendVO5 attr5 = new MtCommonExtendVO5();
            attr5.setAttrName("STATUS");
            attr5.setAttrValue("INSTOCK");
            attrList.add(attr5);
            MtCommonExtendVO5 attr6 = new MtCommonExtendVO5();
            attr6.setAttrName("REMARK");
            attr6.setAttrValue(hmeCosRetestV08.getRemark());
            attrList.add(attr6);
            MtCommonExtendVO5 attr7 = new MtCommonExtendVO5();
            attr7.setAttrName("ORIGINAL_ID");
            attr7.setAttrValue(sourceMaterialLotId);
            attrList.add(attr7);
            MtCommonExtendVO5 attr8 = new MtCommonExtendVO5();
            attr8.setAttrName("MF_FLAG");
            attr8.setAttrValue(HmeConstants.ConstantValue.YES);
            attrList.add(attr8);
            MtCommonExtendVO5 attr9 = new MtCommonExtendVO5();
            attr9.setAttrName("LOTNO");
            attr9.setAttrValue(hmeCosRetestV08.getLotNo());
            attrList.add(attr9);
            MtCommonExtendVO5 attr10 = new MtCommonExtendVO5();
            attr10.setAttrName("CURRENT_ROUTER_STEP");
            attr10.setAttrValue(routerOperation.getRouterStepId());
            attrList.add(attr10);
            MtCommonExtendVO5 attr11 = new MtCommonExtendVO5();
            attr11.setAttrName("COS_TYPE");
            attr11.setAttrValue(hmeCosRetestV08.getCosType());
            attrList.add(attr11);
            MtCommonExtendVO5 attr12 = new MtCommonExtendVO5();
            attr12.setAttrName("AVG_WAVE_LENGTH");
            attr12.setAttrValue(hmeCosRetestV08.getAverageWavelength() != null ? hmeCosRetestV08.getAverageWavelength().toString() : "");
            attrList.add(attr12);
            mtCommonExtendVO6.setAttrs(attrList);
            attrPropertyList.add(mtCommonExtendVO6);
        }
        // 批量插入EoJobSn
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            hmeEoJobSnRepository.myBatchInsert(hmeEoJobSnList);
        }
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
        }
        // 更新扩展字段
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        // 跟新装载信息工单
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
                List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(materialLotLoadList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                    hmeCosRetestMapper.batchLoadUpdate(tenantId, userId, domains);
                }
            }
        }
    }

    private void verifyBackFactoryMaterialLot(Long tenantId, MtMaterialLot mtMaterialLot, String workOrderId) {
        // 校验有效性
        if (!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_001", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 查询条码扩展字段
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        //校验在制品
        String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        if (StringUtils.equals(HmeConstants.ConstantValue.YES, mfFlag)) {
            throw new MtException("HME_COS_BARCODE_RETEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 校验物料
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        // 获取工单组件的芯片物料
        String cosMaterialId = hmeCosRetestMapper.queryCosMaterialByBomId(tenantId, mtWorkOrder.getBomId(), mtWorkOrder.getSiteId());
        if (!StringUtils.equals(cosMaterialId, mtMaterialLot.getMaterialId())) {
            throw new MtException("HME_COS_BARCODE_RETEST_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_014", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 校验条码是否冻结/盘点停用
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
    }


    private void createTransaction(Long tenantId, List<WmsTransactionType> wmsTransactionTypeList, BigDecimal totalDemand, BigDecimal feedDemand, MtMaterialLot sourceMaterialLot, String bomComponentId, MtWorkOrder mtWorkOrder, String locatorId, Double totalCosNum, String eventId) {
        List<WmsObjectTransactionRequestVO> transactionList = new ArrayList<>();
        if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(totalCosNum)) >= 0) {
            // 工单组件总需求数量-工单组件投料数量>=拆分数量  计划内投料事务 事务数量：拆分数量
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
            transactionRequestVO.setTransactionQty(BigDecimal.valueOf(totalCosNum));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                transactionRequestVO.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            if (StringUtils.isNotBlank(bomComponentId)) {
                //行号
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                transactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                //bomReserveNum
                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_bom_component_attr");
                extendVO.setKeyId(bomComponentId);
                extendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    transactionRequestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                }
            }
            transactionList.add(transactionRequestVO);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) > 0 && totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(totalCosNum)) < 0) {
            // 0<工单组件总需求数量-工单组件投料数量<拆分数量 做两笔事务
            // 计划内投料 事务数量:工单组件总需求数量-工单组件投料数量
            WmsObjectTransactionRequestVO woIssueTransaction = new WmsObjectTransactionRequestVO();
            woIssueTransaction.setTransactionTypeCode("HME_WO_ISSUE");
            woIssueTransaction.setTransactionQty(totalDemand.subtract(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                woIssueTransaction.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            if (StringUtils.isNotBlank(bomComponentId)) {
                //行号
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                woIssueTransaction.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                //bomReserveNum
                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_bom_component_attr");
                extendVO.setKeyId(bomComponentId);
                extendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    woIssueTransaction.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                }
            }

            // 计划外投料 事务数量:拆分数量-（工单组件总需求数量-工单组件投料数量）
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(totalCosNum).subtract(totalDemand).add(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) <= 0) {
            // 工单组件总需求数量-工单组件投料数量<=0  计划外投料事务 事务数量：拆分数量
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(totalCosNum));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        }
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        // 当前时间
        Date currentDate = CommonUtils.currentTimeGet();
        // 查询产线信息
        MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());

        for (WmsObjectTransactionRequestVO requestVO : transactionList) {
            // 记录计划内/计划外投料事务
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
            transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
            transactionRequestVO.setMoveType(requestVO.getMoveType());
            transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
            transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
            transactionRequestVO.setEventId(eventId);
            transactionRequestVO.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            transactionRequestVO.setMaterialId(sourceMaterialLot.getMaterialId());
            transactionRequestVO.setLotNumber(sourceMaterialLot.getLot());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
            transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
            transactionRequestVO.setTransactionTime(currentDate);
            transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
            transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
            transactionRequestVO.setLocatorId(locatorId);
            transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : null);
            transactionRequestVO.setRemark("COS报废复测投料");
            transactionRequestVOList.add(transactionRequestVO);
        }

        if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
        }
    }

    @Override
    public List<String> createBatchGenerateNum(Long tenantId, Integer numQty, HmeCosRetestVO5 hmeCosRetestVO5) {
        List<MtNumrangeVO11> mtNumrangeVO11List = this.createRuleCode(tenantId, numQty, hmeCosRetestVO5);

        MtNumrangeVO9 dto = new MtNumrangeVO9();
        // 调用API{ numrangeBatchGenerate}
        dto.setObjectCode("COS_PASTER_CODE");

        dto.setIncomingValueList(mtNumrangeVO11List);
        dto.setObjectNumFlag(HmeConstants.ConstantValue.YES);
        dto.setNumQty(Long.valueOf(numQty));

        MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, dto);

        return mtNumrangeVO8.getNumberList();
    }

    private List<MtNumrangeVO11> createRuleCode(Long tenantId, Integer numQty, HmeCosRetestVO5 hmeCosRetestVO5) {
        List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList<>();

        String siteId = hmeCosRetestVO5.getSiteId();
        String productionLineId = hmeCosRetestVO5.getProdLineId();

        //获取工厂简码
        List<HmeSnBindEoVO2> siteCodeList = hmeSnBindEoRepository.modSiteAttrValueGet(tenantId, Collections.singletonList(siteId));

        //获取生产线简码
        List<MtExtendAttrVO> proLineInfoList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setKeyId(productionLineId);
            setTableName("mt_mod_production_line_attr");
            setAttrName("SHORT_NAME");
        }});

        //当前年 后两位
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
        //月：1-9，A-Z 获取的月比真实少一个月
        String month = hmeSnBindEoRepository.handleMonth(calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(day);
        if (day/10 < 1) {
            dayStr = "0" + day;
        }
        // 热沉类型
        if (StringUtils.isBlank(hmeCosRetestVO5.getHotSinkType())) {
            throw new MtException("HME_COS_BARCODE_RETEST_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_018", "HME"));
        }
        // cos类型
        if (StringUtils.isBlank(hmeCosRetestVO5.getCosType())) {
            throw new MtException("HME_COS_BARCODE_RETEST_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_019", "HME"));
        }

        Long index = 0L;
        for (int i= 0; i < numQty; i++) {
            StringBuffer codeStr = new StringBuffer();
            // 工厂
            if (CollectionUtils.isEmpty(siteCodeList)) {
                throw new MtException("HME_SN_BIND_EO_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SN_BIND_EO_001", "HME", ""));
            }
            //产线
            if (CollectionUtils.isEmpty(proLineInfoList)) {
                throw new MtException("HME_SN_BIND_EO_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SN_BIND_EO_002", "HME", ""));
            }

            codeStr.append(siteCodeList.get(0).getSiteCode())
                    .append(proLineInfoList.get(0).getAttrValue())
                    .append(year)
                    .append(month)
                    .append(dayStr)
                    .append(hmeCosRetestVO5.getHotSinkType())
                    .append(hmeCosRetestVO5.getCosType());

            String ruleCode = codeStr.toString();

            MtNumrangeVO11 vo11 = new MtNumrangeVO11();
            vo11.setSequence(index);
            List<String> valList = new ArrayList<>();
            valList.add(ruleCode);
            vo11.setIncomingValue(valList);
            mtNumrangeVO11List.add(vo11);

            index++;
        }
        return mtNumrangeVO11List;
    }

    private List<HmeCosRetestVO7> splitBarcode(Long tenantId, MtMaterialLot sourceMaterialLot, List<HmeCosRetestVO7> targetList, HmeCosRetestVO5 hmeCosRetestVO5, HmeCosOperationRecord hmeCosOperationRecord, HmeContainerCapacity hmeContainerCapacity, MtRouterOperation routerOperation, BigDecimal capacity, String eventId) {
        List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
        // 批量获取号码段
        List<String> numberList = this.createBatchGenerateNum(tenantId, targetList.size(), hmeCosRetestVO5);

        Integer numIndex = 0;
        for (HmeCosRetestVO7 hmeCosRetestVO7 : targetList) {
            // 20210723 modify by sanfeng.zhang for wenxin.zhang 拆分数量不得超出容量
            if (BigDecimal.valueOf(Double.valueOf(hmeCosRetestVO7.getCosNum())).compareTo(capacity) > 0) {
                throw new MtException("HME_COS_BARCODE_RETEST_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_026", "HME", String.valueOf(capacity)));
            }
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotCode(numberList.get(numIndex++));
            mtMaterialLotVO20.setSiteId(sourceMaterialLot.getSiteId());
            mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
            mtMaterialLotVO20.setQualityStatus(sourceMaterialLot.getQualityStatus());
            mtMaterialLotVO20.setMaterialId(sourceMaterialLot.getMaterialId());
            mtMaterialLotVO20.setPrimaryUomId(sourceMaterialLot.getPrimaryUomId());
            mtMaterialLotVO20.setPrimaryUomQty(Double.valueOf(hmeCosRetestVO7.getCosNum()));
            mtMaterialLotVO20.setLocatorId(sourceMaterialLot.getLocatorId());
            mtMaterialLotVO20.setLot(hmeCosRetestVO5.getLot());
            mtMaterialLotVO20.setSupplierId(sourceMaterialLot.getSupplierId());
            mtMaterialLotVO20.setInLocatorTime(sourceMaterialLot.getInLocatorTime());
            mtMaterialLotVO20.setLoadTime(sourceMaterialLot.getLoadTime());
            mtMaterialLotVO20.setUnloadTime(sourceMaterialLot.getUnloadTime());
            mtMaterialLotVO20.setCreateReason("INITIALIZE");
            updateMaterialLotList.add(mtMaterialLotVO20);
        }
        List<MtMaterialLotVO19> materialLotVO19List = new ArrayList<>();
        // 批量更新拆分条码
        if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
            materialLotVO19List = mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, eventId, HmeConstants.ConstantValue.NO);
        }
        // 查询来料条码扩展字段
        MtExtendVO1 codeExtend = new MtExtendVO1();
        codeExtend.setTableName("mt_material_lot_attr");
        codeExtend.setKeyIdList(Collections.singletonList(sourceMaterialLot.getMaterialLotId()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 cosTypeAttr = new MtExtendVO5();
        cosTypeAttr.setAttrName("COS_TYPE");
        attrs.add(cosTypeAttr);
        MtExtendVO5 waferAttr = new MtExtendVO5();
        waferAttr.setAttrName("WAFER_NUM");
        attrs.add(waferAttr);
        MtExtendVO5 dateAttr = new MtExtendVO5();
        dateAttr.setAttrName("PRODUCT_DATE");
        attrs.add(dateAttr);
        codeExtend.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, codeExtend);

        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        Date date = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<String> materialLotIdList = materialLotVO19List.stream().map(MtMaterialLotVO19::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        //批量获取EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", mtMaterialLots.size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", mtMaterialLots.size());
        Integer eoJobSnIndex = 0;
        List<HmeCosTestSelectCancle> hmeCosTestSelectCancleList = new ArrayList<>();
        for (MtMaterialLot mtMaterialLot : mtMaterialLots) {
            // 记录hme_eo_job_sn表
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(date);
            hmeEoJobSn.setSiteOutDate(date);
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setWorkcellId(hmeCosRetestVO5.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(hmeCosRetestVO5.getWorkOrderId());
            hmeEoJobSn.setOperationId(hmeCosRetestVO5.getOperationId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setSnMaterialId(sourceMaterialLot.getMaterialId());
            hmeEoJobSn.setSnQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            hmeEoJobSn.setJobType("COS_SCRAP_RETEST");
            hmeEoJobSn.setJobId(eoJobSnIdList.get(eoJobSnIndex));
            hmeEoJobSn.setCid(Long.parseLong(eoJobSnCidList.get(eoJobSnIndex++)));
            hmeEoJobSn.setObjectVersionNumber(1L);
            hmeEoJobSn.setCreatedBy(userId);
            hmeEoJobSn.setCreationDate(date);
            hmeEoJobSn.setLastUpdatedBy(userId);
            hmeEoJobSn.setLastUpdateDate(date);
            hmeEoJobSn.setShiftId(hmeCosRetestVO5.getWkcShiftId());
            Integer eoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeCosRetestVO5.getWorkcellId(), null, mtMaterialLot.getMaterialLotId(), "N", "COS_SCRAP_RETEST", hmeCosRetestVO5.getOperationId());
            hmeEoJobSn.setEoStepNum(eoStepNum + 1);
            hmeEoJobSn.setReworkFlag("N");
            Optional<MtExtendAttrVO1> cosTypeOpt = attrVO1List.stream().filter(attr -> StringUtils.equals(attr.getKeyId(), sourceMaterialLot.getMaterialLotId()) && StringUtils.equals(attr.getAttrName(), "COS_TYPE")).findFirst();
            if (cosTypeOpt.isPresent()) {
                hmeEoJobSn.setAttribute3(cosTypeOpt.get().getAttrValue());
            }
            hmeEoJobSn.setAttribute5(hmeCosRetestVO5.getWafer());
            hmeEoJobSn.setAttribute6(String.valueOf(mtMaterialLot.getPrimaryUomQty()));
            hmeEoJobSnList.add(hmeEoJobSn);

            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtMaterialLot.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            MtCommonExtendVO5 attrOne = new MtCommonExtendVO5();
            attrOne.setAttrName("WORK_ORDER_ID");
            attrOne.setAttrValue(hmeCosRetestVO5.getWorkOrderId());
            attrList.add(attrOne);
            MtCommonExtendVO5 attrTwo = new MtCommonExtendVO5();
            attrTwo.setAttrName("WORKING_LOT");
            attrTwo.setAttrValue(hmeCosRetestVO5.getJobBatch());
            attrList.add(attrTwo);
            MtCommonExtendVO5 attrThree = new MtCommonExtendVO5();
            attrThree.setAttrName("WAFER_NUM");
            attrThree.setAttrValue(hmeCosRetestVO5.getWafer());
            attrList.add(attrThree);
            MtCommonExtendVO5 attrFour = new MtCommonExtendVO5();
            attrFour.setAttrName("TYPE");
            attrFour.setAttrValue(hmeCosRetestVO5.getType());
            attrList.add(attrFour);
            MtCommonExtendVO5 attrFive = new MtCommonExtendVO5();
            attrFive.setAttrName("STATUS");
            attrFive.setAttrValue("INSTOCK");
            attrList.add(attrFive);
            MtCommonExtendVO5 attrSix = new MtCommonExtendVO5();
            attrSix.setAttrName("REMARK");
            attrSix.setAttrValue(hmeCosRetestVO5.getRemark());
            attrList.add(attrSix);
            MtCommonExtendVO5 attrSeven = new MtCommonExtendVO5();
            attrSeven.setAttrName("PRODUCT_DATE");
            Optional<MtExtendAttrVO1> dateOpt = attrVO1List.stream().filter(attr -> StringUtils.equals(attr.getKeyId(), sourceMaterialLot.getMaterialLotId()) && StringUtils.equals(attr.getAttrName(), "PRODUCT_DATE")).findFirst();
            if (dateOpt.isPresent()) {
                attrSeven.setAttrValue(dateOpt.get().getAttrValue());
            }
            attrList.add(attrSeven);
            MtCommonExtendVO5 attrEight = new MtCommonExtendVO5();
            attrEight.setAttrName("ORIGINAL_ID");
            attrEight.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrList.add(attrEight);
            MtCommonExtendVO5 attrNine = new MtCommonExtendVO5();
            attrNine.setAttrName("MF_FLAG");
            attrNine.setAttrValue(HmeConstants.ConstantValue.YES);
            attrList.add(attrNine);
            MtCommonExtendVO5 attrTen = new MtCommonExtendVO5();
            attrTen.setAttrName("LOTNO");
            attrTen.setAttrValue(hmeCosRetestVO5.getLotNo());
            attrList.add(attrTen);
            MtCommonExtendVO5 attrEleven = new MtCommonExtendVO5();
            attrEleven.setAttrName("CURRENT_ROUTER_STEP");
            attrEleven.setAttrValue(routerOperation.getRouterStepId());
            attrList.add(attrEleven);
            MtCommonExtendVO5 attrTwelve = new MtCommonExtendVO5();
            attrTwelve.setAttrName("COS_TYPE");
            attrTwelve.setAttrValue(hmeCosRetestVO5.getCosType());
            attrList.add(attrTwelve);
            MtCommonExtendVO5 attrThirteen = new MtCommonExtendVO5();
            attrThirteen.setAttrName("COS_RECORD");
            attrThirteen.setAttrValue(hmeCosOperationRecord.getOperationRecordId());
            attrList.add(attrThirteen);
            MtCommonExtendVO5 attrFourteen = new MtCommonExtendVO5();
            attrFourteen.setAttrName("AVG_WAVE_LENGTH");
            attrFourteen.setAttrValue(hmeCosRetestVO5.getAverageWavelength() != null ? hmeCosRetestVO5.getAverageWavelength().toString() : "");
            attrList.add(attrFourteen);
            MtCommonExtendVO5 attrFiveteen = new MtCommonExtendVO5();
            attrFiveteen.setAttrName("LOCATION_ROW");
            attrFiveteen.setAttrValue(String.valueOf(hmeContainerCapacity.getLineNum()));
            attrList.add(attrFiveteen);
            MtCommonExtendVO5 attrSixteen = new MtCommonExtendVO5();
            attrSixteen.setAttrName("LOCATION_COLUMN");
            attrSixteen.setAttrValue(String.valueOf(hmeContainerCapacity.getColumnNum()));
            attrList.add(attrSixteen);
            MtCommonExtendVO5 attrSeventeen = new MtCommonExtendVO5();
            attrSeventeen.setAttrName("CHIP_NUM");
            attrSeventeen.setAttrValue(String.valueOf(hmeContainerCapacity.getCapacity()));
            attrList.add(attrSeventeen);
            MtCommonExtendVO5 attrEighteen = new MtCommonExtendVO5();
            attrEighteen.setAttrName("CONTAINER_TYPE");
            attrEighteen.setAttrValue(String.valueOf(hmeCosRetestVO5.getContainerTypeCode()));
            attrList.add(attrEighteen);
            mtCommonExtendVO6.setAttrs(attrList);
            attrPropertyList.add(mtCommonExtendVO6);

            HmeCosTestSelectCancle cosTestSelectCancle = new HmeCosTestSelectCancle();
            cosTestSelectCancle.setWafer(hmeCosRetestVO5.getWafer());
            cosTestSelectCancle.setTenantId(tenantId);
            hmeCosTestSelectCancleList.add(cosTestSelectCancle);
        }
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            hmeEoJobSnRepository.myBatchInsert(hmeEoJobSnList);
        }
        // 更新/新增条码扩展字段
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        if (CollectionUtils.isNotEmpty(hmeCosTestSelectCancleList)) {
            hmeCosTestSelectCancleRepository.batchInsertSelective(hmeCosTestSelectCancleList);
        }
        List<String> materialLotId = materialLotVO19List.stream().map(MtMaterialLotVO19::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotId);
        // 生成装载信息
        this.setMaterialLotLoad(tenantId, hmeContainerCapacity, materialLotList, hmeCosOperationRecord, hmeCosRetestVO5);
        List<HmeCosRetestVO7> hmeCosRetestVO7List = materialLotList.stream().map(ml -> {
            HmeCosRetestVO7 hmeCosRetestVO7 = new HmeCosRetestVO7();
            hmeCosRetestVO7.setMaterialLotId(ml.getMaterialLotId());
            hmeCosRetestVO7.setMaterialLotCode(ml.getMaterialLotCode());
            hmeCosRetestVO7.setCosNum(ml.getPrimaryUomQty());
            return hmeCosRetestVO7;
        }).collect(Collectors.toList());
        return hmeCosRetestVO7List;
    }

    /**
     * 保存装载信息
     *
     * @param tenantId
     * @param hmeContainerCapacity
     * @param mtMaterialLotList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/4 9:16
     */
    private void setMaterialLotLoad(Long tenantId, HmeContainerCapacity hmeContainerCapacity, List<MtMaterialLot> mtMaterialLotList, HmeCosOperationRecord hmeCosOperationRecord, HmeCosRetestVO5 hmeCosRetestVO5) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
        String data = sdf.format(new Date());
        for (MtMaterialLot mtMaterialLot : mtMaterialLotList) {
            // 容器容量默认为1 按条码数量生成格子
            int num = (int) Math.ceil(mtMaterialLot.getPrimaryUomQty());
            if (StringUtils.isEmpty(hmeContainerCapacity.getAttribute1()) || "A".equals(hmeContainerCapacity.getAttribute1())) {
                for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                    for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadRow((long) i);
                        hmeMaterialLotLoad.setLoadColumn((long) j);
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                        hmeMaterialLotLoad.setCosNum(1L);
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                        hmeMaterialLotLoad.setAttribute6(hmeCosRetestVO5.getHotSinkSupplierLot());
                        hmeMaterialLotLoad.setAttribute9(hmeCosRetestVO5.getGoldSupplierLot());
                        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                        num--;
                        // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                        createLoadJob(tenantId, hmeMaterialLotLoad, hmeCosRetestVO5, "COS_SCRAP_RETEST");

                        if (num == 0) {
                            break;
                        }
                    }
                    if (num == 0) {
                        break;
                    }
                }
            } else if ("B".equals(hmeContainerCapacity.getAttribute1())) {
                for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                    for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadRow((long) i);
                        hmeMaterialLotLoad.setLoadColumn((long) j);
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                        hmeMaterialLotLoad.setCosNum(1L);
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                        hmeMaterialLotLoad.setAttribute6(hmeCosRetestVO5.getHotSinkSupplierLot());
                        hmeMaterialLotLoad.setAttribute9(hmeCosRetestVO5.getGoldSupplierLot());
                        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                        num--;
                        // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                        createLoadJob(tenantId, hmeMaterialLotLoad, hmeCosRetestVO5, "COS_SCRAP_RETEST");

                        if (num == 0) {
                            break;
                        }
                    }
                    if (num == 0) {
                        break;
                    }
                }
            } else if ("C".equals(hmeContainerCapacity.getAttribute1())) {
                for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                    for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadRow((long) i);
                        hmeMaterialLotLoad.setLoadColumn((long) j);
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                        hmeMaterialLotLoad.setCosNum(1L);
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                        hmeMaterialLotLoad.setAttribute6(hmeCosRetestVO5.getHotSinkSupplierLot());
                        hmeMaterialLotLoad.setAttribute9(hmeCosRetestVO5.getGoldSupplierLot());
                        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                        num--;
                        // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                        createLoadJob(tenantId, hmeMaterialLotLoad, hmeCosRetestVO5, "COS_SCRAP_RETEST");

                        if (num == 0) {
                            break;
                        }
                    }
                    if (num == 0) {
                        break;
                    }
                }
            } else if ("D".equals(hmeContainerCapacity.getAttribute1())) {
                for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                    for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadRow((long) i);
                        hmeMaterialLotLoad.setLoadColumn((long) j);
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                        hmeMaterialLotLoad.setCosNum(1L);
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                        hmeMaterialLotLoad.setAttribute6(hmeCosRetestVO5.getHotSinkSupplierLot());
                        hmeMaterialLotLoad.setAttribute9(hmeCosRetestVO5.getGoldSupplierLot());
                        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                        num--;
                        // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                        createLoadJob(tenantId, hmeMaterialLotLoad, hmeCosRetestVO5, "COS_SCRAP_RETEST");

                        if (num == 0) {
                            break;
                        }
                    }
                    if (num == 0) {
                        break;
                    }
                }
            } else {
                for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                    for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadRow((long) i);
                        hmeMaterialLotLoad.setLoadColumn((long) j);
                        hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                        hmeMaterialLotLoad.setCosNum(1L);
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setAttribute6(hmeCosRetestVO5.getHotSinkSupplierLot());
                        hmeMaterialLotLoad.setAttribute9(hmeCosRetestVO5.getGoldSupplierLot());
                        hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                        num--;
                        // Add、by 田欣 20210915 把信息记录到装载信息作业记录表
                        createLoadJob(tenantId, hmeMaterialLotLoad, hmeCosRetestVO5, "COS_SCRAP_RETEST");

                        if (num == 0) {
                            break;
                        }
                    }
                    if (num == 0) {
                        break;
                    }
                }
            }
        }
    }
    public void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeCosRetestVO5 dto, String loadJobType) {
        HmeLoadJob hmeLoadJob = new HmeLoadJob();
        hmeLoadJob.setTenantId(tenantId);
        hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
        hmeLoadJob.setLoadJobType(loadJobType);
        hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
        hmeLoadJob.setSiteId(mtMaterialLot.getSiteId());
        hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
        hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
        hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
        hmeLoadJob.setCosNum(1L);
        hmeLoadJob.setOperationId(dto.getOperationId());
        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
        hmeLoadJob.setWorkOrderId(dto.getWorkOrderId());
        hmeLoadJob.setWaferNum(dto.getWafer());
        hmeLoadJob.setCosType(dto.getCosType());
        hmeLoadJob.setStatus("0");
        hmeLoadJobRepository.insertSelective(hmeLoadJob);
    }

    private void checkNonEmpty(Long tenantId, HmeCosRetestVO5 hmeCosRetestVO5) {
        // 校验：抬头字段WAFER、容器类型、COS类型以及条码清单内条码个数字段必输校验，若未输入，提示对应字段未输入报错！
        // WAFER
        if (StringUtils.isBlank(hmeCosRetestVO5.getWafer())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "WAFER"));
        }
        // 容器类型
        if (StringUtils.isBlank(hmeCosRetestVO5.getContainerTypeCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "容器类型"));
        }
        // COS类型
        if (StringUtils.isBlank(hmeCosRetestVO5.getCosType())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "COS类型"));
        }
        // 清单内条码数量
        if (CollectionUtils.isEmpty(hmeCosRetestVO5.getTargetList())) {
            throw new MtException("HME_COS_BARCODE_RETEST_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_012", "HME"));
        }
        Optional<HmeCosRetestVO7> cosNumOpt = hmeCosRetestVO5.getTargetList().stream().filter(tar -> tar.getCosNum() == null).findFirst();
        if (cosNumOpt.isPresent()) {
            throw new MtException("HME_COS_BARCODE_RETEST_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_013", "HME"));
        }
    }

    private void verifyMaterialLot(Long tenantId, MtMaterialLot materialLot, HmeCosRetestVO vo, HmeCosRetestVO2 retestVO2) {
        //校验有效性
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_001", "HME", materialLot.getMaterialLotCode()));
        }
        //校验物料一致
        if (!StringUtils.equals(vo.getMaterialId(), materialLot.getMaterialId())) {
            throw new MtException("HME_COS_BARCODE_RETEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_002", "HME", materialLot.getMaterialLotCode()));
        }
        // 校验条码是否冻结/盘点停用
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", materialLot.getMaterialLotCode()));
        }
        //查询条码的扩展字段
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_material_lot_attr");
        mtExtendVO1.setKeyIdList(Collections.singletonList(materialLot.getMaterialLotId()));
        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 mfFlagAttr = new MtExtendVO5();
        mfFlagAttr.setAttrName("MF_FLAG");
        attrList.add(mfFlagAttr);
        MtExtendVO5 waferNumAttr = new MtExtendVO5();
        waferNumAttr.setAttrName("WAFER_NUM");
        attrList.add(waferNumAttr);
        MtExtendVO5 cosTypeAttr = new MtExtendVO5();
        cosTypeAttr.setAttrName("COS_TYPE");
        attrList.add(cosTypeAttr);
        mtExtendVO1.setAttrs(attrList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        //校验在制品
        Optional<MtExtendAttrVO1> mfFlagOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("MF_FLAG", dto.getAttrName())).findFirst();
        String mfFlag = mfFlagOpt.isPresent() ? mfFlagOpt.get().getAttrValue() : "";
        if (StringUtils.equals(HmeConstants.ConstantValue.YES, mfFlag)) {
            throw new MtException("HME_COS_BARCODE_RETEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_004", "HME", materialLot.getMaterialLotCode()));
        }
        //WAFER
        Optional<MtExtendAttrVO1> waferOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("WAFER_NUM", dto.getAttrName())).findFirst();
        retestVO2.setWaferNum(waferOpt.isPresent() ? waferOpt.get().getAttrValue() : "");
        // COS类型
        Optional<MtExtendAttrVO1> cosTypeOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("COS_TYPE", dto.getAttrName())).findFirst();
        retestVO2.setCosType(cosTypeOpt.isPresent() ? cosTypeOpt.get().getAttrValue() : "");
    }

}
