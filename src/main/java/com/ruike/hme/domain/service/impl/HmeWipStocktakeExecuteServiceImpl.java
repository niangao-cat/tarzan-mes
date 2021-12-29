package com.ruike.hme.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.assembler.WipStocktakeAssembler;
import com.ruike.hme.app.service.HmeWipStocktakeDocService;
import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.entity.HmeWipStocktakeActualHis;
import com.ruike.hme.domain.entity.HmeWipStocktakeDoc;
import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualHisRepository;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualRepository;
import com.ruike.hme.domain.repository.HmeWipStocktakeRangeRepository;
import com.ruike.hme.domain.service.HmeWipStocktakeExecuteService;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWipStocktakeDocMapper;
import com.ruike.hme.infra.mapper.HmeWipStocktakeExecuteMapper;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.repository.WmsContainerRepository;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsContainerVO;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.lov.feign.LovFeignClient;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.EventType.*;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.CONTAINER;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.MATERIAL_LOT;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.RELEASED;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeType.FIRST_COUNT;

/**
 * <p>
 * 在制品盘点执行 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:00
 */
@Service
public class HmeWipStocktakeExecuteServiceImpl implements HmeWipStocktakeExecuteService, AopProxy<HmeWipStocktakeExecuteServiceImpl> {
    private final HmeWipStocktakeExecuteMapper mapper;
    private final MtModProductionLineRepository productionLineRepository;
    private final MtModWorkcellRepository workcellRepository;
    private final HmeWipStocktakeRangeRepository stocktakeRangeRepository;
    private final WmsContainerRepository containerRepository;
    private final MtContainerRepository mtContainerRepository;
    private final WipStocktakeAssembler wipStocktakeAssembler;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final LovFeignClient lovFeignClient;
    private final MtMaterialSiteRepository mtMaterialSiteRepository;
    private final MtMaterialBasisRepository mtMaterialBasisRepository;
    private final HmeWipStocktakeActualRepository stocktakeActualRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final WmsEventService wmsEventService;
    private final HmeWipStocktakeActualHisRepository wipStocktakeActualHisRepository;
    private final HmeWipStocktakeDocMapper stocktakeDocMapper;
    private final MtMaterialRepository materialRepository;
    private final MtUomRepository uomRepository;
    private final HmeWipStocktakeDocService wipStocktakeDocService;

    public HmeWipStocktakeExecuteServiceImpl(HmeWipStocktakeExecuteMapper mapper, MtModProductionLineRepository productionLineRepository, MtModWorkcellRepository workcellRepository, HmeWipStocktakeRangeRepository stocktakeRangeRepository, WmsContainerRepository containerRepository, MtContainerRepository mtContainerRepository, WipStocktakeAssembler wipStocktakeAssembler, WmsMaterialLotRepository wmsMaterialLotRepository, LovFeignClient lovFeignClient, MtMaterialSiteRepository mtMaterialSiteRepository, MtMaterialBasisRepository mtMaterialBasisRepository, HmeWipStocktakeActualRepository stocktakeActualRepository, MtMaterialLotRepository mtMaterialLotRepository, WmsEventService wmsEventService, HmeWipStocktakeActualHisRepository wipStocktakeActualHisRepository, HmeWipStocktakeDocMapper stocktakeDocMapper, MtMaterialRepository materialRepository, MtUomRepository uomRepository, HmeWipStocktakeDocService wipStocktakeDocService) {
        this.mapper = mapper;
        this.productionLineRepository = productionLineRepository;
        this.workcellRepository = workcellRepository;
        this.stocktakeRangeRepository = stocktakeRangeRepository;
        this.containerRepository = containerRepository;
        this.mtContainerRepository = mtContainerRepository;
        this.wipStocktakeAssembler = wipStocktakeAssembler;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.lovFeignClient = lovFeignClient;
        this.mtMaterialSiteRepository = mtMaterialSiteRepository;
        this.mtMaterialBasisRepository = mtMaterialBasisRepository;
        this.stocktakeActualRepository = stocktakeActualRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.wmsEventService = wmsEventService;
        this.wipStocktakeActualHisRepository = wipStocktakeActualHisRepository;
        this.stocktakeDocMapper = stocktakeDocMapper;
        this.materialRepository = materialRepository;
        this.uomRepository = uomRepository;
        this.wipStocktakeDocService = wipStocktakeDocService;
    }

    @Override
    @ProcessLovValue
    public HmeWipStocktakeExecuteDocRepresentationDTO docScan(Long tenantId, String stocktakeNum) {
        List<HmeWipStocktakeExecuteDocRepresentationDTO> list = mapper.selectByCondition(tenantId, new HmeWipStocktakeExecuteDocQueryDTO(stocktakeNum));
        return CollectionUtil.isEmpty(list) ? new HmeWipStocktakeExecuteDocRepresentationDTO() : list.get(0);
    }

    @Override
    @ProcessLovValue
    public List<HmeWipStocktakeExecuteDocRepresentationDTO> docSelect(Long tenantId, HmeWipStocktakeExecuteDocQueryDTO dto) {
        return mapper.selectByCondition(tenantId, dto);
    }

    @Override
    public WipStocktakeRangeScanResponseDTO rangeScan(Long tenantId, WipStocktakeRangeScanQueryDTO dto) {
        WipStocktakeRangeScanResponseDTO rangeObject;
        switch (dto.getRangeObjectType()) {
            case "PL":
                List<MtModProductionLine> prodLines = productionLineRepository.select(MtModProductionLine.FIELD_PROD_LINE_CODE, dto.getRangeObjectCode());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtil.isEmpty(prodLines), "WMS_WIP_STOCKTAKE_002", WMS, dto.getRangeObjectCode());
                rangeObject = new WipStocktakeRangeScanResponseDTO(dto.getRangeObjectType(), dto.getRangeObjectCode(), prodLines.get(0).getProdLineName(), prodLines.get(0).getProdLineId());
                break;
            case "WP":
                List<MtModWorkcell> wkcLines = workcellRepository.select(MtModWorkcell.FIELD_WORKCELL_CODE, dto.getRangeObjectCode());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtil.isEmpty(wkcLines), "WMS_WIP_STOCKTAKE_003", WMS, dto.getRangeObjectCode());
                rangeObject = new WipStocktakeRangeScanResponseDTO(dto.getRangeObjectType(), dto.getRangeObjectCode(), wkcLines.get(0).getWorkcellName(), wkcLines.get(0).getWorkcellId());
                break;
            default:
                rangeObject = new WipStocktakeRangeScanResponseDTO();
                break;
        }
        HmeWipStocktakeRange range = stocktakeRangeRepository.selectOne(new HmeWipStocktakeRange() {{
            setRangeObjectType(dto.getRangeObjectType());
            setRangeObjectId(rangeObject.getRangeObjectId());
            setStocktakeId(dto.getStocktakeId());
        }});
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(range), "PL".equals(dto.getRangeObjectType()) ? "WMS_WIP_STOCKTAKE_002" : "WMS_WIP_STOCKTAKE_003", WMS, dto.getRangeObjectCode());
        return rangeObject;
    }

    @Override
    public WipStocktakeBarcodeScanResponseDTO barcodeScan(Long tenantId, WipStocktakeBarcodeScanQueryDTO dto) {
        WmsContainerVO container = containerRepository.getInfoByCode(tenantId, dto.getBarcode());
        List<WmsMaterialLotAttrVO> materialLotList;
        // 若是容器进行基本校验
        if (Objects.nonNull(container)) {
            WmsCommonUtils.processValidateMessage(tenantId, NO.equals(dto.getOpenFlag()), "WMS_STOCKTAKE_018", "WMS");
            mtContainerRepository.containerAvailableValidate(tenantId, container.getContainerId());
            materialLotList = containerRepository.getMaterialLotInContainer(tenantId, container.getContainerId());
        } else {
            // 若是物料批检验有效性
            WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(tenantId, dto.getBarcode());
            WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(materialLot) || !YES.equals(materialLot.getEnableFlag()), "WMS_WIP_COST_CENTER_0006", "WMS");
            materialLotList = Collections.singletonList(materialLot);
        }
        // 构建返回结果
        WipStocktakeBarcodeScanResponseDTO result = new WipStocktakeBarcodeScanResponseDTO(Objects.nonNull(container) ? CONTAINER : MATERIAL_LOT, dto.getBarcode(), Objects.nonNull(container) ? container.getContainerId() : materialLotList.get(0).getMaterialLotId());

        // 对物料批列表进行校验
        this.materialLotListValidate(tenantId, dto, result, materialLotList);
        return result;
    }

    @Override
    public WipStocktakeBarcodeScanResponseDTO actualExistsValid(Long tenantId, WipStocktakeMaterialLotValidQueryDTO dto) {
        List<HmeWipStocktakeActual> list = stocktakeActualRepository.selectByCondition(Condition.builder(HmeWipStocktakeActual.class).where(Sqls.custom().andEqualTo(HmeWipStocktakeActual.FIELD_STOCKTAKE_ID, dto.getStocktakeId())).build());
        Set<String> snapshotMaterialLotIds = list.stream().map(HmeWipStocktakeActual::getMaterialLotId).collect(Collectors.toSet());
        List<MaterialLotVO> notExistMaterialLotList = dto.getMaterialLotList().stream().filter(rec -> !snapshotMaterialLotIds.contains(rec.getMaterialLotId())).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(notExistMaterialLotList)){
            List<String> repairMaterialLotIdList = notExistMaterialLotList.stream().map(MaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            HmeWipStocktakeActualVO2 hmeWipStocktakeActualVO2 = stocktakeActualRepository.queryWipStocktakeActualByRepairMaterialLotId(tenantId, repairMaterialLotIdList, dto.getStocktakeId());
            if(CollectionUtils.isNotEmpty(hmeWipStocktakeActualVO2.getErrorMaterialLotIdList())){
                notExistMaterialLotList = notExistMaterialLotList.stream().filter(item -> hmeWipStocktakeActualVO2.getErrorMaterialLotIdList().contains(item.getMaterialLotId())).collect(Collectors.toList());
            }else {
                notExistMaterialLotList = new ArrayList<>();
            }
        }

        WipStocktakeBarcodeScanResponseDTO result = new WipStocktakeBarcodeScanResponseDTO();
        result.setInvalidMaterialLots(notExistMaterialLotList);
        result.addWarning(CollectionUtil.isEmpty(notExistMaterialLotList) ? null : WmsCommonUtils.messageContentGet(tenantId, "WMS_WIP_STOCKTAKE_008", "WMS", Strings.join(notExistMaterialLotList.stream().map(MaterialLotVO::getMaterialLotCode).collect(Collectors.toList()), ',')));
        return result;
    }

    @Override
    public WipStocktakeBarcodeScanResponseDTO countedValid(Long tenantId, WipStocktakeMaterialLotValidQueryDTO dto) {
        List<HmeWipStocktakeActual> list = stocktakeActualRepository.selectByCondition(Condition.builder(HmeWipStocktakeActual.class).where(Sqls.custom().andEqualTo(HmeWipStocktakeActual.FIELD_STOCKTAKE_ID, dto.getStocktakeId()).andIn(HmeWipStocktakeActual.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotList().stream().map(MaterialLotVO::getMaterialLotId).collect(Collectors.toSet()))).build());
        Map<String, HmeWipStocktakeActual> actualMap = list.stream().collect(Collectors.toMap(HmeWipStocktakeActual::getMaterialLotId, Function.identity(), (a, b) -> a));
        boolean firstFlag = "FIRST_COUNT".equals(dto.getStocktakeTypeCode());
        List<MaterialLotVO> countedMaterialLotList = dto.getMaterialLotList().stream().filter(rec -> (actualMap.containsKey(rec.getMaterialLotId()) && (firstFlag && Objects.nonNull(actualMap.get(rec.getMaterialLotId()).getFirstcountBy()) || (!firstFlag && Objects.nonNull(actualMap.get(rec.getMaterialLotId()).getRecountBy()))))).collect(Collectors.toList());
        WipStocktakeBarcodeScanResponseDTO result = new WipStocktakeBarcodeScanResponseDTO();
        result.setInvalidMaterialLots(countedMaterialLotList);
        result.addWarning(CollectionUtil.isEmpty(countedMaterialLotList) ? null : WmsCommonUtils.messageContentGet(tenantId, "WMS_WIP_STOCKTAKE_013", "WMS", Strings.join(countedMaterialLotList.stream().map(MaterialLotVO::getMaterialLotCode).collect(Collectors.toList()), ',')));
        return result;
    }

    @Override
    public List<WipStocktakeMaterialDetailRepresentationDTO> materialDetailGet(Long tenantId, WipStocktakeMaterialDetailQueryDTO query) {
        HmeWipStocktakeDocDTO7 condition = new HmeWipStocktakeDocDTO7();
        condition.setStocktakeIdList(Collections.singletonList(query.getStocktakeId()));
        condition.setMaterialCode(query.getMaterialCode());
        List<HmeWipStocktakeDocVO3> list = stocktakeDocMapper.wipStocktakeSumPageQuery(tenantId, condition);
        this.materialDetailQuantityGet(tenantId, list, condition.getStocktakeIdList());
        return list.stream().map(rec -> wipStocktakeAssembler.materialDetailToRepresentation(rec, query.getStocktakeTypeCode())).sorted().collect(Collectors.toList());
    }

    @Override
    public Page<WipStocktakeExecSnRepresentationDTO> snDetailGet(Long tenantId, WipStocktakeSnDetailQueryDTO query, PageRequest pageRequest) {
        Page<WipStocktakeExecSnRepresentationDTO> resultPage = PageHelper.doPage(pageRequest, () -> mapper.selectSnDetails(tenantId, query));
        if (CollectionUtils.isNotEmpty(resultPage.getContent())) {
            List<WipStocktakeExecSnRepresentationDTO> list = resultPage.getContent();
            list.forEach(rec -> rec.setNotMatchFlag(FIRST_COUNT.equals(query.getStocktakeTypeCode()) ? (Optional.ofNullable(rec.getFirstcountQuantity()).orElse(BigDecimal.ZERO).compareTo(rec.getCurrentQuantity()) != 0) : (Optional.ofNullable(rec.getRecountQuantity()).orElse(BigDecimal.ZERO).compareTo(rec.getCurrentQuantity()) != 0)));
            list = list.stream().sorted().collect(Collectors.toList());
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int stocktakeRangeAddition(Long tenantId, WipStocktakeRangeSaveCommandDTO command) {
        command.setTenantId(tenantId);
        HmeWipStocktakeDocDTO11 addCommand = wipStocktakeAssembler.rangeSaveCommandToAddRangeCommand(command);
        HmeWipStocktakeDocDTO11 result = wipStocktakeDocService.rangeCreate(tenantId, addCommand);
        return result.getAddList().size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int actualAddition(Long tenantId, WipStocktakeActualSaveCommandDTO command) {
        command.setTenantId(tenantId);
        List<HmeWipStocktakeActual> list = wipStocktakeAssembler.actualSaveCommandToEntities(command);
        HmeWipStocktakeActual dbQuery = new HmeWipStocktakeActual();
        dbQuery.setStocktakeId(command.getStocktakeId());
        List<HmeWipStocktakeActual> dbList = stocktakeActualRepository.select(dbQuery);
        Set<String> dbMaterialIds = dbList.stream().map(HmeWipStocktakeActual::getMaterialLotId).collect(Collectors.toSet());
        list = list.stream().filter(rec -> !dbMaterialIds.contains(rec.getMaterialLotId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            List<MtMaterialLotVO20> materialLotList = list.stream().map(rec -> wipStocktakeAssembler.actualToUpdateStocktake(rec, YES)).collect(Collectors.toList());
            // 更新条码
            WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, WIP_STOCKTAKE_ACTUAL_CREATE);
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, event.getEventId(), NO);
        } else {
            return 0;
        }
        return list.stream().mapToInt(stocktakeActualRepository::save).sum();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeWipStocktakeExecuteVO execute(Long tenantId, WipStocktakeExecuteCommandDTO command) {
        HmeWipStocktakeExecuteVO result = new HmeWipStocktakeExecuteVO();
        result.setFlag(NO);
        command.setTenantId(tenantId);
        boolean firstFlag = FIRST_COUNT.equals(command.getStocktakeTypeCode());
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, firstFlag ? WIP_STOCKTAKE_FIRSTCOUNT : WIP_STOCKTAKE_RECOUNT);
        HmeWipStocktakeExecuteVO hmeWipStocktakeExecuteVO = wipStocktakeAssembler.executeCommandToEntities(command);
        //2021-10-22 14:32 edit by chaonan.hu for peng.zhao 将盘点单执行以后的弹窗永远去掉，让其不要再弹窗
//        if(HmeConstants.ConstantValue.YES.equals(hmeWipStocktakeExecuteVO.getFlag())){
//            result.setFlag(HmeConstants.ConstantValue.YES);
//            result.setMaterialLotCode(hmeWipStocktakeExecuteVO.getMaterialLotCode());
//            return result;
//        }
        self().stocktakeActualUpdate(hmeWipStocktakeExecuteVO.getHmeWipStocktakeActualList(), event.getEventId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void stocktakeActualUpdate(List<HmeWipStocktakeActual> list, String eventId) {
        // 将旧实际数据转化为历史数据
        List<HmeWipStocktakeActualHis> hisList = list.stream().map(rec -> wipStocktakeAssembler.actualToHis(rec, eventId)).collect(Collectors.toList());
        // 执行保存
        list.forEach(stocktakeActualRepository::save);
        hisList.forEach(wipStocktakeActualHisRepository::save);
    }

    private void materialDetailQuantityGet(Long tenantId, List<HmeWipStocktakeDocVO3> list, List<String> stocktakeIdList) {
        for (HmeWipStocktakeDocVO3 hmeWipStocktakeDoc : list) {
            MtMaterial mtMaterial = materialRepository.selectByPrimaryKey(hmeWipStocktakeDoc.getMaterialId());
            hmeWipStocktakeDoc.setMaterialCode(mtMaterial.getMaterialCode());
            hmeWipStocktakeDoc.setMaterialName(mtMaterial.getMaterialName());
            MtUom mtUom = uomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
            hmeWipStocktakeDoc.setUomId(mtMaterial.getPrimaryUomId());
            hmeWipStocktakeDoc.setUomCode(mtUom.getUomCode());
            MtModProductionLine mtModProductionLine = productionLineRepository.selectByPrimaryKey(hmeWipStocktakeDoc.getProdLineId());
            hmeWipStocktakeDoc.setProdLineCode(mtModProductionLine.getProdLineCode());
            hmeWipStocktakeDoc.setProdLineName(mtModProductionLine.getProdLineName());
            MtModWorkcell mtModWorkcell = workcellRepository.selectByPrimaryKey(hmeWipStocktakeDoc.getWorkcellId());
            hmeWipStocktakeDoc.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            hmeWipStocktakeDoc.setWorkcellName(mtModWorkcell.getWorkcellName());
            //账面数量
            BigDecimal currentQuantity = stocktakeDocMapper.currentQuantityQuery(tenantId, hmeWipStocktakeDoc.getMaterialId(),
                    hmeWipStocktakeDoc.getProdLineId(), hmeWipStocktakeDoc.getWorkcellId(), stocktakeIdList);
            hmeWipStocktakeDoc.setCurrentQuantity(currentQuantity);
            //初盘数量
            BigDecimal firstcountQuantity = stocktakeDocMapper.firstcountQuantityQuery(tenantId, hmeWipStocktakeDoc.getMaterialId(), hmeWipStocktakeDoc.getProdLineId(), hmeWipStocktakeDoc.getWorkcellId(), stocktakeIdList);
            hmeWipStocktakeDoc.setFirstcountQuantity(firstcountQuantity);
            //复盘数量
            BigDecimal recountQuantity = stocktakeDocMapper.recountQuantityQuery(tenantId, hmeWipStocktakeDoc.getMaterialId(), hmeWipStocktakeDoc.getProdLineId(), hmeWipStocktakeDoc.getWorkcellId(), stocktakeIdList);
            hmeWipStocktakeDoc.setRecountQuantity(recountQuantity);
            //初盘差异
            hmeWipStocktakeDoc.setFirstcountDiff(firstcountQuantity.subtract(currentQuantity));
            //复盘差异
            hmeWipStocktakeDoc.setRecountDiff(recountQuantity.subtract(currentQuantity));
        }
    }

    private void materialLotListValidate(Long tenantId, WipStocktakeBarcodeScanQueryDTO dto, WipStocktakeBarcodeScanResponseDTO result, List<WmsMaterialLotAttrVO> materialLots) {
        Map<String, WmsMaterialLotAttrVO> materialLotIdMap = defaultValidate(tenantId, dto.getStocktakeId(), materialLots);
        // 获取物料对应的物料组,区分是否为COS物料
        Map<String, Boolean> groupMap = materialGroupGet(tenantId, dto.getSiteId(), materialLots);
        Set<String> cosMaterialLotIds = groupMap.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        Set<String> nonCosMaterialLotIds = groupMap.entrySet().stream().filter(entry -> !entry.getValue()).map(Map.Entry::getKey).collect(Collectors.toSet());
        List<WipStocktakeMaterialLotWorkVO> materialLotWorkList = new ArrayList<>(materialLots.size());
        materialLotWorkList.addAll(CollectionUtil.isNotEmpty(cosMaterialLotIds) ? mapper.selectCosMaterialLots(tenantId, dto.getSiteId(), cosMaterialLotIds) : new ArrayList<>());
        //2021-10-28 11:36 edit by chaonan.hu for peng.zhao 需要对返修的条码进行二次查询在制工序及产线
        if(CollectionUtils.isNotEmpty(nonCosMaterialLotIds)){
            List<WipStocktakeMaterialLotWorkVO> nonCosMaterialLotWorkList = mapper.selectNonCosMaterialLots(tenantId, dto.getSiteId(), nonCosMaterialLotIds);
            List<WipStocktakeMaterialLotWorkVO> nonCosMaterialLotList = nonCosMaterialLotWorkList.stream().filter(item -> StringUtils.isBlank(item.getWorkcellId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(nonCosMaterialLotList)){
                //如果有在制工序为空的条码，则需要二次查询
                List<String> materialLotIdList = nonCosMaterialLotList.stream().map(WipStocktakeMaterialLotWorkVO::getMaterialLotId).collect(Collectors.toList());
                List<WipStocktakeMaterialLotWorkVO> materialLotIdWorkList = mapper.selectNonCosMaterialLots2(tenantId, materialLotIdList, dto.getStocktakeId());
                if(CollectionUtils.isNotEmpty(materialLotIdWorkList)){
                    for (WipStocktakeMaterialLotWorkVO nonCosMaterialLot:nonCosMaterialLotList) {
                        List<WipStocktakeMaterialLotWorkVO> singleMaterialLotIdWorkList = materialLotIdWorkList.stream()
                                .filter(item -> item.getMaterialLotId().equals(nonCosMaterialLot.getMaterialLotId())
                                        && StringUtils.isNotBlank(item.getWorkcellId()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(singleMaterialLotIdWorkList)){
                            WipStocktakeMaterialLotWorkVO wipStocktakeMaterialLotWorkVO = singleMaterialLotIdWorkList.get(0);
                            nonCosMaterialLot.setWorkcellId(wipStocktakeMaterialLotWorkVO.getWorkcellId());
                            nonCosMaterialLot.setWorkcellCode(wipStocktakeMaterialLotWorkVO.getWorkcellCode());
                            nonCosMaterialLot.setProdLineId(wipStocktakeMaterialLotWorkVO.getProdLineId());
                            nonCosMaterialLot.setProdLineCode(wipStocktakeMaterialLotWorkVO.getProdLineCode());
                        }
                    }
                }
            }
            materialLotWorkList.addAll(nonCosMaterialLotWorkList);
        }
        rangeValidate(tenantId, dto, result, materialLotIdMap, materialLotWorkList);

        // 将不在范围内的物料ID列表返回
        result.setLoadedMaterialLotList(materialLotWorkList);
    }

    private void rangeValidate(Long tenantId, WipStocktakeBarcodeScanQueryDTO dto, WipStocktakeBarcodeScanResponseDTO result, Map<String, WmsMaterialLotAttrVO> materialLotIdMap, List<WipStocktakeMaterialLotWorkVO> materialLotWorkList) {
        List<String> prodLineNotMatch = new ArrayList<>(), workcellNotMatch = new ArrayList<>();
        Map<String, List<WipStocktakeMaterialLotWorkVO>> materialLotWorkMap = materialLotWorkList.stream().collect(Collectors.groupingBy(WipStocktakeMaterialLotWorkVO::getMaterialLotCode));
        materialLotWorkMap.forEach((materialLotCode, list) -> {
            if (list.size() > 1 && list.stream().filter(rec -> Objects.nonNull(rec.getWorkcellId()) || Objects.nonNull(rec.getProdLineId())).count() > 1) {
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_WIP_STOCKTAKE_014", "WMS", materialLotCode);
            }
            // 若仅存在一个非空的工段，则过滤掉
            if (list.stream().filter(rec -> Objects.nonNull(rec.getWorkcellId()) || Objects.nonNull(rec.getProdLineId())).count() == 1) {
                List<WipStocktakeMaterialLotWorkVO> nullList = list.stream().filter(rec -> Objects.isNull(rec.getWorkcellId()) || Objects.isNull(rec.getProdLineId())).collect(Collectors.toList());
                materialLotWorkList.removeAll(nullList);
            }
        });
        // 将不相等的产线和工序返回警告
        materialLotWorkList.forEach(rec -> {
            if (!StringCommonUtils.equalsIgnoreBlank(dto.getProdLineId(), rec.getProdLineId())) {
                prodLineNotMatch.add(materialLotIdMap.get(rec.getMaterialLotId()).getMaterialLotCode());
            }
            if (!StringCommonUtils.equalsIgnoreBlank(dto.getWorkcellId(), rec.getWorkcellId())) {
                workcellNotMatch.add(materialLotIdMap.get(rec.getMaterialLotId()).getMaterialLotCode());
            }
        });

        // 组装警告消息
        result.addWarning(CollectionUtil.isEmpty(prodLineNotMatch) ? null : WmsCommonUtils.messageContentGet(tenantId, "WMS_WIP_STOCKTAKE_005", "WMS", Strings.join(prodLineNotMatch, ',')));
        result.addWarning(CollectionUtil.isEmpty(workcellNotMatch) ? null : WmsCommonUtils.messageContentGet(tenantId, "WMS_WIP_STOCKTAKE_006", "WMS", Strings.join(workcellNotMatch, ',')));
    }

    private Map<String, WmsMaterialLotAttrVO> defaultValidate(Long tenantId, String stocktakeId, List<WmsMaterialLotAttrVO> materialLots) {
        // 校验产线和工序是否符合
        materialLots.forEach(materialLot -> WmsCommonUtils.processValidateMessage(tenantId, !YES.equals(materialLot.getMfFlag()), "WMS_DISTRIBUTION_0020", "WMS", materialLot.getMaterialLotCode()));
        Map<String, WmsMaterialLotAttrVO> materialLotIdMap = materialLots.stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, Function.identity()));
        // 校验条码是否已经存在在下达状态的单据中
        List<HmeWipStocktakeDoc> releasedDocList = stocktakeDocMapper.selectByCondition(Condition.builder(HmeWipStocktakeDoc.class).andWhere(Sqls.custom().andEqualTo(HmeWipStocktakeDoc.FIELD_STOCKTAKE_STATUS, RELEASED).andNotEqualTo(HmeWipStocktakeDoc.FIELD_STOCKTAKE_ID, stocktakeId)).build());
        if (CollectionUtils.isNotEmpty(releasedDocList)) {
            List<HmeWipStocktakeActual> actualList = stocktakeActualRepository.selectByCondition(Condition.builder(HmeWipStocktakeActual.class).andWhere(Sqls.custom().andIn(HmeWipStocktakeActual.FIELD_STOCKTAKE_ID, releasedDocList.stream().map(HmeWipStocktakeDoc::getStocktakeId).collect(Collectors.toSet())).andIn(HmeWipStocktakeActual.FIELD_MATERIAL_LOT_ID, materialLotIdMap.keySet())).andWhere(Sqls.custom().andIsNotNull(HmeWipStocktakeActual.FIELD_FIRSTCOUNT_QUANTITY).orIsNotNull(HmeWipStocktakeActual.FIELD_RECOUNT_QUANTITY)).build());
            if (CollectionUtils.isNotEmpty(actualList)) {
                Set<String> duplicateIds = actualList.stream().map(HmeWipStocktakeActual::getMaterialLotId).collect(Collectors.toSet());
                WmsCommonUtils.processValidateMessage(tenantId, "WMS_WIP_STOCKTAKE_021", "WMS", materialLotIdMap.entrySet().stream().filter(entrySet -> duplicateIds.contains(entrySet.getKey())).map(entrySet -> entrySet.getValue().getMaterialLotCode()).collect(Collectors.joining(",")), stocktakeDocMapper.selectByPrimaryKey(actualList.get(0).getStocktakeId()).getStocktakeNum());
            }
        }
        return materialLotIdMap;
    }

    private Map<String, Boolean> materialGroupGet(Long tenantId, String siteId, List<WmsMaterialLotAttrVO> materialLots) {
        Set<String> values = lovFeignClient.queryLovValue("HME.COS_ITEM_GROUP", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Map<String, String> map = materialLots.stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, WmsMaterialLotAttrVO::getMaterialId));
        Set<String> materialIds = new HashSet<>(map.values());
        List<MtMaterialSite> materialSites = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class).andWhere(Sqls.custom().andEqualTo(MtMaterialSite.FIELD_SITE_ID, siteId).andIn(MtMaterialSite.FIELD_MATERIAL_ID, materialIds)).build());
        Map<String, String> sitesMap = materialSites.stream().collect(Collectors.toMap(MtMaterialSite::getMaterialId, MtMaterialSite::getMaterialSiteId));
        List<MtMaterialBasic> materialBasics = mtMaterialBasisRepository.selectByIds(Strings.join(sitesMap.values(), ','));
        Map<String, String> basicMap = materialBasics.stream().collect(Collectors.toMap(MtMaterialBasic::getMaterialSiteId, MtMaterialBasic::getItemGroup));
        Map<String, Boolean> resultMap = new HashMap<>(map.size());
        map.forEach((key, value) -> resultMap.put(key, values.contains(basicMap.get(sitesMap.get(value)))));
        return resultMap;
    }
}
