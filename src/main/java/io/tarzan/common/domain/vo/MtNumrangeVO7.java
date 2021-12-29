package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtNumrange;

/**
 * @author yuan.yuan@hand-china.com
 * @ClassName MtNumrangeVO7
 * @createTime 2019年08月26日 15:30:00
 */
public class MtNumrangeVO7 extends MtNumrange implements Serializable {

    private static final long serialVersionUID = 7071300233682584518L;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象描述")
    private String objectName;

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
