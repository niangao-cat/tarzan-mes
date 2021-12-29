package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO2;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO4;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO5;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItfCosQueryCollectIfaceMapper {

    List<CosQueryCollectItfReturnDTO2> selectMaterialLotByCode(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialLotCode") String materialLotCode);

    /**
     * 根据cos类型和wafer查询偏振度和发散角测试结果数据
     *
     * @param tenantId 租户ID
     * @param cosType cos类型
     * @param wafer wafer
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 10:43:24
     * @return java.util.List<com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO4>
     */
    List<CosQueryCollectItfReturnDTO4> cosDegreeTestActualQuery(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "cosType") String cosType,
                                                                @Param(value = "wafer") String wafer);
    /**
     * 根据cos类型查询偏振度和发散角良率维护头表数据
     *
     * @param tenantId 租户ID
     * @param cosType cos类型
     * @param testObject 测试对象
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 11:05:50
     * @return com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO4
     */
    CosQueryCollectItfReturnDTO4 tagPassRateHeaderQuery(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "cosType") String cosType,
                                                        @Param(value = "testObject") String testObject);

    /**
     * 批量插入偏振度和发散角测试结果表
     *
     * @param tenantId 租户ID
     * @param hmeCosDegreeTestActualInsertList 插入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 01:47:05
     * @return void
     */
    void batchInsertHmeCosDegreeTestActual(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "insertList") List<CosQueryCollectItfReturnDTO5> hmeCosDegreeTestActualInsertList);

    /**
     * 批量插入偏振度和发散角测试结果历史表
     *
     * @param tenantId 租户ID
     * @param hmeCosDegreeTestActualInsertList 插入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 01:58:47
     * @return void
     */
    void batchInsertHmeCosDegreeTestActualHis(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "insertList") List<CosQueryCollectItfReturnDTO5> hmeCosDegreeTestActualInsertList);

    /**
     * 根据物料批ID查询装载信息
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 02:19:04
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> materialLotLoadQuery(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "materialLotId") String materialLotId,
                                                  @Param(value = "testQty") Long testQty);

    /**
     * 根据cos类型和wafer查询Attribute17 = Y的个数
     *
     * @param tenantId 租户ID
     * @param cosType cos类型
     * @param wafer wafer
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 03:38:55
     * @return java.lang.Long
     */
    Long countAttribute17Y(@Param(value = "tenantId") Long tenantId,
                           @Param(value = "cosType") String cosType,
                           @Param(value = "wafer") String wafer);

    /**
     * 根据cos类型和wafer查询Attribute18 = Y的个数
     *
     * @param tenantId 租户ID
     * @param cosType cos类型
     * @param wafer wafer
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 03:38:55
     * @return java.lang.Long
     */
    Long countAttribute18Y(@Param(value = "tenantId") Long tenantId,
                           @Param(value = "cosType") String cosType,
                           @Param(value = "wafer") String wafer);

    /**
     * 根据主键更新偏振度和发散角测试结果
     *
     * @param tenantId 租户ID
     * @param degreeTestId 主键
     * @param targetQty 目标数量
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/27 09:47:57
     * @return void
     */
    void updateTargetQty(@Param(value = "tenantId") Long tenantId, @Param(value = "degreeTestId") String degreeTestId,
                         @Param(value = "targetQty") Long targetQty, @Param(value = "userId") Long userId);

    /**
     * 根据主键查询偏振度和发散角测试结果
     *
     * @param tenantId
     * @param degreeTestId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/27 09:58:34
     * @return com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO5
     */
    CosQueryCollectItfReturnDTO5 hmeCosDegreeTestActualQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "degreeTestId") String degreeTestId);
}
