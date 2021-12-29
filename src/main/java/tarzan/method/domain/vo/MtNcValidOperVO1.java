package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtNcValidOperVO1 implements Serializable {
    private static final long serialVersionUID = 3029424865328831564L;

    private String ncCodeId; // 不良代码
    private String operationId; // 工艺

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
