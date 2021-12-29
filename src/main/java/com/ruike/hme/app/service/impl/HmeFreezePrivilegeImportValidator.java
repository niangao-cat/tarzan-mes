package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeImportVO;
import com.ruike.hme.infra.mapper.HmeFreezePrivilegeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 冻结解冻权限导入 校验
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/24 16:53
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.FREEZE_PRIVILEGE_IMPORT")
})
@Slf4j
public class HmeFreezePrivilegeImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeFreezePrivilegeMapper hmeFreezePrivilegeMapper;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {

            HmeFreezePrivilegeImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeFreezePrivilegeImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            if (importVO.getTenantId() != null) {
                tenantId = importVO.getTenantId();
            }

            //邮箱格式检验
            if (!CommonUtils.isEmail(importVO.getEmail())) {
                getContext().addErrorMsg("邮箱格式错误");
                return false;
            }

            String detailObjectId = null;
            //若输入则校验对象编码存在性
            if (StringUtils.isNotBlank(importVO.getDetailObjectType()) && StringUtils.isNotBlank(importVO.getDetailObjectCode())) {
                if ("PROD_LINE".equals(importVO.getDetailObjectType())) {
                    List<MtModProductionLine> mtModProductionLines = mtModProductionLineRepository.select(MtModProductionLine.FIELD_PROD_LINE_CODE, importVO.getDetailObjectCode());
                    if (CollectionUtils.isEmpty(mtModProductionLines)) {
                        //当前产线编码不存在
                        getContext().addErrorMsg("当前产线编码不存在");
                        return false;
                    } else {
                        detailObjectId = mtModProductionLines.get(0).getProdLineId();
                    }
                } else if ("WAREHOUSE".equals(importVO.getDetailObjectType())) {
                    List<MtModLocator> mtModLocators = mtModLocatorRepository.select(MtModLocator.FIELD_LOCATOR_CODE, importVO.getDetailObjectCode());
                    if (CollectionUtils.isEmpty(mtModLocators)) {
                        //当前仓库编码不存在
                        getContext().addErrorMsg("当前仓库编码不存在");
                        return false;
                    } else {
                        detailObjectId = mtModLocators.get(0).getLocatorId();
                    }
                }
            }else if((StringUtils.isNotBlank(importVO.getDetailObjectType()) && StringUtils.isBlank(importVO.getDetailObjectCode()))
                    || (StringUtils.isBlank(importVO.getDetailObjectType()) && StringUtils.isNotBlank(importVO.getDetailObjectCode()))){
                getContext().addErrorMsg("对象类型和对象编码需同时有值或无值");
                return false;
            }
            //用户
            Long userId = hmeFreezePrivilegeMapper.getUserId(importVO.getRealName());
            if (Objects.isNull(userId)) {
                //该用户不存在
                getContext().addErrorMsg("该用户不存在");
                return false;
            }

            int flag = hmeFreezePrivilegeMapper.selectExist(tenantId, userId, detailObjectId);
            log.info("===================tenantId"+tenantId+"=========flag>"+flag);
            if (flag > 0) {
                //用户（显示用户账号）+对象类型（显示对象类型）+对象编码（显示对象编码）重复
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME.FREEZE_PRIVILEGE_001", "HME", importVO.getRealName(), importVO.getDetailObjectType(), importVO.getDetailObjectCode()));
                return false;
            }
        }
        return true;
    }
}
