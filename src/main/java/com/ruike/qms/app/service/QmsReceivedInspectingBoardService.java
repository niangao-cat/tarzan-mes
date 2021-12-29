package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsReceivedQuantutyDTO;
import com.ruike.qms.api.dto.QmsRqAndItDTO;
import com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description:
 * @author: han.zhang
 * @create: 2020/04/29 14:41
 */
public interface QmsReceivedInspectingBoardService {
    /**
     * 已收待验看板查询卡片内容
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @return com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO
     * @author han.zhang 2020-04-30 10:18
     */
    Page<QmsSelectCardDataReturnDTO> selectCardData(Long tenantId, PageRequest pageRequest);

    /**
     * 查询30天内的已收待验数量
     *
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>
     * @author han.zhang 2020-04-30 11:57
     */
    List<QmsReceivedQuantutyDTO> selectReceivedQuantity(Long tenantId);

    /**
     * 查询全年(近12个月)的已接收待检验数量和待检时间
     *
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>
     * @author han.zhang 2020-04-30 15:01
     */
    QmsRqAndItDTO selectYearRqAndIt(Long tenantId);
}