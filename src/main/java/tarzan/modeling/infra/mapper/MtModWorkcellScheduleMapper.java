package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;

/**
 * 工作单元计划属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellScheduleMapper extends BaseMapper<MtModWorkcellSchedule> {

    List<MtModWorkcellSchedule> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "workcellIds") List<String> workcellIds);
}
