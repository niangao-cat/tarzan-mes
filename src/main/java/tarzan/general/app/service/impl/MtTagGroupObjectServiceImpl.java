package tarzan.general.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tarzan.general.api.dto.MtTagGroupObjectDTO2;
import tarzan.general.app.service.MtTagGroupObjectHisService;
import tarzan.general.app.service.MtTagGroupObjectService;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.mapper.MtTagGroupObjectMapper;

/**
 * 数据收集组关联对象表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupObjectServiceImpl implements MtTagGroupObjectService {

    @Autowired
    private MtTagGroupObjectHisService mtTagGroupObjectHisService;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepo;

    @Autowired
    private MtTagGroupObjectMapper mtTagGroupObjectMapper;

    @Override
    public MtTagGroupObjectDTO2 queryTagGroupObjectForUi(Long tenantId, String tagGroupId) {
        return mtTagGroupObjectMapper.queryTagGroupObjectForUi(tenantId, tagGroupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTagGroupObjectForUi(Long tenantId, String tagGroupId, MtTagGroupObjectDTO2 dto) {
        MtTagGroupObject mtTagGroupObject = new MtTagGroupObject();
        BeanUtils.copyProperties(dto, mtTagGroupObject);
        mtTagGroupObject.setTenantId(tenantId);
        mtTagGroupObject.setTagGroupId(tagGroupId);

        if (StringUtils.isEmpty(dto.getTagGroupObjectId())) {
            mtTagGroupObjectRepo.insertSelective(mtTagGroupObject);
        } else {
            mtTagGroupObjectRepo.updateByPrimaryKeySelective(mtTagGroupObject);
        }

        // record history
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("TAG_GROUP_OBJECT");
        String eventId = mtEventRepo.eventCreate(tenantId, mtEventCreateVO);
        mtTagGroupObjectHisService.saveTagGroupObjectHis(tenantId, eventId, mtTagGroupObject);

        return mtTagGroupObject.getTagGroupObjectId();
    }
}
