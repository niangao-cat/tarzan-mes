package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName HmeProdLineInfo
 * @Description 生产线信息
 * @Author lkj
 * @Date 2021/2/25
 */
@Data
@ApiModel("生产线信息")
public class HmeProdLineInfo {

    @ApiModelProperty("产线Id")
    private String ProdLineId;

    @ApiModelProperty("产线编码")
    private String ProdLineCode;

    @ApiModelProperty("产线名称")
    private String ProdLineName;

}
