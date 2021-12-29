package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/4/3 9:28
 */
public class MtContainerVO1 implements Serializable {
    private static final long serialVersionUID = 3713565620082375642L;

    private List<String> locatorIds;
    private String enableFlag;

    public List<String> getLocatorIds() {
        return locatorIds;
    }

    public void setLocatorIds(List<String> locatorIds) {
        this.locatorIds = locatorIds;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
