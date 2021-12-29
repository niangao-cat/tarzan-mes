package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.itf.utils.Utils;
import io.choerodon.core.oauth.CustomUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.iface.domain.entity.MtCostcenter;
import tarzan.iface.domain.entity.MtCostcenterIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtCostcenterIfaceRepository;
import tarzan.iface.domain.repository.MtCostcenterRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtCostcenterIfaceMapper;

/**
 * 成本中心数据接口 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtCostcenterIfaceRepositoryImpl extends BaseRepositoryImpl<MtCostcenterIface>
        implements MtCostcenterIfaceRepository {


    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCostcenterRepository mtCostcenterRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtCostcenterIfaceMapper mtCostcenterIfaceMapper;

    @Override
    public void costCenterInterfaceImport(Long tenantId, Long batchId) {
        Long start = System.currentTimeMillis();
        // change data status
        List<MtCostcenterIface> mtCostCenterIfaceList = self().updateIfaceStatus(tenantId, "P", batchId);
        if (CollectionUtils.isEmpty(mtCostCenterIfaceList)) {
            return;
        }

        try {
            List<AuditDomain> ifaceList = self().saveCostCenterIface(tenantId, mtCostCenterIfaceList);
            self().log(tenantId, ifaceList);
        } catch (Exception e) {
            List<AuditDomain> ifaceList = new ArrayList<>(mtCostCenterIfaceList.size());

            String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
            if (errorMsg.length() > 1000) {
                errorMsg = errorMsg.substring(0, 999);
            }
            final String msg = errorMsg;

            mtCostCenterIfaceList.forEach(ifs -> {
                ifs.setStatus("E");
                ifs.setMessage(msg);
                ifs.setTenantId(tenantId);
                ifaceList.add(ifs);
            });

            self().log(tenantId, ifaceList);
        }
        System.out.println("成本中心数据接口总耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveCostCenterIface(Long tenantId, List<MtCostcenterIface> mtCostCenterIfaceList) {

        List<AuditDomain> ifaceList = new ArrayList<>(mtCostCenterIfaceList.size());

        // 获取工厂站点关系
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, mtCostCenterIfaceList);

        // 获取已有数据
        List<MtCostcenter> mtCostCenterList = mtCostcenterRepository.queryCustomerByCode(tenantId, mtCostCenterIfaceList
                .stream().map(MtCostcenterIface::getCostcenterCode).collect(Collectors.toList()));

        Map<CostcenterTuple, MtCostcenter> mtCostcenterMap = mtCostCenterList.stream().collect(
                Collectors.toMap(c -> new CostcenterTuple(c.getCostcenterCode(), c.getSiteId()), c -> c));


        // 公有变量
        Long userId = Long.valueOf(-1L);
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }
        Date now = Utils.getNowDate();

        // 批量获取Cid、Id
        List<String> pfepPurchaseSupplierIds = this.customDbRepository.getNextKeys("mt_costcenter_s", mtCostCenterIfaceList.size());
        List<String> pfepPurchaseSupplierCids = this.customDbRepository.getNextKeys("mt_costcenter_cid_s", mtCostCenterIfaceList.size());
        CostcenterTuple costcenterTuple;
        MtCostcenter originCostCenter;
        MtCostcenter mtCostcenter;
        List<AuditDomain> mtCostcenterList = new ArrayList<>(mtCostCenterIfaceList.size());
        for (MtCostcenterIface iface : mtCostCenterIfaceList) {
            if (sitePlantMap == null || sitePlantMap.get(iface.getPlantCode()) == null) {
                ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                "INTERFACE", iface.getIfaceId(), "【API：costCenterInterfaceImport】"),
                        now, userId));
                continue;
            }
            mtCostcenter = new MtCostcenter();
            costcenterTuple = new CostcenterTuple(iface.getCostcenterCode(),
                    sitePlantMap.get(iface.getPlantCode()).getSiteId());
            if (MapUtils.isNotEmpty(mtCostcenterMap) && mtCostcenterMap.get(costcenterTuple) != null) {
                originCostCenter = mtCostcenterMap.get(costcenterTuple);
            } else {
                originCostCenter = null;
            }
            mtCostcenter.setCostcenterCode(iface.getCostcenterCode());
            // add 刘克金，王康 start
            mtCostcenter.setCostcenterType(iface.getCostcenterType());
            // end

            mtCostcenter.setDescription(iface.getDescription());
            mtCostcenter.setDateFromTo(iface.getDateFromTo());
            mtCostcenter.setDateEndTo(iface.getDateEndTo());
            mtCostcenter.setSourceIdentificationId(iface.getDispositionId());
            mtCostcenter.setSiteId(sitePlantMap.get(iface.getPlantCode()).getSiteId());
            mtCostcenter.setCid(Long.valueOf(pfepPurchaseSupplierCids.remove(0)));
            mtCostcenter.setTenantId(tenantId);
            mtCostcenter.setLastUpdateDate(now);
            mtCostcenter.setLastUpdatedBy(userId);
            if (originCostCenter != null) {
                mtCostcenter.setCostcenterId(originCostCenter.getCostcenterId());
                mtCostcenter.setCreatedBy(originCostCenter.getCreatedBy());
                mtCostcenter.setCreationDate(originCostCenter.getCreationDate());
                mtCostcenter.setObjectVersionNumber(originCostCenter.getObjectVersionNumber() + 1L);

            } else {
                mtCostcenter.setCostcenterId(pfepPurchaseSupplierIds.remove(0));
                mtCostcenter.setCreatedBy(userId);
                mtCostcenter.setCreationDate(now);
                mtCostcenter.setObjectVersionNumber(1L);
            }
            mtCostcenterList.add(mtCostcenter);
            ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
        }
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtCostcenterList)) {
            sqlList.addAll(constructSql(mtCostcenterList));
            mtCostcenterList.clear();
        }
        /**
         * 保存数据
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        return ifaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtCostcenterIface> updateIfaceStatus(Long tenantId, String status, Long batchId) {
        List<MtCostcenterIface> mtCostCenterIfaceList = mtCostcenterIfaceMapper.getUnprocessedList(tenantId, batchId);
        if (CollectionUtils.isEmpty(mtCostCenterIfaceList)) {
            return new ArrayList<>();
        }
        List<String> sqlList = new ArrayList<>(mtCostCenterIfaceList.size());
        List<AuditDomain> auditDomains = new ArrayList<>(mtCostCenterIfaceList.size());
        mtCostCenterIfaceList.stream().forEach(ifs -> {
            ifs.setStatus(status);
            ifs.setTenantId(tenantId);
        });
        auditDomains.addAll(mtCostCenterIfaceList);
        sqlList.addAll(constructSql(auditDomains));
        List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
        for (List<String> commitSql : commitSqlList) {
            this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
        }

        return mtCostCenterIfaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }

        }
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId              租户Id
     * @param mtCostCenterIfaceList List<MtCostCenterIface>
     * @return Map<String, MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId,
                                                                   List<MtCostcenterIface> mtCostCenterIfaceList) {
        List<String> plantCodeList = mtCostCenterIfaceList.stream().map(MtCostcenterIface::getPlantCode).distinct()
                .collect(Collectors.toList());
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        List<MtSitePlantReleation> relationByPlantAndSiteType =
                mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);
        Map<String, MtSitePlantReleation> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
            sitePlantMap = relationByPlantAndSiteType.stream()
                    .collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, t -> t));
        }
        return sitePlantMap;
    }

    /**
     * 构建返回消息
     *
     * @param mtCostCenterIface MtCostcenterIface
     * @param message           错误消息
     * @return MtCostCenterIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtCostcenterIface constructIfaceMessage(Long tenantId, MtCostcenterIface mtCostCenterIface, String status,
                                                    String message, Date date, Long userId) {
        mtCostCenterIface.setTenantId(tenantId);
        mtCostCenterIface.setStatus(status);
        mtCostCenterIface.setMessage(message);
        mtCostCenterIface.setLastUpdateDate(date);
        mtCostCenterIface.setLastUpdatedBy(userId);

        return mtCostCenterIface;
    }

    /**
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
     *
     * @author benjamin
     * @date 2019-06-25 18:40
     * @param sqlList Sql数据集合
     * @return List<List>
     */
    /**
     * 生成拆分的sql
     *
     * @param ifaceSqlList
     * @return
     */
    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    private List<List<String>> commitSqlList(List<String> sqlList, int splitNum) {

        List<List<String>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private static class CostcenterTuple implements Serializable {
        private static final long serialVersionUID = -5931546099494490530L;
        private String costcenterCode;
        private String siteId;

        public CostcenterTuple(String costcenterCode, String siteId) {
            this.costcenterCode = costcenterCode;
            this.siteId = siteId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CostcenterTuple that = (CostcenterTuple) o;
            return Objects.equals(costcenterCode, that.costcenterCode) && Objects.equals(siteId, that.siteId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(costcenterCode, siteId);
        }
    }
}
