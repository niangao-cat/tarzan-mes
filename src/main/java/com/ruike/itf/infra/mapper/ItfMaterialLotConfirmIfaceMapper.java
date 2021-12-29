package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 立库入库复核接口表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
public interface ItfMaterialLotConfirmIfaceMapper extends BaseMapper<ItfMaterialLotConfirmIface> {

    /**
     * 根据物料批编码查询物料批
     *
     * @param tenantId 租户ID
     * @param materialLotCodeList 物料批编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/13 08:06:06
     * @return java.util.List<tarzan.inventory.domain.entity.MtMaterialLot>
     */
    List<MtMaterialLot> materialLotBatchQuery(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList);

    /**
     * 根据容器编码查询容器
     *
     * @param tenantId 租户ID
     * @param containerCodeList 容器编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/13 08:13:01
     * @return java.util.List<tarzan.inventory.domain.entity.MtContainer>
     */
    List<MtContainer> containerBatchQuery(@Param("tenantId") Long tenantId, @Param("containerCodeList") List<String> containerCodeList);

    /**
     * 根据条码查询相关信息，存入接口表
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 09:11:59
     * @return java.util.List<com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface>
     */
    List<ItfMaterialLotConfirmIface> materialLotConfirmIfaceQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询本批次下要传输的数据
     *
     * @param tenantId 租户ID
     * @param batchId 批次ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 10:53:26
     * @return java.util.List<com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO>
     */
    List<ItfMaterialLotConfirmIface> ifaceSendDataQuery(@Param("tenantId") Long tenantId, @Param("batchId") String batchId);

    /**
     * 将异常情况下的错误信息记录到接口表
     *
     * @param tenantId 租户ID
     * @param ifaceIdList 接口表ID集合
     * @param message 错误信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 03:38:46
     * @return void
     */
    void updateIfaceData(@Param("tenantId") Long tenantId, @Param("ifaceIdList") List<String> ifaceIdList,
                         @Param("message") String message, @Param("userId") Long userId);
}
