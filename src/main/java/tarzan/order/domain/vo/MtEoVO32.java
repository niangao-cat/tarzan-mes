package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/21 10:25
 * @Author: ${yiyang.xie}
 */
public class MtEoVO32 implements Serializable {
    private static final long serialVersionUID = -5012235488288655473L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("调用标准对象传入参数列表")
    private MtEoVO33 eoPropertyList;
    @ApiModelProperty("传入值参数列表")
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

    public MtEoVO33 getEoPropertyList() {
        return eoPropertyList;
    }

    public void setEoPropertyList(MtEoVO33 eoPropertyList) {
        this.eoPropertyList = eoPropertyList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
