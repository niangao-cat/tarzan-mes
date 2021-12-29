package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.*;
import org.hzero.core.base.AopProxy;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
* @Classname HmeFinishProductsInStorageRepository
* @Description 半成品/成品入库扫描 资源库
* @Date  2020/6/2 16:25
* @Created by Deng xu
*/
public interface HmeFinishProductsInStorageRepository extends AopProxy<HmeFinishProductsInStorageRepository> {

    /**
    * @Description: 半成品/成品入库扫描-扫描外箱条码获取信息
    * @author: Deng Xu
    * @date 2020/6/2 16:35
    * @param tenantId 租户ID
    * @param containerVO 参数
    * @return : com.ruike.hme.domain.vo.HmeInStorageContainerVO
    * @version 1.0
    */
    HmeInStorageContainerVO queryContainer(Long tenantId ,HmeInStorageContainerVO containerVO);

    /**
    * @Description: 半成品/成品入库扫描-装箱明细查询
    * @author: Deng Xu
    * @date 2020/6/3 11:01
    * @param tenantId 租户ID
    * @param containerId 外箱ID
    * @return : java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>
    * @version 1.0
    */
    List<HmeInStorageMaterialVO> queryContainerDetail(Long tenantId , String containerId);

    /**
    * @Description: 半成品/成品入库扫描-入库
    * @author: Deng Xu
    * @date 2020/6/3 13:44
    * @param tenantId 租户ID
    * @param containerVOList 容器对象集合
    * @return : void
    * @version 1.0
    */
    List<HmeInStorageContainerVO> finishProductsInStorage(Long tenantId , List<HmeInStorageContainerVO> containerVOList);

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
     * @param tenantId      租户id
     * @author sanfeng.zhang@hand-china.com 2020/8/13 15:25
     * @return tarzan.modeling.domain.entity.MtModLocator
     */
    MtModLocator queryDefaultLocatorList(Long tenantId);

    /**
     * COS完工-扫描条码
     *
     * @param tenantId          租户id
     * @param codeVO            参数
     * @author sanfeng.zhang@hand-china.com 2020/9/22 11:30
     * @return com.ruike.hme.domain.vo.HmeCosResVO
     */
    HmeCosResVO scanMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO);

    /**
     * COS完工-条码进站
     *
     * @param tenantId          租户id
     * @param codeVO            参数
     * @author sanfeng.zhang@hand-china.com 2020/9/22 14:56
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
     * @author sanfeng.zhang@hand-china.com 2020/11/16 14:20
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
