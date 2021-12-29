package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/8/18 13:58
 */
@Data
public class HmeChipTransferVO6 implements Serializable {

    private static final long serialVersionUID = 4988949821018615712L;

    private HmeChipTransferVO transferVo;

    private List<HmeChipTransferVO> targetList;
}
