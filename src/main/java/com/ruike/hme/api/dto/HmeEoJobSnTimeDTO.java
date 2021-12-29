package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeMaterialLotVO3;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO2;
import com.ruike.hme.domain.vo.HmeRouterStepVO5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtEo;

import javax.persistence.Transient;
import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobSnTimeDTO
 * @Description HmeEoJobSnTimeDTO
 * @Date 2020/12/24 10:10
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnTimeDTO implements Serializable {
    private static final long serialVersionUID = 5933637389971616950L;

    @ApiModelProperty("错误编码")
    private String errorCode;

    @ApiModelProperty("错误消息")
    private String errorMessage;

    @ApiModelProperty("返修标识")
    private String reworkFlag;

    @ApiModelProperty("当前步骤信息")
    private HmeRouterStepVO5 currentStep;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;

    @ApiModelProperty("条码信息")
    private List<HmeMaterialLotVO3> materialLotList;

    @ApiModelProperty("当前作业列表")
    private List<HmeEoJobSn> hmeEoJobSnEntityList;

    @ApiModelProperty("返修条码对应的EO")
    private MtEo reworkEo;

    @ApiModelProperty("工序不良")
    private HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO;

    @ApiModelProperty("工序不良集合")
    private List<HmeProcessNcDetailVO2> processNcDetailList;
}