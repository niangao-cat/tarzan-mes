package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterStepGroupDTO implements Serializable {
    private static final long serialVersionUID = 3552908236162936841L;

    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty("工艺路线步骤组唯一标识")
    private String routerStepGroupId;
    @ApiModelProperty(value = "工艺路线步骤组类型")
    @NotBlank
    private String routerStepGroupType;
    @ApiModelProperty(value = "工艺路线步骤组步骤")
    private List<MtRouterStepGroupStepDTO> mtRouterStepGroupStepDTO;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterStepGroupId() {
        return routerStepGroupId;
    }

    public void setRouterStepGroupId(String routerStepGroupId) {
        this.routerStepGroupId = routerStepGroupId;
    }

    public String getRouterStepGroupType() {
        return routerStepGroupType;
    }

    public void setRouterStepGroupType(String routerStepGroupType) {
        this.routerStepGroupType = routerStepGroupType;
    }

    public List<MtRouterStepGroupStepDTO> getMtRouterStepGroupStepDTO() {
        return mtRouterStepGroupStepDTO;
    }

    public void setMtRouterStepGroupStepDTO(List<MtRouterStepGroupStepDTO> mtRouterStepGroupStepDTO) {
        this.mtRouterStepGroupStepDTO = mtRouterStepGroupStepDTO;
    }
}
