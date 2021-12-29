package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtExtendAttrVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1757755242623013506L;
    private String attrId;
    private String attrHisId;
    private String mainTableKeyValue;
    private String hisTableKeyValue;
    private String attrName;
    private String attrValue;
    private String lang;
    private Date createDate;
    private Long createBy;
    private Long objectVersionNumber;
    private Long cid;
    private Long hisCid;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getMainTableKeyValue() {
        return mainTableKeyValue;
    }

    public void setMainTableKeyValue(String mainTableKeyValue) {
        this.mainTableKeyValue = mainTableKeyValue;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Date getCreateDate() {
        if (createDate == null) {
            return null;
        } else {
            return (Date) createDate.clone();
        }
    }

    public void setCreateDate(Date createDate) {
        if (createDate == null) {
            this.createDate = null;
        } else {
            this.createDate = (Date) createDate.clone();
        }
    }

    public String getHisTableKeyValue() {
        return hisTableKeyValue;
    }

    public void setHisTableKeyValue(String hisTableKeyValue) {
        this.hisTableKeyValue = hisTableKeyValue;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getAttrHisId() {
        return attrHisId;
    }

    public void setAttrHisId(String attrHisId) {
        this.attrHisId = attrHisId;
    }

    public Long getHisCid() {
        return hisCid;
    }

    public void setHisCid(Long hisCid) {
        this.hisCid = hisCid;
    }
}
