package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * * @description
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/28 10:06
 * @change: 2020/2/28 10:06 by jin.xu@hand-china.com for init
 */
public class MtPfepDistributionDTO7 implements Serializable {

    private static final long serialVersionUID = -34233936884805962L;
    
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
