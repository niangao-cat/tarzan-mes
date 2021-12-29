package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 17:12
 * @Description:
 */
public class MtModProdLineManufacturingVO implements Serializable {
    private static final long serialVersionUID = -1615466226699386808L;

    private String prodLineId; // 生产线ID
    private String issuedLocatorId; // 默认发料库位
    private String completionLocatorId; // 默认完工库位
    private String inventoryLocatorId; // 默认入库库位
    private String dispatchMethod; // 调度方式
    private List<String> operationId;


    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }

    public String getInventoryLocatorId() {
        return inventoryLocatorId;
    }

    public void setInventoryLocatorId(String inventoryLocatorId) {
        this.inventoryLocatorId = inventoryLocatorId;
    }

    public String getDispatchMethod() {
        return dispatchMethod;
    }

    public void setDispatchMethod(String dispatchMethod) {
        this.dispatchMethod = dispatchMethod;
    }

    public List<String> getOperationId() {
        return operationId;
    }

    public void setOperationId(List<String> operationId) {
        this.operationId = operationId;
    }

}
