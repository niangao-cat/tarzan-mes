package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP on 2019/3/19.
 */
public class MtWorkOrderVO10 implements Serializable {

    private static final long serialVersionUID = -1223100760721061340L;

    private String primaryWorkOrderId;

    private List<String> secondaryWorkOrderIds;

    public String getPrimaryWorkOrderId() {
        return primaryWorkOrderId;
    }

    public void setPrimaryWorkOrderId(String primaryWorkOrderId) {
        this.primaryWorkOrderId = primaryWorkOrderId;
    }

    public List<String> getSecondaryWorkOrderIds() {
        return secondaryWorkOrderIds;
    }

    public void setSecondaryWorkOrderIds(List<String> secondaryWorkOrderIds) {
        this.secondaryWorkOrderIds = secondaryWorkOrderIds;
    }
}
