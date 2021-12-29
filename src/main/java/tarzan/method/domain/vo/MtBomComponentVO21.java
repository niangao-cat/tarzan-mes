package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/3 9:38 上午
 */
public class MtBomComponentVO21 implements Serializable {
    private static final long serialVersionUID = -5354978917960316520L;
    @ApiModelProperty("装配清单ID")
    private String bomId;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("产品需求数量")
    private Double qty;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public MtBomComponentVO21(String bomId, String siteId, Double qty) {
        this.bomId = bomId;
        this.siteId = siteId;
        this.qty = qty;
    }

    public MtBomComponentVO21() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtBomComponentVO21 that = (MtBomComponentVO21) o;
        return Objects.equals(getBomId(), that.getBomId()) && Objects.equals(getSiteId(), that.getSiteId())
                        && Objects.equals(getQty(), that.getQty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBomId(), getSiteId(), getQty());
    }
}
