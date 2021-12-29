package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsStockDynamicReportDTO;
import com.ruike.wms.app.service.WmsStockDynamicReportService;
import com.ruike.wms.domain.vo.WmsStockDynamicReportVO;
import com.ruike.wms.infra.mapper.WmsStockDynamicReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/28 9:26
 */
@Service
public class WmsStockDynamicReportServiceImpl implements WmsStockDynamicReportService {

    @Autowired
    private WmsStockDynamicReportMapper wmsStockDynamicReportMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<WmsStockDynamicReportVO> queryList(Long tenantId, WmsStockDynamicReportDTO dto, PageRequest pageRequest) {
        if(StringUtils.isBlank(dto.getStockType())){
            throw new MtException("exception","出入库类型为空");
        }
        Page<WmsStockDynamicReportVO> page = new Page<>();
        if(dto.getStockType().equals("IN")){
            page = PageHelper.doPage(pageRequest, () -> wmsStockDynamicReportMapper.queryListIn(tenantId, dto));
        }else if(dto.getStockType().equals("OUT")){
            page = PageHelper.doPage(pageRequest, () -> wmsStockDynamicReportMapper.queryListOut(tenantId, dto));
        }
        String stockTypeMeaning = lovAdapter.queryLovMeaning("WMS.OUT_IN_TYPE", tenantId, dto.getStockType());
        for(WmsStockDynamicReportVO wmsStockDynamicReportVO:page){
            wmsStockDynamicReportVO.setStockTypeMeaning(stockTypeMeaning);
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public List<WmsStockDynamicReportVO> export(Long tenantId, WmsStockDynamicReportDTO dto, ExportParam exportParam) {
        if(StringUtils.isBlank(dto.getStockType())){
            throw new MtException("exception","出入库类型为空");
        }
        List<WmsStockDynamicReportVO> wmsStockDynamicReportVOList = new ArrayList<>();
        if(dto.getStockType().equals("IN")){
            wmsStockDynamicReportVOList = wmsStockDynamicReportMapper.queryListIn(tenantId, dto);
        }else if(dto.getStockType().equals("OUT")){
            wmsStockDynamicReportVOList = wmsStockDynamicReportMapper.queryListOut(tenantId, dto);
        }
        String stockTypeMeaning = lovAdapter.queryLovMeaning("WMS.OUT_IN_TYPE", tenantId, dto.getStockType());
        for(WmsStockDynamicReportVO wmsStockDynamicReportVO:wmsStockDynamicReportVOList){
            wmsStockDynamicReportVO.setStockTypeMeaning(stockTypeMeaning);
        }
        return wmsStockDynamicReportVOList;
    }
}
