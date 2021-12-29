package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotNcLoadRepository;
import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO3;
import com.ruike.hme.infra.mapper.HmeMaterialLotNcLoadMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 不良位置 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:28
 */
@Component
public class HmeMaterialLotNcLoadRepositoryImpl extends BaseRepositoryImpl<HmeMaterialLotNcLoad> implements HmeMaterialLotNcLoadRepository {

    @Autowired
    private HmeMaterialLotNcLoadMapper hmeMaterialLotNcLoadMapper;

    /**
     *
     * @Description 查询不良位置是否还有不良明细
     *
     * @author yuchao.wang
     * @date 2020/8/24 11:14
     * @param tenantId 租户ID
     * @param ncLoadId 不良位置ID
     * @return boolean
     *
     */
    @Override
    public boolean checkHasNcRecodeFlag(Long tenantId, String ncLoadId) {
        Integer hasNcLoadFlag = hmeMaterialLotNcLoadMapper.checkHasNcRecodeFlag(tenantId, ncLoadId);
        return !Objects.isNull(hasNcLoadFlag) && 1 == hasNcLoadFlag;
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
    public void myBatchInsert(List<HmeMaterialLotNcLoad> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeMaterialLotNcLoad>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotNcLoad> domains : splitSqlList) {
                hmeMaterialLotNcLoadMapper.batchInsert(domains);
            }
        }
    }

    /**
     *
     * @Description 根据条码ID删除不良装载表数据
     *
     * @author yuchao.wang
     * @date 2020/10/9 0:08
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return int
     *
     */
    @Override
    public int deleteNcLoadByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotNcLoadMapper.deleteNcLoadByMaterialLotId(tenantId, materialLotId);
    }

    /**
     *
     * @Description 根据装载位置获取任意一条不良代码信息
     *
     * @author yuchao.wang
     * @date 2020/10/26 20:44
     * @param tenantId 租户ID
     * @param loadSequence 装载位置
     * @return com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO3
     *
     */
    @Override
    public HmeMaterialLotNcLoadVO3 queryNcInfoByLoadSequence(Long tenantId, String loadSequence) {
        return hmeMaterialLotNcLoadMapper.queryNcInfoByLoadSequence(tenantId, loadSequence);
    }
}
