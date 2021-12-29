package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtRouterReturnStep;

/**
 * Created by slj on 2018-12-12.
 */
public class MtRouterReturnStepVO extends MtRouterReturnStep implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4708380513517567965L;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    private String operationName;

}
