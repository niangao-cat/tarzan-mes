package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.entity.MtBomSubstituteGroup;

/**
 * @author Leeloing
 * @date 2020/10/22 23:03
 */
public class MtBomSubstituteGroupVO6 implements Serializable {
    private static final long serialVersionUID = -1463646511446391897L;

    @ApiModelProperty(value = "组件行主键")
    private String bomComponentId;

    @ApiModelProperty(value = "替代组")
    private MtBomSubstituteGroup mtBomSubstituteGroup;

    @ApiModelProperty(value = "替代项")
    private List<MtBomSubstitute> mtBomSubstitutes;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public MtBomSubstituteGroup getMtBomSubstituteGroup() {
        return mtBomSubstituteGroup;
    }

    public void setMtBomSubstituteGroup(MtBomSubstituteGroup mtBomSubstituteGroup) {
        this.mtBomSubstituteGroup = mtBomSubstituteGroup;
    }

    public List<MtBomSubstitute> getMtBomSubstitutes() {
        return mtBomSubstitutes;
    }

    public void setMtBomSubstitutes(List<MtBomSubstitute> mtBomSubstitutes) {
        this.mtBomSubstitutes = mtBomSubstitutes;
    }
}
