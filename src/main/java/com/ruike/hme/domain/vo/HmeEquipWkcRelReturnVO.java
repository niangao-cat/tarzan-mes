package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 设备工位基础数据查询返回实体类
 * @author: han.zhang
 * @create: 2020/06/09 11:40
 */
@Getter
@Setter
@ToString
public class HmeEquipWkcRelReturnVO extends HmeEquipmentWkcRel implements Serializable {
    private static final long serialVersionUID = -5032762753268442386L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点描述")
    private String siteName;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "资产描述")
    private String assetDesc;
    @ApiModelProperty(value = "部门编码")
    private String businessCode;
    @ApiModelProperty(value = "部门描述")
    private String businessDesc;
    @ApiModelProperty(value = "车间编码")
    private String workShopCode;
    @ApiModelProperty(value = "车间描述")
    private String workShopDesc;
    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "产线描述")
    private String proLineDesc;
    @ApiModelProperty(value = "工段编码")
    private String lineWorkcellCode;
    @ApiModelProperty(value = "工段描述")
    private String lineWorkcellName;
    @ApiModelProperty(value = "工序编码")
    private String processWorkcellCode;
    @ApiModelProperty(value = "工序描述")
    private String processWorkcellName;
    @ApiModelProperty(value = "工位编码")
    private String stationWorkcellCode;
    @ApiModelProperty(value = "工位描述")
    private String stationWorkcellName;
}