package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/6 17:43
 * @Author: ${yiyang.xie}
 */
public class MtBomSubstituteVO12 implements Serializable {
    private static final long serialVersionUID = 8181419510209970948L;

    @ApiModelProperty("主料所在的组件行ID")
    private String bomComponentId;

    @ApiModelProperty("替代列表")
    private List<MtBomSubstituteVO13> substituteList = new ArrayList<>();

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public List<MtBomSubstituteVO13> getSubstituteList() {
        return substituteList;
    }

    public void setSubstituteList(List<MtBomSubstituteVO13> substituteList) {
        this.substituteList = substituteList;
    }
}
