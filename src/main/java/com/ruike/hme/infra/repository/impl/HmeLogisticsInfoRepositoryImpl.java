package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeLogisticsInfoDTO;
import com.ruike.hme.domain.entity.HmeLogisticsInfoHis;
import com.ruike.hme.domain.repository.HmeLogisticsInfoHisRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.util.List;

/**
 * 物流信息表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-31 11:06:23
 */
@Component
public class HmeLogisticsInfoRepositoryImpl extends BaseRepositoryImpl<HmeLogisticsInfo> implements HmeLogisticsInfoRepository {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeLogisticsInfoHisRepository hmeLogisticsInfoHisRepository;

    @Override
    public List<LovValueDTO> logisticsCompanyQuery(Long tenantId) {
        return lovAdapter.queryLovValue("HME.LOGISTICS", tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeLogisticsInfoDTO> confirmReceive(Long tenantId, List<HmeLogisticsInfoDTO> dtoList) {
        //调用API {numrangeGenerate}生成批次
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setSiteId(dtoList.get(0).getSiteId());
        mtNumrangeVO2.setObjectCode("HME.LOGISTIC_BATCH_NUM");
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        //生成事件ID
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("HME_LOGISTICS_SIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        for (HmeLogisticsInfoDTO dto:dtoList) {
            HmeLogisticsInfo hmeLogisticsInfo = new HmeLogisticsInfo();
            hmeLogisticsInfo.setTenantId(tenantId);
            hmeLogisticsInfo.setLogisticsCompany(dto.getValue());
            hmeLogisticsInfo.setSiteId(dto.getSiteId());
            hmeLogisticsInfo.setEnableFlag("Y");
            hmeLogisticsInfo.setBatchNumber(mtNumrangeVO5.getNumber());
            hmeLogisticsInfo.setLogisticsNumber(dto.getLogisticsNumber());
            hmeLogisticsInfo.setRemark(dto.getRemark());
            this.insertSelective(hmeLogisticsInfo);
            HmeLogisticsInfoHis hmeLogisticsInfoHis = new HmeLogisticsInfoHis();
            hmeLogisticsInfoHis.setEventId(eventId);
            BeanUtils.copyProperties(hmeLogisticsInfo, hmeLogisticsInfoHis);
            hmeLogisticsInfoHisRepository.insertSelective(hmeLogisticsInfoHis);
        }
        return dtoList;
    }
}
