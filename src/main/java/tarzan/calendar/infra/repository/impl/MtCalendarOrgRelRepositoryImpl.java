package tarzan.calendar.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.entity.MtCalendarOrgRel;
import tarzan.calendar.domain.repository.MtCalendarOrgRelRepository;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO1;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO2;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO3;
import tarzan.calendar.infra.mapper.MtCalendarOrgRelMapper;

/**
 * 日历组织关系表 资源库实现
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
@Component
public class MtCalendarOrgRelRepositoryImpl extends BaseRepositoryImpl<MtCalendarOrgRel>
                implements MtCalendarOrgRelRepository {

    @Autowired
    private MtCalendarOrgRelMapper mtCalendarOrgRelMapper;

    @Autowired
    private MtCalendarRepository mtCalendarRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Override
    public List<MtCalendarOrgRelVO> calendarLimitOrganizationRelationQuery(Long tenantId, String calendarId) {
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "calendarId", "【API：calendarLimitOrganizationRelationQuery】"));
        }

        MtCalendarOrgRel mtCalendarOrgRel = new MtCalendarOrgRel();
        mtCalendarOrgRel.setTenantId(tenantId);
        mtCalendarOrgRel.setCalendarId(calendarId);
        List<MtCalendarOrgRel> calendarOrgRelList = mtCalendarOrgRelMapper.select(mtCalendarOrgRel);
        if (CollectionUtils.isEmpty(calendarOrgRelList)) {
            return Collections.emptyList();
        }

        List<MtCalendarOrgRelVO> resultList = new ArrayList<>(calendarOrgRelList.size());
        calendarOrgRelList.stream().forEach(t -> {
            MtCalendarOrgRelVO result = new MtCalendarOrgRelVO();
            result.setCalendarOrgRelId(t.getCalendarOrgRelId());
            result.setOrganizationId(t.getOrganizationId());
            result.setOrganizationType(t.getOrganizationType());
            result.setEnableFlag(t.getEnableFlag());
            resultList.add(result);
        });

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String calendarOrganizationRelationUpdate(Long tenantId, MtCalendarOrgRelVO1 vo) {
        // 第一步，判断参数是否均有输入且正确输入
        if (StringUtils.isEmpty(vo.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarOrganizationRelationUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getOrganizationId())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationId", "【API：calendarOrganizationRelationUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationType", "【API：calendarOrganizationRelationUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getEnableFlag())) {
            vo.setEnableFlag("Y");
        }
        // calendarId数据存在否则返回空
        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setCalendarId(vo.getCalendarId());
        mtCalendar.setTenantId(tenantId);
        mtCalendar = mtCalendarRepository.selectOne(mtCalendar);
        if (null == mtCalendar) {
            return null;
        }
        // 第二步创建一条数据，返回成功并输出参数calendarOrgRelId
        MtCalendarOrgRel mtCalendarOrgRel = new MtCalendarOrgRel();
        mtCalendarOrgRel.setCalendarId(vo.getCalendarId());
        mtCalendarOrgRel.setOrganizationId(vo.getOrganizationId());
        mtCalendarOrgRel.setOrganizationType(vo.getOrganizationType());
        mtCalendarOrgRel.setTenantId(tenantId);
        mtCalendarOrgRel = self().selectOne(mtCalendarOrgRel);
        if (null != mtCalendarOrgRel) {
            // 更新逻辑
            mtCalendarOrgRel.setEnableFlag(vo.getEnableFlag());
            self().updateByPrimaryKeySelective(mtCalendarOrgRel);
        } else {
            // 新增逻辑
            mtCalendarOrgRel = new MtCalendarOrgRel();
            mtCalendarOrgRel.setCalendarId(vo.getCalendarId());
            mtCalendarOrgRel.setOrganizationId(vo.getOrganizationId());
            mtCalendarOrgRel.setOrganizationType(vo.getOrganizationType());
            mtCalendarOrgRel.setEnableFlag(vo.getEnableFlag());
            mtCalendarOrgRel.setTenantId(tenantId);
            self().insertSelective(mtCalendarOrgRel);
        }
        return mtCalendarOrgRel.getCalendarOrgRelId();
    }


    @Override
    public List<MtCalendarOrgRelVO2> propertyLimitCalendarOrganizationRelationQuery(Long tenantId,
                    MtCalendarOrgRelVO2 vo) {
        List<MtCalendarOrgRelVO2> result = new ArrayList<>();
        if (vo != null) {
            MtCalendarOrgRel mtCalendarOrgRel = new MtCalendarOrgRel();
            if (StringUtils.isNotEmpty(vo.getCalendarId())) {
                mtCalendarOrgRel.setCalendarId(vo.getCalendarId());
            }
            if (StringUtils.isNotEmpty(vo.getCalendarOrgRelId())) {
                mtCalendarOrgRel.setCalendarOrgRelId(vo.getCalendarOrgRelId());
            }
            if (StringUtils.isNotEmpty(vo.getOrganizationId())) {
                mtCalendarOrgRel.setOrganizationId(vo.getOrganizationId());
            }
            if (StringUtils.isNotEmpty(vo.getOrganizationType())) {
                mtCalendarOrgRel.setOrganizationType(vo.getOrganizationType());
            }
            if (StringUtils.isNotEmpty(vo.getEnableFlag())) {
                mtCalendarOrgRel.setEnableFlag(vo.getEnableFlag());
            }
            List<MtCalendarOrgRel> orgRelList = self().select(mtCalendarOrgRel);
            if (CollectionUtils.isNotEmpty(orgRelList)) {
                result = orgRelList.stream().map(this::transFormMtCalendarOrgRel).collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    public List<MtCalendarOrgRelVO2> propertyLimitCalendarOrganizationRelationBatchQuery(Long tenantId,
                    MtCalendarOrgRelVO3 vo) {
        List<MtCalendarOrgRelVO2> result = new ArrayList<>();
        if (vo != null) {
            Sqls sql = Sqls.custom().andEqualTo(MtCalendarOrgRel.FIELD_TENANT_ID, tenantId);
            if (CollectionUtils.isNotEmpty(vo.getCalendarIdList())) {
                sql = sql.andIn(MtCalendarOrgRel.FIELD_CALENDAR_ID, vo.getCalendarIdList());
            }
            if (CollectionUtils.isNotEmpty(vo.getCalendarOrgRelIdList())) {
                sql = sql.andIn(MtCalendarOrgRel.FIELD_CALENDAR_ORG_REL_ID, vo.getCalendarOrgRelIdList());
            }
            if (CollectionUtils.isNotEmpty(vo.getOrganizationIdList())) {
                sql = sql.andIn(MtCalendarOrgRel.FIELD_ORGANIZATION_ID, vo.getOrganizationIdList());
            }
            if (CollectionUtils.isNotEmpty(vo.getOrganizationTypeList())) {
                sql = sql.andIn(MtCalendarOrgRel.FIELD_ORGANIZATION_TYPE, vo.getOrganizationTypeList());
            }
            if (StringUtils.isNotEmpty(vo.getEnableFlag())) {
                sql = sql.andEqualTo(MtCalendarOrgRel.FIELD_ENABLE_FLAG, vo.getEnableFlag());
            }
            List<MtCalendarOrgRel> orgRelList = mtCalendarOrgRelMapper
                            .selectByCondition(Condition.builder(MtCalendarOrgRel.class).andWhere(sql).build());
            if (CollectionUtils.isNotEmpty(orgRelList)) {
                result = orgRelList.stream().map(this::transFormMtCalendarOrgRel).collect(Collectors.toList());
            }
        }
        return result;
    }

    private MtCalendarOrgRelVO2 transFormMtCalendarOrgRel(MtCalendarOrgRel calendarOrgRel) {
        MtCalendarOrgRelVO2 calendarOrgRelVO2 = new MtCalendarOrgRelVO2();
        calendarOrgRelVO2.setCalendarOrgRelId(calendarOrgRel.getCalendarOrgRelId());
        calendarOrgRelVO2.setCalendarId(calendarOrgRel.getCalendarId());
        calendarOrgRelVO2.setOrganizationId(calendarOrgRel.getOrganizationId());
        calendarOrgRelVO2.setOrganizationType(calendarOrgRel.getOrganizationType());
        calendarOrgRelVO2.setEnableFlag(calendarOrgRel.getEnableFlag());
        return calendarOrgRelVO2;
    }
}
