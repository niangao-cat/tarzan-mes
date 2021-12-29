package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 料废调换接口记录表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
public interface ItfSrmMaterialWasteIfaceMapper extends BaseMapper<ItfSrmMaterialWasteIface> {

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWaste(@Param("tenantId") Long tenantId);

    /**
     * 查询SRM料废调换接口数据
     *
     * @param tenantId
     * @return
     * @author @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
     */
    List<ItfSrmMaterialWasteIfaceSyncDTO> selectSrmMaterialWasteParam(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                                      @Param("materialId") String materialId, @Param("ownerId") String ownerId);
}
