package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModProdLineSchedule;

/**
 * 生产线计划属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModProdLineScheduleMapper extends BaseMapper<MtModProdLineSchedule> {

    List<MtModProdLineSchedule> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "prodLineIds") List<String> prodLineIds);

}
