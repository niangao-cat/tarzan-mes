package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author benjamin
 */
public class MtModProdLineDispatchOperVO2 implements Serializable {
    private static final long serialVersionUID = -787328359757578954L;
    private String dispatchOperationId;// 主键
    private String prodLineId; // 生产线ID
    private String prodLineCode; // 生产线编码
    private String operationId; // 指定的调度工艺ID
    private String operationName; // 指定的调度工艺ID
    private String version; // 指定的调度工艺版本

    public String getDispatchOperationId() {
        return dispatchOperationId;
    }

    public void setDispatchOperationId(String dispatchOperationId) {
        this.dispatchOperationId = dispatchOperationId;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
