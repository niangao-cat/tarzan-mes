package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 10:50:31
 **/
@Data
public class HmeWipStocktakeDocDTO3 implements Serializable {
    private static final long serialVersionUID = -2084460408067735876L;

    private String prodLineId;

    private String prodLineCode;

    private String prodLineName;

    private String departmentId;
}
