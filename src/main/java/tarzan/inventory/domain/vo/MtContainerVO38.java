package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-02-10 17:17
 **/
public class MtContainerVO38 implements Serializable {
    private static final long serialVersionUID = 3607498340358479296L;
    private String loadMaterialId;
    private String loadUomId;
    private Double loadUomQty;

    public String getLoadMaterialId() {
        return loadMaterialId;
    }

    public void setLoadMaterialId(String loadMaterialId) {
        this.loadMaterialId = loadMaterialId;
    }

    public String getLoadUomId() {
        return loadUomId;
    }

    public void setLoadUomId(String loadUomId) {
        this.loadUomId = loadUomId;
    }

    public Double getLoadUomQty() {
        return loadUomQty;
    }

    public void setLoadUomQty(Double loadUomQty) {
        this.loadUomQty = loadUomQty;
    }
}
