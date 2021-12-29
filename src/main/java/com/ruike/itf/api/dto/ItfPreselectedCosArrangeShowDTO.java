package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @description COS芯片归集接口返回参数 - 芯片列表
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/26
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosArrangeShowDTO implements Serializable {
    private static final long serialVersionUID = -420743419059510923L;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "行")
    private Long loadRow;

    @ApiModelProperty(value = "列")
    private Long loadColumn;

    @ApiModelProperty(value = "位置")
    private String load;

    @ApiModelProperty(value = "热沉编号")
    private String hotSinkCode;

    @ApiModelProperty(value = "来源盒子号")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "来源行")
    private Long sourceLoadRow;

    @ApiModelProperty(value = "来源列")
    private Long sourceLoadColumn;

    @ApiModelProperty(value = "旧位置")
    private String oldLoad;
}
