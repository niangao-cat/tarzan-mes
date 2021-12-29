package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProductionGroupDTO2;
import com.ruike.hme.domain.entity.HmeProductionGroup;
import com.ruike.hme.domain.vo.HmeProductionGroupVO;
import com.ruike.hme.domain.vo.HmeProductionGroupVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品组Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
public interface HmeProductionGroupMapper extends BaseMapper<HmeProductionGroup> {

    /**
     * 产品组查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/27 02:47:43
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionGroupVO>
     */
    List<HmeProductionGroupVO> listQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeProductionGroupDTO2 dto);

    /**
     * 产品组行查询
     *
     * @param tenantId 租户ID
     * @param productionGroupId 头ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/4 01:52:28
     * @return java.util.List<HmeProductionGroupVO2>
     */
    List<HmeProductionGroupVO2> linePageQuery(@Param("tenantId") Long tenantId, @Param("productionGroupId") String productionGroupId);
}
