package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.entity.MtStandardOperationIface;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.repository.MtStandardOperationIfaceRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtStandardOperationIfaceMapper;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

/**
 * 标准工序接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtStandardOperationIfaceRepositoryImpl extends BaseRepositoryImpl<MtStandardOperationIface>
        implements MtStandardOperationIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtStandardOperationIfaceMapper mtStandardOperationIfaceMapper;

    @Override
    public void standardOperationInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        // get data list
        List<MtStandardOperationIface> standardOperationIfaceList = self().updateIfaceStatus(tenantId, "P");
        Map<Double, List<MtStandardOperationIface>> ifacePerBatch = standardOperationIfaceList.stream().collect(
                Collectors.groupingBy(MtStandardOperationIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtStandardOperationIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveStandardOperationIface(tenantId, entry.getValue());

                self().log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());

                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;

                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                    ifaceList.add(ifs);
                });

                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("标准工序接口导入" + standardOperationIfaceList.size() + "条数据，总耗时："
                + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveStandardOperationIface(Long tenantId,
                                                        List<MtStandardOperationIface> mtStandardOperationIfaceList) {
        List<String> sqlList = new ArrayList<>();

        List<AuditDomain> ifaceList = new ArrayList<>(mtStandardOperationIfaceList.size());

        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        // get site plant relation
        Map<String, MtSitePlantReleation> plantSiteMap = getSitePlantRelation(tenantId, mtStandardOperationIfaceList);

        // 根据operationCode获取数据
        List<String> operationCodes = mtStandardOperationIfaceList.stream()
                .map(MtStandardOperationIface::getOperationCode).distinct().collect(Collectors.toList());
        List<MtOperation> mtOperations = mtOperationRepository.selectByOperationName(tenantId, operationCodes);
        Map<OperationTuple, MtOperation> mtOperationMap = null;
        if (CollectionUtils.isNotEmpty(mtOperations)) {
            mtOperationMap = mtOperations.stream().collect(Collectors.toMap(
                    c -> new OperationTuple(c.getOperationName(), c.getRevision(), c.getSiteId()), c -> c));
        }
        OperationTuple operationTuple;
        MtOperation mtOperation;
        List<MtOperation> updateList = new ArrayList<>(mtStandardOperationIfaceList.size());
        for (MtStandardOperationIface iface : mtStandardOperationIfaceList) {
            if (plantSiteMap.get(iface.getPlantCode()) == null) {
                ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0003",
                                "INTERFACE", iface.getIfaceId(),
                                "【API：standardOperationInterfaceImport】"),
                        now, userId));
                continue;
            }
            operationTuple = new OperationTuple(iface.getOperationCode(), "MAIN",
                    plantSiteMap.get(iface.getPlantCode()).getSiteId());
            if (MapUtils.isNotEmpty(mtOperationMap) && mtOperationMap.get(operationTuple) != null) {
                mtOperation = mtOperationMap.get(operationTuple);
            } else {
                mtOperation = new MtOperation();
            }
            mtOperation.setRevision("MAIN");
            mtOperation.setSiteId(plantSiteMap.get(iface.getPlantCode()).getSiteId());
            updateList.add(constructOperation(tenantId, iface, mtOperation, now, userId));
            ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
        }

        /**
         * 处理数据
         */
        if (CollectionUtils.isNotEmpty(updateList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_operation_cid_s", updateList.size());
            List<MtOperation> insertList = updateList.stream().filter(t -> StringUtils.isEmpty(t.getOperationId()))
                    .collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_operation_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(updateList.size());
            updateList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getOperationId())) {
                    c.setOperationId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(updateList);
            sqlList.addAll(constructSql(tempList));
            updateList = null;
            tempList = null;
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
    public List<MtStandardOperationIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtStandardOperationIface> list = mtStandardOperationIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId                     租户Id
     * @param mtStandardOperationIfaceList List<MtStandardOperationIface>
     * @return Map<String, MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId,
                                                                   List<MtStandardOperationIface> mtStandardOperationIfaceList) {
        List<String> plantCodeList = mtStandardOperationIfaceList.stream().map(MtStandardOperationIface::getPlantCode)
                .distinct().collect(Collectors.toList());
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
     * 构建Operation对象
     *
     * @param tenantId 租户Id
     * @param iface    MtStandardOperationIface
     * @return MtOperation
     * @author benjamin
     * @date 2019-07-10 21:47
     */
    private MtOperation constructOperation(Long tenantId, MtStandardOperationIface iface, MtOperation mtOperation,
                                           Date date, Long userId) {

        if (StringUtils.isNotEmpty(mtOperation.getOperationId())) {
            mtOperation.setObjectVersionNumber(mtOperation.getObjectVersionNumber() + 1L);
        } else {
            mtOperation.setDateFrom(new Date(System.currentTimeMillis()));
            mtOperation.setObjectVersionNumber(1L);
            mtOperation.setCreationDate(date);
            mtOperation.setCreatedBy(userId);
        }
        Map<String, String> customerTlMap = new HashMap<>(2);
        customerTlMap.put("description", iface.getOperationDescription());
        mtOperation.setCurrentFlag("Y");
        mtOperation.setOperationName(iface.getOperationCode());
        mtOperation.setDescription(iface.getOperationDescription());
        mtOperation.setOperationStatus(Strings.isEmpty(mtOperation.getOperationId()) ? "CAN_RELEASE" : mtOperation.getOperationStatus());
        mtOperation.setOperationType(Strings.isEmpty(mtOperation.getOperationId()) ? "NORMAL" : mtOperation.getOperationType());
        mtOperation.set_tls(getTlsMap(customerTlMap));
        mtOperation.setTenantId(tenantId);
        mtOperation.setLastUpdateDate(date);
        mtOperation.setLastUpdatedBy(userId);
        return mtOperation;
    }


    /**
     * 构建返回消息
     *
     * @param mtStandardOperationIface MtStandardOperationIface
     * @param message                  错误消息
     * @return MtBomComponentIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtStandardOperationIface constructIfaceMessage(Long tenantId,
                                                           MtStandardOperationIface mtStandardOperationIface, String status, String message, Date date,
                                                           Long userId) {
        mtStandardOperationIface.setTenantId(tenantId);
        mtStandardOperationIface.setStatus(status);
        mtStandardOperationIface.setMessage(message);
        mtStandardOperationIface.setLastUpdateDate(date);
        mtStandardOperationIface.setLastUpdatedBy(userId);

        return mtStandardOperationIface;
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
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
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

    private static class OperationTuple implements Serializable {
        private static final long serialVersionUID = 7362404800293645104L;
        private String operationName;
        private String revision;
        private String siteId;

        public OperationTuple(String operationName, String revision, String siteId) {
            this.operationName = operationName;
            this.revision = revision;
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
            OperationTuple that = (OperationTuple) o;
            return Objects.equals(operationName, that.operationName) && Objects.equals(revision, that.revision)
                    && Objects.equals(siteId, that.siteId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(operationName, revision, siteId);
        }
    }

    private Map<String, Map<String, String>> getTlsMap(Map<String, String> fieldValueMap) {
        Map<String, Map<String, String>> tlMap = new HashMap<>(fieldValueMap.size());
        Map<String, String> map;
        for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
            map = new HashMap<>(0);
            map.put("zh_CN", entry.getValue());
            tlMap.put(entry.getKey(), map);
        }
        return tlMap;
    }
}
