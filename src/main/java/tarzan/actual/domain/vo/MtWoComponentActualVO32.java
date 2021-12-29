package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/10/31 13:47
 */
public class MtWoComponentActualVO32 implements Serializable {
	private static final long serialVersionUID = -477220398617472386L;
	@ApiModelProperty("工单")
	private String workOrderId;
	@ApiModelProperty("装配清单")
	private String bomId;
	@ApiModelProperty("步骤Id")
	private String routerStepId;
	@ApiModelProperty("工艺路线类型")
	private String assembleRouterType;
	@ApiModelProperty("工序装配标识")
	private String operationAssembleFlag;

	private List<MtEoComponentActualVO29> materialInfo;

	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	public String getBomId() {
		return bomId;
	}

	public void setBomId(String bomId) {
		this.bomId = bomId;
	}

	public String getRouterStepId() {
		return routerStepId;
	}

	public void setRouterStepId(String routerStepId) {
		this.routerStepId = routerStepId;
	}

	public String getAssembleRouterType() {
		return assembleRouterType;
	}

	public void setAssembleRouterType(String assembleRouterType) {
		this.assembleRouterType = assembleRouterType;
	}

	public String getOperationAssembleFlag() {
		return operationAssembleFlag;
	}

	public void setOperationAssembleFlag(String operationAssembleFlag) {
		this.operationAssembleFlag = operationAssembleFlag;
	}

	public List<MtEoComponentActualVO29> getMaterialInfo() {
		return materialInfo;
	}

	public void setMaterialInfo(List<MtEoComponentActualVO29> materialInfo) {
		this.materialInfo = materialInfo;
	}
}
