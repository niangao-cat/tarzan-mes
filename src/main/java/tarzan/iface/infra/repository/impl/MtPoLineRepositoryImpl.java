package tarzan.iface.infra.repository.impl;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.entity.MtPoLine;
import tarzan.iface.domain.repository.MtPoLineRepository;
import org.springframework.stereotype.Component;
import tarzan.iface.infra.mapper.MtPoLineMapper;

/**
 * 采购订单计划发运行 资源库实现
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
@Component
public class MtPoLineRepositoryImpl extends BaseRepositoryImpl<MtPoLine> implements MtPoLineRepository {

    @Autowired
    private MtPoLineMapper mtPoLineMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;


    @Override
    public void poLineAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_INTERFACE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INTERFACE_0001", "INTERFACE", "keyId", "【API:poLineAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPoLine poLine = new MtPoLine();
        poLine.setTenantId(tenantId);
        poLine.setPoLineId(dto.getKeyId());
        poLine = mtPoLineMapper.selectOne(poLine);
        if (poLine == null) {
            throw new MtException("MT_INTERFACE_0041",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0041",
                                            "INTERFACE", dto.getKeyId(), "mt_po_line",
                                            "【API:poLineAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_po_line_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
