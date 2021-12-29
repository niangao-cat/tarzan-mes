package tarzan.actual.domain.vo;


import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/9/16 16:19
 */
public class MtEoActualVO7 implements Serializable {

    private static final long serialVersionUID = 9100133085851962854L;
    private String eOActualId;
    private String eOActualHisId;

    public String geteOActualId() {
        return eOActualId;
    }

    public void seteOActualId(String eOActualId) {
        this.eOActualId = eOActualId;
    }

    public String geteOActualHisId() {
        return eOActualHisId;
    }

    public void seteOActualHisId(String eOActualHisId) {
        this.eOActualHisId = eOActualHisId;
    }
}
