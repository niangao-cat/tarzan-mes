package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeShiftVO3
 *
 * @author chaonan.hu@hand-china.com 2020/07/28 17:54:09
 */
@Data
public class HmeShiftVO3 implements Serializable {
    private static final long serialVersionUID = 3077469251365332416L;

    private String employeeId;

    private String name;

    private String userId;
}
