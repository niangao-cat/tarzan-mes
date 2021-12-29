package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 2:54 下午
 */
public class MtBomSubstituteGroupVO7 implements Serializable {
    private static final long serialVersionUID = 173404207384025265L;
    @ApiModelProperty("装配清单行Id")
    private String bomComponentId;

    @ApiModelProperty("替代组属性ID")
    private String bomSubstituteGroupId;

    @ApiModelProperty(value = "替代策略")
    private String substitutePolicy;

    @ApiModelProperty("替代项属性")
    private List<MtBomSubstituteGroupVO8> bomSubstituteList;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }

    public List<MtBomSubstituteGroupVO8> getBomSubstituteList() {
        return bomSubstituteList;
    }

    public void setBomSubstituteList(List<MtBomSubstituteGroupVO8> bomSubstituteList) {
        this.bomSubstituteList = bomSubstituteList;
    }
}
