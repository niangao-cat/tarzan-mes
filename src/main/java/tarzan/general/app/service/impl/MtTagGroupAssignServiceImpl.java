package tarzan.general.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.choerodon.mybatis.pagehelper.domain.Sort;
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
import tarzan.general.api.dto.MtTagGroupAssignDTO;
import tarzan.general.api.dto.MtTagGroupAssignDTO2;
import tarzan.general.app.service.MtTagGroupAssignHisService;
import tarzan.general.app.service.MtTagGroupAssignService;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.mapper.MtTagGroupAssignMapper;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

/**
 * 数据收集项分配收集组表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupAssignServiceImpl implements MtTagGroupAssignService {

    private static final String SERIALNUMBER = "serialNumber";

    @Autowired
    private MtTagGroupAssignHisService mtTagGroupAssignHisService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepo;

    @Autowired
    private MtUomRepository mtUomRepo;

    @Autowired
    private MtTagGroupAssignMapper mtTagGroupAssignMapper;

    @Override
    public Page<MtTagGroupAssignDTO> queryTagGroupAssignForUi(Long tenantId, MtTagGroupAssignDTO2 dto,
                                                              PageRequest pageRequest) {
        pageRequest.setSort(new Sort(Sort.Direction.ASC, SERIALNUMBER));
        Page<MtTagGroupAssignDTO> page = PageHelper.doPageAndSort(pageRequest,
                        () -> mtTagGroupAssignMapper.queryTagGroupAssignForUi(tenantId, dto));
        List<MtUomVO> uomList = mtUomRepo.uomPropertyBatchGet(tenantId,
                        page.getContent().stream().map(MtTagGroupAssignDTO::getUnit).collect(Collectors.toList()));

        for (MtTagGroupAssignDTO tagGroupAssignDTO : page.getContent()) {
            Optional<MtUomVO> optional =
                            uomList.stream().filter(u -> u.getUomId().equals(tagGroupAssignDTO.getUnit())).findAny();
            optional.ifPresent(u -> {
                tagGroupAssignDTO.setUomCode(u.getUomCode());
                tagGroupAssignDTO.setUomDesc(u.getUomName());
            });
        }
        return page;
    }

    @Override
    public List<MtTagGroupAssignDTO> queryTagGroupAssignForUi(Long tenantId, MtTagGroupAssignDTO2 dto) {
        return mtTagGroupAssignMapper.queryTagGroupAssignForUi(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagGroupAssignForUi(Long tenantId, String tagGroupId, List<MtTagGroupAssignDTO> dtoList) {
        // record history
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("TAG_GROUP_ASSIGN");
        String eventId = mtEventRepo.eventCreate(tenantId, mtEventCreateVO);

        for (MtTagGroupAssignDTO dto : dtoList) {
            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
            BeanUtils.copyProperties(dto, mtTagGroupAssign);
            mtTagGroupAssign.setTenantId(tenantId);
            mtTagGroupAssign.setTagGroupId(tagGroupId);

            MtTagGroupAssign queryTagGroupAssign = new MtTagGroupAssign();
            queryTagGroupAssign.setTenantId(tenantId);
            queryTagGroupAssign.setTagGroupId(tagGroupId);
            List<MtTagGroupAssign> originTagGroupAssignList = mtTagGroupAssignRepo.select(queryTagGroupAssign);

            if (StringUtils.isEmpty(dto.getTagGroupAssignId())) {
                if (originTagGroupAssignList.stream().anyMatch(a -> a.getTagId().equals(dto.getTagId()))) {
                    throw new MtException("MT_GENERAL_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0012", "GENERAL", "数据收集组, 数据收集项"));
                }
                if (originTagGroupAssignList.stream()
                                .anyMatch(a -> a.getSerialNumber().equals(dto.getSerialNumber()))) {
                    throw new MtException("MT_GENERAL_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0012", "GENERAL", "数据收集组, 序号"));
                }
                mtTagGroupAssignRepo.insertSelective(mtTagGroupAssign);
            } else {
                if (originTagGroupAssignList.stream().anyMatch(a -> a.getTagId().equals(dto.getTagId())
                                && !a.getTagGroupAssignId().equals(dto.getTagGroupAssignId()))) {
                    throw new MtException("MT_GENERAL_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0012", "GENERAL", "数据收集组, 数据收集项"));
                }
                if (originTagGroupAssignList.stream().anyMatch(a -> a.getSerialNumber().equals(dto.getSerialNumber())
                                && !a.getTagGroupAssignId().equals(dto.getTagGroupAssignId()))) {
                    throw new MtException("MT_GENERAL_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0012", "GENERAL", "数据收集组, 序号"));
                }
                mtTagGroupAssignRepo.updateByPrimaryKey(mtTagGroupAssign);
            }
            mtTagGroupAssignHisService.saveTagGroupAssignHis(tenantId, eventId, mtTagGroupAssign);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTagGroupAssignForUi(Long tenantId, String tagGroupAssignId) {
        if (StringUtils.isEmpty(tagGroupAssignId)) {
            return;
        }

        MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
        mtTagGroupAssign.setTenantId(tenantId);
        mtTagGroupAssign.setTagGroupAssignId(tagGroupAssignId);

        if (mtTagGroupAssignRepo.delete(mtTagGroupAssign) == 0) {
            throw new MtException("数据删除失败.");
        }
    }
}
