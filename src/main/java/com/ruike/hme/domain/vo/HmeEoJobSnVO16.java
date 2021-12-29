package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.method.domain.entity.MtBomComponent;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobSnVO16
 * @Description 进出站基础数据
 * @Date 2020/11/3 14:53
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnVO16 implements Serializable {
    private static final long serialVersionUID = 2817850661649871536L;

    private Long userId;
    private Date currentDate;

    @ApiModelProperty("key:MaterialLotId, value:作业信息")
    private Map<String, HmeEoJobSnVO5> eoJobSnMap;

    @ApiModelProperty("key:EoId, value:最近一步步骤")
    private Map<String, HmeRouterStepVO2> nearStepMap;

    @ApiModelProperty("key:EoId, value:最近一步正常加工步骤")
    private Map<String, HmeRouterStepVO2> normalStepMap;

    @ApiModelProperty("key:EoId, value:所有正常加工步骤(按照最近更新时间倒序排序)")
    private Map<String, List<HmeRouterStepVO2>> allNormalStepMap;

    @ApiModelProperty("key:EoId, value:eo")
    private Map<String, HmeEoVO4> eoMap;

    @ApiModelProperty("key:WoId, value:wo")
    private Map<String, HmeWorkOrderVO2> woMap;

    @ApiModelProperty("key:MaterialLotId, value:MaterialLot")
    private Map<String, HmeMaterialLotVO3> materialLotMap;

    @ApiModelProperty("当前作业列表")
    private List<HmeEoJobSn> hmeEoJobSnEntityList;

    @ApiModelProperty("key:MaterialLotId, value:返修标识")
    private Map<String, String> reworkFlagMap;

    @ApiModelProperty("是否容器出站 true:是 false:否")
    private Boolean containerOutSiteFlag;

    @ApiModelProperty("工艺扩展属性BOM_FLAG 出站是否卡BOM true:不卡 false:卡")
    private Boolean operationBomFlag;

    @ApiModelProperty("容器信息")
    private HmeEoJobContainer eoJobContainer;

    @ApiModelProperty("反冲料")
    private List<MtBomComponent> backFlushBomComponentList;

    @ApiModelProperty("EO 工艺路线 装配清单关系")
    HmeEoRouterBomRelVO eoRouterBomRel;

    @ApiModelProperty("不良的数据采集项")
    private List<HmeEoJobDataRecordVO2> ngDataRecordList;

    @ApiModelProperty("时效返修作业平台标识")
    private boolean isTimeRework;

    @ApiModelProperty("不良记录数据项")
    Map<String,HmeEoJobDataRecordVO2> ncJobDataRecordMap;

    @ApiModelProperty("新条码标识")
    private boolean newMaterialLotFlag;
}