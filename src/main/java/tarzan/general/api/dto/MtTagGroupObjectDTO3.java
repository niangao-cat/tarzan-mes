package tarzan.general.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MtTagGroupObjectDTO3
 * @author: chaonan.hu@hand-china.com 2020-08-10 15:47:23
 **/
@Data
public class MtTagGroupObjectDTO3 implements Serializable {
    private static final long serialVersionUID = 3583581703755169209L;

    @ApiModelProperty("版本名称")
    private String productionVersion;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("物料版本")
    private String materialId;
}
