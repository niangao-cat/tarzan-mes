package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeShiftVO2
 *
 * @author chaonan.hu@hand-china.com 2020/07/28 16:53:13
 */
@Data
public class HmeShiftVO2 implements Serializable {
    private static final long serialVersionUID = -7311114701947862389L;

    private String positionId;

    private String supervisorFlag;
}
