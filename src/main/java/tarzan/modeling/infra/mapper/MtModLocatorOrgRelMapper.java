package tarzan.modeling.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocatorOrgRel;

/**
 * 组织与库位结构关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModLocatorOrgRelMapper extends BaseMapper<MtModLocatorOrgRel> {

    List<String> selectByParentOrganization(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "organizationType") String organizationType,
                                            @Param(value = "organizationIds") String organizationIds);

    List<MtModLocatorOrgRel> selectCountByOrganization(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "rel") MtModLocatorOrgRel rel);
}
