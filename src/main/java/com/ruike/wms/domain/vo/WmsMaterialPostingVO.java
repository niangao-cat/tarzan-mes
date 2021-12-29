package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsMaterialPostingVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 11:57
 */
@Data
public class WmsMaterialPostingVO implements Serializable {

    private static final long serialVersionUID = -2430888839932072353L;

    @ApiModelProperty("送货单")
    private String instructionDocNum;
    @ApiModelProperty("送货单状态列表")
    private List<String> instructionDocStatus;
    @ApiModelProperty("送货单行状态列表")
    private List<String> instructionStatus;
    @ApiModelProperty("物料ID列表")
    private List<String> materialId;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("工厂")
    private String siteId;
    @ApiModelProperty("供应商")
    private String supplierId;
    @ApiModelProperty("接收仓库")
    private String toLocatorId;
    @ApiModelProperty("检验单")
    private String iqcNumber;
    @ApiModelProperty("检验单类型列表")
    private List<String> inspectionType;
    @ApiModelProperty("检验单状态列表")
    private List<String> inspectionStatus;

    @ApiModelProperty("接收完成时间从")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date actualReceivedDateFrom;

    @ApiModelProperty("接收完成时间至")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date actualReceivedDateTo;

    @ApiModelProperty("检验完成时间从")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date inspectionFinishDateFrom;

    @ApiModelProperty("检验完成时间至")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date inspectionFinishDateTo;

    @ApiModelProperty("指令ID列表")
    private List<String> instructionIds;
}
