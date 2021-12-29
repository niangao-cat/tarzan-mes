package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomVO8 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6442780951323197054L;
    private String bomId;
    private String lang;
    private String description;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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
