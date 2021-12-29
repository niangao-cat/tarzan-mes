package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 17:56
 */
@Data
public class WmsInstructionExecuteDTO implements Serializable {

    private static final long serialVersionUID = -6653428895210405621L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("单据号")
    private String instructionDocNum;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("来源仓库")
    private String fromLocatorId;
    @ApiModelProperty("目标仓库")
    private String toLocatorId;
    @ApiModelProperty("创建时间从")
    private String creationDateFrom;
    @ApiModelProperty("创建时间至")
    private String creationDateTo;
    @ApiModelProperty("物料组Id")
    private String itemGroupId;
    @ApiModelProperty("创建人")
    private String person;
    @ApiModelProperty("制单人")
    private String createdBy;

    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;
    @ApiModelProperty(value = "单据类型列表", hidden = true)
    @JsonIgnore
    private List<String> instructionDocTypeList;

    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialId) ? null : Arrays.asList(StringUtils.split(this.materialId, ","));
        this.instructionDocTypeList = StringUtils.isBlank(this.instructionDocType) ? null : Arrays.asList(StringUtils.split(this.instructionDocType, ","));
    }
}
