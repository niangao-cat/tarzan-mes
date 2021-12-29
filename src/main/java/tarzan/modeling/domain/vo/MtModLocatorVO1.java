package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModLocatorVO1 implements Serializable {

    private static final long serialVersionUID = -6537647532542705480L;

    private String locatorCode; // 货位编码
    private String locatorName; // 货位名称
    private String locatorLocation; // 货位位置
    private String enableFlag; // 是否有效
    private String locatorType; // 货位类型
    private String locatorCategory; // 库位类别
    private String negativeFlag; // 负库存标识

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getLocatorLocation() {
        return locatorLocation;
    }

    public void setLocatorLocation(String locatorLocation) {
        this.locatorLocation = locatorLocation;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    public String getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(String negativeFlag) {
        this.negativeFlag = negativeFlag;
    }
}
