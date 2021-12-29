package com.ruike.itf.domain.vo;

import com.ruike.itf.api.dto.ItfExceptionDTO;
import com.ruike.itf.api.dto.ItfExceptionUserInfoDTO;
import com.ruike.itf.api.dto.ItfExceptionWkcRecordDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IftSendOAExceptionMsgVO {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("异常信息主键")
    private String excWkcRecordId;

    @ApiModelProperty("异常大项")
    private String exceptionType;

    @ApiModelProperty("异常编码")
    private String exceptionCode;

    @ApiModelProperty("异常小项")
    private String exceptionName;

    @ApiModelProperty("异常描述")
    private String exceptionRemark;

    @ApiModelProperty("工位")
    private String workcellName;

    @ApiModelProperty("当前时间")
    private String currentTime;

    @ApiModelProperty("当前创建时间")
    private String createTime;

    @ApiModelProperty("发起人")
    private String applicant;

    @ApiModelProperty("status")
    private String status;

    @ApiModelProperty("升级时长")
    private String upgradeTime;

    @ApiModelProperty("异常等级")
    private String exceptionLevel;

    @ApiModelProperty("组织信息")
    private String department;

}
