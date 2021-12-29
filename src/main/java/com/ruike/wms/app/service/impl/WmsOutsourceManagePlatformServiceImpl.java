package com.ruike.wms.app.service.impl;

import com.ruike.qms.api.dto.QmsInvoiceHeadReturnDTO;
import com.ruike.wms.app.service.WmsOutsourceManagePlatformService;
import com.ruike.wms.domain.repository.WmsOutsourceManagePlatformRepository;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
* @Classname WmsOutsourceManagePlatformServiceImpl
* @Description 外协管理平台 -service实现
* @Date  2020/6/11 19:11
* @Created by Deng xu
*/
@Service
public class WmsOutsourceManagePlatformServiceImpl implements WmsOutsourceManagePlatformService {

    @Autowired
    private WmsOutsourceManagePlatformRepository repository;

    /**
    * @Description: 外协管理平台-查询单据头信息
    * @author: Deng Xu
    * @date 2020/6/11 20:52
    * @param tenantId 租户ID
    * @param condition 查询条件
    * @param pageRequest 分页信息
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    @Override
    @ProcessLovValue
    public Page<WmsOutsourceOrderHeadVO> listHeadForUi(Long tenantId, WmsOutsourceOrderHeadVO condition, PageRequest pageRequest) {
        return repository.listHeadForUi(tenantId,condition,pageRequest);
    }

    /**
    * @Description: 外协管理平台-根据单据头ID查询行信息
    * @author: Deng Xu
    * @date 2020/6/12 9:47
    * @param tenantId 租户ID
    * @param sourceDocId 单据头ID
    * @param pageRequest 分页信息
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
    * @version 1.0
    */
    @Override
    @ProcessLovValue
    public Page<WmsOutsourceOrderLineVO> listLineForUi(Long tenantId, String sourceDocId, PageRequest pageRequest) {
        return repository.listLineForUi(tenantId,sourceDocId,pageRequest);
    }

    @Override
    public Page<WmsOutsourceOrderDetailsVO> listLineDetailForUi(Long tenantId, String lineId, PageRequest pageRequest) {
        return repository.listLineDetailForUi(tenantId, lineId, pageRequest);
    }

    /**
    * @Description: 外协管理平台-退货单创建-查询头行信息
    * @author: Deng Xu
    * @date 2020/6/16 11:46
    * @param tenantId 租户ID
    * @param sourceDocId 外协送货单头ID
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    @Override
    public WmsOutsourceOrderHeadVO listLineForCreateReturnDoc(Long tenantId, String sourceDocId ) {
        return repository.listLineForCreateReturnDoc(tenantId , sourceDocId  );
    }

    /**
    * @Description: 外协管理平台-退货单创建-查询单号
    * @author: Deng Xu
    * @date 2020/7/2 11:37
    * @param tenantId 租户ID
    * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
    * @version 1.0
    */
    @Override
    public WmsOutsourceOrderHeadVO createHeadDoc(Long tenantId) {
        return repository.createHeadDoc(tenantId);
    }

    /**
    * @Description: 外协管理平台-退货单创建
    * @author: Deng Xu
    * @date 2020/6/16 13:43
    * @param tenantId 租户ID
    * @param createVo 创建VO
    * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
    * @version 1.0
    */
    @Override
    public WmsOutsourceOrderHeadVO createReturnDoc(Long tenantId, WmsOutsourceOrderHeadVO createVo) {
        return repository.createReturnDoc(tenantId ,createVo);
    }

    @Override
    public WmsReplenishmentOrderVO createHeaderQuery(Long tenantId, String sourceDocId) {
        return repository.createHeaderQuery(tenantId, sourceDocId);
    }

    @Override
    public WmsReplenishmentOrderVO createReplenishment(Long tenantId, WmsReplenishmentOrderVO wmsReplenishmentOrderVO) {
        return repository.createReplenishment(tenantId, wmsReplenishmentOrderVO);
    }

    @Override
    public WmsInstructionVO2 closeReturnDoc(Long tenantId, WmsInstructionVO2 wmsInstructionVO2) {
        return repository.closeReturnDoc(tenantId, wmsInstructionVO2);
    }

    @Override
    public void outsourceCreatePdf(Long tenantId, List<String> sourceDocIdList, HttpServletResponse response) {
        repository.outsourceCreatePdf(tenantId, sourceDocIdList, response);
    }

    @Override
    public WmsReplenishmentOrderLineVO queryInventoryQuantity(Long tenantId, WmsReplenishmentOrderLineVO lineVO) {
        return repository.queryInventoryQuantity(tenantId, lineVO);
    }

    @Override
    public List<WmsOutsourceExportVO> inventoryExcelExport(Long tenantId, WmsOutsourceOrderHeadVO headVO) {
        return repository.inventoryExcelExport(tenantId, headVO);
    }


}
