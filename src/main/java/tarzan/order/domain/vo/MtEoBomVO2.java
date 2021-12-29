package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtEoBomVO2 implements Serializable {
    private static final long serialVersionUID = -70286268201487491L;

    @ApiModelProperty("EO装配清单Id")
    private String eoBomId;
    @ApiModelProperty("EO装配清单历史Id")
    private String eoBomHisId;

    public String getEoBomId() {
        return eoBomId;
    }

    public void setEoBomId(String eoBomId) {
        this.eoBomId = eoBomId;
    }

    public String getEoBomHisId() {
        return eoBomHisId;
    }

    public void setEoBomHisId(String eoBomHisId) {
        this.eoBomHisId = eoBomHisId;
    }
}
