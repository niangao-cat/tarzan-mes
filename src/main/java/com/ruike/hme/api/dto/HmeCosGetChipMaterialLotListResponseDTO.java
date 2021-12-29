package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeCosGetChipMaterialLotListResponseDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 10:25
 **/
@Data
public class HmeCosGetChipMaterialLotListResponseDTO implements Serializable {
    private static final long serialVersionUID = 6362689225255755715L;

    @ApiModelProperty("物料批条码ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("来料数量")
    private Long primaryUomQty;

    @ApiModelProperty("SN数量")
    private Long snQty;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态标识")
    private String statusFlag;

    @ApiModelProperty("出站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteOutDate;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验代码备注")
    private String labRemark;
}
