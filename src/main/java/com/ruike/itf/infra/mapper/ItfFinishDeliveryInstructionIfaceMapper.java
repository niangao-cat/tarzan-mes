package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.domain.entity.ItfWcsTaskIface;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO2;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:42
 */
public interface ItfFinishDeliveryInstructionIfaceMapper {

    /**
     * 批量查询单据号
     *
     * @param tenantId
     * @param instructionDocIdList
     * @author sanfeng.zhang@hand-china.com 2021/7/18 23:38
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstructionDoc>
     */
    List<MtInstructionDoc> batchQueryDocNumList(@Param("tenantId") Long tenantId, @Param("instructionDocIdList") List<String> instructionDocIdList);

    /**
     * 批量查询指令行行号
     *
     * @param tenantId
     * @param instructionIdList
     * @author sanfeng.zhang@hand-china.com 2021/7/18 23:56
     * @return java.util.List<com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO2>
     */
    List<ItfFinishDeliveryInstructionIfaceVO2> batchQueryDocLineNumList(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 查询成品出库指令信息
     *
     * @param tenantId
     * @param taskNumList
     * @author sanfeng.zhang@hand-china.com 2021/7/19 0:17
     * @return java.util.List<com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO>
     */
    List<ItfFinishDeliveryInstructionIfaceDTO> queryWcsTaskIfaceList(@Param("tenantId") Long tenantId, @Param("taskNumList") List<String> taskNumList);

    /**
     * 批量新增
     *
     * @param domains
     * @author sanfeng.zhang@hand-china.com 2021/7/19 2:00
     * @return void
     */
    void batchInsertTaskIfaces(@Param("domains") List<ItfWcsTaskIface> domains);

    /**
     * 批量更新
     *
     * @param tenantId
     * @param userId
     * @param taskIfaces
     * @author sanfeng.zhang@hand-china.com 2021/7/19 2:04
     * @return void
     */
    void batchUpdateTaskIfaces(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("taskIfaces") List<ItfWcsTaskIface> taskIfaces);
}
