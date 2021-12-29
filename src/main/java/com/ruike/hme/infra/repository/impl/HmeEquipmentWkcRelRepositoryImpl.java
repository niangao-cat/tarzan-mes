package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEquipmentWkcRelDTO;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRelHis;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelHisRepository;
import com.ruike.hme.infra.mapper.HmeEquipmentWkcRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.util.List;

/**
 * 设备工位关系表 资源库实现
 *
 * @author han.zhang03@hand-china.com 2020-06-09 11:32:08
 */
@Component
public class HmeEquipmentWkcRelRepositoryImpl extends BaseRepositoryImpl<HmeEquipmentWkcRel> implements HmeEquipmentWkcRelRepository {
    @Autowired
    private HmeEquipmentWkcRelMapper equipmentWkcRelMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEquipmentWkcRelHisRepository hmeEquipmentWkcRelHisRepository;

    @Override
    public Page<HmeEquipmentWkcRel> queryBaseData(Long tenantId, PageRequest pageRequest, HmeEquipmentWkcRelDTO hmeEquipmentWkcRel) {
        Page<HmeEquipmentWkcRel> hmeEquipmentWkcRels = PageHelper.doPage(pageRequest, () -> equipmentWkcRelMapper.queryBaseData(tenantId, hmeEquipmentWkcRel));
        return hmeEquipmentWkcRels;
    }

    @Override
    public List<HmeEquipmentWkcRel> update(Long tenantId, List<HmeEquipmentWkcRel> hmeEquipmentWkcRels) {
        hmeEquipmentWkcRels.forEach(hmeEquipmentWkcRel -> {
            HmeEquipmentWkcRel rel = new HmeEquipmentWkcRel();
            if(StringUtils.isEmpty(hmeEquipmentWkcRel.getEquipmentWkcRelId())){
                //主键id为空是新增
                rel.setEquipmentId(hmeEquipmentWkcRel.getEquipmentId());
//                rel.setProdLineId(hmeEquipmentWkcRel.getProdLineId());
//                rel.setStationId(hmeEquipmentWkcRel.getStationId());
                rel.setEnableFlag(hmeEquipmentWkcRel.getEnableFlag());
                List<HmeEquipmentWkcRel> rels = equipmentWkcRelMapper.select(rel);
                if(CollectionUtils.isNotEmpty(rels)){
                    throw new MtException("MT_GENERAL_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0006",
                                    "GENERAL"));
                }

                this.insertSelective(hmeEquipmentWkcRel);
            }else{
                //不为空则更新
                rel.setEquipmentId(hmeEquipmentWkcRel.getEquipmentId());
//                rel.setProdLineId(hmeEquipmentWkcRel.getProdLineId());
//                rel.setStationId(hmeEquipmentWkcRel.getStationId());
                rel.setEnableFlag(hmeEquipmentWkcRel.getEnableFlag());
                List<HmeEquipmentWkcRel> rels = equipmentWkcRelMapper.select(rel);
                if (CollectionUtils.isNotEmpty(rels) && !rels.get(0).getEquipmentWkcRelId().equals(hmeEquipmentWkcRel.getEquipmentWkcRelId())) {
                    throw new MtException("MT_GENERAL_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0006",
                                    "GENERAL"));
                }
                this.equipmentWkcRelMapper.updateByPrimaryKeySelective(hmeEquipmentWkcRel);
                // 记录历史
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("HME_EQUIPMENT_WKC_MODIFIED");
                }});
                HmeEquipmentWkcRelHis wkcRelHis = new HmeEquipmentWkcRelHis();
                BeanUtils.copyProperties(hmeEquipmentWkcRel, wkcRelHis);
                wkcRelHis.setEventId(eventId);
                hmeEquipmentWkcRelHisRepository.insertSelective(wkcRelHis);
            }
        });
        return hmeEquipmentWkcRels;
    }
}
