package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomVO4 implements Serializable {

    private static final long serialVersionUID = 8205814535393535805L;

    private String bomId;
    private String revision;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
