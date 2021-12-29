package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Leeloing
 * @date 2019/7/2 15:04
 */
public class MtMqttMessageVO2 implements Serializable {
    private static final long serialVersionUID = -8117975520956537445L;

    @JsonProperty("TagName")
    private String tagName;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("ItemValue")
    private String itemValue;

    @JsonProperty("GetDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date getDate;

    @JsonProperty("PublishFlag")
    private String publishFlag;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Date getGetDate() {
        if (getDate == null) {
            return null;
        } else {
            return (Date) getDate.clone();
        }
    }

    public void setGetDate(Date getDate) {
        if (getDate == null) {
            this.getDate = null;
        } else {
            this.getDate = (Date) getDate.clone();
        }
    }

    public String getPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(String publishFlag) {
        this.publishFlag = publishFlag;
    }

    public String getRecordChangesFlag() {
        return recordChangesFlag;
    }

    public void setRecordChangesFlag(String recordChangesFlag) {
        this.recordChangesFlag = recordChangesFlag;
    }

    private String recordChangesFlag;
}
