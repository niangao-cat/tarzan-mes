package com.ruike.hme.domain.vo;

import java.io.*;
import java.util.*;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 14:52
 * @Description:
 */
public class HmeEoJobEquipmentVO2 implements Serializable {
    private static final long serialVersionUID = -6803072149175730131L;

    private String jobId;
    private String equipmentId;
    private String equipmentStatus;

    public HmeEoJobEquipmentVO2() {}

    public HmeEoJobEquipmentVO2(String jobId, String equipmentId, String equipmentStatus) {
        this.jobId = jobId;
        this.equipmentId = equipmentId;
        this.equipmentStatus = equipmentStatus;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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
        HmeEoJobEquipmentVO2 that = (HmeEoJobEquipmentVO2) o;
        return Objects.equals(jobId, that.jobId) &&
                Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(equipmentStatus, that.equipmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, equipmentId, equipmentStatus);
    }
}
