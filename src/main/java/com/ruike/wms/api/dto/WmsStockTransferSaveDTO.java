package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Description  DTO
 * @Author xuanyu.huang
 * @Date 11:55 上午 2020/4/21
 */
@ApiModel("调拨平台 保存DTO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsStockTransferSaveDTO implements Serializable {

    private static final long serialVersionUID = -2279117314797306296L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户id")
    private Long personId;

    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "行list")
    private List<LineDTO> lineDTOList;


    @ApiModel("调拨平台 保存行信息")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class LineDTO {

        @ApiModelProperty(value = "单据头id")
        private String sourceDocId;

        @ApiModelProperty(value = "行号")
        private Long instructionLineNum;

        @ApiModelProperty(value = "单据行id")
        private String instructionId;

        @ApiModelProperty(value = "单据号")
        private String instructionNum;

        @ApiModelProperty(value = "单据状态")
        private String instructionStatus;

        @ApiModelProperty(value = "工厂id")
        private String siteId;

        @ApiModelProperty(value = "物料id")
        private String materialId;

        @ApiModelProperty(value = "单位")
        private String uomId;
        @ApiModelProperty(value = "制单数量")
        private Double quantity;

        @ApiModelProperty(value = "来源工厂")
        private String fromSiteId;

        @ApiModelProperty("来源货位")
        private String fromLocatorId;

        @ApiModelProperty(value = "目标工厂")
        private String toSiteId;

        @ApiModelProperty(value = "目标货位")
        private String toLocatorId;

        @ApiModelProperty(value = "目标仓库")
        private String toWarehouseId;

        @ApiModelProperty(value = "来源仓库")
        private String fromWarehouseId;

        @ApiModelProperty(value = "物料版本")
        private String materialVersion;

        @ApiModelProperty(value = "超发设置")
        @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
        private String excessSetting;

        @ApiModelProperty(value = "超发设置含义")
        private String excessSettingMeaning;

        @ApiModelProperty(value = "超发值")
        private String excessValue;


    }

}
