package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeNcCodeRouterRelHisVO extends HmeNcCodeRouterRelHis implements Serializable {
    private static final long serialVersionUID = 4221721185280518750L;

    @ApiModelProperty(value = "不良代码组编码")
    private String ncGroupCode;

    @ApiModelProperty(value = "不良代码组描述")
    private String ncGroupDescription;

    @ApiModelProperty(value = "不良代码编码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeDescription;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线描述")
    private String prodLineDescription;

    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;

    @ApiModelProperty(value = "事件创建人")
    private String loginName;

    @ApiModelProperty(value = "工艺名称")
    private String operationName;

    @ApiModelProperty(value = "工艺")
    private String operationId;
}
