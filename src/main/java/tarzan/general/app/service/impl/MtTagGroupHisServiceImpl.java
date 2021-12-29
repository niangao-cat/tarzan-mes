package tarzan.general.app.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

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
import tarzan.general.api.dto.MtTagGroupHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.app.service.MtTagGroupHisService;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.repository.MtTagGroupHisRepository;
import tarzan.general.infra.mapper.MtTagGroupHisMapper;

/**
 * 数据收集组历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupHisServiceImpl implements MtTagGroupHisService {

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtTagGroupHisRepository mtTagGroupHisRepo;

    @Autowired
    private MtTagGroupHisMapper mtTagGroupHisMapper;

    @Override
    public Page<MtTagGroupHisDTO> queryTagGroupHisForUi(Long tenantId, MtTagGroupHisDTO2 dto, PageRequest pageRequest) {
        Page<MtTagGroupHisDTO> result =
                        PageHelper.doPage(pageRequest, () -> mtTagGroupHisMapper.queryTagGroupHisForUi(tenantId, dto));

        // user info
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId,
                        result.getContent().stream().map(MtTagGroupHisDTO::getEventBy).collect(Collectors.toList()));

        if (MapUtils.isNotEmpty(userInfoMap)) {
            for (MtTagGroupHisDTO his : result) {
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(his.getEventBy())) {
                    his.setEventUserName(userInfoMap.get(his.getEventBy()).getLoginName());
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTagGroupHis(Long tenantId, String eventId, MtTagGroup mtTagGroup) {
        MtTagGroupHis mtTagGroupHis = new MtTagGroupHis();

        BeanUtils.copyProperties(mtTagGroup, mtTagGroupHis);

        mtTagGroupHis.setEventId(eventId);
        mtTagGroupHis.setTenantId(tenantId);

        mtTagGroupHisRepo.insertSelective(mtTagGroupHis);
        return mtTagGroupHis.getTagGroupHisId();
    }
}
