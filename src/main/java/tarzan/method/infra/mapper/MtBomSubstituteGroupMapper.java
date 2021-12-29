package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO2;

/**
 * 装配清单行替代组Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteGroupMapper extends BaseMapper<MtBomSubstituteGroup> {

    List<MtBomSubstituteGroupVO2> selectConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtBomSubstituteGroupVO condition);

    List<MtBomSubstituteGroup> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomSubstituteGroupIds") List<String> bomSubstituteGroupIds);

    List<MtBomSubstituteGroup> selectByComponentIdAndGroupIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponentId") String bomComponentId,
                    @Param(value = "bomSubstituteGroupIds") List<String> bomSubstituteGroupIds);

}
