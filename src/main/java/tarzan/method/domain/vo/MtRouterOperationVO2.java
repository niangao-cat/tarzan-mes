package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterOperationVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8529064816053531022L;
    private String routerOperationId;
    private String lang;
    private String specialIntruction;

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSpecialIntruction() {
        return specialIntruction;
    }

    public void setSpecialIntruction(String specialIntruction) {
        this.specialIntruction = specialIntruction;
    }

}
