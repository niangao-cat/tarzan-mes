package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtModLocatorVO7
 * @description
 * @date 2019年09月29日 15:36
 */
public class MtModLocatorVO7 implements Serializable {
    private static final long serialVersionUID = -8750095837188717853L;

    private String locatorId;// 库位ID
    private String locatorCode;// 库位编码
    private String locatorName;// 库位名称
    private String locatorLocation;// 库位位置
    private String enableFlag;// 有效标识
    private String locatorType;// 库位类型
    private String locatorCategory;// 库位类别
    private String negativeFlag;// 负库存标识
    private String locatorGroupId;// 所属库位组ID
    private String parentLocatorId;// 父层库位ID

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

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

    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String parentLocatorId) {
        this.parentLocatorId = parentLocatorId;
    }

}
