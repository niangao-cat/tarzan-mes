package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 异常信息查看报表返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 15:03
 */
@Data
public class HmeExceptionReportVO2 implements Serializable {

    private static final long serialVersionUID = -8609606311354243584L;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "制造部")
    private String areaName;

    private String areaId;

    @ApiModelProperty(value = "车间")
    private String workshopName;

    private String workshopId;

    @ApiModelProperty(value = "产线")
    private String prodLineName;

    private String prodLineId;

    @ApiModelProperty(value = "工位")
    private String workcellName;

    @ApiModelProperty(value = "班组")
    private String shiftName;

    private String shiftId;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "异常产品所属工单")
    private String workOrderNum;

    @ApiModelProperty(value = "异常产品序列号")
    private String identification;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "异常类型描述")
    private String exceptionTypeName;

    @ApiModelProperty(value = "异常描述")
    private String exceptionName;

    @ApiModelProperty(value = "异常物料")
    private String materialName;

    @ApiModelProperty(value = "异常物料条码")
    private String materialLotCode;

    @ApiModelProperty(value = "异常设备")
    private String assetEncoding;

    @ApiModelProperty(value = "异常备注")
    private String exceptionRemark;

    @ApiModelProperty(value = "异常状态")
    private String exceptionStatus;

    @ApiModelProperty(value = "异常状态描述")
    private String exceptionStatusName;

    @ApiModelProperty(value = "异常等级")
    private String exceptionLevel;

    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    private String createdBy;

    @ApiModelProperty(value = "发起人")
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发起时间")
    private Date creationDate;

    private String respondedBy;

    @ApiModelProperty(value = "响应人")
    private String respondedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "响应时间")
    private Date respondTime;

    @ApiModelProperty(value = "响应备注")
    private String respondRemark;

    private String closedBy;

    @ApiModelProperty(value = "关闭人")
    private String closedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;

    @ApiModelProperty(value = "文件列表")
    private List<HmeHzeroFileDTO> fileList;

    @ApiModelProperty("组织类型")
    private String organizationType;

    @ApiModelProperty("组织主键")
    private String organizationId;
}
