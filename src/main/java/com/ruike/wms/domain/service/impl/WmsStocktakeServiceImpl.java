package com.ruike.wms.domain.service.impl;

import com.ruike.wms.api.dto.WmsStocktakeCountExecuteDTO;
import com.ruike.wms.api.dto.WmsStocktakeScanDTO;
import com.ruike.wms.api.dto.WmsStocktakeSubmitDTO;
import com.ruike.wms.api.dto.WmsStocktakeValidationDTO;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.app.service.WmsStocktakeActualService;
import com.ruike.wms.app.service.WmsStocktakeDocService;
import com.ruike.wms.domain.repository.WmsContainerRepository;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.repository.WmsStocktakeActualRepository;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.service.WmsStocktakeService;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendVO5;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.actual.domain.vo.MtStocktakeActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.*;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.*;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotType.CONTAINER;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotType.MATERIAL_LOT;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeRangeObjectType.MATERIAL;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeType.FIRST_COUNT;

/**
 * 库存盘点 业务层服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 11:47
 */
@Service
public class WmsStocktakeServiceImpl implements WmsStocktakeService {
    private final WmsContainerRepository wmsContainerRepository;
    private final WmsEventService wmsEventService;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtStocktakeActualRepository mtStocktakeActualRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final WmsStocktakeRangeRepository wmsStocktakeRangeRepository;
    private final WmsStocktakeActualRepository stocktakeActualRepository;
    private final WmsStocktakeActualService wmsStocktakeActualService;
    private final WmsStocktakeDocService wmsStocktakeDocService;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtEventRepository mtEventRepository;

    public WmsStocktakeServiceImpl(WmsContainerRepository wmsContainerRepository, WmsEventService wmsEventService, WmsMaterialLotRepository wmsMaterialLotRepository, MtStocktakeActualRepository mtStocktakeActualRepository, MtExtendSettingsRepository mtExtendSettingsRepository, WmsStocktakeRangeRepository wmsStocktakeRangeRepository, WmsStocktakeActualRepository stocktakeActualRepository, WmsStocktakeActualService wmsStocktakeActualService, WmsStocktakeDocService wmsStocktakeDocService, MtEventRequestRepository mtEventRequestRepository, MtMaterialLotRepository mtMaterialLotRepository, MtEventRepository mtEventRepository) {
        this.wmsContainerRepository = wmsContainerRepository;
        this.wmsEventService = wmsEventService;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.mtStocktakeActualRepository = mtStocktakeActualRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.wmsStocktakeRangeRepository = wmsStocktakeRangeRepository;
        this.stocktakeActualRepository = stocktakeActualRepository;
        this.wmsStocktakeActualService = wmsStocktakeActualService;
        this.wmsStocktakeDocService = wmsStocktakeDocService;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtEventRepository = mtEventRepository;
    }

    private final static String LOCK = "LOCK";

    @Override
    public WmsStocktakeBarcodeScanVO barcodeScan(Long tenantId, WmsStocktakeScanDTO dto) {
        WmsStocktakeBarcodeScanVO result = new WmsStocktakeBarcodeScanVO();
        // 此方法只执行硬校验，软校验执行到校验货位
        WmsContainerVO container = wmsContainerRepository.getInfoByCode(tenantId, dto.getBarcode());
        if (Objects.nonNull(container)) {
            // 暗盘时仅能扫描实物条码盘点
            WmsCommonUtils.processValidateMessage(tenantId, NO.equals(dto.getOpenFlag()),
                    "WMS_STOCKTAKE_018", WMS, dto.getBarcode());
            WmsCommonUtils.processValidateMessage(tenantId, !"CANRELEASE".equals(container.getStatus()),
                    "MT_MATERIAL_LOT_0033", "MATERIAL_LOT");
            // 组建返回值
            List<WmsMaterialLotAttrVO> lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, container.getContainerId());
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
            result.setLoadObjectType(CONTAINER);
            result.setLoadObjectId(container.getContainerId());
            result.setLoadObjectCode(container.getContainerCode());
            result.setLocatorId(container.getLocatorId());
            result.setLotCount(lotList.size());
            result.setLocatorFlag(dto.getLocatorId().equals(container.getLocatorId()));
        } else {
            // 按照物料批判断
            WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(tenantId, dto.getBarcode());
            WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(materialLot),
                    "WMS_COST_CENTER_0006", WMS, dto.getBarcode());
            WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLot.getMfFlag()),
                    "WMS_DISTRIBUTION_0003", WMS, dto.getBarcode());
            WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLot.getVirtualFlag()),
                    "WMS_STOCKTAKE_017", WMS, dto.getBarcode());
            // 组建返回值
            result.setLoadObjectType(MATERIAL_LOT);
            result.setLoadObjectId(materialLot.getMaterialLotId());
            result.setLoadObjectCode(materialLot.getMaterialLotCode());
            result.setLocatorId(materialLot.getLocatorId());
            result.setLocatorCode(materialLot.getLocatorCode());
            result.setMaterialId(materialLot.getMaterialId());
            result.setMaterialCode(materialLot.getMaterialCode());
            result.setMaterialName(materialLot.getMaterialName());
            result.setMaterialVersion(materialLot.getMaterialVersion());
            result.setUomCode(materialLot.getPrimaryUomCode());
            result.setQuantity(materialLot.getPrimaryUomQty());
            result.setLocatorFlag(dto.getLocatorId().equals(materialLot.getLocatorId()));
        }
        return result;
    }

    @Override
    public List<WmsMaterialVO> materialValidation(Long tenantId, WmsStocktakeValidationDTO dto) {
        // 若为容器，获取容器下所有的物料批
        List<WmsMaterialLotAttrVO> lotList;
        if (CONTAINER.equals(dto.getLoadObjectType())) {
            lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, dto.getLoadObjectId());
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
            lotList.forEach(materialLot -> {
                WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLot.getMfFlag()),
                        "WMS_DISTRIBUTION_0003", WMS, materialLot.getMaterialLotCode());
                WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLot.getVirtualFlag()),
                        "WMS_STOCKTAKE_017", WMS, materialLot.getMaterialLotCode());
            });
        } else {
            // 直接校验物料
            lotList = Collections.singletonList(wmsMaterialLotRepository.selectListWithAttrById(tenantId, dto.getLoadObjectId()));
        }
        return getMaterialNotInRange(tenantId, dto.getStocktakeId(), lotList);
    }

    @Override
    public List<WmsMaterialLotAttrVO> actualValidation(Long tenantId, WmsStocktakeValidationDTO dto) {
        List<MtStocktakeActual> actualList = mtStocktakeActualRepository.selectByCondition(Condition.builder(MtStocktakeActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtStocktakeActual.FIELD_STOCKTAKE_ID, dto.getStocktakeId())).build());
        Set<String> idSet = actualList.stream().map(MtStocktakeActual::getMaterialLotId).collect(Collectors.toSet());
        List<WmsMaterialLotAttrVO> lotList;
        if (CONTAINER.equals(dto.getLoadObjectType())) {
            lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, dto.getLoadObjectId());
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
        } else {
            // 直接校验物料
            lotList = Collections.singletonList(wmsMaterialLotRepository.selectListWithAttrById(tenantId, dto.getLoadObjectId()));
        }
        List<WmsMaterialLotAttrVO> list = new ArrayList<>();
        lotList.forEach(rec -> {
            if (!idSet.contains(rec.getMaterialLotId())) {
                WmsMaterialLotAttrVO lot = new WmsMaterialLotAttrVO();
                lot.setMaterialLotId(rec.getMaterialLotId());
                lot.setMaterialLotCode(rec.getMaterialLotCode());
                list.add(lot);
            }
        });
        return list;
    }

    @Override
    public List<WmsMaterialLotAttrVO> countedValidation(Long tenantId, String stocktakeType, WmsStocktakeValidationDTO dto) {
        List<MtStocktakeActual> actualList = mtStocktakeActualRepository.selectByCondition(Condition.builder(MtStocktakeActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtStocktakeActual.FIELD_STOCKTAKE_ID, dto.getStocktakeId())).build());
        Map<String, List<MtStocktakeActual>> actualMap = actualList.stream().collect(Collectors.groupingBy(MtStocktakeActual::getMaterialLotId));
        List<WmsMaterialLotAttrVO> lotList;
        if (CONTAINER.equals(dto.getLoadObjectType())) {
            lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, dto.getLoadObjectId());
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
        } else {
            // 直接校验物料
            lotList = Collections.singletonList(wmsMaterialLotRepository.selectListWithAttrById(tenantId, dto.getLoadObjectId()));
        }
        List<WmsMaterialLotAttrVO> list = new ArrayList<>();
        lotList.forEach(rec -> {
            // 判断初盘/复盘数据是否有，若有则交给前端判断是否强制执行
            if (actualMap.containsKey(rec.getMaterialLotId()) && actualMap.get(rec.getMaterialLotId()).stream()
                    .anyMatch(a -> FIRST_COUNT.equals(stocktakeType) ? StringUtils.isNotBlank(a.getFirstcountLocatorId()) : StringUtils.isNotBlank(a.getRecountLocatorId()))) {
                WmsMaterialLotAttrVO lot = new WmsMaterialLotAttrVO();
                lot.setMaterialLotId(rec.getMaterialLotId());
                lot.setMaterialLotCode(rec.getMaterialLotCode());
                list.add(lot);
            }
        });

        return list;
    }

    private List<WmsMaterialVO> getMaterialNotInRange(Long tenantId, String stocktakeId, List<WmsMaterialLotAttrVO> list) {
        List<WmsStocktakeRangeVO> rangeList = wmsStocktakeRangeRepository.selectListByDocId(tenantId, stocktakeId, MATERIAL);
        Set<String> materialIdSet = rangeList.stream().map(WmsStocktakeRangeVO::getRangeObjectId).collect(Collectors.toSet());
        List<WmsMaterialVO> materialList = new ArrayList<>();
        if (materialIdSet.size() > 0) {
            list.forEach(rec -> {
                if (!materialIdSet.contains(rec.getMaterialId())) {
                    WmsMaterialVO material = new WmsMaterialVO();
                    material.setMaterialId(rec.getMaterialId());
                    material.setMaterialCode(rec.getMaterialCode());
                    materialList.add(material);
                }
            });
        }
        return materialList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean stocktakeSubmit(Long tenantId, WmsStocktakeSubmitDTO dto) {
        // 创建事件请求
        String eventType = FIRST_COUNT.equals(dto.getStocktakeTypeCode()) ? STOCKTAKE_FIRSTCOUNT : STOCKTAKE_RECOUNT;
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, eventType);
        // 判断是物料批还是容器，如果为容器拆分成物料批
        List<WmsStocktakeCountExecuteDTO> materialLotList = this.getMaterialLotList(tenantId, dto);
        // 根据盘点类型，调用初盘/复盘执行API
        this.countExecute(tenantId, dto, materialLotList, event.getEventRequestId());
        // 更新盘点实际
        List<String> materialLotIdList = materialLotList.stream().map(WmsStocktakeCountExecuteDTO::getMaterialLotId).collect(Collectors.toList());
        this.stocktakeActualUpdate(tenantId, dto.getStocktakeId(), dto.getStocktakeTypeCode(), materialLotIdList, event.getEventId());
        return true;
    }

    @Override
    public List<WmsStocktakeMaterialDetailVO> stockDetailGet(Long tenantId, String stocktakeId, String stocktakeTypeCode,
                                                             String materialCode) {
        List<WmsStocktakeMaterialDetailVO> list = stocktakeActualRepository.stocktakeDetailGet(tenantId, stocktakeId, stocktakeTypeCode, materialCode);
        list = list.stream().sorted(Comparator.comparing(rec -> FIRST_COUNT.equals(stocktakeTypeCode) ? rec.getFirstcountDifferentQuantity().abs() : rec.getRecountDifferentQuantity().abs())).collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<WmsStocktakeMaterialLotVO> materialLotInContainerGet(Long tenantId, String stocktakeTypeCode, String stocktakeId, String containerId) {
        List<WmsStocktakeMaterialLotVO> list = new ArrayList<>();
        if (StringUtils.isBlank(stocktakeId)) {
            List<WmsMaterialLotAttrVO> lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, containerId);
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
            BeanCopier copier = BeanCopier.create(WmsMaterialLotAttrVO.class, WmsStocktakeMaterialLotVO.class, false);
            for (WmsMaterialLotAttrVO rec : lotList) {
                WmsStocktakeMaterialLotVO vo = new WmsStocktakeMaterialLotVO();
                copier.copy(rec, vo, null);
                vo.setLotCode(rec.getLot());
                vo.setCurrentQuantity(rec.getPrimaryUomQty());
                vo.setUomId(rec.getPrimaryUomId());
                vo.setUomCode(rec.getPrimaryUomCode());
                list.add(vo);
            }
        } else {
            list = stocktakeActualRepository.selectMaterialLotByType(tenantId, stocktakeId, stocktakeTypeCode);
            list.removeIf(rec -> FIRST_COUNT.equals(stocktakeTypeCode) ? !containerId.equals(rec.getFirstcountContainerId()) : !containerId.equals(rec.getRecountContainerId()));
        }
        return list;
    }

    private void switchMaterialCodeStocktake(Long tenantId, String eventRequestId, List<String> materialLotIdList, String switchMode) {
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(LOCK.equals(switchMode) ? STOCKTAKE_MATERIALLOT_LOCK : STOCKTAKE_MATERIALLOT_UNLOCK);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 批量更新条码
        List<MtMaterialLotVO20> materialLotList = materialLotIdList.stream().map(materialLotId -> new MtMaterialLotVO20() {{
            setMaterialLotId(materialLotId);
            setStocktakeFlag((LOCK.equals(switchMode) ? YES : NO));
        }}).collect(Collectors.toList());
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, NO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsStocktakeActualVO> insertReleasedActual(Long tenantId, WmsStocktakeValidationDTO dto) {
        List<WmsStocktakeActualVO> list = wmsStocktakeActualService.insertByLoadObject(tenantId, dto);
        // 锁定条码
        List<String> idList = list.stream().map(WmsStocktakeActualVO::getMaterialLotId).collect(Collectors.toList());
        List<MtMaterialLot> lotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                .andWhere(Sqls.custom().andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, idList)
                        .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)).build());
        List<String> materialLotIdList = lotList.stream().filter(rec -> !Optional.ofNullable(rec.getStocktakeFlag()).orElse(NO).equals(YES)).map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);
            this.switchMaterialCodeStocktake(tenantId, eventRequestId, materialLotIdList, LOCK);
        }
        return list;
    }

    private List<WmsStocktakeCountExecuteDTO> getMaterialLotList(Long tenantId, WmsStocktakeSubmitDTO dto) {
        List<WmsStocktakeCountExecuteDTO> materialLotList = new ArrayList<>();
        if (CONTAINER.equals(dto.getLoadObjectType())) {
            // 查询容器下的所有物料批加入列表中
            List<WmsMaterialLotAttrVO> lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, dto.getLoadObjectId());
            lotList = lotList.stream().filter(rec -> YES.equals(rec.getEnableFlag())).collect(Collectors.toList());
            lotList.forEach(rec -> {
                WmsStocktakeCountExecuteDTO count = new WmsStocktakeCountExecuteDTO();
                count.setLocatorId(dto.getLocatorId());
                count.setMaterialId(rec.getMaterialId());
                count.setMaterialLotId(rec.getMaterialLotId());
                count.setQuantity(rec.getPrimaryUomQty());
                count.setContainerId(dto.getLoadObjectId());
                materialLotList.add(count);
            });
        } else {
            WmsStocktakeCountExecuteDTO count = new WmsStocktakeCountExecuteDTO();
            count.setLocatorId(dto.getLocatorId());
            count.setMaterialId(dto.getMaterialId());
            count.setMaterialLotId(dto.getLoadObjectId());
            count.setQuantity(dto.getQuantity());
            materialLotList.add(count);
        }
        return materialLotList;
    }

    private void countExecute(Long tenantId, WmsStocktakeSubmitDTO dto, List<WmsStocktakeCountExecuteDTO> list, String eventRequestId) {
        // 根据盘点类型判断进入初盘还是复盘
        if (FIRST_COUNT.equals(dto.getStocktakeTypeCode())) {
            list.forEach(rec -> {
                MtStocktakeActualVO1 actual = new MtStocktakeActualVO1();
                actual.setEventRequestId(eventRequestId);
                actual.setStocktakeId(dto.getStocktakeId());
                actual.setMaterialLotId(rec.getMaterialLotId());
                actual.setFirstcountMaterialId(rec.getMaterialId());
                actual.setFirstcountLocatorId(rec.getLocatorId());
                actual.setFirstcountContainerId(rec.getContainerId());
                actual.setFirstcountQuantity(rec.getQuantity().doubleValue());
                actual.setFirstcountRemark(dto.getRemark());
                mtStocktakeActualRepository.stocktakeFirstcount(tenantId, actual);
            });
        } else {
            list.forEach(rec -> {
                MtStocktakeActualVO1 actual = new MtStocktakeActualVO1();
                actual.setEventRequestId(eventRequestId);
                actual.setStocktakeId(dto.getStocktakeId());
                actual.setMaterialLotId(rec.getMaterialLotId());
                actual.setRecountMaterialId(rec.getMaterialId());
                actual.setRecountLocatorId(rec.getLocatorId());
                actual.setRecountContainerId(rec.getContainerId());
                actual.setRecountQuantity(rec.getQuantity().doubleValue());
                actual.setRecountRemark(dto.getRemark());
                mtStocktakeActualRepository.stocktakeRecount(tenantId, actual);
            });
        }
    }

    private void stocktakeActualUpdate(Long tenantId, String stocktakeId, String stocktakeTypeCode, List<String> list, String eventId) {
        List<MtStocktakeActual> actualList = mtStocktakeActualRepository.selectByCondition(Condition.builder(MtStocktakeActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtStocktakeActual.FIELD_STOCKTAKE_ID, stocktakeId)
                .andEqualTo(MtStocktakeActual.FIELD_TENANT_ID, tenantId)
                .andIn(MtStocktakeActual.FIELD_MATERIAL_LOT_ID, list)
        ).build());
        if (CollectionUtils.isNotEmpty(actualList)) {
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            long userId = Optional.ofNullable(userDetails.getUserId()).orElse(-1L);
            String nowDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            actualList.forEach(rec -> {
                String updateUser = FIRST_COUNT.equals(stocktakeTypeCode) ? "FIRSTCOUNT_BY" : "RECOUNT_BY";
                String updateDate = FIRST_COUNT.equals(stocktakeTypeCode) ? "FIRSTCOUNT_DATE" : "RECOUNT_DATE";
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 userAttr = new MtExtendVO5();
                userAttr.setAttrName(updateUser);
                userAttr.setAttrValue(String.valueOf(userId));
                attrList.add(userAttr);
                MtExtendVO5 dateAttr = new MtExtendVO5();
                dateAttr.setAttrName(updateDate);
                dateAttr.setAttrValue(nowDateStr);
                attrList.add(dateAttr);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_stocktake_actual_attr", rec.getStocktakeActualId(), eventId, attrList);
            });
        }
    }
}
