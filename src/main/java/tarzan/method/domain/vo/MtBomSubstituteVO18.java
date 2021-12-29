package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/4/19 22:44
 * @Description:
 */
public class MtBomSubstituteVO18 implements Serializable {
    private static final long serialVersionUID = -8227970613257832713L;

    @ApiModelProperty(value = "主键")
    private String bomSubstituteId;

    @ApiModelProperty(value = "替代组主键")
    private String bomSubstituteGroupId;

    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }
}
