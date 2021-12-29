package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/10 16:14
 * @Description:
 */
public class MtBomSubstituteGroupVO10 implements Serializable {
    private static final long serialVersionUID = 8125267595160260355L;

    @ApiModelProperty("替代组ID")
    private String bomSubstituteGroupId;
    @ApiModelProperty("装配清单ID")
    private String bomComponentId;
    @ApiModelProperty("替代组编码")
    private String substituteGroup;

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getSubstituteGroup() {
        return substituteGroup;
    }

    public void setSubstituteGroup(String substituteGroup) {
        this.substituteGroup = substituteGroup;
    }
}
