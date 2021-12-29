package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/01/21 11:04
 */
@Data
public class HmeProcessNcLineVO implements Serializable {

    private static final long serialVersionUID = -297978580600109518L;

    @ApiModelProperty("头表ID")
    private String headerId;

    @ApiModelProperty("行表ID")
    private String lineId;

    @ApiModelProperty("数据项ID")
    private String tagId;

    @ApiModelProperty("数据项编码")
    private String tagCode;

    @ApiModelProperty("数据项描述")
    private String tagDescription;

    @ApiModelProperty("数据组ID")
    private String tagGroupId;

    @ApiModelProperty("数据组编码")
    private String tagGroupCode;

    @ApiModelProperty("数据组描述")
    private String tagGroupDescription;

    @ApiModelProperty("优先级")
    private String priority;

    @ApiModelProperty("标准编码")
    private String standardCode;


}
