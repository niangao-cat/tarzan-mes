package tarzan.order.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.order.domain.entity.MtEoRouterHis;
import tarzan.order.domain.repository.MtEoRouterHisRepository;
import tarzan.order.domain.vo.MtEoRouterHisVO;
import tarzan.order.infra.mapper.MtEoRouterHisMapper;

/**
 * EO工艺路线历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoRouterHisRepositoryImpl extends BaseRepositoryImpl<MtEoRouterHis> implements MtEoRouterHisRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRouterHisMapper mtEoRouterHisMapper;

    @Override
    public List<MtEoRouterHis> eoRouterHisQuery(Long tenantId, MtEoRouterHisVO condition) {
        if (StringUtils.isEmpty(condition.getEoRouterId()) && StringUtils.isEmpty(condition.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "【eoRouterId、eventId】", "【API:eoRouterHisQuery】"));
        }

        return this.mtEoRouterHisMapper.selectByConditionCustom(tenantId, condition);
    }
}
