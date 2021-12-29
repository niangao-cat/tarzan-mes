package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/15.
 */
public class MtWoComponentActualVO12 extends MtWoComponentActualVO1 implements Serializable {

    private static final long serialVersionUID = -5120215894752650968L;

    private String bomId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
}
