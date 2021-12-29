package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnReturnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/12 19:01
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -4704992320107978764L;
    @ApiModelProperty(value = "工单来料芯片数")
    private String incomingQty;

    @ApiModelProperty("剩余数量")
    private String remainingQty;

    @ApiModelProperty("芯片类型")
    @LovValue(value = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("芯片类型含义")
    private String cosTypeMeaning;
}
