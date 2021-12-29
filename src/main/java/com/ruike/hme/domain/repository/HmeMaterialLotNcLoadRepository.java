package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO3;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;

import java.util.*;

/**
 * 不良位置资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:28
 */
public interface HmeMaterialLotNcLoadRepository extends BaseRepository<HmeMaterialLotNcLoad> {

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
    boolean checkHasNcRecodeFlag(Long tenantId, String ncLoadId);

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
    void myBatchInsert(List<HmeMaterialLotNcLoad> insertList);

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
    int deleteNcLoadByMaterialLotId(Long tenantId, String materialLotId);

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
    HmeMaterialLotNcLoadVO3 queryNcInfoByLoadSequence(Long tenantId, String loadSequence);
}
