package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 异常处理平台-异常清单内容VO
 *
 * @author Deng xu
 * @date 2020/6/22 14:11
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HmeExceptionRecordQueryVO implements Serializable {

    private static final long serialVersionUID = 8088121138742865279L;

    @ApiModelProperty("工序异常反馈记录ID")
    private String exceptionWkcRecordId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("异常名称")
    private String exceptionName;

    @ApiModelProperty("班次编码：班次日期月日-班次编码")
    private String shiftCode;

    @ApiModelProperty("异常等级")
    private String exceptionLevel;

    @ApiModelProperty("异常备注")
    private String exceptionRemark;

    @ApiModelProperty("异常创建时间")
    private String creationDate;

    @ApiModelProperty("异常处理时间")
    private String respondTime;

    @ApiModelProperty("异常关闭时间")
    private String closeTime;

    @ApiModelProperty("异常处理状态：NEW,UPGRADE,RESPOND,CLOSE")
    private String exceptionStatus;

    @ApiModelProperty("异常状态时间轴信息")
    private List<HmeExceptionStatusHistoryVO> statusHistoryList;

    @ApiModelProperty("异常ID")
    private String exceptionId;

    @ApiModelProperty("异常编码")
    private String exceptionCode;

    @ApiModelProperty("异常类型")
    private String exceptionType;

    @ApiModelProperty("异常组ID")
    private String exceptionGroupId;

    @ApiModelProperty("工位")
    private String workcellName;

    @ApiModelProperty("操作者")
    private String realName;

    @ApiModelProperty("设备编码")
    private String equipmentCode;

    @ApiModelProperty("附件ID")
    private String attachmentUuid;

    @ApiModelProperty("物料条码")
    private String materialLotCode;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("异常处理结果")
    private String respondRemark;

    @ApiModelProperty("组织类型")
    private String attribute1;
    @ApiModelProperty("组织类型ID")
    private String attribute2;

}
