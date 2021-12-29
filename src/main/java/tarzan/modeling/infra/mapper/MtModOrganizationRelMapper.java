package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.api.dto.*;
import tarzan.modeling.domain.entity.MtModOrganizationRel;

/**
 * 组织结构关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModOrganizationRelMapper extends BaseMapper<MtModOrganizationRel> {
    List<MtModOrganizationRel> selectByRelIds(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "relIds") List<String> relIds);

    void deleteBatch(@Param(value = "tenantId") Long tenantId, @Param(value = "ids") List<String> ids);

    List<MtModOrganizationDTO4> notExistLocatorGroupByLocator(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtModOrganizationDTO3 dto);

    List<MtModOrganizationDTO4> notExistLocatorGroupByOrganization(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "dto") MtModOrganizationDTO3 dto);

    List<MtModOrganizationDTO4> notExistLocatorByLocator(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "dto") MtModOrganizationDTO3 dto);

    List<MtModOrganizationDTO4> notExistLocatorByOrganization(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtModOrganizationDTO3 dto);

    List<MtModOrganizationDTO4> notExistOrganizationByOrganization(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "dto") MtModOrganizationDTO3 dto);

    List<MtModOrganizationDTO6> currentNodeOrder(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") MtModOrganizationDTO5 dto);

    List<MtModOrganizationRel> selectChildrenCount(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "rel") MtModOrganizationRel rel);

    List<String> selectByParentOrganizationIds(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "parentOrganizationIds") String parentOrganizationIds,
                                               @Param(value = "parentOrganizationType") String parentOrganizationType,
                                               @Param(value = "topSiteId") String topSiteId);

    List<MtModOrganizationRelDTO8> topSiteLimitOrganizaitonQuery(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "dto") MtModOrganizationRelDTO9 dto);

    List<MtModOrganizationRelDTO8> childLocatorQuery(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "locatorId") String locatorId);
}
