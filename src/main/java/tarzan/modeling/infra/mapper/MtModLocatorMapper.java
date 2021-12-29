package tarzan.modeling.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.api.dto.MtModLocatorDTO6;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.vo.*;

import java.util.List;

/**
 * 库位Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorMapper extends BaseMapper<MtModLocator> {


    List<MtModLocator> selectLocatorByGroup(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "locatorGroupId") String locatorGroupId,
                                            @Param(value = "parentCheck") String parentCheck);

    MtModLocatorVO2 selectLocatorById(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "dto") MtModLocatorVO2 dto);

    List<MtModLocator> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "whereSql") String whereSql);

    List<MtModLocatorVO6> selectByConditionForUi(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") MtModLocatorVO5 dto);

    MtModLocatorVO3 selectByIdCustom(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "locatorId") String locatorId);

    /**
     * 根据站点组织对象类型对象获取所属的站点货位LOV
     *
     * @author benjamin
     * @date 2019-08-19 10:43
     * @param tenantId 租户Id
     * @param dto MtModLocatorDTO6
     * @return list
     */
    List<MtModLocator> selectLovByConditionCustom(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "dto") MtModLocatorDTO6 dto);

    List<MtModLocatorVO8> selectLikeQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtModLocatorVO7 dto);

    List<String> selectByParentLocatorIds(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "parentLocatorIds") String parentLocatorIds);

    List<MtModLocator> selectByLocatorCodes(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "locatorCodes") String locatorCodes);

    List<MtModLocatorVO10> selectParentLocatorIds(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "dto") MtModLocator dto);
}
