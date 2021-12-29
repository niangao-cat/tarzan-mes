package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4;
import io.tarzan.common.domain.sys.MtUserInfo;

import java.util.List;

/**
 * 时效加工作业平台应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:45:23
 **/
public interface HmeTimeProcessPdaService {

    /**
     * 获取当前登录用户信息
     *
     * @param tenantId 租户ID
     * @return io.tarzan.common.domain.sys.MtUserInfo
     */
    MtUserInfo getCurrentUser(Long tenantId);

    /**
     * 设备查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/20 11:20:39
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4>
     */
    List<HmeTimeProcessPdaVO4> equipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto);

    /**
     * 扫描设备编码
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/19 14:12:20
     * @return com.ruike.hme.domain.vo.HmeTimeProcessPdaVO
     */
    HmeTimeProcessPdaVO scanEquipment(Long tenantId, HmeTimeProcessPdaDTO dto);

    /**
     * 扫描实物条码
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/19 15:16:55
     * @return com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2
     */
    HmeTimeProcessPdaVO2 scanBarcode(Long tenantId, HmeTimeProcessPdaDTO2 dto);

    /**
     * 进站
     *
     * @param tenantId 租户ID
     * @param dto 进站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/20 10:29:33
     * @return com.ruike.hme.api.dto.HmeTimeProcessPdaDTO3
     */
    HmeTimeProcessPdaDTO3 siteIn(Long tenantId, HmeTimeProcessPdaDTO3 dto);

    /**
     * 出站
     *
     * @param tenantId 租户ID
     * @param dto 出站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/20 10:51:42
     * @return com.ruike.hme.api.dto.HmeTimeProcessPdaDTO4
     */
    HmeTimeProcessPdaDTO4 siteOut(Long tenantId, HmeTimeProcessPdaDTO4 dto);

    /**
     * 默认设备查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/10 19:56:16
     * @return com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4
     */
    HmeTimeProcessPdaVO4 defectEquipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto);
}
