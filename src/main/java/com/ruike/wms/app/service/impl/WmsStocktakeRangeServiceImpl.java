package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsStocktakeRangeService;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.StocktakeRangeObjectType.LOCATOR;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeRangeObjectType.MATERIAL;

/**
 * <p>
 * 盘点范围 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 16:11
 */
@Service
public class WmsStocktakeRangeServiceImpl implements WmsStocktakeRangeService {
    private final WmsStocktakeRangeRepository stocktakeRangeRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtModLocatorRepository mtModLocatorRepository;

    public WmsStocktakeRangeServiceImpl(WmsStocktakeRangeRepository stocktakeRangeRepository, MtErrorMessageRepository mtErrorMessageRepository, MtModLocatorRepository mtModLocatorRepository) {
        this.stocktakeRangeRepository = stocktakeRangeRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
    }

    private void validateRangeBeforeSubmit(Long tenantId, String stocktakeId, String rangeObjectType, List<WmsStocktakeRangeVO> rangeList) {
        // 根据新增的这几个对象ID查询到含有这几个对象的单据，若含有则需要做交叉性校验
        List<String> rangeIdList = rangeList.stream().map(WmsStocktakeRangeVO::getRangeObjectId).collect(Collectors.toList());
        List<String> stocktakeIdList = stocktakeRangeRepository.selectCoveredRangeList(tenantId, rangeObjectType, stocktakeId, rangeIdList);
        // 若有交叉，则需要另一类型是否有重叠的范围
        if (stocktakeIdList.size() > 0) {
            // 取得另一个类型的活跃的盘点单据的范围是否有交叉
            String otherRangeObjectType = MATERIAL.equals(rangeObjectType) ? LOCATOR : MATERIAL;
            List<WmsStocktakeRangeVO> otherList = stocktakeRangeRepository.selectActiveRangeList(tenantId, otherRangeObjectType);
            Set<String> curOtherIdSet = otherList.stream().filter(rec -> rec.getStocktakeId().equals(stocktakeId)).map(WmsStocktakeRangeVO::getRangeObjectId).collect(Collectors.toSet());
            Set<String> otherIdSet = otherList.stream().filter(rec -> stocktakeIdList.contains(rec.getStocktakeId())).map(WmsStocktakeRangeVO::getRangeObjectId).collect(Collectors.toSet());
            // 判断是否有交集
            curOtherIdSet.retainAll(otherIdSet);
            if (curOtherIdSet.size() > 0) {
                throw new MtException("MT_STOCKTAKE_0006",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0006", "STOCKTAKE", stocktakeId, "【API：validateRangeBeforeSubmit】"));
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsStocktakeRangeVO> batchInsert(Long tenantId, String stocktakeId, String rangeObjectType, List<WmsStocktakeRangeVO> rangeList) {
        // 查询已经存在的范围对象，若存在这些数据直接忽略不处理
        List<WmsStocktakeRangeVO> dbList = stocktakeRangeRepository.selectListByDocId(tenantId, stocktakeId, rangeObjectType);
        Set<String> dbIdList = dbList.stream().map(WmsStocktakeRangeVO::getRangeObjectId).collect(Collectors.toSet());
        rangeList.removeIf(rec -> dbIdList.contains(rec.getRangeObjectId()));
        if (CollectionUtils.isNotEmpty(rangeList)) {
            this.validateRangeBeforeSubmit(tenantId, stocktakeId, rangeObjectType, rangeList);
            rangeList.forEach(obj -> obj.setRangeObjectType(rangeObjectType));
            stocktakeRangeRepository.batchInsertList(rangeList, tenantId, stocktakeId);
        }
        return rangeList;
    }

    @Override
    public MtModLocator locatorGet(Long tenantId, String stocktakeId, String locatorCode) {
        List<MtModLocator> list = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId).
                andEqualTo(MtModLocator.FIELD_LOCATOR_CODE, locatorCode)).build());
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("WMS_COST_CENTER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0041", "WMS", locatorCode));
        }
        String locatorId = list.get(0).getLocatorId();
        List<WmsStocktakeRangeVO> rangeList = stocktakeRangeRepository.selectListByDocId(tenantId, stocktakeId, LOCATOR);
        if (rangeList.stream().noneMatch(rec -> locatorId.equals(rec.getRangeObjectId()))) {
            throw new MtException("WMS_STOCKTAKE_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_STOCKTAKE_002", "WMS", locatorCode));
        }
        return mtModLocatorRepository.selectByPrimaryKey(locatorId);
    }
}
