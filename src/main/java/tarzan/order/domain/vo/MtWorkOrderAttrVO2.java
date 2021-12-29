package tarzan.order.domain.vo;


import java.io.Serializable;


public class MtWorkOrderAttrVO2 implements Serializable {


    private static final long serialVersionUID = -2750945588929034578L;
    private String workOrderId;
    private String attrName;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
