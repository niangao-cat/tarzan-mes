package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单管理
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 11:09:08
 */

@Data
public class HmeWorkOrderVO59 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;
    @ApiModelProperty(value = "工单ID" , required = true)
    private String workOrderId;

    @ApiModelProperty("工单扩展属性")
    private List<MtExtendAttrDTO3> orderAttrList;

}
