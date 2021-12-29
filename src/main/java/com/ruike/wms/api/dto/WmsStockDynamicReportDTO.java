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
 * @author li.zhang 2021/09/28 9:15
 */
@Data
public class WmsStockDynamicReportDTO implements Serializable {

    private static final long serialVersionUID = -6961584675629696381L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("物料组Id")
    private String itemGroupId;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("出入库类型")
    private String stockType;
    @ApiModelProperty("出入库时间从")
    private String startDate;
    @ApiModelProperty("出入库时间至")
    private String endDate;




    // 非前端传输参数

    @ApiModelProperty(value = "物料组ID列表", hidden = true)
    @JsonIgnore
    private List<String> itemGroupIdList;
    @ApiModelProperty(value = "仓库ID列表", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    public void initParam() {
        this.itemGroupIdList = StringUtils.isBlank(this.itemGroupId) ? null : Arrays.asList(StringUtils.split(this.itemGroupId, ","));
        this.locatorIdList = StringUtils.isBlank(this.warehouseId) ? null : Arrays.asList(StringUtils.split(this.warehouseId, ","));
    }
}
