package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/21 19:46
 * @Description:
 */
public class HmeEoJobSnVO13 implements Serializable {
    private static final long serialVersionUID = -6859456389832987559L;

    private String eoId;
    private String operationId;
    private String eoStepId;

    public HmeEoJobSnVO13() {

    }

    public HmeEoJobSnVO13(String eoId, String operationId, String eoStepId) {
        this.eoId = eoId;
        this.operationId = operationId;
        this.eoStepId = eoStepId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getEoStepId() {
        return eoStepId;
    }

    public void setEoStepId(String eoStepId) {
        this.eoStepId = eoStepId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnVO13 that = (HmeEoJobSnVO13) o;
        return Objects.equals(eoId, that.eoId) && Objects.equals(operationId, that.operationId)
                        && Objects.equals(eoStepId, that.eoStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, operationId, eoStepId);
    }
}
