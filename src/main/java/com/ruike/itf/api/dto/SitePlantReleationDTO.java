package com.ruike.itf.api.dto;

import lombok.Data;

import java.util.List;

/**
 * WCS与WMS成品SN核对接口 管理 API
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
@Data
public class SitePlantReleationDTO extends ItfCommonReturnDTO{
    private List<String> materiaLotCodes;
    private List<ItfCommonReturnDTO> detail;
}
