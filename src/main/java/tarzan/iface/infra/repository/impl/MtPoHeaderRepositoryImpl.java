package tarzan.iface.infra.repository.impl;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtPoHeader;
import tarzan.iface.domain.repository.MtPoHeaderRepository;
import org.springframework.stereotype.Component;
import tarzan.iface.infra.mapper.MtPoHeaderMapper;

/**
 * 采购订单头表 资源库实现
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
@Component
public class MtPoHeaderRepositoryImpl extends BaseRepositoryImpl<MtPoHeader> implements MtPoHeaderRepository {

    @Autowired
    private MtPoHeaderMapper mtPoHeaderMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void poHeaderAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_INTERFACE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INTERFACE_0001", "INTERFACE", "keyId", "【API:poHeaderAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPoHeader poHeader = new MtPoHeader();
        poHeader.setTenantId(tenantId);
        poHeader.setPoHeaderId(dto.getKeyId());
        poHeader = mtPoHeaderMapper.selectOne(poHeader);
        if (poHeader == null) {
            throw new MtException("MT_INTERFACE_0041",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0041",
                                            "INTERFACE", dto.getKeyId(), "mt_po_header",
                                            "【API:poHeaderAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_po_header_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
