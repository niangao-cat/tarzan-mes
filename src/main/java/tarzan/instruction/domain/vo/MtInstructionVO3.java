package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * instructionExecute-指令执行时传入参数使用VO类
 * 
 * @author benjamin
 * @date 2019-06-20 10:48
 */
public class MtInstructionVO3 implements Serializable {

    private static final long serialVersionUID = -777932468222786310L;

    /**
     * 指令Id
     */
    @ApiModelProperty("指令Id")
    private String instructionId;

    /**
     * 物料批信息列表
     */
    @ApiModelProperty("物料批信息列表")
    private List<MaterialLotList> materialLotMessageList;

    /**
     * 事件组Id
     */
    @ApiModelProperty("事件组Id")
    private String eventRequestId;

    @ApiModelProperty("物料信息列表")
    private List<MaterialMessageList> materialMessageList;

    @ApiModelProperty("容器信息列表")
    private List<ContainerMessageList> containerMessageList;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<MaterialLotList> getMaterialLotMessageList() {
        return materialLotMessageList;
    }

    public void setMaterialLotMessageList(List<MaterialLotList> materialLotMessageList) {
        this.materialLotMessageList = materialLotMessageList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public List<MaterialMessageList> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MaterialMessageList> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }

    public List<ContainerMessageList> getContainerMessageList() {
        return containerMessageList;
    }

    public void setContainerMessageList(List<ContainerMessageList> containerMessageList) {
        this.containerMessageList = containerMessageList;
    }


    /**
     * 物料批信息
     * 
     * @author benjamin
     * @date 2019-06-20 10:53
     */
    public static class MaterialLotList implements Serializable {
        private static final long serialVersionUID = -1387316858478498919L;
        /**
         * 物料批Id
         */
        private String materialLotId;
        /**
         * 数量
         */
        private Double qty;
        /**
         * 单位Id
         */
        private String uomId;

        /**
         * 容器Id
         */
        private String containerId;

        private String fromLocatorId;

        private String toLocatorId;

        public String getMaterialLotId() {
            return materialLotId;
        }

        public void setMaterialLotId(String materialLotId) {
            this.materialLotId = materialLotId;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public String getUomId() {
            return uomId;
        }

        public void setUomId(String uomId) {
            this.uomId = uomId;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
        }

        public String getFromLocatorId() {
            return fromLocatorId;
        }

        public void setFromLocatorId(String fromLocatorId) {
            this.fromLocatorId = fromLocatorId;
        }

        public String getToLocatorId() {
            return toLocatorId;
        }

        public void setToLocatorId(String toLocatorId) {
            this.toLocatorId = toLocatorId;
        }

    }

    public static class MaterialMessageList implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -7761564415937859000L;

        private String materialId;

        private Double qty;

        private String fromLocatorId;

        private String toLocatorId;

        public String getMaterialId() {
            return materialId;
        }

        public void setMaterialId(String materialId) {
            this.materialId = materialId;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public String getFromLocatorId() {
            return fromLocatorId;
        }

        public void setFromLocatorId(String fromLocatorId) {
            this.fromLocatorId = fromLocatorId;
        }

        public String getToLocatorId() {
            return toLocatorId;
        }

        public void setToLocatorId(String toLocatorId) {
            this.toLocatorId = toLocatorId;
        }
    }


    public static class ContainerMessageList implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1606776181547131858L;
        private String containerId;
        private String toLocatorId;

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
        }

        public String getToLocatorId() {
            return toLocatorId;
        }

        public void setToLocatorId(String toLocatorId) {
            this.toLocatorId = toLocatorId;
        }
    }

    public static class MaterialLotNewList implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 2397339220338048387L;
        private String materialLotId;
        private String primaryUomId;
        private Double primaryUomQty;
        private String locatorId;
        private String containerId;
        private String toLocatorId;

        public String getMaterialLotId() {
            return materialLotId;
        }

        public void setMaterialLotId(String materialLotId) {
            this.materialLotId = materialLotId;
        }

        public String getPrimaryUomId() {
            return primaryUomId;
        }

        public void setPrimaryUomId(String primaryUomId) {
            this.primaryUomId = primaryUomId;
        }

        public Double getPrimaryUomQty() {
            return primaryUomQty;
        }

        public void setPrimaryUomQty(Double primaryUomQty) {
            this.primaryUomQty = primaryUomQty;
        }

        public String getLocatorId() {
            return locatorId;
        }

        public void setLocatorId(String locatorId) {
            this.locatorId = locatorId;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setContainerId(String containerId) {
            this.containerId = containerId;
        }

        public String getToLocatorId() {
            return toLocatorId;
        }

        public void setToLocatorId(String toLocatorId) {
            this.toLocatorId = toLocatorId;
        }


    }
}
