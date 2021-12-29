package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterOperationDTO implements Serializable {
    private static final long serialVersionUID = -1021656859537359060L;

    @ApiModelProperty("步骤标识")
    private String routerStepId;
    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerOperationId;
    @ApiModelProperty(value = "工艺标识")
    @NotBlank
    private String operationId;
    @ApiModelProperty(value = "最大循环次数")
    private Long maxLoop;
    @ApiModelProperty(value = "特殊指令，展示在前台的文本")
    private String specialInstruction;
    @ApiModelProperty(value = "步骤处理所需时间（分钟）")
    private Double requiredTimeInProcess;
    @ApiModelProperty(value = "工艺组件信息")
    private List<MtRouterOperationComponentDTO> mtRouterOperationComponentDTO;
    @ApiModelProperty(value = "子步骤信息")
    private List<MtRouterSubstepDTO> mtRouterSubstepDTO;

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getMaxLoop() {
        return maxLoop;
    }

    public void setMaxLoop(Long maxLoop) {
        this.maxLoop = maxLoop;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public Double getRequiredTimeInProcess() {
        return requiredTimeInProcess;
    }

    public void setRequiredTimeInProcess(Double requiredTimeInProcess) {
        this.requiredTimeInProcess = requiredTimeInProcess;
    }

    public List<MtRouterOperationComponentDTO> getMtRouterOperationComponentDTO() {
        return mtRouterOperationComponentDTO;
    }

    public void setMtRouterOperationComponentDTO(List<MtRouterOperationComponentDTO> mtRouterOperationComponentDTO) {
        this.mtRouterOperationComponentDTO = mtRouterOperationComponentDTO;
    }

    public List<MtRouterSubstepDTO> getMtRouterSubstepDTO() {
        return mtRouterSubstepDTO;
    }

    public void setMtRouterSubstepDTO(List<MtRouterSubstepDTO> mtRouterSubstepDTO) {
        this.mtRouterSubstepDTO = mtRouterSubstepDTO;
    }
}
