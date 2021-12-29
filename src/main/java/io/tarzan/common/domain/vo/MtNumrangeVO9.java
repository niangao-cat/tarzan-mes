package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-14 09:21
 */
public class MtNumrangeVO9 implements Serializable {
    private static final long serialVersionUID = -4495554976903233065L;
    @ApiModelProperty(value = "编码对象编码")
    private String objectCode;
    @ApiModelProperty(value = "编码对象类型")
    private String objectTypeCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "外部输入编码列表")
    private List<String> outsideNumList;
    @ApiModelProperty(value = "生成编号的数量")
    private Long numQty;
    @ApiModelProperty(value = "是否重复生成标识")
    private String objectNumFlag;
    @ApiModelProperty(value = "调用标准对象传入参数列表")
    private List<MtNumrangeVO10> callObjectCodeList = new ArrayList<>();
    @ApiModelProperty(value = "传入值参数列表")
    private List<MtNumrangeVO11> incomingValueList = new ArrayList<>();

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<String> getOutsideNumList() {
        return outsideNumList;
    }

    public void setOutsideNumList(List<String> outsideNumList) {
        this.outsideNumList = outsideNumList;
    }

    public String getObjectNumFlag() {
        return objectNumFlag;
    }

    public void setObjectNumFlag(String objectNumFlag) {
        this.objectNumFlag = objectNumFlag;
    }

    public Long getNumQty() {
        return numQty;
    }

    public void setNumQty(Long numQty) {
        this.numQty = numQty;
    }

    public List<MtNumrangeVO10> getCallObjectCodeList() {
        return callObjectCodeList;
    }

    public void setCallObjectCodeList(List<MtNumrangeVO10> callObjectCodeList) {
        this.callObjectCodeList = callObjectCodeList;
    }

    public List<MtNumrangeVO11> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<MtNumrangeVO11> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
