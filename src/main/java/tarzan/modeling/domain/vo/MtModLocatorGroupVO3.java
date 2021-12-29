package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtModLocatorGroupVO3
 * @description
 * @date 2019年09月29日 14:42
 */
public class MtModLocatorGroupVO3 implements Serializable {
    private static final long serialVersionUID = 8766115572279934986L;

    private String locatorGroupId;// 库位组ID
    private String locatorGroupCode;// 库位组编码
    private String locatorGroupName;// 库位组名称
    private String enableFlag;// 有效标识

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
