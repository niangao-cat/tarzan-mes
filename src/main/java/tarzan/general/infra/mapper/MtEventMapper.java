package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtEventDTO;
import tarzan.general.api.dto.MtEventDTO2;
import tarzan.general.api.dto.MtEventDTO3;
import tarzan.general.domain.entity.MtEvent;
import tarzan.general.domain.vo.MtEventVO;
import tarzan.general.domain.vo.MtEventVO3;
import tarzan.general.domain.vo.MtEventVO4;
import tarzan.general.domain.vo.MtEventVO5;

/**
 * 事件记录Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventMapper extends BaseMapper<MtEvent> {

    List<MtEvent> selectEventByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<String> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtEventVO condition);

    List<String> requestLimitEventQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventRequestId") String eventRequestId,
                    @Param(value = "eventTypeCode") String eventTypeCode);

    List<MtEventVO3> selectParentEventId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtEventVO5> propertyLimitRequestAndEventQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventVO4 dto);

    /**
     * 查询事件
     * 
     * 特殊情况：如果有事件请求Id，需要进行分组，查询事件请求信息
     * 
     * @author benjamin
     * @date 2019-08-13 10:50
     * @param tenantId 租户Id
     * @param dto MtEventVO4
     * @param eventJudgeFlag String
     * @return list
     */
    List<MtEventDTO2> eventUnionRequestGroupQueryForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventDTO dto, @Param(value = "eventIdList") List<String> eventIdList,
                    @Param(value = "requestIdList") List<String> requestIdList,
                    @Param(value = "eventJudgeFlag") String eventJudgeFlag);

    List<MtEventDTO3> requestBatchLimitQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventDTO dto, @Param(value = "eventIdList") List<String> eventIdList,
                    @Param(value = "requestIdList") List<String> requestIdList);

    /**
     * 查询父事件对应事件集合
     *
     * @author benjamin
     * @date 2019-08-13 13:24
     * @param tenantId 租户Id
     * @param parentEventId 父事件Id
     * @return list
     */
    List<MtEventVO5> parentEventQueryForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "parentEventId") String parentEventId);


}
