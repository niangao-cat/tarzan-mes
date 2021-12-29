package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeCmsEoSnRelImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.io.IOException;
import java.util.List;

/**
 * CMS设备光纤EO码和盖板SN码数据导入校验
 *
 *@author qinxia.huang@raycus-china.com 2021/9/28 11:05
 */

@ImportValidators({
        @ImportValidator(templateCode = "HME.CMS_EO_SN_REL")
})
public class HmeCmsEoSnRelImportValidator extends ValidatorHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    /**
     * 校验
     *
     *@author qinxia.huang@raycus-china.com 2021/9/28 11:05
     */
    @Override
    public boolean validate(String data) {
        //校验租户信息，如果为空值，则默认以租户0填充
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        //校验传入的数据是否不为空或者不为空格
        if (data != null && !"".equals(data)) {
            HmeCmsEoSnRelImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data,  HmeCmsEoSnRelImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 设备编码,检验设备编码是否为空
            if (StringUtils.isBlank(importVO.getEquipmentNum())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CMSEOSN_IMPORT_001", "HME", "设备编码"));
                return false;
            }
            // 光纤EO检验是否为空以及不为空时是否存在于mes系统中
            if (StringUtils.isBlank(importVO.getIdentification())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CMSEOSN_IMPORT_001", "HME", "光纤EO"));
                return false;
            }
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtEo.FIELD_IDENTIFICATION, importVO.getIdentification())).build());
            if (CollectionUtils.isEmpty(mtEoList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CMSEOSN_IMPORT_002", "HME", importVO.getIdentification()));
                return false;
            }

            // 盖板SN，检验是否为空
            if (StringUtils.isBlank(importVO.getSnNum())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CMSEOSN_IMPORT_001", "HME", "盖板SN"));
                return false;
            }

        }

        return true;
    }

}
