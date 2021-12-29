package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWoJobSnReturnDTO5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/1 15:19
 */
@Data
public class HmeNcDisposePlatformVO7 implements Serializable {

    private static final long serialVersionUID = -7454390065495045385L;

    @ApiModelProperty(value = "条码主键")
    private String materialLotId;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "良品数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "盒内总数")
    private BigDecimal totalQty;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "芯片类型")
    private String cosType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "列")
    private Long locationColumn;
    @ApiModelProperty(value = "行")
    private Long locationRow;
    private List<HmeWoJobSnReturnDTO5> loadLocationList;

}
