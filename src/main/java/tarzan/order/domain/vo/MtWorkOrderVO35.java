package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtNumrangeVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO11;

/**
 * @author peng.yuan
 * @ClassName MtWorkOrderVO35
 * @description
 * @date 2019年11月22日 11:13
 */
public class MtWorkOrderVO35 implements Serializable {

    private static final long serialVersionUID = 4072921888269427941L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "外部输入编码")
    private List<String> outsideNumList;
    @ApiModelProperty(value = "调用标准对象传入参数列表")
    private List<MtNumrangeVO10> woPropertyList;
    @ApiModelProperty(value = "传入值参数列表")
    private List<MtNumrangeVO11> incomingValueList;
    @ApiModelProperty(value = "特定对象编码生成标识")
    private String objectNumFlag;
    @ApiModelProperty(value = "生成编号的数量")
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

    public List<MtNumrangeVO11> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<MtNumrangeVO11> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }

    public List<MtNumrangeVO10> getWoPropertyList() {
        return woPropertyList;
    }

    public void setWoPropertyList(List<MtNumrangeVO10> woPropertyList) {
        this.woPropertyList = woPropertyList;
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
