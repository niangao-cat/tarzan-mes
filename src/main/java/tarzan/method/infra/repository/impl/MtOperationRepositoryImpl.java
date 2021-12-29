package tarzan.method.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.vo.MtOperationVO;
import tarzan.method.domain.vo.MtOperationVO1;
import tarzan.method.domain.vo.MtOperationVO2;
import tarzan.method.domain.vo.MtOperationVO3;
import tarzan.method.infra.mapper.MtOperationMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * 工序 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@Component
public class MtOperationRepositoryImpl extends BaseRepositoryImpl<MtOperation> implements MtOperationRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtOperationMapper mtOperationMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public MtOperation operationGet(Long tenantId, String operationId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationId", "【API:operationGet】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setOperationId(operationId);
        mtOperation.setTenantId(tenantId);
        return mtOperationMapper.selectOne(mtOperation);
    }

    @Override
    public String operationCurrentVersionGet(Long tenantId, String operationName, String siteId) {
        if (StringUtils.isEmpty(operationName)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationName", "【API:operationCurrentVersionGet】"));
        }
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "siteId", "【API:operationCurrentVersionGet】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setTenantId(tenantId);
        mtOperation.setSiteId(siteId);
        mtOperation.setOperationName(operationName);
        mtOperation.setCurrentFlag("Y");
        mtOperation = mtOperationMapper.selectOne(mtOperation);
        if (mtOperation == null) {
            return "";
        }
        return mtOperation.getOperationId();
    }

    @Override
    public List<String> operationAllVersionQuery(Long tenantId, String operationName, String siteId) {
        if (StringUtils.isEmpty(operationName)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationName", "【API:operationAllVersionQuery】"));
        }
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "siteId", "【API:operationAllVersionQuery】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setTenantId(tenantId);
        mtOperation.setOperationName(operationName);
        mtOperation.setSiteId(siteId);
        List<MtOperation> mtOperations = mtOperationMapper.select(mtOperation);
        List<String> result = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(mtOperations)) {
            mtOperations.forEach(t -> result.add(t.getOperationId()));
        }
        return result;
    }

    @Override
    public List<String> operationTypeQuery(Long tenantId, String operationType, String siteId) {
        if (StringUtils.isEmpty(operationType)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationType", "【API:operationTypeQuery】"));
        }
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "siteId", "【API:operationTypeQuery】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setTenantId(tenantId);
        mtOperation.setOperationType(operationType);
        mtOperation.setSiteId(siteId);

        List<MtOperation> mtOperations = mtOperationMapper.select(mtOperation);
        List<String> result = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(mtOperations)) {
            mtOperations.forEach(t -> result.add(t.getOperationId()));
        }
        return result;
    }

    @Override
    public String operationSpecialRouterGet(Long tenantId, String operationId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationId", "【API:operationSpecialRouterGet】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setTenantId(tenantId);
        mtOperation.setOperationId(operationId);
        mtOperation = mtOperationMapper.selectOne(mtOperation);
        if (mtOperation == null) {
            return null;
        }

        return mtOperation.getSpecialRouterId();
    }

    @Override
    public String operationAvailabilityValidate(Long tenantId, String operationId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationId", "【API:operationAvailabilityValidate】"));
        }

        return 0 < mtOperationMapper.selectOperAvailability(tenantId, operationId) ? "Y" : "N";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String operationCurrentVersionUpdate(Long tenantId, String operationId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationId", "【API:opeationCurrentVersionUpdate】"));
        }

        MtOperation mtOperation = new MtOperation();
        mtOperation.setTenantId(tenantId);
        mtOperation.setOperationId(operationId);
        mtOperation = mtOperationMapper.selectOne(mtOperation);
        if (mtOperation == null) {
            throw new MtException("MT_ROUTER_0006", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0006", "ROUTER", "【API:opeationCurrentVersionUpdate】"));
        }

        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date currentDate = new Date(System.currentTimeMillis());

        MtOperation tmpMtOperation = new MtOperation();
        tmpMtOperation.setTenantId(tenantId);
        tmpMtOperation.setOperationId(operationId);
        tmpMtOperation.setCurrentFlag("Y");
        tmpMtOperation.setCid(Long.valueOf(customDbRepository.getNextKey("mt_operation_cid_s")));
        tmpMtOperation.setLastUpdateDate(currentDate);
        tmpMtOperation.setLastUpdatedBy(userId);
        sqlList.addAll(customDbRepository.getUpdateSql(tmpMtOperation));

        List<String> operationIds = self().operationAllVersionQuery(tenantId, mtOperation.getOperationName(),
                mtOperation.getSiteId());
        operationIds.remove(operationId);
        for (String c : operationIds) {
            MtOperation updateOperation = new MtOperation();
            updateOperation.setTenantId(tenantId);
            updateOperation.setOperationId(c);
            updateOperation.setCurrentFlag("N");
            updateOperation.setCid(Long.valueOf(customDbRepository.getNextKey("mt_operation_cid_s")));
            updateOperation.setLastUpdateDate(currentDate);
            updateOperation.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getUpdateSql(updateOperation));
        }


        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return mtOperation.getOperationId();
    }

    @Override
    public List<MtOperationVO1> propertyLimitOperationPropertyQuery(Long tenantId, MtOperationVO dto) {
        List<MtOperationVO1> voList = mtOperationMapper.selectCondition(tenantId, dto);

        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 根据第一步获取到的站点 siteId 列表，调用API{siteBasicPropertyBatchGet }获取站点编码和站点名称
        List<String> siteIds = voList.stream().map(MtOperationVO1::getSiteId).filter(StringUtils::isNotEmpty).distinct()
                .collect(Collectors.toList());

        Map<String, MtModSite> siteMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(siteIds)) {
            List<MtModSite> sites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
            // 站点编码,站点描述
            if (CollectionUtils.isNotEmpty(sites)) {
                siteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }
        }

        // 根据第一步获取到的工作单元 workcellId 列表，调用API{workcellBasicPropertyBatchGet }获取工作单元编码和工作单元名称
        List<String> workCellIds = voList.stream().map(MtOperationVO1::getWorkcellId).filter(StringUtils::isNotEmpty)
                .distinct().collect(Collectors.toList());

        Map<String, MtModWorkcell> workcellMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(workCellIds)) {
            List<MtModWorkcell> mtModWorkcells =
                    mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }
        }

        MtModSite site;
        MtModWorkcell workcell;
        for (MtOperationVO1 vo1 : voList) {
            site = siteMap.get(vo1.getSiteId());
            vo1.setSiteCode(null != site ? site.getSiteCode() : null);
            vo1.setSiteName(null != site ? site.getSiteName() : null);

            workcell = workcellMap.get(vo1.getWorkcellId());
            vo1.setWorkcellCode(null != workcell ? workcell.getWorkcellCode() : null);
            vo1.setWorkcellName(null != workcell ? workcell.getWorkcellName() : null);
        }
        voList.sort(Comparator.comparingDouble((MtOperationVO1 t) -> Double
                .valueOf(StringUtils.isEmpty(t.getOperationId()) ? "0" : t.getOperationId())));
        return voList;
    }

    @Override
    public List<MtOperation> operationBatchGet(Long tenantId, List<String> operationIds) {
        if (CollectionUtils.isEmpty(operationIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationId", "【API:operationBatchGet】"));
        }
        return mtOperationMapper.selectByIdsCustom(tenantId, operationIds);
    }

    @Override
    public List<MtOperation> operationCurrentVersionQuery(Long tenantId, List<String> operationName, String siteId) {
        if (CollectionUtils.isEmpty(operationName)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "operationName", "【API:operationCurrentVersionQuery】"));
        }
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "siteId", "【API:operationCurrentVersionQuery】"));
        }

        return mtOperationMapper.selectByNameCustom(tenantId, siteId, operationName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo) {
        if (StringUtils.isEmpty(vo.getKeyId())) {
            throw new MtException("MT_OPERATION_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_OPERATION_0001", "OPERATION", "keyId", "【API:operationAttrPropertyUpdate】"));
        }

        MtOperation operation = new MtOperation();
        operation.setTenantId(tenantId);
        operation.setOperationId(vo.getKeyId());
        operation = mtOperationMapper.selectOne(operation);
        if (null == operation) {
            throw new MtException("MT_OPERATION_0071",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_OPERATION_0071", "OPERATION",
                            "keyId:" + vo.getKeyId(), "mt_operation",
                            "【API:operationAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_operation_attr", vo.getKeyId(), vo.getEventId(),
                vo.getAttrs());
    }

    @Override
    public List<String> propertyLimitOperationQuery(Long tenantId, MtOperationVO2 vo) {
        List<MtOperation> list = mtOperationMapper.propertyLimitOperationQuery(tenantId, vo);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtOperation::getOperationId).collect(Collectors.toList());
    }

    @Override
    public List<String> propertyLimitOperationBatchQuery(Long tenantId, MtOperationVO3 vo) {
        List<MtOperation> list = mtOperationMapper.propertyLimitOperationBatchQuery(tenantId, vo);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtOperation::getOperationId).collect(Collectors.toList());
    }

    @Override
    public List<MtOperation> selectByOperationName(Long tenantId, List<String> operationName) {
        if (CollectionUtils.isEmpty(operationName)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("b.OPERATION_NAME", operationName, 1000);
        return mtOperationMapper.selectByOperationName(tenantId, whereInValuesSql);
    }
}
