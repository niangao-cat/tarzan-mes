package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO2;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO5;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO6;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * COS良率计算定时任务资源库
 *
 * @author chaonan.hu@hand-china.com 2021-09-17 11:35:12
 */
public interface HmeCosYieldComputeRepository {

    /**
     * 查询上次Job执行时间
     *
     * @param tenantId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 01:56:14
     * @return java.util.Date
     */
    Date getLastJobDate(Long tenantId);

    /**
     * 查询每个COS类型与WAFER的组合下有多少物料批
     *
     * @param tenantId 租户ID
     * @param cosTypeList COS类型集合
     * @param waferList wafer集合
     * @param cosTypeWaferComposeList COS类型与WAFER的组合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/17 03:39:21
     * @return com.ruike.hme.domain.vo.HmeCosYieldComputeVO2
     */
    HmeCosYieldComputeVO2 getCosTypeWaferMaterialLotIdMap(Long tenantId, List<String> cosTypeList, List<String> waferList,
                                                          List<HmeMaterialLotLoad> cosTypeWaferComposeList);

    /**
     * LoadSequence的数量大于等于基准数量场景下创建单据
     *
     * @param tenantId 租户ID
     * @param targetPassRate 目标良率
     * @param yield 计算值
     * @param cosType cos类型
     * @param wafer wafer
     * @param hmeCosYieldComputeVO5List COS测试良率监控头表数据
     * @param waferMfFlagMap wafer下是否有在制品物料批关系map
     * @param singleLoadSequenceCount 当前cos类型与wafer组合下的做过性能测试的LoadSequence个数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 02:35:46
     * @return com.ruike.hme.domain.vo.HmeCosYieldComputeVO6
     */
    HmeCosYieldComputeVO6 createDoc(Long tenantId, BigDecimal targetPassRate, BigDecimal yield, String cosType, String wafer,
                                    List<HmeCosYieldComputeVO5> hmeCosYieldComputeVO5List, Map<String, String> waferMfFlagMap,
                                    BigDecimal singleLoadSequenceCount);

    /**
     * LoadSequence的数量小于基准数量场景下创建单据
     *
     * @param tenantId 租户ID
     * @param yield 计算值
     * @param cosType cos类型
     * @param wafer wafer
     * @param hmeCosYieldComputeVO5List COS测试良率监控头表数据
     * @param singleLoadSequenceCount 当前cos类型与wafer组合下的做过性能测试的LoadSequence个数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 02:40:08
     * @return com.ruike.hme.domain.vo.HmeCosYieldComputeVO6
     */
    HmeCosYieldComputeVO6 createDoc2(Long tenantId, BigDecimal yield, String cosType, String wafer,
                                    List<HmeCosYieldComputeVO5> hmeCosYieldComputeVO5List, BigDecimal singleLoadSequenceCount);

    /**
     * 批量插入或更新COS测试良率监控头表和COS测试良率监控记录表
     *
     * @param tenantId 租户ID
     * @param monitorHeaderInsertDataList COS测试良率监控头表插入数据
     * @param monitorHeaderUpdateDataList COS测试良率监控头表更新数据
     * @param monitorRecordInsertDataList COS测试良率监控记录表插入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/18 02:42:14
     * @return void
     */
    void batchInserUpdateCosTestMonitorData(Long tenantId, List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList,
                                            List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList, List<HmeCosYieldComputeVO5> monitorRecordInsertDataList);
}
