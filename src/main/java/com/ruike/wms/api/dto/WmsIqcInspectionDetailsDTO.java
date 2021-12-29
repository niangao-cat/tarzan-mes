package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 10:21
 */
@Data
public class WmsIqcInspectionDetailsDTO implements Serializable {

    private static final long serialVersionUID = 2062333821454734847L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("检验单号")
    private String iqcNumber;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("检验状态")
    private String inspectionStatus;
    @ApiModelProperty("检验结果")
    private String inspectionResult;
    @ApiModelProperty("供应商Id")
    private String supplierId;
    @ApiModelProperty("检验类型")
    private String inspectionType;
    @ApiModelProperty("审批结果")
    private String finalDecision;
    @ApiModelProperty("创建时间从")
    private String creationDateFrom;
    @ApiModelProperty("创建时间至")
    private String creationDateTo;
    @ApiModelProperty("检验员")
    private String qcBy;
    @ApiModelProperty("检验完成时间从")
    private String inspectionFinishDateFrom;
    @ApiModelProperty("检验完成时间至")
    private String inspectionFinishDateTo;




    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;
    @ApiModelProperty(value = "供应商列表", hidden = true)
    @JsonIgnore
    private List<String> supplierIdList;
    @ApiModelProperty(value = "检验员列表", hidden = true)
    @JsonIgnore
    private List<String> qcByList;

    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialId) ? null : Arrays.asList(StringUtils.split(this.materialId, ","));
        this.supplierIdList = StringUtils.isBlank(this.supplierId) ? null : Arrays.asList(StringUtils.split(this.supplierId, ","));
        this.qcByList = StringUtils.isBlank(this.qcBy) ? null : Arrays.asList(StringUtils.split(this.qcBy, ","));
    }
}
