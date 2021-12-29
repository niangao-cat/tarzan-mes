package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-05 17:03
 */
public class MtSapPoIfaceVO2 implements Serializable {
    private static final long serialVersionUID = 3918038959311837220L;
    @ApiModelProperty("采购订单号")
    private String erpPoNum;
    @ApiModelProperty("采购订单行号")
    private String erpPoLineNum;

    public String getErpPoNum() {
        return erpPoNum;
    }

    public void setErpPoNum(String erpPoNum) {
        this.erpPoNum = erpPoNum;
    }

    public String getErpPoLineNum() {
        return erpPoLineNum;
    }

    public void setErpPoLineNum(String erpPoLineNum) {
        this.erpPoLineNum = erpPoLineNum;
    }

    public MtSapPoIfaceVO2(String erpPoNum, String erpPoLineNum) {
        this.erpPoNum = erpPoNum;
        this.erpPoLineNum = erpPoLineNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MtSapPoIfaceVO2 that = (MtSapPoIfaceVO2) o;

        if (getErpPoNum() != null ? !getErpPoNum().equals(that.getErpPoNum()) : that.getErpPoNum() != null) {
            return false;
        }
        return getErpPoLineNum() != null ? getErpPoLineNum().equals(that.getErpPoLineNum())
                        : that.getErpPoLineNum() == null;
    }

    @Override
    public int hashCode() {
        int result = getErpPoNum() != null ? getErpPoNum().hashCode() : 0;
        result = 31 * result + (getErpPoLineNum() != null ? getErpPoLineNum().hashCode() : 0);
        return result;
    }
}
