package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModAreaSchedule;

/**
 * 区域计划属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModAreaScheduleMapper extends BaseMapper<MtModAreaSchedule> {


    List<MtModAreaSchedule> selectByIdsCustom(@Param("tenantId") Long tenantId, @Param("areaIds") List<String> areaIds);
}
