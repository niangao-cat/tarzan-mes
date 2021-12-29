package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO;
import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO2;
import com.ruike.hme.api.dto.HmeEoJobPumpCombDTO3;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 泵浦源组合关系表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
public interface HmeEoJobPumpCombService {

    /**
     * 扫描工单。对工单物料进行校验并自动获取物料序列号
     * 
     * @param tenantId 租户ID
     * @param dto 工单ID、工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 11:52:25 
     * @return com.ruike.hme.domain.vo.HmeEoJobPumpCombVO
     */
    HmeEoJobPumpCombVO scanWorkOrder(Long tenantId, HmeEoJobPumpCombDTO dto);

    /**
     * 进站标签打印
     *
     * @param tenantId 租户ID
     * @param dto 打印信息
     * @param response 响应体
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 03:27:56
     * @return void
     */
    void siteInPrint(Long tenantId, HmeEoJobPumpCombDTO2 dto, HttpServletResponse response);

    /**
     * 条码扫描-从首序工序作业平台复制而来加以改造
     * 
     * @param tenantId 租户ID
     * @param dto 扫描信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 07:23:11 
     * @return com.ruike.hme.domain.vo.HmeEoJobSnBatchVO14
     */
    HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchDTO2 dto);

    /**
     * 批量工序作业平台-条码绑定校验
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnBatchVO8
     */
    HmeEoJobSnBatchVO8 releaseScanValidate(Long tenantId, HmeEoJobSnBatchDTO2 dto);

    /**
     * 组合子条码打印
     *
     * @param tenantId 租户ID
     * @param dto 打印信息
     * @param response 响应体
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/24 04:46:34
     * @return void
     */
    void subBarcodePrint(Long tenantId, HmeEoJobPumpCombDTO3 dto, HttpServletResponse response);

    /**
     * 投料退回-从首序工序作业平台复制而来加以改造
     *
     * @param tenantId 租户ID
     * @param dto 投料退回信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 09:55:09
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO9
     */
    HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto);

    /**
     * 出站-从首序工序作业平台复制而来加以改造
     *
     * @param tenantId 租户ID
     * @param dto 出站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 01:47:51
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     */
    HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 删除物料-从首序工序作业平台复制而来加以改造
     *
     * @param tenantId 租户ID
     * @param dto 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 08:03:03
     * @return com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4
     */
    HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId , HmeEoJobSnBatchVO4 dto);

    /**
     * 进站
     * 
     * @param tenantId 租户ID
     * @param dto 进站数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/26 11:15:38 
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO
     */
    HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);
}
