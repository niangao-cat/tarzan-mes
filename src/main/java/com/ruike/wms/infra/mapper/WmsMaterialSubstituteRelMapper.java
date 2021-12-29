package com.ruike.wms.infra.mapper;

import java.util.List;

import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.vo.WmsMaterialPostingVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 物料全局替代关系表Mapper
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 16:54:02
 */
public interface WmsMaterialSubstituteRelMapper extends BaseMapper<WmsMaterialSubstituteRel> {

    /**
     * 获取物料全局替代关系
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @return WmsMaterialSubstituteRel
     */
    List<WmsMaterialSubstituteRel> selectMaterialSubstituteRel(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialId") String materialId);
}
