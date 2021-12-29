package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 不良审核
 *
 * @author chaonan.hu@hand-china.com 2020-07-20 14:54:48
 */
@Data
public class HmeNcCheckVO implements Serializable {
    private static final long serialVersionUID = 5744497533046389752L;

    private String ncGroupId;

    private String ncGroupCode;

    private String description;
}
