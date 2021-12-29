package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtOperationDTO5 implements Serializable {
    private static final long serialVersionUID = -6796184519836072447L;

    @ApiModelProperty("工艺唯一标识")
    private String operationId;
    @ApiModelProperty(value = "站点标识", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "工艺名称", required = true)
    @NotBlank
    private String operationName;
    @ApiModelProperty(value = "工艺版本", required = true)
    @NotBlank
    private String revision;
    @ApiModelProperty(value = "Y/N，是否当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "有效时间从", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date dateFrom;
    @ApiModelProperty(value = "有效时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTo;
    @ApiModelProperty(value = "备注，有条件的话做成多行文本")
    private String description;
    @ApiModelProperty(value = "工艺状态（STATUS_GROUP:OPERATION_STATUS）", required = true)
    @NotBlank
    private String operationStatus;
    @ApiModelProperty(value = "工艺类型（TYPE_GROUP:OPERATION_TYPE）", required = true)
    @NotBlank
    private String operationType;
    @ApiModelProperty(value = "特殊工艺路线ID")
    private String specialRouterId;
    @ApiModelProperty(value = "工具类型")
    private String toolType;
    @ApiModelProperty(value = "工具")
    private String toolId;
    @ApiModelProperty(value = "工作单元类型")
    private String workcellType;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。")
    private String defaultNcId;
    @ApiModelProperty(value = "可以在此操作中处理一个 EO 编号的最大次数此字段不适用于已选中〖松散路线流〗复选框的路线上的车间作业控制编号。是路线上工艺 最大循环次数 的多语言")
    private Long standardMaxLoop;
    @ApiModelProperty(value = "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")
    private String standardSpecialIntroduction;
    @ApiModelProperty(value = "处理车间作业控制所需的占用时间或有效工作时间（按分钟计）")
    private Double standardReqdTimeInProcess;
    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;
    @ApiModelProperty(value = "工艺子步骤列表")
    private List<MtOperationSubstepDTO> mtOperationSubstepList = Collections.emptyList();
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("工艺扩展属性")
    private List<MtExtendAttrDTO3> operationAttrs;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        } else {
            return (Date) dateFrom.clone();
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo == null) {
            return null;
        } else {
            return (Date) dateTo.clone();
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getSpecialRouterId() {
        return specialRouterId;
    }

    public void setSpecialRouterId(String specialRouterId) {
        this.specialRouterId = specialRouterId;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getDefaultNcId() {
        return defaultNcId;
    }

    public void setDefaultNcId(String defaultNcId) {
        this.defaultNcId = defaultNcId;
    }

    public Long getStandardMaxLoop() {
        return standardMaxLoop;
    }

    public void setStandardMaxLoop(Long standardMaxLoop) {
        this.standardMaxLoop = standardMaxLoop;
    }

    public String getStandardSpecialIntroduction() {
        return standardSpecialIntroduction;
    }

    public void setStandardSpecialIntroduction(String standardSpecialIntroduction) {
        this.standardSpecialIntroduction = standardSpecialIntroduction;
    }

    public Double getStandardReqdTimeInProcess() {
        return standardReqdTimeInProcess;
    }

    public void setStandardReqdTimeInProcess(Double standardReqdTimeInProcess) {
        this.standardReqdTimeInProcess = standardReqdTimeInProcess;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public List<MtOperationSubstepDTO> getMtOperationSubstepList() {
        return mtOperationSubstepList;
    }

    public void setMtOperationSubstepList(List<MtOperationSubstepDTO> mtOperationSubstepList) {
        this.mtOperationSubstepList = mtOperationSubstepList;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getOperationAttrs() {
        return operationAttrs;
    }

    public void setOperationAttrs(List<MtExtendAttrDTO3> operationAttrs) {
        this.operationAttrs = operationAttrs;
    }
}
