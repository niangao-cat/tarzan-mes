package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtAssembleGroupActualVO6 implements Serializable {
    private static final long serialVersionUID = -3961421459727929185L;

    @ApiModelProperty("装配组实绩Id")
    private String assembleGroupActualId;
    @ApiModelProperty("装配组实绩历史Id")
    private String assembleGroupActualHisId;

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getAssembleGroupActualHisId() {
        return assembleGroupActualHisId;
    }

    public void setAssembleGroupActualHisId(String assembleGroupActualHisId) {
        this.assembleGroupActualHisId = assembleGroupActualHisId;
    }
}
