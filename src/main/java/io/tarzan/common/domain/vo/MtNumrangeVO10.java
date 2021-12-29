package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-14 09:37
 */
public class MtNumrangeVO10 implements Serializable {
    private static final long serialVersionUID = 4246791302079192785L;
    @ApiModelProperty("标准对象参数")
    private Map<String, String> callObjectCode;
    @ApiModelProperty("序列")
    private Long sequence;

    public Map<String, String> getCallObjectCode() {
        return callObjectCode;
    }

    public void setCallObjectCode(Map<String, String> callObjectCode) {
        this.callObjectCode = callObjectCode;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
