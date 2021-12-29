package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtExtendDTO4 implements Serializable {
    private static final long serialVersionUID = -7750839323085624278L;
    @ApiModelProperty(value = "原表主键", required = true)
    private String kid;
    @ApiModelProperty(value = "表名", required = true)
    private String tableName;
    @ApiModelProperty(value = "整个表格中所有值", required = true)
    private List<MtExtendAttrDTO3> attrs;

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

    public List<MtExtendAttrDTO3> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendAttrDTO3> attrs) {
        this.attrs = attrs;
    }
}
