package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModOrganizationVO6
 *
 * @author: {xieyiyang}
 * @date: 2020/2/20 13:37
 * @description:
 */
public class MtModOrganizationVO6 implements Serializable {
    private static final long serialVersionUID = 6379056721082982965L;

    @ApiModelProperty("目标组织对象ID")
    private String parentOrganizationId;
    @ApiModelProperty("父层对象下的顺序")
    private Long sequence;

    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
