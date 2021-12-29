package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO implements Serializable {
    private static final long serialVersionUID = 8159896234508816641L;

    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("进站日期")
    private Date siteInDate;
    @ApiModelProperty("出站日期")
    private Date siteOutDate;
    @ApiModelProperty("节拍")
    private String meterTimeStr;
    @ApiModelProperty("班次ID")
    private String shiftId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("操作员ID")
    private Long siteInBy;
    @ApiModelProperty("操作员")
    private String siteInByName;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工位名称")
    private String workcellName;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("工单编码")
    private String workOrderNum;
    /**
     * SAP料ID
     */
    @ApiModelProperty("SN料ID")
    private String snMaterialId;
    /**
     * SAP料号
     */
    @ApiModelProperty("SN料号")
    private String snMaterialCode;
    /**
     * SAP料名称
     */
    @ApiModelProperty("SN料名称")
    private String snMaterialName;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("条码")
    private String snNum;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("工单总量")
    private BigDecimal woQuantity;
    @ApiModelProperty("已加工数量")
    private BigDecimal woQuantityOut;
    @ApiModelProperty("已预装数量")
    private BigDecimal woPreparedQty;

    @ApiModelProperty("质量状态编码")
    private String qualityStatus;
    @ApiModelProperty("质量状态")
    private String qualityStatusMeaning;
    @ApiModelProperty("工单备注")
    private String remark;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("工艺步骤加工次数")
    private Integer eoStepNum;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("来源容器ID")
    private String sourceContainerId;
    @ApiModelProperty("容器作业ID")
    private String jobContainerId;
    @ApiModelProperty("当前步骤序号")
    private Long currentStepSequence;
    @ApiModelProperty("当前步骤识别码")
    private String currentStepName;
    @ApiModelProperty("当前步骤识别码描述")
    private String currentStepDescription;
    @ApiModelProperty("下一步骤识别码")
    private String nextStepName;
    @ApiModelProperty("下一步骤识别码描述")
    private String nextStepDescription;
    @ApiModelProperty("多步骤(工艺)序作业")
    private List<HmeEoJobSn> hmeEoStepList;
    @ApiModelProperty("多步骤(次数)工序作业")
    private List<HmeEoJobSn> hmeEoJobSnList;
    @ApiModelProperty("是否可以加工完成")
    private String canProcessCompleteFlag;
    @ApiModelProperty("投料器具")
    private String sourceContainerCode;
    @ApiModelProperty("生产版本")
    private String productionVersion;

    private List<HmeEoJobMaterialVO> materialVOList;
    private List<HmeEoJobLotMaterialVO> lotMaterialVOList;
    private List<HmeEoJobTimeMaterialVO> timeMaterialVOList;
    private List<HmeEoJobDataRecordVO> dataRecordVOList;

    private Integer materialScanCount;
    private Integer materialScanAllCount;
    private Integer dataScanCount;
    private Integer dataScanAllCount;
    private Integer generalScanCount;
    private Integer generalScanAllCount;

    @ApiModelProperty("是否可以点击加工完成")
    private String isClickProcessComplete;

    @ApiModelProperty("禁止点击继续返修 Y:不允许点击继续返修")
    private String prohibitClickContinueReworkFlag;

    @ApiModelProperty("不良发起工位编码")
    private String ncRecordWorkcellCode;

    @ApiModelProperty("不良发起工位名称")
    private String ncRecordWorkcellName;
    @ApiModelProperty("是否容器进站")
    private Boolean isContainer;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("装配清单名称")
    private String bomName;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;

    @ApiModelProperty("完成步骤标识 Y:完成步骤 其他:非完成步骤")
    private String doneStepFlag;
    @ApiModelProperty(value = "入口步骤标识")
    private String entryStepFlag;
    @ApiModelProperty("实验代码")
    private String labCode;
    @ApiModelProperty("备注")
    private String routerStepRemark;

    @ApiModelProperty("数据采集全检(1)/抽检(2)")
    String inspection;

    @ApiModelProperty("是否可以点击抽检/全检")
    private String isClickInspectionBtn;

    @ApiModelProperty("是否显示异常出站按钮")
    private String isShowAbnormalOutBtn;

    @ApiModelProperty("器件测试标识")
    private String testFlag;

    @ApiModelProperty("是否显示交叉复测")
    private String isShowCrossRetestBtn;

    @ApiModelProperty("展示数据项弹框标识 Y-是 N-否")
    private String showTagModelFlag;

    @ApiModelProperty("弹窗消息")
    private String message;
}
