package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandDetailQueryDTO;
import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandQueryDTO;
import com.ruike.wms.app.service.WmsBarcodeInventoryOnHandQueryService;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandDetailVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO;
import com.ruike.wms.infra.mapper.WmsBarcodeInventoryOnHandQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description 条码库存现有量查询
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 11:12
 * @version 0.0.1
 * @return
 */
@Service
public class WmsBarcodeInventoryOnHandQueryServiceImpl implements WmsBarcodeInventoryOnHandQueryService {

    @Autowired
    private WmsBarcodeInventoryOnHandQueryMapper mapper;

    /**
     * @description 条码库存现有量查询
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     */
    @Override
    @ProcessLovValue
    public Page<WmsBarcodeInventoryOnHandVO> list(Long tenantId, PageRequest pageRequest, WmsBarcodeInventoryOnHandQueryDTO dto) {
        Page<WmsBarcodeInventoryOnHandVO> page = PageHelper.doPageAndSort(pageRequest, () -> mapper.headList(tenantId, dto));
        for(WmsBarcodeInventoryOnHandVO a: page){
            if(a.getSapAccountFlag()!=null && a.getSapAccountFlag().equals("N")){
                a.setSapAccountFlag("N");
            }else{
                a.setSapAccountFlag("Y");
            }
        }
        return  page;
    }


    /**
     * @description 条码库存现有量明细查询
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     */
    @Override
    @ProcessLovValue
    public Page<WmsBarcodeInventoryOnHandDetailVO> listDetail(Long tenantId, PageRequest pageRequest, WmsBarcodeInventoryOnHandDetailQueryDTO dto) {
        Page<WmsBarcodeInventoryOnHandDetailVO> page = PageHelper.doPageAndSort(pageRequest, () -> mapper.detailList(tenantId, dto));
        for(WmsBarcodeInventoryOnHandDetailVO a: page){
            if(a.getSapAccountFlag()!=null && a.getSapAccountFlag().equals("N")){
                a.setSapAccountFlag("N");
            }else{
                a.setSapAccountFlag("Y");
            }
        }
        return page;
    }

    /**
     * @description 条码库存现有量导出数据查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     */
    @Override
    public List<WmsBarcodeInventoryOnHandQueryExportVO> excelExport(Long tenantId, WmsBarcodeInventoryOnHandQueryDTO dto) {
        return mapper.excelExport(tenantId, dto);
    }
}
