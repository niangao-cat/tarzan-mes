package tarzan.general.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.repository.MtEventObjectColumnRepository;
import tarzan.general.domain.vo.MtEventObjectColumnVO;
import tarzan.general.infra.mapper.MtEventObjectColumnMapper;

/**
 * 对象列定义 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventObjectColumnRepositoryImpl extends BaseRepositoryImpl<MtEventObjectColumn>
                implements MtEventObjectColumnRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventObjectColumnMapper mtEventObjectColumnMapper;

    @Override
    public List<String> propertyLimitObjectColumnQuery(Long tenantId, MtEventObjectColumnVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0002", "EVENT", "【API：propertyLimitObjectColumnQuery】"));
        }

        return mtEventObjectColumnMapper.propertyLimitObjectColumnQuery(tenantId, dto);
    }

    @Override
    public MtEventObjectColumn objectColumnGet(Long tenantId, String objectColumnId) {
        if (StringUtils.isEmpty(objectColumnId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "【API：objectColumnGet】"));
        }

        MtEventObjectColumn mtEventObjectColumn = new MtEventObjectColumn();
        mtEventObjectColumn.setTenantId(tenantId);
        mtEventObjectColumn.setObjectColumnId(objectColumnId);
        return mtEventObjectColumnMapper.selectOne(mtEventObjectColumn);
    }
}
