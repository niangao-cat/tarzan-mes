package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 3:35 下午
 */
public class MtBomSubstituteVO15 implements Serializable {
    private static final long serialVersionUID = 4084531874430851190L;
    @ApiModelProperty("组件参数列表")
    private List<MtBomSubstituteVO14> bomComponentList;
    @ApiModelProperty("是否考虑替代标识")
    private String substituteFlag;
    @ApiModelProperty("组件物料Id列表")
    private List<String> materialIds;

    public List<MtBomSubstituteVO14> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtBomSubstituteVO14> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public List<String> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<String> materialIds) {
        this.materialIds = materialIds;
    }
}
