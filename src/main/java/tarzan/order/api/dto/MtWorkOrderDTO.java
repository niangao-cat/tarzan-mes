package tarzan.order.api.dto;


import java.io.Serializable;
import java.util.List;



public class MtWorkOrderDTO implements Serializable {

    private static final long serialVersionUID = -1128301526084982196L;

    private String workOrderId;    //生产指令ID
    private List<String> demandStatusList;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public List<String> getDemandStatusList() {
        return demandStatusList;
    }
    public void setDemandStatusList(List<String> demandStatusList) {
        this.demandStatusList = demandStatusList;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderDTO [workOrderId=");
        builder.append(workOrderId);
        builder.append(", demandStatusList=");
        builder.append(demandStatusList);
        builder.append("]");
        return builder.toString();
    }
    
}
