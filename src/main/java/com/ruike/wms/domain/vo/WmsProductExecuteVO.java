package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/16 13:07
 */
@Data
public class WmsProductExecuteVO implements Serializable {

    private static final long serialVersionUID = 2193103475279097244L;

    @ApiModelProperty(value = "行Id")
    private String instructionId;
    @ApiModelProperty(value = "报错信息")
    private String msg;
}
