package com.ruike.wms.api.dto;

import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MaterialGetReturnLineSavePageDTO
 * @Description 分页信息
 * @Date 2019/12/3 8:39
 * @author  by 许博思
 */
@ApiModel("新建领退料单据行信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnLineSavePageDTO {
    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "来源工厂")
    private String currentSite;
    @ApiModelProperty(value = "来源工厂Code")
    private String currentSiteCode;
    @ApiModelProperty(value = "目标工厂")
    private String targetSite;
    @ApiModelProperty(value = "目标工厂Code")
    private String targetSiteCode;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "来源仓库")
    private String currentWarehouse;
    @ApiModelProperty(value = "来源仓库Code")
    private String currentWarehouseCode;
    @ApiModelProperty(value = "目标仓库")
    private String targetWarehouse;
    @ApiModelProperty(value = "目标仓库Code")
    private String targetWarehouseCode;
    @ApiModelProperty(value = "单据类型")
    private String typeCode;
    @ApiModelProperty(value = "行信息列表")
    Page<Line> lineList;
    @ApiModelProperty(value = "成本中心ID")
    private String costCenterId;
    @ApiModelProperty(value = "成本中心Code")
    private String costCenterCode;
    @ApiModelProperty(value = "创建头信息时的事件组id（新建行时再传入）")
    private String eventRequestId;
    @ApiModelProperty(value = "创建头信息时的事件id（新建行时再传入）")
    private String eventId;
    @ApiModelProperty(value = "单据状态")
    private String status;
    @ApiModelProperty(value = "判断下达或取消下达")
    private Boolean signal;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "质量状态")
    private String returnQcFlag;
    @ApiModelProperty(value = "报废部门ID")
    private String scrapDepartment;
    @ApiModelProperty(value = "报废部门Code")
    private String scrapDepartmentCode;
    @ApiModel("行信息")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Line{
        @ApiModelProperty(value = "单据行ID")
        private String instructionId;
        @ApiModelProperty(value = "单据行号(序号)")
        private String instructionLineNum;
        @ApiModelProperty(value = "物料ID")
        private String materialId;
        @ApiModelProperty(value = "物料编号")
        private String materialCode;
        @ApiModelProperty(value = "单位ID")
        private String uomId;
        @ApiModelProperty(value = "单位Code")
        private String uomCode;
        @ApiModelProperty(value = "需求数")
        private Double demandQuantity;
        @ApiModelProperty(value = "目标仓库ID")
        private String targetWarehouse;
        @ApiModelProperty(value = "目标库位ID")
        private String targetLocator;
        @ApiModelProperty(value = "目标库位Code")
        private String targetLocatorCode;
        @ApiModelProperty(value = "来源库位ID")
        private String currentLocator;
        @ApiModelProperty(value = "来源库位Code")
        private String currentLocatorCode;
        @ApiModelProperty(value = "指定批次")
        private String lot;
        @ApiModelProperty(value = "等级编码")
        private String gradeCode;
        @ApiModelProperty(value = "备注")
        private String remark;
        @ApiModelProperty(value = "报废原因")
        private String scrapReason;
        @ApiModelProperty(value = "报废原因Code")
        private String scrapReasonCode;
        @ApiModelProperty(value = "物料描述")
        private String materialName;
    }
}