package com.ruike.itf.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 成品出入库容器信息返回
 *
 * @author taowen.wang@hand-china.com 2021/7/1 15:23
 */
@Data
public class ItfConcodeReturnIfaceDTO extends ItfCommonReturnDTO{
    private String containerCode;
    private List<ItfCommonReturnDTO> detail;
}
