package com.ruike.itf.domain.vo;

import com.ruike.itf.domain.entity.ItfDeliveryDocIface;
import com.ruike.itf.domain.entity.ItfDeliveryDocLineIface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 送货单接口数据
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@Data
@ApiModel("送货单接口数据")
public class ItfDeliveryDocAndLineIfaceVO implements Serializable {
    private static final long serialVersionUID = 4498118946962598437L;

    @ApiModelProperty("送货单头表数据")
    private ItfDeliveryDocIface itfDeliveryDocIface;

    @ApiModelProperty("送货单行表数据")
    private List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList;



}
