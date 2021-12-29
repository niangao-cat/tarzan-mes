package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/27 14:59
 */
@Data
public class HmeNcCheckDTO4 implements Serializable {

    private static final long serialVersionUID = -2792216740712641882L;

    @ApiModelProperty(value = "不良记录Id", required = true)
    private List<String> ncRecordIdList;

    @ApiModelProperty(value = "不良代码Id集合", required = true)
    private List<String> ncCodeIdList;

    @ApiModelProperty(value = "不良代码组", required = true)
    private String ncGroupId;

    @ApiModelProperty(value = "处理方法（1代表返修，2代表放行，3代表报废，4代表降级转型,5退库 6自制件返修,7工艺路线返修）", required = true)
    private String processMethod;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "转型物料Id")
    private String transitionMaterialId;

    @ApiModelProperty(value = "条码")
    private String barcode;

    @ApiModelProperty(value = "工艺路线")
    private String routerId;

    @ApiModelProperty(value = "处置返修方法Id")
    private String dispositionFunctionId;

    @ApiModelProperty(value = "返修记录")
    private String reworkRecordFlag;
}
