package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomSubstituteHis;

/**
 * 装配清单行替代项历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteHisMapper extends BaseMapper<MtBomSubstituteHis> {

    List<MtBomSubstituteHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtBomSubstituteHis> selectByBomSubstituteId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomSubstituteId") String bomSubstituteId);

}
