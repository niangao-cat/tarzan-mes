package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsInspectionTimeDTO;
import com.ruike.qms.api.dto.QmsReceivedQuantutyDTO;
import com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 已收待检验看板mapper
 * @author: han.zhang
 * @create: 2020/04/30 11:47
 */
public interface QmsReceivedInspectingBoardMapper {
    /**
     * @Description 已收待验看板查询卡片内容
     * @param tenantId 租户id
     * @return com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO
     * @Date 2020-04-30 10:18
     * @Author han.zhang
     */
    List<QmsSelectCardDataReturnDTO> selectCardData(Long tenantId);

    /**
     * @Description 查询30天内的已收待验数量
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>
     * @Date 2020-04-30 11:57
     * @Author han.zhang
     */
    List<QmsReceivedQuantutyDTO> selectReceivedQuantity(Long tenantId);

    /**
     * @Description 查询全年(近12个月)的已接收待检验数量
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>
     * @Date 2020-04-30 15:01
     * @Author han.zhang
     */
    List<QmsReceivedQuantutyDTO> selectYearReceivedQuantity(Long tenantId);

    /**
     * @Description 查询全年(近12个月)的待检时长
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>
     * @Date 2020-04-30 15:01
     * @Author han.zhang
     */
    List<QmsInspectionTimeDTO> selectYearInspectionTime(Long tenantId);
}