package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeWoDispatchVO6 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    List<HmeWoDispatchVO4> prodLineWkcList;
}
