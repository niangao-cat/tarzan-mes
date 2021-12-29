package tarzan.dispatch.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-02-10 11:48
 **/
public class MtOpWkcDispatchRelVO10 implements Serializable {
    private static final long serialVersionUID = -1363504448812898504L;
    private String operationId; // 工艺
    private String operationWkcDispatchRelId; // 工艺和工作单元关系ID
    private String workcellId; // 工作单元

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationWkcDispatchRelId() {
        return operationWkcDispatchRelId;
    }

    public void setOperationWkcDispatchRelId(String operationWkcDispatchRelId) {
        this.operationWkcDispatchRelId = operationWkcDispatchRelId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
