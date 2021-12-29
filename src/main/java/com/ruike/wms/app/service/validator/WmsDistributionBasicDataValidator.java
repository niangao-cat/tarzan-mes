package com.ruike.wms.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.domain.vo.WmsDistributionBasicVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.DistributionType.*;

/**
 * 配送基础数据维护导入校验
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/3 18:36
 */
@ImportValidators({
        @ImportValidator(templateCode = "WMS.DISTRIBUTION_BASIC")
})
public class WmsDistributionBasicDataValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (StringUtils.isNotBlank(data)) {
            WmsDistributionBasicVO importVO;
            try {
                importVO = objectMapper.readValue(data, WmsDistributionBasicVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //策略类型
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DISTRIBUTION", tenantId);
            List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getMeaning(), importVO.getDistributionType())).collect(Collectors.toList());
            String distributionType = "";
            if (CollectionUtils.isEmpty(collect)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_022", "HME"));
                return false;
            }
            distributionType = collect.get(0).getValue();

            if (StringUtils.isBlank(importVO.getEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_023", "HME"));
                return false;
            }

            //站点
            MtModSite mtModSite = new MtModSite();
            mtModSite.setSiteCode(importVO.getSiteCode());
            mtModSite.setEnableFlag(HmeConstants.ConstantValue.YES);
            List<MtModSite> siteList = mtModSiteRepository.select(mtModSite);
            if (CollectionUtils.isEmpty(siteList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_021", "HME", importVO.getSiteCode()));
                return false;
            }

            //产线
            MtModProductionLine line = new MtModProductionLine();
            line.setProdLineCode(importVO.getProductionLineCode());
            line.setEnableFlag(HmeConstants.ConstantValue.YES);
            List<MtModProductionLine> lineList = mtModProductionLineRepository.select(line);
            if (CollectionUtils.isEmpty(lineList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_020", "HME", importVO.getProductionLineCode()));
                return false;
            }

            if (StringUtils.isBlank(importVO.getMaterialGroupCode()) && StringUtils.isBlank(importVO.getMaterialCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_019", "HME"));
                return false;
            }

            if(StringUtils.isBlank(importVO.getWorkcellCode())&&(StringUtils.isNotBlank(importVO.getBackflushFlag())||null != importVO.getEveryQty()))
            {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_013", "WMS"));
                return false;
            }

            switch (distributionType) {
                case MIN_MAX:
                    if (importVO.getInventoryLevel() == null) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DIS_BASIC_0002", "WMS"));
                        return false;
                    }
                    break;
                case PROPORTION_DISTRIBUTION:
                    if (importVO.getProportion() == null) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DIS_BASIC_0003", "WMS"));
                        return false;
                    }
                    break;
                case PACKAGE_DELIVERY:
                    if (importVO.getMinimumPackageQty() == null) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DIS_BASIC_0004", "WMS"));
                        return false;
                    }
                    break;
                default:
                    break;
            }

        }
        return true;
    }

}
