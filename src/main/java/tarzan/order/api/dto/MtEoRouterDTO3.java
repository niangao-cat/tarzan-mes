package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 2:40 下午
 */
public class MtEoRouterDTO3 implements Serializable {
    private static final long serialVersionUID = -5909841502562257128L;
    @ApiModelProperty("数据项ID")
    private String tagId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty("收集方式")
    private String collectionMethod;
    @ApiModelProperty("收集方式描述")
    private String collectionMethodDesc;
    @ApiModelProperty("收集值")
    private String tagValue;
    @ApiModelProperty("判定结果")
    private String tagCalculateResult;
    @ApiModelProperty("记录人ID")
    private Long userId;
    @ApiModelProperty("记录人")
    private String userName;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTagCalculateResult() {
        return tagCalculateResult;
    }

    public void setTagCalculateResult(String tagCalculateResult) {
        this.tagCalculateResult = tagCalculateResult;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getCollectionMethodDesc() {
        return collectionMethodDesc;
    }

    public void setCollectionMethodDesc(String collectionMethodDesc) {
        this.collectionMethodDesc = collectionMethodDesc;
    }
}
