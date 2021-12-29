package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomReferencePoint;
import tarzan.method.domain.vo.MtBomReferencePointVO;
import tarzan.method.domain.vo.MtBomReferencePointVO2;

/**
 * 装配清单行参考点关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomReferencePointMapper extends BaseMapper<MtBomReferencePoint> {

    List<MtBomReferencePointVO2> selectConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtBomReferencePointVO condition);

    List<MtBomReferencePoint> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomReferencePointIds") List<String> bomReferencePointIds);

    List<MtBomReferencePoint> selectByComponentIdAndPointIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponentId") String bomComponentId,
                    @Param(value = "bomReferencePointIds") List<String> bomReferencePointIds);

}
