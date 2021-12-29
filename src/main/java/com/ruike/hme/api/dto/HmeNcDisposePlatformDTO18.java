package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO3;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 20:32:56
 **/
@Data
public class HmeNcDisposePlatformDTO18 implements Serializable {
    private static final long serialVersionUID = -4377281744033353453L;

    @ApiModelProperty(value = "序列号")
    private String snNumber;

    @ApiModelProperty(value = "不良类型Id")
    private String ncCodeId;

    @ApiModelProperty(value = "子不良代码Id集合")
    private List<String> childNcCodeIdList;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;

    @ApiModelProperty(value = "备注")
    private String comments;

    @ApiModelProperty(value = "附件id")
    private String uuid;

    @ApiModelProperty(value = "直接报废/通知工艺判定标识（1代表直接报废，2代表通知工艺判定标识）")
    private String flag;

    List<HmeNcDisposePlatformVO4> materilalList;

    @ApiModelProperty(value = "冻结标识，Y代表冻结")
    private String freeze;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "工序Id")
    private String processId;
}
