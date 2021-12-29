package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/22 9:44
 */
public class MtContainerVO23 implements Serializable {

    private static final long serialVersionUID = 7121022680114682764L;
    private String inputContainerId;
    private String parentContainerId;
    private String needConsumeContainerId;
    private String consumePrimaryUomId;
    private String consumeSecondaryUomId;
    private String materialId;
    private String reservedObjectId;
    private String reservedObjectType;
    private Long parentSequence;
    private Long currentSequence;
    private Double needConsumePrimaryQty;
    private Double needConsumeSecondaryQty;

    public String getInputContainerId() {
        return inputContainerId;
    }

    public void setInputContainerId(String inputContainerId) {
        this.inputContainerId = inputContainerId;
    }

    public String getParentContainerId() {
        return parentContainerId;
    }

    public void setParentContainerId(String parentContainerId) {
        this.parentContainerId = parentContainerId;
    }

    public String getNeedConsumeContainerId() {
        return needConsumeContainerId;
    }

    public void setNeedConsumeContainerId(String needConsumeContainerId) {
        this.needConsumeContainerId = needConsumeContainerId;
    }

    public String getConsumePrimaryUomId() {
        return consumePrimaryUomId;
    }

    public void setConsumePrimaryUomId(String consumePrimaryUomId) {
        this.consumePrimaryUomId = consumePrimaryUomId;
    }

    public String getConsumeSecondaryUomId() {
        return consumeSecondaryUomId;
    }

    public void setConsumeSecondaryUomId(String consumeSecondaryUomId) {
        this.consumeSecondaryUomId = consumeSecondaryUomId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public Long getParentSequence() {
        return parentSequence;
    }

    public void setParentSequence(Long parentSequence) {
        this.parentSequence = parentSequence;
    }

    public Long getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Long currentSequence) {
        this.currentSequence = currentSequence;
    }

    public Double getNeedConsumePrimaryQty() {
        return needConsumePrimaryQty;
    }

    public void setNeedConsumePrimaryQty(Double needConsumePrimaryQty) {
        this.needConsumePrimaryQty = needConsumePrimaryQty;
    }

    public Double getNeedConsumeSecondaryQty() {
        return needConsumeSecondaryQty;
    }

    public void setNeedConsumeSecondaryQty(Double needConsumeSecondaryQty) {
        this.needConsumeSecondaryQty = needConsumeSecondaryQty;
    }
}
