package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtExtendDTO3 implements Serializable {
    private static final long serialVersionUID = -6154944566723639955L;

    @ApiModelProperty(value = "主键")
    private String kid;
    @ApiModelProperty(value = "表名", required = true)
    private String tableName;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
