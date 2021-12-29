package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.infra.mapper.ItfSrmMaterialWasteIfaceMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 料废调换接口记录表 资源库实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
@Component
public class ItfSrmMaterialWasteIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSrmMaterialWasteIface> implements ItfSrmMaterialWasteIfaceRepository {

    @Autowired
    private ItfSrmMaterialWasteIfaceMapper itfSrmMaterialWasteIfaceMapper;

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    @Override
    public List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWaste(Long tenantId) {
        return itfSrmMaterialWasteIfaceMapper.selectSrmMaterialWaste(tenantId);
    }

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    @Override
    public List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWaste(Long tenantId, String siteId, String materialId, String locatorId, String ownerId) {
        return itfSrmMaterialWasteIfaceMapper.selectSrmMaterialWasteParam(tenantId, siteId, materialId, ownerId);
    }
}
