package tarzan.order.api.dto;


import java.io.Serializable;


public class MtWorkOrderRelDTO3 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;
    
   
    private String parentWorkOrderId;  // 上层生产指令ID
    private String relType;   //关系类型
    public String getParentWorkOrderId() {
        return parentWorkOrderId;
    }
    public void setParentWorkOrderId(String parentWorkOrderId) {
        this.parentWorkOrderId = parentWorkOrderId;
    }
    public String getRelType() {
        return relType;
    }
    public void setRelType(String relType) {
        this.relType = relType;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderRelDTO");
        builder.append("[parentWorkOrderId=");
        builder.append(parentWorkOrderId);
        builder.append(", relType=");
        builder.append(relType);
        builder.append("]");
        return builder.toString();
    }
    
}
