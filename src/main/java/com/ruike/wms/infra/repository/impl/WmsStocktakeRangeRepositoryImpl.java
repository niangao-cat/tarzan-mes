package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import com.ruike.wms.infra.mapper.WmsStocktakeRangeMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.CustomSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.stocktake.domain.entity.MtStocktakeRange;
import tarzan.stocktake.infra.mapper.MtStocktakeRangeMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存盘点范围 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:20
 */
@Component
public class WmsStocktakeRangeRepositoryImpl implements WmsStocktakeRangeRepository {

    private final WmsStocktakeRangeMapper mapper;
    private final CustomSequence customSequence;
    private final MtStocktakeRangeMapper stocktakeRangeMapper;

    @Autowired
    public WmsStocktakeRangeRepositoryImpl(WmsStocktakeRangeMapper mapper, CustomSequence customSequence, MtStocktakeRangeMapper stocktakeRangeMapper) {
        this.mapper = mapper;
        this.customSequence = customSequence;
        this.stocktakeRangeMapper = stocktakeRangeMapper;
    }

    @Override
    public List<WmsStocktakeRangeVO> selectListByDocId(Long tenantId, String stocktakeId, String rangeObjectType) {
        WmsStocktakeRangeQueryDTO dto = new WmsStocktakeRangeQueryDTO();
        dto.setStocktakeId(stocktakeId);
        dto.setRangeObjectType(rangeObjectType);
        return mapper.selectListByDocId(tenantId, dto);
    }

    @Override
    public Page<WmsStocktakeRangeVO> pageStocktakeRange(Long tenantId, WmsStocktakeRangeQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mapper.selectListByDocId(tenantId, dto));
    }

    @Override
    public void batchInsertList(List<WmsStocktakeRangeVO> data, Long tenantId, String stocktakeId) {
        data.forEach(rec -> {
            MtStocktakeRange range = new MtStocktakeRange();
            range.setTenantId(tenantId);
            range.setStocktakeRangeId(customSequence.getNextKey("mt_stocktake_range_s"));
            range.setCid(Long.valueOf(customSequence.getNextKey("mt_stocktake_range_cid_s")));
            range.setStocktakeId(stocktakeId);
            range.setRangeObjectType(rec.getRangeObjectType());
            range.setRangeObjectId(rec.getRangeObjectId());
            range.setObjectVersionNumber(1L);
            stocktakeRangeMapper.insert(range);
            rec.setStocktakeRangeId(range.getStocktakeRangeId());
        });
    }

    @Override
    public void batchUpdateList(List<WmsStocktakeRangeVO> data) {
        List<List<WmsStocktakeRangeVO>> list = WmsCommonUtils.splitSqlList(data, null);
        list.forEach(mapper::batchUpdateList);
    }

    @Override
    public void batchDeleteList(Long tenantId, List<String> idList) {
        List<List<String>> list = WmsCommonUtils.splitSqlList(idList, null);
        list.forEach(deleteList -> mapper.batchDeleteList(tenantId, deleteList));
    }

    @Override
    public List<WmsStocktakeRangeVO> selectLocatorsByDocIds(Long tenantId, String stocktakeIdList) {
        return mapper.selectLocatorsByDocIds(tenantId, stocktakeIdList);
    }

    @Override
    public List<String> selectCoveredRangeList(Long tenantId, String rangeObjectType, String stocktakeId, List<String> rangeObjectIdList) {
        return mapper.selectCoveredRangeList(tenantId, rangeObjectType, stocktakeId, rangeObjectIdList);
    }

    @Override
    public List<WmsStocktakeRangeVO> selectActiveRangeList(Long tenantId, String rangeObjectType) {
        return mapper.selectActiveRangeList(tenantId, rangeObjectType);
    }

    @Override
    public List<WmsMaterialLotVO> selectMaterialLotInRange(Long tenantId, String stocktakeId) {
        return mapper.selectMaterialLotInRange(tenantId, stocktakeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsStocktakeRangeVO> batchDeleteByPrimaryKey(Long tenantId, String stocktakeId, String rangeObjectType, List<WmsStocktakeRangeVO> list) {
        List<String> idList = list.stream().map(WmsStocktakeRangeVO::getStocktakeRangeId).collect(Collectors.toList());
        this.batchDeleteList(tenantId, idList);
        return this.selectListByDocId(tenantId, stocktakeId, rangeObjectType);
    }
}
