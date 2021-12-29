package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomVO5 implements Serializable {
    private static final long serialVersionUID = -46670599999822202L;

    private String sourceBomId;
    private String targetBomId;

    public String getSourceBomId() {
        return sourceBomId;
    }

    public void setSourceBomId(String sourceBomId) {
        this.sourceBomId = sourceBomId;
    }

    public String getTargetBomId() {
        return targetBomId;
    }

    public void setTargetBomId(String targetBomId) {
        this.targetBomId = targetBomId;
    }
}
