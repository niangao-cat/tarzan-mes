package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterStep;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobSnSingleBasicVO
 * @Description 单件作业平台基础数据视图
 * @Date 2020/11/3 14:53
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnSingleBasicVO implements Serializable {
    private static final long serialVersionUID = 5368889071038155553L;

    private Long userId;
    private Date currentDate;

    @ApiModelProperty("当前步骤")
    private MtRouterStep currentStep;

    @ApiModelProperty("最近一步步骤")
    private HmeRouterStepVO nearStep;

    @ApiModelProperty("最近一步正常加工步骤")
    private HmeRouterStepVO2 normalStep;

    @ApiModelProperty("eo")
    private HmeEoVO4 eo;

    @ApiModelProperty("wo")
    private HmeWorkOrderVO2 wo;

    @ApiModelProperty("当前作业列表")
    private HmeEoJobSn hmeEoJobSnEntity;

    @ApiModelProperty("条码信息")
    private HmeMaterialLotVO3 materialLot;

    @ApiModelProperty("是否容器出站 true:是 false:否")
    private Boolean containerOutSiteFlag;

    @ApiModelProperty("容器信息")
    private HmeEoJobContainer eoJobContainer;

    @ApiModelProperty("反冲料")
    private List<MtBomComponent> backFlushBomComponentList;

    @ApiModelProperty("完成步骤标识 Y:完成步骤 其他:非完成步骤")
    private String doneStepFlag;

    @ApiModelProperty("批次")
    private String lotCode;

    @ApiModelProperty("售后订单内部单号/SAP号")
    HmeServiceSplitRecordVO2 rk06InternalOrderNum;

    @ApiModelProperty("EO 工艺路线 装配清单关系")
    private HmeEoRouterBomRelVO eoRouterBomRel;

    @ApiModelProperty("返修作业平台标识 Y:是")
    private String reworkProcessFlag;

    @ApiModelProperty("不良的数据采集项")
    private List<HmeEoJobDataRecordVO2> ngDataRecordList;

    @ApiModelProperty("不良记录数据项")
    private Map<String, HmeEoJobDataRecordVO2> ncJobDataRecordMap;

    @ApiModelProperty("新条码标识")
    private Boolean newMaterialLotFlag;
}