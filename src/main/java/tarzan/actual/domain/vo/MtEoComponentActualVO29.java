package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2020/10/31 10:52
 */
public class MtEoComponentActualVO29  implements Serializable {
	private static final long serialVersionUID = 5418924167083583092L;
	@ApiModelProperty("物料Id")
	private String materialId;

	@ApiModelProperty("装配清单组件")
	private String bomComponentId;

	@ApiModelProperty("装配数量")
	private Double trxAssembleQty;

	@ApiModelProperty("强制装配标识")
	private String assembleExcessFlag;

	@ApiModelProperty("替代标识")
	private String substituteFlag;

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getBomComponentId() {
		return bomComponentId;
	}

	public void setBomComponentId(String bomComponentId) {
		this.bomComponentId = bomComponentId;
	}

	public Double getTrxAssembleQty() {
		return trxAssembleQty;
	}

	public void setTrxAssembleQty(Double trxAssembleQty) {
		this.trxAssembleQty = trxAssembleQty;
	}

	public String getAssembleExcessFlag() {
		return assembleExcessFlag;
	}

	public void setAssembleExcessFlag(String assembleExcessFlag) {
		this.assembleExcessFlag = assembleExcessFlag;
	}

	public String getSubstituteFlag() {
		return substituteFlag;
	}

	public void setSubstituteFlag(String substituteFlag) {
		this.substituteFlag = substituteFlag;
	}
}
