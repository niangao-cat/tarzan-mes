package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;

import java.util.List;

/**
 * 料废调换接口记录表资源库
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
public interface ItfSrmMaterialWasteIfaceRepository extends BaseRepository<ItfSrmMaterialWasteIface> {

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWaste(Long tenantId);

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWaste(Long tenantId, String siteId, String materialId, String locatorId, String ownerId);
}
