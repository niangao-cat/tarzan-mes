package tarzan.modeling.api.dto;

import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorDTO implements Serializable {
    private static final long serialVersionUID = -8846871082455863218L;
    private String queryType;
    private String locatorId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }


    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }


}

