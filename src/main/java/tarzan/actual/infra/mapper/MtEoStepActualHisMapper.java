package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoStepActualHis;

/**
 * 执行作业-工艺路线步骤执行实绩Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepActualHisMapper extends BaseMapper<MtEoStepActualHis> {

    List<MtEoStepActualHis> eoStepActualHisQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoId") String eoId);

}
