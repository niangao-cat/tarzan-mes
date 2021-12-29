package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeTagDaqAttrVO
 *
 * @author chaonan.hu@hand-china.com 2020/07/21 10:38:23
 */
@Data
public class HmeTagDaqAttrVO implements Serializable {
    private static final long serialVersionUID = -6058992163128281346L;

    private String value;

    private String meaning;
}
