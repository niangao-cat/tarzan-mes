package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import com.ruike.itf.domain.repository.ItfSoDeliveryLineChanIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 交货单修改过账接口行表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
@Component
public class ItfSoDeliveryLineChanIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSoDeliveryLineChanIface> implements ItfSoDeliveryLineChanIfaceRepository {

    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void myBatchInsert(List<ItfSoDeliveryLineChanIface> lineChanIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryLineChanIface lineChanIface : lineChanIfaceList) {
            sqlList.addAll(customDbRepository.getInsertSql(lineChanIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    public void myBatchUpdateSelective(List<ItfSoDeliveryLineChanIface> lineChanIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryLineChanIface lineChanIface : lineChanIfaceList) {
            sqlList.addAll(customDbRepository.getUpdateSql(lineChanIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }
}
