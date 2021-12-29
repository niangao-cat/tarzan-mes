package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRouterLabCodeDTO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.app.service.MtGenTypeService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.vo.MtGenTypeVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO4;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * HmeRouterLabCodeValidator
 * 工艺实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/03/31 10:32:12
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.ROUTER_LABCODE")
})
public class HmeRouterLabCodeValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtRouterRepository mtRouterRepository;
    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtGenTypeService mtGenTypeService;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeRouterLabCodeDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeRouterLabCodeDTO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 业务校验
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 业务逻辑校验
     *
     * @param tenantId 租户ID
     * @param importVO 导入单行数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/31 10:41:35
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeRouterLabCodeDTO importVO){
        boolean flag = true;
        //站点是否存在、有效性校验
        MtModSite mtModSite = mtModSiteRepository.selectOne(new MtModSite() {{
            setTenantId(tenantId);
            setSiteCode(importVO.getSiteCode());
        }});
        if(Objects.isNull(mtModSite) || !HmeConstants.ConstantValue.YES.equals(mtModSite.getEnableFlag())){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEL_IMPORT_043", "HME", importVO.getSiteCode()));
            flag = false;
        }
        //工艺类型是否存在校验
        String routerType = null;
        MtGenTypeVO3 mtGenTypeVO3 = new MtGenTypeVO3();
        mtGenTypeVO3.setTypeGroup("ROUTER_TYPE");
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(9999);
        Page<MtGenTypeVO4> mtGenTypeVO4Page = mtGenTypeService.listGenTypeForUi(tenantId, mtGenTypeVO3, pageRequest);
        if(CollectionUtils.isEmpty(mtGenTypeVO4Page.getContent())){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEL_IMPORT_044", "HME", importVO.getRouterType()));
            flag = false;
            return flag;
        }else{
            List<MtGenTypeVO4> mtGenTypeVO4List = mtGenTypeVO4Page.getContent().stream().filter(item -> importVO.getRouterType().equals(item.getDescription())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(mtGenTypeVO4List)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_044", "HME", importVO.getRouterType()));
                flag = false;
                return flag;
            }else{
                routerType = mtGenTypeVO4List.get(0).getTypeCode();
            }
        }
        //工艺路线+版本+工艺类型是否存在校验
        MtRouter mtRouterQuery = new MtRouter();
        mtRouterQuery.setTenantId(tenantId);
        mtRouterQuery.setRouterName(importVO.getRouterName());
        mtRouterQuery.setRevision(importVO.getRevision());
        mtRouterQuery.setRouterType(routerType);
        MtRouter mtRouter = mtRouterRepository.selectOne(mtRouterQuery);
        if(Objects.isNull(mtRouter)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEL_IMPORT_037", "HME", importVO.getRouterName(), importVO.getRevision(), importVO.getRouterType()));
            flag = false;
        }else {
            //工艺路线是否分配站点校验
            List<MtRouterSiteAssign> mtRouterSiteAssignList = mtRouterSiteAssignRepository.select(new MtRouterSiteAssign() {{
                setTenantId(tenantId);
                setRouterId(mtRouter.getRouterId());
            }});
            if(CollectionUtils.isEmpty(mtRouterSiteAssignList)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_038", "HME", importVO.getRouterName()));
                flag = false;
            }else{
                //工艺路线与站点是否匹配校验
                if(Objects.nonNull(mtModSite)){
                    MtRouterSiteAssign mtRouterSiteAssign = mtRouterSiteAssignRepository.selectOne(new MtRouterSiteAssign() {{
                        setTenantId(tenantId);
                        setRouterId(mtRouter.getRouterId());
                        setSiteId(mtModSite.getSiteId());
                    }});
                    if(Objects.isNull(mtRouterSiteAssign)){
                        getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_039", "HME", importVO.getSiteCode(), importVO.getRouterName()));
                        flag = false;
                    }
                }
            }
            //工艺路线是否有此步骤识别码校验
            MtRouterStep mtRouterStep = mtRouterStepRepository.selectOne(new MtRouterStep() {{
                setTenantId(tenantId);
                setRouterId(mtRouter.getRouterId());
                setStepName(importVO.getStepName());
            }});
            if(Objects.isNull(mtRouterStep)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_040", "HME", importVO.getRouterName(), importVO.getStepName()));
                flag = false;
            }
        }
        return flag;
    }
}
