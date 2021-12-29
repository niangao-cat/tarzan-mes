package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO2 implements Serializable {
    private static final long serialVersionUID = -6907353057839154664L;

    @ApiModelProperty(value = "描述该物料批的唯一编码，用于方便识别")
    private List<String> materialLotCodeList;
    @ApiModelProperty(value = "该物料批所在生产站点的标识ID")
    private String siteId;
    @ApiModelProperty(value = "描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效")
    private String enableFlag;
}
