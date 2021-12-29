package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterLinkDTO;
import tarzan.method.api.dto.MtRouterLinkDTO2;
import tarzan.method.domain.entity.MtRouterLink;

/**
 * 嵌套工艺路线步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterLinkMapper extends BaseMapper<MtRouterLink> {

    List<MtRouterLink> selectRouterLinkByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterLinkDTO> queryRouterLinkForUi(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "routerStepIds") List<String> routerStepIds);

    MtRouterLinkDTO2 queryRouterLinkDetailForUi(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "routerId") String routerId);
}
