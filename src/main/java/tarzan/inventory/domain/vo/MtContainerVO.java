package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/3 8:49
 */
public class MtContainerVO implements Serializable {
    private static final long serialVersionUID = -3544383461130554430L;

    private String locatorId;
    private String enableFlag;
    private String emptyFlag;
    private String subLocatorFlag;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(String emptyFlag) {
        this.emptyFlag = emptyFlag;
    }

    public String getSubLocatorFlag() {
        return subLocatorFlag;
    }

    public void setSubLocatorFlag(String subLocatorFlag) {
        this.subLocatorFlag = subLocatorFlag;
    }
}
