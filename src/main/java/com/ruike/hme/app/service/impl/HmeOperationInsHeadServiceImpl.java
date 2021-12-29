package com.ruike.hme.app.service.impl;

import java.util.List;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeOperationInsHeadService;
import com.ruike.hme.domain.entity.HmeOperationInsHead;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import com.ruike.hme.domain.repository.HmeOperationInsHeadRepository;
import com.ruike.hme.domain.vo.HmeOperationInsHeadVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeOperationInsHeadMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 作业指导头表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
@Service
public class HmeOperationInsHeadServiceImpl implements HmeOperationInsHeadService {

    @Autowired
    private HmeOperationInsHeadRepository hmeOperationInsHeadRepository;

    @Autowired
    private HmeOperationInsHeadMapper hmeOperationInsHeadMapper;

    @Override
    public Page<HmeOperationInsHeadDTO2> listForUi(Long tenantId, HmeOperationInsHeadDTO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeOperationInsHeadRepository.operationInsHeadQuery(tenantId, dto));
    }

    @Override
    public HmeOperationInsHeadDTO2 detailListForUi(Long tenantId, HmeOperationInsHeadDTO dto) {
        return hmeOperationInsHeadRepository.detailListForUi(tenantId, dto);
    }

    @Override
    public HmeOperationInsHead saveForUi(Long tenantId, HmeOperationInsHead hmeOperationInsHead) {
        return hmeOperationInsHeadRepository.operationInsHeadUpdate(tenantId, hmeOperationInsHead);
    }

    @Override
    public Page<HmeOperationInsHeadVO> eSopQuery(Long tenantId, HmeOperationInsHeadDTO3 dto, PageRequest pageRequest) {
        // 如果筛选有值 按筛选查询 没值 按进站条码物料查询 找不到 则置空查询
        if (StringUtils.isNotBlank(dto.getMaterialId())) {
           return PageHelper.doPage(pageRequest, () -> hmeOperationInsHeadMapper.eSopQuery(tenantId, dto));
        }
        Page<HmeOperationInsHeadVO> sitePageObj = new Page<>();
        if (StringUtils.isNotBlank(dto.getSnMaterialId())) {
            dto.setMaterialId(dto.getSnMaterialId());
            sitePageObj = PageHelper.doPage(pageRequest, () -> hmeOperationInsHeadMapper.eSopQuery(tenantId, dto));
        }
        if (CollectionUtils.isEmpty(sitePageObj.getContent())) {
            dto.setMaterialId(null);
            sitePageObj = PageHelper.doPage(pageRequest, () -> hmeOperationInsHeadMapper.eSopQuery(tenantId, dto));
        }
        return sitePageObj;
    }

    @Override
    public Page<HmeEoJobSnDTO5> noSiteOutEoQuery(Long tenantId, HmeEoJobSnDTO4 dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeOperationInsHeadMapper.noSiteOutEoQuery(tenantId, dto));
    }
}
