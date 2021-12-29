package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsStocktakeActualExportDTO;
import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeValidationDTO;
import com.ruike.wms.app.service.WmsStocktakeActualService;
import com.ruike.wms.domain.repository.WmsContainerRepository;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.repository.WmsStocktakeActualRepository;
import com.ruike.wms.domain.repository.WmsStocktakeDocRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;
import tarzan.actual.domain.repository.MtStocktakeActualHisRepository;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.actual.domain.vo.MtStocktakeActualVO;
import tarzan.actual.domain.vo.MtStocktakeActualVO4;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.repository.MtStocktakeDocRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotType.CONTAINER;

/**
 * 库存盘点实绩 服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 19:17
 */
@Service
public class WmsStocktakeActualServiceImpl implements WmsStocktakeActualService {

    private final WmsStocktakeActualRepository stocktakeActualRepository;
    private final MtEventRepository mtEventRepository;
    private final MtStocktakeDocRepository mtStocktakeDocRepository;
    private final MtStocktakeActualRepository mtStocktakeActualRepository;
    private final WmsStocktakeDocRepository wmsStocktakeDocRepository;
    private final WmsStocktakeActualRepository wmsStocktakeActualRepository;
    private final WmsContainerRepository wmsContainerRepository;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtStocktakeActualHisRepository mtStocktakeActualHisRepository;
    private final CustomSequence customSequence;

    public WmsStocktakeActualServiceImpl(WmsStocktakeActualRepository stocktakeActualRepository, MtEventRepository mtEventRepository, MtStocktakeDocRepository mtStocktakeDocRepository, MtStocktakeActualRepository mtStocktakeActualRepository, WmsStocktakeDocRepository wmsStocktakeDocRepository, WmsStocktakeActualRepository wmsStocktakeActualRepository, WmsContainerRepository wmsContainerRepository, WmsMaterialLotRepository wmsMaterialLotRepository, MtErrorMessageRepository mtErrorMessageRepository, MtMaterialLotRepository mtMaterialLotRepository, MtStocktakeActualHisRepository mtStocktakeActualHisRepository, CustomSequence customSequence) {
        this.stocktakeActualRepository = stocktakeActualRepository;
        this.mtEventRepository = mtEventRepository;
        this.mtStocktakeDocRepository = mtStocktakeDocRepository;
        this.mtStocktakeActualRepository = mtStocktakeActualRepository;
        this.wmsStocktakeDocRepository = wmsStocktakeDocRepository;
        this.wmsStocktakeActualRepository = wmsStocktakeActualRepository;
        this.wmsContainerRepository = wmsContainerRepository;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtStocktakeActualHisRepository = mtStocktakeActualHisRepository;
        this.customSequence = customSequence;
    }

    private static final String STOCKTAKE_ACTUAL_CREATE_EVENT_TYPE_CODE = "STOCKTAKE_ACTUAL_CREATE";

    @Override
    @ProcessLovValue
    public Page<WmsStocktakeActualVO> pageAndSort(Long tenantId, WmsStocktakeActualQueryDTO dto, PageRequest pageRequest) {
        return stocktakeActualRepository.pageAndSort(tenantId, dto, pageRequest);
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsStocktakeActualVO> insertByLoadObject(Long tenantId, WmsStocktakeValidationDTO dto) {
        List<WmsMaterialLotAttrVO> lotList;
        if (CONTAINER.equals(dto.getLoadObjectType())) {
            lotList = wmsContainerRepository.getMaterialLotInContainer(tenantId, dto.getLoadObjectId());
        } else {
            // 直接校验物料
            lotList = Collections.singletonList(wmsMaterialLotRepository.selectListWithAttrById(tenantId, dto.getLoadObjectId()));
        }
        lotList.forEach(rec -> {
            MtStocktakeActualVO actual = new MtStocktakeActualVO();
            actual.setStocktakeId(dto.getStocktakeId());
            actual.setMaterialLotId(rec.getMaterialLotId());
            mtStocktakeActualRepository.stocktakeActualUpdate(tenantId, actual);
        });
        Set<String> idSet = lotList.stream().map(WmsMaterialLotAttrVO::getMaterialLotId).collect(Collectors.toSet());
        List<MtStocktakeActual> actualList = mtStocktakeActualRepository.selectByCondition(Condition.builder(MtStocktakeActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtStocktakeActual.FIELD_STOCKTAKE_ID, dto.getStocktakeId())
                .andIn(MtStocktakeActual.FIELD_MATERIAL_LOT_ID, idSet)).build());
        List<WmsStocktakeActualVO> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(MtStocktakeActual.class, WmsStocktakeActualVO.class, false);
        actualList.forEach(rec -> {
            WmsStocktakeActualVO vo = new WmsStocktakeActualVO();
            copier.copy(rec, vo, null);
            list.add(vo);
        });
        return list;
    }

    @Override
    @ProcessLovValue
    public List<WmsStocktakeActualExportDTO> export(Long tenantId, ExportParam exportParam, WmsStocktakeActualQueryDTO condition) {
        List<WmsStocktakeActualVO> list = wmsStocktakeActualRepository.listByDocId(tenantId, condition);
        List<WmsStocktakeActualExportDTO> result = new ArrayList<>(list.size());
        BeanCopier copier = BeanCopier.create(WmsStocktakeActualVO.class, WmsStocktakeActualExportDTO.class, false);
        list.forEach(src -> {
            WmsStocktakeActualExportDTO dto = new WmsStocktakeActualExportDTO();
            copier.copy(src, dto, null);
            result.add(dto);
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void adjust(Long tenantId, String stocktakeId, List<String> stocktakeActualIdList) {
        if (CollectionUtils.isEmpty(stocktakeActualIdList)) {
            return;
        }
        // 查询列表验证
        List<WmsStocktakeActualVO> actualList = wmsStocktakeActualRepository.selectListByIds(tenantId, stocktakeId, stocktakeActualIdList);
        List<String> notPassList = actualList.stream().filter(rec -> BigDecimal.ZERO.equals(rec.getDifferentQuantity()) || WmsConstant.CONSTANT_Y.equals(rec.getAdjustFlag()))
                .map(WmsStocktakeActualVO::getMaterialLotCode).distinct().collect(Collectors.toList());
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(notPassList), "WMS_STOCKTAKE_015",
                "WMS", StringUtils.join(notPassList, ","));

        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Date now = new Date();
        // 批量调整盘点实绩差异
        WmsStocktakeCostCenterVO costCenter = wmsStocktakeDocRepository.selectCostCenterByIds(tenantId, stocktakeId).get(0);

        // 获取盘点范围内的条码
        List<String> materialLotIdList = mtStocktakeDocRepository.stocktakeRangeLimitMaterialLotQuery(tenantId, stocktakeId);
        String costcenterId = costCenter.getCostcenterId();

        // 量调整盘点实绩差异
        MtStocktakeActualVO4 adjustData = new MtStocktakeActualVO4();
        adjustData.setStocktakeId(stocktakeId);
        adjustData.setMaterialLotIdList(materialLotIdList);
        adjustData.setMiscellaneousIssueCostCenterId(costcenterId);
        adjustData.setMiscellaneousReceiptCostCenterId(costcenterId);
        mtStocktakeActualRepository.stocktakeActualDifferenceBatchAdjust(tenantId, adjustData);

        // 创建事件
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(WmsConstant.EventType.STOCKTAKE_BATCH_ADJUST);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        // 更新盘点实绩扩展表
        WmsStocktakeActualAttrVO attrs = new WmsStocktakeActualAttrVO();
        attrs.setAdjustBy(userDetails.getUserId());
        attrs.setAdjustDate(now);
        wmsStocktakeActualRepository.attrUpdate(tenantId, stocktakeId, eventId, attrs);
        // 生成盘点调整事务
        wmsStocktakeActualRepository.stocktakeAdjustTransaction(tenantId, actualList, eventId);
    }

    @Override
    public void batchInsert(Long tenantId, String stocktakeId, String eventRequestId, List<String> materialLotIdList) {
        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocRepository.selectByPrimaryKey(stocktakeId);
        if (mtStocktakeDoc == null) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API:stocktakeActualUpdate】"));
        }

        // 验证条码有效性
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.selectByIds(Strings.join(materialLotIdList, ','));
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("MT_STOCKTAKE_0019",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0019", "STOCKTAKE",
                            Strings.join(materialLotList, ','), "【API:stocktakeActualUpdate】"));
        }
        if (materialLotList.size() < materialLotIdList.size()) {
            throw new CommonException("部分条码ID无效请检查");
        }

        // 验证条码是否已经在actual表中
        List<MtStocktakeActual> actualList = mtStocktakeActualRepository.selectByCondition(Condition.builder(MtStocktakeActual.class).andWhere(Sqls.custom().andEqualTo(MtStocktakeActual.FIELD_STOCKTAKE_ID, stocktakeId)
                .andIn(MtStocktakeActual.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
        if (CollectionUtils.isNotEmpty(actualList)) {
            throw new CommonException("以下条码已存在" + Strings.join(actualList.stream().map(MtStocktakeActual::getMaterialLotId).collect(Collectors.toSet()), ','));
        }

        // 插入actual
        this.batchInsertByMaterialLots(tenantId, stocktakeId, eventRequestId, materialLotList);
    }

    /**
     * 根据物料批批量新增盘点实际
     *
     * @param tenantId        租户
     * @param stocktakeId     盘点单ID
     * @param eventRequestId  事件请求ID
     * @param materialLotList 物料批列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 11:19:50
     */
    private void batchInsertByMaterialLots(Long tenantId, String stocktakeId, String eventRequestId, List<MtMaterialLot> materialLotList) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date nowDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        long userId = DetailsHelper.getUserDetails().getUserId();
        // 批量获取ID
        List<String> ids = customSequence.getNextKeys("mt_stocktake_actual_s", materialLotList.size());
        List<String> cids = customSequence.getNextKeys("mt_stocktake_actual_cid_s", materialLotList.size());
        List<String> hisIds = customSequence.getNextKeys("mt_stocktake_actual_his_s", materialLotList.size());
        List<String> hisCids = customSequence.getNextKeys("mt_stocktake_actual_his_cid_s", materialLotList.size());
        List<MtStocktakeActual> actualList = new ArrayList<>(materialLotList.size());
        List<MtStocktakeActualHis> actualHisList = new ArrayList<>(materialLotList.size());
        BeanCopier copier = BeanCopier.create(MtStocktakeActual.class, MtStocktakeActualHis.class, false);
        AtomicInteger indexGen = new AtomicInteger(0);
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(STOCKTAKE_ACTUAL_CREATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        materialLotList.forEach(materialLot -> {
            MtStocktakeActual actual = new MtStocktakeActual();
            int index = indexGen.getAndAdd(1);
            actual.setSiteId(materialLot.getSiteId());
            actual.setLotCode(materialLot.getLot());
            actual.setMaterialId(materialLot.getMaterialId());
            actual.setLocatorId(materialLot.getLocatorId());
            actual.setOwnerType(materialLot.getOwnerType());
            actual.setOwnerId(materialLot.getOwnerId());
            actual.setReservedObjectType(materialLot.getReservedObjectType());
            actual.setReservedObjectId(materialLot.getReservedObjectId());
            actual.setUomId(materialLot.getPrimaryUomId());
            actual.setCurrentQuantity(materialLot.getPrimaryUomQty());
            actual.setContainerId(materialLot.getCurrentContainerId());
            actual.setAdjustFlag(NO);
            actual.setTenantId(tenantId);
            actual.setStocktakeId(stocktakeId);
            actual.setMaterialLotId(materialLot.getMaterialLotId());
            actual.setStocktakeActualId(ids.get(index));
            actual.setCid(Long.valueOf(cids.get(index)));
            actual.setCreatedBy(userId);
            actual.setLastUpdatedBy(userId);
            actual.setCreationDate(nowDate);
            actual.setLastUpdateDate(nowDate);
            actual.setObjectVersionNumber(1L);
            actualList.add(actual);
            MtStocktakeActualHis his = new MtStocktakeActualHis();
            copier.copy(actual, his, null);
            his.setStocktakeActualHisId(hisIds.get(index));
            his.setCid(Long.valueOf(hisCids.get(index)));
            his.setEventId(eventId);
            actualHisList.add(his);
        });
        wmsStocktakeActualRepository.insertBatch(actualList);
        wmsStocktakeActualRepository.insertHisBatch(actualHisList);
    }
}
