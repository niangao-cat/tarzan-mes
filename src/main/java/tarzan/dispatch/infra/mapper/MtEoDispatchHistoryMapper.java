package tarzan.dispatch.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.dispatch.domain.entity.MtEoDispatchHistory;
import tarzan.dispatch.domain.vo.MtEoDispatchHistoryVO1;

/**
 * 调度历史表，记录历史发布的调度结果和版本Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
public interface MtEoDispatchHistoryMapper extends BaseMapper<MtEoDispatchHistory> {

    /**
     * select eo dispatch history
     * <p>
     * condition: WORKCELL_ID & SHIFT_DATE & SHIFT_CODE
     *
     * @param tenantId
     * @param condition
     * @return list
     * @author benjamin
     * @date 2019-06-12 16:27
     */
    List<MtEoDispatchHistory> selectEoDispatchHistoryByWkc(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "condition") MtEoDispatchHistoryVO1 condition);

}
