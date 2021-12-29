package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeLoadContainerVO;
import org.hzero.core.base.AopProxy;

/**
 * 批量完工装箱 资源库
 *
 * @author Deng xu 2020/6/5 9:41
 */
public interface HmeBatchLoadContainerRepository extends AopProxy<HmeFinishProductsInStorageRepository> {

    /**
     * 批量完工装箱-扫描外箱条码
     *
     * @param tenantId           租户ID
     * @param outerContainerCode 外箱条码
     * @return : com.ruike.hme.domain.vo.HmeLoadContainerVO
     * @author Deng Xu 2020/6/5 10:17
     */
    HmeLoadContainerVO scanOuterContainer(Long tenantId, String outerContainerCode);

    /**
     * 批量完工装箱-扫描待装箱条码
     *
     * @param tenantId        租户ID
     * @param loadContainerVO 外箱条码ID、待装箱条码code
     * @return : com.ruike.hme.domain.vo.HmeLoadContainerVO
     * @author Deng Xu 2020/6/7 15:12
     */
    HmeLoadContainerVO scanContainer(Long tenantId, HmeLoadContainerVO loadContainerVO);

    /**
     * 批量完工装箱-卸载容器/物料批
     *
     * @param tenantId      租户ID
     * @param loadContainer 待装箱条码信息VO
     * @author Deng Xu 2020/6/5 11:37
     */
    void unloadContainer(Long tenantId, HmeLoadContainerVO loadContainer);

    /**
     * 批量完工装箱-容器/物料批装箱
     *
     * @param tenantId        租户ID
     * @param loadContainerVO 待装箱容器/物料批、外箱ID
     * @author Deng Xu 2020/6/5 16:08
     */
    void loadContainer(Long tenantId, HmeLoadContainerVO loadContainerVO);

    /**
     * 验证容器/物料批
     * 
     * @param tenantId
     * @param loadContainerVO
     * @author sanfeng.zhang@hand-china.com 2020/8/27 17:10 
     * @return com.ruike.hme.domain.vo.HmeLoadContainerVO
     */
    HmeLoadContainerVO verifyContainer(Long tenantId, HmeLoadContainerVO loadContainerVO);

    /**
     * 卸载原外箱容器/物料批
     *
     * @param tenantId          租户id
     * @param loadContainerVO   参数
     * @author sanfeng.zhang@hand-china.com 2020/8/27 18:29
     * @return com.ruike.hme.domain.vo.HmeLoadContainerVO
     */
    HmeLoadContainerVO unloadOriginalContainer(Long tenantId, HmeLoadContainerVO loadContainerVO);
}
