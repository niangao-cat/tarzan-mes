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
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.api.dto.MtTagGroupObjectHisDTO;
import tarzan.general.app.service.MtTagGroupObjectHisService;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.entity.MtTagGroupObjectHis;
import tarzan.general.domain.repository.MtTagGroupObjectHisRepository;
import tarzan.general.infra.mapper.MtTagGroupObjectHisMapper;

/**
 * 数据收集组关联对象历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagGroupObjectHisServiceImpl implements MtTagGroupObjectHisService {

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtTagGroupObjectHisRepository mtTagGroupObjectHisRepo;

    @Autowired
    private MtTagGroupObjectHisMapper mtTagGroupObjectHisMapper;

    @Override
    public Page<MtTagGroupObjectHisDTO> queryTagGroupObjectHisForUi(Long tenantId, MtTagGroupHisDTO2 dto,
                                                                    PageRequest pageRequest) {
        Page<MtTagGroupObjectHisDTO> result = PageHelper.doPage(pageRequest,
                        () -> mtTagGroupObjectHisMapper.queryTagGroupObjectHisForUi(tenantId, dto));
        // user info
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, result.getContent().stream()
                        .map(MtTagGroupObjectHisDTO::getEventBy).collect(Collectors.toList()));

        if (MapUtils.isNotEmpty(userInfoMap)) {
            for (MtTagGroupObjectHisDTO his : result) {
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(his.getEventBy())) {
                    his.setEventUserName(userInfoMap.get(his.getEventBy()).getLoginName());
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagGroupObjectHis(Long tenantId, String eventId, MtTagGroupObject mtTagGroupObject) {
        MtTagGroupObjectHis mtTagGroupObjectHis = new MtTagGroupObjectHis();

        BeanUtils.copyProperties(mtTagGroupObject, mtTagGroupObjectHis);

        mtTagGroupObjectHis.setTenantId(tenantId);
        mtTagGroupObjectHis.setEventId(eventId);

        mtTagGroupObjectHisRepo.insertSelective(mtTagGroupObjectHis);
    }
}
