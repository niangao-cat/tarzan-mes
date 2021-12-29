package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import io.choerodon.core.domain.Page;
import org.hzero.core.base.AopProxy;
import tarzan.instruction.domain.vo.MtInstructionVO;

import java.util.List;

public interface WmsMiscOutHipsService extends AopProxy<WmsMiscOutHipsService> {

    String MATERIALLOT = "实物条码";
    String CONTAINER = "物流器具";
    String FLAGN = "N";
    String FLAGY = "Y";

    /**查询成本中心lov
     * @Description 查询成本中心lov
     * @param tenantId
     * @param page
     * @param pageSize
     * @param costCenterCode
     * @return io.choerodon.core.domain.Page<com.superlighting.hwms.api.controller.dto.CostCenterLovResponseDTO>
     * @Date 2019/9/25 19:18
     * @Created by {HuangYuBin}
     */
    Page<WmsCostCenterLovResponseDTO> costCenterLovQuery(Long tenantId, Integer page, Integer pageSize, String costCenterCode);

    /**扫描条码查询
     * @Description 扫描条码查询
     * @param tenantId
     * @param dto
     * @return com.superlighting.hwms.api.controller.dto.MiscOutBarcodeHipsResponseDTO
     * @Date 2019/9/26 8:46
     * @Created by {HuangYuBin}
     */
    WmsMiscOutBarcodeHipsResponseDTO getInfoBarcode(Long tenantId, WmsMiscOutBarcodeHipsRequestDTO dto);

    /**对缓存中的数据进行杂发
     * @Description 对缓存中的数据进行杂发
     * @param tenantId
     * @param dtoList
     * @return java.lang.String
     * @Date 2019/9/26 14:31
     * @Created by {HuangYuBin}
     */
    void miscOut(Long tenantId, List<WmsMiscOutHipsRequestDTO> dtoList, Boolean enableFlag);

    /**对处理后的数据循环调用API
     * @Description 对处理后的数据循环调用API
     * @param vo
     * @param materialLotList
     * @param eventRequestId
     * @param tenantId
     * @return void
     * @Date 2019/9/28 8:13
     * @Created by {HuangYuBin}
     */
    void miscOutApi(MtInstructionVO vo, List<WmsInstructionCreationDTO> materialLotList, String eventRequestId, Long tenantId, Boolean enableFlag);

    /**杂发功能前端缓存数据查询
     * @Description 杂发功能前端缓存数据查询
     * @param dtoList
     * @param search
     * @return com.superlighting.hwms.api.controller.dto.MiscOutTempDTO
     * @Date 2019/9/28 8:50
     * @Created by {HuangYuBin}
     */
    WmsMiscOutTempDTO miscOutTempQuery(List<WmsMiscOutTempDTO> dtoList, String search);
}
