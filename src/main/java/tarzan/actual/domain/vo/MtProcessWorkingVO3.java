package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao tang
 * @date 2020-02-12 18:07
 */
public class MtProcessWorkingVO3 implements Serializable {
    
    private static final long serialVersionUID = 3174059217416767051L;
    
    @ApiModelProperty("eo步骤实绩ID")
    private String eoStepActualId;
    
    @ApiModelProperty("步骤识别码")
    private String stepName;
    
    @ApiModelProperty("完成数量")
    private Double completedQty;
    
    @ApiModelProperty("步骤顺序")
    private Long sequence;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
    
}
