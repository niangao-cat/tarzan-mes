package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobSnLotMaterialVO5 implements Serializable {

    private static final long serialVersionUID = 1808815301911895304L;
    @ApiModelProperty("物料ID")
    private String materialLotId;
    @ApiModelProperty("类型")
    private String materialType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnLotMaterialVO5 that = (HmeEoJobSnLotMaterialVO5) o;
        return Objects.equals(materialLotId, that.materialLotId) &&
                Objects.equals(materialType, that.materialType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialLotId, materialType);
    }
}
