package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomComponentVO6 implements Serializable {
    private static final long serialVersionUID = -3763075320066647068L;
    private String bomComponentId;
    private Double qty;
    private String attritionFlag;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getAttritionFlag() {
        return attritionFlag;
    }

    public void setAttritionFlag(String attritionFlag) {
        this.attritionFlag = attritionFlag;
    }

    private List<MtBomVO26> bomMessageList;

    public List<MtBomVO26> getBomMessageList() {
        return bomMessageList;
    }

    public void setBomMessageList(List<MtBomVO26> bomMessageList) {
        this.bomMessageList = bomMessageList;
    }

}
