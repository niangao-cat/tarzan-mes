package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/16 20:07
 */
public class MtEoAttrVO2 implements Serializable {
    private static final long serialVersionUID = -8572079873491376556L;

    private String eoId;
    private String attrName;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
