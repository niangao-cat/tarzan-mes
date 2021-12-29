package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsStockAllocateSettingDTO;
import com.ruike.wms.domain.entity.WmsStockAllocateSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 库存调拨审核设置表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 17:21:32
 */
public interface WmsStockAllocateSettingMapper extends BaseMapper<WmsStockAllocateSetting> {
    
    /**
     * 
     * 
     * @param tenantId
     * @param dto 
     * @author jiangling.zheng@hand-china.com 2020/8/5 19:47
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockAllocateSettingDTO>
     */
    
    List<WmsStockAllocateSettingDTO> selectByConditionForUi(@Param("tenantId") Long tenantId, 
                                                            @Param("dto") WmsStockAllocateSettingDTO dto);
}
