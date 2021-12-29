package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO3 implements Serializable {


    private static final long serialVersionUID = -258620840461288308L;

    private String holdId;// 对象类型

    private String holdDetailId;// 对象ID

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public String getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(String holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

}
