package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 14:52
 * @Description:
 */
public class HmeEoJobEquipmentVO implements Serializable {
    private static final long serialVersionUID = -2853723013633219832L;

    private String equipmentId;
    private String equipmentStatus;

    public HmeEoJobEquipmentVO() {}

    public HmeEoJobEquipmentVO(String equipmentId, String equipmentStatus) {
        this.equipmentId = equipmentId;
        this.equipmentStatus = equipmentStatus;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobEquipmentVO that = (HmeEoJobEquipmentVO) o;
        return Objects.equals(equipmentId, that.equipmentId) && Objects.equals(equipmentStatus, that.equipmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId, equipmentStatus);
    }
}
