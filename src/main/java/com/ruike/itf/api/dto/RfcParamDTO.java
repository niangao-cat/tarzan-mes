package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class RfcParamDTO {

    @ApiModelProperty(value = "SAP工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "开始日期")
    private String dateFrom;
    @ApiModelProperty(value = "结束日期")
    private String dateTo;

}
