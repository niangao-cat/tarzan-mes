package tarzan.modeling.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtModProdLineDispatchOperDTO implements Serializable {
    private static final long serialVersionUID = -3541527015663380005L;

    @ApiModelProperty("生产线调度指定工艺ID")
    private String dispatchOperationId;
    @ApiModelProperty(value = "生产线ID", required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    private String operationId;

    public String getDispatchOperationId() {
        return dispatchOperationId;
    }

    public void setDispatchOperationId(String dispatchOperationId) {
        this.dispatchOperationId = dispatchOperationId;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
