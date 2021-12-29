package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname ContainerService
 * @Description
 * @Date 2019/9/16 14:54
 * @Created by admin
 */
public interface WmsContainerService {
    String SUCCESS = "0";

    /**
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @Description 创建条码
     * @Date 2019/9/16 14:57
     * @Created by admin
     */
    String containerCreate(Long tenantId, WmsContainerDTO dto);

    /**
     * @param containerQryDTO
     * @return java.util.List<com.superlighting.hwms.api.controller.dto.ZMaterialLotQryResultDTO>
     * @Description 条件查询
     * @Date 2019/9/18 16:41
     * @Created by admin
     */
    Page<WmsContainerResultDTO> containerHeaderQuery(PageRequest pageRequest, WmsContainerQryDTO containerQryDTO);

    /***
     * @Description 行查询
     * @param pageRequest
     * @param containerQryDTO
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.WmsMaterialLotHisResultDTO>
     * @Date 2019/9/19 9:50
     * @Created by admin
     */
    Page<WmsContainerLineResultDTO> containerLineQuery(PageRequest pageRequest, WmsContainerQryDTO containerQryDTO);

    /**
     * @param containerHisQryDTO
     * @return java.util.List<com.superlighting.hwms.api.controller.dto.ZMaterialLotQryResultDTO>
     * @Description 头历史查询
     * @Date 2019/9/18 16:41
     * @Created by admin
     */
    Page<WmsContainerHisResultDTO> containerHeaderHis(PageRequest pageRequest, WmsContainerHisQryDTO containerHisQryDTO);

    /***
     * @Description 行历史查询
     * @param pageRequest
     * @param containerHisQryDTO
     * @return io.choerodon.core.domain.Page<com.superlighting.hwms.api.controller.dto.ZMaterialLotHisResultDTO>
     * @Date 2019/9/19 9:50
     * @Created by admin
     */
    Page<WmsContainerLineHisResultDTO> containerLineHis(PageRequest pageRequest, WmsContainerHisQryDTO containerHisQryDTO);

    /**
     * @param tenantId
     * @param containerCode
     * @return
     * @Description 物流器具条码查询
     * @Date 2019/9/25 14:22
     * @Created by lijinghua
     */
    WmsContainerInfoResultDTO containerCodeQuery(Long tenantId, String containerCode);

    /**
     * @param tenantId
     * @param type
     * @param materialLotCodeList
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @Created by yaoyapeng
     */
    void containerCodePrint(Long tenantId, String type, List<String> materialLotCodeList, HttpServletResponse response);
}
