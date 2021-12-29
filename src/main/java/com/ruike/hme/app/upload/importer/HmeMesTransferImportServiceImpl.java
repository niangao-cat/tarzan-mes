package com.ruike.hme.app.upload.importer;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeMesTransformDTO;
import com.ruike.hme.domain.repository.HmeWorkOrderManagementRepository;
import com.ruike.hme.domain.vo.HmeBomComponentTrxVO;
import com.ruike.hme.domain.vo.HmeRouterOperationVO;
import com.ruike.hme.infra.util.JsonUtils;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.actual.domain.vo.MtWorkOrderActualVO8;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO29;
import tarzan.order.domain.vo.MtWorkOrderVO4;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.Event.WO_MATERIAL_CHANGE;
import static com.ruike.hme.infra.constant.HmeConstants.Event.WO_MATERIAL_CHANGE_A;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.MATERIAL_LOT;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.*;
import static org.hzero.core.base.BaseConstants.DEFAULT_TENANT_ID;

/**
 * <p>
 * MES转编码导入处理服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 11:38
 */
@ImportService(templateCode = "HME.MES_TRANSFORM")
public class HmeMesTransferImportServiceImpl implements IBatchImportService {

    private final MtModSiteRepository siteRepository;
    private final MtWorkOrderRepository workOrderRepository;
    private final MtMaterialLotRepository materialLotRepository;
    private final MtMaterialRepository materialRepository;
    private final MtContainerRepository containerRepository;
    private final MtModLocatorRepository locatorRepository;
    private final WmsEventService wmsEventService;
    private final MtWorkOrderComponentActualRepository workOrderComponentActualRepository;
    private final MtInvOnhandQuantityRepository invOnhandQuantityRepository;
    private final MtWorkOrderActualRepository workOrderActualRepository;
    private final HmeWorkOrderManagementRepository workOrderManagementRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final MtUomRepository uomRepository;
    private final MtModProductionLineRepository productionLineRepository;
    private final WmsTransactionTypeRepository transactionTypeRepository;
    private final ItfWorkOrderIfaceService workOrderIfaceService;

    public HmeMesTransferImportServiceImpl(MtModSiteRepository siteRepository, MtWorkOrderRepository workOrderRepository, MtMaterialLotRepository materialLotRepository, MtMaterialRepository materialRepository, MtContainerRepository containerRepository, MtModLocatorRepository locatorRepository, WmsEventService wmsEventService, MtWorkOrderComponentActualRepository workOrderComponentActualRepository, MtInvOnhandQuantityRepository invOnhandQuantityRepository, MtWorkOrderActualRepository workOrderActualRepository, HmeWorkOrderManagementRepository workOrderManagementRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, MtUomRepository uomRepository, MtModProductionLineRepository productionLineRepository, WmsTransactionTypeRepository transactionTypeRepository, ItfWorkOrderIfaceService workOrderIfaceService) {
        this.siteRepository = siteRepository;
        this.workOrderRepository = workOrderRepository;
        this.materialLotRepository = materialLotRepository;
        this.materialRepository = materialRepository;
        this.containerRepository = containerRepository;
        this.locatorRepository = locatorRepository;
        this.wmsEventService = wmsEventService;
        this.workOrderComponentActualRepository = workOrderComponentActualRepository;
        this.invOnhandQuantityRepository = invOnhandQuantityRepository;
        this.workOrderActualRepository = workOrderActualRepository;
        this.workOrderManagementRepository = workOrderManagementRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.uomRepository = uomRepository;
        this.productionLineRepository = productionLineRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.workOrderIfaceService = workOrderIfaceService;
    }

    @Override
    public Boolean doImport(List<String> data) {
        List<HmeMesTransformDTO> transformList = new ArrayList<>();
        data.forEach(rec -> {
            HmeMesTransformDTO transform = JsonUtils.jsonToObject(rec, HmeMesTransformDTO.class);
            if (Objects.isNull(transform)) {
                throw new CommonException("数据读取失败");
            }
            transformList.add(transform);
        });
        // 验证条码是否有重复的
        List<String> materialLotList = transformList.stream().map(HmeMesTransformDTO::getMaterialLotCode).collect(Collectors.toList());
        if (materialLotList.size() > materialLotList.stream().distinct().count()) {
            throw new CommonException("本次导入了重复的物料批数据，请检查");
        }

        CustomUserDetails details = DetailsHelper.getUserDetails();
        Long tenantId = Optional.ofNullable(details.getTenantId()).orElse(DEFAULT_TENANT_ID);

        // 翻译数据
        this.processTranslate(tenantId, transformList);

        // 执行
        this.processExecution(tenantId, transformList);

        return true;
    }

    /**
     * 处理执行逻辑
     *
     * @param tenantId 租户
     * @param list     导入数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 02:13:45
     */
    private void processExecution(Long tenantId, List<HmeMesTransformDTO> list) {
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, WO_MATERIAL_CHANGE);
        WmsEventVO onhandEvent = wmsEventService.createEventByRequestId(tenantId, WO_MATERIAL_CHANGE_A, event.getEventRequestId());
        List<MtMaterialLotVO20> materialLotUpdateList = new ArrayList<>();
        // 现有量批量更新
        MtInvOnhandQuantityVO16 onHandUpdate = new MtInvOnhandQuantityVO16();
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        onHandUpdate.setEventId(onhandEvent.getEventId());
        onHandUpdate.setOnhandList(onhandList);
        // 容器批量装载
        List<MtContainerVO9> containerVerifyList = new ArrayList<>();
        MtContainerVO30 containerUpdate = new MtContainerVO30();
        List<MtContainerVO31> containerLoadList = new ArrayList<>();
        containerUpdate.setEventRequestId(event.getEventRequestId());
        containerUpdate.setContainerLoadList(containerLoadList);
        Map<String, Double> woQtyMap = new HashMap<>(list.size());
        Map<String, Double> woAssemblyMap = list.stream().collect(Collectors.toMap(rec -> rec.getWorkOrderId() + "," + rec.getMaterialId(), rec -> rec.getBom().getAssembleQty(), (a, b) -> a));
        String batchId = Utils.getBatchId();
        list.forEach(rec -> {
            // 组件投料
            this.componentFeed(tenantId, event, rec);

            // 工单装配
            this.woComponentAssemble(tenantId, event, rec);

            // 记录投料事务和报工完工事务
            this.addInvTransaction(tenantId, event.getEventId(), rec, batchId, woAssemblyMap);

            // 准备批量处理的数据
            this.prepareForBatchTrx(materialLotUpdateList, onhandList, containerVerifyList, containerLoadList, rec);

            woQtyMap.put(rec.getWorkOrderId(), woQtyMap.getOrDefault(rec.getWorkOrderId(), 0.0D) + rec.getPrimaryUomQty());
        });

        // 批量物料批更新
        materialLotRepository.materialLotBatchUpdate(tenantId, materialLotUpdateList, event.getEventId(), NO);

        // 批量现有量更新
        invOnhandQuantityRepository.onhandQtyBatchUpdate(tenantId, onHandUpdate);

        // 找出所有容器不为空的数据，进行容器批量装载验证之后执行装载
        if (CollectionUtils.isNotEmpty(containerVerifyList)) {
            containerRepository.containerLoadBatchVerify(tenantId, containerVerifyList);
            containerRepository.containerBatchLoad(tenantId, containerUpdate);
        }

        // MES中工单完工
        this.woComplete(tenantId, event, woQtyMap);
    }

    private void woComplete(Long tenantId, WmsEventVO event, Map<String, Double> woQtyMap) {
        // 根据生产指令验证结果执行不同的逻辑
        MtWorkOrderActualVO8 actualUpdate = new MtWorkOrderActualVO8();
        List<MtWorkOrderActualVO8.ActualInfo> actualInfoList = new ArrayList<>();
        List<String> workOrderIds = new ArrayList<>();
        actualUpdate.setEventId(event.getEventId());
        actualUpdate.setActualInfoList(actualInfoList);
        woQtyMap.forEach((workOrderId, qty) -> {
            MtWorkOrderVO29 woVerify = new MtWorkOrderVO29();
            woVerify.setWorkOrderId(workOrderId);
            woVerify.setTrxCompletedQty(qty);
            // 判断工单是否通过校验
            boolean passFlag = true;
            try {
                workOrderRepository.woCompleteVerify(tenantId, woVerify);
            } catch (MtException e) {
                passFlag = false;
            }
            if (!passFlag) {
                actualInfoList.add(new MtWorkOrderActualVO8.ActualInfo() {{
                    setWorkOrderId(workOrderId);
                    setCompletedQty(qty);
                    setReleasedQty(qty);
                }});
            } else {
                // 生产指令完成
                workOrderRepository.woComplete(tenantId, new MtWorkOrderVO4() {{
                    setWorkOrderId(workOrderId);
                    setTrxCompletedQty(qty);
                }});
                workOrderIds.add(workOrderId);
            }
        });

        // 工单状态回传接口
        workOrderIfaceService.erpWorkOrderStatusReturnRestProxy(tenantId, workOrderIds);

        // 批量执行工单实际更新
        if (CollectionUtils.isNotEmpty(actualInfoList)) {
            workOrderActualRepository.woActualBatchUpdate(tenantId, actualUpdate, NO);
        }
    }

    private void prepareForBatchTrx(List<MtMaterialLotVO20> materialLotUpdateList, List<MtInvOnhandQuantityVO13> onhandList, List<MtContainerVO9> containerVerifyList, List<MtContainerVO31> containerLoadList, HmeMesTransformDTO rec) {
        // 准备批量更新数据
        materialLotUpdateList.add(new MtMaterialLotVO20() {{
            setMaterialLotId(rec.getMaterialLotId());
            setMaterialId(rec.getTransformMaterialId());
            setEnableFlag(YES);
            setTrxPrimaryUomQty(rec.getPrimaryUomQty());
        }});

        onhandList.add(new MtInvOnhandQuantityVO13() {{
            setSiteId(rec.getSiteId());
            setMaterialId(rec.getTransformMaterialId());
            setLocatorId(rec.getLocatorId());
            setLotCode(rec.getLotCode());
            setChangeQuantity(rec.getPrimaryUomQty());
        }});

        // 若存在容器则需要执行容器装载和转移功能
        if (StringUtils.isNotBlank(rec.getContainerId())) {
            // 批量验证容器装载
            containerVerifyList.add(new MtContainerVO9() {{
                setContainerId(rec.getContainerId());
                setLoadObjectType(MATERIAL_LOT);
                setLoadObjectId(rec.getMaterialLotId());
            }});

            // 批量装载容器
            containerLoadList.add(new MtContainerVO31() {{
                setContainerId(rec.getContainerId());
                setLoadObjectType(MATERIAL_LOT);
                setLoadObjectId(rec.getMaterialLotId());
            }});
        }
    }

    private void woComponentAssemble(Long tenantId, WmsEventVO event, HmeMesTransformDTO rec) {
        workOrderComponentActualRepository.woComponentAssemble(tenantId, new MtWoComponentActualVO1() {{
            setAssembleExcessFlag(NO);
            setBomComponentId(rec.getBom().getBomComponentId());
            setEventRequestId(event.getEventRequestId());
            setMaterialId(rec.getMaterialId());
            setParentEventId(event.getEventId());
            setTrxAssembleQty(rec.getPrimaryUomQty());
            setWorkOrderId(rec.getWorkOrderId());
            setRouterStepId(rec.getRouterStep().getRouterStepId());
            setOperationId(rec.getRouterStep().getOperationId());
        }});
    }

    private void componentFeed(Long tenantId, WmsEventVO event, HmeMesTransformDTO rec) {
        materialLotRepository.materialLotConsume(tenantId, new MtMaterialLotVO1() {{
            setEventRequestId(event.getEventRequestId());
            setMaterialLotId(rec.getMaterialLotId());
            setTrxPrimaryUomQty(rec.getPrimaryUomQty());
        }});
    }

    private void addInvTransaction(Long tenantId, String eventId, HmeMesTransformDTO dto, String batchId, Map<String, Double> woAssemblyMap) {
        Date nowDate = DateUtil.date();
        HmeBomComponentTrxVO bom = dto.getBom();

        List<WmsObjectTransactionRequestVO> list = new ArrayList<>();
        // 公共字段
        WmsObjectTransactionRequestVO request = new WmsObjectTransactionRequestVO();
        request.setEventId(eventId);
        request.setMaterialLotId(dto.getMaterialLotId());
        request.setMaterialId(dto.getMaterialId());
        request.setLotNumber(dto.getLotCode());
        request.setTransactionUom(dto.getUomCode());
        request.setTransactionTime(nowDate);
        request.setPlantId(dto.getSiteId());
        request.setWarehouseId(dto.getWarehouseId());
        request.setLocatorId(dto.getLocatorId());
        request.setWorkOrderNum(dto.getWorkOrderNum());
        request.setMergeFlag(NO);
        request.setSoNum(bom.getSoNum());
        request.setSoLineNum(bom.getSoLineNum());
        request.setContainerId(dto.getContainerId());

        // 投料事务
        this.feedTransaction(tenantId, dto, list, request, bom, woAssemblyMap);

        // 报工和完工事务
        this.reportAndCompleteTransaction(tenantId, batchId, dto, list, request, bom);

        wmsObjectTransactionRepository.objectTransactionSync(tenantId, list);
    }

    private void reportAndCompleteTransaction(Long tenantId, String batchId, HmeMesTransformDTO dto, List<WmsObjectTransactionRequestVO> list, WmsObjectTransactionRequestVO request, HmeBomComponentTrxVO bom) {
        // 增加报工事务
        WmsObjectTransactionRequestVO woReportRequest = new WmsObjectTransactionRequestVO();
        BeanUtils.copyProperties(request, woReportRequest);
        woReportRequest.setTransactionTypeCode(HME_WORK_REPORT);
        woReportRequest.setMaterialId(dto.getTransformMaterialId());
        woReportRequest.setOperationSequence(dto.getRouterStep().getStepName());
        woReportRequest.setProdLineCode(dto.getProdLineCode());
        woReportRequest.setAttribute16(batchId);
        woReportRequest.setTransactionQty(BigDecimal.valueOf(dto.getPrimaryUomQty()));
        woReportRequest.setMoveType(transactionTypeRepository.getTransactionType(tenantId, woReportRequest.getTransactionTypeCode()).getMoveType());
        list.add(woReportRequest);

        // 增加完工事务
        WmsObjectTransactionRequestVO woCompleteRequest = new WmsObjectTransactionRequestVO();
        BeanUtils.copyProperties(woReportRequest, woCompleteRequest);
        woCompleteRequest.setTransactionTypeCode(HME_WO_COMPLETE);
        woCompleteRequest.setMoveType(transactionTypeRepository.getTransactionType(tenantId, woCompleteRequest.getTransactionTypeCode()).getMoveType());
        woCompleteRequest.setAttribute30(bom.getProductLineNum());
        list.add(woCompleteRequest);
    }

    private void feedTransaction(Long tenantId, HmeMesTransformDTO dto, List<WmsObjectTransactionRequestVO> list, WmsObjectTransactionRequestVO request, HmeBomComponentTrxVO bom, Map<String, Double> woAssemblyMap) {
        String assemblyKey = dto.getWorkOrderId() + "," + dto.getMaterialId();
        double assemblyQty = woAssemblyMap.get(assemblyKey);
        // 计算计划内数量，装配数量已经大于需求数量时为0
        // 否则判断增加条码数量后是否大于需求数量，是则将需求数量-装配数量记入计划内数量，否则条码数量全部计入计划内
        double planQty = assemblyQty > bom.getDemandQty() ? 0 : (assemblyQty + dto.getPrimaryUomQty() > bom.getDemandQty() ? bom.getDemandQty() - assemblyQty : dto.getPrimaryUomQty());
        // 计算计划外数量，装配数量已经大于需求数量时为条码数量
        // 否则判断增加条码数量后是否大于需求数量，是则将装配数量+条码数量-需求数量计入，否则记为0
        double outPlanQty = assemblyQty > bom.getDemandQty() ? dto.getPrimaryUomQty() : (assemblyQty + dto.getPrimaryUomQty() > bom.getDemandQty() ? assemblyQty + dto.getPrimaryUomQty() - bom.getDemandQty() : 0);
        // 新增一笔计划内投料事务
        if (planQty > 0) {
            WmsObjectTransactionRequestVO inRequest = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(request, inRequest);
            inRequest.setTransactionTypeCode(HME_WO_ISSUE);
            inRequest.setMoveType(transactionTypeRepository.getTransactionType(tenantId, inRequest.getTransactionTypeCode()).getMoveType());
            inRequest.setTransactionQty(BigDecimal.valueOf(planQty));
            inRequest.setMoveReason("转型工单导入计划内投料");
            inRequest.setBomReserveNum(bom.getBomReserveNum());
            inRequest.setBomReserveLineNum(bom.getBomReserveLineNum());
            list.add(inRequest);
        }

        // 新增一笔计划外投料事务
        if (outPlanQty > 0) {
            WmsObjectTransactionRequestVO outRequest = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(request, outRequest);
            outRequest.setTransactionTypeCode(HME_WO_ISSUE_EXT);
            outRequest.setMoveType(transactionTypeRepository.getTransactionType(tenantId, outRequest.getTransactionTypeCode()).getMoveType());
            outRequest.setTransactionQty(BigDecimal.valueOf(outPlanQty));
            outRequest.setMoveReason("转型工单导入计划外投料");
            outRequest.setBomReserveNum(bom.getBomReserveNum());
            outRequest.setBomReserveLineNum(bom.getBomReserveLineNum());
            list.add(outRequest);
        }
        woAssemblyMap.put(assemblyKey, woAssemblyMap.getOrDefault(assemblyKey, 0.0D) + dto.getPrimaryUomQty());
    }

    /**
     * 翻译数据
     *
     * @param transformList 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 01:39:01
     */
    private void processTranslate(Long tenantId, List<HmeMesTransformDTO> transformList) {
        // 获取字段映射map
        Map<String, String> siteMap = siteRepository.selectByCondition(Condition.builder(MtModSite.class).andWhere(Sqls.custom().andIn(MtModSite.FIELD_SITE_CODE, transformList.stream().map(HmeMesTransformDTO::getSiteCode).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtModSite::getSiteCode, MtModSite::getSiteId, (a, b) -> a));

        Map<String, MtWorkOrder> workOrderMap = workOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class).andWhere(Sqls.custom().andIn(MtWorkOrder.FIELD_WORK_ORDER_NUM, transformList.stream().map(HmeMesTransformDTO::getWorkOrderNum).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderNum, a -> a, (a, b) -> a));

        Map<String, MtMaterialLot> materialLotMap = materialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom().andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, transformList.stream().map(HmeMesTransformDTO::getMaterialLotCode).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode, m -> m, (a, b) -> a));

        Map<String, String> locatorMap = locatorRepository.selectByCondition(Condition.builder(MtModLocator.class).andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_ID, materialLotMap.values().stream().map(MtMaterialLot::getLocatorId).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getParentLocatorId, (a, b) -> a));

        Map<String, String> materialMap = materialRepository.selectByCondition(Condition.builder(MtMaterial.class).andWhere(Sqls.custom().andIn(MtMaterial.FIELD_MATERIAL_CODE, transformList.stream().map(HmeMesTransformDTO::getTransformMaterial).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, MtMaterial::getMaterialId, (a, b) -> a));

        Set<String> containerCodes = transformList.stream().map(HmeMesTransformDTO::getContainerCode).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, String> containerMap = CollectionUtils.isNotEmpty(containerCodes) ? containerRepository.selectByCondition(Condition.builder(MtContainer.class).andWhere(Sqls.custom().andIn(MtContainer.FIELD_CONTAINER_CODE, containerCodes)).build()).stream().collect(Collectors.toMap(MtContainer::getContainerCode, MtContainer::getContainerId, (a, b) -> a)) : new HashMap<>();

        Map<String, String> uomMap = uomRepository.selectAll().stream().collect(Collectors.toMap(MtUom::getUomId, MtUom::getUomCode, (a, b) -> a));

        Map<String, String> prodLineMap = productionLineRepository.selectByCondition(Condition.builder(MtModProductionLine.class).andWhere(Sqls.custom().andIn(MtModProductionLine.FIELD_PROD_LINE_ID, workOrderMap.values().stream().map(MtWorkOrder::getProductionLineId).collect(Collectors.toSet()))).build()).stream().collect(Collectors.toMap(MtModProductionLine::getProdLineId, MtModProductionLine::getProdLineCode, (a, b) -> a));

        // 循环数据，从map中映射出转换值
        transformList.forEach(rec -> {
            MtMaterialLot materialLot = materialLotMap.get(rec.getMaterialLotCode());
            MtWorkOrder workOrder = workOrderMap.get(rec.getWorkOrderNum());
            HmeRouterOperationVO routerStep = workOrderManagementRepository.routerStepGetByWo(tenantId, workOrder.getWorkOrderId());
            rec.setSiteId(siteMap.get(rec.getSiteCode()));
            rec.setWorkOrderId(workOrder.getWorkOrderId());
            rec.setMaterialLotId(materialLot.getMaterialLotId());
            rec.setPrimaryUomQty(materialLot.getPrimaryUomQty());
            rec.setMaterialId(materialLot.getMaterialId());
            rec.setLocatorId(materialLot.getLocatorId());
            rec.setLotCode(materialLot.getLot());
            rec.setProdLineId(workOrder.getProductionLineId());
            rec.setRouterStep(Optional.ofNullable(routerStep).orElse(new HmeRouterOperationVO()));
            rec.setProdLineCode(prodLineMap.get(rec.getProdLineId()));
            rec.setUomCode(uomMap.get(materialLot.getPrimaryUomId()));
            rec.setWarehouseId(locatorMap.get(materialLot.getLocatorId()));
            rec.setTransformMaterialId(materialMap.getOrDefault(rec.getTransformMaterial(), null));
            rec.setContainerId(containerMap.getOrDefault(rec.getContainerCode(), materialLot.getCurrentContainerId()));
        });

        // 根据工单分组，获取其下的条码物料，查找到对应的bom行
        Map<String, Set<String>> woMaterialMap = transformList.stream().collect(Collectors.groupingBy(HmeMesTransformDTO::getWorkOrderId, Collectors.mapping(HmeMesTransformDTO::getMaterialId, Collectors.toSet())));
        Map<String, HmeBomComponentTrxVO> bomMap = new HashMap<>(transformList.size());
        woMaterialMap.forEach((workOrderId, materialIdList) -> {
            List<HmeBomComponentTrxVO> bomList = workOrderManagementRepository.bomMaterialTrxListGet(tenantId, workOrderId, new ArrayList<>(materialIdList));
            bomList.forEach(rec -> bomMap.put(workOrderId + "," + rec.getMaterialId(), rec));
        });
        transformList.forEach(rec -> rec.setBom(bomMap.getOrDefault(rec.getWorkOrderId() + "," + rec.getMaterialId(), new HmeBomComponentTrxVO())));
    }

}
