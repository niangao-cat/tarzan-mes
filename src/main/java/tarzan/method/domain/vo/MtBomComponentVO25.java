package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/3 10:30 上午
 */
public class MtBomComponentVO25 implements Serializable {
    private static final long serialVersionUID = 6511460393786882005L;

    private String siteId;

    @ApiModelProperty("装配清单ID")
    private String bomId;

    @ApiModelProperty("装配清单数量")
    private Double qty;

    @ApiModelProperty("组件行信息列表")
    private List<MtBomComponentVO24> bomComponentList = new ArrayList<>();

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public List<MtBomComponentVO24> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtBomComponentVO24> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
