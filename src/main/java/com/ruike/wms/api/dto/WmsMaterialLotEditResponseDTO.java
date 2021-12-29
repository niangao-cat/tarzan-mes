package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditDTO
 * @Description 条码调整查询结果实体类
 * @Date 2020/03/17 08:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WmsMaterialLotEditResponseDTO {

    @ApiModelProperty("实物标签Id")
    private String materialLotId;
    @ApiModelProperty("实物标签")
    private String materialLotCode;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("是否有效")
    private String enableFlagMeaning;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("状态")
    private String statusMeaning;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("质量状态")
    private String qualityStatusMeaning;
    @ApiModelProperty("物料编码")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("启动时间")
    private String enableDate;
    @ApiModelProperty("截止时间")
    private String deadlineDate;
    @ApiModelProperty("物料有效性")
    private String materialEnableFlag;
    @ApiModelProperty("数量")
    private Double primaryUomQty;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("工厂")
    private String siteCode;
    @ApiModelProperty("工厂名称")
    private String siteName;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("仓库")
    private String warehouseCode;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位")
    private String locatorCode;
    @ApiModelProperty("等级编码")
    private String gradeCode;
    @ApiModelProperty("指令Id")
    private String instructionId;
    @ApiModelProperty("生产日期")
    private String productDate;
    @ApiModelProperty("超期检验日期")
    private String overdueInspectionDate;
    @ApiModelProperty("工单发料时间")
    private String woIssueDate;
    @ApiModelProperty("色温bin")
    private String colorBin;
    @ApiModelProperty("亮度bin")
    private String lightBin;
    @ApiModelProperty("电压bin")
    private String voltageBin;
    @ApiModelProperty("不干胶号")
    private String stickerNumber;
    @ApiModelProperty("印字内容")
    private String printing;
    @ApiModelProperty("湿敏等级(HUMIDITY_LEVEL由此拓展字段值映射得出)")
    private String msl;
    @ApiModelProperty("膨胀系数")
    private String expansionCoefficients;
    @ApiModelProperty("采购订单号")
    private String poNum;
    @ApiModelProperty("采购订单行号")
    private String poLineNum;
    @ApiModelProperty("采购订单发运行号")
    private String poLineLocationNum;
    @ApiModelProperty("销售订单头号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("WBS元素")
    private String wbsNum;
    @ApiModelProperty("容器条码")
    private String containerId;
    @ApiModelProperty("容器条码")
    private String containerCode;
    @ApiModelProperty("供应商Id")
    private String supplierId;
    @ApiModelProperty("供应商编码")
    private String supplierCode;
    @ApiModelProperty("供应商描述")
    private String supplierName;
    @ApiModelProperty("预留工单号")
    private String reservedObjectId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("实验代码")
    private String labCode;
    @ApiModelProperty("实验代码备注")
    private String labRemark;

}
