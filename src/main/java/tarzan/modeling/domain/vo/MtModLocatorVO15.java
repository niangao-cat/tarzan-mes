package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModLocatorVO15
 */
public class MtModLocatorVO15 implements Serializable {


    private static final long serialVersionUID = -7010100371071796271L;

    @ApiModelProperty(value = "库位Id")
    private String locatorId;

    @ApiModelProperty(value = "库存库位ID")
    private String inventoryLocatorId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getInventoryLocatorId() {
        return inventoryLocatorId;
    }

    public void setInventoryLocatorId(String inventoryLocatorId) {
        this.inventoryLocatorId = inventoryLocatorId;
    }

    public MtModLocatorVO15() {}

    public MtModLocatorVO15(String locatorId, String inventoryLocatorId) {
        this.locatorId = locatorId;
        this.inventoryLocatorId = inventoryLocatorId;
    }
}
