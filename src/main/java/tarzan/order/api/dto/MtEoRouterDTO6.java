package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/7 2:04 下午
 */
public class MtEoRouterDTO6 implements Serializable {
    private static final long serialVersionUID = -8482413097863105482L;
    @ApiModelProperty("数据收集组ID")
    private String tagGroupId;
    @ApiModelProperty("数据收集组编码")
    private String tagGroupCode;
    @ApiModelProperty("数据收集组描述")
    private String tagGroupDescription;

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

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

    public MtEoRouterDTO6(String tagGroupId, String tagGroupCode, String tagGroupDescription) {
        this.tagGroupId = tagGroupId;
        this.tagGroupCode = tagGroupCode;
        this.tagGroupDescription = tagGroupDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoRouterDTO6 that = (MtEoRouterDTO6) o;
        return Objects.equals(getTagGroupId(), that.getTagGroupId())
                        && Objects.equals(getTagGroupCode(), that.getTagGroupCode())
                        && Objects.equals(getTagGroupDescription(), that.getTagGroupDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagGroupId(), getTagGroupCode(), getTagGroupDescription());
    }
}
