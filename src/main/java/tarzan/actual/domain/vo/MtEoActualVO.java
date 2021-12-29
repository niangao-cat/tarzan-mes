package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoActualVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6625624060015612133L;
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
