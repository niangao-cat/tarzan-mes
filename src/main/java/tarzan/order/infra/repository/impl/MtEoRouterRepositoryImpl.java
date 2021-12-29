package tarzan.order.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.vo.MtEoRouterActualVO20;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtRouterStepVO17;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtEoRouterHis;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterHisRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoMapper;
import tarzan.order.infra.mapper.MtEoRouterMapper;

/**
 * EO工艺路线 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoRouterRepositoryImpl extends BaseRepositoryImpl<MtEoRouter> implements MtEoRouterRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRouterMapper mtEoRouterMapper;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtEoRouterHisRepository mtEoRouterHisRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String eoRouterGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterGet】"));
        }

        MtEoRouter mtEoRouter = new MtEoRouter();
        mtEoRouter.setTenantId(tenantId);
        mtEoRouter.setEoId(eoId);
        mtEoRouter = this.mtEoRouterMapper.selectOne(mtEoRouter);
        if (mtEoRouter == null) {
            return "";
        }

        return mtEoRouter.getRouterId();
    }

    @Override
    public void eoRouterVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterVerify】"));
        }

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoId(eoId);
        mtEo = this.mtEoMapper.selectOne(mtEo);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoRouterVerify】"));
        }

        String routerId = eoRouterGet(tenantId, eoId);
        if (StringUtils.isNotEmpty(routerId)) {
            mtRouterRepository.routerAvailabilityValidate(tenantId, routerId);

            MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
            mtRouterSiteAssign.setRouterId(routerId);
            mtRouterSiteAssign.setEnableFlag("Y");
            List<MtRouterSiteAssign> mtRouterSiteAssigns = mtRouterSiteAssignRepository
                    .propertyLimitRouterSiteAssignQuery(tenantId, mtRouterSiteAssign);
            List<String> siteIds = mtRouterSiteAssigns.stream().map(MtRouterSiteAssign::getSiteId)
                    .collect(Collectors.toList());
            if (!siteIds.contains(mtEo.getSiteId())) {
                throw new MtException("MT_ORDER_0152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0152", "ORDER", "eoId", "eoId", "ROUTER", "【API:eoRouterVerify】"));
            }
        }
    }

    @Override
    public void eoRouterBomMatchValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterBomMatchValidate】"));
        }

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoId(eoId);
        mtEo = this.mtEoMapper.selectOne(mtEo);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoRouterBomMatchValidate】"));
        }

        String bomId = this.mtEoBomRepository.eoBomGet(tenantId, eoId);
        String routerId = eoRouterGet(tenantId, eoId);

        if (StringUtils.isNotEmpty(routerId)) {
            MtRouter mtRouter = this.mtRouterRepository.routerGet(tenantId, routerId);

            if (mtRouter != null && StringUtils.isNotEmpty(mtRouter.getBomId()) && !mtRouter.getBomId().equals(bomId)) {
                throw new MtException("MT_ORDER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0029", "ORDER", "【API:eoRouterBomMatchValidate】"));

            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterVO2 eoRouterUpdate(Long tenantId, MtEoRouterVO vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getRouterId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "routerId", "【API:eoRouterUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoRouterUpdate】"));
        }

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoId(vo.getEoId());
        mtEo = this.mtEoMapper.selectOne(mtEo);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoRouterUpdate】"));
        }

        MtEoRouter mtEoRouter = new MtEoRouter();
        mtEoRouter.setTenantId(tenantId);
        mtEoRouter.setEoId(vo.getEoId());
        mtEoRouter = this.mtEoRouterMapper.selectOne(mtEoRouter);
        if (mtEoRouter == null) {
            mtEoRouter = new MtEoRouter();
            mtEoRouter.setTenantId(tenantId);
            mtEoRouter.setEoId(vo.getEoId());
            mtEoRouter.setRouterId(vo.getRouterId());
            self().insertSelective(mtEoRouter);
        } else {
            mtEoRouter.setTenantId(tenantId);
            mtEoRouter.setRouterId(vo.getRouterId());
            self().updateByPrimaryKeySelective(mtEoRouter);
        }

        MtEoRouterHis mtEoRouterHis = new MtEoRouterHis();
        mtEoRouterHis.setTenantId(tenantId);
        mtEoRouterHis.setEoRouterId(mtEoRouter.getEoRouterId());
        mtEoRouterHis.setEoId(mtEoRouter.getEoId());
        mtEoRouterHis.setRouterId(mtEoRouter.getRouterId());
        mtEoRouterHis.setEventId(vo.getEventId());
        this.mtEoRouterHisRepository.insertSelective(mtEoRouterHis);

        // update by chuang.yang 2019.2.19
        // 使用 eoUpdate，里面包含历史和对象的记录
        mtEo.setValidateFlag("N");
        MtEoVO mtEoVO = new MtEoVO();
        BeanUtils.copyProperties(mtEo, mtEoVO);
        mtEoVO.setEventId(vo.getEventId());
        this.mtEoRepository.eoUpdate(tenantId, mtEoVO, "N");

        MtEoRouterVO2 result = new MtEoRouterVO2();
        result.setEoRouterId(mtEoRouter.getEoRouterId());
        result.setEoRouterHisId(mtEoRouterHis.getEoRouterHisId());
        return result;
    }

    @Override
    public List<String> eoEntryStepGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoEntryStepGet】"));
        }

        // 1. 获取EoRouter关系
        MtEoRouter mtEoRouter = new MtEoRouter();
        mtEoRouter.setTenantId(tenantId);
        mtEoRouter.setEoId(eoId);
        mtEoRouter = this.mtEoRouterMapper.selectOne(mtEoRouter);
        if (mtEoRouter == null) {
            return null;
        }
        // 2. 根据eoRouter获取的routerId获取entryStepId返回
        return mtRouterStepRepository.routerEntryRouterStepGet(tenantId, mtEoRouter.getRouterId());
    }

    @Override
    public List<MtEoRouterVO4> eoEntryStepBatchGet(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "eoId", "ORDER",
                    "【API:eoEntryStepBatchGet】"));
        }

        List<MtEoRouterVO4> resultList = new ArrayList<>();

        List<MtEoRouter> eoRouterList = this.eoRouterBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(eoRouterList)) {
            return resultList;
        }

        Map<String, String> eoRouterMap =
                eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));

        List<MtRouterStepVO17> routerStepList = mtRouterStepRepository.routerEntryRouterStepBatchGet(tenantId,
                Lists.newArrayList(eoRouterMap.values().iterator()));
        if (CollectionUtils.isEmpty(routerStepList)) {
            return resultList;
        }

        Map<String, String> routerStepMap = routerStepList.stream()
                .collect(Collectors.toMap(MtRouterStepVO17::getRouterId, MtRouterStepVO17::getRouterStepId));
        eoRouterList.forEach(t -> {
            MtEoRouterVO4 result = new MtEoRouterVO4();
            result.setEoId(t.getEoId());
            result.setRouterStepId(routerStepMap.get(eoRouterMap.get(t.getEoId())));
            resultList.add(result);
        });

        return resultList;
    }

    @Override
    public void eoRouterUpdateVerify(Long tenantId, MtEoRouterVO1 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "routerId", "【API:eoRouterUpdateVerify】"));
        }
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoRouterUpdateVerify】"));
        }
        String status = mtEo.getStatus();

        // 验证routerId是否为可用的工艺路线
        try {
            mtRouterRepository.routerAvailabilityValidate(tenantId, dto.getRouterId());
        } catch (MtException me) {
            throw new MtException("MT_ORDER_0135", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0135", "ORDER", "【API:eoRouterUpdateVerify】"));
        }


        // 获取工艺路线类型routerType
        if (!"EO".equals(this.mtRouterRepository.routerGet(tenantId, dto.getRouterId()).getRouterType())) {
            throw new MtException("MT_ORDER_0136", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0136", "ORDER", "【EO】", "【API:eoRouterUpdateVerify】"));
        }

        // Step 2
        if (!"NEW".equals(status) && !"HOLD".equals(status)) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0044", "ORDER", "[‘NEW’，‘HOLD’]", "【API:eoRouterUpdateVerify】"));
        }
        // Step 3验证执行作业是否当前工艺路线已产生实绩
        String routerId = eoRouterGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(routerId)) {
            return;
        }
        // 3-b
        MtEoRouterActualVO20 mtEoRouterActualVO20 = new MtEoRouterActualVO20();

        mtEoRouterActualVO20.setEoId(dto.getEoId());
        mtEoRouterActualVO20.setRouterId(routerId);
        List<String> stringList =
                mtEoRouterActualRepository.propertyLimitEoRouterActualQuery(tenantId, mtEoRouterActualVO20);
        if (CollectionUtils.isNotEmpty(stringList)) {
            throw new MtException("MT_ORDER_0139", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0139", "ORDER", "【API:eoRouterUpdateVerify】"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoRouterVO2> eoRouterBatchUpdate(Long tenantId, MtEoRouterVO3 dto) {
        // 1.参数校验
        if (dto == null || CollectionUtils.isEmpty(dto.getEoMessageList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoMessageList", "【API:eoRouterBatchUpdate】"));
        }
        if (dto.getEoMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRouterBatchUpdate】"));
        }
        if (dto.getEoMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterId()))) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "routerId", "【API:eoRouterBatchUpdate】"));
        }
        // 2.校验eoId是否存在
        List<String> eoId = dto.getEoMessageList().stream().map(MtEoRouterVO1::getEoId).distinct()
                .collect(Collectors.toList());
        List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoId);
        if (eoId.size() > mtEos.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoRouterBatchUpdate】"));
        }
        // 共有变量
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        // 获取id、Cid
        List<String> eoRouterId = this.customDbRepository.getNextKeys("mt_eo_router_s", dto.getEoMessageList().size());
        List<String> eoRouterCid = this.customDbRepository.getNextKeys("mt_eo_router_cid_s", dto.getEoMessageList().size());
        List<String> eoRouterHisId =
                this.customDbRepository.getNextKeys("mt_eo_router_his_s", dto.getEoMessageList().size());
        List<String> eoRouterHisCid =
                this.customDbRepository.getNextKeys("mt_eo_router_his_cid_s", dto.getEoMessageList().size());
        // 获取eoRouter数据
        List<MtEoRouter> mtEoRouters = mtEoRouterMapper.selectEoRouterByEoId(tenantId, eoId);
        Map<String, MtEoRouter> mtEoRouterMap = null;
        if (CollectionUtils.isNotEmpty(mtEoRouters)) {
            mtEoRouterMap = mtEoRouters.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));
        }
        Map<String, MtEoRouter> finalMtEoRouterMap = mtEoRouterMap;
        List<MtEoRouterVO2> mtEoRouterVO2s = new ArrayList<>(dto.getEoMessageList().size());
        List<AuditDomain> sqlList = new ArrayList<>(dto.getEoMessageList().size());
        List<AuditDomain> hisSqlList = new ArrayList<>(dto.getEoMessageList().size());
        dto.getEoMessageList().stream().forEach(c -> {
            MtEoRouterVO2 mtEoRouterVO2 = new MtEoRouterVO2();
            MtEoRouter mtEoRouter;
            if (MapUtils.isEmpty(finalMtEoRouterMap) || finalMtEoRouterMap.get(c.getEoId()) == null) {
                // 新增数据
                mtEoRouter = new MtEoRouter();
                mtEoRouter.setEoRouterId(eoRouterId.get(mtEoRouterVO2s.size()));
                mtEoRouter.setEoId(c.getEoId());
                mtEoRouter.setRouterId(c.getRouterId());
                mtEoRouter.setObjectVersionNumber(1L);
                mtEoRouter.setCreatedBy(userId);
                mtEoRouter.setCreationDate(now);
            } else {
                // 更新数据
                mtEoRouter = finalMtEoRouterMap.get(c.getEoId());
                mtEoRouter.setRouterId(c.getRouterId());
            }
            mtEoRouter.setTenantId(tenantId);
            mtEoRouter.setCid(Long.valueOf(eoRouterCid.get(mtEoRouterVO2s.size())));
            mtEoRouter.setLastUpdatedBy(userId);
            mtEoRouter.setLastUpdateDate(now);
            sqlList.add(mtEoRouter);

            // 记录历史
            MtEoRouterHis mtEoRouterHis = new MtEoRouterHis();
            mtEoRouterHis.setTenantId(tenantId);
            mtEoRouterHis.setEoRouterHisId(eoRouterHisId.get(mtEoRouterVO2s.size()));
            mtEoRouterHis.setEoId(c.getEoId());
            mtEoRouterHis.setRouterId(c.getRouterId());
            mtEoRouterHis.setEoRouterId(mtEoRouter.getEoRouterId());
            mtEoRouterHis.setEventId(dto.getEventId());
            mtEoRouterHis.setCid(Long.valueOf(eoRouterHisCid.get(mtEoRouterVO2s.size())));
            mtEoRouterHis.setObjectVersionNumber(1L);
            mtEoRouterHis.setCreatedBy(userId);
            mtEoRouterHis.setCreationDate(now);
            mtEoRouterHis.setLastUpdatedBy(userId);
            mtEoRouterHis.setLastUpdateDate(now);
            hisSqlList.add(mtEoRouterHis);

            mtEoRouterVO2.setEoRouterId(mtEoRouterHis.getEoRouterId());
            mtEoRouterVO2.setEoRouterHisId(mtEoRouterHis.getEoRouterHisId());
            mtEoRouterVO2s.add(mtEoRouterVO2);
        });
        List<String> replaceSql = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sqlList)) {
            replaceSql.addAll(customDbRepository.getReplaceSql(sqlList));
        }
        if (CollectionUtils.isNotEmpty(hisSqlList)) {
            replaceSql.addAll(customDbRepository.getReplaceSql(hisSqlList));
        }

        // 执行SQL
        if (CollectionUtils.isNotEmpty(replaceSql)) {
            this.jdbcTemplate.batchUpdate(replaceSql.toArray(new String[replaceSql.size()]));
        }
        return mtEoRouterVO2s;
    }

    @Override
    public List<MtEoRouter> eoRouterBatchGet(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            return Collections.emptyList();
        }
        return mtEoRouterMapper.selectEoRouterByEoId(tenantId, eoIds);
    }
}
