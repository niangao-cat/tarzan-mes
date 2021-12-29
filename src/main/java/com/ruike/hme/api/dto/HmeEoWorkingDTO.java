package com.ruike.hme.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/04/21 14:10
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoWorkingDTO implements Serializable {
    private static final long serialVersionUID = -771095396459414541L;

    private String siteId;
    private String siteName;
    private String areaId;
    private String areaName;
    private String prodLineId;
    private String prodLineName;
    private String typeName;
}
