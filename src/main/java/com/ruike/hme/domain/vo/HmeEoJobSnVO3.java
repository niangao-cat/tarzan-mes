package com.ruike.hme.domain.vo;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruike.hme.api.dto.HmeNcDisposePlatformDTO11;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
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
public class HmeEoJobSnVO3 implements Serializable {

    private static final long serialVersionUID = 1127444006562789476L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工位名称")
    private String workcellName;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("工艺ID列表")
    private List<String> operationIdList;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("班组ID")
    private String wkcShiftId;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("EO")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String eoNum;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("SN编码")
    private String snNum;
    @ApiModelProperty("主物料ID")
    private String snMaterialId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("预装数量")
    private BigDecimal prepareQty;
    @ApiModelProperty("工序作业容器ID")
    private String jobContainerId;
    @ApiModelProperty("作业平台类型")
    private String jobType;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("工艺步骤加工次数")
    private Integer eoStepNum;
    @ApiModelProperty("来源容器ID")
    private String sourceContainerId;
    @ApiModelProperty("来源作业ID")
    private String sourceJobId;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("查询返修标识")
    private String queryReworkFlag;
    @ApiModelProperty("当前步骤序号")
    private Long currentStepSequence;

    @ApiModelProperty("时效工序作业平台-SN类型")
    private String snType;
    @ApiModelProperty("时效工序作业平台-进炉/出炉扫描, IN代表进炉扫描，OUT代表出炉扫描")
    private String inOutType;
    @ApiModelProperty("时效工序作业平台-容器ID")
    private String containerId;
    @ApiModelProperty("时效工序作业平台-标准时长")
    private BigDecimal standardReqdTimeInProcess;
    @ApiModelProperty("时效工序作业平台-EO数量")
    private BigDecimal sumEoQty;
    @ApiModelProperty("时效工序作业平台-物料编码")
    private String materialCode;
    @ApiModelProperty("时效工序作业平台-物料名称")
    private String materialName;
    @ApiModelProperty("时效工序作业平台-进站操作人ID")
    private Long siteInBy;
    @ApiModelProperty("时效工序作业平台-进站操作人")
    private String siteInByName;
    @ApiModelProperty("时效工序作业平台-进站时间")
    private Date siteInDate;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;
    @ApiModelProperty("时效工序作业平台-EO行列表")
    private List<HmeEoJobSnVO3> snLineList;

    @ApiModelProperty("出站动作(返修、完工)")
    private String outSiteAction;
    @ApiModelProperty("是否继续")
    private String continueFlag;
    @ApiModelProperty("是否校验数据采集完成")
    private Boolean isDataRecordValidate;
    @ApiModelProperty("工序作业平台-SN作业绑定容器")
    private HmeEoJobContainerVO2 hmeEoJobContainerVO2;
    @ApiModelProperty("物料类型")
    private String materialType;

    private List<HmeEoJobMaterialVO> materialVOList;
    private List<HmeEoJobLotMaterialVO> lotMaterialVOList;
    private List<HmeEoJobTimeMaterialVO> timeMaterialVOList;
    private List<HmeEoJobDataRecordVO> dataRecordVOList;

    @ApiModelProperty("超量标识")
    private String overReleaseFlag;
    @ApiModelProperty("是否校验超量")
    private String checkOverReleaseFlag;

    @ApiModelProperty("是否校验不良判定")
    private String checkTagNcFlag;

    @ApiModelProperty("是否批量作业平台SN条码扫描")
    private String batchProcessSnScanFlag;

    @ApiModelProperty("虚拟号ID列表")
    private List<String> virtualIdList;
    @ApiModelProperty("条码类型")
    String codeType;

    @ApiModelProperty("是否记录实验代码")
    String isRecordLabCode;

    @ApiModelProperty("实验代码")
    String labCode;
    @ApiModelProperty("工单生产版本")
    private String woProductionVersion;
    @ApiModelProperty("物料类")
    private String itemType;

    @ApiModelProperty("全检(1)/抽检(2)")
    String inspection;

    @ApiModelProperty("错误编码")
    private String errorCode;

    @ApiModelProperty("工序不良集合")
    private List<HmeProcessNcDetailVO2> processNcDetailList;

    @ApiModelProperty("是否异常出站")
    private String isAbnormalOutSite;

    @ApiModelProperty("交叉复测标识")
    private String crossRetestFlag;

    @ApiModelProperty(value = "泵浦源作业平台标识")
    private String isPumpProcess;

    @ApiModelProperty("工序Id")
    private String processId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
