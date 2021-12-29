package tarzan.calendar.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.calendar.domain.entity.MtCalendarOrgRel;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO1;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO2;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO3;

/**
 * 日历组织关系表资源库
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
public interface MtCalendarOrgRelRepository
                extends BaseRepository<MtCalendarOrgRel>, AopProxy<MtCalendarOrgRelRepository> {
    /**
     * calendarLimitOrganizationRelationQuery
     * 
     * @Author peng.yuan
     * @Date 2019/9/19 13:39
     * @param tenantId :
     * @param calendarId :
     * @return java.util.List<tarzan.calendar.domain.vo.MtCalendarOrgRelVO>
     */
    List<MtCalendarOrgRelVO> calendarLimitOrganizationRelationQuery(Long tenantId, String calendarId);

    /**
     * calendarOrganizationRelationUpdate-新增更新日历组织关系
     *
     * @param tenantId
     * @param vo
     * @return String calendarOrgRelId
     */
    String calendarOrganizationRelationUpdate(Long tenantId, MtCalendarOrgRelVO1 vo);

    /**
     * propertyLimitCalendarOrganizationRelationQuery-根据属性获取日历组织关系基础属性
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarOrgRelVO2> propertyLimitCalendarOrganizationRelationQuery(Long tenantId, MtCalendarOrgRelVO2 vo);

    /**
     * propertyLimitCalendarOrganizationRelationBatchQuery-根据属性批量获取日历组织关系基础属性
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarOrgRelVO2> propertyLimitCalendarOrganizationRelationBatchQuery(Long tenantId,
                    MtCalendarOrgRelVO3 vo);
}
