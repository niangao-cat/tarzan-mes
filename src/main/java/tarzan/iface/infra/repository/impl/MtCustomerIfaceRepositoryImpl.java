package tarzan.iface.infra.repository.impl;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtCustomerIface;
import tarzan.iface.domain.repository.MtCustomerIfaceRepository;
import tarzan.iface.infra.mapper.MtCustomerIfaceMapper;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtCustomerSite;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.domain.repository.MtCustomerSiteRepository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户数据接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtCustomerIfaceRepositoryImpl extends BaseRepositoryImpl<MtCustomerIface>
                implements MtCustomerIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomerIfaceMapper mtCustomerIfaceMapper;

    @Autowired
    private MtCustomerRepository mtCustomerRepository;
    @Autowired
    private MtCustomerSiteRepository mtCustomerSiteRepository;

    @Override
    public void customerInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        List<MtCustomerIface> mtSubinventoryIfaceList = self().updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Double, List<MtCustomerIface>> ifacePerBatch = mtSubinventoryIfaceList.stream()
                        .collect(Collectors.groupingBy(MtCustomerIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtCustomerIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                List<AuditDomain> ifaceList = self().saveCustomerIface(tenantId, entry.getValue());
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
                    ifs.setTenantId(tenantId);
                    ifaceList.add(ifs);
                });
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("customer导入" + mtSubinventoryIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveCustomerIface(Long tenantId, List<MtCustomerIface> mtCustomerIfaceList) {
        List<AuditDomain> resultList = new ArrayList<>(mtCustomerIfaceList.size());
        Map<String, List<MtCustomerIface>> ifacePerCustomer =
                        mtCustomerIfaceList.stream().collect(Collectors.groupingBy(MtCustomerIface::getCustomerCode));
        List<String> customerCodeList = new ArrayList<>(ifacePerCustomer.keySet());
        List<MtCustomer> originCustomerList = mtCustomerRepository.queryCustomerByCode(tenantId, customerCodeList);
        Map<String, List<MtCustomer>> existCustomerPerCode = null;
        Map<Customer, MtCustomerSite> sitePerCustomer = null;
        if (CollectionUtils.isNotEmpty(originCustomerList)) {
            existCustomerPerCode =
                            originCustomerList.stream().collect(Collectors.groupingBy(MtCustomer::getCustomerCode));

            List<String> customerIdList = originCustomerList.stream().map(MtCustomer::getCustomerId).distinct()
                            .collect(Collectors.toList());
            List<MtCustomerSite> mtCustomerSites =
                            mtCustomerSiteRepository.queryCustomerSiteByCustomerId(tenantId, customerIdList);
            if (CollectionUtils.isNotEmpty(mtCustomerSites)) {
                sitePerCustomer = mtCustomerSites.stream().collect(Collectors
                                .toMap(c -> new Customer(c.getCustomerId(), c.getCustomerSiteCode()), c -> c));
            }
        }

        // 获取Cid、Id
        List<String> customerIds = this.customDbRepository.getNextKeys("mt_customer_s",
                        ifacePerCustomer.keySet().size() - originCustomerList.size());
        List<String> customerCids =
                        this.customDbRepository.getNextKeys("mt_customer_cid_s", ifacePerCustomer.keySet().size());
        List<String> customerSiteIds =
                        this.customDbRepository.getNextKeys("mt_customer_site_s", mtCustomerIfaceList.size());
        List<String> customerSiteCids =
                        this.customDbRepository.getNextKeys("mt_customer_site_cid_s", mtCustomerIfaceList.size());

        // 公有变量
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());

        List<AuditDomain> customerList = new ArrayList<>(ifacePerCustomer.keySet().size());
        List<AuditDomain> customerSiteList = new ArrayList<>(mtCustomerIfaceList.size());
        MtCustomer mtCustomer;
        Customer customer;
        MtCustomerSite mtCustomerSite;
        for (Map.Entry<String, List<MtCustomerIface>> entry : ifacePerCustomer.entrySet()) {
            boolean isNew = false;
            MtCustomerIface headCustomerIface = entry.getValue().get(0);
            if (MapUtils.isNotEmpty(existCustomerPerCode)
                            && CollectionUtils.isNotEmpty(existCustomerPerCode.get(entry.getKey()))) {
                mtCustomer = existCustomerPerCode.get(entry.getKey()).get(0);
                mtCustomer.setObjectVersionNumber(mtCustomer.getObjectVersionNumber() + 1L);
            } else {
                mtCustomer = new MtCustomer();
                mtCustomer.setCustomerId(customerIds.remove(0));
                mtCustomer.setObjectVersionNumber(1L);
                mtCustomer.setCreatedBy(userId);
                mtCustomer.setCreationDate(now);
                isNew = true;
            }

            Map<String, String> customerTlMap = new HashMap<>(2);
            customerTlMap.put("customerName", headCustomerIface.getCustomerName());
            customerTlMap.put("customerNameAlt", headCustomerIface.getCustomerNameAlt());
            mtCustomer.set_tls(getTlsMap(customerTlMap));
            mtCustomer.setCid(Long.valueOf(customerCids.remove(0)));
            mtCustomer.setCustomerCode(headCustomerIface.getCustomerCode());
            mtCustomer.setCustomerName(headCustomerIface.getCustomerName());
            mtCustomer.setCustomerNameAlt(headCustomerIface.getCustomerNameAlt());
            mtCustomer.setCustomerType(headCustomerIface.getCustomerType());
            mtCustomer.setDateFrom(headCustomerIface.getCustomerDateFrom());
            mtCustomer.setDateTo(headCustomerIface.getCustomerDateTo());
            mtCustomer.setSourceIdentificationId(headCustomerIface.getCustomerId());
            mtCustomer.setTenantId(tenantId);
            mtCustomer.setLastUpdatedBy(userId);
            mtCustomer.setLastUpdateDate(now);
            customerList.add(mtCustomer);

            for (MtCustomerIface iface : entry.getValue()) {
                customer = new Customer(mtCustomer.getCustomerId(), iface.getCustomerSiteCode());
                if (isNew || MapUtils.isEmpty(sitePerCustomer) || sitePerCustomer.get(customer) == null) {
                    mtCustomerSite = new MtCustomerSite();
                    mtCustomerSite.setCustomerSiteId(customerSiteIds.remove(0));
                    mtCustomerSite.setObjectVersionNumber(1L);
                    mtCustomerSite.setCreatedBy(userId);
                    mtCustomerSite.setCreationDate(now);
                } else {
                    mtCustomerSite = sitePerCustomer.get(customer);
                    mtCustomerSite.setObjectVersionNumber(mtCustomerSite.getObjectVersionNumber() + 1L);
                }
                Map<String, String> customerSiteTlMap = new HashMap<>(6);
                customerSiteTlMap.put("description", iface.getDescription());
                customerSiteTlMap.put("country", iface.getCountry());
                customerSiteTlMap.put("province", iface.getProvince());
                customerSiteTlMap.put("city", iface.getCity());
                customerSiteTlMap.put("person", iface.getContactPerson());
                customerSiteTlMap.put("address", iface.getAddress());
                mtCustomerSite.set_tls(getTlsMap(customerSiteTlMap));
                mtCustomerSite.set_tls(iface.get_tls());
                mtCustomerSite.setCid(Long.valueOf(customerSiteCids.remove(0)));
                mtCustomerSite.setCustomerId(mtCustomer.getCustomerId());
                mtCustomerSite.setCustomerSiteCode(iface.getCustomerSiteCode());
                mtCustomerSite.setDescription(iface.getDescription());
                mtCustomerSite.setSiteUseType(iface.getSiteUseType());
                mtCustomerSite.setAddress(iface.getAddress());
                mtCustomerSite.setCountry(iface.getCountry());
                mtCustomerSite.setProvince(iface.getProvince());
                mtCustomerSite.setCity(iface.getCity());
                mtCustomerSite.setPhone(iface.getContactPhoneNumber());
                mtCustomerSite.setPerson(iface.getContactPerson());
                mtCustomerSite.setDateFrom(iface.getSiteDateFrom());
                mtCustomerSite.setDateTo(iface.getSiteDateTo());
                mtCustomerSite.setSourceIdentificationId(iface.getCustomerSiteNumber());
                mtCustomerSite.setTenantId(tenantId);
                mtCustomerSite.setLastUpdatedBy(userId);
                mtCustomerSite.setLastUpdateDate(now);
                customerSiteList.add(mtCustomerSite);

                iface.setStatus("S");
                iface.setMessage("成功.");
                iface.setLastUpdatedBy(userId);
                iface.setLastUpdateDate(now);
                resultList.add(iface);
            }
        }
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customerList)) {
            sqlList.addAll(constructSql(customerList));
            customerList = null;
        }
        if (CollectionUtils.isNotEmpty(customerSiteList)) {
            sqlList.addAll(constructSql(customerSiteList));
            customerSiteList = null;
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
    public List<MtCustomerIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtCustomerIface> mtCustomerIfaceList = mtCustomerIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(mtCustomerIfaceList)) {
            List<String> sqlList = new ArrayList<>(mtCustomerIfaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(mtCustomerIfaceList.size());
            mtCustomerIfaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(mtCustomerIfaceList);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return mtCustomerIfaceList;
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

    private static class Customer implements Serializable {
        private static final long serialVersionUID = 3505045256948726705L;
        private String customerId;
        private String customerSiteCode;

        public Customer(String customerId, String customerSiteCode) {
            this.customerId = customerId;
            this.customerSiteCode = customerSiteCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Customer customer = (Customer) o;
            return Objects.equals(customerId, customer.customerId)
                            && Objects.equals(customerSiteCode, customer.customerSiteCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, customerSiteCode);
        }
    }

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
}
