package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomReferencePointVO3 implements Serializable {
    private static final long serialVersionUID = 8226245872948700192L;
    private String bomComponentId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }
}
