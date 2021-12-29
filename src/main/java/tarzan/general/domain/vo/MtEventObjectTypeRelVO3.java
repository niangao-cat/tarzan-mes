package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectTypeRelVO3 implements Serializable {
    private static final long serialVersionUID = 5044606615967568293L;

    private String objectTypeCode;
    private String enableFlag;

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
