package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * HmeTagVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/21 14:44
 */
@Data
public class HmeTagVO implements Serializable {

    private static final long serialVersionUID = -1853227582127941621L;
    private String tagId;
    private String tagCode;
    private String tagDescription;
    private String tagGroupId;
    private String tagGroupCode;
    private String valueType;
    private String groupPurpose;
    private String materialId;
    private BigDecimal maximalValue;
    private BigDecimal minimumValue;
}
