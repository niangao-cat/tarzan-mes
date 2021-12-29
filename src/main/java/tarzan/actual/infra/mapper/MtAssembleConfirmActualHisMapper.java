package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;

/**
 * 装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleConfirmActualHisMapper extends BaseMapper<MtAssembleConfirmActualHis> {

    List<MtAssembleConfirmActualHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "eventIds") List<String> eventIds);

    List<MtAssembleConfirmActualHis> mySelect(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtAssembleConfirmActualHis dto);

}
