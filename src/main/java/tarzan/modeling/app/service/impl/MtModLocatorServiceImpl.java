package tarzan.modeling.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.app.service.MtExtendSettingsService;
import tarzan.modeling.api.dto.MtModLocatorDTO5;
import tarzan.modeling.app.service.MtModLocatorService;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO3;
import tarzan.modeling.domain.vo.MtModLocatorVO5;
import tarzan.modeling.domain.vo.MtModLocatorVO6;
import tarzan.modeling.infra.mapper.MtModLocatorMapper;

/**
 * 库位应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModLocatorServiceImpl extends BaseServiceImpl<MtModLocator> implements MtModLocatorService {

    private static final String MT_MOD_LOCATOR_ATTR = "mt_mod_locator_attr";

    @Autowired
    private MtModLocatorMapper mtModLocatorMapper;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public Page<MtModLocatorVO6> listModLocatorForUi(Long tenantId, MtModLocatorVO5 condition,
                                                     PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtModLocatorMapper.selectByConditionForUi(tenantId, condition));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveModLocatorForUi(Long tenantId, MtModLocatorDTO5 dto) {
        MtModLocator locator = new MtModLocator();
        BeanUtils.copyProperties(dto, locator);
        if (StringUtils.isNotEmpty(dto.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getLocatorId());
            if (mtModLocator != null) {
                locator.setParentLocatorId(mtModLocator.getParentLocatorId());
            }
        }
        String locatorId = mtModLocatorRepository.locatorBasicPropertyUpdate(tenantId, locator, "Y");

        if (CollectionUtils.isNotEmpty(dto.getLocatorAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_LOCATOR_ATTR, locatorId, null, dto.getLocatorAttrList());
        }

        return locatorId;
    }

    @Override
    public MtModLocatorVO3 detailModLocatorForUi(Long tenantId, String locatorId) {
        MtModLocatorVO3 vo = mtModLocatorMapper.selectByIdCustom(tenantId, locatorId);
        if (vo != null && vo.getLocatorId() != null) {
            vo.setLocatorAttrList(mtExtendSettingsService.attrQuery(tenantId, locatorId, MT_MOD_LOCATOR_ATTR));
        }
        return vo;
    }

}
