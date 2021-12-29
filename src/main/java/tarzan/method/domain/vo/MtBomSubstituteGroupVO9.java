package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/10 16:10
 * @Description:
 */
public class MtBomSubstituteGroupVO9 implements Serializable {
    private static final long serialVersionUID = 4967395151413690342L;

    @ApiModelProperty("装配清单ID")
    private String bomComponentId;
    @ApiModelProperty("替代组编码")
    private String substituteGroup;

    public MtBomSubstituteGroupVO9() {}

    public MtBomSubstituteGroupVO9(String bomComponentId, String substituteGroup) {
        this.bomComponentId = bomComponentId;
        this.substituteGroup = substituteGroup;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtBomSubstituteGroupVO9 that = (MtBomSubstituteGroupVO9) o;
        return Objects.equals(bomComponentId, that.bomComponentId)
                        && Objects.equals(substituteGroup, that.substituteGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomComponentId, substituteGroup);
    }
}
