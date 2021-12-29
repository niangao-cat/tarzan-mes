package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/7 10:48 上午
 */
public class MtAssembleConfirmActualDTO3 implements Serializable {
    private static final long serialVersionUID = 1618021148744947558L;
    @ApiModelProperty("装配确认实绩ID")
    private String assembleConfirmActualId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码+名称")
    private String materialCodeName;


    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺名称")
    private String operation;

    @ApiModelProperty("装配确认标识")
    private String confirmFlag;
    @ApiModelProperty("装配确认人Id")
    private Long confirmBy;
    @ApiModelProperty("装配确认人")
    private String confirmByName;

    @ApiModelProperty("装配数量-过程")
    private Double totalAssembleQty;
    @ApiModelProperty("报废数量-过程")
    private Double totalScrapQty;

    @ApiModelProperty("装配确认时间")
    private Date assembleLastDate;

    @ApiModelProperty("装配过程实绩ID")
    private List<MtAssembleProcessActualDTO> assembleProcessActualList;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCodeName() {
        return materialCodeName;
    }

    public void setMaterialCodeName(String materialCodeName) {
        this.materialCodeName = materialCodeName;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Long getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(Long confirmBy) {
        this.confirmBy = confirmBy;
    }

    public String getConfirmByName() {
        return confirmByName;
    }

    public void setConfirmByName(String confirmByName) {
        this.confirmByName = confirmByName;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public List<MtAssembleProcessActualDTO> getAssembleProcessActualList() {
        return assembleProcessActualList;
    }

    public void setAssembleProcessActualList(List<MtAssembleProcessActualDTO> assembleProcessActualList) {
        this.assembleProcessActualList = assembleProcessActualList;
    }

    public Double getTotalAssembleQty() {
        return totalAssembleQty;
    }

    public void setTotalAssembleQty(Double totalAssembleQty) {
        this.totalAssembleQty = totalAssembleQty;
    }

    public Double getTotalScrapQty() {
        return totalScrapQty;
    }

    public void setTotalScrapQty(Double totalScrapQty) {
        this.totalScrapQty = totalScrapQty;
    }

    public Date getAssembleLastDate() {
        if (assembleLastDate == null) {
            return null;
        } else {
            return (Date) assembleLastDate.clone();
        }
    }

    public void setAssembleLastDate(Date assembleLastDate) {
        if (assembleLastDate == null) {
            this.assembleLastDate = null;
        } else {
            this.assembleLastDate = (Date) assembleLastDate.clone();
        }
    }
}
