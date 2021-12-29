package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO;
import com.ruike.hme.domain.entity.HmeNcDowngradeHis;
import com.ruike.hme.domain.repository.HmeNcDowngradeHisRepository;
import com.ruike.hme.infra.mapper.HmeNcDowngradeMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeNcDowngrade;
import com.ruike.hme.domain.repository.HmeNcDowngradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;

import java.util.Objects;

/**
 * 产品降级关系维护 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
@Component
public class HmeNcDowngradeRepositoryImpl extends BaseRepositoryImpl<HmeNcDowngrade> implements HmeNcDowngradeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private HmeNcDowngradeMapper hmeNcDowngradeMapper;

    @Autowired
    private HmeNcDowngradeHisRepository hmeNcDowngradeHisRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNcDowngrade createOrUpdate(Long tenantId, HmeNcDowngradeDTO dto) {
        HmeNcDowngrade hmeNcDowngrade = this.selectOne(new HmeNcDowngrade() {{
            setMaterialId(dto.getMaterialId());
            setNcCodeId(dto.getNcCodeId());
            setTransitionMaterialId(dto.getTransitionMaterialId());
            setTenantId(tenantId);
        }});
        if(StringUtils.isEmpty(dto.getDowngradeId())){
            //新增时，进行唯一性校验 产品编码+不良代码+降级物料编码不允许重复
            if(Objects.nonNull(hmeNcDowngrade)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
                MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(dto.getNcCodeId());
                MtMaterial transitionMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getTransitionMaterialId());
                throw new MtException("HME_NC_DOWNGRADE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_DOWNGRADE_001", "HME", mtMaterial.getMaterialCode(),
                        mtNcCode.getNcCode(), transitionMaterial.getMaterialCode()));
            }
            hmeNcDowngrade = new HmeNcDowngrade();
            BeanCopierUtil.copy(dto, hmeNcDowngrade);
            this.insertSelective(hmeNcDowngrade);
            //记录历史
            HmeNcDowngradeHis hmeNcDowngradeHis = new HmeNcDowngradeHis();
            BeanCopierUtil.copy(hmeNcDowngrade, hmeNcDowngradeHis);
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("RETEST_WORK_ORDER_INPUT");
            }});
            hmeNcDowngradeHis.setEventId(eventId);
            hmeNcDowngradeHisRepository.insertSelective(hmeNcDowngradeHis);
        }else{
            //更新时，进行唯一性校验 产品编码+不良代码+降级物料编码不允许重复
            if(Objects.nonNull(hmeNcDowngrade) && !hmeNcDowngrade.getDowngradeId().equals(dto.getDowngradeId())){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
                MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(dto.getNcCodeId());
                MtMaterial transitionMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getTransitionMaterialId());
                throw new MtException("HME_NC_DOWNGRADE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_DOWNGRADE_001", "HME", mtMaterial.getMaterialCode(),
                        mtNcCode.getNcCode(), transitionMaterial.getMaterialCode()));
            }
            hmeNcDowngrade = new HmeNcDowngrade();
            BeanCopierUtil.copy(dto, hmeNcDowngrade);
            hmeNcDowngradeMapper.updateByPrimaryKeySelective(hmeNcDowngrade);
            //记录历史
            HmeNcDowngradeHis hmeNcDowngradeHis = new HmeNcDowngradeHis();
            BeanCopierUtil.copy(hmeNcDowngrade, hmeNcDowngradeHis);
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("RETEST_WORK_ORDER_INPUT");
            }});
            hmeNcDowngradeHis.setEventId(eventId);
            hmeNcDowngradeHisRepository.insertSelective(hmeNcDowngradeHis);
        }
        return hmeNcDowngrade;
    }
}
