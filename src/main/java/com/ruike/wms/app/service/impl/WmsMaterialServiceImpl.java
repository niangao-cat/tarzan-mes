package com.ruike.wms.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruike.wms.app.service.WmsMaterialService;
import com.ruike.wms.domain.repository.WmsMaterialRepository;
import com.ruike.wms.domain.vo.WmsMaterialVO;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * WmsMaterialServiceImpl
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:22
 */
@Service
public class WmsMaterialServiceImpl implements WmsMaterialService {

    @Autowired
    private WmsMaterialRepository wmsMaterialRepository;

    @Override
    public Page<WmsMaterialVO> siteLimitMaterialQuery(Long tenantId, WmsMaterialVO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> wmsMaterialRepository.siteLimitMaterialQuery(tenantId, dto));
    }

    @Override
    public Page<WmsMaterialVO> userPermissionMaterialQuery(Long tenantId, WmsMaterialVO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> wmsMaterialRepository.userPermissionMaterialQuery(tenantId, dto));
    }
}
