package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.tarzan.common.domain.entity.MtNumrange;


/**
 * @author Leeloing
 * @date 2019/7/5 15:48
 */
public class MtNumrangeAssignVO1 extends MtNumrange implements Serializable {
    private static final long serialVersionUID = -3296233211815223300L;
    private String objectCode;
    private String objectDescription;

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }
}
