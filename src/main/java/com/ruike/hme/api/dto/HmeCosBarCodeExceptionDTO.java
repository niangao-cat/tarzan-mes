package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * COS条码加工异常汇总输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:40
 */
@Data
public class HmeCosBarCodeExceptionDTO implements Serializable {

    private static final long serialVersionUID = -620134520984423351L;


    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("盒子号")
    private String materialLotCode;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("热沉批次")
    private String heatSinkLot;

    @ApiModelProperty("金线批次")
    private String goldWireLot;

    @ApiModelProperty("操作者")
    private String realName;

    @ApiModelProperty("设备台机")
    private String assetEncoding;

    @ApiModelProperty("产品")
    private String materialId;

    @ApiModelProperty("操作时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String beginTime;

    @ApiModelProperty("操作时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    public void validObject() {
        if (StringUtils.isBlank(workOrderNum) && StringUtils.isBlank(materialLotCode) & (Objects.isNull(beginTime) && Objects.isNull(endTime))) {
            throw new CommonException("工单号，盒子号与操作时间范围必输其一");
        }
    }
}
