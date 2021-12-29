package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 送货单扫的类型
 * @author: wenzhang.yu
 * @create: 2020/06/03 14:36
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsMaterialReturnScanLocatorDTO implements Serializable {

    private static final long serialVersionUID = -5567437168822147008L;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称")
    private String locatorName;
    @ApiModelProperty(value = "父层仓库id")
    private String firstLocatorId;


}