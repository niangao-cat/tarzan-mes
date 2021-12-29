package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipSiteInConfirmDTO
 * @Description COS取片平台-进站确认输入参数
 * @Date 2020/8/18 19:46
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipSiteInConfirmDTO implements Serializable {
    private static final long serialVersionUID = 3709866540965809670L;

    @ApiModelProperty("勾选条码集合")
    private List<HmeCosGetChipMaterialLotConfirmDTO> materialLotList;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("站点ID")
    private String siteId;


    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;
}