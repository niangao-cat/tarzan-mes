package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmePumpPreSelectionVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 17:10:21
 **/
@Data
public class HmePumpPreSelectionVO6 implements Serializable {
    private static final long serialVersionUID = -418799103715577364L;

    @ApiModelProperty(value = "条码ID集合")
    private List<String> materialLotIdList;

    @ApiModelProperty(value = "容器卸载集合")
    private List<HmePumpPreSelectionVO4> containerUnloadList;
}
