package com.ruike.hme.app.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeExceptionRouter;
import com.ruike.hme.domain.vo.HmeContainerImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 容器导入
 *
 * @author penglin.sui@hand-china.com 2020/07/21 15:36
 */
@ImportService(templateCode = "AMTC.SR")
public class HmeImportContainerServiceImpl implements IBatchImportService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租戶id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        List<MtContainer> mtContainerList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(data)){
            for(String vo : data){
                HmeContainerImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeContainerImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                MtContainer mtContainer = new MtContainer();
                mtContainer.setTenantId(tenantId);
                mtContainer.setContainerCode(importVO.getContainerCode());
                mtContainer.setIdentification(importVO.getContainerCode());

                //获取工厂ID
                MtModSite mtModSitePara = new MtModSite();
                mtModSitePara.setTenantId(tenantId);
                mtModSitePara.setSiteCode(importVO.getSiteCode());
                mtModSitePara.setEnableFlag(HmeConstants.ConstantValue.YES);
                MtModSite mtModSite = mtModSiteRepository.selectOne(mtModSitePara);
                mtContainer.setSiteId(mtModSite.getSiteId());

                //获取容器编码ID
                MtContainerType mtContainerTypePara = new MtContainerType();
                mtContainerTypePara.setTenantId(tenantId);
                mtContainerTypePara.setContainerTypeCode(importVO.getContainerTypeCode());
                mtContainerTypePara.setEnableFlag(HmeConstants.ConstantValue.YES);
                MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(mtContainerTypePara);
                mtContainer.setContainerTypeId(mtContainerType.getContainerTypeId());

                mtContainer.setStatus(importVO.getContainerStatus());

                mtContainer.setContainerName(importVO.getContainerName());
                mtContainer.setDescription(importVO.getDescription());

                //获取货位ID
                if(!StringUtils.isBlank(importVO.getLocatorCode())){
                    MtModLocator mtModLocatorPara = new MtModLocator();
                    mtModLocatorPara.setTenantId(tenantId);
                    mtModLocatorPara.setLocatorCode(importVO.getLocatorCode());
                    mtModLocatorPara.setEnableFlag(HmeConstants.ConstantValue.YES);
                    MtModLocator mtModLocator = mtModLocatorRepository.selectOne(mtModLocatorPara);
                    mtContainer.setLocatorId(mtModLocator.getLocatorId());
                }

                mtContainerList.add(mtContainer);
            }

            if(CollectionUtils.isNotEmpty(mtContainerList)){
                mtContainerRepository.batchInsertSelective(mtContainerList);
            }
        }

        return true;
    }

    @Override
    public int getSize(){
        return 500;
    }
}
