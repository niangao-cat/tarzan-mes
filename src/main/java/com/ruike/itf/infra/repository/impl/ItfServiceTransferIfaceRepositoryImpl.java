package com.ruike.itf.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruike.itf.domain.entity.ItfServiceTransferIface;
import com.ruike.itf.domain.repository.ItfServiceTransferIfaceRepository;
import com.ruike.itf.infra.mapper.ItfServiceTransferIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 售后大仓回调 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-04-01 14:05:32
 */
@Component
public class ItfServiceTransferIfaceRepositoryImpl extends BaseRepositoryImpl<ItfServiceTransferIface> implements ItfServiceTransferIfaceRepository {
    private final ItfServiceTransferIfaceMapper mapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ItfServiceTransferIfaceRepositoryImpl(ItfServiceTransferIfaceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int insertRecord(ItfServiceTransferIface record) {
        return mapper.insertSelective(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchInsertRecord(List<ItfServiceTransferIface> recordList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfServiceTransferIface record : recordList) {
            sqlList.addAll(customDbRepository.getInsertSql(record));
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchUpdateRecord(List<ItfServiceTransferIface> recordList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfServiceTransferIface record : recordList) {
            sqlList.addAll(customDbRepository.getUpdateSql(record));
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }
}
