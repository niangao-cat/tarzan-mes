package tarzan.dispatch.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/6/25 8:33
 * @Description:
 */
public class MtOpWkcDispatchRelVO6 implements Serializable {
    private static final long serialVersionUID = 3852690483960336616L;

    private String operationId; // 工艺
    private String operationWkcDispatchRelId; // 工艺和工作单元关系ID

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
}
