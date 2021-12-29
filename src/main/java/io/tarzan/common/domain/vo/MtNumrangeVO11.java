package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-14 09:41
 */
public class MtNumrangeVO11 implements Serializable {
    private static final long serialVersionUID = 5120556636775921208L;
    @ApiModelProperty("传入值参数")
    private List<String> incomingValue;
    @ApiModelProperty("序列")
    private Long sequence;

    public List<String> getIncomingValue() {
        return incomingValue;
    }

    public void setIncomingValue(List<String> incomingValue) {
        this.incomingValue = incomingValue;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
