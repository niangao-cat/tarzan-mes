package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.api.dto.*;
import tarzan.general.app.service.MtTagGroupAssignService;
import tarzan.general.app.service.MtTagGroupHisService;
import tarzan.general.app.service.MtTagGroupObjectService;
import tarzan.general.app.service.MtTagGroupService;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtTagGroupVO1;
import tarzan.general.infra.mapper.MtTagGroupObjectMapper;

/**
 * 数据收集组表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupServiceImpl implements MtTagGroupService {

    private static final String MT_TAG_GROUP_ATTR = "mt_tag_group_attr";

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtTagGroupAssignService mtTagGroupAssignService;

    @Autowired
    private MtTagGroupObjectService mtTagGroupObjectService;

    @Autowired
    private MtTagGroupHisService mtTagGroupHisService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepo;

    @Autowired
    private MtTagGroupObjectMapper mtTagGroupObjectMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Override
    public Page<MtTagGroupDTO2> queryTagGroupListForUi(Long tenantId, MtTagGroupDTO2 dto, PageRequest pageRequest) {
        MtTagGroupVO1 mtTagGroupVO1 = new MtTagGroupVO1();
        BeanUtils.copyProperties(dto,mtTagGroupVO1);
        return PageHelper.doPage(pageRequest, () -> mtTagGroupRepo.propertyLimitTagGroupPropertyQuery(tenantId, mtTagGroupVO1));
    }

    @Override
    public MtTagGroupDTO4 queryTagGroupDetailForUi(Long tenantId, String tagGroupId) {
        if (StringUtils.isEmpty(tagGroupId)) {
            return null;
        }

        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setTenantId(tenantId);
        mtTagGroup.setTagGroupId(tagGroupId);
        mtTagGroup = mtTagGroupRepo.selectOne(mtTagGroup);
        if (null == mtTagGroup) {
            return null;
        }
        MtTagGroupDTO3 mtTagGroupDTO = new MtTagGroupDTO3();
        BeanUtils.copyProperties(mtTagGroup, mtTagGroupDTO);
        if (StringUtils.isNotEmpty(mtTagGroupDTO.getSourceGroupId())) {
            MtTagGroup temp = new MtTagGroup();
            temp.setTenantId(tenantId);
            temp.setTagGroupId(mtTagGroupDTO.getSourceGroupId());
            temp = mtTagGroupRepo.selectOne(temp);
            if (temp != null) {
                mtTagGroupDTO.setSourceGroupCode(temp.getTagGroupCode());
            }
        }

        List<MtExtendAttrDTO> tagGroupAttrList =
                        mtExtendSettingsService.attrQuery(tenantId, tagGroupId, MT_TAG_GROUP_ATTR);
        mtTagGroupDTO.setTagGroupAttrList(tagGroupAttrList);

        // tag group object
        MtTagGroupObjectDTO2 mtTagGroupObjectDTO =
                        mtTagGroupObjectService.queryTagGroupObjectForUi(tenantId, mtTagGroup.getTagGroupId());

        //2020/9/15 add by sanfeng.zhang 物料版本
        if(StringUtils.isNotBlank(mtTagGroupObjectDTO.getProductionVersion())){
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);

            MtTagGroupObjectDTO4 dto = new MtTagGroupObjectDTO4();
            dto.setProductionVersion(mtTagGroupObjectDTO.getProductionVersion());
            dto.setSiteId(defaultSiteId);
            dto.setMaterialId(mtTagGroupObjectDTO.getMaterialId());
            List<MtTagGroupObjectDTO3> mtTagGroupObjectDTO3s = mtTagGroupObjectMapper.productionVersionQuery(tenantId, dto);
            if(CollectionUtils.isNotEmpty(mtTagGroupObjectDTO3s)){
                mtTagGroupObjectDTO.setDescription(mtTagGroupObjectDTO3s.get(0).getDescription());
            }
        }

        MtTagGroupDTO4 result = new MtTagGroupDTO4();
        result.setMtTagGroupDTO(mtTagGroupDTO);
        result.setMtTagGroupObjectDTO(mtTagGroupObjectDTO);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTagGroupForUi(Long tenantId, MtTagGroupDTO5 dto) {
        MtTagGroup mtTagGroup = new MtTagGroup();
        BeanUtils.copyProperties(dto, mtTagGroup);
        mtTagGroup.setTenantId(tenantId);

        if (StringUtils.isEmpty(mtTagGroup.getTagGroupId())) {
            MtTagGroup queryTagGroup = new MtTagGroup();
            queryTagGroup.setTenantId(tenantId);
            queryTagGroup.setTagGroupCode(mtTagGroup.getTagGroupCode());
            if (null != mtTagGroupRepo.selectOne(queryTagGroup)) {
                throw new MtException("MT_GENERAL_0047",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_GENERAL_0047", "GENERAL"));
            }
            mtTagGroupRepo.insertSelective(mtTagGroup);
        } else {
            mtTagGroupRepo.updateByPrimaryKeySelective(mtTagGroup);
        }

        // record history
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("TAG_GROUP_UPDATE");
        String eventId = mtEventRepo.eventCreate(tenantId, mtEventCreateVO);
        String mtTagGroupHisId = mtTagGroupHisService.saveTagGroupHis(tenantId, eventId, mtTagGroup);

        if (CollectionUtils.isNotEmpty(dto.getTagGroupAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_TAG_GROUP_ATTR, mtTagGroup.getTagGroupId(), null,
                            dto.getTagGroupAttrList(), mtTagGroupHisId);
        }
        // save tag group assign
        if (CollectionUtils.isNotEmpty(dto.getMtTagGroupAssignDTO())) {
            mtTagGroupAssignService.saveTagGroupAssignForUi(tenantId, mtTagGroup.getTagGroupId(),
                            dto.getMtTagGroupAssignDTO());
        }

        // save tag group object
        if (null == dto.getMtTagGroupObjectDTO() || ObjectFieldsHelper.isAllFieldNull(dto.getMtTagGroupObjectDTO())) {
            throw new MtException("MT_GENERAL_0053",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_GENERAL_0053", "GENERAL"));
        }
        mtTagGroupObjectService.saveTagGroupObjectForUi(tenantId, mtTagGroup.getTagGroupId(),
                        dto.getMtTagGroupObjectDTO());

        return mtTagGroup.getTagGroupId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyTagGroupForUi(Long tenantId, MtTagGroupDTO6 dto) {
        if (StringUtils.isEmpty(dto.getSourceTagGroupId())) {
            return null;
        }

        MtTagGroup queryTagGroup = new MtTagGroup();
        queryTagGroup.setTenantId(tenantId);
        queryTagGroup.setTagGroupCode(dto.getTagGroupCode());
        if (null != mtTagGroupRepo.selectOne(queryTagGroup)) {
            throw new MtException("MT_GENERAL_0047",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_GENERAL_0047", "GENERAL"));
        }

        // query source tag group
        MtTagGroup srcTagGroup = new MtTagGroup();
        srcTagGroup.setTenantId(tenantId);
        srcTagGroup.setTagGroupId(dto.getSourceTagGroupId());
        srcTagGroup = mtTagGroupRepo.selectOne(srcTagGroup);

        // save tag group
        MtTagGroup curTagGroup = new MtTagGroup();
        BeanUtils.copyProperties(srcTagGroup, curTagGroup);
        curTagGroup.setTagGroupId(null);
        curTagGroup.setTagGroupCode(dto.getTagGroupCode());
        curTagGroup.setTagGroupDescription(dto.getTagGroupDescription());
        curTagGroup.set_tls(dto.get_tls());
        mtTagGroupRepo.insertSelective(curTagGroup);

        // save tag group attr
        List<MtExtendAttrDTO> tagGroupAttrList =
                        mtExtendSettingsService.attrQuery(tenantId, srcTagGroup.getTagGroupId(), MT_TAG_GROUP_ATTR);
        if (CollectionUtils.isNotEmpty(tagGroupAttrList)) {
            List<MtExtendAttrDTO3> attrList = new ArrayList<>();
            tagGroupAttrList.forEach(e -> {
                MtExtendAttrDTO3 dto3 = new MtExtendAttrDTO3();
                dto3.setAttrName(e.getAttrName());
                dto3.setAttrValue(e.getAttrValue());
                attrList.add(dto3);
            });
            mtExtendSettingsService.attrSave(tenantId, MT_TAG_GROUP_ATTR, curTagGroup.getTagGroupId(), null, attrList);
        }

        // record history
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("TAG_GROUP_UPDATE_COPY");
        String eventId = mtEventRepo.eventCreate(tenantId, mtEventCreateVO);
        mtTagGroupHisService.saveTagGroupHis(tenantId, eventId, curTagGroup);

        // save tag group assign
        MtTagGroupAssignDTO2 queryTagGroupAssignDTO = new MtTagGroupAssignDTO2();
        queryTagGroupAssignDTO.setTagGroupId(dto.getSourceTagGroupId());
        List<MtTagGroupAssignDTO> tagGroupAssignList =
                        mtTagGroupAssignService.queryTagGroupAssignForUi(tenantId, queryTagGroupAssignDTO);
        if (CollectionUtils.isNotEmpty(tagGroupAssignList)) {
            for (MtTagGroupAssignDTO assign : tagGroupAssignList) {
                assign.setTagGroupAssignId(null);
            }
        }
        mtTagGroupAssignService.saveTagGroupAssignForUi(tenantId, curTagGroup.getTagGroupId(), tagGroupAssignList);

        // save tag group object
        MtTagGroupObjectDTO2 tagGroupObjectDTO =
                        mtTagGroupObjectService.queryTagGroupObjectForUi(tenantId, srcTagGroup.getTagGroupId());
        if (null != tagGroupObjectDTO) {
            tagGroupObjectDTO.setTagGroupObjectId(null);
            mtTagGroupObjectService.saveTagGroupObjectForUi(tenantId, curTagGroup.getTagGroupId(), tagGroupObjectDTO);
        }

        return curTagGroup.getTagGroupId();
    }
}
