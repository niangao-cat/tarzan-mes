package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-05 16:45
 */
public class MtSapPoIfaceVO1 implements Serializable {
    private static final long serialVersionUID = -2160206376109373722L;
    @ApiModelProperty("采购订单号")
    private String poNumber;
    @ApiModelProperty("协议订单")
    private String poReleaseNum;

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoReleaseNum() {
        return poReleaseNum;
    }

    public void setPoReleaseNum(String poReleaseNum) {
        this.poReleaseNum = poReleaseNum;
    }

    public MtSapPoIfaceVO1(String poNumber, String poReleaseNum) {
        this.poNumber = poNumber;
        this.poReleaseNum = poReleaseNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MtSapPoIfaceVO1 that = (MtSapPoIfaceVO1) o;

        if (getPoNumber() != null ? !getPoNumber().equals(that.getPoNumber()) : that.getPoNumber() != null) {
            return false;
        }
        return getPoReleaseNum() != null ? getPoReleaseNum().equals(that.getPoReleaseNum())
                        : that.getPoReleaseNum() == null;
    }

    @Override
    public int hashCode() {
        int result = getPoNumber() != null ? getPoNumber().hashCode() : 0;
        result = 31 * result + (getPoReleaseNum() != null ? getPoReleaseNum().hashCode() : 0);
        return result;
    }
}
