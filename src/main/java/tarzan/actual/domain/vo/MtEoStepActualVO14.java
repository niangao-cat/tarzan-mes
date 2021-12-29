package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO14 implements Serializable {

    private static final long serialVersionUID = 1378573277834025362L;
    private String routerId;//工艺路线ID
    private String eoId;//执行作业ID
    
    public String getRouterId() {
        return routerId;
    }
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
    public String getEoId() {
        return eoId;
    }
    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
    
    

}
