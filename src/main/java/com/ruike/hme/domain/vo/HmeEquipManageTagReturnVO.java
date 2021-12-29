package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEqManageTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 设备管理项维护
 * @author: han.zhang
 * @create: 2020/06/11 11:29
 */
@Getter
@Setter
@ToString
public class HmeEquipManageTagReturnVO extends HmeEqManageTag implements Serializable {

    @ApiModelProperty(value = "周期")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_CYCLE", meaningField = "manageCycleMeaning")
    private String manageCycle;
    @ApiModelProperty(value = "周期含义")
    private String manageCycleMeaning;
    @ApiModelProperty(value = "数据类型含义")
    private String valueTypeMeaning;
    @ApiModelProperty(value = "收集方式含义")
    private String collectionMethodMeaning;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "保养提前周期")
    @LovValue(value = "HME.MAINTAIN_LEADTIME", meaningField = "maintainLeadtimeMeaning")
    private String maintainLeadtime;
    @ApiModelProperty(value = "保养提前周期含义")
    private String maintainLeadtimeMeaning;
    @ApiModelProperty(value = "符合值")
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    private String falseValue;

}