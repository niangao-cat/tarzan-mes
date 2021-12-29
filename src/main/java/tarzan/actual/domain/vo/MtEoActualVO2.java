package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoActualVO2 implements Serializable {
    private static final long serialVersionUID = 4155565226559767548L;
    private String eoId;
    private String eoActualId;
    private String periodUom;

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

    public String getPeriodUom() {
        return periodUom;
    }

    public void setPeriodUom(String periodUom) {
        this.periodUom = periodUom;
    }
}
