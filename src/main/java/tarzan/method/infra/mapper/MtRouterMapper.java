package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterDTO;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.vo.MtRouterStepVO8;
import tarzan.method.domain.vo.MtRouterVO11;
import tarzan.method.domain.vo.MtRouterVO12;
import tarzan.method.domain.vo.MtRouterVO2;

/**
 * 工艺路线Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterMapper extends BaseMapper<MtRouter> {
    List<MtRouter> selectRouterByIds(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "routerIds") List<String> routerIds);

    int selectRouterAvailability(@Param(value = "tenantId") Long tenantId, @Param(value = "routerId") String routerId);

    String selectMaxRevision(@Param(value = "tenantId") Long tenantId, @Param(value = "routerName") String routerName,
                             @Param(value = "routerType") String routerType);

    List<MtRouterVO2> selectRouterTL(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "routerId") String routerId);

    List<MtRouterStepVO8> selectRouterStep(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "routerId") String routerId);

    /**
     * UI查询工艺路线列表
     *
     * @param tenantId 租户Id
     * @param dto      MtRouterDTO
     * @return page
     * @author benjamin
     * @date 2019/9/18 3:57 PM
     */
    List<MtRouterDTO> queryRouterListForUi(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "dto") MtRouterDTO dto);

    List<MtRouterVO12> selectCondition(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "dto") MtRouterVO11 dto);

    List<MtRouter> selectRouterByNames(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "routerName") List<String> routerName);
}