package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeMaterialLotNcRecordMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeMaterialLotNcRecord;
import com.ruike.hme.domain.repository.HmeMaterialLotNcRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.method.domain.entity.MtNcCode;

import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 不良明细表 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-08-19 14:50:28
 */
@Component
public class HmeMaterialLotNcRecordRepositoryImpl extends BaseRepositoryImpl<HmeMaterialLotNcRecord> implements HmeMaterialLotNcRecordRepository {

    @Autowired
    private HmeMaterialLotNcRecordMapper hmeMaterialLotNcRecordMapper;

    /**
     *
     * @Description 根据工艺ID查询可选的不良代码
     *
     * @author yuchao.wang
     * @date 2020/8/21 10:36
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return tarzan.method.domain.entity.MtNcCode
     *
     */
    @Override
    public List<MtNcCode> queryNcCodeByOperationId(Long tenantId, String operationId) {
        return hmeMaterialLotNcRecordMapper.queryNcCodeByOperationId(tenantId, operationId);
    }

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param insertList 新增数据列表
     * @return void
     *
     */
    @Override
    public void myBatchInsert(List<HmeMaterialLotNcRecord> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeMaterialLotNcRecord>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotNcRecord> domains : splitSqlList) {
                hmeMaterialLotNcRecordMapper.batchInsert(domains);
            }
        }
    }

    /**
     *
     * @Description 根据条码ID删除不良明细表数据
     *
     * @author yuchao.wang
     * @date 2020/10/9 0:08
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return int
     *
     */
    @Override
    public int deleteNcRecordByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotNcRecordMapper.deleteNcRecordByMaterialLotId(tenantId, materialLotId);
    }
}
