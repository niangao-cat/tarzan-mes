package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/10 9:27
 * @Description:
 */
public class MtInstructionVO20 implements Serializable {
    private static final long serialVersionUID = 8677197361842785786L;

    @ApiModelProperty("指令Id")
    private String instructionId;

    @ApiModelProperty("物料批信息列表")
    private List<MaterialLotMessage> materialLotMessageList;

    @ApiModelProperty("物料信息列表")
    private List<MaterialMessage> materialMessageList;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public List<MaterialLotMessage> getMaterialLotMessageList() {
        return materialLotMessageList;
    }

    public void setMaterialLotMessageList(List<MaterialLotMessage> materialLotMessageList) {
        this.materialLotMessageList = materialLotMessageList;
    }

    public List<MaterialMessage> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MaterialMessage> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }

    /**
     * 物料信息
     *
     * @author chuang.yang
     * @date 2020/1/10
     */
    public static class MaterialMessage implements Serializable {
        private static final long serialVersionUID = -7761564415937859000L;

        @ApiModelProperty("物料ID")
        private String materialId;
        @ApiModelProperty("数量")
        private Double qty;
        @ApiModelProperty("来源库位ID")
        private String fromLocatorId;
        @ApiModelProperty("目标库位ID")
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

    /**
     * 物料批信息
     *
     * @author chuang.yang
     * @date 2020/1/10
     */
    public static class MaterialLotMessage implements Serializable {
        private static final long serialVersionUID = -1387316858478498919L;

        @ApiModelProperty("物料批ID")
        private String materialLotId;
        @ApiModelProperty("数量")
        private Double qty;
        @ApiModelProperty("单位ID")
        private String uomId;
        @ApiModelProperty("容器ID")
        private String containerId;
        @ApiModelProperty("来源库位ID")
        private String fromLocatorId;
        @ApiModelProperty("目标库位ID")
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
}
