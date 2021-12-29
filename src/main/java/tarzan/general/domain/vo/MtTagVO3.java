package tarzan.general.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 11:14
 */
public class MtTagVO3 implements Serializable {

    private static final long serialVersionUID = -8682018187482954663L;
    private String collectionMethod;// 收集方法
    private String tagCode;// 数据项编码
    private String tagId;// 数据项ID
    private String valueType;// 数据类型
    private String remark;// 备注
    private String valueAllowMissing;// 允许缺失值
    private String enableFlag;// 是否有效
    private String apiId;// 转换API
    private String tagDescription;// 数据项描述

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValueAllowMissing() {
        return valueAllowMissing;
    }

    public void setValueAllowMissing(String valueAllowMissing) {
        this.valueAllowMissing = valueAllowMissing;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

}
