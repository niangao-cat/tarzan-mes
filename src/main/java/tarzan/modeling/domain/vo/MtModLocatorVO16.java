package tarzan.modeling.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author li.zhang 2021/07/06 10:40
 */
public class MtModLocatorVO16 implements Serializable {

    private static final long serialVersionUID = 8253477602129209546L;

    @ApiModelProperty(value = "批次编号")
    private String lot;
    @ApiModelProperty(value = "库位Id")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public BigDecimal getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(BigDecimal primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public MtModLocatorVO16() {
    }

    public MtModLocatorVO16(String lot, String locatorId, String locatorCode, BigDecimal primaryUomQty) {
        this.lot = lot;
        this.locatorId = locatorId;
        this.locatorCode = locatorCode;
        this.primaryUomQty = primaryUomQty;
    }
}
