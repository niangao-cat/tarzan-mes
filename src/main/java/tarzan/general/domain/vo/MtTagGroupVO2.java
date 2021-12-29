package tarzan.general.domain.vo;

import java.io.Serializable;

/**
 * 
 * @Author peng.yuan
 * @Date 2019/10/17 10:44
 */
public class MtTagGroupVO2 implements Serializable {

    private static final long serialVersionUID = -3577671629024769166L;
    private String userVerification;// 需要用户验证
    private String tagGroupId;// 数据组收集组ID
    private String tagGroupDescription;// 数据收集组描述
    private String tagGroupCode;// 数据收集组编码
    private String collectionTimeControl;// 数据收集时点
    private String businessType;// 业务类型
    private String tagGroupType;// 数据收集组类型
    private String sourceGroupId;// 源数据收集组ID
    private String status;// 状态
    private String productionVersion;//物料版本

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
    }
}
