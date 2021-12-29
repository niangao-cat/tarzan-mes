package tarzan.general.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import tarzan.general.api.dto.MtTagGroupAssignHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.app.service.MtTagGroupAssignHisService;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.entity.MtTagGroupAssignHis;
import tarzan.general.domain.repository.MtTagGroupAssignHisRepository;
import tarzan.general.infra.mapper.MtTagGroupAssignHisMapper;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

/**
 * 数据收集项分配收集组历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupAssignHisServiceImpl implements MtTagGroupAssignHisService {

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtTagGroupAssignHisRepository mtTagGroupAssignHisRepo;

    @Autowired
    private MtUomRepository mtUomRepo;

    @Autowired
    private MtTagGroupAssignHisMapper mtTagGroupAssignHisMapper;

    @Override
    public Page<MtTagGroupAssignHisDTO> queryTagGroupAssignHisForUi(Long tenantId, MtTagGroupHisDTO2 dto,
                                                                    PageRequest pageRequest) {
        Page<MtTagGroupAssignHisDTO> result = PageHelper.doPage(pageRequest,
                        () -> mtTagGroupAssignHisMapper.queryTagGroupAssignHisForUi(tenantId, dto));

        List<MtUomVO> uomList = mtUomRepo.uomPropertyBatchGet(tenantId,
                        result.getContent().stream().map(MtTagGroupAssignHisDTO::getUnit).collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(uomList)) {
            for (MtTagGroupAssignHisDTO tagGroupAssignHisDTO : result.getContent()) {
                Optional<MtUomVO> optional = uomList.stream()
                                .filter(u -> u.getUomId().equals(tagGroupAssignHisDTO.getUnit())).findAny();
                optional.ifPresent(u -> {
                    tagGroupAssignHisDTO.setUomCode(u.getUomCode());
                    tagGroupAssignHisDTO.setUomDesc(u.getUomName());
                });
            }
        }

        // user info
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, result.getContent().stream()
                        .map(MtTagGroupAssignHisDTO::getEventBy).collect(Collectors.toList()));

        if (MapUtils.isNotEmpty(userInfoMap)) {
            for (MtTagGroupAssignHisDTO his : result) {
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(his.getEventBy())) {
                    his.setEventUserName(userInfoMap.get(his.getEventBy()).getLoginName());
                }
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagGroupAssignHis(Long tenantId, String eventId, MtTagGroupAssign mtTagGroupAssign) {
        MtTagGroupAssignHis mtTagGroupAssignHis = new MtTagGroupAssignHis();

        BeanUtils.copyProperties(mtTagGroupAssign, mtTagGroupAssignHis);

        mtTagGroupAssignHis.setEventId(eventId);
        mtTagGroupAssignHis.setTenantId(tenantId);

        mtTagGroupAssignHisRepo.insertSelective(mtTagGroupAssignHis);
    }
}
