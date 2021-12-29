package tarzan.general.api.dto;

import java.io.Serializable;

public class MtTagGroupDTO implements Serializable {
    private static final long serialVersionUID = -8934128200720949813L;

    private String tagGroupCode;
    private String tagGroupDescription;
    private String tagGroupType;
    private String sourceGroupId;
    private String businessType;
    private String status;
    private String collectionTimeControl;
    private String userVerification;

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }
}
