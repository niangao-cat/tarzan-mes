package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 生产领退料单查询 : 查询条件区域字段
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/8 11:41
 */
@Data
public class WmsInstructionDocDTO {
    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "工单号")
    private String workOrderNumber;
    @ApiModelProperty(value = "物料编码")
    private String materialLotCode;
    @ApiModelProperty(value = "版本")
    private String materialVersion;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "目标仓库")
    private String locator;
    @ApiModelProperty(value = "执行人")
    private String lastUpdatedBy;
    @ApiModelProperty(value = "创建时间")
    private String creationDate;
    @ApiModelProperty(value = "创建时间")
    private String lastUpdateDate;
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "部门")
    private String department;



    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;
    @ApiModelProperty(value = "部门列表", hidden = true)
    @JsonIgnore
    private List<String> departmentList;
    @ApiModelProperty(value = "目标仓库Id列表", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;
    @ApiModelProperty(value = "单据状态列表", hidden = true)
    @JsonIgnore
    private List<String> instructionDocStatusList;

    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialLotCode) ? null : Arrays.asList(StringUtils.split(this.materialLotCode, ","));
        this.departmentList = StringUtils.isBlank(this.department) ? null : Arrays.asList(StringUtils.split(this.department, ","));
        this.locatorIdList = StringUtils.isBlank(this.locator) ? null : Arrays.asList(StringUtils.split(this.locator, ","));
        this.instructionDocStatusList = StringUtils.isBlank(this.instructionDocStatus) ? null : Arrays.asList(StringUtils.split(this.instructionDocStatus, ","));
    }
}
