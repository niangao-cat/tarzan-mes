package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/4 9:06
 */
public class MtContainerVO4 implements Serializable {
    private static final long serialVersionUID = 7899551049722676175L;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getRowPriorityFlag() {
        return rowPriorityFlag;
    }

    public void setRowPriorityFlag(String rowPriorityFlag) {
        this.rowPriorityFlag = rowPriorityFlag;
    }

    private String containerId;
    private String rowPriorityFlag;
}
