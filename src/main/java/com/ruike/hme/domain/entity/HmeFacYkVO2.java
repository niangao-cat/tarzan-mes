package com.ruike.hme.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 11:07
 */
@Data
public class HmeFacYkVO2 extends HmeFacYk implements Serializable {

    private static final long serialVersionUID = 879364941152285037L;

    @ApiModelProperty("工位列表")
    private List<String> workcellIdList;
}
