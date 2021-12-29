package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeFinishProductsInStorageService;
import com.ruike.hme.domain.repository.HmeFinishProductsInStorageRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
* @Classname HmeFinishProductsInStorageServiceImpl
* @Description 半成品/成品入库扫描 应用服务默认实现
* @Date  2020/6/2 16:23
* @Created by Deng xu
*/
@Service
public class HmeFinishProductsInStorageServiceImpl implements HmeFinishProductsInStorageService {

    @Autowired
    private HmeFinishProductsInStorageRepository hmeFinishProductsInStorageRepository;

    /**
    * @Description: 半成品/成品入库扫描-扫描外箱条码获取信息
    * @author: Deng Xu
    * @date 2020/6/3 10:56
    * @param tenantId 租户ID
    * @param containerVO 参数
    * @return : com.ruike.hme.domain.vo.HmeInStorageContainerVO
    * @version 1.0
    */
    @Override
    public HmeInStorageContainerVO queryContainer(Long tenantId, HmeInStorageContainerVO containerVO) {
        return hmeFinishProductsInStorageRepository.queryContainer(tenantId, containerVO);
    }

    /**
    * @Description: 半成品/成品入库扫描-装箱明细查询
    * @author: Deng Xu
    * @date 2020/6/3 11:30
    * @param tenantId 租户ID
    * @param containerId 外箱ID
    * @return : java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>
    * @version 1.0
    */
    @Override
    public List<HmeInStorageMaterialVO> queryContainerDetail(Long tenantId, String containerId) {
        return hmeFinishProductsInStorageRepository.queryContainerDetail(tenantId,containerId);
    }

    /**
    * @Description: 半成品/成品入库扫描-入库
    * @author: Deng Xu
    * @date 2020/6/3 13:49
    * @param tenantId 租户ID
    * @param containerVOList 容器对象集合
    * @return : void
    * @version 1.0
    */
    @Override
    public List<HmeInStorageContainerVO> finishProductsInStorage(Long tenantId, List<HmeInStorageContainerVO> containerVOList) {
        return hmeFinishProductsInStorageRepository.finishProductsInStorage(tenantId,containerVOList);
    }

    @Override
    public List<MtModLocator> queryLocatorList(Long tenantId) {
        return hmeFinishProductsInStorageRepository.queryLocatorList(tenantId);
    }

    @Override
    public MtModLocator queryDefaultLocatorList(Long tenantId) {
        return hmeFinishProductsInStorageRepository.queryDefaultLocatorList(tenantId);
    }

    @Override
    public HmeCosResVO scanMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO) {
        return hmeFinishProductsInStorageRepository.scanMaterialLot(tenantId, codeVO);
    }

    @Override
    public HmeCosScanCodeVO2 materialLotSiteOut(Long tenantId, HmeCosScanCodeVO2 codeVO) {
        return hmeFinishProductsInStorageRepository.materialLotSiteOut(tenantId, codeVO);
    }

    @Override
    public List<HmeCosResVO> querySiteInMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO) {
        return hmeFinishProductsInStorageRepository.querySiteInMaterialLot(tenantId, codeVO);
    }

    @Override
    public List<HmeCosResVO> batchCancelSiteIn(Long tenantId, List<HmeCosResVO> cosResVOList) {
        return hmeFinishProductsInStorageRepository.batchCancelSiteIn(tenantId, cosResVOList);
    }

    @Override
    public List<MtModLocator> queryLocatorListPermission(Long tenantId) {
        return hmeFinishProductsInStorageRepository.queryLocatorListPermission(tenantId);
    }


}
