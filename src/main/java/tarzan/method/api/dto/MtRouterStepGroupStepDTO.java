package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterStepGroupStepDTO implements Serializable {
    private static final long serialVersionUID = 2558438204539025981L;

    @ApiModelProperty("工艺路线步骤组分配唯一标识")
    private String routerStepGroupStepId;
    @ApiModelProperty("工艺路线步骤组唯一标识")
    private String routerStepGroupId;
    @ApiModelProperty(value = "步骤组步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "工艺路线步骤标识")
    @NotBlank
    private String routerStepId;
    @ApiModelProperty(value = "步骤识别码")
    @NotBlank
    private String stepName;
    @ApiModelProperty(value = "工艺路线步骤描述")
    private String stepDesc;
    @ApiModelProperty(value = "路径选择策略")
    private String queueDecisionType;
    @ApiModelProperty(value = "步骤组步骤顺序")
    private Long stepSequence;
    @ApiModelProperty(value = "工艺信息")
    private MtRouterOperationDTO mtRouterOperationDTO;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("工艺路线步骤组步骤扩展属性")
    private List<MtExtendAttrDTO3> routerStepGroupStepAttrs;

    public String getRouterStepGroupStepId() {
        return routerStepGroupStepId;
    }

    public void setRouterStepGroupStepId(String routerStepGroupStepId) {
        this.routerStepGroupStepId = routerStepGroupStepId;
    }

    public String getRouterStepGroupId() {
        return routerStepGroupId;
    }

    public void setRouterStepGroupId(String routerStepGroupId) {
        this.routerStepGroupId = routerStepGroupId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getQueueDecisionType() {
        return queueDecisionType;
    }

    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
    }

    public Long getStepSequence() {
        return stepSequence;
    }

    public void setStepSequence(Long stepSequence) {
        this.stepSequence = stepSequence;
    }

    public MtRouterOperationDTO getMtRouterOperationDTO() {
        return mtRouterOperationDTO;
    }

    public void setMtRouterOperationDTO(MtRouterOperationDTO mtRouterOperationDTO) {
        this.mtRouterOperationDTO = mtRouterOperationDTO;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getRouterStepGroupStepAttrs() {
        return routerStepGroupStepAttrs;
    }

    public void setRouterStepGroupStepAttrs(List<MtExtendAttrDTO3> routerStepGroupStepAttrs) {
        this.routerStepGroupStepAttrs = routerStepGroupStepAttrs;
    }
}
