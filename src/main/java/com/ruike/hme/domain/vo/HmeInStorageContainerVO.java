package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
* @Classname HmeInStorageContainerVO
* @Description 半成品/成品入库扫描 箱体栏信息VO
* @Date  2020/6/2 16:33
* @Created by Deng xu
*/
@Data
public class HmeInStorageContainerVO implements Serializable {

    private static final long serialVersionUID = -4494847842702108957L;

    @ApiModelProperty("类型 CONTAINER-容器 MATERIAL_LOT-物料批")
    private String codeType;

    @ApiModelProperty("箱体ID")
    private String containerId;

    @ApiModelProperty("箱体编码")
    private String containerCode;

    @ApiModelProperty("数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty("物料标识-为Y则显示物料编码，为N显示...")
    private String sameMaterialFlag;

    @ApiModelProperty("物料批状态-不为OK则前台需要弹出选择框")
    private String qualityStatus;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    //2020-08-13 add by sanfeng.zhang
    @ApiModelProperty("仓库id")
    private String locatorId;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("物料信息")
    private List<HmeInStorageMaterialVO> materialList;

    @ApiModelProperty("原物料信息")
    private List<HmeInStorageMaterialVO> sourceMaterialList;

    @ApiModelProperty("查询标识")
    private String selectFlag;

}
