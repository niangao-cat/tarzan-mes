package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2020/11/2 09:59
 */
public class MtAssembleProcessActualVO13  implements Serializable {
	private static final long serialVersionUID = -5229177961295866539L;

	@ApiModelProperty("装配确认实绩ID")
	private String assembleConfirmActualId;

	@ApiModelProperty("装配过程实绩ID")
	private String assembleProcessActualId;

	@ApiModelProperty("本次装配数量")
	private Double assembleQty;
	@ApiModelProperty("本次报废数量")
	private Double scrappedQty;
	@ApiModelProperty("装配时工艺路线")
	private String routerId;
	@ApiModelProperty("装配时子步骤")
	private String substepId;
	@ApiModelProperty("装配时所在工作单元")
	private String workcellId;
	@ApiModelProperty("装配时所使用的装配组")
	private String assembleGroupId;
	@ApiModelProperty("装配时所使用的装配点")
	private String assemblePointId;
	@ApiModelProperty("装配时对应的参考区域")
	private String referenceArea;
	@ApiModelProperty("装配时对应的参考点")
	private String referencePoint;
	@ApiModelProperty("装配时所在货位")
	private String locatorId;
	@ApiModelProperty("装配方式")
	private String assembleMethod;
	@ApiModelProperty("装配时所在工艺路线步骤")
	private String routerStepId;
	@ApiModelProperty("装配使用的物料批")
	private String materialLotId;

	public String getAssembleConfirmActualId() {
		return assembleConfirmActualId;
	}

	public void setAssembleConfirmActualId(String assembleConfirmActualId) {
		this.assembleConfirmActualId = assembleConfirmActualId;
	}

	public String getAssembleProcessActualId() {
		return assembleProcessActualId;
	}

	public void setAssembleProcessActualId(String assembleProcessActualId) {
		this.assembleProcessActualId = assembleProcessActualId;
	}

	public Double getAssembleQty() {
		return assembleQty;
	}

	public void setAssembleQty(Double assembleQty) {
		this.assembleQty = assembleQty;
	}

	public Double getScrappedQty() {
		return scrappedQty;
	}

	public void setScrappedQty(Double scrappedQty) {
		this.scrappedQty = scrappedQty;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	public String getSubstepId() {
		return substepId;
	}

	public void setSubstepId(String substepId) {
		this.substepId = substepId;
	}

	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}

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

	public String getReferenceArea() {
		return referenceArea;
	}

	public void setReferenceArea(String referenceArea) {
		this.referenceArea = referenceArea;
	}

	public String getReferencePoint() {
		return referencePoint;
	}

	public void setReferencePoint(String referencePoint) {
		this.referencePoint = referencePoint;
	}

	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}

	public String getAssembleMethod() {
		return assembleMethod;
	}

	public void setAssembleMethod(String assembleMethod) {
		this.assembleMethod = assembleMethod;
	}

	public String getRouterStepId() {
		return routerStepId;
	}

	public void setRouterStepId(String routerStepId) {
		this.routerStepId = routerStepId;
	}

	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
}
