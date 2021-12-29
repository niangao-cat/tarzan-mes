package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * HmeEoJobSnVO6
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO6 implements Serializable {

    private static final long serialVersionUID = 1127444006562789476L;
    @ApiModelProperty("进站类型")
    private String jobType;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("工位Id")
    private String workcellId;

    @ApiModelProperty("工序作业列表")
    private List<HmeEoJobSnVO3> snLineList;

    @ApiModelProperty("出站动作(返修、完工)")
    private String outSiteAction;
    @ApiModelProperty("是否继续")
    private String continueFlag;

}
