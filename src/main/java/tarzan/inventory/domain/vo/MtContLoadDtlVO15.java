package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019-10-22 17:37
 */
public class MtContLoadDtlVO15 implements Serializable {
    private static final long serialVersionUID = 2067305763814847562L;
    private String containerId;
    private Long loadSequence;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Long getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(Long loadSequence) {
        this.loadSequence = loadSequence;
    }
}
