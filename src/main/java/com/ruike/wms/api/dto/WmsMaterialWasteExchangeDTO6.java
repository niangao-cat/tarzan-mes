package com.ruike.wms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/13 16:11
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO6 implements Serializable {
    private static final long serialVersionUID = 44401483441140884L;

    List<WmsMaterialWasteExchangeDTO3> lineList;
}
