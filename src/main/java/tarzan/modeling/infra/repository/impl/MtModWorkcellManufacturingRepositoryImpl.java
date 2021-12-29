package tarzan.modeling.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.MtModWorkcellManufacturing;
import tarzan.modeling.domain.repository.MtModWorkcellManufacturingRepository;
import tarzan.modeling.infra.mapper.MtModWorkcellManufacturingMapper;

/**
 * 工作单元生产属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModWorkcellManufacturingRepositoryImpl extends BaseRepositoryImpl<MtModWorkcellManufacturing>
                implements MtModWorkcellManufacturingRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModWorkcellManufacturingMapper mtModWorkcellManufacturingMapper;

    @Override
    public List<MtModWorkcellManufacturing> workcellManufacturingPropertyBatchGet(Long tenantId,
                    List<String> workcellId) {

        if (CollectionUtils.isEmpty(workcellId)) {
            throw new MtException("MT_MODELING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                                            "workcellId", "【API:workcellManufacturingPropertyBatchGet】"));
        }
        return mtModWorkcellManufacturingMapper.selectByIdsCustom(tenantId, workcellId);
    }

    @Override
    public MtModWorkcellManufacturing workcellManufacturingPropertyGet(Long tenantId, String workcellId) {

        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellManufacturingPropertyGet】"));
        }

        MtModWorkcellManufacturing tmp = new MtModWorkcellManufacturing();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(workcellId);
        return mtModWorkcellManufacturingMapper.selectOne(tmp);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void workcellManufacturingPropertyUpdate(Long tenantId, MtModWorkcellManufacturing dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellManufacturingPropertyUpdate】"));
        }

        MtModWorkcellManufacturing mtModWorkcellManufacturing = new MtModWorkcellManufacturing();
        mtModWorkcellManufacturing.setTenantId(tenantId);
        mtModWorkcellManufacturing.setWorkcellId(dto.getWorkcellId());
        mtModWorkcellManufacturing = this.mtModWorkcellManufacturingMapper.selectOne(mtModWorkcellManufacturing);
        if (null == mtModWorkcellManufacturing) {
            dto.setTenantId(tenantId);
            self().insertSelective(dto);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtModWorkcellManufacturing.setForwardShiftNumber(dto.getForwardShiftNumber());
                mtModWorkcellManufacturing.setBackwardShiftNumber(dto.getBackwardShiftNumber());
                mtModWorkcellManufacturing.setTenantId(tenantId);
                self().updateByPrimaryKey(mtModWorkcellManufacturing);
            } else {
                if (null != dto.getForwardShiftNumber()) {
                    mtModWorkcellManufacturing.setForwardShiftNumber(dto.getForwardShiftNumber());
                }
                if (null != dto.getBackwardShiftNumber()) {
                    mtModWorkcellManufacturing.setBackwardShiftNumber(dto.getBackwardShiftNumber());
                }
                mtModWorkcellManufacturing.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(mtModWorkcellManufacturing);
            }
        }
    }


}
