package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsLocatorRepository;
import com.ruike.wms.infra.mapper.WmsLocatorMapper;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * 库位 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 14:33
 */
@Component
public class WmsLocatorRepositoryImpl implements WmsLocatorRepository {
    private final WmsLocatorMapper mapper;

    public WmsLocatorRepositoryImpl(WmsLocatorMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<MtModLocator> selectListByType(Long tenantId, String warehouseId, String locatorType) {
        return mapper.selectListByType(tenantId, warehouseId, locatorType);
    }
}
