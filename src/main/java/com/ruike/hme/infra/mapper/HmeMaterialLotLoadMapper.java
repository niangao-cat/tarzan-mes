package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 来料装载位置表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:29
 */
public interface HmeMaterialLotLoadMapper extends BaseMapper<HmeMaterialLotLoad> {

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
    List<HmeMaterialLotLoadVO> queryLoadDatasByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

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
    List<HmeMaterialLotLoadVO2> queryLoadDetailByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 判断物料批是否存在不良芯片
     *
     * @author yuchao.wang
     * @date 2020/8/19 20:15
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return java.lang.Integer
     *
     */
    Integer checkHasNcLoadFlag(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param domains 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param("domains") List<HmeMaterialLotLoad> domains);

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
    List<HmeMaterialLotLoadVO3> queryLoadHotSinkByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

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
    int deleteLoadByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据条码ID查询装载表主键
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:17:49
     * @return java.util.List<java.lang.String>
     */
    List<String> getLoadByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量删除装载表数据
     *
     * @param tenantId 租户ID
     * @param loadIdList 主键ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:33:00
     * @return void
     */
    void deleteLoadByPrimarykey(@Param("tenantId") Long tenantId, @Param("loadIdList") List<String> loadIdList);

    /**
     * 批量解绑
     *
     * @param tenantId
     * @param userId
     * @param materialLotLoadList
     * @author sanfeng.zhang@hand-china.com 2020/12/1 16:46
     * @return void
     */
    void batchLoadUntie(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("materialLotLoadList") List<HmeMaterialLotLoad> materialLotLoadList);

    /**
     *
     * @Description 根据虚拟号ID查询装载信息
     *
     * @author penglin.sui
     * @date 2021/1/6 15:47
     * @param tenantId 租户ID
     * @param virtualIdList 虚拟号ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnSingleVO7>
     *
     */
    List<HmeEoJobSnSingleVO7> queryMaterialLotLoadByVirtualId(@Param("tenantId") Long tenantId
                                                             ,@Param("virtualIdList") List<String> virtualIdList);

    /**
     *
     * @Description 根据虚拟号ID查询装载信息
     *
     * @author penglin.sui
     * @date 2021/1/7 16:44
     * @param tenantId 租户ID
     * @param eoId EO ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnSingleVO7>
     *
     */
    List<HmeEoJobSnSingleVO7> queryMaterialLotLoadByEoId(@Param("tenantId") Long tenantId
                                                        ,@Param("eoId") String eoId);

    /**
     * 根据虚拟号ID查询含不良的装载信息
     *
     * @param tenantId
     * @param virtualIdList
     * @author sanfeng.zhang@hand-china.com 2021/3/25 20:32
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> queryMaterialLotNcLoadByVirtualId(@Param("tenantId") Long tenantId, @Param("virtualIdList") List<String> virtualIdList);

    /**
     * 查询挑选物料ID
     *
     * @param tenantId
     * @param materialLotIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmePreSelectionMaterialVO>
     * @author sanfeng.zhang@hand-china.com 2021/11/11
     */
    List<HmePreSelectionMaterialVO> queryPreSelectionMaterialList(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
