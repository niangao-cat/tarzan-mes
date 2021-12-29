package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.app.service.HmeNcCodeRouterRelService;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelHisRepository;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelRepository;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeNcCodeRouterRelMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

/**
 * 不良代码工艺路线关系表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@Service
public class HmeNcCodeRouterRelServiceImpl implements HmeNcCodeRouterRelService {

    @Autowired
    private HmeNcCodeRouterRelRepository hmeNcCodeRouterRelRepository;

    @Autowired
    private HmeNcCodeRouterRelMapper hmeNcCodeRouterRelMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeNcCodeRouterRelHisRepository hmeNcCodeRouterRelHisRepository;

    @Override
    public Page<HmeNcCodeRouterRelVO> ncCodeRouterRelList(Long tenantId, HmeNcCodeRouterRelDTO dto, PageRequest pageRequest) {
        return this.hmeNcCodeRouterRelRepository.ncCodeRouterRelList(tenantId,dto,pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNcCodeRouterRel ncCodeRouterRelSave(Long tenantId, HmeNcCodeRouterRel dto) {
        String eventTypeCode = Strings.EMPTY;
        if(StringUtils.isNotBlank(dto.getNcCodeRouterRelId())){
            //更新
            hmeNcCodeRouterRelMapper.updateByPrimaryKeySelective(dto);
            //事件编码
            eventTypeCode = HmeConstants.EventType.HME_NC_CODE_ROUTER_REL_INCREASE;
        }else{
            //新增
            dto.setTenantId(tenantId);
            hmeNcCodeRouterRelRepository.insertSelective(dto);
            //事件编码
            eventTypeCode = HmeConstants.EventType.HME_NC_CODE_ROUTER_REL_UPDATE;
        }

        //创建事件
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        //记录历史
        HmeNcCodeRouterRelHis hmeNcCodeRouterRelHis = new HmeNcCodeRouterRelHis();
        BeanUtils.copyProperties(dto,hmeNcCodeRouterRelHis);
        hmeNcCodeRouterRelHis.setEventId(eventId);
        hmeNcCodeRouterRelHisRepository.insertSelective(hmeNcCodeRouterRelHis);

        return dto;
    }
}
