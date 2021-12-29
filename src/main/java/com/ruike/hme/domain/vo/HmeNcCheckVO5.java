package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HmeNcCheckVO5 implements Serializable {
    private static final long serialVersionUID = 3612221925623279412L;

    private String ncRecordId;

    private String ncCodeId;

    private String ncCode;

    private String description;
}
