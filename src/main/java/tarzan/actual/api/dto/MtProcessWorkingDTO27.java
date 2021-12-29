package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/20 11:06 上午
 */
public class MtProcessWorkingDTO27 implements Serializable {
    private static final long serialVersionUID = -3792511618757547043L;
    @ApiModelProperty(value = "收集组Id", required = true)
    private String tagGroupId;
    @ApiModelProperty(value = "eo步骤实绩ID", required = true)
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
