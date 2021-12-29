package tarzan.calendar.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.calendar.api.dto.MtCalendarOrgRelDTO;
import tarzan.calendar.api.dto.MtCalendarOrgRelDTO1;
import tarzan.calendar.api.dto.MtCalendarOrgRelDTO3;
import tarzan.calendar.domain.entity.MtCalendarOrgRel;

/**
 * 日历组织关系表Mapper
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
public interface MtCalendarOrgRelMapper extends BaseMapper<MtCalendarOrgRel> {

    List<String> organizationLimitCalendarQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "organizationType") String organizationType,
                    @Param(value = "organizationId") String organizationId);

    List<MtCalendarOrgRelDTO> queryCalendarOrgRelForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtCalendarOrgRelDTO1 dto);

    List<String> organizationCalendarLimitBatchQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "calendarId") String calendarId,
                    @Param(value = "organizationType") String organizationType,
                    @Param(value = "organizationIds") List<String> organizationIds);

    List<MtCalendarOrgRel> organizationLimitBatchQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "organizations") List<MtCalendarOrgRelDTO3> organizations);

    /**
     * 查询除该日历ID之外，相同类型日历的组织信息
     *
     * @author chuang.yang
     * @date 2019/12/31
     * @param tenantId
     * @param calendarType
     * @param calendarId
     * @return java.util.List<tarzan.calendar.domain.entity.MtCalendarOrgRel>
     */
    List<MtCalendarOrgRel> calendarTypeLimitIdBesides(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "calendarType") String calendarType, @Param(value = "calendarId") String calendarId);

}
