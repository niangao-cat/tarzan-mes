package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 熔接机数据接口接收返回DTO
 * 防止被恶意攻击
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com
 * @date 2020/7/13 6:50 下午
 */
@Data
public class FsmCollectItfDTO {
    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "熔接点")
    private Integer fsmFusionSpot;
    @ApiModelProperty(value = "熔接时间(yyyy-MM-dd HH:mm:ss)")
    private Date fsmFusionTime;
    @ApiModelProperty(value = "放电功率")
    private BigDecimal fsmDischargePower;
    @ApiModelProperty(value = "放电次数")
    private Integer fsmDischargeTimes;
    @ApiModelProperty(value = "熔接损耗")
    private String fsmFusionLoss;
    @ApiModelProperty(value = "左光纤切割角")
    private BigDecimal fsmCutangleLeft;
    @ApiModelProperty(value = "右光纤切割角")
    private BigDecimal fsmCutangleRight;
    @ApiModelProperty(value = "轴向偏移")
    private BigDecimal fsmAxialOffset;
    @ApiModelProperty(value = "光纤端面间隔")
    private BigDecimal fsmFiberSpace;
    @ApiModelProperty(value = "左光纤端面")
    private String fsmFiberLeft;
    @ApiModelProperty(value = "右光纤端面")
    private String fsmFiberRight;
    @ApiModelProperty(value = "L-R角")
    private BigDecimal fsmLR;
    @ApiModelProperty(value = "异常报警信息")
    private String fsmErrMsg;

    @ApiModelProperty(value = "")
    private String fsmAttribute1;
    @ApiModelProperty(value = "")
    private String fsmAttribute2;
    @ApiModelProperty(value = "")
    private String fsmAttribute3;
    @ApiModelProperty(value = "")
    private String fsmAttribute4;
    @ApiModelProperty(value = "")
    private String fsmAttribute5;
    @ApiModelProperty(value = "")
    private String fsmAttribute6;
    @ApiModelProperty(value = "")
    private String fsmAttribute7;
    @ApiModelProperty(value = "")
    private String fsmAttribute8;
    @ApiModelProperty(value = "")
    private String fsmAttribute9;
    @ApiModelProperty(value = "")
    private String fsmAttribute10;
    @ApiModelProperty(value = "")
    private String fsmAttribute11;
    @ApiModelProperty(value = "")
    private String fsmAttribute12;
    @ApiModelProperty(value = "")
    private String fsmAttribute13;
    @ApiModelProperty(value = "")
    private String fsmAttribute14;
    @ApiModelProperty(value = "")
    private String fsmAttribute15;
}
