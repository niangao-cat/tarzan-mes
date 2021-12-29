package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4;

import java.util.List;

/**
 * 时效加工作业平台资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:54:19
 **/
public interface HmeTimeProcessPdaRepository {

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

    /**
     * 对条码的工单的工艺工步校验
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param operationId 登录工位对应的工艺Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/1 14:06:19
     * @return void
     */
    void operationVerify(Long tenantId, String materialLotId, String operationId);
}
