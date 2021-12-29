package com.ruike.wms.domain.vo;

import com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 成本中心领退料头行一起查询
 * @author: han.zhang
 * @create: 2020/05/28 17:28
 */
@Getter
@Setter
@ToString
public class WmsPickReturnHeadAndLine implements Serializable {
    private static final long serialVersionUID = 6531031365957359514L;
    /**
     * 头信息
     */
    private WmsPickReturnReceiveVO headVO;

    /**
     * 行信息
     */
    private List<WmsPickReturnLineReceiveVO> lineVOs;
}