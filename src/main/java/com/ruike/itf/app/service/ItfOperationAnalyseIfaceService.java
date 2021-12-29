package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfOperationAnalyseDTO;

import java.util.List;

/**
 * 工艺质量分析接口服务
 *
 * @author xin.t@raycuslaser.com 2021/10/25 19:06
 */
public interface ItfOperationAnalyseIfaceService {

    /**
     * 查询不良信息
     *
     * @param tenantId    租户ID
     * @param dto SN+工序
     * @return com.ruike.itf.api.dto.ItfOperationAnalyseDTO
     * @author xin.t@raycuslaser.com
     * @date 2021/10/25 19:06
     */
    ItfOperationAnalyseDTO invokeA(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto);

    /**
     * 查询SN投料信息
     *
     * @param tenantId    租户ID
     * @param dto SN+工序 / 时间区间+工序
     * @return com.ruike.itf.api.dto.ItfOperationAnalyseDTO.ReturnDTO
     * @author xin.t@raycuslaser.com
     * @date 2021/11/2 19:06
     */
    ItfOperationAnalyseDTO.ReturnDTO1 invokeB(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto);

    /**
     * 查询工序的工艺质量信息
     * @param tenantId
     * @param dto
     * @return
     */
    ItfOperationAnalyseDTO.ReturnDTO2 invokeC(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto);

    /**
     * 良率统计
     * @param tenantId
     * @param dto
     * @return
     */
    ItfOperationAnalyseDTO.AcceptedRate invokeD(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto);
}
