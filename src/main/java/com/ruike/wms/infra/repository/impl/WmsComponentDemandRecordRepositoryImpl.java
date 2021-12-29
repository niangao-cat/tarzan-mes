package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.vo.WmsComponentDemandDateVO;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import com.ruike.wms.infra.mapper.WmsComponentDemandRecordMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsComponentDemandRecord;
import com.ruike.wms.domain.repository.WmsComponentDemandRecordRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 组件需求记录表 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
@Component
public class WmsComponentDemandRecordRepositoryImpl extends BaseRepositoryImpl<WmsComponentDemandRecord> implements WmsComponentDemandRecordRepository {

    private final WmsComponentDemandRecordMapper wmsComponentDemandRecordMapper;

    public WmsComponentDemandRecordRepositoryImpl(WmsComponentDemandRecordMapper wmsComponentDemandRecordMapper) {
        this.wmsComponentDemandRecordMapper = wmsComponentDemandRecordMapper;
    }

    @Override
    public List<WmsComponentDemandRecord> selectListFromDispatch(Long tenantId, String woDispatchId) {
        return wmsComponentDemandRecordMapper.selectListFromDispatch(tenantId, woDispatchId);
    }

    @Override
    public List<WmsDistDemandDispatchRelVO> selectRelListByDispatchId(Long tenantId, String woDispatchId) {
        return wmsComponentDemandRecordMapper.selectRelListByDispatchId(tenantId, woDispatchId);
    }

    @Override
    public List<WmsComponentDemandRecord> selectNonCreatedListByDateRange(Long tenantId, Date startDate, Date endDate) {
        return wmsComponentDemandRecordMapper.selectNonCreatedListByDateRange(tenantId, startDate, endDate);
    }

    @Override
    public List<WmsComponentDemandSumVO> selectListByDateRange(Long tenantId, String workOrderId, Date startDate, Date endDate) {
        return wmsComponentDemandRecordMapper.selectSummaryListByDateRange(tenantId, workOrderId, startDate, endDate);
    }

    @Override
    public List<WmsComponentDemandDateVO> selectRequirementWithDate(Long tenantId, String workOrderId, String materialId, String materialVersion, String workcellId, Date startDate, Date endDate) {
        return wmsComponentDemandRecordMapper.selectRequirementWithDate(tenantId, workOrderId, materialId, materialVersion, workcellId, startDate, endDate);
    }

    @Override
    public List<WmsComponentDemandRecord> selectListByIds(Long tenantId, List<String> idList) {
        return wmsComponentDemandRecordMapper.selectListByIds(tenantId, idList);
    }

    @Override
    public List<WmsDistributionQtyVO> selectBarcodeOnhandBySite(Long tenantId, String siteId) {
        return wmsComponentDemandRecordMapper.selectBarcodeOnhandBySite(tenantId, siteId);
    }

    @Override
    public int updateByPrimaryKey(WmsComponentDemandRecord demandRecord) {
        return wmsComponentDemandRecordMapper.updateByPrimaryKey(demandRecord);
    }

    @Override
    public List<WmsComponentDemandRecord> batchUpdateByPrimaryKey(List<WmsComponentDemandRecord> demandRecordList) {
        demandRecordList.forEach(wmsComponentDemandRecordMapper::updateByPrimaryKey);
        return demandRecordList;
    }
}
