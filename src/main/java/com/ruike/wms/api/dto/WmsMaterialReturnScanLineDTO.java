package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName WmsMaterialReturnScanDTO
 * @Deacription  成本中心退料单扫描条码行信息
 * @Author ywz
 * @Date 2020/4/20 10:18
 * @Version 1.0
 **/

@Data
public class WmsMaterialReturnScanLineDTO implements Serializable {

    private static final long serialVersionUID = -5149518739099489008L;

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;

    @ApiModelProperty(value = "单据行状态")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT_LINE.STATUS",meaningField="lineStatusMeaning",defaultMeaning = "无")
    private String instructionStatus;

    @ApiModelProperty(value = "单据行状态说明")
    private String lineStatusMeaning;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;

    @ApiModelProperty(value = "接收仓库ID")
    private String toWarehouseId;

    @ApiModelProperty(value = "接收仓库编码")
    private String toWarehouseCode;

    @ApiModelProperty(value = "接收货位ID")
    private String toLocatorId;

    @ApiModelProperty(value = "接收货位编码")
    private String toLocatorCode;

    @ApiModelProperty(value = "接收货位描述")
    private String toLocatorName;

    @ApiModelProperty(value = "物料单位")
    private String uomCode;

    @ApiModelProperty(value = "超发设置")
    private String excessSetting;

    @ApiModelProperty(value = "超发值")
    private String excessValue;

    @ApiModelProperty(value = "物料单位ID")
    private String uomId;

    @ApiModelProperty(value = "条码个数")
    private int codeQty;

    @ApiModelProperty(value = "免检/质检通过")
    private int needIqc;

    @ApiModelProperty(value = "推荐货位Code")
    private String recommendLocatorCode;

    @ApiModelProperty(value = "亮灯接口返回标识")
    private String status;
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "亮灯状态")
    private String taskStatus;
}
