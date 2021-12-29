package com.ruike.qms.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 查询全年已接收数量和待检时间
 * @author: han.zhang
 * @create: 2020/04/30 15:24
 */
@Getter
@Setter
@ToString
public class QmsRqAndItDTO implements Serializable {
    private static final long serialVersionUID = 8443437164682493662L;

    private List<QmsReceivedQuantutyDTO> receivedQuantutyDTOList;

    private List<QmsInspectionTimeDTO> inspectionTimeDTOList;
}