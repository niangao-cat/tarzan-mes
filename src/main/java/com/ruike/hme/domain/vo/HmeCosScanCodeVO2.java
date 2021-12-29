package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/22 14:51
 */
@Data
public class HmeCosScanCodeVO2 implements Serializable {

    private static final long serialVersionUID = 7673649230320559748L;

    @ApiModelProperty(value = "进站条码列表")
    private List<HmeCosResVO> codeList;
}
