package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * COS工位加工异常汇总表 页面展示
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 15:34
 */
@Data
public class HmeCosQuantityVO implements Serializable {

    private static final long serialVersionUID = 5535650910212878088L;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("不良总数")
    private BigDecimal defectCountQuantity;

    @ApiModelProperty("工位编码")
    private String workcellCode;
}
