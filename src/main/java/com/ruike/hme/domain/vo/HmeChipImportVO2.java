package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeChipImportVO2
 * @description:
 * @author: 胡超男
 **/
@Data
public class HmeChipImportVO2 implements Serializable {
    private static final long serialVersionUID = 6310637324194570205L;

    private String kid;

    private String materialLotId;
}
