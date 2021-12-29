package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeOpenEndShiftVO3
 * @author chaonan.hu@hand-china.com 2020-07-09 15:33:16
 **/
@Data
public class HmeOpenEndShiftVO4 implements Serializable {
    private static final long serialVersionUID = 2918242954543680371L;

    private String shiftDateAndCode;

    private String shiftCode;

    private String shiftDate;
}
