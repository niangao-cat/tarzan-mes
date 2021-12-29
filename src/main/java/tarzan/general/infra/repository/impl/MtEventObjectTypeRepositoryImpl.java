package tarzan.general.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.repository.MtEventObjectTypeRepository;
import tarzan.general.domain.vo.MtEventObjectTypeVO;
import tarzan.general.infra.mapper.MtEventObjectTypeMapper;

/**
 * 对象类型定义 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventObjectTypeRepositoryImpl extends BaseRepositoryImpl<MtEventObjectType>
                implements MtEventObjectTypeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventObjectTypeMapper mtEventObjectTypeMapper;

    @Override
    public MtEventObjectType objectTypeGet(Long tenantId, String objectTypeId) {
        if (StringUtils.isEmpty(objectTypeId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "objectTypeId", "【API：objectTypeGet】"));
        }
        MtEventObjectType mtEventObjectType = new MtEventObjectType();
        mtEventObjectType.setTenantId(tenantId);
        mtEventObjectType.setObjectTypeId(objectTypeId);
        return mtEventObjectTypeMapper.selectOne(mtEventObjectType);
    }

    @Override
    public List<String> propertyLimitObjectTypeQuery(Long tenantId, MtEventObjectTypeVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0002", "EVENT", "【API：propertyLimitObjectTypeQuery】"));
        }
        return mtEventObjectTypeMapper.propertyLimitObjectTypeQuery(tenantId, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eventObjectTypeBasicPropertyUpdate(Long tenantId, MtEventObjectType dto) {
        String objectTypeId = dto.getObjectTypeId();

        if (StringUtils.isNotEmpty(dto.getObjectTypeId())) {
            if (null != dto.getObjectTypeCode() && "".equals(dto.getObjectTypeCode())) {
                throw new MtException("MT_EVENT_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0001", "EVENT",
                                                "objectTypeCode", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0001", "EVENT", "enableFlag", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }

            MtEventObjectType mtEventObjectType = new MtEventObjectType();
            mtEventObjectType.setTenantId(tenantId);
            mtEventObjectType.setObjectTypeId(dto.getObjectTypeId());
            mtEventObjectType = this.mtEventObjectTypeMapper.selectOne(mtEventObjectType);
            if (null == mtEventObjectType) {
                throw new MtException("MT_EVENT_0008", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0008", "EVENT", "objectTypeId", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getObjectTypeCode())) {
                MtEventObjectType mtEventObjectType2 = new MtEventObjectType();
                mtEventObjectType2.setTenantId(tenantId);
                mtEventObjectType2.setObjectTypeCode(dto.getObjectTypeCode());
                mtEventObjectType2 = this.mtEventObjectTypeMapper.selectOne(mtEventObjectType2);
                if (null != mtEventObjectType2 && !mtEventObjectType2.getObjectTypeId().equals(dto.getObjectTypeId())) {
                    throw new MtException("MT_EVENT_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_EVENT_0015", "EVENT", "【API：eventObjectTypeBasicPropertyUpdate】"));
                }
            }

            self().updateByPrimaryKeySelective(dto);
        } else {
            if (StringUtils.isEmpty(dto.getObjectTypeCode())) {
                throw new MtException("MT_EVENT_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0001", "EVENT",
                                                "objectTypeCode", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0001", "EVENT", "enableFlag", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }

            MtEventObjectType mtEventObjectType2 = new MtEventObjectType();
            mtEventObjectType2.setTenantId(tenantId);
            mtEventObjectType2.setObjectTypeCode(dto.getObjectTypeCode());
            mtEventObjectType2 = this.mtEventObjectTypeMapper.selectOne(mtEventObjectType2);
            if (null != mtEventObjectType2) {
                throw new MtException("MT_EVENT_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0015", "EVENT", "【API：eventObjectTypeBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            objectTypeId = dto.getObjectTypeId();
        }

        return objectTypeId;
    }
}
