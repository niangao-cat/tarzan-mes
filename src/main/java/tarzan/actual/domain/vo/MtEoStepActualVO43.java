package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 16:27
 * @Description:
 */
public class MtEoStepActualVO43 implements Serializable {
    private static final long serialVersionUID = 7467073649226868408L;

    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("EO对应步骤实绩顺序")
    private Long maxSequence;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Long getMaxSequence() {
        return maxSequence;
    }

    public void setMaxSequence(Long maxSequence) {
        this.maxSequence = maxSequence;
    }
}
