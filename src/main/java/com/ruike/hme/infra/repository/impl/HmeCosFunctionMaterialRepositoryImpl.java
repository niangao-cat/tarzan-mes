package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeCosFunctionMaterialMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterial;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * COS投料性能表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
@Component
public class HmeCosFunctionMaterialRepositoryImpl extends BaseRepositoryImpl<HmeCosFunctionMaterial> implements HmeCosFunctionMaterialRepository {

    @Autowired
    private HmeCosFunctionMaterialMapper hmeCosFunctionMaterialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteByPrimary(List<String> cosFunctionMaterialIdList) {
        List<List<String>> subCosFunctionMaterialIdList = InterfaceUtils.splitSqlList(cosFunctionMaterialIdList, SQL_ITEM_COUNT_LIMIT);
        subCosFunctionMaterialIdList.forEach(item -> {
            hmeCosFunctionMaterialMapper.batchDeleteByPrimary(item);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void myBatchInsert(List<HmeCosFunctionMaterial> cosFunctionMaterialList) {
        //批量新增数据
        List<List<HmeCosFunctionMaterial>> splitInsertList = InterfaceUtils.splitSqlList(cosFunctionMaterialList, SQL_ITEM_COUNT_LIMIT);
        for (List<HmeCosFunctionMaterial> domains : splitInsertList) {
            hmeCosFunctionMaterialMapper.myBatchInsert(domains);
        }
    }
}
