package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtNcCodeDTO2;
import tarzan.method.api.dto.MtNcCodeDTO3;
import tarzan.method.api.dto.MtNcCodeDTO5;
import tarzan.method.api.dto.MtNcSecondaryCodeDTO;
import tarzan.method.app.service.MtNcCodeService;
import tarzan.method.app.service.MtNcSecondaryCodeService;
import tarzan.method.app.service.MtNcValidOperService;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.infra.mapper.MtNcCodeMapper;

/**
 * 不良代码数据应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Service
public class MtNcCodeServiceImpl implements MtNcCodeService {

    private static final String MT_NC_CODE_ATTR = "mt_nc_code_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private MtNcSecondaryCodeService mtNcSecondaryCodeService;

    @Autowired
    private MtNcValidOperService mtNcValidOperService;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;


    @Autowired
    private MtNcCodeMapper mtNcCodeMapper;

    @Override
    public Page<MtNcCodeDTO2> queryNcCodeListForUi(Long tenantId, MtNcCodeDTO2 dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtNcCodeMapper.queryNcCodeForUi(tenantId, dto));
    }

    @Override
    public MtNcCodeDTO3 queryNcCodeDetailForUi(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            return null;
        }
        MtNcCodeDTO3 result = new MtNcCodeDTO3();

        MtNcCodeDTO2 queryNcCodeDTO = new MtNcCodeDTO2();
        queryNcCodeDTO.setNcCodeId(ncCodeId);
        List<MtNcCodeDTO2> ncCodeList = mtNcCodeMapper.queryNcCodeForUi(tenantId, queryNcCodeDTO);
        if (CollectionUtils.isEmpty(ncCodeList)) {
            return null;
        }

        List<MtExtendAttrDTO> ncCodeAttrList =
                        mtExtendSettingsService.attrQuery(tenantId, ncCodeList.get(0).getNcCodeId(), MT_NC_CODE_ATTR);
        ncCodeList.get(0).setNcCodeAttrList(ncCodeAttrList);

        result.setMtNcCode(ncCodeList.get(0));
        result.setMtNcSecondaryCodeList(mtNcSecondaryCodeService.querySecondaryCodeListForUi(tenantId,
                        ncCodeList.get(0).getNcCodeId(), "NC_CODE"));
        result.setMtNcValidOperList(mtNcValidOperService.queryNcValidOperListForUi(tenantId,
                        ncCodeList.get(0).getNcCodeId(), "NC_CODE"));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveNcCodeForUi(Long tenantId, MtNcCodeDTO5 dto) {
        MtNcCode ncCode = new MtNcCode();
        BeanUtils.copyProperties(dto.getMtNcCode(), ncCode);
        // 校验数据唯一性
        MtNcCode vaildNc = new MtNcCode();
        vaildNc.setTenantId(tenantId);
        vaildNc.setNcCodeId(ncCode.getNcCodeId());
        vaildNc.setSiteId(ncCode.getSiteId());
        vaildNc.setNcCode(ncCode.getNcCode());

        Criteria criteria = new Criteria(vaildNc);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtNcCode.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNcCode.FIELD_SITE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNcCode.FIELD_NC_CODE, Comparison.EQUAL));
        if (StringUtils.isNotEmpty(vaildNc.getNcCodeId())) {
            whereFields.add(new WhereField(MtNcCode.FIELD_NC_CODE_ID, Comparison.NOT_EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (mtNcCodeMapper.selectOptional(vaildNc, criteria).size() > 0) {
            throw new MtException("MT_NC_CODE_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0002", "NC_CODE"));
        }

        List<MtNcSecondaryCodeDTO> secondaryCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getMtNcSecondaryCodeList())) {
            secondaryCodeList.addAll(dto.getMtNcSecondaryCodeList());
        }
        if (StringUtils.isNotEmpty(ncCode.getNcCodeId())) {
            secondaryCodeList.addAll(mtNcSecondaryCodeService.querySecondaryCodeListForUi(tenantId,
                            ncCode.getNcCodeId(), "NC_CODE"));
        }

        if ("Y".equals(ncCode.getSecondaryReqdForClose())) {
            if (CollectionUtils.isEmpty(secondaryCodeList)
                            || secondaryCodeList.stream().noneMatch(c -> "Y".equals(c.getRequiredFlag()))) {
                throw new MtException("MT_NC_CODE_0006",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0006", "NC_CODE"));
            }
        }

        // 保存不良代码
        ncCode.setTenantId(tenantId);
        if (StringUtils.isEmpty(ncCode.getNcCodeId())) {
            mtNcCodeRepository.insertSelective(ncCode);
        } else {
            mtNcCodeRepository.updateByPrimaryKeySelective(ncCode);
        }

        mtNcSecondaryCodeService.saveSecondaryCodeListForUi(tenantId, ncCode.getNcCodeId(), "NC_CODE",
                        dto.getMtNcSecondaryCodeList());
        mtNcValidOperService.saveNcValidOperListForUi(tenantId, ncCode.getNcCodeId(), "NC_CODE",
                        dto.getMtNcValidOperList());

        if (CollectionUtils.isNotEmpty(dto.getMtNcCode().getNcCodeAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_NC_CODE_ATTR, ncCode.getNcCodeId(), null,
                            dto.getMtNcCode().getNcCodeAttrList());
        }

        return ncCode.getNcCodeId();
    }
}
