package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/22 9:58
 * @Description:
 */
public class HmeEoJobSnRecordVO implements Serializable {
    private static final long serialVersionUID = 4092749049167651277L;

    private String workOrderId;
    private String wkcShiftId;
    private String materialId;
    private String workcellId;

    public HmeEoJobSnRecordVO() {}

    public HmeEoJobSnRecordVO(String workOrderId, String wkcShiftId, String materialId, String workcellId) {
        this.materialId = materialId;
        this.wkcShiftId = wkcShiftId;
        this.workcellId = workcellId;
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWkcShiftId() {
        return wkcShiftId;
    }

    public void setWkcShiftId(String wkcShiftId) {
        this.wkcShiftId = wkcShiftId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnRecordVO that = (HmeEoJobSnRecordVO) o;
        return Objects.equals(workOrderId, that.workOrderId) && Objects.equals(wkcShiftId, that.wkcShiftId)
                        && Objects.equals(materialId, that.materialId) && Objects.equals(workcellId, that.workcellId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrderId, wkcShiftId, materialId, workcellId);
    }
}
