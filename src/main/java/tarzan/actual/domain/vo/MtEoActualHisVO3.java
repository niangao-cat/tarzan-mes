package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoActualHisVO3 implements Serializable {
    private static final long serialVersionUID = 1210176430610259750L;

    private String eoId;
    private String eoActualId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }
}
