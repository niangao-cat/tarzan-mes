package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModLocatorGroupVO2 implements Serializable {

    private static final long serialVersionUID = 7354084268677155003L;
    private String locatorGroupId;
    private String locatorGroupCode; // 库位组编码
    private String locatorGroupName; // 库位组名称
    private String enableFlag; // 是否有效

    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }



}
