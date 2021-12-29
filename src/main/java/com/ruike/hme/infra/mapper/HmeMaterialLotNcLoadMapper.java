package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO3;
import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO4;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 不良位置Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:28
 */
public interface HmeMaterialLotNcLoadMapper extends BaseMapper<HmeMaterialLotNcLoad> {

    /**
     *
     * @Description 根据装载表行序号查询ID
     *
     * @author yuchao.wang
     * @date 2020/8/20 22:06
     * @param tenantId 租户ID
     * @param loadSequenceList 装载表行序号
     * @return java.util.List<java.lang.String>
     *
     */
    List<String> queryNcLoadIdsByLoadSeqs(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<Long> loadSequenceList);

    /**
     *
     * @Description 根据装载表行序号删除不良装载数据
     *
     * @author yuchao.wang
     * @date 2020/8/21 10:14
     * @param tenantId 租户ID
     * @param loadSequenceList 装载表行序号
     * @return int
     *
     */
    int deleteNcLoadByLoadSeqs(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<Long> loadSequenceList);

    /**
     *
     * @Description 查询不良位置是否还有不良明细
     *
     * @author yuchao.wang
     * @date 2020/8/24 11:12
     * @param tenantId 租户ID
     * @param ncLoadId 不良位置ID
     * @return java.lang.Integer
     *
     */
    Integer checkHasNcRecodeFlag(@Param("tenantId") Long tenantId, @Param("ncLoadId") String ncLoadId);

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
    void batchInsert(@Param("domains") List<HmeMaterialLotNcLoad> domains);

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
    int deleteNcLoadByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据条码ID查询不良装载表主键
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:11:52
     * @return java.util.List<java.lang.String>
     */
    List<String> getNcLoadByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量删除不良装载表数据
     *
     * @param tenantId 租户ID
     * @param ncLoadIdList 主键ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:29:38
     * @return void
     */
    void deleteNcLoadByPrimarykey(@Param("tenantId") Long tenantId, @Param("ncLoadIdList") List<String> ncLoadIdList);

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
    HmeMaterialLotNcLoadVO3 queryNcInfoByLoadSequence(@Param("tenantId") Long tenantId, @Param("loadSequence") String loadSequence);

    /**
     * 根据虚拟号ID查询不良信息
     *
     * @param tenantId
     * @param virtualIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/7 15:28
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotNcLoad>
     */
    List<HmeMaterialLotNcLoadVO4> queryNcLoadByVirtualId(@Param("tenantId") Long tenantId, @Param("virtualIdList") List<String> virtualIdList);
}
