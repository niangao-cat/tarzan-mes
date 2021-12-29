package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotNcRecord;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.MtNcCode;

import java.util.*;

/**
 * 不良明细表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-08-19 14:50:28
 */
public interface HmeMaterialLotNcRecordMapper extends BaseMapper<HmeMaterialLotNcRecord> {

    /**
     *
     * @Description 根据不良装载ID删除不良明细
     *
     * @author yuchao.wang
     * @date 2020/8/21 10:14
     * @param tenantId 租户ID
     * @param ncLoadList 不良装载ID
     * @return int
     *
     */
    int deleteNcRecordByNcLoadIds(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> ncLoadList);

    /**
     *
     * @Description 根据不良明细主键批量删除
     *
     * @author yuchao.wang
     * @date 2020/10/14 17:10
     * @param tenantId 租户ID
     * @param ncRecordIdList 不良明细ID
     * @return int
     *
     */
    int deleteNcRecordByIdList(@Param("tenantId") Long tenantId, @Param("ncRecordIdList") List<String> ncRecordIdList);

    /**
     *
     * @Description 删除不良明细记录
     *
     * @author yuchao.wang
     * @date 2020/8/24 11:03
     * @param tenantId 租户ID
     * @param ncLoadId 不良装载ID
     * @param ncCodeList 不良代码
     * @return int
     *
     */
    int deleteNcRecordByNcCode(@Param("tenantId") Long tenantId, @Param("ncLoadId") String ncLoadId, @Param("ncCodeList") List<String> ncCodeList);

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
    List<MtNcCode> queryNcCodeByOperationId(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

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
    void batchInsert(@Param("domains") List<HmeMaterialLotNcRecord> domains);

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
    int deleteNcRecordByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据条码ID查询不良明细表主键
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:00:13
     * @return java.util.List<java.lang.String>
     */
    List<String> getNcRecordByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据主键批量删除不良明细表数据
     *
     * @param tenantId 租户ID
     * @param ncRecordIdList 主键ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/21 03:23:54
     * @return void
     */
    void deleteNcRecordByPrimarykey(@Param("tenantId") Long tenantId, @Param("ncRecordIdList") List<String> ncRecordIdList);
}
