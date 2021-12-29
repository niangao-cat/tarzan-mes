package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 返回给前端封装好的总数据
 *
 * @author wengang.qiang@hand-china 2021/09/02 18:12
 */
@Data
public class HmePumpingSourceAllVO implements Serializable {

    private static final long serialVersionUID = 2489026308306604945L;

    List<HmePumpingSourceVO> hmePumpingSourceVOList;
    List<HumepingVO> humePingVOList;
    BigDecimal powerSum;

}
