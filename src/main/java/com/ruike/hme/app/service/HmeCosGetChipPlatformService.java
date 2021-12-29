package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipPlatformService
 * @Description COS取片平台
 * @Date 2020/8/18 10:26
 * @Author yuchao.wang
 */
public interface HmeCosGetChipPlatformService {

    /**
     *
     * @Description 待取片容器进站条码扫描
     *
     * @author yuchao.wang
     * @date 2020/8/18 10:35
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.lang.Object
     *
     */
    HmeCosGetChipScanBarcodeResponseDTO scanBarcode(Long tenantId, HmeCosGetChipScanBarcodeDTO dto);

    /**
     *
     * @Description 进站确认
     *
     * @author yuchao.wang
     * @date 2020/8/18 19:50
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void fetchInConfirm(Long tenantId, HmeCosGetChipSiteInConfirmDTO dto);

    /**
     *
     * @Description 出站确认
     *
     * @author yuchao.wang
     * @date 2020/8/18 20:12
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    List<HmeCosGetChipSiteOutConfirmResponseDTO> fetchOutCreated(Long tenantId, HmeCosGetChipSiteOutConfirmDTO dto);

    /**
     *
     * @Description 出站查询
     *
     * @author yuchao.wang
     * @date 2020/8/19 16:01
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipSiteOutConfirmResponseDTO>
     *
     */
    HmeCosGetChipProcessingResponseDTO queryProcessing(Long tenantId, HmeCosGetChipSiteOutQueryDTO dto);

    /**
     *
     * @Description 出站打印
     *
     * @author yuchao.wang
     * @date 2020/8/19 19:16
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.lang.String
     *
     */
    List<String> fetchOut(Long tenantId, HmeCosGetChipSiteOutPrintDTO dto, HttpServletResponse response);

    /**
     *
     * @Description 输出PDF文件流,每个物料批为一页
     *
     * @author yuchao.wang
     * @date 2020/8/25 18:55
     * @param tenantId
     * @param materialLotId
     * @param response
     * @return void
     *
     */
    void printPdf(Long tenantId, List<String>  materialLotId, HttpServletResponse response);

    /**
     * 
     * @Description 查询容器对应最大装载数量
     * 
     * @author yuchao.wang
     * @date 2020/9/8 10:29
     * @param tenantId 租户ID
     * @param dto 参数
     * 
     */
    HmeCosGetChipMaxLoadDTO queryMaxLoadNumber(Long tenantId, HmeCosGetChipMaxLoadDTO dto);

    /**
     * 批量删除
     * 
     * @param tenantId 租户ID
     * @param dto 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 10:29:47 
     * @return com.ruike.hme.api.dto.HmeCosGetChipDeleteDTO
     */
    HmeCosGetChipDeleteDTO batchDelete(Long tenantId, HmeCosGetChipDeleteDTO dto);

    /**
     * 投入条码列表查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 11:50:47 
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipMaterialLotListResponseDTO>
     */
    List<HmeCosGetChipMaterialLotListResponseDTO> queryInputMaterialLotList(Long tenantId, HmeCosGetChipMaterialLotListDTO dto);

    /**
     * 根据条码查询芯片不良列表
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 15:26:17
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipNcListDTO>
     */
    List<HmeCosGetChipNcListDTO> queryNcList(Long tenantId, String materialLotId);
}
