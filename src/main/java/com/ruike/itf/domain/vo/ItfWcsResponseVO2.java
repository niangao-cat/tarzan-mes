package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/08/09 15:39
 */
@Data
public class ItfWcsResponseVO2 implements Serializable {

    private static final long serialVersionUID = 1912046390714080286L;

    @ApiModelProperty("header")
    private List<ItfLightTaskIfaceVO3> header;

    @ApiModelProperty("body")
    private Object body;
}
