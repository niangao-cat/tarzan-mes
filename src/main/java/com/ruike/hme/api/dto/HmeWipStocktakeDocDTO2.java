package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 10:43:31
 **/
@Data
public class HmeWipStocktakeDocDTO2 implements Serializable {
    private static final long serialVersionUID = -699781106293107246L;

    private String areaId;

    private String areaCode;

    private String areaName;
}
