package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/10 16:56
 * @Description:
 */
public class MtBomSubstituteVO19 implements Serializable {
    private static final long serialVersionUID = 3061483030846488549L;

    @ApiModelProperty("替代料ID")
    private String bomSubstituteId;
    @ApiModelProperty("替代组ID")
    private String bomSubstituteGroupId;
    @ApiModelProperty("替代物料")
    private String materialId;

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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
