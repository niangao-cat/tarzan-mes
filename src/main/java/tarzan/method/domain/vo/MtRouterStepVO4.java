package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterStepVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1516730634054515876L;
    private String routerStepId;
    private String lang;
    private String description;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
