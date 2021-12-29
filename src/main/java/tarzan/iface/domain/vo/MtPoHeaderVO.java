package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-04 21:05
 */
public class MtPoHeaderVO implements Serializable {
    private static final long serialVersionUID = 3187155826045536848L;
    @ApiModelProperty("采购订单头ID")
    private String poHeaderId;
    @ApiModelProperty("一揽子发放ID")
    private String poReleaseId;

    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    public String getPoReleaseId() {
        return poReleaseId;
    }

    public void setPoReleaseId(String poReleaseId) {
        this.poReleaseId = poReleaseId;
    }

    public MtPoHeaderVO(String poHeaderId, String poReleaseId) {
        this.poHeaderId = poHeaderId;
        this.poReleaseId = poReleaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MtPoHeaderVO that = (MtPoHeaderVO) o;

        if (getPoHeaderId() != null ? !getPoHeaderId().equals(that.getPoHeaderId()) : that.getPoHeaderId() != null) {
            return false;
        }
        return getPoReleaseId() != null ? getPoReleaseId().equals(that.getPoReleaseId())
                        : that.getPoReleaseId() == null;
    }

    @Override
    public int hashCode() {
        int result = getPoHeaderId() != null ? getPoHeaderId().hashCode() : 0;
        result = 31 * result + (getPoReleaseId() != null ? getPoReleaseId().hashCode() : 0);
        return result;
    }
}
