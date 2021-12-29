package tarzan.dispatch.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.dispatch.domain.entity.MtEoDispatchHistory;
import tarzan.dispatch.domain.repository.MtEoDispatchHistoryRepository;
import tarzan.dispatch.domain.vo.MtEoDispatchHistoryVO1;
import tarzan.dispatch.infra.mapper.MtEoDispatchHistoryMapper;

/**
 * 调度历史表，记录历史发布的调度结果和版本 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
@Component
public class MtEoDispatchHistoryRepositoryImpl extends BaseRepositoryImpl<MtEoDispatchHistory>
                implements MtEoDispatchHistoryRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoDispatchHistoryMapper mtEoDispatchHistoryMapper;

    @Override
    public List<MtEoDispatchHistory> wkcLimitDispatchedHisQuery(Long tenantId, MtEoDispatchHistoryVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "【API：wkcLimitDispatchedHisQuery】"));
        }

        return mtEoDispatchHistoryMapper.selectEoDispatchHistoryByWkc(tenantId, dto);
    }
}
