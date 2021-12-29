package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
import io.tarzan.common.domain.util.StringHelper;
import tarzan.iface.domain.entity.MtSupplierIface;
import tarzan.iface.domain.repository.MtSupplierIfaceRepository;
import tarzan.iface.infra.mapper.MtSupplierIfaceMapper;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.entity.MtSupplierSite;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.repository.MtSupplierSiteRepository;

/**
 * 供应商数据接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtSupplierIfaceRepositoryImpl extends BaseRepositoryImpl<MtSupplierIface>
                implements MtSupplierIfaceRepository {
    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtSupplierIfaceMapper mtSupplierIfaceMapper;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteService;

    @Override
    public void supplierInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        List<MtSupplierIface> mtSubinventoryIfaceList = updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Double, List<MtSupplierIface>> ifacePerBatch = mtSubinventoryIfaceList.stream()
                        .collect(Collectors.groupingBy(MtSupplierIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtSupplierIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = saveSupplierIface(tenantId, entry.getValue());
                log(tenantId, ifaceList);
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

                });
                ifaceList.addAll(entry.getValue());
                log(tenantId, ifaceList);
            }
        }
        System.out.println("supplier接口导入" + mtSubinventoryIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveSupplierIface(Long tenantId, List<MtSupplierIface> mtSupplierIfaceList) {
        List<AuditDomain> resultList = new ArrayList<>(mtSupplierIfaceList.size());
        Long userId = Long.valueOf(-1L);
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        Map<String, List<MtSupplierIface>> ifacePerSupplier =
                        mtSupplierIfaceList.stream().collect(Collectors.groupingBy(MtSupplierIface::getSupplierCode));

        List<String> supplierCodeList = new ArrayList<>(ifacePerSupplier.keySet());

        List<MtSupplier> originSupplierList = mtSupplierRepository.querySupplierByCode(tenantId, supplierCodeList);
        Map<String, MtSupplier> existSupplierPerCode = null;
        Map<SupplierSite, MtSupplierSite> sitePerSupplier = null;
        if (CollectionUtils.isNotEmpty(originSupplierList)) {
            existSupplierPerCode =
                            originSupplierList.stream().collect(Collectors.toMap(MtSupplier::getSupplierCode, c -> c));
            List<String> supplierIdList =
                            originSupplierList.stream().map(MtSupplier::getSupplierId).collect(Collectors.toList());

            sitePerSupplier = mtSupplierSiteService.querySupplierSiteBySupplierId(tenantId, supplierIdList).stream()
                            .collect(Collectors.toMap(c -> new SupplierSite(c.getSupplierId(), c.getSupplierSiteCode()),
                                            c -> c));
        }

        // 获取id,cid
        List<String> cids = this.customDbRepository.getNextKeys("mt_supplier_cid_s", ifacePerSupplier.size());
        List<String> ids = this.customDbRepository.getNextKeys("mt_supplier_s",
                        ifacePerSupplier.size() - originSupplierList.size());
        List<AuditDomain> supplierList = new ArrayList<>(ifacePerSupplier.keySet().size());
        List<MtSupplierSite> supplierSiteList = new ArrayList<>(mtSupplierIfaceList.size());

        MtSupplier mtSupplier;
        MtSupplierIface headSupplierIface;
        MtSupplierSite mtSupplierSite;
        SupplierSite supplierSite;
        for (Map.Entry<String, List<MtSupplierIface>> entry : ifacePerSupplier.entrySet()) {
            headSupplierIface = entry.getValue().get(0);
            if (MapUtils.isNotEmpty(existSupplierPerCode) && existSupplierPerCode.get(entry.getKey()) != null) {
                mtSupplier = existSupplierPerCode.get(entry.getKey());
                mtSupplier.setObjectVersionNumber(mtSupplier.getObjectVersionNumber() + 1L);
            } else {
                mtSupplier = new MtSupplier();
                mtSupplier.setSupplierId(ids.remove(0));
                mtSupplier.setObjectVersionNumber(1L);
                mtSupplier.setCreatedBy(userId);
                mtSupplier.setCreationDate(now);

            }

            Map<String, String> supplierTlMap = new HashMap<>(2);
            supplierTlMap.put("supplierName", headSupplierIface.getSupplierName());
            supplierTlMap.put("supplierNameAlt", headSupplierIface.getSupplierNameAlt());
            mtSupplier.set_tls(getTlsMap(supplierTlMap));
            mtSupplier.setCid(Long.valueOf(cids.remove(0)));
            mtSupplier.setSupplierCode(headSupplierIface.getSupplierCode());
            mtSupplier.setSupplierName(headSupplierIface.getSupplierName());
            mtSupplier.setSupplierNameAlt(headSupplierIface.getSupplierNameAlt());
            mtSupplier.setSupplierType(headSupplierIface.getSupplierType());
            mtSupplier.setDateFrom(headSupplierIface.getSupplierDateFrom());
            mtSupplier.setDateTo(headSupplierIface.getSupplierDateTo());
            mtSupplier.setSourceIdentificationId(headSupplierIface.getSupplierId());
            mtSupplier.setTenantId(tenantId);
            mtSupplier.setLastUpdatedBy(userId);
            mtSupplier.setLastUpdateDate(now);
            supplierList.add(mtSupplier);
            for (MtSupplierIface iface : entry.getValue()) {
                supplierSite = new SupplierSite(mtSupplier.getSupplierId(), iface.getSupplierSiteCode());
                if (MapUtils.isEmpty(sitePerSupplier) || sitePerSupplier.get(supplierSite) == null) {
                    mtSupplierSite = new MtSupplierSite();
                    mtSupplierSite.setCreatedBy(userId);
                    mtSupplierSite.setCreationDate(now);
                    mtSupplierSite.setObjectVersionNumber(1L);
                } else {
                    mtSupplierSite = sitePerSupplier.get(supplierSite);
                    mtSupplierSite.setObjectVersionNumber(mtSupplierSite.getObjectVersionNumber() + 1);

                }

                Map<String, String> supplierSiteTlMap = new HashMap<>(6);
                supplierSiteTlMap.put("supplierSiteName", iface.getSupplierSiteCode());
                supplierSiteTlMap.put("country", iface.getCountry());
                supplierSiteTlMap.put("province", iface.getProvince());
                supplierSiteTlMap.put("city", iface.getCity());
                supplierSiteTlMap.put("person", iface.getContactPerson());
                supplierSiteTlMap.put("address", iface.getSupplierSiteAddress());
                mtSupplierSite.set_tls(getTlsMap(supplierSiteTlMap));
                mtSupplierSite.setSupplierId(mtSupplier.getSupplierId());
                mtSupplierSite.setSupplierId(mtSupplier.getSupplierId());
                mtSupplierSite.setSupplierSiteCode(iface.getSupplierSiteCode());
                mtSupplierSite.setSupplierSiteName(iface.getSupplierSiteCode());
                mtSupplierSite.setAddress(iface.getSupplierSiteAddress());
                mtSupplierSite.setCountry(iface.getCountry());
                mtSupplierSite.setProvince(iface.getProvince());
                mtSupplierSite.setCity(iface.getCity());
                mtSupplierSite.setPhone(iface.getContactPhoneNumber());
                mtSupplierSite.setPerson(iface.getContactPerson());
                mtSupplierSite.setDateFrom(iface.getSiteDateFrom());
                mtSupplierSite.setDateTo(iface.getSiteDateTo());
                mtSupplierSite.setSourceIdentificationId(iface.getSupplierSiteId());
                mtSupplierSite.setTenantId(tenantId);
                mtSupplierSite.setLastUpdatedBy(userId);
                mtSupplierSite.setLastUpdateDate(now);
                supplierSiteList.add(mtSupplierSite);
                iface.setStatus("S");
                iface.setMessage("成功.");
                iface.setLastUpdateDate(now);
                iface.setLastUpdatedBy(userId);
                resultList.add(iface);
            }
        }
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(supplierList)) {
            sqlList.addAll(constructSql(supplierList));
            supplierList = null;
        }

        if (CollectionUtils.isNotEmpty(supplierSiteList)) {
            List<String> newCids = this.customDbRepository.getNextKeys("mt_supplier_site_cid_s", supplierSiteList.size());
            List<MtSupplierSite> insertList = supplierSiteList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getSupplierSiteId())).collect(Collectors.toList());
            List<String> newIds = this.customDbRepository.getNextKeys("mt_supplier_site_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(supplierSiteList.size());
            supplierSiteList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getSupplierSiteId())) {
                    c.setSupplierSiteId(newIds.remove(0));
                }
                c.setCid(Long.valueOf(newCids.remove(0)));
            });
            tempList.addAll(supplierSiteList);
            sqlList.addAll(constructSql(tempList));
            supplierSiteList = null;
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
        return resultList;
    }

    /**
     * 获得多语言Map
     *
     * @author benjamin
     * @date 2019-07-10 21:47
     * @param fieldValueMap 多语言描述
     * @return Map
     */
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


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtSupplierIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtSupplierIface> list = mtSupplierIfaceMapper.getUnprocessedList(tenantId);
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
     *
     * @author benjamin
     * @date 2019-06-25 18:40
     * @return List<List>
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

    private static class SupplierSite implements Serializable {
        private static final long serialVersionUID = 64671682726335104L;
        private String supplierId;
        private String supplierSiteCode;

        public SupplierSite(String supplierId, String supplierSiteCode) {
            this.supplierId = supplierId;
            this.supplierSiteCode = supplierSiteCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SupplierSite that = (SupplierSite) o;
            return Objects.equals(supplierId, that.supplierId)
                            && Objects.equals(supplierSiteCode, that.supplierSiteCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplierId, supplierSiteCode);
        }
    }

}
