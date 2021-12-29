package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 3:39 下午
 */
public class MtBomSubstituteVO17 implements Serializable {
    private static final long serialVersionUID = -8925209136980629892L;
    @ApiModelProperty("组件参数列表")
    private List<MtBomSubstituteVO16> substituteMaterialList;
    @ApiModelProperty("装配清单行ID")
    private String bomComponentId;
    @ApiModelProperty("装配清单行数量")
    private Double qty;
    private String substituteFlag;

    public List<MtBomSubstituteVO16> getSubstituteMaterialList() {
        return substituteMaterialList;
    }

    public void setSubstituteMaterialList(List<MtBomSubstituteVO16> substituteMaterialList) {
        this.substituteMaterialList = substituteMaterialList;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

}
