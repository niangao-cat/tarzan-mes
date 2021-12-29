package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeTagDaqAttrDTO;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO2;
import com.ruike.hme.infra.mapper.HmeTagDaqAttrMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeTagDaqAttr;
import com.ruike.hme.domain.repository.HmeTagDaqAttrRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据项数据采集扩展属性表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 09:52:44
 */
@Component
public class HmeTagDaqAttrRepositoryImpl extends BaseRepositoryImpl<HmeTagDaqAttr> implements HmeTagDaqAttrRepository {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeTagDaqAttrMapper hmeTagDaqAttrMapper;

    @Override
    public Page<LovValueDTO> dataCollectionLovQuery(Long tenantId, HmeTagDaqAttrDTO dto, PageRequest pageRequest) {
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.DATA_ACQUISITION_FIELD", tenantId);
        lovValueDTOList.removeIf(item -> !(item.getDescription().contains(dto.getEquipmentCategory())));
//        lovValueDTOList.removeIf(item -> !(dto.getEquipmentCategory().equals(item.getDescription())));
        if(StringUtils.isNotEmpty(dto.getValue())){
            //数据采集编码筛选
            lovValueDTOList.removeIf(item -> !(item.getValue().contains(dto.getValue())));
        }
        if(StringUtils.isNotEmpty(dto.getMeaning())){
            //数据采集描述筛选
            lovValueDTOList.removeIf(item -> !(item.getMeaning().contains(dto.getMeaning())));
        }
        List<LovValueDTO> soredList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lovValueDTOList)){
            soredList = lovValueDTOList.stream().sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).collect(Collectors.toList());
        }
        List<LovValueDTO> lovValueDTOS = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), soredList);
        return new Page<>(lovValueDTOS, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), soredList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTagDaqAttr createOrUpdate(Long tenantId, HmeTagDaqAttr dto) {
        dto.setTenantId(tenantId);
        if(StringUtils.isEmpty(dto.getTagDaqAttrId())){
            //新增
            this.insertSelective(dto);
        }else{
            //更新
            HmeTagDaqAttr hmeTagDaqAttr = this.selectByPrimaryKey(dto.getTagDaqAttrId());
            hmeTagDaqAttr.setEquipmentCategory(dto.getEquipmentCategory());
            hmeTagDaqAttr.setValueField(dto.getValueField());
            hmeTagDaqAttr.setLimitCond1(dto.getLimitCond1());
            hmeTagDaqAttr.setLimitCond2(dto.getLimitCond2());
            hmeTagDaqAttr.setCond1Value(dto.getCond1Value());
            hmeTagDaqAttr.setCond2Value(dto.getCond2Value());
            hmeTagDaqAttrMapper.updateByPrimaryKey(hmeTagDaqAttr);
        }
        return dto;
    }

    @Override
    public HmeTagDaqAttrVO2 query(Long tenantId, String tagId) {
        HmeTagDaqAttrVO2 result = new HmeTagDaqAttrVO2();
        HmeTagDaqAttr hmeTagDaqAttr = this.selectOne(new HmeTagDaqAttr() {{
            setTenantId(tenantId);
            setTagId(tagId);
        }});
        if(hmeTagDaqAttr != null){
            BeanUtils.copyProperties(hmeTagDaqAttr, result);
            if(StringUtils.isNotEmpty(result.getValueField())){
                String valueFieldMeaning = lovAdapter.queryLovMeaning("HME.DATA_ACQUISITION_FIELD", tenantId, result.getValueField());
                result.setValueFieldMeaning(valueFieldMeaning);
            }
            if(StringUtils.isNotEmpty(result.getLimitCond1())){
                String limitCond1Meaning = lovAdapter.queryLovMeaning("HME.DATA_ACQUISITION_FIELD", tenantId, result.getLimitCond1());
                result.setLimitCond1Meaning(limitCond1Meaning);
            }
            if(StringUtils.isNotEmpty(result.getLimitCond2())){
                String limitCond2Meaning = lovAdapter.queryLovMeaning("HME.DATA_ACQUISITION_FIELD", tenantId, result.getLimitCond2());
                result.setLimitCond2Meaning(limitCond2Meaning);
            }
        }
        return result;
    }
}
