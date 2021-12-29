package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/6/25 16:28
 */
public class MtNumrangeAssignVO4 implements Serializable {

    private static final long serialVersionUID = -8285227748255536153L;

    @ApiModelProperty(value = "编码对象Id")
    private String objectId;
    
    @ApiModelProperty(value = "编码对象类型描述")
    private String description;
    
    @ApiModelProperty(value = "编码对象类型编码")
    private String typeCode;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
