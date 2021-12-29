package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeOpenEndShiftVO
 * @author chaonan.hu@hand-china.com 2020-07-07 09:46:16
 **/
@Data
public class HmeOpenEndShiftVO implements Serializable {
    private static final long serialVersionUID = 7724905083270239636L;

    private String lineWorkcellId;

    private String lineWorkcellName;
}
