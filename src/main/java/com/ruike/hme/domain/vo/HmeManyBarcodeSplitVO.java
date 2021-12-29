package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/8 15:47
 */
@Data
public class HmeManyBarcodeSplitVO implements Serializable {

    private static final long serialVersionUID = -1861224562996044707L;

    private String materialLotCode;
}
