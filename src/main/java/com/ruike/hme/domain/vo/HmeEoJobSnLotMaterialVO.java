package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobSnLotMaterialVO implements Serializable {

    private static final long serialVersionUID = 1808815301911895304L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("类型")
    private String materialType;
    @ApiModelProperty("虚拟件标识")
    private String virtualFlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnLotMaterialVO that = (HmeEoJobSnLotMaterialVO) o;
        return Objects.equals(materialId, that.materialId) &&
                Objects.equals(materialType, that.materialType) &&
                Objects.equals(virtualFlag, that.virtualFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialType, virtualFlag);
    }
}
