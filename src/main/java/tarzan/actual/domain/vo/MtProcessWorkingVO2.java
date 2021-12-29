package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao tang
 * @date 2020-02-11 18:07
 */
public class MtProcessWorkingVO2 implements Serializable {

	private static final long serialVersionUID = -5354993899752972273L;
	
    @ApiModelProperty(value = "EO主键", required = true)
    private String eoId;
    
    @ApiModelProperty(value = "EO状态", required = true)
    private String eoStatus;
    
    @ApiModelProperty(value = "EO编码", required = true)
    private String eoNum;
    
    @ApiModelProperty(value = "EO数量", required = true)
    private Double eoQty;
    
    @ApiModelProperty(value = "装配清单ID", required = true)
    private String bomId;
    
    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String routerId;
    
    @ApiModelProperty(value = "eo步骤实绩ID", required = true)
    private String eoStepActualId;
    
    @ApiModelProperty(value = "步骤实绩", required = true)
    private List<MtProcessWorkingVO3> eoStepActualList;
    
    @ApiModelProperty(value = "上步骤实绩", required = true)
    private List<MtProcessWorkingVO3> eoBackStepActualList;
    
    @ApiModelProperty(value = "步骤ID（对于特殊操作步骤ID就是操作ID）", required = true)
    private String routerStepId;
    
    @ApiModelProperty(value = "wkc实绩", required = true)
    private MtProcessWorkingVO4 wkcActual;
    
    @ApiModelProperty(value = "调度数量", required = true)
    private List<MtProcessWorkingVO5> dispatchActualList;
    
    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;
    
    @ApiModelProperty(value = "物料编码", required = true)
    private String materialCode;
    
    @ApiModelProperty(value = "物料名称", required = true)
    private String materialName;
    
    @ApiModelProperty(value = "生产指令ID", required = true)
    private String workOrderId;
    
    @ApiModelProperty(value = "生产指令编码", required = true)
    private String workOrderNum;
    
    @ApiModelProperty(value = "步骤排队在制品", required = true)
    private Double stepWipQueueQty;
    
    @ApiModelProperty(value = "松散标识", required = true)
    private String relaxedFlowFlag;

	public String getEoId() {
		return eoId;
	}

	public void setEoId(String eoId) {
		this.eoId = eoId;
	}

	public String getEoNum() {
		return eoNum;
	}

	public void setEoNum(String eoNum) {
		this.eoNum = eoNum;
	}

	public String getRouterStepId() {
		return routerStepId;
	}

	public void setRouterStepId(String routerStepId) {
		this.routerStepId = routerStepId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	public String getWorkOrderNum() {
		return workOrderNum;
	}

	public void setWorkOrderNum(String workOrderNum) {
		this.workOrderNum = workOrderNum;
	}

	public String getRelaxedFlowFlag() {
		return relaxedFlowFlag;
	}

	public void setRelaxedFlowFlag(String relaxedFlowFlag) {
		this.relaxedFlowFlag = relaxedFlowFlag;
	}

    public String getEoStatus() {
        return eoStatus;
    }

    public void setEoStatus(String eoStatus) {
        this.eoStatus = eoStatus;
    }

    public List<MtProcessWorkingVO3> getEoStepActualList() {
        return eoStepActualList;
    }

    public void setEoStepActualList(List<MtProcessWorkingVO3> eoStepActualList) {
        this.eoStepActualList = eoStepActualList;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public MtProcessWorkingVO4 getWkcActual() {
        return wkcActual;
    }

    public void setWkcActual(MtProcessWorkingVO4 wkcActual) {
        this.wkcActual = wkcActual;
    }

    public Double getEoQty() {
        return eoQty;
    }

    public void setEoQty(Double eoQty) {
        this.eoQty = eoQty;
    }

    public List<MtProcessWorkingVO5> getDispatchActualList() {
        return dispatchActualList;
    }

    public void setDispatchActualList(List<MtProcessWorkingVO5> dispatchActualList) {
        this.dispatchActualList = dispatchActualList;
    }

    public Double getStepWipQueueQty() {
        return stepWipQueueQty;
    }

    public void setStepWipQueueQty(Double stepWipQueueQty) {
        this.stepWipQueueQty = stepWipQueueQty;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public List<MtProcessWorkingVO3> getEoBackStepActualList() {
        return eoBackStepActualList;
    }

    public void setEoBackStepActualList(List<MtProcessWorkingVO3> eoBackStepActualList) {
        this.eoBackStepActualList = eoBackStepActualList;
    }

}
