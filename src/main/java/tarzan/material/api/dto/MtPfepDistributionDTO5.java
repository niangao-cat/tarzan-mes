package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/13 16:10
 */
public class MtPfepDistributionDTO5 implements Serializable {
    private static final long serialVersionUID = -2274829716662504643L;

    @ApiModelProperty(value = "主键ID")
    private String pfepDistributionId;

    @ApiModelProperty(value = "pfep类型(material/category)")
    private String pfepDistributionType;

    public String getPfepDistributionId() {
        return pfepDistributionId;
    }

    public void setPfepDistributionId(String pfepDistributionId) {
        this.pfepDistributionId = pfepDistributionId;
    }

    public String getPfepDistributionType() {
        return pfepDistributionType;
    }

    public void setPfepDistributionType(String pfepDistributionType) {
        this.pfepDistributionType = pfepDistributionType;
    }
}
