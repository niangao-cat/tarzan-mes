package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomVO10 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7778195798379866965L;
    private String bomId;
    private String bomHisId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomHisId() {
        return bomHisId;
    }

    public void setBomHisId(String bomHisId) {
        this.bomHisId = bomHisId;
    }

}
