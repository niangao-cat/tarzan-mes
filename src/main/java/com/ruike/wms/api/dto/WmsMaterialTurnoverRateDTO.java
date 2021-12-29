package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/27 17:03
 */
@Data
public class WmsMaterialTurnoverRateDTO implements Serializable {

    private static final long serialVersionUID = -7034468914820457438L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("开始时间")
    private String startDate;
    @ApiModelProperty("结束时间")
    private String endDate;




    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;
    @ApiModelProperty(value = "仓库ID列表", hidden = true)
    @JsonIgnore
    private List<String> warehouseIdList;
    @ApiModelProperty(value = "货位ID列表", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialId) ? null : Arrays.asList(StringUtils.split(this.materialId, ","));
        this.warehouseIdList = StringUtils.isBlank(this.warehouseId) ? null : Arrays.asList(StringUtils.split(this.warehouseId, ","));
        this.locatorIdList = StringUtils.isBlank(this.locatorId) ? null : Arrays.asList(StringUtils.split(this.locatorId, ","));
    }
}
