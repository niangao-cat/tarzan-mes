package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanSnDetailIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 拣配SN明细接口表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:09
 */
@Component
public class ItfSoDeliveryChanSnDetailIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSoDeliveryChanSnDetailIface> implements ItfSoDeliveryChanSnDetailIfaceRepository {

    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void myBatchInsert(List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanSnDetailIface snDetailIface : snDetailIfaceList) {
            sqlList.addAll(customDbRepository.getInsertSql(snDetailIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    public void myBatchUpdateSelective(List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanSnDetailIface snDetailIface : snDetailIfaceList) {
            sqlList.addAll(customDbRepository.getUpdateSql(snDetailIface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }
}
