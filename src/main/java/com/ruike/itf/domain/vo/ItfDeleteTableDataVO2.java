package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItfDeleteTableDataVO2 implements Serializable {
    private static final long serialVersionUID = 164868089804692196L;

    public ItfDeleteTableDataVO2(String keyId , String filed1, String filed2, String filed3, Long filed4, Long filed5, Long filed6){
        this.keyId = keyId;
        this.filed1 = filed1;
        this.filed2 = filed2;
        this.filed3 = filed3;
        this.filed4 = filed4;
        this.filed5 = filed5;
        this.filed6 = filed6;
    }

    @ApiModelProperty("主键ID")
    private String keyId;

    @ApiModelProperty("字段1")
    private String filed1;

    @ApiModelProperty("字段2")
    private String filed2;

    @ApiModelProperty("字段3")
    private String filed3;

    @ApiModelProperty("字段4")
    private Long filed4;

    @ApiModelProperty("字段5")
    private Long filed5;

    @ApiModelProperty("字段6")
    private Long filed6;
}
