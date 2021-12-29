package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WmsLibraryAgeReportVO
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 15:11:23
 **/
@Data
public class WmsLibraryAgeReportVO implements Serializable {
    private static final long serialVersionUID = 116499092776602051L;

    @ApiModelProperty(value = "实物条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "生产日期")
    private String productDate;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inLocatorTime;

    @ApiModelProperty(value = "保质期")
    private BigDecimal shelfLife;

    @ApiModelProperty(value = "条码状态")
    @LovValue(value = "Z.MTLOT.STATUS.G", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "条码状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "仓库")
    private String parentLocatorCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "库龄")
    private BigDecimal libraryAge;

    @ApiModelProperty(value = "超期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beyondDate;

    @ApiModelProperty(value = "超期天数")
    private BigDecimal beyondDay;

    @ApiModelProperty(value = "接收时间")
    private String receiptDate;
}
