package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO2;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 来料装载位置表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:29
 */
@Component
public class HmeMaterialLotLoadRepositoryImpl extends BaseRepositoryImpl<HmeMaterialLotLoad> implements HmeMaterialLotLoadRepository {

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    /**
     *
     * @Description 根据物料批查询装载表数据
     *
     * @author yuchao.wang
     * @date 2020/8/18 17:17
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.vo.HmeMaterialLotLoadVO
     *
     */
    @Override
    public List<HmeMaterialLotLoadVO> queryLoadDatasByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotLoadMapper.queryLoadDatasByMaterialLotId(tenantId, materialLotId);
    }

    /**
     *
     * @Description 根据物料批查询装载表及不良数据
     *
     * @author yuchao.wang
     * @date 2020/8/21 9:34
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotLoadVO>
     *
     */
    @Override
    public List<HmeMaterialLotLoadVO2> queryLoadDetailByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotLoadMapper.queryLoadDetailByMaterialLotId(tenantId, materialLotId);
    }

    /**
     *
     * @Description 判断物料批是否存在不良芯片
     *
     * @author yuchao.wang
     * @date 2020/8/19 20:15
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return boolean
     *
     */
    @Override
    public boolean checkHasNcLoadFlag(Long tenantId, String materialLotId) {
        Integer hasNcLoadFlag = hmeMaterialLotLoadMapper.checkHasNcLoadFlag(tenantId, materialLotId);
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
    public void myBatchInsert(List<HmeMaterialLotLoad> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                hmeMaterialLotLoadMapper.batchInsert(domains);
            }
        }
    }

    /**
     *
     * @Description 根据物料批查询热沉编号相关数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 22:11
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3>
     *
     */
    @Override
    public List<HmeMaterialLotLoadVO3> queryLoadHotSinkByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotLoadMapper.queryLoadHotSinkByMaterialLotId(tenantId, materialLotId);
    }

    /**
     *
     * @Description 根据条码ID删除装载表数据
     *
     * @author yuchao.wang
     * @date 2020/10/9 0:08
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return int
     *
     */
    @Override
    public int deleteLoadByMaterialLotId(Long tenantId, String materialLotId) {
        return hmeMaterialLotLoadMapper.deleteLoadByMaterialLotId(tenantId, materialLotId);
    }

    @Override
    public int updateByPrimaryKeySelective(HmeMaterialLotLoad obj) {
        return hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(obj);
    }
}
