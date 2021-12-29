package com.ruike.itf.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobFirstProcessService;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO2;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO3;
import com.ruike.itf.app.service.ItfFirstProcessIfaceService;
import com.ruike.itf.domain.repository.ItfFirstProcessIfaceRepository;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:06
 */
@Service
public class ItfFirstProcessIfaceServiceImpl implements ItfFirstProcessIfaceService {

    @Autowired
    private ItfFirstProcessIfaceRepository itfFirstProcessIfaceRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Override
    public ItfFirstProcessIfaceVO inSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO dto) {
        //必输性校验
        itfFirstProcessIfaceRepository.firstProcessInSiteVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return itfFirstProcessIfaceRepository.inSiteInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Override
    public ItfFirstProcessIfaceVO releaseInvoke(Long tenantId, ItfFirstProcessIfaceDTO2 dto) {
        //必输性校验
        itfFirstProcessIfaceRepository.firstProcessReleaseVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return itfFirstProcessIfaceRepository.releaseInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Override
    public ItfFirstProcessIfaceVO outSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO3 dto) {
        //必输性校验
        itfFirstProcessIfaceRepository.firstProcessSiteOutVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return itfFirstProcessIfaceRepository.outSiteInvoke(tenantId, dto, hmeEoJobSnVO4);
    }
}
