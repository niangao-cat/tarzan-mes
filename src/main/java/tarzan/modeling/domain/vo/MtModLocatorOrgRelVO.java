package tarzan.modeling.domain.vo;

import java.io.Serializable;


public class MtModLocatorOrgRelVO implements Serializable {

    private static final long serialVersionUID = 7282440211087648646L;

    private String locatorId;
    private Long sequence;


    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

}
