package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosEoJobSnSiteOutVO
 * @Description EoJobSn出站
 * @Date 2020/8/20 21:33
 * @Author yuchao.wang
 */
@Data
public class HmeCosEoJobSnSiteOutVO implements Serializable {
    private static final long serialVersionUID = 2510240969358052677L;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;
}