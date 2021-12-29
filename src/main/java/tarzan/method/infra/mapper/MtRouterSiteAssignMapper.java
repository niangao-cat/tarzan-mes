package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterSiteAssignDTO2;
import tarzan.method.domain.entity.MtRouterSiteAssign;

/**
 * 工艺路线站点分配表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterSiteAssignMapper extends BaseMapper<MtRouterSiteAssign> {

    /**
     * query router site assign by router id
     *
     * @author benjamin
     * @date 2019-07-31 10:08
     * @param routerIdList 工艺路线Id集合
     * @return List
     */
    List<MtRouterSiteAssign> queryRouterSiteAssignByRouterId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerIdList") List<String> routerIdList);

    List<MtRouterSiteAssignDTO2> queryRouterSiteAssignForUi(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "routerId") String routerIdList);
}
