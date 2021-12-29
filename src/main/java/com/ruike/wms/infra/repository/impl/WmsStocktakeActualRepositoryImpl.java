package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsStocktakeActualRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsStocktakeActualMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.hzero.boot.message.util.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.NO;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.YES;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeType.FIRST_COUNT;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionReasonCode.MISC_ISSUE;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionReasonCode.MISC_RECEIPT;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionTypeCode.MISC_IN;
import static com.ruike.wms.infra.constant.WmsConstant.TransactionTypeCode.MISC_OUT;

/**
 * 库存盘点实绩 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 19:09
 */
@Component
public class WmsStocktakeActualRepositoryImpl implements WmsStocktakeActualRepository {

    private final WmsStocktakeActualMapper wmsStocktakeActualMapper;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final WmsObjectTransactionRepository objectTransactionRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;

    public WmsStocktakeActualRepositoryImpl(WmsStocktakeActualMapper wmsStocktakeActualMapper, MtExtendSettingsRepository mtExtendSettingsRepository, WmsObjectTransactionRepository objectTransactionRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository) {
        this.wmsStocktakeActualMapper = wmsStocktakeActualMapper;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.objectTransactionRepository = objectTransactionRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
    }

    @Override
    public Page<WmsStocktakeActualVO> pageAndSort(Long tenantId, WmsStocktakeActualQueryDTO dto, PageRequest pageRequest) {
        List<WmsStocktakeActualVO> list = this.listByDocId(tenantId, dto);
        // 查询重复的物料批
        Set<String> duplicateMaterialLot = list.stream().collect(Collectors.groupingBy(WmsStocktakeActualVO::getMaterialLotId, Collectors.counting())).entrySet().stream().filter(rec -> rec.getValue().compareTo(1L) > 0).map(Map.Entry::getKey).collect(Collectors.toSet());
        Page<WmsStocktakeActualVO> page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
        page.getContent().forEach(rec -> rec.setDuplicateFlag(duplicateMaterialLot.contains(rec.getMaterialLotId()) ? YES : NO));
        return page;
    }

    @Override
    public List<WmsStocktakeActualVO> listByDocId(Long tenantId, WmsStocktakeActualQueryDTO condition) {
        condition.setTenantId(tenantId);
        List<WmsStocktakeActualVO> list = wmsStocktakeActualMapper.selectListByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>();
        }
        // 排序
        list = list.stream().sorted((k1, k2) -> {
            if (k1.getMaterialLotCode().equals(k2.getMaterialLotCode())) {
                if (Objects.isNull(k1.getDifferentQuantity()) && Objects.isNull(k2.getDifferentQuantity())) {
                    return 0;
                } else {
                    if (Objects.isNull(k1.getDifferentQuantity()) && Objects.nonNull(k2.getDifferentQuantity())) {
                        return -1;
                    } else if (Objects.nonNull(k1.getDifferentQuantity()) && Objects.isNull(k2.getDifferentQuantity())) {
                        return 1;
                    }
                    return k2.getDifferentQuantity().abs().compareTo(k1.getDifferentQuantity().abs());
                }
            } else {
                return k1.getMaterialLotCode().compareTo(k2.getMaterialLotCode());
            }
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<String> attrUpdate(Long tenantId, String stocktakeId, String eventId, WmsStocktakeActualAttrVO attrs) {
        List<MtExtendVO5> attrList = new ArrayList<>();
        if (Objects.nonNull(attrs.getFirstcountBy())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("FIRSTCOUNT_BY");
            attr.setAttrValue(String.valueOf(attrs.getFirstcountBy()));
        }
        if (Objects.nonNull(attrs.getFirstcountDate())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("FIRSTCOUNT_DATE");
            attr.setAttrValue(DateUtils.format(attrs.getFirstcountDate(), WmsConstant.DATETIME_FORMAT));
        }
        if (Objects.nonNull(attrs.getRecountBy())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("RECOUNT_BY");
            attr.setAttrValue(String.valueOf(attrs.getRecountBy()));
        }
        if (Objects.nonNull(attrs.getRecountDate())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("RECOUNT_DATE");
            attr.setAttrValue(DateUtils.format(attrs.getRecountDate(), WmsConstant.DATETIME_FORMAT));
        }
        if (Objects.nonNull(attrs.getAdjustBy())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("ADJUST_BY");
            attr.setAttrValue(String.valueOf(attrs.getAdjustBy()));
        }
        if (Objects.nonNull(attrs.getAdjustDate())) {
            MtExtendVO5 attr = new MtExtendVO5();
            attr.setAttrName("ADJUST_DATE");
            attr.setAttrValue(DateUtils.format(attrs.getAdjustDate(), WmsConstant.DATETIME_FORMAT));
        }
        return mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_stocktake_actual_attr", stocktakeId,
                eventId, attrList);
    }

    @Override
    public List<WmsStocktakeMaterialLotVO> selectMaterialLotByType(Long tenantId, String stocktakeId, String stocktakeTypeCode) {
        List<WmsStocktakeMaterialLotVO> list = wmsStocktakeActualMapper.selectMaterialLotByType(tenantId, stocktakeId, stocktakeTypeCode);
        list.forEach(rec -> {
            BigDecimal countQty = FIRST_COUNT.equals(stocktakeTypeCode) ? rec.getFirstcountQuantity() : rec.getRecountQuantity();
            if (Objects.isNull(countQty)) {
                rec.setSequence(1);
            } else if (countQty.equals(rec.getCurrentQuantity())) {
                rec.setSequence(3);
            } else {
                rec.setSequence(2);
            }
        });
        list = list.stream().sorted(Comparator.comparing(WmsStocktakeMaterialLotVO::getSequence)).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<WmsStocktakeActualVO> selectListByIds(Long tenantId, String stocktakeId, List<String> idList) {
        return wmsStocktakeActualMapper.selectListByIds(tenantId, stocktakeId, idList);
    }

    @Override
    public List<WmsObjectTransactionResponseVO> stocktakeAdjustTransaction(Long tenantId, List<WmsStocktakeActualVO> stocktakeActualList, String eventId) {
        List<WmsObjectTransactionRequestVO> requestList = new ArrayList<>();
        Date now = new Date();
        List<WmsTransactionTypeDTO> list = wmsTransactionTypeRepository.batchGetTransactionType(tenantId, Arrays.asList(MISC_IN, MISC_OUT));
        Map<String, String> typeMap = list.stream().collect(Collectors.toMap(WmsTransactionTypeDTO::getTransactionTypeCode, WmsTransactionTypeDTO::getMoveType));
        stocktakeActualList.forEach(rec -> {
            WmsObjectTransactionRequestVO request = new WmsObjectTransactionRequestVO();
            String transactionTypeCode = BigDecimal.ZERO.compareTo(rec.getDifferentQuantity()) > 0 ? MISC_IN : MISC_OUT;
            request.setTransactionTypeCode(transactionTypeCode);
            request.setEventId(eventId);
            request.setMaterialLotId(rec.getMaterialLotId());
            request.setMaterialId(rec.getMaterialId());
            request.setTransactionQty(rec.getDifferentQuantity().abs());
            request.setLotNumber(rec.getLotCode());
            request.setTransactionUom(rec.getUomCode());
            request.setTransactionTime(now);
            if (MISC_IN.equals(transactionTypeCode)) {
                request.setTransactionReasonCode(MISC_RECEIPT);
                request.setTransferPlantId(rec.getSiteId());
                request.setTransferWarehouseId(rec.getWarehouseId());
                request.setTransferLocatorId(rec.getLocatorId());
            } else {
                request.setTransactionReasonCode(MISC_ISSUE);
                request.setPlantId(rec.getSiteId());
                request.setWarehouseId(rec.getWarehouseId());
                request.setLocatorId(rec.getLocatorId());
            }
            request.setMoveType(typeMap.get(transactionTypeCode));
            requestList.add(request);
        });
        // 生成请求数据
        return objectTransactionRepository.objectTransactionSync(tenantId, requestList);
    }

    @Override
    public List<WmsStocktakeMaterialDetailVO> stocktakeDetailGet(Long tenantId, String stocktakeId, String stocktakeTypeCode, String materialCode) {
        return wmsStocktakeActualMapper.selectStocktakeDetailById(tenantId, stocktakeId, stocktakeTypeCode, materialCode);
    }

    @Override
    public Integer insertBatch(List<MtStocktakeActual> data) {
        return wmsStocktakeActualMapper.insertBatch(data);
    }

    @Override
    public Integer insertHisBatch(List<MtStocktakeActualHis> data) {
        return wmsStocktakeActualMapper.insertHisBatch(data);
    }
}
