package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author lijinghua
 * @Classname ZContainerDetailResultDTO
 * @Description 物流器具条码查询返回的实体
 * @Date 2019/9/25 13:55
 * @Created by lijinghua
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerDetailResultDTO implements Serializable {

    private static final long serialVersionUID = 7127832365905986239L;

    @ApiModelProperty(value = "对象条码")
    private String objectCode;

    @ApiModelProperty(value = "对象类型")
    @LovValue(value = "Z_OBJECT_TYPE", meaningField = "objectTypeMeaning")
    private String objectType;

    @ApiModelProperty(value = "对象类型meaning")
    private String objectTypeMeaning;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料数量")
    private String quantity;

    @ApiModelProperty(value = "单位")
    private String unitofmaterial;

    @ApiModelProperty(value = "质量状态")
    private String qualitystatus;


}
