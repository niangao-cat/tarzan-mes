package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 半成品/成品入库扫描 应用服务
 *
 * @author Deng xu 2020/6/2 16:21
 */
public interface HmeFinishProductsInStorageService {

    /**
     * 半成品/成品入库扫描-扫描外箱条码获取信息
     *
     * @param tenantId      租户ID
     * @param containerVO   参数
     * @return : com.ruike.hme.domain.vo.HmeInStorageContainerVO
     */
    HmeInStorageContainerVO queryContainer(Long tenantId, HmeInStorageContainerVO containerVO);

    /**
     * 半成品/成品入库扫描-装箱明细查询
     *
     * @param tenantId    租户ID
     * @param containerId 外箱ID
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>
     */
    List<HmeInStorageMaterialVO> queryContainerDetail(Long tenantId, String containerId);

    /**
     * 半成品/成品入库扫描-入库
     *
     * @param tenantId        租户ID
     * @param containerVOList 容器对象集合
     */
    List<HmeInStorageContainerVO> finishProductsInStorage(Long tenantId, List<HmeInStorageContainerVO> containerVOList);

    /**
     * 目标仓库列表
     *
     * @param tenantId    租户id
     * @author sanfeng.zhang@hand-china.com 2020/8/13 14:48
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryLocatorList(Long tenantId);

    /**
     * 默认仓库
     *
     * @param tenantId     租户id
     * @author sanfeng.zhang@hand-china.com 2020/8/13 15:24
     * @return tarzan.modeling.domain.entity.MtModLocator
     */
    MtModLocator queryDefaultLocatorList(Long tenantId);

    /**
     * COS完工-条码扫描
     *
     * @param tenantId      租户id
     * @param codeVO        参数
     * @author sanfeng.zhang@hand-china.com 2020/9/22 11:17
     * @return com.ruike.hme.domain.vo.HmeCosResVO
     */
    HmeCosResVO scanMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO);

    /**
     * COS完工-条码进站
     *
     * @param tenantId        租户id
     * @param codeVO          参数
     * @author sanfeng.zhang@hand-china.com 2020/9/22 14:54
     * @return com.ruike.hme.domain.vo.HmeCosScanCodeVO2
     */
    HmeCosScanCodeVO2 materialLotSiteOut(Long tenantId, HmeCosScanCodeVO2 codeVO);

    /**
     * COS完工-未出站条码
     *
     * @param tenantId          租户id
     * @param codeVO            参数
     * @author sanfeng.zhang@hand-china.com 2020/9/22 15:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosResVO>
     */
    List<HmeCosResVO> querySiteInMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO);

    /**
     * 批量取消进站
     * 
     * @param tenantId
     * @param cosResVOList
     * @author sanfeng.zhang@hand-china.com 2020/11/16 14:18 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosResVO>
     */
    List<HmeCosResVO> batchCancelSiteIn(Long tenantId, List<HmeCosResVO> cosResVOList);

    /**
     * 有权限的目标仓库列表
     *
     * @param tenantId    租户id
     * @author li.zhang13@hand-china.com 2021/1/26 17:32
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryLocatorListPermission(Long tenantId);
}
