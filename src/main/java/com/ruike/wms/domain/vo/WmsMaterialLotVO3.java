package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * WmsMaterialLotVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 15:09
 */
@Data
public class WmsMaterialLotVO3 implements Serializable {

    private static final long serialVersionUID = 1312336067275441145L;

    private String freezeFlag;
    private String freezeReason;
    List<WmsMaterialLotVO2> wmsMaterialLotVo2List;

}
