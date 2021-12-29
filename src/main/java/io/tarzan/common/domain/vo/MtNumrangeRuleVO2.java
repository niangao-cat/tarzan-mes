package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtNumrangeRule;

/**
 * @author yuan.yuan@hand-china.com
 * @ClassName MtNumrangeRuleVO2
 * @createTime 2019年08月26日 11:36:00
 */
public class MtNumrangeRuleVO2 extends MtNumrangeRule implements Serializable {

    private static final long serialVersionUID = 6640141451397715037L;
    @ApiModelProperty(value = "标准对象编码属性代码")
    private String callStandardObjectCode;

    public String getCallStandardObjectCode() {
        return callStandardObjectCode;
    }

    public void setCallStandardObjectCode(String callStandardObjectCode) {
        this.callStandardObjectCode = callStandardObjectCode;
    }
}
