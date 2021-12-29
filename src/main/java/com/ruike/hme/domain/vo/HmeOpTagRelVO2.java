package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/26 13:53
 */
@Data
public class HmeOpTagRelVO2 implements Serializable {

    private static final long serialVersionUID = 5655852646783608235L;

    @ApiModelProperty("主键")
    private String opTagRelId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "采集项ID")
    private String tagId;

    @ApiModelProperty(value = "采集项编码")
    private String tagCode;

    @ApiModelProperty(value = "采集项描述")
    private String tagName;

}
