package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanLotDetailIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 拣配批次明细接口表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
@Component
public class ItfSoDeliveryChanLotDetailIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSoDeliveryChanLotDetailIface> implements ItfSoDeliveryChanLotDetailIfaceRepository {

    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void myBatchInsert(List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanLotDetailIface lotDetailIface : lotDetailIfaceList) {
            sqlList.addAll(customDbRepository.getInsertSql(lotDetailIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    public void myBatchUpdateSelective(List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanLotDetailIface lotDetailIface : lotDetailIfaceList) {
            sqlList.addAll(customDbRepository.getUpdateSql(lotDetailIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }
}
