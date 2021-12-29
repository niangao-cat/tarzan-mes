package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterSubstepDTO implements Serializable {
    private static final long serialVersionUID = 7020763640883785290L;

    @ApiModelProperty("工艺路线子步骤唯一标识")
    private String routerSubstepId;
    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty(value = "子步骤Id")
    @NotBlank
    private String substepId;
    @ApiModelProperty(value = "子步骤")
    private String substepName;
    @ApiModelProperty(value = "子步骤描述")
    private String substepDesc;
    @ApiModelProperty(value = "子步骤顺序")
    @NotNull
    private Long sequence;
    @ApiModelProperty("工艺路线子步骤扩展属性")
    private List<MtExtendAttrDTO3> routerSubstepAttrs;
    @ApiModelProperty(value = "子步骤组件信息")
    private List<MtRouterSubstepComponentDTO> mtRouterSubstepComponentDTO;

    public String getRouterSubstepId() {
        return routerSubstepId;
    }

    public void setRouterSubstepId(String routerSubstepId) {
        this.routerSubstepId = routerSubstepId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getSubstepName() {
        return substepName;
    }

    public void setSubstepName(String substepName) {
        this.substepName = substepName;
    }

    public String getSubstepDesc() {
        return substepDesc;
    }

    public void setSubstepDesc(String substepDesc) {
        this.substepDesc = substepDesc;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public List<MtRouterSubstepComponentDTO> getMtRouterSubstepComponentDTO() {
        return mtRouterSubstepComponentDTO;
    }

    public void setMtRouterSubstepComponentDTO(List<MtRouterSubstepComponentDTO> mtRouterSubstepComponentDTO) {
        this.mtRouterSubstepComponentDTO = mtRouterSubstepComponentDTO;
    }

    public List<MtExtendAttrDTO3> getRouterSubstepAttrs() {
        return routerSubstepAttrs;
    }

    public void setRouterSubstepAttrs(List<MtExtendAttrDTO3> routerSubstepAttrs) {
        this.routerSubstepAttrs = routerSubstepAttrs;
    }
}
