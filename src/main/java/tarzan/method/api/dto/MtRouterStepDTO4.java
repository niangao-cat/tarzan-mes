package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterStepDTO4 implements Serializable {
    private static final long serialVersionUID = 1958218720993895938L;

    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty("工艺路线唯一标识")
    private String routerId;
    @ApiModelProperty(value = "大致的执行顺序，实际的顺序看ROUTER_NEXT_STEP")
    @NotNull
    private Long sequence;
    @ApiModelProperty(value = "步骤识别码")
    @NotBlank
    private String stepName;
    @ApiModelProperty(value = "步骤类型：TYPE_GROUP:ROUTER_STEP_TYPE")
    @NotBlank
    private String routerStepType;
    @ApiModelProperty(value = "工艺路线步骤描述")
    private String description;
    @ApiModelProperty(value = "入口步骤")
    private String entryStepFlag;
    @ApiModelProperty(value = "路径选择策略")
    private String queueDecisionType;
    @ApiModelProperty(value = "是否关键步骤")
    private String keyStepFlag;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("工艺路线步骤扩展属性")
    private List<MtExtendAttrDTO3> routerStepAttrs;

    @ApiModelProperty(value = "嵌套工艺路线信息")
    private MtRouterLinkDTO mtRouterLinkDTO;
    @ApiModelProperty(value = "工艺信息")
    private MtRouterOperationDTO mtRouterOperationDTO;
    @ApiModelProperty(value = "步骤组信息")
    private MtRouterStepGroupDTO mtRouterStepGroupDTO;

    @ApiModelProperty(value = "下一步骤信息")
    private List<MtRouterNextStepDTO> mtRouterNextStepDTO;
    @ApiModelProperty(value = "完成步骤信息")
    private String routerDoneStepId;
    @ApiModelProperty(value = "完成步骤标识")
    private String routerDoneStepFlag;
    @ApiModelProperty(value = "返回步骤信息")
    private MtRouterReturnStepDTO mtRouterReturnStepDTO;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryStepFlag() {
        return entryStepFlag;
    }

    public void setEntryStepFlag(String entryStepFlag) {
        this.entryStepFlag = entryStepFlag;
    }

    public String getQueueDecisionType() {
        return queueDecisionType;
    }

    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
    }

    public String getKeyStepFlag() {
        return keyStepFlag;
    }

    public void setKeyStepFlag(String keyStepFlag) {
        this.keyStepFlag = keyStepFlag;
    }

    public MtRouterLinkDTO getMtRouterLinkDTO() {
        return mtRouterLinkDTO;
    }

    public void setMtRouterLinkDTO(MtRouterLinkDTO mtRouterLinkDTO) {
        this.mtRouterLinkDTO = mtRouterLinkDTO;
    }

    public MtRouterOperationDTO getMtRouterOperationDTO() {
        return mtRouterOperationDTO;
    }

    public void setMtRouterOperationDTO(MtRouterOperationDTO mtRouterOperationDTO) {
        this.mtRouterOperationDTO = mtRouterOperationDTO;
    }

    public MtRouterStepGroupDTO getMtRouterStepGroupDTO() {
        return mtRouterStepGroupDTO;
    }

    public void setMtRouterStepGroupDTO(MtRouterStepGroupDTO mtRouterStepGroupDTO) {
        this.mtRouterStepGroupDTO = mtRouterStepGroupDTO;
    }

    public List<MtRouterNextStepDTO> getMtRouterNextStepDTO() {
        return mtRouterNextStepDTO;
    }

    public void setMtRouterNextStepDTO(List<MtRouterNextStepDTO> mtRouterNextStepDTO) {
        this.mtRouterNextStepDTO = mtRouterNextStepDTO;
    }

    public String getRouterDoneStepId() {
        return routerDoneStepId;
    }

    public void setRouterDoneStepId(String routerDoneStepId) {
        this.routerDoneStepId = routerDoneStepId;
    }

    public String getRouterDoneStepFlag() {
        return routerDoneStepFlag;
    }

    public void setRouterDoneStepFlag(String routerDoneStepFlag) {
        this.routerDoneStepFlag = routerDoneStepFlag;
    }

    public MtRouterReturnStepDTO getMtRouterReturnStepDTO() {
        return mtRouterReturnStepDTO;
    }

    public void setMtRouterReturnStepDTO(MtRouterReturnStepDTO mtRouterReturnStepDTO) {
        this.mtRouterReturnStepDTO = mtRouterReturnStepDTO;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getRouterStepAttrs() {
        return routerStepAttrs;
    }

    public void setRouterStepAttrs(List<MtExtendAttrDTO3> routerStepAttrs) {
        this.routerStepAttrs = routerStepAttrs;
    }
}
