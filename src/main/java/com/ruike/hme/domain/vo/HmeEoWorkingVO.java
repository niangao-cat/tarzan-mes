package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * HmeEoJobWipVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/21 14:44
 */
@Data
public class HmeEoWorkingVO implements Serializable {

    private Long sequenceNum;
    private String workcellId;
    private String workcellName;
    private Long sequence;
    private Long sequenceTwo;

    List<HmeEoJobWipVO3> hmeEoJobWipVO3List;
}
