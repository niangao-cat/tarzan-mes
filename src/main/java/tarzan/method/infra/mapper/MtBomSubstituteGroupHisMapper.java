package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomSubstituteGroupHis;
import tarzan.method.domain.vo.MtBomSubstituteGroupHisVO1;

/**
 * 装配清单行替代组历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteGroupHisMapper extends BaseMapper<MtBomSubstituteGroupHis> {

    List<MtBomSubstituteGroupHisVO1> selectBomSubstituteGroup(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponentId") String bomComponentId, @Param(value = "eventId") String eventId);

    List<MtBomSubstituteGroupHis> selectBomSubstituteGroupByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtBomSubstituteGroupHis> selectByBomSubstituteGroupId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomSubstituteGroupId") String bomSubstituteGroupId);

}
