package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2020/10/30 19:21
 */
public class MtAssemblePointActualVO12 implements Serializable {
    private static final long serialVersionUID = 552827770038166928L;

    @ApiModelProperty("装配组ID")
    private String assembleGroupId;

    @ApiModelProperty("装配点ID")
    private String assemblePointId;

    @ApiModelProperty("装配点装载物料ID")
    private String materialId;

	public String getAssembleGroupId() {
		return assembleGroupId;
	}

	public void setAssembleGroupId(String assembleGroupId) {
		this.assembleGroupId = assembleGroupId;
	}

	public String getAssemblePointId() {
		return assemblePointId;
	}

	public void setAssemblePointId(String assemblePointId) {
		this.assemblePointId = assemblePointId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public MtAssemblePointActualVO12(String assembleGroupId, String assemblePointId, String materialId) {
        this.assembleGroupId = assembleGroupId;
        this.assemblePointId = assemblePointId;
        this.materialId = materialId;
    }

    public MtAssemblePointActualVO12() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtAssemblePointActualVO12 that = (MtAssemblePointActualVO12) o;
        return Objects.equals(assembleGroupId, that.assembleGroupId)
                        && Objects.equals(assemblePointId, that.assemblePointId)
                        && Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assembleGroupId, assemblePointId, materialId);
    }

}
