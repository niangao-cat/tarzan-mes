package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosTestPassRate;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO4;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO5;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * COS良率计算定时任务Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-09-17 11:35:12
 */
public interface HmeCosYieldComputeMapper {

    /**
     * 查询上次Job执行时间
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 01:50:41
     * @return java.util.Date
     */
    Date getLastJobDate(@Param("tenantId") Long tenantId);

    /**
     * 插入一条表hme_cos_test_monitor_header数据，用于记录job执行时间
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 01:59:06
     * @return void
     */
    void insertJobDate(@Param("tenantId") Long tenantId, @Param("nowDate") Date nowDate);

    /**
     * 更新job执行时间
     *
     * @param tenantId 租户ID
     * @param nowDate 当前时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 02:25:48
     * @return void
     */
    void updateJobDate(@Param("tenantId") Long tenantId, @Param("nowDate") Date nowDate);

    /**
     * 查询芯片性能表中最后更新时间大于上次Job执行时间的LoadSequence
     *
     * @param tenantId 租户ID
     * @param lastJobDate 上次job时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 02:34:01
     * @return java.util.List<java.lang.String>
     */
    List<String> getLoadSequenceByFunctionUpdatedDate(@Param("tenantId") Long tenantId, @Param("lastJobDate") Date lastJobDate);

    /**
     * 根据LoadSequence集合查询cos类型和wafer的组合
     *
     * @param tenantId 租户ID
     * @param loadSequenceList LoadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 02:42:02
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> getCosTypeWaferComposeByLoadSequence(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 根据COS类型查询来料良率
     *
     * @param tenantId 租户ID
     * @param cosTypeList COS类型集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 02:53:18
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosTestPassRate>
     */
    List<HmeCosTestPassRate> getInputPassRateByCosType(@Param("tenantId") Long tenantId, @Param("cosTypeList") List<String> cosTypeList);

    /**
     * 根据扩展名和扩展值查询物料批编码以3开头的物料批
     *
     * @param tenantId 租户ID
     * @param attrName 扩展名
     * @param attrValueList 扩展值集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 03:17:11
     * @return java.util.List<java.lang.String>
     */
    List<HmeCosYieldComputeVO> getMaterialLotIdByAttr(@Param("tenantId") Long tenantId, @Param("attrName") String attrName, @Param("attrValueList") List<String> attrValueList);

    /**
     * 根据满足wafer的物料批Id、扩展名、扩展值查询满足条件的物料批Id
     *
     * @param tenantId 租户ID
     * @param attrName 扩展名
     * @param attrValueList 扩展值
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 09:08:24
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosYieldComputeVO>
     */
    List<HmeCosYieldComputeVO> getCosTypeMaterialLotId(@Param("tenantId") Long tenantId, @Param("attrName") String attrName, @Param("attrValueList") List<String> attrValueList,
                                                       @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询传入的物料批中是否存在MF_FLAG=Y的在制品
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 09:27:40
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosYieldComputeVO>
     */
    List<String> getMfFlagMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 获取物料批的产出数
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 04:03:37
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> getMaterialLotSnQty(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据COS类型和wafer的组合查询芯片性能表的LoadSequence及A24
     *
     * @param tenantId
     * @param cosTypeWaferComposeList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 04:36:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosYieldComputeVO4>
     */
    List<HmeCosYieldComputeVO4> getLoadSequenceByCosTypeWafer(@Param("tenantId") Long tenantId, @Param("cosTypeWaferComposeList") List<HmeMaterialLotLoad> cosTypeWaferComposeList);

    /**
     * 根据cos_type、wafer查询hme_cos_test_monitor_header表
     *
     * @param tenantId 租户ID
     * @param cosTypeWaferComposeList cos类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 10:43:40
     * @return java.lang.Long
     */
    List<HmeCosYieldComputeVO5> testMonitorHeaderQueryByCosTypeWafer(@Param("tenantId") Long tenantId, @Param("cosTypeWaferComposeList") List<HmeMaterialLotLoad> cosTypeWaferComposeList);

    /**
     * 批量插入COS测试良率监控头表
     *
     * @param tenantId 租户ID
     * @param monitorHeaderInsertDataList COS测试良率监控头表插入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 02:49:21
     * @return void
     */
    void batchInsertCosTestMonitorHeader(@Param("tenantId") Long tenantId, @Param("monitorHeaderInsertDataList") List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList);

    /**
     * 批量更新COS测试良率监控头表
     *
     * @param tenantId 租户ID
     * @param monitorHeaderUpdateDataList COS测试良率监控头表更新数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 03:11:22
     * @return void
     */
    void batchUpdateCosTestMonitorHeader(@Param("tenantId") Long tenantId, @Param("monitorHeaderUpdateDataList") List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList);

    /**
     * 批量插入COS测试良率监控记录表
     *
     * @param tenantId 租户ID
     * @param monitorRecordInsertDataList COS测试良率监控监控表插入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 03:22:58
     * @return void
     */
    void batchInsertCosTestMonitorRecord(@Param("tenantId") Long tenantId, @Param("monitorRecordInsertDataList") List<HmeCosYieldComputeVO5> monitorRecordInsertDataList);
}
