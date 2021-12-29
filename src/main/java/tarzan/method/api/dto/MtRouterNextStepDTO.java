package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterNextStepDTO implements Serializable {
    private static final long serialVersionUID = 1639751127567674448L;

    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty("步骤关系唯一标识")
    private String routerNextStepId;
    @ApiModelProperty(value = "下一步骤标识")
    @NotBlank
    private String nextStepId;
    @ApiModelProperty(value = "下一步骤识别码")
    private String nextStepName;
    @ApiModelProperty(value = "下一步骤描述")
    private String nextStepDesc;
    @ApiModelProperty(value = "顺序")
    private Long sequence;
    @ApiModelProperty(value = "选择策略类型")
    @NotBlank
    private String nextDecisionType;
    @ApiModelProperty(value = "选择策略对应值(MAIN时无值，PRODUCT时为物料ID，NC时为编码)")
    private String nextDecisionValue;
    @ApiModelProperty("工艺路线下一步骤扩展属性")
    private List<MtExtendAttrDTO3> routerNextStepAttrs;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterNextStepId() {
        return routerNextStepId;
    }

    public void setRouterNextStepId(String routerNextStepId) {
        this.routerNextStepId = routerNextStepId;
    }

    public String getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(String nextStepId) {
        this.nextStepId = nextStepId;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getNextStepDesc() {
        return nextStepDesc;
    }

    public void setNextStepDesc(String nextStepDesc) {
        this.nextStepDesc = nextStepDesc;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getNextDecisionType() {
        return nextDecisionType;
    }

    public void setNextDecisionType(String nextDecisionType) {
        this.nextDecisionType = nextDecisionType;
    }

    public String getNextDecisionValue() {
        return nextDecisionValue;
    }

    public void setNextDecisionValue(String nextDecisionValue) {
        this.nextDecisionValue = nextDecisionValue;
    }

    public List<MtExtendAttrDTO3> getRouterNextStepAttrs() {
        return routerNextStepAttrs;
    }

    public void setRouterNextStepAttrs(List<MtExtendAttrDTO3> routerNextStepAttrs) {
        this.routerNextStepAttrs = routerNextStepAttrs;
    }
}
