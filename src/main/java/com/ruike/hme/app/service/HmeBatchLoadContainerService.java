package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeLoadContainerVO;

/**
 * 批量完工装箱 应用服务
 *
 * @author Deng xu
 * @date 2020/6/5 9:52
 */
public interface HmeBatchLoadContainerService {

    /**
     * 批量完工装箱-扫描外箱条码
     * @author Deng Xu
     * @date 2020/6/5 11:27
     * @param tenantId 租户ID
     * @param outerContainerCode 外箱条码
     * @return com.ruike.hme.domain.vo.HmeLoadContainerVO
    */
    HmeLoadContainerVO scanOuterContainer(Long tenantId , String outerContainerCode);

    /**
     * 批量完工装箱-扫描待装箱条码
     * @author Deng Xu
     * @date 2020/6/5 15:07
     * @param tenantId 租户ID
     * @param loadContainerVO 外箱条码ID、待装箱条码code
     * @return : com.ruike.hme.domain.vo.HmeLoadContainerVO
    */
    HmeLoadContainerVO scanContainer(Long tenantId ,HmeLoadContainerVO loadContainerVO);

    /**
     * 批量完工装箱-卸载容器/物料批
     * @author Deng Xu
     * @date 2020/6/5 12:30
     * @param tenantId 租户ID
     * @param loadContainer 待装箱条码信息VO
    */
    HmeLoadContainerVO unloadContainer(Long tenantId , HmeLoadContainerVO loadContainer);

    /**
     * 批量完工装箱-容器/物料批装箱
     * @author Deng Xu
     * @date 2020/6/6 16:19
     * @param tenantId 租户ID
     * @param loadContainerVO 待装箱容器/物料批、外箱ID
     */
    HmeLoadContainerVO loadContainer(Long tenantId , HmeLoadContainerVO loadContainerVO);

    /**
     * 验证容器/物料批
     *
     * @param tenantId              租户id
     * @param loadContainerVO       参数
     * @author sanfeng.zhang@hand-china.com 2020/8/27 17:09
     * @return com.ruike.hme.domain.vo.HmeLoadContainerVO
     */
    HmeLoadContainerVO verifyContainer(Long tenantId , HmeLoadContainerVO loadContainerVO);

    /**
     * 卸载原外箱容器/物料批
     * 
     * @param tenantId              租户id
     * @param loadContainerVO       参数
     * @author sanfeng.zhang@hand-china.com 2020/8/27 17:35 
     * @return com.ruike.hme.domain.vo.HmeLoadContainerVO
     */
    HmeLoadContainerVO unloadOriginalContainer(Long tenantId , HmeLoadContainerVO loadContainerVO);
}
