package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO;
import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO2;
import com.ruike.hme.api.dto.HmeCenterKanbanLineDTO;
import com.ruike.hme.app.service.HmeCenterKanbanHeaderService;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;
import com.ruike.hme.domain.entity.HmeCenterKanbanLine;
import com.ruike.hme.domain.repository.HmeCenterKanbanHeaderRepository;
import com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO;
import com.ruike.hme.domain.vo.HmeCenterKanbanLineVO;
import com.ruike.hme.infra.mapper.HmeCenterKanbanHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 制造中心看板信息头表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
@Service
public class HmeCenterKanbanHeaderServiceImpl implements HmeCenterKanbanHeaderService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCenterKanbanHeaderRepository hmeCenterKanbanHeaderRepository;
    @Autowired
    private HmeCenterKanbanHeaderMapper hmeCenterKanbanHeaderMapper;

    @Override
    public HmeCenterKanbanHeader createOrUpdateHeader(Long tenantId, HmeCenterKanbanHeaderDTO dto) {
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getKanbanArea())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "看板区域"));
        }
        if(StringUtils.isBlank(dto.getEnableFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        if("1".equals(dto.getKanbanArea())){
            //当看板区域为1时，产线及物料必选
            if(StringUtils.isBlank(dto.getProdLineId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "产线"));
            }
            if(StringUtils.isBlank(dto.getMaterialId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "物料"));
            }
        }else if("2".equals(dto.getKanbanArea())){
            //看板区域=2时，产线及产品组必选
            if(StringUtils.isBlank(dto.getProdLineId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "产线"));
            }
            if(StringUtils.isBlank(dto.getProductionGroupId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "产品组"));
            }
        }else if("3".equals(dto.getKanbanArea())){
            //看板区域=3时，产线及不良展示工序必选
            if(StringUtils.isBlank(dto.getProdLineId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "产线"));
            }
            if(StringUtils.isBlank(dto.getWorkcellId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "工序"));
            }
        }else if("4".equals(dto.getKanbanArea())){
            //看板区域=4时，部门及物料必选
            if(StringUtils.isBlank(dto.getBusinessId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "部门"));
            }
            if(StringUtils.isBlank(dto.getMaterialId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "物料"));
            }
        }
        if(StringUtils.isBlank(dto.getProdLineId())){
            dto.setProdLineId("");
        }
        if(StringUtils.isBlank(dto.getBusinessId())){
            dto.setBusinessId("");
        }
        if(StringUtils.isBlank(dto.getMaterialId())){
            dto.setMaterialId("");
        }
        if(StringUtils.isBlank(dto.getProductionGroupId())){
            dto.setProductionGroupId("");
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            dto.setWorkcellId("");
        }
        return hmeCenterKanbanHeaderRepository.createOrUpdateHeader(tenantId, dto);
    }

    @Override
    public HmeCenterKanbanLine createOrUpdateLine(Long tenantId, HmeCenterKanbanLineDTO dto) {
        if(StringUtils.isBlank(dto.getCenterKanbanHeaderId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "头ID"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工序"));
        }
        if(Objects.isNull(dto.getThroughRate())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "直通率"));
        }
        if(StringUtils.isBlank(dto.getEnableFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        return hmeCenterKanbanHeaderRepository.createOrUpdateLine(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public Page<HmeCenterKanbanHeaderVO> headPageQuery(Long tenantId, HmeCenterKanbanHeaderDTO2 dto, PageRequest pageRequest) {
        Page<HmeCenterKanbanHeaderVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeCenterKanbanHeaderMapper.headPageQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCenterKanbanLineVO> linePageQuery(Long tenantId, String centerKanbanHeaderId, PageRequest pageRequest) {
        Page<HmeCenterKanbanLineVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeCenterKanbanHeaderMapper.linePageQuery(tenantId, centerKanbanHeaderId));
        return resultPage;
    }
}
