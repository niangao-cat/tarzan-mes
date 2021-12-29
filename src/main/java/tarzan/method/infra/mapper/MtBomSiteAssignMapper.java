package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomSiteAssign;

/**
 * 装配清单站点分配Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSiteAssignMapper extends BaseMapper<MtBomSiteAssign> {

    List<MtBomSiteAssign> queryBomSiteAssignByRouterId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomIdList") List<String> bomIdList);

}
