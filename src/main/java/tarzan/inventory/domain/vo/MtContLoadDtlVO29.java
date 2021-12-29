package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/13 11:02
 * @Description:
 */
public class MtContLoadDtlVO29 implements Serializable {

    private static final long serialVersionUID = -1122497977717777470L;
    @ApiModelProperty("装载对象类型")
    private String loadObjectType;
    @ApiModelProperty("装载对象ID")
    private String loadObjectId;
    @ApiModelProperty("卸载数量")
    private Double trxUnloadQty;

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public Double getTrxUnloadQty() {
        return trxUnloadQty;
    }

    public void setTrxUnloadQty(Double trxUnloadQty) {
        this.trxUnloadQty = trxUnloadQty;
    }
}
