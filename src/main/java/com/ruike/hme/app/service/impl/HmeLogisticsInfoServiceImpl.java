package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeLogisticsInfoDTO;
import com.ruike.hme.app.service.HmeLogisticsInfoService;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
import com.ruike.hme.domain.vo.HmeLogisticsInfoVO;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物流信息表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-31 11:06:23
 */
@Service
public class HmeLogisticsInfoServiceImpl implements HmeLogisticsInfoService {

    @Autowired
    private HmeLogisticsInfoRepository hmeLogisticsInfoRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<LovValueDTO> logisticsCompanyQuery(Long tenantId) {
        return hmeLogisticsInfoRepository.logisticsCompanyQuery(tenantId);
    }

    @Override
    public HmeLogisticsInfoVO scanLogisticsNum(Long tenantId, String logisticsNumber) {
        //校验物流单号是否已存在表hme_logistics_Info中
        HmeLogisticsInfo hmeLogisticsInfo = hmeLogisticsInfoRepository.selectOne(new HmeLogisticsInfo() {{
            setTenantId(tenantId);
            setLogisticsNumber(logisticsNumber);
        }});
        if(hmeLogisticsInfo != null){
            //若存在，则报错 物流单号${1}已由${2}在${3}签收
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeLogisticsInfo.getCreatedBy());
            String creationDate = DateUtil.date2String(hmeLogisticsInfo.getCreationDate(), "yyyy-MM-dd HH:mm:ss");
            throw new MtException("HME_LOGISTICS_INFO_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0001", "HME", logisticsNumber, mtUserInfo.getRealName(), creationDate));
        }
        HmeLogisticsInfoVO result = new HmeLogisticsInfoVO();
        result.setLogisticsNumber(logisticsNumber);
        return result;
    }

    @Override
    public List<HmeLogisticsInfoDTO> confirmReceive(Long tenantId, List<HmeLogisticsInfoDTO> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
            throw new MtException("HME_LOGISTICS_INFO_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0003", "HME"));
        }
        for (HmeLogisticsInfoDTO dto:dtoList) {
            if(StringUtils.isEmpty(dto.getValue())){
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "物流公司"));
            }
            if(StringUtils.isEmpty(dto.getLogisticsNumber())){
                throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOGISTICS_INFO_0002", "HME", "物流单号"));
            }
        }
        return hmeLogisticsInfoRepository.confirmReceive(tenantId, dtoList);
    }
}
