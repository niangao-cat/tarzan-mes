package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-10-21 18:47
 */
public class MtExtendAttrDTO4 implements Serializable {
    private static final long serialVersionUID = -3976844708041715429L;
    @ApiModelProperty(value = "表名")
    private String attrTable;
    @ApiModelProperty(value = "表描述")
    private String attrTableDesc;
    @ApiModelProperty(value = "系统初始化标识")
    private String initialFlag;

    public String getAttrTable() {
        return attrTable;
    }

    public void setAttrTable(String attrTable) {
        this.attrTable = attrTable;
    }

    public String getAttrTableDesc() {
        return attrTableDesc;
    }

    public void setAttrTableDesc(String attrTableDesc) {
        this.attrTableDesc = attrTableDesc;
    }

    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
    }
}
