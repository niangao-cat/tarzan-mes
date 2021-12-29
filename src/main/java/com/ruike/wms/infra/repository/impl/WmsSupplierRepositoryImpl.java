package com.ruike.wms.infra.repository.impl;

import java.util.List;

import com.ruike.wms.domain.repository.WmsSupplierRepository;
import com.ruike.wms.domain.vo.WmsSupplierVO;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.infra.mapper.MtSupplierMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WmsSupplierRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 17:03
 */
@Component
public class WmsSupplierRepositoryImpl implements WmsSupplierRepository {
    @Autowired
    private MtSupplierMapper mtSupplierMapper;

    @Override
    public List<MtSupplier> selectByCondition(Long tenantId, WmsSupplierVO dto) {
        MtSupplier mtSupplier = new MtSupplier();
        mtSupplier.setTenantId(tenantId);
        if (StringUtils.isNotBlank(dto.getSupplierCode())) {
            mtSupplier.setSupplierCode(dto.getSupplierCode());
        }

        if (StringUtils.isNotBlank(dto.getSupplierName())) {
            mtSupplier.setSupplierName(dto.getSupplierName());
        }
        return mtSupplierMapper.select(mtSupplier);
    }
}
