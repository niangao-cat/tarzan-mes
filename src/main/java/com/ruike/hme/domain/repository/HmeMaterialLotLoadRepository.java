package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO2;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 来料装载位置表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:29
 */
public interface HmeMaterialLotLoadRepository extends BaseRepository<HmeMaterialLotLoad> {


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
    List<HmeMaterialLotLoadVO> queryLoadDatasByMaterialLotId(Long tenantId, String materialLotId);

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
    List<HmeMaterialLotLoadVO2> queryLoadDetailByMaterialLotId(Long tenantId, String materialLotId);

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
    boolean checkHasNcLoadFlag(Long tenantId, String materialLotId);

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
    void myBatchInsert(List<HmeMaterialLotLoad> insertList);

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
    List<HmeMaterialLotLoadVO3> queryLoadHotSinkByMaterialLotId(Long tenantId, String materialLotId);

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
    int deleteLoadByMaterialLotId(Long tenantId, String materialLotId);

    @Override
    int updateByPrimaryKeySelective(HmeMaterialLotLoad obj);
}
