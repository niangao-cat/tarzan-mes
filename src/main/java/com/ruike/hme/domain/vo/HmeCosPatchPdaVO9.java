package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeLoadJob;
import com.ruike.hme.domain.entity.HmeLoadJobObject;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chaonan.hu@hand-china.com 2021/10/27 11:44
 */
@Data
public class HmeCosPatchPdaVO9 implements Serializable {
    private static final long serialVersionUID = 7583061958859659995L;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验代码备注")
    private String remark;

    private String attrValue;

    @ApiModelProperty("工单ID")
    private String workOrderId;
}
