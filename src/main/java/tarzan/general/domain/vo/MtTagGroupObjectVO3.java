package tarzan.general.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author liyuan.lv@hand-china.com
 * @date 2019-09-18 15:18
 */
public class MtTagGroupObjectVO3 implements Serializable {

    private static final long serialVersionUID = 1957373030212179161L;
    @ApiModelProperty(value = "数据采集组")
    private String tagGroupId;
    @ApiModelProperty(value = "数据采集扩展类型")
    private String attrValue;

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
