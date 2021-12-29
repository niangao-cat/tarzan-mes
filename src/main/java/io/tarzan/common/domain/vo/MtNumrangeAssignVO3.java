package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/6/25 16:28
 */
public class MtNumrangeAssignVO3 implements Serializable {

    private static final long serialVersionUID = 3248214294608203464L;
    
    @ApiModelProperty(value = "编码对象Id")
    private String objectId;
    
    @ApiModelProperty(value = "号段描述")
    private String numDescription;
    
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getNumDescription() {
        return numDescription;
    }
    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }
}
