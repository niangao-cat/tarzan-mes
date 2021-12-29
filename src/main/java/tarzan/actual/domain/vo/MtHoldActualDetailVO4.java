package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO4 implements Serializable {

    private static final long serialVersionUID = 6974037375908833675L;

    private String holdDetailId;
    private String releaseComment;
    private String releaseReasonCode;
    private String releaseEventId;

    public String getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(String holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

    public String getReleaseComment() {
        return releaseComment;
    }

    public void setReleaseComment(String releaseComment) {
        this.releaseComment = releaseComment;
    }

    public String getReleaseReasonCode() {
        return releaseReasonCode;
    }

    public void setReleaseReasonCode(String releaseReasonCode) {
        this.releaseReasonCode = releaseReasonCode;
    }

    public String getReleaseEventId() {
        return releaseEventId;
    }

    public void setReleaseEventId(String releaseEventId) {
        this.releaseEventId = releaseEventId;
    }

}
