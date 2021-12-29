package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 20:32:56
 **/
@Data
public class HmeNcDisposePlatformDTO11 implements Serializable {
    private static final long serialVersionUID = -1329921026073392507L;

    @ApiModelProperty(value = "序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "不良代码组Id")
    private String ncGroupId;

    @ApiModelProperty(value = "不良代码Id集合")
    private List<String> ncCodeIdList;

    @ApiModelProperty(value = "本库位Id")
    private String currentwWorkcellId;

    @ApiModelProperty(value = "责任库位Id")
    private String workcellId;

    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "附件id")
    private String uuid;

    @ApiModelProperty(value = "直接报废/通知工艺判定标识/直接返修（1或2或3）")
    private String flag;

    @ApiModelProperty(value = "工步id")
    private String eoStepActualId;

    @ApiModelProperty(value = "工艺id")
    private String rootCauseOperationId;

    @ApiModelProperty(value = "返修记录")
    private String reworkRecordFlag;
}
