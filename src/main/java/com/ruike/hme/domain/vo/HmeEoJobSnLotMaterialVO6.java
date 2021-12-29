package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/22 11:26
 * @Description:
 */
public class HmeEoJobSnLotMaterialVO6 implements Serializable {
    private static final long serialVersionUID = -4901250621618803074L;

    private String workcellId;
    private String materialId;
    private String materialLotId;

    public HmeEoJobSnLotMaterialVO6() {}

    public HmeEoJobSnLotMaterialVO6(String workcellId, String materialId, String materialLotId) {
        this.materialId = materialId;
        this.materialLotId = materialLotId;
        this.workcellId = workcellId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnLotMaterialVO6 that = (HmeEoJobSnLotMaterialVO6) o;
        return Objects.equals(workcellId, that.workcellId) && Objects.equals(materialId, that.materialId)
                        && Objects.equals(materialLotId, that.materialLotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workcellId, materialId, materialLotId);
    }
}
