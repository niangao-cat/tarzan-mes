package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsInstructionDocVO;
import com.ruike.wms.domain.vo.WmsInstructionVO2;
import com.ruike.wms.domain.vo.WmsStockTransferVO;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.vo.MtInstructionVO;

import java.util.List;

/**
 *
 * @Description //调拨平台查询
 * @Author xuanyu.huang
 * @Date 1:37 下午 2020/4/21
 */
public interface WmsStockTransferMapper {

    List<MtInstructionVO> selectInstruction(String instructionDocId);

    List<String> selectMtInstructionDocIds(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "property") WmsInstructionDocVO mtLogisticInstructionDoc);

    List<WmsStockTransferLineDTO> selectInstructionLine(@Param("tenantId") Long tenantId,
                                                        @Param("instructionDocId") String instructionDocId);

    int verifyData(@Param(value = "property") WmsInstructionVO2 mtLogisticInstruction);

    /**
     * @Description 库存调拨平台头数据查询
     * @param tenantId
     * @param wmsStockTransferDTO
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
     * @Date 2020-05-09 16:01
     * @Author han.zhang
     */
    List<WmsStockTransferHeadDTO> selectStockTransferData(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "dto") WmsStockTransferDTO wmsStockTransferDTO);
    
    /**
    * @Description: 库存调拨-通过行ID查询条码信息
    * @author: Deng Xu
    * @date 2020/6/8 13:51
    * @param tenantId 租户ID
    * @param instructionId 调拨单行ID
    * @return : java.util.List<com.ruike.wms.domain.vo.WmsStockTransferVO>
    * @version 1.0
    */
    List<WmsStockTransferVO> listMaterialLotForUi(@Param("tenantId") Long tenantId, @Param("instructionId")String instructionId );

    /**
     * 调拨单打印头数据
     *
     * @param tenantId 租户ID
     * @param instructionDocId 单据ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/21 17:18:31
     * @return com.ruike.wms.api.dto.WmsStockTransferDTO2
     */
    WmsStockTransferDTO2 printHeadDataQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 调拨单打印行数据
     *
     * @param tenantId 租户ID
     * @param instructionDocId 单据ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/21 17:55:19
     * @return com.ruike.wms.api.dto.WmsStockTransferDTO3
     */
    List<WmsStockTransferDTO3> printLineDataQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);
}
