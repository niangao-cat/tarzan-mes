package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeNcCheckVO2
 *
 * @author chaonan.hu@hand-china.com 2020-08-07 15:38:34
 */
@Data
public class HmeNcCheckVO2 implements Serializable {
    private static final long serialVersionUID = -5077043124600518433L;

    private String ncRecordId;

    private String ncCodeId;
}
