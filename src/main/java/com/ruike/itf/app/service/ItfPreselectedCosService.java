package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.*;

import java.util.List;

/***
 * @description COS 接口挑选
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:08
 * @version 0.0.1
 * @return
 */
public interface ItfPreselectedCosService {

    /***
     * @description 数据查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 13:53
     * @version 0.0.1
     * @return com.ruike.itf.api.dto.ItfPreselectedCosOneReturnDTO
     */
    List<ItfPreselectedCosOneReturnDTO> invoke(Long tenantId, List<String> dto);


    /***
     * @description 数据查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 13:53
     * @version 0.0.1
     * @return com.ruike.itf.api.dto.ItfPreselectedCosOneReturnDTO
     */
    List<ItfPreselectedCosTwoReturnDTO> invokeSecond(Long tenantId, List<String> dto);

    /***
     * @description 数据查询
     * @param tenantId
     * @param dto
     * @author 田欣
     * @email xin.t@raycuslaser.com
     * @date 2021/9/26
     * @time 13:53
     * @version 0.0.1
     * @return com.ruike.itf.api.dto.ItfPreselectedCosPick1ReturnDTO
     */
    ItfPreselectedCosSelectReturnDTO invokeSelect(Long tenantId, List<String> dto);

    /***
     * @description 数据查询
     * @param tenantId
     * @param dto
     * @author 田欣
     * @email xin.t@raycuslaser.com
     * @date 2021/9/26
     * @time 13:53
     * @version 0.0.1
     * @return List<com.ruike.itf.api.dto.ItfPreselectedCosArrangeReturnDTO>
     */
    List<ItfPreselectedCosArrangeReturnDTO> invokeArrange(Long tenantId, List<String> dto);
}
