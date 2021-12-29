package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.entity.MtHoldActualDetail;

public class MtHoldActualVO implements Serializable {

    private static final long serialVersionUID = -8048029760603908598L;

    private MtHoldActual mtHoldActual;// 头

    private List<MtHoldActualDetail> mtHoldActualDetails;// 行

    public MtHoldActual getMtHoldActual() {
        return mtHoldActual;
    }

    public void setMtHoldActual(MtHoldActual mtHoldActual) {
        this.mtHoldActual = mtHoldActual;
    }

    public List<MtHoldActualDetail> getMtHoldActualDetails() {
        return mtHoldActualDetails;
    }

    public void setMtHoldActualDetails(List<MtHoldActualDetail> mtHoldActualDetails) {
        this.mtHoldActualDetails = mtHoldActualDetails;
    }
}
