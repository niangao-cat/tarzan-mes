package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnReworkVO2 implements Serializable {
    private static final long serialVersionUID = 8358216231707432479L;
    @ApiModelProperty(value = "条码")
    private HmeEoJobSnReworkVO3 materialLot;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "序列物料作业ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "条码扩展属性")
    private Map<String,String> materialLotExtendAttrMap;
    @ApiModelProperty(value = "当前条码物料匹配的组件")
    HmeEoJobSnBatchVO4 component;

    @ApiModelProperty(value = "删除标识，为Y时代表要删除物料，需弹窗确认，为N则不需弹窗")
    private String deleteFlag;
}
