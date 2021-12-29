package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投料记录参数
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO6 implements Serializable {

    private static final long serialVersionUID = -5035538972425603059L;

    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "工单BOM ID")
    private String bomId;

}
