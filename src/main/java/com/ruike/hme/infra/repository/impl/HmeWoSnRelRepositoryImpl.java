package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWoSnRel;
import com.ruike.hme.domain.repository.HmeWoSnRelRepository;
import com.ruike.hme.infra.mapper.HmeWoSnRelMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 生产订单SN码关系表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2020-08-14 17:31:42
 */
@Component
public class HmeWoSnRelRepositoryImpl extends BaseRepositoryImpl<HmeWoSnRel> implements HmeWoSnRelRepository {

    @Autowired
    private HmeWoSnRelMapper hmeWoSnRelMapper;

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/11/6 16:43
     * @param insertList 新增数据列表
     * @return void
     *
     */
    @Override
    public void myBatchInsert(List<HmeWoSnRel> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeWoSnRel>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeWoSnRel> domains : splitSqlList) {
                hmeWoSnRelMapper.batchInsert(domains);
            }
        }
    }
}
