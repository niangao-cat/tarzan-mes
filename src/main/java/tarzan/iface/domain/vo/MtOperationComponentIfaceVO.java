package tarzan.iface.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/11 13:45
 */
public class MtOperationComponentIfaceVO implements Serializable {

    private static final long serialVersionUID = 3434086107052249366L;
    private String routerId;
    private String routerOperationId;
    private String routerCode;
    private String routerType;
    private String routerAlternate;
    private String operationSeqNum;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getRouterCode() {
        return routerCode;
    }

    public void setRouterCode(String routerCode) {
        this.routerCode = routerCode;
    }

    public String getRouterAlternate() {
        return routerAlternate;
    }

    public void setRouterAlternate(String routerAlternate) {
        this.routerAlternate = routerAlternate;
    }

    public String getOperationSeqNum() {
        return operationSeqNum;
    }

    public void setOperationSeqNum(String operationSeqNum) {
        this.operationSeqNum = operationSeqNum;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }
}
