package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/23 8:04 下午
 */
public class MtEoDTO4 implements Serializable {
    private static final long serialVersionUID = 4220138846117947556L;
    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty("EO状态")
    private List<String> status;
    @ApiModelProperty("执行作业类型")
    private List<String> eoType;
    @ApiModelProperty("生产线ID")
    private String productionLineId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("WO状态")
    private List<String> workOrderStatus;
    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("开始时间从")
    private Date startTimeFrom;
    @ApiModelProperty("开始时间至")
    private Date startTimeTo;
    @ApiModelProperty("结束时间从")
    private Date endTimeFrom;
    @ApiModelProperty("结束时间至")
    private Date endTimeTo;

    //2020-07-25 add by sanfeng.zhang
    //EO标识
    @ApiModelProperty("EO标识")
    private String eoIdentification;

    //2020-12-24 add by sanfeng.zahng for wuyongjiang
    @ApiModelProperty("工序id")
    private String processId;

    //2021-08-26 add by sanfeng.zahng for hui.gu 工单批量
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("工单集合")
    private List<String> workOrderNumList;

    @ApiModelProperty("绑定SN标识")
    private String bindSnFlag;

    @ApiModelProperty("EO主键列表")
    private List<String> eoIdList;

    @ApiModelProperty("返修SN,以逗号分隔的字符串")
    private String repairSn;

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getEoType() {
        return eoType;
    }

    public void setEoType(List<String> eoType) {
        this.eoType = eoType;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<String> getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(List<String> workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getStartTimeFrom() {
        if (startTimeFrom == null) {
            return null;
        } else {
            return (Date) startTimeFrom.clone();
        }
    }

    public void setStartTimeFrom(Date startTimeFrom) {
        if (startTimeFrom == null) {
            this.startTimeFrom = null;
        } else {
            this.startTimeFrom = (Date) startTimeFrom.clone();
        }
    }

    public Date getStartTimeTo() {
        if (startTimeTo == null) {
            return null;
        } else {
            return (Date) startTimeTo.clone();
        }
    }

    public void setStartTimeTo(Date startTimeTo) {
        if (startTimeTo == null) {
            this.startTimeTo = null;
        } else {
            this.startTimeTo = (Date) startTimeTo.clone();
        }
    }

    public Date getEndTimeFrom() {
        if (endTimeFrom == null) {
            return null;
        } else {
            return (Date) endTimeFrom.clone();
        }
    }

    public void setEndTimeFrom(Date endTimeFrom) {
        if (endTimeFrom == null) {
            this.endTimeFrom = null;
        } else {
            this.endTimeFrom = (Date) endTimeFrom.clone();
        }
    }

    public Date getEndTimeTo() {
        if (endTimeTo == null) {
            return null;
        } else {
            return (Date) endTimeTo.clone();
        }
    }

    public void setEndTimeTo(Date endTimeTo) {
        if (endTimeTo == null) {
            this.endTimeTo = null;
        } else {
            this.endTimeTo = (Date) endTimeTo.clone();
        }
    }

    public String getEoIdentification() {
        return eoIdentification;
    }

    public void setEoIdentification(String eoIdentification) {
        this.eoIdentification = eoIdentification;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<String> getEoIdList() {
        return eoIdList;
    }

    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
    }

    public String getRepairSn() {
        return repairSn;
    }

    public void setRepairSn(String repairSn) {
        this.repairSn = repairSn;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public List<String> getWorkOrderNumList() {
        return workOrderNumList;
    }

    public void setWorkOrderNumList(List<String> workOrderNumList) {
        this.workOrderNumList = workOrderNumList;
    }

    public String getBindSnFlag() {
        return bindSnFlag;
    }

    public void setBindSnFlag(String bindSnFlag) {
        this.bindSnFlag = bindSnFlag;
    }
}
