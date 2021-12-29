package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeOrganizationUnitRel;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 组织职能关系表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-28 10:43:15
 */
public interface HmeOrganizationUnitRelMapper extends BaseMapper<HmeOrganizationUnitRel> {

    /**
     * 工作单元Id和类型查找唯一
     *
     * @param organizationId         工作单元Id
     * @param organizationType        类型
     * @return com.ruike.hme.domain.entity.HmeOrganizationUnitRel
     */
    HmeOrganizationUnitRel queryOrganizationUnitByOrganization(@Param("organizationId") String organizationId,@Param("organizationType") String organizationType);

}
