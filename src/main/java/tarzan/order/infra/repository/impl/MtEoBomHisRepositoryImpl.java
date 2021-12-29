package tarzan.order.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.order.domain.entity.MtEoBomHis;
import tarzan.order.domain.repository.MtEoBomHisRepository;
import tarzan.order.domain.vo.MtEoBomHisVO;
import tarzan.order.infra.mapper.MtEoBomHisMapper;

/**
 * EO装配清单历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
public class MtEoBomHisRepositoryImpl extends BaseRepositoryImpl<MtEoBomHis> implements MtEoBomHisRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoBomHisMapper mtEoBomHisMapper;

    @Override
    public List<MtEoBomHis> eoBomHisQuery(Long tenantId, MtEoBomHisVO condition) {
        if (StringUtils.isEmpty(condition.getEoBomId()) && StringUtils.isEmpty(condition.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "【eoBomId、eventId】", "【API:eoBomHisQuery】"));
        }

        return this.mtEoBomHisMapper.selectByConditionCustom(tenantId, condition);
    }
}
