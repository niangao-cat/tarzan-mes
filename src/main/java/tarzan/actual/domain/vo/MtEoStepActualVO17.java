package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO17 implements Serializable {
    private static final long serialVersionUID = 8838188515984826788L;

    private String allStepCompletedFlag; // 全部完成标识
    private String sequentlyCompletedFlag; // 顺序完成标识

    public String getAllStepCompletedFlag() {
        return allStepCompletedFlag;
    }

    public void setAllStepCompletedFlag(String allStepCompletedFlag) {
        this.allStepCompletedFlag = allStepCompletedFlag;
    }

    public String getSequentlyCompletedFlag() {
        return sequentlyCompletedFlag;
    }

    public void setSequentlyCompletedFlag(String sequentlyCompletedFlag) {
        this.sequentlyCompletedFlag = sequentlyCompletedFlag;
    }
}
