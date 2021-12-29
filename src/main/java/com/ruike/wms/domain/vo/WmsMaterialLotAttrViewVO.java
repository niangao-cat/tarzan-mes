package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/4/20 15:38
 * @Description:
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WmsMaterialLotAttrViewVO implements Serializable {
    private static final long serialVersionUID = -7859552030607577860L;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("等级编码")
    private String gradeCode;
    private String originalId;
    @ApiModelProperty("采购订单号")
    private String poNum;
    @ApiModelProperty("采购订单行号")
    private String poLineNum;
    private String warehouseId;
    @ApiModelProperty("生产日期")
    private String productDate;
    private String instructionDocNum;
    private String instructionLineNum;
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
    @ApiModelProperty("采购订单发运行号")
    private String poLineLocationNum;
    @ApiModelProperty("销售订单头号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    private String printTime;
    private String printReason;
    private String humidityLevel;
    @ApiModelProperty("印字内容")
    private String printing;
    @ApiModelProperty("膨胀系数")
    private String expansionCoefficients;
    @ApiModelProperty("指令Id")
    private String instructionId;
    @ApiModelProperty("不干胶号")
    private String stickerNumber;
    private String workorderId;
    private String reviewResult;
    private String approvalResult;
    private String approvalOpinion;
    private String approvalNote;
    private String solderGlueStatus;
    private String statusDateTime;
    private String recipientsProdLine;
    private String recipientsCounts;
    private String returnOuttime;
    private String returnEmptyBottle;
    private String executorId;
    private String costcenterCode;
    private String enteredBy;
    private String enteringDate;
    private String scrapReason;
    private String receiptDate;
    private String deliveryBatch;
    private String qualityBatch;
    private String instockBy;
    private String qmFlag;
    private String asnNum;
    private String asnLineNum;
    private String qcCompleteTime;
    private String qualityDisposalStrategy;
    @ApiModelProperty("湿敏等级(HUMIDITY_LEVEL由此拓展字段值映射得出)")
    private String msl;
    private String wbsNum;
    private String remark;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("启动时间")
    private String enableDate;
    @ApiModelProperty("截止时间")
    private String deadlineDate;
    @ApiModelProperty("实验代码")
    private String labCode;
    @ApiModelProperty("实验代码备注")
    private String labRemark;
}
