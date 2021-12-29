package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeCompleteBoxingVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/01 10:24
 */
@Data
public class HmeEoJobSnVO5 implements Serializable {

    private static final long serialVersionUID = 6213925191290383753L;

    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("工单Id")
    private String workOrderId;
    @ApiModelProperty("工单编号")
    private String workOrderNum;
    @ApiModelProperty("工单数量")
    private BigDecimal woQty;
    @ApiModelProperty("工单货位Id")
    private String locatorId;
    @ApiModelProperty("完工数量")
    private BigDecimal completedQty;
    @ApiModelProperty("质量状态编码")
    private String qualityStatus;
    @ApiModelProperty("质量状态")
    private String qualityStatusMeaning;
    @ApiModelProperty("工单备注")
    private String remark;
    @ApiModelProperty("执行作业Id")
    private String eoId;
    @ApiModelProperty("执行作业数量")
    private BigDecimal eoQty;
    @ApiModelProperty("执行作业状态")
    private String status;
    @ApiModelProperty("前次EO状态")
    private String lastEoStatus;
    @ApiModelProperty("条码Id")
    private String materialLotId;
    @ApiModelProperty("条码Code")
    private String materialLotICode;
    @ApiModelProperty("当前容器ID")
    private String currentContainerId;
    @ApiModelProperty("条码CODE")
    private String materialLotCode;
    @ApiModelProperty("条码有效性")
    private String materialLotEnableFlag;
    @ApiModelProperty("条码返修标识")
    private String reworkFlag;
    @ApiModelProperty("生产线编码")
    private String prodLineCode;
    @ApiModelProperty("条码对应的物料的物料类型")
    private String itemType;
    @ApiModelProperty("工单生产版本")
    private String woProductionVersion;
    @ApiModelProperty("工艺步骤信息")
    private List<HmeRouterStepVO2> routerStepList;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("装配清单名称")
    private String bomName;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;
    @ApiModelProperty("工单类型")
    private String workOrderType;
    @ApiModelProperty("工单状态")
    private String workOrderStatus;

    @ApiModelProperty("在制标识")
    private String afFlag;

    @ApiModelProperty("盘点停用标识")
    private String stocktakeFlag;

    @ApiModelProperty("冻结标识")
    private String freezeFlag;
    @ApiModelProperty("器件测试标识")
    private String testFlag;
    @ApiModelProperty("判定交叉复测标识")
    private String crossRetestFlag;
    @ApiModelProperty("是否显示交叉复测")
    private String isShowCrossRetestBtn;
    @ApiModelProperty("批次")
    private String lot;
}