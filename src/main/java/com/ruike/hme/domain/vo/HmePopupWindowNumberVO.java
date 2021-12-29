package com.ruike.hme.domain.vo;

import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author Ric 2021/09/12 18:14
 */
@Data
public class HmePopupWindowNumberVO implements Serializable {

    private static final long serialVersionUID = 302009206594445845L;
    @ApiModelProperty(value = "拦截数")
    private Long interceptNumber;
    @ApiModelProperty(value = "放行数")
    private Long releaseNumber;

    private Page<HmePopupWindowVO> hmePopupWindowVOList;
}
