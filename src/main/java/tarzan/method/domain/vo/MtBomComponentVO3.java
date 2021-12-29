package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO3 implements Serializable {

    private static final long serialVersionUID = -549434334736010857L;
    private String verifyResult;
    private String message;

    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
