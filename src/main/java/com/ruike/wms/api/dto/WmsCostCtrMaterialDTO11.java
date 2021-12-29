package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsCostCtrMaterialVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/02 9:21
 */
@Data
public class WmsCostCtrMaterialDTO11 implements Serializable {

    private static final long serialVersionUID = 2967076294394645484L;

    @ApiModelProperty(value = "条码集合")
    private List<WmsCostCtrMaterialDTO3> wmsCostCtrMaterialDTO3List;

    @ApiModelProperty(value = "条码集合")
    private List<WmsCostCtrMaterialVO> wmsCostCtrMaterialVOS;
}
