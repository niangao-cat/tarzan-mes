package tarzan.general.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MtTagGroupObjectDTO4
 * @author: chaonan.hu@hand-china.com 2020-08-10 15:56:28
 **/
@Data
public class MtTagGroupObjectDTO4 implements Serializable {
    private static final long serialVersionUID = 7102634133601436481L;

    @ApiModelProperty(value = "物料Id", required = true)
    private String materialId;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty("版本名称")
    private String productionVersion;

    @ApiModelProperty("版本描述")
    private String description;
}
