package tarzan.general.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.repository.MtEventRequestTypeRepository;
import tarzan.general.domain.vo.MtEventRequestTypeVO;
import tarzan.general.infra.mapper.MtEventRequestTypeMapper;

/**
 * 事件组类型定义 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventRequestTypeRepositoryImpl extends BaseRepositoryImpl<MtEventRequestType>
                implements MtEventRequestTypeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRequestTypeMapper mtEventRequestTypeMapper;

    @Override
    public List<String> propertyLimitEventGroupTypeQuery(Long tenantId, MtEventRequestTypeVO dto) {
        if (StringUtils.isEmpty(dto.getRequestTypeCode()) && StringUtils.isEmpty(dto.getDescription())
                        && StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0002", "EVENT", "【API：propertyLimitEventGroupTypeQuery】"));
        }

        return mtEventRequestTypeMapper.propertyLimitEventGroupTypeQuery(tenantId, dto);
    }

    @Override
    public MtEventRequestType eventGroupTypeGet(Long tenantId, String requestTypeId) {
        if (StringUtils.isEmpty(requestTypeId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "requestTypeId", "【API：eventGroupTypeGet】"));
        }
        MtEventRequestType mtEventRequestType = new MtEventRequestType();
        mtEventRequestType.setTenantId(tenantId);
        mtEventRequestType.setRequestTypeId(requestTypeId);
        return mtEventRequestTypeMapper.selectOne(mtEventRequestType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eventRequestTypeBasicPropertyUpdate(Long tenantId, MtEventRequestType dto) {
        String requestTypeId = dto.getRequestTypeId();

        if (StringUtils.isNotEmpty(dto.getRequestTypeId())) {
            if (null != dto.getRequestTypeCode() && "".equals(dto.getRequestTypeCode())) {
                throw new MtException("MT_EVENT_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0001", "EVENT",
                                                "requestTypeCode", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0001", "EVENT", "enableFlag", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }

            MtEventRequestType mtEventRequestType = new MtEventRequestType();
            mtEventRequestType.setTenantId(tenantId);
            mtEventRequestType.setRequestTypeId(dto.getRequestTypeId());
            mtEventRequestType = this.mtEventRequestTypeMapper.selectOne(mtEventRequestType);
            if (null == mtEventRequestType) {
                throw new MtException("MT_EVENT_0008",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0008", "EVENT",
                                                "requestTypeId", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getRequestTypeCode())) {
                MtEventRequestType mtEventRequestType2 = new MtEventRequestType();
                mtEventRequestType2.setTenantId(tenantId);
                mtEventRequestType2.setRequestTypeCode(dto.getRequestTypeCode());
                mtEventRequestType2 = this.mtEventRequestTypeMapper.selectOne(mtEventRequestType2);
                if (null != mtEventRequestType2
                                && !mtEventRequestType2.getRequestTypeId().equals(dto.getRequestTypeId())) {
                    throw new MtException("MT_EVENT_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_EVENT_0011", "EVENT", "【API：eventRequestTypeBasicPropertyUpdate】"));
                }
            }

            self().updateByPrimaryKeySelective(dto);
        } else {
            if (StringUtils.isEmpty(dto.getRequestTypeCode())) {
                throw new MtException("MT_EVENT_0001",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0001", "EVENT",
                                                "requestTypeCode", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0001", "EVENT", "enableFlag", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }

            MtEventRequestType mtEventRequestType2 = new MtEventRequestType();
            mtEventRequestType2.setTenantId(tenantId);
            mtEventRequestType2.setRequestTypeCode(dto.getRequestTypeCode());
            mtEventRequestType2 = this.mtEventRequestTypeMapper.selectOne(mtEventRequestType2);
            if (null != mtEventRequestType2) {
                throw new MtException("MT_EVENT_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0011", "EVENT", "【API：eventRequestTypeBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            requestTypeId = dto.getRequestTypeId();
        }

        return requestTypeId;
    }
}
