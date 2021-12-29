package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import org.hzero.boot.platform.lov.annotation.LovValue;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsMaterialPostingVO3
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 11:57
 */
@Data
public class WmsMaterialPostingVO3 implements Serializable {

    private static final long serialVersionUID = 1583699591612541042L;
    @ApiModelProperty("采购订单头ID")
    private String instructionDocId;
    @ApiModelProperty("采购订单行ID")
    private String instructionId;
    @ApiModelProperty("工厂ID")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("BOM用量")
    private String bomUsage;
    @ApiModelProperty("单位id")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
}
