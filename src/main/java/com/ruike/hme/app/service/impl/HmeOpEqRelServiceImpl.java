package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeOpEqRelDTO;
import com.ruike.hme.api.dto.HmeOpEqRelDTO2;
import com.ruike.hme.app.service.HmeOpEqRelService;
import com.ruike.hme.domain.entity.HmeOpEqRel;
import com.ruike.hme.domain.repository.HmeOpEqRelRepository;
import com.ruike.hme.infra.mapper.HmeOpEqRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovDTO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.lov.dto.LovViewDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工艺设备类关系表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-22 09:42:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HmeOpEqRelServiceImpl implements HmeOpEqRelService {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeOpEqRelRepository hmeOpEqRelRepository;
    @Autowired
    private HmeOpEqRelMapper hmeOpEqRelMapper;

    @Override
    public Page<HmeOpEqRelDTO2> listForLov(Long tenantId, HmeOpEqRelDTO2 dto, PageRequest pageRequest) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
        lovValueDTOS.stream().sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).collect(Collectors.toList());
        List<HmeOpEqRelDTO2> hmeOpEqRelDtoS = new ArrayList<>();
        for (LovValueDTO LovValueDto : lovValueDTOS) {
            //模糊查询
            if (StringUtils.isNotBlank(dto.getEquipmentCategory()) && !LovValueDto.getValue().contains(dto.getEquipmentCategory())) {
                continue;
            }
            if (StringUtils.isNotBlank(dto.getEquipmentCategoryDesc()) && !LovValueDto.getMeaning().contains(dto.getEquipmentCategoryDesc())) {
                continue;
            }
            HmeOpEqRelDTO2 hmeOpEqRelDTO = new HmeOpEqRelDTO2();
            hmeOpEqRelDTO.setEquipmentCategory(LovValueDto.getValue());
            hmeOpEqRelDTO.setEquipmentCategoryDesc(LovValueDto.getMeaning());
            hmeOpEqRelDtoS.add(hmeOpEqRelDTO);
        }
        //总页数
        int totalPages = 0;
        if(CollectionUtils.isNotEmpty(hmeOpEqRelDtoS)){
            totalPages = hmeOpEqRelDtoS.size() / pageRequest.getSize();
            if(hmeOpEqRelDtoS.size() % pageRequest.getSize() > 0){
                totalPages++;
            }
        }
        //设置分页所需数据
        List<HmeOpEqRelDTO2> hmeOpEqRelDtoList = new ArrayList<>();
        int start = 0;
        int end = 0;
        start = pageRequest.getPage() * pageRequest.getSize();
        end = start + pageRequest.getSize();
        end = hmeOpEqRelDtoS.size() < end ? hmeOpEqRelDtoS.size() : end;
        for (int i = start; i < end; i++) {
            hmeOpEqRelDtoList.add(hmeOpEqRelDtoS.get(i));
        }

        Page<HmeOpEqRelDTO2> resultList = new Page<>();
        resultList.setTotalPages(totalPages);
        resultList.setTotalElements(hmeOpEqRelDtoS.size());
        resultList.setNumberOfElements(hmeOpEqRelDtoS.size());
        resultList.setSize(pageRequest.getSize());
        resultList.setNumber(pageRequest.getPage());
        resultList.setContent(hmeOpEqRelDtoList);
        return resultList;
    }

    @Override
    public List<HmeOpEqRel> createOrUpdate(List<HmeOpEqRel> hmeOpEqRels) {
        List<HmeOpEqRel> resultList = new ArrayList<>();
        for (HmeOpEqRel hmeOpEqRel : hmeOpEqRels) {
            //唯一性校验
            HmeOpEqRel hmeOpEqRel1 = new HmeOpEqRel();
            hmeOpEqRel1.setTenantId(hmeOpEqRel.getTenantId());
            hmeOpEqRel1.setOperationId(hmeOpEqRel.getOperationId());
            hmeOpEqRel1.setEquipmentCategory(hmeOpEqRel.getEquipmentCategory());
            List<HmeOpEqRel> hmeOpEqRelList = hmeOpEqRelRepository.select(hmeOpEqRel1);
            //判断新增还是更新
            if (StringUtils.isNotBlank(hmeOpEqRel.getOpEqRelId())) {
                //更新
                if (CollectionUtils.isNotEmpty(hmeOpEqRelList) && !hmeOpEqRelList.get(0).getOpEqRelId().equals(hmeOpEqRel.getOpEqRelId())) {
                    throw new MtException("HME_OP_EQ_001", mtErrorMessageRepository.getErrorMessageWithModule(hmeOpEqRel.getTenantId(),
                            "HME_OP_EQ_001", "HME", hmeOpEqRel.getEquipmentCategory()));
                }
                hmeOpEqRelMapper.updateByPrimaryKeySelective(hmeOpEqRel);
            } else {
                //新增
                if (CollectionUtils.isNotEmpty(hmeOpEqRelList)) {
                    throw new MtException("HME_OP_EQ_001", mtErrorMessageRepository.getErrorMessageWithModule(hmeOpEqRel.getTenantId(),
                            "HME_OP_EQ_001", "HME", hmeOpEqRel.getEquipmentCategory()));
                }
                hmeOpEqRelRepository.insertSelective(hmeOpEqRel);
            }
            resultList.add(hmeOpEqRel);
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public Page<HmeOpEqRelDTO> query(Long tenantId, String operationId, PageRequest pageRequest) {
        HmeOpEqRel hmeOpEqRel = new HmeOpEqRel();
        hmeOpEqRel.setTenantId(tenantId);
        hmeOpEqRel.setOperationId(operationId);
        Page<HmeOpEqRel> hmeOpEqRels = PageHelper
                .doPageAndSort(pageRequest, () -> hmeOpEqRelRepository.select(hmeOpEqRel));
        List<HmeOpEqRelDTO> hmeOpEqRelList = new ArrayList<>();
        hmeOpEqRels.forEach(t -> {
            HmeOpEqRelDTO hmeOpEqRelDto = new HmeOpEqRelDTO();
            BeanUtils.copyProperties(t, hmeOpEqRelDto);
            hmeOpEqRelList.add(hmeOpEqRelDto);
        });

        Page<HmeOpEqRelDTO> resultList = new Page<>();
        resultList.setTotalPages(hmeOpEqRels.getTotalPages());
        resultList.setTotalElements(hmeOpEqRels.getTotalElements());
        resultList.setNumberOfElements(hmeOpEqRels.getNumberOfElements());
        resultList.setSize(hmeOpEqRels.getSize());
        resultList.setNumber(hmeOpEqRels.getNumber());
        resultList.setContent(hmeOpEqRelList);
        return resultList;
    }
}
