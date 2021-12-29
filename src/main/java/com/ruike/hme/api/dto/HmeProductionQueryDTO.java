package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeProcessInfoVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description: HmeProductionQueryDTO，在制查询报表
 * @author bao.xu@hand-china.com 2020/7/13 14:14
 */
@Data
public class HmeProductionQueryDTO implements Serializable {

    private static final long serialVersionUID = 8591599587143119197L;

    @ApiModelProperty(value = "EO编号")
    private String eoId;
    @ApiModelProperty(value = "站点编号")
    private String siteId;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "生产线编号")
    private String prodLineId;
    @ApiModelProperty(value = "生产线名称")
    private String prodLineName;
    @ApiModelProperty(value = "产品类型")
    private String itemType;
    @ApiModelProperty(value = "待上线")
    private Long queueNum;
    @ApiModelProperty(value = "未入库库存")
    private Long unCount;
    @ApiModelProperty(value = "产品分类")
    private String productGroup;
    @ApiModelProperty(value = "产品型号")
    private String materialType;
    @ApiModelProperty(value = "物料编号")
    private String materialId;
    @ApiModelProperty(value = "产品编号")
    private String materialCode;
    @ApiModelProperty(value = "产品描述")
    private String materialName;
    @ApiModelProperty(value = "物料工厂关系表编号")
    private String materialSiteId;
    @ApiModelProperty(value = "完成和运行数量汇总")
    private List<HmeProcessInfoVO> workcells;
}
