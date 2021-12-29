package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0017, 2020-02-17 8:42
 */
public class MtProcessWorkingDTO15 implements Serializable {
    private static final long serialVersionUID = 1839795704864819930L;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("物料编码")
    private String materialLotCode;

    @ApiModelProperty("装配数量")
    private Double assembleQty;

    @ApiModelProperty("报废数量")
    private Double scrapQty;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    public Double getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(Double assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Double getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(Double scrapQty) {
        this.scrapQty = scrapQty;
    }
}
