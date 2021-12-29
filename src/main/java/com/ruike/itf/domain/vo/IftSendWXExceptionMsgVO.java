package com.ruike.itf.domain.vo;

import com.ruike.itf.api.dto.ItfExceptionDTO;
import com.ruike.itf.api.dto.ItfExceptionUserInfoDTO;
import com.ruike.itf.api.dto.ItfExceptionWkcRecordDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IftSendWXExceptionMsgVO{

    @ApiModelProperty("主键")
    String exceptionWkcRecordId;

    @ApiModelProperty("异常大项")
    String exceptionType;

    @ApiModelProperty("异常编码")
    String exceptionCode;

    @ApiModelProperty("异常小项")
    String exceptionName;

    @ApiModelProperty("异常描述")
    String exceptionRemark;

    @ApiModelProperty("工位")
    String workcellName;

    @ApiModelProperty("当前时间")
    String currentTime;
    @ApiModelProperty("发起人")
    ItfExceptionUserInfoDTO applicant;
    @ApiModelProperty("审批人")
    ItfExceptionDTO approvedBy;

    public IftSendWXExceptionMsgVO(ItfExceptionWkcRecordDTO dto) {
        this.exceptionWkcRecordId = dto.getExceptionWkcRecordId();
        this.exceptionType = dto.getExceptionType();
        this.exceptionCode = dto.getExceptionCode();
        this.exceptionName = dto.getExceptionName();
        this.exceptionRemark = dto.getExceptionRemark();
        this.workcellName = dto.getWorkcellName();
        this.currentTime = dto.getCurrentTime();
        this.applicant = dto.getInitiator();
    }
}
