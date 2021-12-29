package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtHoldActualVO2 implements Serializable {


    private static final long serialVersionUID = 6814982769078343987L;

    private List<String> holdDetailId; // 保留实绩明细ID，主键，供其他表做外键

    private String holdId; // 保留实绩ID

    public List<String> getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(List<String> holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }


}
