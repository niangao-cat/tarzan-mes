package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/9/14 14:40
 * @Description:
 */
public class MtBomVO23 implements Serializable {
    private static final long serialVersionUID = -7436470824356147967L;

    @ApiModelProperty("站点编码")
    private String siteCode;

    @ApiModelProperty("调用标准对象传入参数列表")
    private Map<String, String> bomVersionPropertyList;

    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Map<String, String> getBomVersionPropertyList() {
        return bomVersionPropertyList;
    }

    public void setBomVersionPropertyList(Map<String, String> bomVersionPropertyList) {
        this.bomVersionPropertyList = bomVersionPropertyList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
