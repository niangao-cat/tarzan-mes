package com.ruike.hme.app.service.impl;

import com.ruike.hme.domain.vo.HmeModLocatorVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruike.hme.app.service.HmeEoJobSnService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.HmePrepareMaterialVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO;
import tarzan.modeling.domain.entity.MtModLocator;
import java.util.stream.Collectors;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

import java.util.*;

/**
 * 工序作业平台-SN作业应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
@Service
public class HmeEoJobSnServiceImpl extends BaseServiceImpl<HmeEoJobSn> implements HmeEoJobSnService {

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Override
    public Page<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWorkOrderVO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnRepository.workOrderQuery(tenantId, dto));
    }

    @Override
    public Page<HmePrepareMaterialVO> materialQuery(Long tenantId, HmeWorkOrderVO dto, PageRequest pageRequest) {
        //如果工艺ID列表不为空，转成List add by yuchao.wang for fang.pan at 2020.9.12
        if(StringUtils.isNotBlank(dto.getOperationIdListStr())){
            dto.setOperationIdList(Arrays.asList(dto.getOperationIdListStr().split(",")));
        }

        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnRepository.materialQuery(tenantId, dto));
    }

    @Override
    public Page<HmeModLocatorVO> locatorLovQuery(Long tenantId, HmeModLocatorVO dto, PageRequest pageRequest) {
        List<String> workcellLocatorTypeList = new ArrayList<String>();
        List<String> prodLineLocatorTypeList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(dto.getLocatorTypeList())) {
            workcellLocatorTypeList = dto.getLocatorTypeList().stream().filter(material -> HmeConstants.LocaltorType.DEFAULT_STORAGE.equals(material)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(workcellLocatorTypeList)) {
                dto.setWorkcellLocatorTypeList(workcellLocatorTypeList);
            }
            prodLineLocatorTypeList = dto.getLocatorTypeList().stream().filter(locatorType -> HmeConstants.LocaltorType.BACKFLUSH.equals(locatorType) || HmeConstants.LocaltorType.PRE_LOAD.equals(locatorType)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(prodLineLocatorTypeList)) {
                dto.setProdLineLocatorTypeList(prodLineLocatorTypeList);
            }
        }else{
            workcellLocatorTypeList.add(HmeConstants.LocaltorType.DEFAULT_STORAGE);
            dto.setWorkcellLocatorTypeList(workcellLocatorTypeList);
            prodLineLocatorTypeList.add(HmeConstants.LocaltorType.BACKFLUSH);
            prodLineLocatorTypeList.add(HmeConstants.LocaltorType.PRE_LOAD);
            dto.setProdLineLocatorTypeList(prodLineLocatorTypeList);
        }
        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnMapper.queryLocator(tenantId,dto));
    }
}
