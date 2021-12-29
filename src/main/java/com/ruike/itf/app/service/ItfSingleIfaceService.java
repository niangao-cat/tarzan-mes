package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO2;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO3;
import com.ruike.itf.domain.vo.ItfSingleIfaceVO;

/**
 * 单件设备接口应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
public interface ItfSingleIfaceService {

    /**
     * 单件进站
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/11/3 7:57
     * @return com.ruike.itf.domain.vo.ItfSingleIfaceVO
     */
    ItfSingleIfaceVO singleInSite(Long tenantId, ItfSingleIfaceDTO dto);

    /**
     * 单件投料
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/10/8 15:40
     * @return com.ruike.itf.domain.vo.ItfSingleIfaceVO
     */
    ItfSingleIfaceVO singleRelease(Long tenantId, ItfSingleIfaceDTO2 dto);

    /**
     * 单件出站
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/10/9 16:05
     * @return com.ruike.itf.domain.vo.ItfSingleIfaceVO
     */
    ItfSingleIfaceVO singleSiteOut(Long tenantId, ItfSingleIfaceDTO3 dto);

    /**
     * 是否容器进站
     *
     * @param tenantId
     * @param workcellId
     * @author sanfeng.zhang@hand-china.com 2021/11/3 8:56
     * @return boolean
     */
    boolean isContainerControl(Long tenantId, String workcellId);
}
