package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoRouterActualVO1 implements Serializable {

    private static final long serialVersionUID = 7441985241807401276L;
    private String eoStepActualId; // 主键
    private String eoRouterActualId; // 外键
    private String routerStepId; // 步骤ID（对于特殊操作步骤ID就是操作ID）
    private String routerId;
    private String routerGroupStepId;
    private Date lastUpdateDate;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterGroupStepId() {
        return routerGroupStepId;
    }

    public void setRouterGroupStepId(String routerGroupStepId) {
        this.routerGroupStepId = routerGroupStepId;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
