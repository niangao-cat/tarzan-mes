package com.ruike.wms.infra.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruike.wms.domain.repository.WmsMaterialRepository;
import com.ruike.wms.domain.vo.WmsMaterialVO;
import com.ruike.wms.infra.mapper.WmsMaterialMapper;

/**
 * WmsMaterialRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:58
 */
@Component
public class WmsMaterialRepositoryImpl implements WmsMaterialRepository {
    @Autowired
    private WmsMaterialMapper materialMapper;

    @Override
    public List<WmsMaterialVO> siteLimitMaterialQuery(Long tenantId, WmsMaterialVO dto) {
        return materialMapper.selectBySiteCondition(tenantId, dto);
    }

    @Override
    public List<WmsMaterialVO> userPermissionMaterialQuery(Long tenantId, WmsMaterialVO dto) {
        return materialMapper.selectByUserPermission(tenantId, dto);
    }

    @Override
    public List<WmsMaterialVO> listGetByItemGroup(Long tenantId, String siteId, String itemGroupCode, String warehouseId) {
        return materialMapper.selectOnhandItemByGroup(tenantId, siteId, itemGroupCode, warehouseId);
    }

}
