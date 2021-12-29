package tarzan.calendar.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.repository.MtCalendarOrgRelRepository;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.*;
import tarzan.calendar.infra.mapper.MtCalendarMapper;
import tarzan.calendar.infra.mapper.MtCalendarOrgRelMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.*;

/**
 * 工作日历 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@Component
public class MtCalendarRepositoryImpl extends BaseRepositoryImpl<MtCalendar> implements MtCalendarRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtCalendarMapper mtCalendarMapper;

    @Autowired
    private MtCalendarOrgRelMapper mtCalendarOrgRelMapper;

    @Autowired
    private MtCalendarOrgRelRepository mtCalendarOrgRelRepository;

    /**
     * 工作日历前台查询数据方法
     *
     * @author chuang.yang
     * @date 2019/12/3
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.calendar.domain.entity.MtCalendar>
     */
    @Override
    public List<MtCalendar> calendarQueryForUi(Long tenantId, MtCalendar dto) {
        return mtCalendarMapper.propertyOrgLimitQuery(tenantId, dto);
    }

    @Override
    public MtCalendarVO3 calendarGet(Long tenantId, String calendarId) {
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarGet】"));
        }

        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setTenantId(tenantId);
        mtCalendar.setCalendarId(calendarId);
        MtCalendar calendar = mtCalendarMapper.selectOne(mtCalendar);
        if (null == calendar) {
            return null;
        }

        MtCalendarVO3 calendarVO3 = new MtCalendarVO3();
        calendarVO3.setCalendarId(calendar.getCalendarId());
        calendarVO3.setCalendarCode(calendar.getCalendarCode());
        calendarVO3.setCalendarType(calendar.getCalendarType());
        calendarVO3.setDescription(calendar.getDescription());
        calendarVO3.setEnableFlag(calendar.getEnableFlag());

        // 查询组织关系
        List<MtCalendarOrgRelVO> calendarOrgRelList =
                        mtCalendarOrgRelRepository.calendarLimitOrganizationRelationQuery(tenantId, calendarId);
        calendarVO3.setCalendarOrgRelList(calendarOrgRelList);
        return calendarVO3;
    }

    @Override
    public List<MtCalendarVO3> calendarBatchGet(Long tenantId, List<String> calendarIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(calendarIds)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarIds", "【API：calendarBatchGet】"));
        }
        return mtCalendarMapper.calendarBatchGet(tenantId, calendarIds);
    }

    @Override
    public List<String> typeLimitCalendarQuery(Long tenantId, String calendarType) {
        if (StringUtils.isEmpty(calendarType)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarType", "【API：typeLimitCalendarQuery】"));
        }
        return mtCalendarMapper.typeLimitCalendarQuery(tenantId, calendarType);
    }

    @Override
    public List<String> organizationLimitCalendarQuery(Long tenantId, MtCalendarVO vo) {
        if (StringUtils.isEmpty(vo.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationType", "【API：organizationLimitCalendarQuery】"));
        }
        return mtCalendarOrgRelMapper.organizationLimitCalendarQuery(tenantId, vo.getOrganizationType(),
                        vo.getOrganizationId());
    }

    @Override
    public String calendarAvailableValidate(Long tenantId, String calendarId) {
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarAvailableValidate】"));
        }
        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setTenantId(tenantId);
        mtCalendar.setCalendarId(calendarId);
        mtCalendar = mtCalendarMapper.selectOne(mtCalendar);
        if (mtCalendar == null) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarAvailableValidate】"));
        } else {
            return mtCalendar.getEnableFlag();
        }
    }

    @Override
    public String organizationLimitOnlyCalendarGet(Long tenantId, MtCalendarVO2 dto) {
        // 校验输入参数是否有值
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationId", "【API：organizationLimitOnlyCalendarGet】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationType", "【API：organizationLimitOnlyCalendarGet】"));
        }
        if (StringUtils.isEmpty(dto.getCalendarType())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarType", "【API：organizationLimitOnlyCalendarGet】"));
        }
        if (StringUtils.isEmpty(dto.getSiteType())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "siteType", "【API：organizationLimitOnlyCalendarGet】"));
        }
        // 第二步调用API{ propertyLimitCalendarOrganizationRelationQuery }
        MtCalendarOrgRelVO2 calendarOrgRelVO2 = new MtCalendarOrgRelVO2();
        calendarOrgRelVO2.setOrganizationId(dto.getOrganizationId());
        calendarOrgRelVO2.setOrganizationType(dto.getOrganizationType());
        calendarOrgRelVO2.setEnableFlag("Y");
        List<MtCalendarOrgRelVO2> orgRels = mtCalendarOrgRelRepository
                        .propertyLimitCalendarOrganizationRelationQuery(tenantId, calendarOrgRelVO2);
        String calendarId = null;
        if (CollectionUtils.isNotEmpty(orgRels)) {
            Optional<String> first = orgRels.stream().map(MtCalendarOrgRelVO2::getCalendarId).findFirst();
            if (first.isPresent()) {
                calendarId = first.get();
            }
        }

        if (StringUtils.isNotEmpty(calendarId)) {
            // 逻辑修改(获取不到数据时将报错改为返回为空)
            MtCalendar calendar = new MtCalendar();
            calendar.setTenantId(tenantId);
            calendar.setCalendarId(calendarId);
            calendar.setCalendarType(dto.getCalendarType());
            calendar.setEnableFlag("Y");
            calendar = mtCalendarMapper.selectOne(calendar);

            if (calendar != null && StringUtils.isNotEmpty(calendar.getCalendarId())) {
                if ("SITE".equals(dto.getOrganizationType())) {
                    MtModSite modSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getOrganizationId());
                    // 逻辑修改(获取到数据为空则返回空)
                    if (modSite == null || StringUtils.isEmpty(modSite.getSiteCode())) {
                        return "";
                    }
                    if (modSite.getSiteType().equals(dto.getSiteType())) {
                        return calendar.getCalendarId();
                    } else {
                        return "";
                    }
                }

                MtModOrganizationRelVO relVO = new MtModOrganizationRelVO();
                relVO.setOrganizationId(dto.getOrganizationId());
                relVO.setOrganizationType(dto.getOrganizationType());
                relVO.setSiteType(dto.getSiteType());
                List<String> siteIds = mtModOrganizationRelRepository.organizationLimitSiteQuery(tenantId, relVO);
                if (CollectionUtils.isEmpty(siteIds)) {
                    return "";
                }
                return calendar.getCalendarId();
            }
        }

        if ("SITE".equals(dto.getOrganizationType())) {
            return "";
        }

        // 第四步
        MtModOrganizationRelVO relVO = new MtModOrganizationRelVO();
        relVO.setOrganizationId(dto.getOrganizationId());
        relVO.setOrganizationType(dto.getOrganizationType());
        relVO.setSiteType(dto.getSiteType());
        List<String> siteIds = mtModOrganizationRelRepository.organizationLimitSiteQuery(tenantId, relVO);

        MtModOrganizationVO2 vo2;

        if (CollectionUtils.isEmpty(siteIds)) {
            return "";
        } else {
            // 若I1=AREA或PROD_LINE
            if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getOrganizationType())
                            || MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(dto.getOrganizationType())) {
                // 获取上层区域，调用API{ parentOrganizationRelQuery }传入参数
                return returnContainerIds(tenantId, siteIds.get(0), dto.getOrganizationType(), dto.getOrganizationId(),
                                dto.getCalendarType());
            }

            // 3)若I1=WORKCELL
            if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getOrganizationType())) {
                // 获取上层产线，调用API{ parentOrganizationRelQuery }传入参数
                vo2 = new MtModOrganizationVO2();
                vo2.setTopSiteId(siteIds.get(0));
                vo2.setOrganizationType(dto.getOrganizationType());
                vo2.setOrganizationId(dto.getOrganizationId());
                vo2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                List<MtModOrganizationItemVO> vos =
                                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);

                if (CollectionUtils.isNotEmpty(vos)) {
                    if (vos.size() > 1) {
                        throw new MtException("MT_CALENDAR_0029", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_CALENDAR_0029", "CALENDAR", "【API：organizationLimitOnlyCalendarGet】"));
                    }

                    MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
                    orgRelVO2.setOrganizationId(vos.get(0).getOrganizationId());
                    orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                    orgRelVO2.setEnableFlag(MtBaseConstants.YES);
                    List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                                    .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);
                    List<MtCalendar> mtCalendars = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(relVO2s)) {
                        mtCalendars = mtCalendarMapper.selectByIdsAndType(tenantId, dto.getCalendarType(), relVO2s
                                        .stream().map(MtCalendarOrgRelVO2::getCalendarId).collect(Collectors.toList()));
                    }
                    if (CollectionUtils.isNotEmpty(mtCalendars)) {
                        Optional<String> optionalS =
                                        mtCalendars.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                        .map(MtCalendar::getCalendarId).findFirst();
                        if (optionalS.isPresent()) {
                            return optionalS.get();
                        }

                    } else {
                        return returnContainerIds(tenantId, siteIds.get(0), MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                        vos.get(0).getOrganizationId(), dto.getCalendarType());
                    }
                } else {
                    return returnContainerIds(tenantId, siteIds.get(0), MtBaseConstants.ORGANIZATION_TYPE.WORKCELL,
                                    dto.getOrganizationId(), dto.getCalendarType());
                }

            }
        }
        return "";
    }

    private String returnContainerIds(Long tenantId, String siteId, String organizationType, String organizationId,
                    String calendarType) {
        MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
        vo2.setTopSiteId(siteId);
        vo2.setOrganizationType(organizationType);
        vo2.setOrganizationId(organizationId);
        vo2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        vo2.setQueryType(MtBaseConstants.QUERY_TYPE.BOTTOM);
        List<MtModOrganizationItemVO> vos = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);
        if (CollectionUtils.isNotEmpty(vos)) {
            // 获取上层区域工作日历
            // 调用API{ calendarLimitOrganizationRelationQuery }
            MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
            orgRelVO2.setOrganizationId(vos.get(0).getOrganizationId());
            orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
            orgRelVO2.setEnableFlag(MtBaseConstants.YES);
            List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                            .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);

            List<MtCalendar> mtCalendars = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(relVO2s)) {
                mtCalendars = mtCalendarMapper.selectByIdsAndType(tenantId, calendarType,
                                relVO2s.stream().map(MtCalendarOrgRelVO2::getCalendarId).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(mtCalendars)) {
                Optional<String> optionalS =
                                mtCalendars.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                .map(MtCalendar::getCalendarId).findFirst();
                if (optionalS.isPresent()) {
                    return optionalS.get();
                }
            } else {
                return returnContainerIds(tenantId, siteId, MtBaseConstants.ORGANIZATION_TYPE.AREA,
                                vos.get(0).getOrganizationId(), calendarType);
            }
        } else {
            MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
            orgRelVO2.setOrganizationId(siteId);
            orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
            orgRelVO2.setEnableFlag(MtBaseConstants.YES);
            List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                            .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);

            List<MtCalendar> mtCalendars = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(relVO2s)) {
                mtCalendars = mtCalendarMapper.selectByIdsAndType(tenantId, calendarType,
                                relVO2s.stream().map(MtCalendarOrgRelVO2::getCalendarId).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(mtCalendars)) {
                Optional<String> optionalS =
                                mtCalendars.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                .map(MtCalendar::getCalendarId).findFirst();
                if (optionalS.isPresent()) {
                    return optionalS.get();
                }
            } else {
                return "";
            }
        }
        return "";
    }

    @Override
    public List<MtCalendarVO5> propertyLimitCalendarPropertyQuery(Long tenantId, MtCalendarVO4 vo) {
        return mtCalendarMapper.propertyLimitCalendarPropertyQuery(tenantId, vo);
    }

    @Override
    public List<MtCalendarVO7> organizationLimitOnlyCalendarBatchGet(Long tenantId, MtCalendarVO6 dto) {
        // 校验输入参数是否有值
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationType", "【API：organizationLimitOnlyCalendarBatchGet】"));
        }
        if (CollectionUtils.isEmpty(dto.getOrganizationIdList())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationIdList", "【API：organizationLimitOnlyCalendarBatchGet】"));
        }
        if (StringUtils.isEmpty(dto.getSiteType())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "siteType", "【API：organizationLimitOnlyCalendarBatchGet】"));
        }
        if (StringUtils.isEmpty(dto.getCalendarType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "calendarType", "【API：organizationLimitOnlyCalendarBatchGet】"));
        }

        // 2.获取calenderId
        MtCalendarOrgRelVO3 mtCalendarOrgRelVO3 = new MtCalendarOrgRelVO3();
        mtCalendarOrgRelVO3.setOrganizationIdList(dto.getOrganizationIdList());
        mtCalendarOrgRelVO3.setOrganizationTypeList(Arrays.asList(dto.getOrganizationType()));
        mtCalendarOrgRelVO3.setEnableFlag(MtBaseConstants.YES);
        List<MtCalendarOrgRelVO2> mtCalendarOrgRelVO2s = mtCalendarOrgRelRepository
                        .propertyLimitCalendarOrganizationRelationBatchQuery(tenantId, mtCalendarOrgRelVO3);


        List<MtCalendarVO7> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtCalendarOrgRelVO2s)) {
            // 3.筛选类型

            List<String> calendarIds = mtCalendarOrgRelVO2s.stream().map(MtCalendarOrgRelVO2::getCalendarId).distinct()
                            .collect(Collectors.toList());
            List<MtCalendar> mtCalendars =
                            mtCalendarMapper.selectByIdsAndType(tenantId, dto.getCalendarType(), calendarIds);
            List<String> filterCalendarIds =
                            mtCalendars.stream().map(MtCalendar::getCalendarId).distinct().collect(Collectors.toList());
            mtCalendarOrgRelVO2s = mtCalendarOrgRelVO2s.stream()
                            .filter(t -> filterCalendarIds.contains(t.getCalendarId())).collect(Collectors.toList());
        }

        List<String> unGetOrganizationId = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtCalendarOrgRelVO2s)) {
            List<String> ids = mtCalendarOrgRelVO2s.stream().map(MtCalendarOrgRelVO2::getOrganizationId)
                            .collect(Collectors.toList());
            unGetOrganizationId = dto.getOrganizationIdList().stream().filter(t -> !ids.contains(t))
                            .collect(Collectors.toList());
            // SITE 类型
            if (MtBaseConstants.ORGANIZATION_TYPE.SITE.equalsIgnoreCase(dto.getOrganizationType())) {
                List<MtModSite> mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId,
                                mtCalendarOrgRelVO2s.stream().map(MtCalendarOrgRelVO2::getOrganizationId).distinct()
                                                .collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(mtModSites)) {
                    List<String> siteIds =
                                    mtModSites.stream().filter(t -> dto.getSiteType().equalsIgnoreCase(t.getSiteType()))
                                                    .map(MtModSite::getSiteId).collect(Collectors.toList());
                    result.addAll(mtCalendarOrgRelVO2s.stream().filter(t -> siteIds.contains(t.getOrganizationId()))
                                    .map(t -> new MtCalendarVO7(t.getCalendarId(), t.getOrganizationId()))
                                    .collect(Collectors.toList()));
                }
            } else {
                MtModOrganizationRelVO2 mtModOrganizationRelVO2 = new MtModOrganizationRelVO2();
                mtModOrganizationRelVO2.setOrganizationIdList(mtCalendarOrgRelVO2s.stream()
                                .map(MtCalendarOrgRelVO2::getOrganizationId).distinct().collect(Collectors.toList()));
                mtModOrganizationRelVO2.setOrganizationType(dto.getOrganizationType());
                mtModOrganizationRelVO2.setSiteType(dto.getSiteType());
                List<MtModOrganizationRelVO3> mtModOrganizationRelVO3s = mtModOrganizationRelRepository
                                .organizationLimitSiteBatchQuery(tenantId, mtModOrganizationRelVO2);
                if (CollectionUtils.isNotEmpty(mtModOrganizationRelVO3s)) {
                    List<String> organizationIds = mtModOrganizationRelVO3s.stream()
                                    .map(MtModOrganizationRelVO3::getOrganizationId).collect(Collectors.toList());
                    result.addAll(mtCalendarOrgRelVO2s.stream()
                                    .filter(t -> organizationIds.contains(t.getOrganizationId()))
                                    .map(t -> new MtCalendarVO7(t.getCalendarId(), t.getOrganizationId()))
                                    .collect(Collectors.toList()));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(unGetOrganizationId)) {
            if (!MtBaseConstants.ORGANIZATION_TYPE.SITE.equalsIgnoreCase(dto.getOrganizationType())) {
                // 4.当前组织日历不存在，则找所属站点日历
                MtModOrganizationRelVO2 mtModOrganizationRelVO2 = new MtModOrganizationRelVO2();
                mtModOrganizationRelVO2.setSiteType(dto.getSiteType());
                mtModOrganizationRelVO2.setOrganizationIdList(unGetOrganizationId);
                mtModOrganizationRelVO2.setOrganizationType(dto.getOrganizationType());
                List<MtModOrganizationRelVO3> mtModOrganizationRelVO3s = mtModOrganizationRelRepository
                                .organizationLimitSiteBatchQuery(tenantId, mtModOrganizationRelVO2);
                if (CollectionUtils.isEmpty(mtModOrganizationRelVO3s)) {
                    result.addAll(unGetOrganizationId.stream().map(t -> {
                        MtCalendarVO7 vo7 = new MtCalendarVO7();
                        vo7.setCalendarId("");
                        vo7.setOrganizationId(t);
                        return vo7;
                    }).collect(Collectors.toList()));
                } else {
                    List<String> siteIds = mtModOrganizationRelVO3s.stream().map(MtModOrganizationRelVO3::getSiteId)
                                    .distinct().collect(Collectors.toList());

                    if (siteIds.size() > 1) {
                        throw new MtException("MT_CALENDAR_0028", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_CALENDAR_0028", "CALENDAR", "【API：organizationLimitOnlyCalendarBatchGet】"));
                    }

                    MtModOrganizationVO4 vo4;

                    // 若I1=AREA或PROD_LINE
                    if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getOrganizationType())
                                    || MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(dto.getOrganizationType())) {
                        // 获取上层区域，调用API{ parentOrganizationRelQuery }传入参数
                        result.addAll(returnBatchContainerIds(tenantId, siteIds.get(0), dto.getOrganizationType(),
                                        unGetOrganizationId, dto.getCalendarType()));
                    }

                    // 3)若I1=WORKCELL
                    if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getOrganizationType())) {
                        // 获取上层产线，调用API{ parentOrganizationRelQuery }传入参数
                        vo4 = new MtModOrganizationVO4();
                        vo4.setTopSiteId(siteIds.get(0));
                        vo4.setOrganizationType(dto.getOrganizationType());
                        vo4.setOrganizationIdList(unGetOrganizationId);
                        vo4.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);

                        List<MtModOrganizationVO5> vos =
                                        mtModOrganizationRelRepository.parentOrganizationRelBatchQuery(tenantId, vo4);
                        for (MtModOrganizationVO5 vo : vos) {
                            MtCalendarVO7 vo7 = new MtCalendarVO7();
                            vo7.setOrganizationId(vo.getOrganizationId());
                            if (CollectionUtils.isNotEmpty(vo.getParentOrganizationList())) {
                                if (vo.getParentOrganizationList().size() > 1) {
                                    throw new MtException("MT_CALENDAR_0029",
                                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                                                    "MT_CALENDAR_0029", "CALENDAR",
                                                                    "【API：organizationLimitOnlyCalendarGet】"));
                                }

                                MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
                                orgRelVO2.setOrganizationId(
                                                vo.getParentOrganizationList().get(0).getParentOrganizationId());
                                orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                                orgRelVO2.setEnableFlag(MtBaseConstants.YES);
                                List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                                                .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);
                                List<MtCalendar> mtCalendar = new ArrayList<>();
                                if (CollectionUtils.isNotEmpty(relVO2s)) {
                                    mtCalendar = mtCalendarMapper.selectByIdsAndType(tenantId, dto.getCalendarType(),
                                                    relVO2s.stream().map(MtCalendarOrgRelVO2::getCalendarId)
                                                                    .collect(Collectors.toList()));
                                }
                                if (CollectionUtils.isNotEmpty(mtCalendar)) {
                                    Optional<String> optionalS = mtCalendar.stream()
                                                    .filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                    .map(MtCalendar::getCalendarId).findFirst();
                                    if (optionalS.isPresent()) {
                                        vo7.setCalendarId(optionalS.get());
                                    }

                                } else {
                                    vo7.setCalendarId(returnContainerIds(tenantId, siteIds.get(0),
                                                    MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                                    vo.getParentOrganizationList().get(0).getParentOrganizationId(),
                                                    dto.getCalendarType()));
                                }
                            } else {
                                vo7.setCalendarId(returnContainerIds(tenantId, siteIds.get(0),
                                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, vo.getOrganizationId(),
                                                dto.getCalendarType()));
                            }
                            result.add(vo7);
                        }
                    }
                }
            }
        }
        return result;
    }


    private List<MtCalendarVO7> returnBatchContainerIds(Long tenantId, String siteId, String organizationType,
                    List<String> organizationIds, String calendarType) {
        List<MtCalendarVO7> resultList = new ArrayList<>();
        MtModOrganizationVO4 vo4 = new MtModOrganizationVO4();
        vo4.setTopSiteId(siteId);
        vo4.setOrganizationType(organizationType);
        vo4.setOrganizationIdList(organizationIds);
        vo4.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        vo4.setQueryType(MtBaseConstants.QUERY_TYPE.BOTTOM);
        List<MtModOrganizationVO5> vos = mtModOrganizationRelRepository.parentOrganizationRelBatchQuery(tenantId, vo4);
        for (MtModOrganizationVO5 vo : vos) {
            MtCalendarVO7 result = new MtCalendarVO7();
            result.setOrganizationId(vo.getOrganizationId());
            if (CollectionUtils.isNotEmpty(vo.getParentOrganizationList())) {
                // 获取上层区域工作日历
                // 调用API{ calendarLimitOrganizationRelationQuery }
                MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
                orgRelVO2.setOrganizationId(vo.getParentOrganizationList().get(0).getParentOrganizationId());
                orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
                orgRelVO2.setEnableFlag(MtBaseConstants.YES);
                List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                                .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);

                List<MtCalendar> mtCalendars = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(relVO2s)) {
                    mtCalendars = mtCalendarMapper.selectByIdsAndType(tenantId, calendarType, relVO2s.stream()
                                    .map(MtCalendarOrgRelVO2::getCalendarId).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(mtCalendars)) {
                    Optional<String> optionalS =
                                    mtCalendars.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                    .map(MtCalendar::getCalendarId).findFirst();
                    if (optionalS.isPresent()) {
                        result.setCalendarId(optionalS.get());
                    }
                } else {
                    result.setCalendarId(returnContainerIds(tenantId, siteId, MtBaseConstants.ORGANIZATION_TYPE.AREA,
                                    vo.getParentOrganizationList().get(0).getParentOrganizationId(), calendarType));
                }

            } else {
                MtCalendarOrgRelVO2 orgRelVO2 = new MtCalendarOrgRelVO2();
                orgRelVO2.setOrganizationId(siteId);
                orgRelVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                orgRelVO2.setEnableFlag(MtBaseConstants.YES);
                List<MtCalendarOrgRelVO2> relVO2s = mtCalendarOrgRelRepository
                                .propertyLimitCalendarOrganizationRelationQuery(tenantId, orgRelVO2);

                List<MtCalendar> mtCalendars = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(relVO2s)) {
                    mtCalendars = mtCalendarMapper.selectByIdsAndType(tenantId, calendarType, relVO2s.stream()
                                    .map(MtCalendarOrgRelVO2::getCalendarId).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(mtCalendars)) {
                    Optional<String> optionalS =
                                    mtCalendars.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                                    .map(MtCalendar::getCalendarId).findFirst();
                    if (optionalS.isPresent()) {
                        result.setCalendarId(optionalS.get());
                    }
                } else {
                    result.setCalendarId("");
                }
            }

            resultList.add(result);
        }
        return resultList;
    }
}
