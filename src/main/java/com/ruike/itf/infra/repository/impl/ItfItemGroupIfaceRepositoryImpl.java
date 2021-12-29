package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.infra.mapper.ItfItemGroupIfaceMapper;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfItemGroupIface;
import com.ruike.itf.domain.repository.ItfItemGroupIfaceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料组接口表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
@Component
public class ItfItemGroupIfaceRepositoryImpl extends BaseRepositoryImpl<ItfItemGroupIface> implements ItfItemGroupIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    @Autowired
    private ItfItemGroupIfaceMapper itfItemGroupIfaceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public void itemGroupIfaceImport(Long tenantId) {
        // change data status
        Long start = System.currentTimeMillis();
        List<ItfItemGroupIface> itfItemGroupIfaceList = self().updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Long, List<ItfItemGroupIface>> ifacePerBatch = itfItemGroupIfaceList.stream().collect(
                Collectors.groupingBy(ItfItemGroupIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Long, List<ItfItemGroupIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveItemGroupIface(tenantId, entry.getValue());

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
        System.out.println("bom接口耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveItemGroupIface(Long tenantId, List<ItfItemGroupIface> itfItemGroupIfaceList) {
        List<AuditDomain> ifaceList = new ArrayList<>(itfItemGroupIfaceList.size());
        List<WmsItemGroup> itemGroupList = new ArrayList<>();
        Long userId = -1L;
        Date now = new Date();
//        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
//            userId = DetailsHelper.getUserDetails().getUserId();
//        }
        for (ItfItemGroupIface iface : itfItemGroupIfaceList) {
            WmsItemGroup itemGroup = new WmsItemGroup();
            if (StringUtils.isBlank(iface.getItemGroupCode())) {
                ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "ITF_INV_ITEM_0004",
                                "ITF", iface.getIfaceId(), "【API：itemGroupIfaceImport】"),
                        now, userId));
                continue;
            }
            // 查询数据是否存在
            WmsItemGroup oldItemGroup = wmsItemGroupRepository.selectOne(new WmsItemGroup(){{
                setItemGroupCode(iface.getItemGroupCode());
                setTenantId(tenantId);
            }});
            if (!ObjectUtils.isEmpty(oldItemGroup)) {
                BeanUtils.copyProperties(oldItemGroup, itemGroup);
                itemGroup.setObjectVersionNumber(itemGroup.getObjectVersionNumber() + 1L);
            } else {
                String itemGroupId = this.customDbRepository.getNextKey("wms_item_group_s");
                itemGroup.setItemGroupId(itemGroupId);
                itemGroup.setCreatedBy(userId);
                itemGroup.setCreationDate(now);
                itemGroup.setObjectVersionNumber(1L);
            }
            itemGroup.setItemGroupCode(iface.getItemGroupCode());
            itemGroup.setItemGroupDescription(iface.getItemGroupDescription());
            itemGroup.setTenantId(tenantId);
            itemGroup.setLastUpdatedBy(userId);
            itemGroup.setLastUpdateDate(now);
            itemGroupList.add(itemGroup);
            ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
        }
        /**
         * 处理物料组数据
         */
        List<AuditDomain> newBomList = new ArrayList<>(itemGroupList.size());
        newBomList.addAll(itemGroupList);
        List<String> replaceSql = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(itemGroupList)) {
            replaceSql.addAll(constructSql(newBomList));
            itemGroupList = null;
        }
        if (CollectionUtils.isNotEmpty(replaceSql)) {
            List<List<String>> commitSqlList = commitSqlList(replaceSql, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return ifaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfItemGroupIface> updateIfaceStatus(Long tenantId, String status) {
        List<ItfItemGroupIface> itemGroupIfaceList = itfItemGroupIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(itemGroupIfaceList)) {
            List<String> sqlList = new ArrayList<>(itemGroupIfaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(itemGroupIfaceList.size());
            itemGroupIfaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(itemGroupIfaceList);
            sqlList.addAll(constructSql(auditDomains));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return itemGroupIfaceList;
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
     * 生成拆分的sql
     *
     * @param ifaceSqlList
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:46
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

    /**
     * 数据分批处理
     *
     * @param sqlList
     * @param splitNum
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:47
     * @return
     */
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

    /**
     * 构建返回消息
     *
     * @param tenantId
     * @param Iface
     * @param status
     * @param message
     * @param date
     * @param userId
     * @author jiangling.zheng@hand-china.com 2020/7/17 17:18
     * @return
     */
    private ItfItemGroupIface constructIfaceMessage(Long tenantId, ItfItemGroupIface Iface,
                                                      String status, String message, Date date, Long userId) {
        Iface.setTenantId(tenantId);
        Iface.setStatus(status);
        Iface.setMessage(message);
        Iface.setLastUpdateDate(date);
        Iface.setLastUpdatedBy(userId);

        return Iface;
    }
}
