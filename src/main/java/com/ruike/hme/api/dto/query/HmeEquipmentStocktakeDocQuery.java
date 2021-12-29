package com.ruike.hme.api.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备盘点单 查询对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 09:47
 */
@Data
public class HmeEquipmentStocktakeDocQuery implements Serializable {
    private static final long serialVersionUID = -6932617774283656939L;

    @ApiModelProperty(value = "盘点单ID", hidden = true)
    @JsonIgnore
    private String stocktakeId;
    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("盘点状态")
    private String stocktakeStatus;
    @ApiModelProperty(value = "盘点类型")
    private String stocktakeType;
    @ApiModelProperty(value = "盘点范围")
    private Integer stocktakeRange;
    @ApiModelProperty(value = "保管部门ID")
    private String businessId;
    @ApiModelProperty(value = "入账日期从")
    private Date postingDateFrom;
    @ApiModelProperty(value = "入账日期至")
    private Date postingDateTo;
    @ApiModelProperty(value = "创建日期从")
    private Date creationDateFrom;
    @ApiModelProperty(value = "创建日期至")
    private Date creationDateTo;
    @ApiModelProperty(value = "台账类型")
    private String ledgerType;
    @ApiModelProperty("租户")
    private Long tenantId;

    public HmeEquipmentStocktakeDocQuery() {
    }

    public HmeEquipmentStocktakeDocQuery(String stocktakeId, Long tenantId) {
        this.stocktakeId = stocktakeId;
        this.tenantId = tenantId;
    }

    public HmeEquipmentStocktakeDocQuery(Long tenantId, String stocktakeNum) {
        this.stocktakeNum = stocktakeNum;
        this.tenantId = tenantId;
    }

    public void paramInit(Long tenantId) {
        this.setTenantId(tenantId);
    }
}
