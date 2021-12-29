package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.modeling.domain.entity.MtModLocator;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobSnVO18
 * @Description outSiteForDoneStep
 * @Date 2020/11/6 20:29
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnVO18 extends HmeEoJobSnVO16 {
    private static final long serialVersionUID = 2083754110099084133L;

    @ApiModelProperty("默认存储货位")
    private MtModLocator defaultStorageLocator ;

    @ApiModelProperty("非完工步骤作业")
    private List<HmeEoJobSnVO3> normalSnLineList;

    @ApiModelProperty("完工步骤作业")
    private List<HmeEoJobSnVO3> doneSnLineList;

    @ApiModelProperty("完工步骤ID集合")
    private List<String> doneEoStepIdList;

    @ApiModelProperty("出站事件请求ID")
    private String invUpdateEventRequestId;

    @ApiModelProperty("key:jobId, value:作业信息")
    private Map<String, HmeEoJobSn> eoJobSnEntityMap;

    @ApiModelProperty("不良的数据采集项")
    private List<HmeEoJobDataRecordVO2> ngDataRecordList;

    @ApiModelProperty("key:MaterialLotId, value:MaterialLot")
    private Map<String, HmeMaterialLotVO3> materialLotMap;

    @ApiModelProperty("时效返修作业平台标识")
    private boolean isTimeRework;
}