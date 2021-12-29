package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO14 implements Serializable {


    private static final long serialVersionUID = -5750193635207099751L;

    private String adjustObjectId;

    private String adjustObjectType;

    public String getAdjustObjectId() {
        return adjustObjectId;
    }

    public void setAdjustObjectId(String adjustObjectId) {
        this.adjustObjectId = adjustObjectId;
    }

    public String getAdjustObjectType() {
        return adjustObjectType;
    }

    public void setAdjustObjectType(String adjustObjectType) {
        this.adjustObjectType = adjustObjectType;
    }
}
