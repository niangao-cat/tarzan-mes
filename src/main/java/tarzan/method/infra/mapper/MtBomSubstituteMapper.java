package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.vo.MtBomSubstituteVO;
import tarzan.method.domain.vo.MtBomSubstituteVO2;
import tarzan.method.domain.vo.MtBomSubstituteVO5;

/**
 * 装配清单行替代项Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteMapper extends BaseMapper<MtBomSubstitute> {

    List<MtBomSubstitute> selectEnableSubstitute(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomSubstituteGroupId") String bomSubstituteGroupId);

    List<MtBomSubstituteVO2> selectConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtBomSubstituteVO condition);

    List<MtBomSubstitute> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "substituteIds") List<String> substituteIds);

    List<MtBomSubstitute> selectByGroupIdAndSubstituteIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomSubstituteGroupId") String bomSubstituteGroupId,
                    @Param(value = "substituteIds") List<String> substituteIds);

    List<MtBomSubstituteVO5> bomSubstituteBatchGetByBomCom(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "bomComponentIds") List<String> bomComponentIds);

    void deleteByBomSubstituteGroupId(@Param(value = "bomSubstituteGroupId") String bomSubstituteGroupId);
}
