package tarzan.modeling.api.dto;

import java.io.Serializable;
/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorDTO3 implements Serializable {
    private static final long serialVersionUID = -8547607178042545136L;
    private String parentLocatorId;
    private String locatorId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }


    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String queryType) {
        this.parentLocatorId = queryType;
    }
}
