package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 领退料新增参数
 * @author: han.zhang
 * @create: 2020/04/17 11:54
 */
@Getter
@Setter
@ToString
public class WmsPickReturnAddDTO {

    @ApiModelProperty(value = "单据头id，新增不传 更新传")
    private String instructionDocId;

    @ApiModelProperty(value = "单据号")
    @NotNull
    private String instructionDocNum;

    @ApiModelProperty(value = "状态")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据类型")
    @NotNull
    private String instructionDocType;

    @ApiModelProperty(value = "工厂ID")
    @NotNull
    private String siteId;

    @ApiModelProperty(value = "成本中心ID")
    @NotNull
    private String costCenterId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "报废部门")
    private String scrapDepartment;

    @ApiModelProperty(value = "领退料行数据")
    private List<LineAddDTO> lineAddDtoS;

    @ApiModelProperty(value = "结算类型")
    private String settleAccounts;

    @ApiModelProperty(value = "内部订单Id")
    private String internalOrderId;

    @ApiModelProperty(value = "移动类型")
    private String moveType;

    @ApiModelProperty(value = "利润中心")
    private String profitCenter;

    @ApiModelProperty(value = "移动原因")
    private String moveReason;

    @ApiModelProperty(value = "内部订单类型")
    private String internalOrderType;

    @ApiModelProperty(value = "成本中心类型")
    private String costCenterType;

    @ApiModelProperty(value = "费用类型")
    private String costType;

    @Getter
    @Setter
    @ToString
    public static class LineAddDTO{

        @ApiModelProperty(value = "行Id,更新传，新增不传")
        private String instructionId;

        @ApiModelProperty(value = "行号")
        @NotNull
        private String instructionLineNum;

        @ApiModelProperty(value = "行状态")
        @NotNull
        private String instructionStatus;

        @ApiModelProperty(value = "物料ID")
        @NotNull
        private String materialId;

        @ApiModelProperty(value = "物料版本")
        @NotNull
        private String materialVersion;

        @ApiModelProperty(value = "单位ID")
        @NotNull
        private String uomId;

        @ApiModelProperty(value = "输入的数量")
        @NotNull
        private BigDecimal quantity;

        @ApiModelProperty(value = "货位ID")
        @NotNull
        private String toLocatorId;

        @ApiModelProperty(value = "仓库id")
        @NotNull
        private String toStorageId;

        @ApiModelProperty(value = "行备注")
        private String remark;

        @ApiModelProperty(value = "超发设置")
        private String excessSetting;

        @ApiModelProperty(value = "超发值")
        private String excessValue;
    }
}