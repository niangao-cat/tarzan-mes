package tarzan.iface.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

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
import tarzan.iface.domain.entity.MtErpSubinvLocator;
import tarzan.iface.domain.entity.MtSubinvLocatorIface;
import tarzan.iface.domain.repository.MtSubinvLocatorIfaceRepository;
import tarzan.iface.domain.vo.MtErpSubinvLocatorVO;
import tarzan.iface.infra.mapper.MtErpSubinvLocatorMapper;
import tarzan.iface.infra.mapper.MtSubinvLocatorIfaceMapper;

/**
 * ERP货位接口表 资源库实现
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:33:46
 */
@Component
public class MtSubinvLocatorIfaceRepositoryImpl extends BaseRepositoryImpl<MtSubinvLocatorIface>
                implements MtSubinvLocatorIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtSubinvLocatorIfaceMapper mtSubinvLocatorIfaceMapper;

    @Autowired
    private MtErpSubinvLocatorMapper mtErpSubinvLocatorMapper;


    @Override
    public void subinvLocatorInterfaceImport(Long tenantId) {

        // Get Data List
        Long start = System.currentTimeMillis();
        List<MtSubinvLocatorIface> unprocessedList = self().updateIfaceStatus(tenantId, "P");
        // Order By batchId
        Map<Double, List<MtSubinvLocatorIface>> ifacePerBatch = unprocessedList.stream().collect(
                        Collectors.groupingBy(MtSubinvLocatorIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtSubinvLocatorIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveSubinvLocatorInterface(tenantId, entry.getValue());
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
                });
                ifaceList.addAll(entry.getValue());
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("erp仓库货位接口导入" + unprocessedList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start)
                        + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveSubinvLocatorInterface(Long tenantId,
                    List<MtSubinvLocatorIface> mtSubinvLocatorIfaceList) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        Map<MtErpSubinvLocatorVO, MtSubinvLocatorIface> voListMap = mtSubinvLocatorIfaceList.stream().collect(Collectors
                        .toMap(t -> new MtErpSubinvLocatorVO(t.getPlantCode(), t.getSubinvCode(), t.getLocatorCode()),
                                        c -> c));

        List<MtErpSubinvLocatorVO> mtErpSubinvLocator = mtSubinvLocatorIfaceList.stream()
                        .map(t -> new MtErpSubinvLocatorVO(t.getPlantCode(), t.getSubinvCode(), t.getLocatorCode()))
                        .collect(Collectors.toList());

        // mt_erp_subinv_locator
        List<MtErpSubinvLocator> insertOrUpdateData =
                        mtErpSubinvLocatorMapper.getInsertOrUpdateData(tenantId, mtErpSubinvLocator);
        Map<MtErpSubinvLocatorVO, MtErpSubinvLocator> listMap = insertOrUpdateData.stream().collect(Collectors.toMap(
                        t -> new MtErpSubinvLocatorVO(t.getPlantCode(), t.getSubinvCode(), t.getLocatorCode()),
                        c -> c));

        // 获取id,cid
        List<String> cids = this.customDbRepository.getNextKeys("mt_erp_subinv_locator_cid_s", voListMap.size());
        List<String> ids =
                        this.customDbRepository.getNextKeys("mt_erp_subinv_locator_s", voListMap.size() - listMap.size());

        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> result = new ArrayList<>(mtSubinvLocatorIfaceList.size());
        List<AuditDomain> changeList = new ArrayList<>(mtSubinvLocatorIfaceList.size());
        MtSubinvLocatorIface mtSubinvLocatorIface;
        MtErpSubinvLocator erpSubinvLocator;
        for (Map.Entry<MtErpSubinvLocatorVO, MtSubinvLocatorIface> entry : voListMap.entrySet()) {
            mtSubinvLocatorIface = entry.getValue();
            if (MapUtils.isNotEmpty(listMap) && listMap.get(entry.getKey()) != null) {
                // update
                erpSubinvLocator = listMap.get(entry.getKey());
                erpSubinvLocator = constructMtErpSubinvLocator(tenantId, false, mtSubinvLocatorIface, erpSubinvLocator,
                                now, userId);
            } else {
                // add
                erpSubinvLocator = new MtErpSubinvLocator();
                erpSubinvLocator.setSubinvLocatorId(ids.remove(0));
                erpSubinvLocator = constructMtErpSubinvLocator(tenantId, true, mtSubinvLocatorIface, erpSubinvLocator,
                                now, userId);
            }
            erpSubinvLocator.setCid(Long.valueOf(cids.remove(0)));

            mtSubinvLocatorIface.setMessage("成功");
            mtSubinvLocatorIface.setStatus("S");
            mtSubinvLocatorIface.setLastUpdatedBy(userId);
            mtSubinvLocatorIface.setLastUpdateDate(now);


            changeList.add(erpSubinvLocator);
            result.add(mtSubinvLocatorIface);
        }
        if (CollectionUtils.isNotEmpty(changeList)) {
            sqlList.addAll(constructSql(changeList));
            changeList = null;
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
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtSubinvLocatorIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtSubinvLocatorIface> list = mtSubinvLocatorIfaceMapper.getUnprocessedList(tenantId);
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

    private MtErpSubinvLocator constructMtErpSubinvLocator(Long tenantId, Boolean isNew, MtSubinvLocatorIface iface,
                    MtErpSubinvLocator erpSubinvLocator, Date now, Long userId) {
        erpSubinvLocator.setTenantId(tenantId);
        erpSubinvLocator.setPlantCode(iface.getPlantCode());
        erpSubinvLocator.setLocatorId(iface.getLocatorId());
        erpSubinvLocator.setLocatorCode(iface.getLocatorCode());
        erpSubinvLocator.setSubinvCode(iface.getSubinvCode());
        erpSubinvLocator.setEnableFlag(iface.getEnableFlag());
        if (isNew) {
            erpSubinvLocator.setCreationDate(now);
            erpSubinvLocator.setCreatedBy(userId);
            erpSubinvLocator.setObjectVersionNumber(1L);
        } else {
            erpSubinvLocator.setObjectVersionNumber(erpSubinvLocator.getObjectVersionNumber() + 1L);
        }
        erpSubinvLocator.setLastUpdateDate(now);
        erpSubinvLocator.setLastUpdatedBy(userId);
        return erpSubinvLocator;
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

}
