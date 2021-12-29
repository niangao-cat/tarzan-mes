package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO3;

/**
 * 库位组Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorGroupMapper extends BaseMapper<MtModLocatorGroup> {

    List<MtModLocatorGroup> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "locatorGroupIds") List<String> locatorGroupIds);

    List<MtModLocatorGroupVO3> selectLikeQuery(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "dto") MtModLocatorGroupVO3 dto);
}
