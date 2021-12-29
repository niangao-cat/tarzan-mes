package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Tangxiao
 */
public class MtEoVO49 implements Serializable {

    private static final long serialVersionUID = 8701943037189534712L;
    @ApiModelProperty("eoId")
    private String eoId;
    @ApiModelProperty("标识")
    private String operationAssembleFlag;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public MtEoVO49() {
    }

    public MtEoVO49(String eoId, String operationAssembleFlag) {
        this.eoId = eoId;
        this.operationAssembleFlag = operationAssembleFlag;
    }
}
