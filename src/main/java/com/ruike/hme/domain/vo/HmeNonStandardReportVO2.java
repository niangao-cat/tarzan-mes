package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 17:17
 */
@Data
public class HmeNonStandardReportVO2 implements Serializable {

    private static final long serialVersionUID = -4438555298412095332L;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "车间")
    private String workshopName;

    @ApiModelProperty(value = "产线")
    private String prodLineName;

    @ApiModelProperty(value = "工单状态")
    @LovValue(lovCode = "MT.WO_STATUS", meaningField = "woStatusMeaning")
    private String woStatus;

    @ApiModelProperty(value = "工单状态含义")
    private String woStatusMeaning;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品料号ID")
    private String materialId;

    @ApiModelProperty(value = "产品料号")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "生产订单创建时间")
    private String creationDate;

    @ApiModelProperty(value = "生产订单下达时间")
    private String releaseDate;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "工单数量")
    private BigDecimal woQty;

    @ApiModelProperty(value = "待上线数量")
    private BigDecimal waitQty;

    @ApiModelProperty(value = "在线数量")
    private BigDecimal wipQty;

    @ApiModelProperty(value = "完成数量")
    private BigDecimal completedQty;

    @ApiModelProperty(value = "待入库")
    private BigDecimal notStock;

    @ApiModelProperty(value = "已入库")
    private BigDecimal inStock;

    @ApiModelProperty(value = "工单备注")
    private String remark;
}
