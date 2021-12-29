package tarzan.general.app.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagDTO1;
import tarzan.general.api.dto.MtTagDTO2;
import tarzan.general.app.service.MtTagHisService;
import tarzan.general.app.service.MtTagService;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

/**
 * 数据收集项表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagServiceImpl implements MtTagService {

    @Autowired
    private MtTagMapper mtTagMapper;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtTagRepository mtTagRepo;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtTagHisService mtTagHisService;


    @Override
    public Page<MtTagDTO1> tagQueryForUi(Long tenantId, MtTagDTO mtTagDTO, PageRequest pageRequest) {
        Page<MtTagDTO1> page =
                        PageHelper.doPage(pageRequest, () -> mtTagMapper.queryTagWithTagApiForUi(tenantId, mtTagDTO));
        if (CollectionUtils.isNotEmpty(page)) {
            for (MtTagDTO1 k : page) {
                if (StringUtils.isNotEmpty(k.getUnit())) {
                    MtUomVO uom = mtUomRepository.uomPropertyGet(tenantId, k.getUnit());
                    if (uom != null) {
                        k.setUomCode(uom.getUomCode());
                    }
                }
            }
        }
        return page;
    }

    @Override
    public MtTagDTO1 queryTagDetailForUi(Long tenantId, String tagId) {
        MtTagDTO mtTagDTO = new MtTagDTO();
        mtTagDTO.setTagId(tagId);
        List<MtTagDTO1> mtTagDTO1s = mtTagMapper.queryTagWithTagApiForUi(tenantId, mtTagDTO);
        if (CollectionUtils.isEmpty(mtTagDTO1s)) {
            return null;
        }
        MtTagDTO1 result = mtTagDTO1s.get(0);
        if (StringUtils.isNotEmpty(result.getUnit())) {
            MtUomVO uom = mtUomRepository.uomPropertyGet(tenantId, result.getUnit());
            if (uom != null) {
                result.setUomCode(uom.getUomCode());
                result.setUomDesc(uom.getUomName());
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String tagSaveForUi(Long tenantId, MtTagDTO mtTagDTO) {
        MtTag mtTag = new MtTag();
        BeanUtils.copyProperties(mtTagDTO, mtTag);
        mtTag.setTenantId(tenantId);
        mtTag.set_tls(mtTagDTO.get_tls());

        MtTag queryTag = new MtTag();
        queryTag.setTenantId(tenantId);
        queryTag.setTagCode(mtTagDTO.getTagCode());
        queryTag = mtTagRepo.selectOne(queryTag);
        if (null != queryTag && (StringUtils.isEmpty(mtTagDTO.getTagId())
                        || !queryTag.getTagId().equals(mtTagDTO.getTagId()))) {
            throw new MtException("MT_GENERAL_0006",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_GENERAL_0006", "GENERAL"));
        }
        if (StringUtils.isEmpty(mtTagDTO.getTagId())) {
            mtTagRepo.insertSelective(mtTag);

        } else {
            mtTagRepo.updateByPrimaryKey(mtTag);
        }

        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("TAG_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, event);

        mtTagHisService.saveTagHistory(tenantId, mtTag, eventId);
        return mtTag.getTagId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String tagCopyForUi(Long tenantId, MtTagDTO2 dto) {
        if (StringUtils.isEmpty(dto.getSourceTagId())) {
            return null;
        }

        // 校验数据是否存在
        MtTag queryTag = new MtTag();
        queryTag.setTenantId(tenantId);
        queryTag.setTagCode(dto.getTagCode());
        queryTag = mtTagRepo.selectOne(queryTag);
        if (null != queryTag) {
            throw new MtException("MT_GENERAL_0006",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_GENERAL_0006", "GENERAL"));
        }
        // source tag
        MtTag sourceTag = mtTagRepo.selectByPrimaryKey(dto.getSourceTagId());

        // save new tag
        MtTag newTag = new MtTag();
        BeanUtils.copyProperties(sourceTag, newTag);
        newTag.setTagId(null);
        newTag.setTagCode(dto.getTagCode());
        newTag.setTagDescription(dto.getTagDescription());
        newTag.set_tls(dto.get_tls());
        mtTagRepo.insertSelective(newTag);

        // save tag his
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("TAG_UPDATE_COPY");
        String eventId = mtEventRepository.eventCreate(tenantId, event);
        mtTagHisService.saveTagHistory(tenantId, newTag, eventId);
        return newTag.getTagId();
    }
}
