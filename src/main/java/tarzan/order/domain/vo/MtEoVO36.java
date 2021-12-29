package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtNumrangeVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO11;

/**
 * @Description:
 * @Date: 2019/11/26 15:02
 * @Author: ${yiyang.xie}
 */
public class MtEoVO36 implements Serializable {
    private static final long serialVersionUID = -2567197220060113928L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("外部输入编码列表")
    private List<String> outsideNumList;

    @ApiModelProperty("调用标准对象传入参数列表")
    private List<MtNumrangeVO10> eoPropertyList;

    @ApiModelProperty("传入值参数列表")
    private List<MtNumrangeVO11> incomingValueList;

    @ApiModelProperty("特定对象编码生成标识")
    private String objectNumFlag;

    @ApiModelProperty("生成编号的数量")
    private Long numQty;

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

    public List<MtNumrangeVO10> getEoPropertyList() {
        return eoPropertyList;
    }

    public void setEoPropertyList(List<MtNumrangeVO10> eoPropertyList) {
        this.eoPropertyList = eoPropertyList;
    }

    public List<MtNumrangeVO11> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<MtNumrangeVO11> incomingValueList) {
        this.incomingValueList = incomingValueList;
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
}
