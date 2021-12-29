package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssembleGroupVO3 implements Serializable {

    private static final long serialVersionUID = 5667338862972053233L;

    private String assembleGroupId;// 装配组ID

    private String targetStatus;// 目标状态

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }
}
