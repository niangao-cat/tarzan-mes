package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * 来料信息记录Mapper
 *
 * @author wenzhnag.yu@hand-china.com 2020-08-17 17:26:54
 */
public interface HmeCosOperationRecordMapper extends BaseMapper<HmeCosOperationRecord> {

    /**
     *
     * @Description 根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
     *
     * @author yuchao.wang
     * @date 2020/8/18 18:49
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param operationId 工艺ID
     * @param equipmentId 设备ID
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     *
     */
    HmeCosOperationRecord queryLastRecord(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "workcellId")  String workcellId,
                                          @Param(value = "operationId")  String operationId,
                                          @Param(value = "equipmentId") String equipmentId,
                                          @Param(value = "processingFlag") String processingFlag);

    /**
     * 根据cosType查询电流点
     * @param tenantId
     * @param cosType
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/8/17
     */
    Long queryCosSelectCurrent(@Param(value = "tenantId") Long tenantId,
                               @Param("cosType") String cosType);

    /**
     * 热沉条码
     *
     * @param tenantId
     * @param materialLotId
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     * @author sanfeng.zhang@hand-china.com 2021/9/26
     */
    MtMaterialLot queryHotSinkMaterialLot(@Param(value = "tenantId") Long tenantId,
                                          @Param("materialLotId") String materialLotId);
}
