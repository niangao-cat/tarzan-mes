package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsStockTransferVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import com.ruike.wms.api.dto.WmsInstructionReturnDTO;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description //库存调拨 服务
 * @Author xuanyu.huang
 * @Date 11:45 上午 2020/4/21
 */
public interface WmsStockTransferService {

    WmsInstructionReturnDTO putInstruction(Long tenantId,WmsStockTransferHeadDTO wmsStockTransferHeadDTO);

    List<WmsStockTransferHeadDTO> queryHeadData(WmsStockTransferDTO dto, Long tenantId, PageRequest pageRequest);


    List<WmsStockTransferLineDTO> queryLineDetailByHeadId(String sourceInstructionId, Long tenantId, PageRequest pageRequest);

    List<WmsStockTransferLineDTO> listStockUpdateTransferLineForUi(String sourceInstructionId, Long tenantId);

    MtInstructionDoc save(WmsStockTransferSaveDTO dto, Long tenantId);


    List<WmsSiteDTO> getSite(Long tenantId);

    List<WmsWarehouseDTO> getWarehouse(Long tenantId, String siteId);

    List<WmsLocatorDTO> getLocator(Long tenantId, String locatorId);

    /**
     * 库存调拨-通过行ID查询条码信息
     *
     * @param tenantId      租户ID
     * @param instructionId 调拨单行ID
     * @param pageRequest   分页信息
     * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsStockTransferVO>
     * @author Deng Xu 2020/6/8 13:55
     */
    Page<WmsStockTransferVO> listMaterialLotForUi(Long tenantId, String instructionId, PageRequest pageRequest);

    /**
     * 库存调拨 删除调拨单行
     *
     * @param tenantId  租户ID
     * @param deleteDto 包含需要删除的行DTO集合*
     * @author Deng Xu 2020/6/8 15:17
     */
    void deleteLine(Long tenantId, WmsStockTransferSaveDTO.LineDTO deleteDto);

    List<WmsStockTransferHeadDTO> print(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDTOList);

    List<WmsStockTransferHeadDTO> hold(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList);

    List<WmsStockTransferHeadDTO> holdCancel(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList);

    List<WmsStockTransferHeadDTO> cancel(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList);

    List<WmsStockTransferHeadDTO> approval(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList);

    /**
     * 调拨单打印
     * @param tenantId 租户ID
     * @param instructionDocIdList 单据ID集合
     * @param response
     */
    void printPdf(Long tenantId, List<String>  instructionDocIdList, HttpServletResponse response);

}
