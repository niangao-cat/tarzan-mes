package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtWorkOrderVO33
 * @description
 * @date 2019年11月21日 10:03
 */
public class MtWorkOrderVO33 implements Serializable {
    private static final long serialVersionUID = -8236794171255674515L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "外部输入编码")
    private String outsideNum;
    @ApiModelProperty(value = "调用标准对象传入参数列表")
    private List<MtWorkOrderVO34> woPropertyList;
    @ApiModelProperty(value = "传入值参数列表")
    private List<String> incomingValueList;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<MtWorkOrderVO34> getWoPropertyList() {
        return woPropertyList;
    }

    public void setWoPropertyList(List<MtWorkOrderVO34> woPropertyList) {
        this.woPropertyList = woPropertyList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
