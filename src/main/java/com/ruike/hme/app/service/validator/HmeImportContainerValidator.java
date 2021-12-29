package com.ruike.hme.app.service.validator;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeContainerImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 容器导入校验
 *
 * @author penglin.sui@hand-china.com 2020/8/18 20:30
 */
@ImportValidators({
        @ImportValidator(templateCode = "AMTC.SR")
})
public class HmeImportContainerValidator extends ValidatorHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    /**
     * 校验
     *
     * @author penglin.sui@hand-china.com 2020/8/18 20:30
     */
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {
            HmeContainerImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeContainerImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //校验容器编码
            if(StringUtils.isBlank(importVO.getContainerCode())){
                //容器编码不能为空
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_007", "HME"));
                return false;
            }
            int recordCount = mtContainerRepository.selectCountByCondition(Condition
                    .builder(MtContainer.class)
                    .andWhere(Sqls.custom().andEqualTo(MtContainer.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtContainer.FIELD_CONTAINER_CODE, importVO.getContainerCode()))
                    .build());
            if(recordCount > 0){
                //容器编码${1}已存在
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_008", "HME", importVO.getContainerCode()));
                return false;
            }

            //校验工厂
            if(StringUtils.isBlank(importVO.getSiteCode())){
                //工厂不能为空
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_002", "HME"));
                return false;
            }
            //获取工厂ID
            MtModSite mtModSitePara = new MtModSite();
            mtModSitePara.setTenantId(tenantId);
            mtModSitePara.setSiteCode(importVO.getSiteCode());
            mtModSitePara.setEnableFlag(HmeConstants.ConstantValue.YES);
            MtModSite mtModSite = mtModSiteRepository.selectOne(mtModSitePara);
            if(Objects.isNull(mtModSite)){
                //根据工厂代码 ${1}没有找到工厂信息
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_001", "HME", importVO.getSiteCode()));
                return false;
            }

            //校验容器类型
            if(StringUtils.isBlank(importVO.getContainerTypeCode())){
                //容器类型编码不能为空
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_003", "HME"));
                return false;
            }
            //获取容器编码ID
            MtContainerType mtContainerTypePara = new MtContainerType();
            mtContainerTypePara.setTenantId(tenantId);
            mtContainerTypePara.setContainerTypeCode(importVO.getContainerTypeCode());
            mtContainerTypePara.setEnableFlag(HmeConstants.ConstantValue.YES);
            MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(mtContainerTypePara);
            if(Objects.isNull(mtContainerType)){
                //根据容器类型编码${1}没有找到容器类型信息
                getContext().addErrorMsg( mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_004", "HME", importVO.getContainerTypeCode()));
                return false;
            }

            //校验容器状态
            if(StringUtils.isBlank(importVO.getContainerStatus())){
                //容器状态不能为空
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_005", "HME"));
                return false;
            }

            //获取货位ID
            if(!StringUtils.isBlank(importVO.getLocatorCode())){
                MtModLocator mtModLocatorPara = new MtModLocator();
                mtModLocatorPara.setTenantId(tenantId);
                mtModLocatorPara.setLocatorCode(importVO.getLocatorCode());
                mtModLocatorPara.setEnableFlag(HmeConstants.ConstantValue.YES);
                MtModLocator mtModLocator = mtModLocatorRepository.selectOne(mtModLocatorPara);
                if(Objects.isNull(mtModLocator)){
                    //根据容器类型编码${1}没有找到容器类型信息
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CONTAINER_IMPORT_006", "HME", importVO.getLocatorCode()));
                    return false;
                }
            }
        }
        return true;
    }
}
