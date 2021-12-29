package com.ruike.wms.domain.vo;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 外协发货单据扫描VO
 * @author: han.zhang
 * @create: 2020/06/19 16:42
 */
@Getter
@Setter
@ToString
public class WmsOutSourceScanVO implements Serializable {

    private static final long serialVersionUID = -2394277823537744987L;

    private WmsOutSourceOrderQueryVO headVO;

    private List<WmsOutSourceLineVO> lineList;
}