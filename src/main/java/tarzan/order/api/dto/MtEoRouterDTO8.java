package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/8 10:36 上午
 */
public class MtEoRouterDTO8 implements Serializable {
    private static final long serialVersionUID = 815140685002039737L;

    @ApiModelProperty("数据收集组ID")
    private String tagGroupId;

    @ApiModelProperty("步骤实绩ID")
    private String eoStepActualId;

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
