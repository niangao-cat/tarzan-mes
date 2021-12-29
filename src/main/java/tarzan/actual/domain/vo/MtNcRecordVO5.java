package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO5 implements Serializable {

    private static final long serialVersionUID = 6991251682084394204L;
    /**
     * 处置组
     */
    private String dispositionGroupId;

    /**
     * 处置工艺路线
     */
    private String dispositionRouterId;

    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    public String getDispositionRouterId() {
        return dispositionRouterId;
    }

    public void setDispositionRouterId(String dispositionRouterId) {
        this.dispositionRouterId = dispositionRouterId;
    }
}
