package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsSupplierService;
import com.ruike.wms.domain.repository.WmsSupplierRepository;
import com.ruike.wms.domain.vo.WmsSupplierVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtSupplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WmsSupplierServiceImpl
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:48
 */
@Service
public class WmsSupplierServiceImpl implements WmsSupplierService {
    @Autowired
    private WmsSupplierRepository wmsSupplierRepository;

    @Override
    public Page<MtSupplier> uiQuery(Long tenantId, WmsSupplierVO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> wmsSupplierRepository.selectByCondition(tenantId, dto));
    }


}
