package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleConfirmActualVO5 implements Serializable {

    private static final long serialVersionUID = 6719830466325635168L;
    private String eoId;
    private String bypassBy;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }
}
