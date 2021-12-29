package tarzan.general.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.repository.MtTagGroupHisRepository;
import tarzan.general.domain.vo.MtTagGroupHisVO;
import tarzan.general.infra.mapper.MtTagGroupHisMapper;

/**
 * 数据收集组历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Component
public class MtTagGroupHisRepositoryImpl extends BaseRepositoryImpl<MtTagGroupHis> implements MtTagGroupHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagGroupHisMapper mtTagGroupHisMapper;


    @Override
    public MtTagGroupHisVO tagGroupLatestHisGet(Long tenantId, String tagGroupId) {
        if (StringUtils.isEmpty(tagGroupId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupId", "【API:tagGroupLatestHisGet】"));
        }
        return mtTagGroupHisMapper.selectRecent(tenantId, tagGroupId);
    }
}
