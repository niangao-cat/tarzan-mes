package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * description 	铭牌打印配置属性维护显示字段
 *
 * @author wengang.qiang@hand-china.com 2021/10/12 11:18
 */
@Data
public class HmeNameplatePrintRelHeaderVO implements Serializable {

    private static final long serialVersionUID = 3966139983197293448L;

    @ApiModelProperty("主键ID")
    private String nameplateHeaderId;

    @ApiModelProperty(value = "类型", required = true)
    @LovValue(lovCode = "HME.IDENTIFYING_CODE_TYPE", meaningField = "typeMeaning")
    private String type;

    @ApiModelProperty(value = "序列")
    private Integer nameplateOrder;

    @ApiModelProperty(value = "内部识别码", required = true)
    private String identifyingCode;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;

    @ApiModelProperty(value = "类型描述")
    private String typeMeaning;
}
