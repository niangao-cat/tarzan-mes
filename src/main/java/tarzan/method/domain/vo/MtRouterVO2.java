package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1516730634054515876L;
    private String routerId;
    private String lang;
    private String description;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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
