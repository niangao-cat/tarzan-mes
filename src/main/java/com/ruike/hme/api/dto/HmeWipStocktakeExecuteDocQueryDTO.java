package com.ruike.hme.api.dto;

import com.ruike.wms.infra.util.DatetimeUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 在制品盘点 单据查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:18
 */
@Data
public class HmeWipStocktakeExecuteDocQueryDTO implements Serializable {
    private static final long serialVersionUID = 2677313560348929116L;

    @ApiModelProperty(value = "盘点单号", hidden = true)
    private String stocktakeNum;
    @ApiModelProperty(value = "盘点单Id", hidden = true)
    private String stocktakeId;
    @ApiModelProperty("盘点单号，模糊查询")
    private String stocktakeNumLike;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("部门编码")
    private String areaCode;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("工序编码")
    private String workcellCode;
    @ApiModelProperty("创建日期")
    private Date creationDate;
    @ApiModelProperty(value = "创建日期从", hidden = true)
    private Date creationDateFrom;
    @ApiModelProperty(value = "创建日期至", hidden = true)
    private Date creationDateTo;
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

    public HmeWipStocktakeExecuteDocQueryDTO() {
    }

    public HmeWipStocktakeExecuteDocQueryDTO(String stocktakeNum) {
        this.stocktakeNum = stocktakeNum;
        this.paramInit();
    }

    public void paramInit() {
        this.setUserId(DetailsHelper.getUserDetails().getUserId());
        if (Objects.nonNull(this.getCreationDate())) {
            this.setCreationDateFrom(DatetimeUtils.getBeginOfDate(this.getCreationDate()));
            this.setCreationDateTo(DatetimeUtils.getEndOfDate(this.getCreationDate()));
        }
    }
}
