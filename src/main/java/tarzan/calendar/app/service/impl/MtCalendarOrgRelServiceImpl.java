package tarzan.calendar.app.service.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.calendar.api.dto.*;
import tarzan.calendar.app.service.MtCalendarOrgRelService;
import tarzan.calendar.domain.entity.MtCalendarOrgRel;
import tarzan.calendar.domain.repository.MtCalendarOrgRelRepository;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO3;
import tarzan.calendar.infra.mapper.MtCalendarOrgRelMapper;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;

/**
 * 日历组织关系表应用服务默认实现
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
@Service
public class MtCalendarOrgRelServiceImpl implements MtCalendarOrgRelService {

    @Autowired
    private MtCalendarOrgRelMapper mtCalendarOrgRelMapper;

    @Autowired
    private MtCalendarOrgRelRepository mtCalendarOrgRelRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtCalendarRepository mtCalendarRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<MtCalendarOrgRelDTO> queryCalendarOrgRelForUi(Long tenantId, MtCalendarOrgRelDTO1 dto,
                    PageRequest pageRequest) {
        Page<MtCalendarOrgRelDTO> base = PageHelper.doPage(pageRequest,
                        () -> mtCalendarOrgRelMapper.queryCalendarOrgRelForUi(tenantId, dto));

        // 获取组织类型数据
        List<MtGenType> orgGenTypes = mtGenTypeRepository.getGenTypes(tenantId,
                        MtBaseConstants.GEN_TYPE_MODULE.MODELING, MtBaseConstants.GEN_TYPE_GROUP.ORGANIZATION_TYPE);

        Map<String, MtGenType> orgGenTypeMap = null;
        if (CollectionUtils.isNotEmpty(orgGenTypes)) {
            orgGenTypeMap = orgGenTypes.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));
        }

        // 获取日历类型数据
        List<MtGenType> calGenTypes = mtGenTypeRepository.getGenTypes(tenantId,
                        MtBaseConstants.GEN_TYPE_MODULE.CALENDAR, MtBaseConstants.GEN_TYPE_GROUP.CALENDAR_TYPE);

        Map<String, MtGenType> calGenTypeMap = null;
        if (CollectionUtils.isNotEmpty(calGenTypes)) {
            calGenTypeMap = calGenTypes.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));
        }

        // 设置描述字段
        for (MtCalendarOrgRelDTO mtCalendarOrgRel : base) {
            if (MapUtils.isNotEmpty(orgGenTypeMap)) {
                MtGenType orgGenType = orgGenTypeMap.get(mtCalendarOrgRel.getOrganizationType());
                if (orgGenType != null) {
                    mtCalendarOrgRel.setOrganizationTypeDesc(orgGenType.getDescription());
                }
            }

            if (MapUtils.isNotEmpty(calGenTypeMap)) {
                MtGenType orgGenType = calGenTypeMap.get(mtCalendarOrgRel.getCalendarType());
                if (orgGenType != null) {
                    mtCalendarOrgRel.setCalendarTypeDesc(orgGenType.getDescription());
                }
            }
        }

        return base;
    }

    /**
     * UI批量保存工作日历分配组织
     *
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param dto
     * @return void
     */
    @Override
    public List<MtCalendarOrgRelDTO> saveCalendarOrgRelBatchForUi(Long tenantId, MtCalendarOrgRelDTO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getCalendarId()) || CollectionUtils.isEmpty(dto.getCalendarOrgRelList())) {
            return Collections.emptyList();
        }

        // 2. 一个组织，只能分配到有效的，且类型不同的日历
        MtCalendarVO3 mtCalendar = mtCalendarRepository.calendarGet(tenantId, dto.getCalendarId());
        if (mtCalendar == null) {
            return Collections.emptyList();
        }

        int updateCount = 0;

        // 3. 根据传入组织批量获取组织已分配的日历
        List<MtCalendarOrgRel> mtCalendarOrgRels =
                        mtCalendarOrgRelMapper.organizationLimitBatchQuery(tenantId, dto.getCalendarOrgRelList());
        if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
            List<String> calendarIdList =
                            mtCalendarOrgRels.stream().filter(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))
                                            .map(MtCalendarOrgRel::getCalendarId).collect(Collectors.toList());

            // 去除传入参数
            calendarIdList.remove(dto.getCalendarId());

            if (CollectionUtils.isNotEmpty(calendarIdList)) {
                List<MtCalendarVO3> mtCalendarList = mtCalendarRepository.calendarBatchGet(tenantId, calendarIdList);
                if (CollectionUtils.isNotEmpty(mtCalendarList)) {
                    // 已分配日历的类型集合
                    List<String> calendarTypes = mtCalendarList.stream().map(MtCalendarVO3::getCalendarType)
                                    .collect(Collectors.toList());
                    if (calendarTypes.contains(mtCalendar.getCalendarType())) {
                        throw new MtException("MT_CALENDAR_0027", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_CALENDAR_0027", "CALENDAR"));
                    }
                }
            }

            // 筛选当前日历下的组织数据
            List<MtCalendarOrgRel> curCalendarOrgRels = mtCalendarOrgRels.stream()
                            .filter(t -> dto.getCalendarId().equals(t.getCalendarId())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(curCalendarOrgRels)) {
                // 转为Map数据
                Map<CalendarOrgRelTuple, MtCalendarOrgRel> mtCalendarOrgRelMap = curCalendarOrgRels.stream()
                                .collect(Collectors.toMap(t -> new CalendarOrgRelTuple(t.getOrganizationId(),
                                                t.getOrganizationType()), t -> t));

                // 4. 判断当前日历下是否有这些组织数据，执行更新：设置已定义的id
                for (MtCalendarOrgRelDTO3 mtCalendarOrgRel : dto.getCalendarOrgRelList()) {
                    MtCalendarOrgRel alreadyCalendarOrgRel = mtCalendarOrgRelMap.get(new CalendarOrgRelTuple(
                                    mtCalendarOrgRel.getOrganizationId(), mtCalendarOrgRel.getOrganizationType()));
                    if (alreadyCalendarOrgRel != null) {
                        mtCalendarOrgRel.setCalendarOrgRelId(alreadyCalendarOrgRel.getCalendarOrgRelId());
                        updateCount = updateCount + 1;
                    }
                }
            }
        }

        // 公用变量
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        // 批量获取ID、CID
        List<String> calendarOrgRelIdList = customDbRepository.getNextKeys("mt_calendar_org_rel_s",
                        dto.getCalendarOrgRelList().size() - updateCount);
        List<String> calendarOrgRelCidList =
                        customDbRepository.getNextKeys("mt_calendar_org_rel_cid_s", dto.getCalendarOrgRelList().size());

        MtCalendarOrgRel mtCalendarOrgRel;

        List<String> dataIds = new ArrayList<>(dto.getCalendarOrgRelList().size());

        for (MtCalendarOrgRelDTO3 calendarOrgRel : dto.getCalendarOrgRelList()) {
            if (StringUtils.isNotEmpty(calendarOrgRel.getCalendarOrgRelId())) {
                mtCalendarOrgRel = new MtCalendarOrgRel();
                mtCalendarOrgRel.setTenantId(tenantId);
                mtCalendarOrgRel.setCalendarOrgRelId(calendarOrgRel.getCalendarOrgRelId());
                mtCalendarOrgRel.setCalendarId(dto.getCalendarId());
                mtCalendarOrgRel.setOrganizationId(calendarOrgRel.getOrganizationId());
                mtCalendarOrgRel.setOrganizationType(calendarOrgRel.getOrganizationType());
                mtCalendarOrgRel.setEnableFlag(calendarOrgRel.getEnableFlag());
                mtCalendarOrgRel.setCid(Long.valueOf(calendarOrgRelCidList.remove(0)));
                mtCalendarOrgRel.setLastUpdateDate(now);
                mtCalendarOrgRel.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getUpdateSql(mtCalendarOrgRel));
                dataIds.add(mtCalendarOrgRel.getCalendarOrgRelId());
            } else {
                mtCalendarOrgRel = new MtCalendarOrgRel();
                mtCalendarOrgRel.setTenantId(tenantId);
                mtCalendarOrgRel.setCalendarOrgRelId(calendarOrgRelIdList.remove(0));
                mtCalendarOrgRel.setCalendarId(dto.getCalendarId());
                mtCalendarOrgRel.setOrganizationId(calendarOrgRel.getOrganizationId());
                mtCalendarOrgRel.setOrganizationType(calendarOrgRel.getOrganizationType());
                mtCalendarOrgRel.setEnableFlag(MtBaseConstants.YES);
                mtCalendarOrgRel.setCid(Long.valueOf(calendarOrgRelCidList.remove(0)));
                mtCalendarOrgRel.setCreationDate(now);
                mtCalendarOrgRel.setCreatedBy(userId);
                mtCalendarOrgRel.setLastUpdateDate(now);
                mtCalendarOrgRel.setLastUpdatedBy(userId);
                sqlList.addAll(customDbRepository.getInsertSql(mtCalendarOrgRel));
                dataIds.add(mtCalendarOrgRel.getCalendarOrgRelId());
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            try {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            } catch (Exception ex) {
                throw new MtException("数据处理失败.");
            }
        }

        MtCalendarOrgRelDTO1 orgRelDTO1 = new MtCalendarOrgRelDTO1();
        orgRelDTO1.setCalendarId(dto.getCalendarId());
        List<MtCalendarOrgRelDTO> mtCalendarOrgRelDTOS =
                        mtCalendarOrgRelMapper.queryCalendarOrgRelForUi(tenantId, orgRelDTO1);

        mtCalendarOrgRelDTOS = mtCalendarOrgRelDTOS.stream().filter(c -> dataIds.contains(c.getCalendarOrgRelId()))
                        .collect(Collectors.toList());

        // 获取类型数据
        List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, MtBaseConstants.GEN_TYPE_MODULE.MODELING,
                        MtBaseConstants.GEN_TYPE_GROUP.ORGANIZATION_TYPE);
        if (CollectionUtils.isEmpty(genTypes)) {
            return mtCalendarOrgRelDTOS;
        }

        // 转为Map数据
        Map<String, MtGenType> mtGenTypeMap = genTypes.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));

        mtCalendarOrgRelDTOS.stream().forEach(t -> {
            MtGenType mtGenType = mtGenTypeMap.get(t.getOrganizationType());
            if (mtGenType != null) {
                t.setOrganizationTypeDesc(mtGenType.getDescription());
            }
        });
        return mtCalendarOrgRelDTOS;
    }

    /**
     * UI批量删除工作日历分配组织
     *
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param calendarOrgRelIdList
     * @return void
     */
    @Override
    public void removeCalendarOrgRelBatchForUi(Long tenantId, List<String> calendarOrgRelIdList) {
        if (CollectionUtils.isEmpty(calendarOrgRelIdList)) {
            return;
        }

        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        List<String> calendarOrgRelCidList =
                        customDbRepository.getNextKeys("mt_calendar_org_rel_cid_s", calendarOrgRelIdList.size());

        for (String calendarOrgRelId : calendarOrgRelIdList) {
            MtCalendarOrgRel mtCalendarOrgRel = new MtCalendarOrgRel();
            mtCalendarOrgRel.setTenantId(tenantId);
            mtCalendarOrgRel.setCalendarOrgRelId(calendarOrgRelId);
            mtCalendarOrgRel.setCid(Long.valueOf(calendarOrgRelCidList.remove(0)));
            mtCalendarOrgRel.setLastUpdateDate(now);
            mtCalendarOrgRel.setLastUpdatedBy(userId);

            // 失效掉
            mtCalendarOrgRel.setEnableFlag(MtBaseConstants.NO);
            sqlList.addAll(customDbRepository.getUpdateSql(mtCalendarOrgRel));
        }

        try {
            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        } catch (Exception ex) {
            throw new MtException("数据处理失败.");
        }
    }

    /**
     * UI日历组织分配，查询用户权限组织树
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.calendar.api.dto.MtCalendarOrgRelDTO4>
     */
    @Override
    public List<MtCalendarOrgRelDTO4> userLimitOrganizationTreeSingleForUi(Long tenantId, MtCalendarOrgRelDTO5 dto) {
        String topSiteId = dto.getTopSiteId();
        String parentOrganizationId = dto.getParentOrganizationId();
        String parentOrganizationType = dto.getParentOrganizationType();
        String calendarId = dto.getCalendarId();
        String calendarType = dto.getCalendarType();

        // 设置默认值
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            parentOrganizationType = MtBaseConstants.ORGANIZATION_TYPE.SITE;
        }

        // 查询该日历类型已经定义的所有组织
        List<MtCalendarOrgRel> mtCalendarOrgRelList =
                        mtCalendarOrgRelMapper.calendarTypeLimitIdBesides(tenantId, calendarType, calendarId);

        // 转为Map数据
        Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap = null;
        if (CollectionUtils.isNotEmpty(mtCalendarOrgRelList)) {
            orgTypeCalendarOrgRelMap = mtCalendarOrgRelList.stream()
                            .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationType));
        }

        boolean isBtnQueryType = MtBaseConstants.YES.equalsIgnoreCase(dto.getIsBtnQuery());

        switch (parentOrganizationType) {
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isBtnQueryType) {
                        MtModSite modSite = mtModSiteRepository.selectByPrimaryKey(parentOrganizationId);

                        MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                        subChildren.setTopSiteId(modSite.getSiteId());
                        subChildren.setCode(modSite.getSiteCode());
                        subChildren.setId(modSite.getSiteId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                        MtBaseConstants.ORGANIZATION_TYPE.SITE));
                        // 查询子结构
                        List<String> parentOrganizationIds =
                                        getParentOrganizationIds(tenantId, Arrays.asList(subChildren.getId()),
                                                        parentOrganizationType, modSite.getSiteId());
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));

                        // 查询是否已定义日历关系
                        subChildren.setAlreadyDefined(isAlreadyDefined(tenantId, calendarId, parentOrganizationId,
                                        parentOrganizationType));

                        // 查询是否存在同类型日历已定义标识
                        Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                        if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                            List<MtCalendarOrgRel> mtCalendarOrgRels =
                                            orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                            if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                                orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                                .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                            }
                        }

                        // 判断该组织是否已被其他同类型日历定义过
                        subChildren.setSameTypeCalDef(isSameTypeCalDef(orgIdCalendarOrgRelMap, modSite.getSiteId()));

                        return Arrays.asList(subChildren);
                    } else {
                        return this.getSingleLayerSite(tenantId, calendarId, topSiteId, parentOrganizationId,
                                        parentOrganizationType, true, orgTypeCalendarOrgRelMap);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isBtnQueryType) {
                        MtModArea modArea = mtModAreaRepository.selectByPrimaryKey(parentOrganizationId);

                        MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modArea.getAreaCode());
                        subChildren.setId(modArea.getAreaId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                        MtBaseConstants.ORGANIZATION_TYPE.AREA));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                        Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));

                        // 查询是否已定义日历关系
                        subChildren.setAlreadyDefined(isAlreadyDefined(tenantId, calendarId, parentOrganizationId,
                                        parentOrganizationType));

                        // 查询是否存在同类型日历已定义标识
                        Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                        if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                            List<MtCalendarOrgRel> mtCalendarOrgRels =
                                            orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.AREA);
                            if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                                orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                                .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                            }
                        }

                        // 判断该组织是否已被其他同类型日历定义过
                        subChildren.setSameTypeCalDef(isSameTypeCalDef(orgIdCalendarOrgRelMap, modArea.getAreaId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerArea(tenantId, calendarId, topSiteId, parentOrganizationId,
                                        parentOrganizationType, true, orgTypeCalendarOrgRelMap);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isBtnQueryType) {
                        MtModProductionLine modProductionLine =
                                        mtModProductionLineRepository.selectByPrimaryKey(parentOrganizationId);

                        MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modProductionLine.getProdLineCode());
                        subChildren.setId(modProductionLine.getProdLineId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                        MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                        Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));

                        // 查询是否已定义日历关系
                        subChildren.setAlreadyDefined(isAlreadyDefined(tenantId, calendarId, parentOrganizationId,
                                        parentOrganizationType));

                        // 查询是否存在同类型日历已定义标识
                        Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                        if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                            List<MtCalendarOrgRel> mtCalendarOrgRels =
                                            orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                            if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                                orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                                .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                            }
                        }

                        // 判断该组织是否已被其他同类型日历定义过
                        subChildren.setSameTypeCalDef(
                                        isSameTypeCalDef(orgIdCalendarOrgRelMap, modProductionLine.getProdLineId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerModelProdline(tenantId, calendarId, topSiteId, parentOrganizationId,
                                        parentOrganizationType, true, orgTypeCalendarOrgRelMap);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isBtnQueryType) {
                        MtModWorkcell modWorkcell = mtModWorkcellRepository.selectByPrimaryKey(parentOrganizationId);

                        MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modWorkcell.getWorkcellCode());
                        subChildren.setId(modWorkcell.getWorkcellId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                        MtBaseConstants.ORGANIZATION_TYPE.WORKCELL));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                        Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));

                        // 查询是否已定义日历关系
                        subChildren.setAlreadyDefined(isAlreadyDefined(tenantId, calendarId, parentOrganizationId,
                                        parentOrganizationType));

                        // 查询是否存在同类型日历已定义标识
                        Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                        if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                            List<MtCalendarOrgRel> mtCalendarOrgRels =
                                            orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                            if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                                orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                                .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                            }
                        }

                        // 判断该组织是否已被其他同类型日历定义过
                        subChildren.setSameTypeCalDef(
                                        isSameTypeCalDef(orgIdCalendarOrgRelMap, modWorkcell.getWorkcellId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerModelWorkcell(tenantId, calendarId, topSiteId, parentOrganizationId,
                                        parentOrganizationType, true, orgTypeCalendarOrgRelMap);
                    }
                }
                break;
            default:
                return null;
        }

        return null;
    }

    /**
     * 单层查询组织关系查询的相关函数 : SingleLayer
     *
     * @author chuang.yang
     * @date 2019/10/12
     */
    private List<MtCalendarOrgRelDTO4> getSingleLayerSite(Long tenantId, String calendarId, String topSiteId,
                    String parentOrganizationId, String parentOrganizationType, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {
        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setTenantId(tenantId);
        mtModOrganizationRel.setParentOrganizationType(parentOrganizationType);
        mtModOrganizationRel.setParentOrganizationId(parentOrganizationId);
        mtModOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(mtModOrganizationRel);

        List<MtCalendarOrgRelDTO4> childrenList = new ArrayList<MtCalendarOrgRelDTO4>();

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = this.getSubChildrenListForSiteOrArea(tenantId, calendarId, topSiteId, parentOrganizationId,
                            parentOrganizationType, mtModOrganizationRels, queryEnable, orgTypeCalendarOrgRelMap);
        }

        childrenList = childrenList.parallelStream()
                        .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                        .collect(Collectors.toList());

        return childrenList;
    }

    private List<MtCalendarOrgRelDTO4> getSingleLayerArea(Long tenantId, String calendarId, String topSiteId,
                    String parentOrganizationId, String parentOrganizationType, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {

        // 查询下是否有子节点
        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(modOrganizationRel);

        List<MtCalendarOrgRelDTO4> childrenList = new ArrayList<MtCalendarOrgRelDTO4>();
        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForSiteOrArea(tenantId, calendarId, topSiteId, parentOrganizationId,
                            parentOrganizationType, mtModOrganizationRels, queryEnable, orgTypeCalendarOrgRelMap);
        }

        childrenList = childrenList.parallelStream()
                        .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                        .collect(Collectors.toList());

        return childrenList;
    }

    /**
     * 查询站点、区域下的子组织：区域、产线、WKC
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param topSiteId
     * @param mtModOrganizationRels
     * @param queryEnable
     * @return java.util.List<hmes.mod_organization_rel.view.Children>
     */
    private List<MtCalendarOrgRelDTO4> getSubChildrenListForSiteOrArea(Long tenantId, String calendarId,
                    String topSiteId, String parentOrganizationId, String parentOrganizationType,
                    List<MtModOrganizationRel> mtModOrganizationRels, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {

        List<MtCalendarOrgRelDTO4> childrenList = Collections.synchronizedList(new ArrayList<MtCalendarOrgRelDTO4>());

        // 按照组织类型分组
        Map<String, List<MtModOrganizationRel>> modOrganizationRelMap = mtModOrganizationRels.stream()
                        .collect(Collectors.groupingBy(MtModOrganizationRel::getOrganizationType));

        // 站点、区域下有：区域、产线、WKC
        List<MtModOrganizationRel> modAreaRelList = new ArrayList<>();
        List<MtModOrganizationRel> modProdLineRelList = new ArrayList<>();
        List<MtModOrganizationRel> modWorkcellRelList = new ArrayList<>();
        for (Map.Entry<String, List<MtModOrganizationRel>> entry : modOrganizationRelMap.entrySet()) {
            switch (entry.getKey()) {
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    modAreaRelList = entry.getValue();
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    modProdLineRelList = entry.getValue();
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    modWorkcellRelList = entry.getValue();
                    break;
                default:
                    break;
            }
        }

        // 批量查询区域信息
        if (CollectionUtils.isNotEmpty(modAreaRelList)) {

            List<String> areaIds = modAreaRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                            .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(areaIds)) {
                List<MtModArea> modAreas = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);

                if (CollectionUtils.isNotEmpty(modAreas)) {
                    // 查询是否存在同类型日历已定义标识
                    Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                    if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                        List<MtCalendarOrgRel> mtCalendarOrgRels =
                                        orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.AREA);
                        if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                            orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                            .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                        }
                    }

                    // 批量查询组织与日历关系
                    List<String> alreadyDefinedOrgIds = getAlreadyDefinedOrganizationIds(tenantId, calendarId, areaIds,
                                    MtBaseConstants.ORGANIZATION_TYPE.AREA);

                    // 批量查询站点下的子结构
                    List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, areaIds,
                                    MtBaseConstants.ORGANIZATION_TYPE.AREA, topSiteId);

                    // 获取描述配置
                    String areaDes = this.getOrganizationDescriptionByCode(tenantId,
                                    MtBaseConstants.ORGANIZATION_TYPE.AREA);

                    // 转为Map数据
                    Map<String, MtModArea> modAreaMap =
                                    modAreas.stream().collect(Collectors.toMap(t -> t.getAreaId(), t -> t));

                    // 填充返回数据
                    Map<String, List<MtCalendarOrgRel>> finalOrgIdCalendarOrgRelMap = orgIdCalendarOrgRelMap;
                    mtModOrganizationRels.parallelStream().forEach(rel -> {
                        MtModArea modArea = modAreaMap.get(rel.getOrganizationId());
                        if (modArea != null) {
                            if ((queryEnable && isEnable(modArea.getEnableFlag())) || !queryEnable) {
                                MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                                subChildren.setTopSiteId(topSiteId);
                                subChildren.setCode(modArea.getAreaCode());
                                subChildren.setId(modArea.getAreaId());
                                subChildren.setType(rel.getOrganizationType());
                                subChildren.setText(areaDes);
                                subChildren.setSequence(rel.getSequence());
                                subChildren.setParentId(parentOrganizationId);
                                subChildren.setParentType(parentOrganizationType);
                                subChildren.setChildren(hasChildren(parentOrganizationIds, modArea.getAreaId()));
                                subChildren.setAlreadyDefined(
                                                isAlreadyDefined(alreadyDefinedOrgIds, modArea.getAreaId()));
                                subChildren.setSameTypeCalDef(
                                                isSameTypeCalDef(finalOrgIdCalendarOrgRelMap, modArea.getAreaId()));
                                childrenList.add(subChildren);
                            }
                        }
                    });
                }
            }
        }

        // 批量查询产线信息
        if (CollectionUtils.isNotEmpty(modProdLineRelList)) {

            List<String> prodLineIds = modProdLineRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                            .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(prodLineIds)) {
                List<MtModProductionLine> modProductionLines =
                                mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, prodLineIds);

                if (CollectionUtils.isNotEmpty(modProductionLines)) {
                    // 查询是否存在同类型日历已定义标识
                    Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                    if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                        List<MtCalendarOrgRel> mtCalendarOrgRels =
                                        orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                        if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                            orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                            .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                        }
                    }

                    // 批量查询组织与日历关系
                    List<String> alreadyDefinedOrgIds = getAlreadyDefinedOrganizationIds(tenantId, calendarId,
                                    prodLineIds, MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);

                    // 批量查询站点下的子结构
                    List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, prodLineIds,
                                    MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE, topSiteId);

                    // 获取描述配置
                    String prodLineDes = this.getOrganizationDescriptionByCode(tenantId,
                                    MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);

                    // 转成Map数据
                    Map<String, MtModProductionLine> modProductionLineMap = modProductionLines.stream()
                                    .collect(Collectors.toMap(t -> t.getProdLineId(), t -> t));

                    // 填充返回数据
                    Map<String, List<MtCalendarOrgRel>> finalOrgIdCalendarOrgRelMap = orgIdCalendarOrgRelMap;
                    mtModOrganizationRels.parallelStream().forEach(rel -> {
                        MtModProductionLine modProductionLine = modProductionLineMap.get(rel.getOrganizationId());
                        if (modProductionLine != null) {
                            if ((queryEnable && isEnable(modProductionLine.getEnableFlag())) || !queryEnable) {
                                MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                                subChildren.setTopSiteId(topSiteId);
                                subChildren.setCode(modProductionLine.getProdLineCode());
                                subChildren.setId(modProductionLine.getProdLineId());
                                subChildren.setType(rel.getOrganizationType());
                                subChildren.setText(prodLineDes);
                                subChildren.setSequence(rel.getSequence());
                                subChildren.setParentId(parentOrganizationId);
                                subChildren.setParentType(parentOrganizationType);
                                subChildren.setChildren(
                                                hasChildren(parentOrganizationIds, modProductionLine.getProdLineId()));
                                subChildren.setAlreadyDefined(isAlreadyDefined(alreadyDefinedOrgIds,
                                                modProductionLine.getProdLineId()));
                                subChildren.setSameTypeCalDef(isSameTypeCalDef(finalOrgIdCalendarOrgRelMap,
                                                modProductionLine.getProdLineId()));
                                childrenList.add(subChildren);
                            }
                        }
                    });
                }
            }
        }

        // 批量查询WKC信息
        if (CollectionUtils.isNotEmpty(modWorkcellRelList)) {

            List<String> workcellIds = modWorkcellRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                            .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(workcellIds)) {
                List<MtModWorkcell> modWorkcells =
                                mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

                if (CollectionUtils.isNotEmpty(modWorkcells)) {
                    // 查询是否存在同类型日历已定义标识
                    Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                    if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                        List<MtCalendarOrgRel> mtCalendarOrgRels =
                                        orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                        if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                            orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                            .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                        }
                    }

                    // 批量查询组织与日历关系
                    List<String> alreadyDefinedOrgIds = getAlreadyDefinedOrganizationIds(tenantId, calendarId,
                                    workcellIds, MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

                    // 批量查询站点下的子结构
                    List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, workcellIds,
                                    MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, topSiteId);

                    // 获取描述配置
                    String workCellDes = this.getOrganizationDescriptionByCode(tenantId,
                                    MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

                    // 转为Map数据
                    Map<String, MtModWorkcell> modWorkcellMap =
                                    modWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));

                    // 填充返回数据
                    Map<String, List<MtCalendarOrgRel>> finalOrgIdCalendarOrgRelMap = orgIdCalendarOrgRelMap;
                    mtModOrganizationRels.parallelStream().forEach(rel -> {
                        MtModWorkcell modWorkcell = modWorkcellMap.get(rel.getOrganizationId());
                        if (modWorkcell != null) {
                            if ((queryEnable && isEnable(modWorkcell.getEnableFlag())) || !queryEnable) {
                                MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                                subChildren.setTopSiteId(topSiteId);
                                subChildren.setCode(modWorkcell.getWorkcellCode());
                                subChildren.setId(modWorkcell.getWorkcellId());
                                subChildren.setType(rel.getOrganizationType());
                                subChildren.setText(workCellDes);
                                subChildren.setSequence(rel.getSequence());
                                subChildren.setParentId(parentOrganizationId);
                                subChildren.setParentType(parentOrganizationType);
                                subChildren.setChildren(
                                                hasChildren(parentOrganizationIds, modWorkcell.getWorkcellId()));
                                subChildren.setAlreadyDefined(
                                                isAlreadyDefined(alreadyDefinedOrgIds, modWorkcell.getWorkcellId()));
                                subChildren.setSameTypeCalDef(isSameTypeCalDef(finalOrgIdCalendarOrgRelMap,
                                                modWorkcell.getWorkcellId()));
                                childrenList.add(subChildren);
                            }
                        }
                    });
                }
            }
        }

        return childrenList;
    }

    private List<MtCalendarOrgRelDTO4> getSingleLayerModelProdline(Long tenantId, String calendarId, String topSiteId,
                    String parentOrganizationId, String parentOrganizationType, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {
        List<MtCalendarOrgRelDTO4> childrenList = new ArrayList<MtCalendarOrgRelDTO4>();

        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(modOrganizationRel);

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForProdLineOrWorkcell(tenantId, calendarId, topSiteId,
                            parentOrganizationId, parentOrganizationType, mtModOrganizationRels, queryEnable,
                            orgTypeCalendarOrgRelMap);
        }

        childrenList = childrenList.parallelStream()
                        .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                        .collect(Collectors.toList());

        return childrenList;
    }

    private List<MtCalendarOrgRelDTO4> getSingleLayerModelWorkcell(Long tenantId, String calendarId, String topSiteId,
                    String parentOrganizationId, String parentOrganizationType, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {
        List<MtCalendarOrgRelDTO4> childrenList = new ArrayList<MtCalendarOrgRelDTO4>();

        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(modOrganizationRel);

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForProdLineOrWorkcell(tenantId, calendarId, topSiteId,
                            parentOrganizationId, parentOrganizationType, mtModOrganizationRels, queryEnable,
                            orgTypeCalendarOrgRelMap);
        }

        childrenList = childrenList.parallelStream()
                        .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                        .collect(Collectors.toList());

        return childrenList;
    }

    /**
     * 查询产线、WKC下的子组织：WKC
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param topSiteId
     * @param mtModOrganizationRels
     * @param queryEnable
     * @return java.util.List<hmes.mod_organization_rel.view.Children>
     */
    private List<MtCalendarOrgRelDTO4> getSubChildrenListForProdLineOrWorkcell(Long tenantId, String calendarId,
                    String topSiteId, String parentOrganizationId, String parentOrganizationType,
                    List<MtModOrganizationRel> mtModOrganizationRels, boolean queryEnable,
                    Map<String, List<MtCalendarOrgRel>> orgTypeCalendarOrgRelMap) {

        List<MtCalendarOrgRelDTO4> childrenList = Collections.synchronizedList(new ArrayList<MtCalendarOrgRelDTO4>());

        // 产线、WKC下有：WKC, 批量查询WKC信息
        List<String> workcellIds = mtModOrganizationRels.stream().map(MtModOrganizationRel::getOrganizationId)
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<MtModWorkcell> modWorkcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

            if (CollectionUtils.isNotEmpty(modWorkcells)) {
                // 查询是否存在同类型日历已定义标识
                Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap = null;
                if (MapUtils.isNotEmpty(orgTypeCalendarOrgRelMap)) {
                    List<MtCalendarOrgRel> mtCalendarOrgRels =
                                    orgTypeCalendarOrgRelMap.get(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                    if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                        orgIdCalendarOrgRelMap = mtCalendarOrgRels.stream()
                                        .collect(Collectors.groupingBy(MtCalendarOrgRel::getOrganizationId));
                    }
                }

                // 批量查询组织与日历关系
                List<String> alreadyDefinedOrgIds = getAlreadyDefinedOrganizationIds(tenantId, calendarId, workcellIds,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

                // 批量查询站点下的子结构
                List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, workcellIds,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, topSiteId);

                // 获取描述配置
                String workCellDes = this.getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

                // 转成Map数据
                Map<String, MtModWorkcell> modWorkcellMap =
                                modWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));

                // 填充返回数据
                Map<String, List<MtCalendarOrgRel>> finalOrgIdCalendarOrgRelMap = orgIdCalendarOrgRelMap;
                mtModOrganizationRels.parallelStream().forEach(rel -> {
                    MtModWorkcell modWorkcell = modWorkcellMap.get(rel.getOrganizationId());
                    if (modWorkcell != null) {
                        if ((queryEnable && isEnable(modWorkcell.getEnableFlag())) || !queryEnable) {
                            MtCalendarOrgRelDTO4 subChildren = new MtCalendarOrgRelDTO4();
                            subChildren.setTopSiteId(topSiteId);
                            subChildren.setCode(modWorkcell.getWorkcellCode());
                            subChildren.setId(modWorkcell.getWorkcellId());
                            subChildren.setType(rel.getOrganizationType());
                            subChildren.setText(workCellDes);
                            subChildren.setSequence(rel.getSequence());
                            subChildren.setParentId(parentOrganizationId);
                            subChildren.setParentType(parentOrganizationType);
                            subChildren.setChildren(hasChildren(parentOrganizationIds, rel.getOrganizationId()));
                            subChildren.setAlreadyDefined(
                                            isAlreadyDefined(alreadyDefinedOrgIds, modWorkcell.getWorkcellId()));
                            subChildren.setSameTypeCalDef(
                                            isSameTypeCalDef(finalOrgIdCalendarOrgRelMap, modWorkcell.getWorkcellId()));
                            childrenList.add(subChildren);
                        }
                    }
                });
            }
        }

        return childrenList;
    }

    /**
     * 根据code查询prompt描述
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param code
     * @return java.lang.String
     */
    private String getOrganizationDescriptionByCode(Long tenantId, String code) {
        MtGenType genType = mtGenTypeRepository.getGenType(tenantId, MtBaseConstants.GEN_TYPE_MODULE.MODELING,
                        MtBaseConstants.GEN_TYPE_GROUP.ORGANIZATION_TYPE, code);
        if (genType != null) {
            return genType.getDescription();
        } else {
            return "";
        }
    }

    /**
     * 批量查询组织与日历关系，如果有则返回的当前组织自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param calendarId
     * @param organizationIds
     * @param organizationType
     * @return java.util.List<java.lang.String>
     */
    private List<String> getAlreadyDefinedOrganizationIds(Long tenantId, String calendarId,
                    List<String> organizationIds, String organizationType) {
        return mtCalendarOrgRelMapper.organizationCalendarLimitBatchQuery(tenantId, calendarId, organizationType,
                        organizationIds);
    }

    /**
     * 批量获取当前组织是否为父组织，如果有则返回当前组织自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param tenantId
     * @param parentOrganizationIds
     * @return java.util.List<java.lang.String>
     */
    private List<String> getParentOrganizationIds(Long tenantId, List<String> parentOrganizationIds,
                    String parentOrganizationType, String topSiteId) {
        List<String> result = new ArrayList<>();

        // 站点、区域、产线、WKC，可能存在子结构和子库位
        // 1. 查询 子结构
        List<String> organizationList = mtModOrganizationRelRepository.selectByParentOrganizationIds(tenantId,
                        parentOrganizationIds, parentOrganizationType, topSiteId);
        if (CollectionUtils.isNotEmpty(organizationList)) {
            result.addAll(organizationList);
        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 查询当前组织是否已经与日历定义关系
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param calendarId
     * @param organizationId
     * @param organizationType
     * @return boolean
     */
    private boolean isAlreadyDefined(Long tenantId, String calendarId, String organizationId, String organizationType) {
        MtCalendarOrgRel mtCalendarOrgRel = new MtCalendarOrgRel();
        mtCalendarOrgRel.setTenantId(tenantId);
        mtCalendarOrgRel.setCalendarId(calendarId);
        mtCalendarOrgRel.setOrganizationId(organizationId);
        mtCalendarOrgRel.setOrganizationType(organizationType);
        mtCalendarOrgRel.setEnableFlag(MtBaseConstants.YES);
        List<MtCalendarOrgRel> mtCalendarOrgRels = mtCalendarOrgRelRepository.select(mtCalendarOrgRel);
        if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断该组织是否已被其他同类型日历定义过
     *
     * @author chuang.yang
     * @date 2019/12/31
     * @param null
     * @return
     */
    private boolean isSameTypeCalDef(Map<String, List<MtCalendarOrgRel>> orgIdCalendarOrgRelMap,
                    String organizationId) {
        if (MapUtils.isNotEmpty(orgIdCalendarOrgRelMap)) {
            // 转为Map数据
            List<MtCalendarOrgRel> mtCalendarOrgRels = orgIdCalendarOrgRelMap.get(organizationId);
            if (CollectionUtils.isNotEmpty(mtCalendarOrgRels)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前组织是否存在子组织：除站点
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param parentOrganizationIds
     * @param organizationId
     * @return boolean
     */
    private boolean hasChildren(List<String> parentOrganizationIds, String organizationId) {
        if (CollectionUtils.isNotEmpty(parentOrganizationIds)) {
            return parentOrganizationIds.contains(organizationId);
        } else {
            return false;
        }
    }

    /**
     * 判断当前组织是否存在子组织：除站点
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param organizationIds
     * @param organizationId
     * @return boolean
     */
    private boolean isAlreadyDefined(List<String> organizationIds, String organizationId) {
        if (CollectionUtils.isNotEmpty(organizationIds)) {
            return organizationIds.contains(organizationId);
        } else {
            return false;
        }
    }

    /**
     * 是否有效
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param enableFlag
     * @return boolean
     */
    private boolean isEnable(String enableFlag) {
        return MtBaseConstants.YES.equals(enableFlag);
    }

    private static class CalendarOrgRelTuple implements Serializable {
        private static final long serialVersionUID = -2451382900126652858L;
        private String organizationId;
        private String organizationType;

        public CalendarOrgRelTuple(String organizationId, String organizationType) {
            this.organizationId = organizationId;
            this.organizationType = organizationType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CalendarOrgRelTuple that = (CalendarOrgRelTuple) o;
            return Objects.equal(organizationId, that.organizationId)
                            && Objects.equal(organizationType, that.organizationType);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(organizationId, organizationType);
        }
    }
}
