package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 值集导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/23 10:02
 */
@Data
public class HmeLovImportVO implements Serializable {


    private static final long serialVersionUID = 1335023628145795756L;

    private String lovCode;

    private String lovName;

    private String lovType;

    private String value;

    private String meaning;

    private Long orderSeq;

    private String tag;

    private String description;

    private Date dateFrom;

    private Date dateTo;

    private String enabledFlag;

    private String lovLang;
}
