package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtSubstepDTO;
import tarzan.method.api.dto.MtSubstepDTO2;
import tarzan.method.domain.entity.MtSubstep;
import tarzan.method.domain.vo.MtSubstepVO;
import tarzan.method.domain.vo.MtSubstepVO1;

/**
 * 子步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
public interface MtSubstepMapper extends BaseMapper<MtSubstep> {

    List<MtSubstep> selectSubstepByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "substepIds") List<String> substepIds);

    List<MtSubstepDTO2> querySubstepForUi(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtSubstepDTO dto);

    List<MtSubstepVO1> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtSubstepVO dto);
}
