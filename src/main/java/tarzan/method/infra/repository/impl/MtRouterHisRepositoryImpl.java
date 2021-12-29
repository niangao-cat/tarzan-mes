package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtSqlHelper;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.MtRouterHisRepository;
import tarzan.method.infra.mapper.*;

/**
 * 工艺路线历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Component
public class MtRouterHisRepositoryImpl extends BaseRepositoryImpl<MtRouterHis> implements MtRouterHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterMapper mtRouterMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Autowired
    private MtRouterStepGroupStepMapper mtRouterStepGroupStepMapper;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterReturnStepMapper mtRouterReturnStepMapper;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtRouterSubstepMapper mtRouterSubstepMapper;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Autowired
    private MtRouterSubstepComponentMapper mtRouterSubstepComponentMapper;

    @Autowired
    private MtRouterSiteAssignMapper mtRouterSiteAssignMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerHisCreate(Long tenantId, MtRouterHis dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerHisCreate】"));

        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "eventId", "【API:routerHisCreate】"));
        }

        // Step2校验是否存在对应ROUTER的数据
        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(dto.getRouterId());
        mtRouter = mtRouterMapper.selectOne(mtRouter);
        if (mtRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerHisCreate】"));
        }

        // Step3已routerId为条件，在以下表中获取数据并备份至其历史表

        List<String> sqlList = new ArrayList<String>();

        // MT_ROUTER_B
        MtRouterHis mtRouterHis = new MtRouterHis();
        BeanUtils.copyProperties(mtRouter, mtRouterHis);
        mtRouterHis.setTenantId(tenantId);
        mtRouterHis.setEventId(dto.getEventId());
        mtRouterHis.setRouterHisId(this.customSequence.getNextKey("mt_router_his_s"));
        mtRouterHis.setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_his_cid_s")));
        mtRouterHis.setCreatedBy(userId);
        mtRouterHis.setCreationDate(now);
        mtRouterHis.setLastUpdateDate(now);
        mtRouterHis.setLastUpdatedBy(userId);
        mtRouterHis.setObjectVersionNumber(1L);
        sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterHis));

        // MT_ROUTER_SITE_ASSIGN
        MtRouterSiteAssign searchMtRouterSiteAssign = new MtRouterSiteAssign();
        searchMtRouterSiteAssign.setRouterId(dto.getRouterId());
        List<MtRouterSiteAssign> mtRouterSiteAssigns = this.mtRouterSiteAssignMapper.select(searchMtRouterSiteAssign);
        for (MtRouterSiteAssign mtRouterSiteAssign : mtRouterSiteAssigns) {
            MtRouterSiteAssignHis mtRouterSiteAssignHis = new MtRouterSiteAssignHis();
            BeanUtils.copyProperties(mtRouterSiteAssign, mtRouterSiteAssignHis);
            mtRouterSiteAssignHis.setTenantId(tenantId);
            mtRouterSiteAssignHis.setEventId(dto.getEventId());
            mtRouterSiteAssignHis
                            .setRouterSiteAssignHisId(this.customSequence.getNextKey("mt_router_site_assign_his_s"));
            mtRouterSiteAssignHis
                            .setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_site_assign_his_cid_s")));
            mtRouterSiteAssignHis.setCreatedBy(userId);
            mtRouterSiteAssignHis.setCreationDate(now);
            mtRouterSiteAssignHis.setLastUpdateDate(now);
            mtRouterSiteAssignHis.setLastUpdatedBy(userId);
            mtRouterSiteAssignHis.setObjectVersionNumber(1L);
            sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterSiteAssignHis));
        }

        // MT_ROUTER_STEP_B
        MtRouterStep searchMtRouterStep = new MtRouterStep();
        searchMtRouterStep.setRouterId(dto.getRouterId());
        List<MtRouterStep> mtRouterSteps = mtRouterStepMapper.select(searchMtRouterStep);

        for (MtRouterStep mtRouterStep : mtRouterSteps) {
            MtRouterStepHis mtRouterStepHis = new MtRouterStepHis();
            BeanUtils.copyProperties(mtRouterStep, mtRouterStepHis);
            mtRouterStepHis.setTenantId(tenantId);
            mtRouterStepHis.setEventId(dto.getEventId());
            mtRouterStepHis.setRouterStepHisId(this.customSequence.getNextKey("mt_router_step_his_s"));
            mtRouterStepHis.setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_step_his_cid_s")));
            mtRouterStepHis.setCreatedBy(userId);
            mtRouterStepHis.setCreationDate(now);
            mtRouterStepHis.setLastUpdateDate(now);
            mtRouterStepHis.setLastUpdatedBy(userId);
            mtRouterStepHis.setObjectVersionNumber(1L);
            sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterStepHis));

            // MT_ROUTER_LINK根据routerStepId获取数据
            MtRouterLink searchMtRouterLink = new MtRouterLink();
            searchMtRouterLink.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterLink> mtRouterLinks = mtRouterLinkMapper.select(searchMtRouterLink);
            for (MtRouterLink mtRouterLink : mtRouterLinks) {
                MtRouterLinkHis his = new MtRouterLinkHis();
                BeanUtils.copyProperties(mtRouterLink, his);
                his.setTenantId(tenantId);
                his.setEventId(dto.getEventId());
                his.setRouterLinkHisId(this.customSequence.getNextKey("mt_router_link_his_s"));
                his.setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_link_his_cid_s")));
                his.setCreatedBy(userId);
                his.setCreationDate(now);
                his.setLastUpdateDate(now);
                his.setLastUpdatedBy(userId);
                his.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(his));
            }

            // MT_ROUTER_NEXT_STEP根据routerStepId获取数据
            MtRouterNextStep searchMtRouterNextStep = new MtRouterNextStep();
            searchMtRouterNextStep.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterNextStep> mtRouterNextSteps = mtRouterNextStepMapper.select(searchMtRouterNextStep);
            for (MtRouterNextStep mtRouterNextStep : mtRouterNextSteps) {
                MtRouterNextStepHis mtRouterNextStepHis = new MtRouterNextStepHis();
                BeanUtils.copyProperties(mtRouterNextStep, mtRouterNextStepHis);
                mtRouterNextStepHis.setTenantId(tenantId);
                mtRouterNextStepHis.setEventId(dto.getEventId());
                mtRouterNextStepHis.setRouterNextStepHisId(this.customSequence.getNextKey("mt_router_next_step_his_s"));
                mtRouterNextStepHis
                                .setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_next_step_his_cid_s")));
                mtRouterNextStepHis.setCreatedBy(userId);
                mtRouterNextStepHis.setCreationDate(now);
                mtRouterNextStepHis.setLastUpdateDate(now);
                mtRouterNextStepHis.setLastUpdatedBy(userId);
                mtRouterNextStepHis.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterNextStepHis));
            }

            // MT_ROUTER_DONE_STEP根据routerStepId获取数据
            MtRouterDoneStep searchMtRouterDoneStep = new MtRouterDoneStep();
            searchMtRouterDoneStep.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterDoneStep> mtRouterDoneSteps = mtRouterDoneStepMapper.select(searchMtRouterDoneStep);
            for (MtRouterDoneStep mtRouterDoneStep : mtRouterDoneSteps) {
                MtRouterDoneStepHis mtRouterDoneStepHis = new MtRouterDoneStepHis();
                BeanUtils.copyProperties(mtRouterDoneStep, mtRouterDoneStepHis);
                mtRouterDoneStepHis.setTenantId(tenantId);
                mtRouterDoneStepHis.setEventId(dto.getEventId());
                mtRouterDoneStepHis.setRouterDoneStepHisId(this.customSequence.getNextKey("mt_router_done_step_his_s"));
                mtRouterDoneStepHis
                                .setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_done_step_his_cid_s")));
                mtRouterDoneStepHis.setCreatedBy(userId);
                mtRouterDoneStepHis.setCreationDate(now);
                mtRouterDoneStepHis.setLastUpdateDate(now);
                mtRouterDoneStepHis.setLastUpdatedBy(userId);
                mtRouterDoneStepHis.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterDoneStepHis));
            }

            // MT_ROUTER_RETURN_STEP根据routerStepId获取数据
            MtRouterReturnStep searchMtRouterReturnStep = new MtRouterReturnStep();
            searchMtRouterReturnStep.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterReturnStep> mtRouterReturnSteps = mtRouterReturnStepMapper.select(searchMtRouterReturnStep);
            for (MtRouterReturnStep mtRouterReturnStep : mtRouterReturnSteps) {
                MtRouterReturnStepHis mtRouterReturnStepHis = new MtRouterReturnStepHis();
                BeanUtils.copyProperties(mtRouterReturnStep, mtRouterReturnStepHis);
                mtRouterReturnStepHis.setTenantId(tenantId);
                mtRouterReturnStepHis.setEventId(dto.getEventId());
                mtRouterReturnStepHis.setRouterReturnStepHisId(
                                this.customSequence.getNextKey("mt_router_return_step_his_s"));
                mtRouterReturnStepHis.setCid(
                                Long.valueOf(this.customSequence.getNextKey("mt_router_return_step_his_cid_s")));
                mtRouterReturnStepHis.setCreatedBy(userId);
                mtRouterReturnStepHis.setCreationDate(now);
                mtRouterReturnStepHis.setLastUpdateDate(now);
                mtRouterReturnStepHis.setLastUpdatedBy(userId);
                mtRouterReturnStepHis.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterReturnStepHis));
            }

            // MT_ROUTER_STEP_GROUP根据routerStepId获取数据
            MtRouterStepGroup searchMtRouterStepGroup = new MtRouterStepGroup();
            searchMtRouterStepGroup.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterStepGroup> mtRouterStepGroups = mtRouterStepGroupMapper.select(searchMtRouterStepGroup);
            for (MtRouterStepGroup mtRouterStepGroup : mtRouterStepGroups) {
                MtRouterStepGroupHis mtRouterStepGroupHis = new MtRouterStepGroupHis();
                BeanUtils.copyProperties(mtRouterStepGroup, mtRouterStepGroupHis);
                mtRouterStepGroupHis.setTenantId(tenantId);
                mtRouterStepGroupHis.setEventId(dto.getEventId());
                mtRouterStepGroupHis
                                .setRouterStepGroupHisId(this.customSequence.getNextKey("mt_router_step_group_his_s"));
                mtRouterStepGroupHis
                                .setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_step_group_his_cid_s")));
                mtRouterStepGroupHis.setCreatedBy(userId);
                mtRouterStepGroupHis.setCreationDate(now);
                mtRouterStepGroupHis.setLastUpdateDate(now);
                mtRouterStepGroupHis.setLastUpdatedBy(userId);
                mtRouterStepGroupHis.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterStepGroupHis));

                // MT_ROUTER_STEP_GROUP_STEP根据MT_ROUTER_STEP_GROUP的ROUTER_STEP_GROUP_ID获取数据
                MtRouterStepGroupStep searchMtRouterStepGroupStep = new MtRouterStepGroupStep();
                searchMtRouterStepGroupStep.setRouterStepGroupId(mtRouterStepGroup.getRouterStepGroupId());
                List<MtRouterStepGroupStep> mtRouterStepGroupSteps =
                                mtRouterStepGroupStepMapper.select(searchMtRouterStepGroupStep);
                for (MtRouterStepGroupStep mtRouterStepGroupStep : mtRouterStepGroupSteps) {
                    MtRouterStepGroupStepHis mtRouterStepGroupStepHis = new MtRouterStepGroupStepHis();
                    BeanUtils.copyProperties(mtRouterStepGroupStep, mtRouterStepGroupStepHis);
                    mtRouterStepGroupStepHis.setTenantId(tenantId);
                    mtRouterStepGroupStepHis.setEventId(dto.getEventId());
                    mtRouterStepGroupStepHis.setRouterStepGroupStepHisId(
                                    this.customSequence.getNextKey("mt_router_step_group_step_his_s"));
                    mtRouterStepGroupStepHis.setCid(Long
                                    .valueOf(this.customSequence.getNextKey("mt_router_step_group_step_his_cid_s")));
                    mtRouterStepGroupStepHis.setCreatedBy(userId);
                    mtRouterStepGroupStepHis.setCreationDate(now);
                    mtRouterStepGroupStepHis.setLastUpdateDate(now);
                    mtRouterStepGroupStepHis.setLastUpdatedBy(userId);
                    mtRouterStepGroupStepHis.setObjectVersionNumber(1L);
                    sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterStepGroupStepHis));
                }
            }

            // MT_ROUTER_OPERATION根据routerStepId获取数据
            MtRouterOperation searchMtRouterOperation = new MtRouterOperation();
            searchMtRouterOperation.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterOperation> mtRouterOperations = mtRouterOperationMapper.select(searchMtRouterOperation);
            for (MtRouterOperation mtRouterOperation : mtRouterOperations) {
                MtRouterOperationHis his = new MtRouterOperationHis();
                BeanUtils.copyProperties(mtRouterOperation, his);
                his.setTenantId(tenantId);
                his.setEventId(dto.getEventId());
                his.setRouterOperationHisId(this.customSequence.getNextKey("mt_router_operation_his_s"));
                his.setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_operation_his_cid_s")));
                his.setCreatedBy(userId);
                his.setCreationDate(now);
                his.setLastUpdateDate(now);
                his.setLastUpdatedBy(userId);
                his.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(his));

                // MT_ROUTER_OPERATION_COMPONENT根据MT_ROUTER_OPERATION的routerOperationId获取组件数据
                MtRouterOperationComponent searchMtRouterOperationComponent = new MtRouterOperationComponent();
                searchMtRouterOperationComponent.setRouterOperationId(mtRouterOperation.getRouterOperationId());
                List<MtRouterOperationComponent> mtRouterOperationComponents =
                                mtRouterOperationComponentMapper.select(searchMtRouterOperationComponent);
                for (MtRouterOperationComponent mtRouterOperationComponent : mtRouterOperationComponents) {
                    MtRouterOperationCompHis mtRouterOperationComponentHis = new MtRouterOperationCompHis();
                    BeanUtils.copyProperties(mtRouterOperationComponent, mtRouterOperationComponentHis);
                    mtRouterOperationComponentHis.setTenantId(tenantId);
                    mtRouterOperationComponentHis.setEventId(dto.getEventId());
                    mtRouterOperationComponentHis.setRouterOperationCompHisId(
                                    this.customSequence.getNextKey("mt_router_operation_comp_his_s"));
                    mtRouterOperationComponentHis.setCid(
                                    Long.valueOf(this.customSequence.getNextKey("mt_router_operation_comp_his_cid_s")));
                    mtRouterOperationComponentHis.setCreatedBy(userId);
                    mtRouterOperationComponentHis.setCreationDate(now);
                    mtRouterOperationComponentHis.setLastUpdateDate(now);
                    mtRouterOperationComponentHis.setLastUpdatedBy(userId);
                    mtRouterOperationComponentHis.setObjectVersionNumber(1L);
                    sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterOperationComponentHis));
                }
            }

            // MT_ROUTER_SUBSTEP根据routerStepId获取数据
            MtRouterSubstep searchMtRouterSubstep = new MtRouterSubstep();
            searchMtRouterSubstep.setRouterStepId(mtRouterStep.getRouterStepId());
            List<MtRouterSubstep> mtRouterSubsteps = mtRouterSubstepMapper.select(searchMtRouterSubstep);
            for (MtRouterSubstep mtRouterSubstep : mtRouterSubsteps) {
                MtRouterSubstepHis mtRouterSubstepHis = new MtRouterSubstepHis();
                BeanUtils.copyProperties(mtRouterSubstep, mtRouterSubstepHis);
                mtRouterSubstepHis.setTenantId(tenantId);
                mtRouterSubstepHis.setEventId(dto.getEventId());
                mtRouterSubstepHis.setRouterSubstepHisId(this.customSequence.getNextKey("mt_router_substep_his_s"));
                mtRouterSubstepHis.setCid(Long.valueOf(this.customSequence.getNextKey("mt_router_substep_his_cid_s")));
                mtRouterSubstepHis.setCreatedBy(userId);
                mtRouterSubstepHis.setCreationDate(now);
                mtRouterSubstepHis.setLastUpdateDate(now);
                mtRouterSubstepHis.setLastUpdatedBy(userId);
                mtRouterSubstepHis.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterSubstepHis));

                // MT_ROUTER_SUBSTEP_COMPONENT根据MT_ROUTER_SUBSTEP的routerSubstepId
                MtRouterSubstepComponent searchMtRouterSubstepComponent = new MtRouterSubstepComponent();
                searchMtRouterSubstepComponent.setRouterSubstepId(mtRouterSubstep.getRouterSubstepId());
                List<MtRouterSubstepComponent> mtRouterSubstepComponents =
                                mtRouterSubstepComponentMapper.select(searchMtRouterSubstepComponent);
                for (MtRouterSubstepComponent mtRouterSubstepComponent : mtRouterSubstepComponents) {
                    MtRouterSubstepCompHis mtRouterSubstepComponentHis = new MtRouterSubstepCompHis();
                    BeanUtils.copyProperties(mtRouterSubstepComponent, mtRouterSubstepComponentHis);
                    mtRouterSubstepComponentHis.setTenantId(tenantId);
                    mtRouterSubstepComponentHis.setEventId(dto.getEventId());
                    mtRouterSubstepComponentHis.setRouterSubstepCompHisId(
                                    this.customSequence.getNextKey("mt_router_substep_comp_his_s"));
                    mtRouterSubstepComponentHis.setCid(
                                    Long.valueOf(this.customSequence.getNextKey("mt_router_substep_comp_his_cid_s")));
                    mtRouterSubstepComponentHis.setCreatedBy(userId);
                    mtRouterSubstepComponentHis.setCreationDate(now);
                    mtRouterSubstepComponentHis.setLastUpdateDate(now);
                    mtRouterSubstepComponentHis.setLastUpdatedBy(userId);
                    mtRouterSubstepComponentHis.setObjectVersionNumber(1L);
                    sqlList.addAll(MtSqlHelper.getInsertSql(mtRouterSubstepComponentHis));
                }
            }
        }
        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }
}
