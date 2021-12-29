package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsPurchaseReturnService;
import com.ruike.wms.domain.repository.WmsPurchaseReturnRepository;
import com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购退货平台
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 12:57
 */
@Service
public class WmsPurchaseReturnServiceImpl implements WmsPurchaseReturnService {

    @Autowired
    private WmsPurchaseReturnRepository wmsPurchaseReturnRepository;

    @Override
    public Page<WmsPurchaseReturnHeadVO> purchaseReturnHeaderQuery(Long tenantId, PageRequest pageRequest, WmsPurchaseReturnVO wmsPurchaseReturnVO) {
        return wmsPurchaseReturnRepository.purchaseReturnHeaderQuery(tenantId, pageRequest, wmsPurchaseReturnVO);
    }

    @Override
    public Page<WmsPurchaseReturnLineVO> purchaseReturnLineQuery(Long tenantId, PageRequest pageRequest, String sourceDocId) {
        return wmsPurchaseReturnRepository.purchaseReturnLineQuery(tenantId, pageRequest, sourceDocId);
    }

    @Override
    public Page<WmsPurchaseReturnDetailsVO> purchaseReturnDetailsQuery(Long tenantId, PageRequest pageRequest, String instructionId) {
        return wmsPurchaseReturnRepository.purchaseReturnDetailsQuery(tenantId, pageRequest, instructionId);
    }

    @Override
    public void multiplePrint(Long tenantId, List<String> instructionDocIdList, HttpServletResponse httpServletResponse) {
        wmsPurchaseReturnRepository.multiplePrint(tenantId, instructionDocIdList, httpServletResponse);
    }
}
