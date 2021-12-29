package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/19.
 */
public class MtWorkOrderVO11 implements Serializable {

    private static final long serialVersionUID = -1485895332980855101L;

    private String sourceWorkOrderId;

    private Double splitQty;

    private String targetWorkOrderNum;

    public String getSourceWorkOrderId() {
        return sourceWorkOrderId;
    }

    public void setSourceWorkOrderId(String sourceWorkOrderId) {
        this.sourceWorkOrderId = sourceWorkOrderId;
    }

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }

    public String getTargetWorkOrderNum() {
        return targetWorkOrderNum;
    }

    public void setTargetWorkOrderNum(String targetWorkOrderNum) {
        this.targetWorkOrderNum = targetWorkOrderNum;
    }
}
