package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeLoadJob;
import com.ruike.hme.domain.entity.HmeLoadJobObject;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/5 15:57
 */
@Data
public class HmeCosPatchPdaVO8 implements Serializable {

    private static final long serialVersionUID = -1429921282553855397L;
    @ApiModelProperty("装载信息")
    private List<HmeMaterialLotLoad> hmeMaterialLotLoadList;
    @ApiModelProperty("装载信息作业记录表")
    private List<HmeLoadJob> hmeLoadJobList;
    @ApiModelProperty("装载信息作业对象表")
    private List<HmeLoadJobObject> hmeLoadJobObjectList;

}
