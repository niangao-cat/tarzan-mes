package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description IQC检验平台  主界面查询传入条件
 * @Author tong.li
 * @Date 2020/5/12 10:32
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformDTO implements Serializable {
    private static final long serialVersionUID = 5676179823576781740L;


    @ApiModelProperty(value = "来源单号")
    private String instructionDocNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "处理状态")
    private String inspectionStatus;

    @ApiModelProperty(value = "是否加急")
    private String identification;

    @ApiModelProperty(value = "供应商")
    private String supplierId;

    @ApiModelProperty(value = "检验单号")
    private String iqcNumber ;

    @ApiModelProperty(value = "到货时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateFrom;

    @ApiModelProperty(value = "到货时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateTo;

    @ApiModelProperty(value = "是否根据用户与物料组关系限制查看单据标识，为REL时限制")
    private String relFlag;

    @ApiModelProperty(value = "库位编码")
    private String locatorCode;

    @ApiModelProperty(value = "检验类型")
    private String inspectionType;
}
