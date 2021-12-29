package com.ruike.qms.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstruction;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/23 19:06
 */
public interface QmsInvoiceMapper {


    /**
     * 获取行明细id
     *
     * @param tenantId
     * @param instructionDocId
     * @author sanfeng.zhang@hand-china.com 2020/10/24 10:13
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstruction>
     */
    List<MtInstruction> queryInvoiceLineList(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 查询外协超发单行
     * 
     * @param tenantId
     * @param supplierId
     * @param materialId
     * @param materialVersion
     * @author sanfeng.zhang@hand-china.com 2020/10/24 11:46 
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstruction>
     */
    List<MtInstruction> queryOverInvoiceInstructionList(@Param("tenantId") Long tenantId, @Param("supplierId") String supplierId, @Param("materialId") String materialId, @Param("materialVersion") String materialVersion);

    /**
     * 不为关闭的数量
     *
     * @param tenantId
     * @param instructionDocNum
     * @author sanfeng.zhang@hand-china.com 2020/11/3 9:19
     * @return java.lang.Integer
     */
    Integer queryInvoiceListByPoNum(@Param("tenantId") Long tenantId, @Param("instructionDocNum") String instructionDocNum);
}
