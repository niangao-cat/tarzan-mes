package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 10:52:12
 **/
@Data
public class HmeWipStocktakeDocDTO4 implements Serializable {
    private static final long serialVersionUID = -6600792663693641585L;

    private String workcellId;

    private String workcellCode;

    private String workcellName;
}
