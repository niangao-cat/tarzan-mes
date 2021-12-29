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
import tarzan.iface.domain.entity.MtSubinventory;
import tarzan.iface.domain.entity.MtSubinventoryIface;
import tarzan.iface.domain.repository.MtSubinventoryIfaceRepository;
import tarzan.iface.domain.repository.MtSubinventoryRepository;
import tarzan.iface.domain.vo.MtSubinventoryIfaceVO;
import tarzan.iface.infra.mapper.MtSubinventoryIfaceMapper;

/**
 * ERP子库存接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtSubinventoryIfaceRepositoryImpl extends BaseRepositoryImpl<MtSubinventoryIface>
                implements MtSubinventoryIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtSubinventoryRepository mtSubinventoryRepository;

    @Autowired
    private MtSubinventoryIfaceMapper mtSubinventoryIfaceMapper;

    @Override
    public void subinventoryInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        List<MtSubinventoryIface> mtSubinventoryIfaceList = self().updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Double, List<MtSubinventoryIface>> ifacePerBatch = mtSubinventoryIfaceList.stream().collect(
                        Collectors.groupingBy(MtSubinventoryIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtSubinventoryIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveSubInventoryIface(tenantId, entry.getValue());
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

                });
                ifaceList.addAll(entry.getValue());
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("ERP子库接口数据导入" + mtSubinventoryIfaceList.size() + "条数据，总耗时："
                        + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveSubInventoryIface(Long tenantId, List<MtSubinventoryIface> mtSubinventoryIfaceList) {
        // 公有变量
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());

        List<AuditDomain> ifaceList = new ArrayList<>(mtSubinventoryIfaceList.size());
        List<MtSubinventoryIfaceVO> subinventoryIfaceVOS = mtSubinventoryIfaceList.stream()
                        .map(c -> new MtSubinventoryIfaceVO(c.getPlantCode(), c.getSubinventoryCode()))
                        .collect(Collectors.toList());

        List<MtSubinventory> originSubInventoryList =
                        mtSubinventoryRepository.subInventoryBatchGet(tenantId, subinventoryIfaceVOS);
        Map<MtSubinventoryIfaceVO, MtSubinventory> subinventoryMap = null;
        if (CollectionUtils.isNotEmpty(originSubInventoryList)) {
            subinventoryMap = originSubInventoryList.stream().collect(Collectors
                            .toMap(c -> new MtSubinventoryIfaceVO(c.getPlantCode(), c.getSubinventoryCode()), c -> c));

        }
        MtSubinventoryIfaceVO ifaceVO;
        MtSubinventory mtSubinventory;
        List<MtSubinventory> changeSubInventoryList = new ArrayList<>(mtSubinventoryIfaceList.size());
        for (MtSubinventoryIface iface : mtSubinventoryIfaceList) {
            ifaceVO = new MtSubinventoryIfaceVO(iface.getPlantCode(), iface.getSubinventoryCode());

            if (MapUtils.isNotEmpty(subinventoryMap) && subinventoryMap.get(ifaceVO) != null) {
                mtSubinventory = subinventoryMap.get(ifaceVO);
                mtSubinventory.setObjectVersionNumber(mtSubinventory.getObjectVersionNumber() + 1L);
            } else {
                mtSubinventory = new MtSubinventory();
                mtSubinventory.setCreatedBy(userId);
                mtSubinventory.setCreationDate(now);
                mtSubinventory.setObjectVersionNumber(1L);
            }
            mtSubinventory.setSubinventoryCode(iface.getSubinventoryCode());
            mtSubinventory.setPlantCode(iface.getPlantCode());
            mtSubinventory.setDescription(iface.getDescription());
            mtSubinventory.setEnableFlag(iface.getEnableFlag());
            mtSubinventory.setTenantId(tenantId);
            mtSubinventory.setLastUpdatedBy(userId);
            mtSubinventory.setLastUpdateDate(now);
            changeSubInventoryList.add(mtSubinventory);
            ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
        }
        /**
         * 处理数据
         */
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(changeSubInventoryList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_subinventory_cid_s", changeSubInventoryList.size());
            List<MtSubinventory> insertList = changeSubInventoryList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getSubinventoryId())).collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_subinventory_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(changeSubInventoryList.size());
            changeSubInventoryList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getSubinventoryId())) {
                    c.setSubinventoryId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(changeSubInventoryList);
            sqlList.addAll(constructSql(tempList));
            changeSubInventoryList = null;
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

    /**
     * 构建返回消息
     *
     * @author benjamin
     * @date 2019-06-27 17:02
     * @param mtSubinventoryIface MtSubinventoryIface
     * @param message 错误消息
     * @return MtBomComponentIface
     */
    private MtSubinventoryIface constructIfaceMessage(Long tenantId, MtSubinventoryIface mtSubinventoryIface,
                    String status, String message, Date date, Long userId) {
        mtSubinventoryIface.setTenantId(tenantId);
        mtSubinventoryIface.setStatus(status);
        mtSubinventoryIface.setMessage(message);
        mtSubinventoryIface.setLastUpdateDate(date);
        mtSubinventoryIface.setLastUpdatedBy(userId);

        return mtSubinventoryIface;
    }

    /**
     * 批量更新ERP库存
     * 
     * @author benjamin
     * @date 2019-07-22 15:19
     */


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtSubinventoryIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtSubinventoryIface> list = mtSubinventoryIfaceMapper.getUnprocessedList(tenantId);
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
}
