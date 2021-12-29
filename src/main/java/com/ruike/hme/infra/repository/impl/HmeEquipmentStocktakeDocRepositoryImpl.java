package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.query.HmeEquipmentStocktakeDocQuery;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO;
import com.ruike.hme.infra.mapper.HmeEquipmentStocktakeDocMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备盘点单 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@Component
public class HmeEquipmentStocktakeDocRepositoryImpl extends BaseRepositoryImpl<HmeEquipmentStocktakeDoc> implements HmeEquipmentStocktakeDocRepository {
    private final HmeEquipmentStocktakeDocMapper mapper;

    @Autowired
    private LovAdapter lovAdapter;

    public HmeEquipmentStocktakeDocRepositoryImpl(HmeEquipmentStocktakeDocMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(HmeEquipmentStocktakeDoc entity) {
        if (StringUtils.isBlank(entity.getStocktakeId())) {
            this.insertSelective(entity);
        } else {
            mapper.updateByPrimaryKeySelective(entity);
        }
    }

    @Override
    @ProcessLovValue
    public HmeEquipmentStocktakeDocRepresentation byId(String stocktakeId, Long tenantId) {
        HmeEquipmentStocktakeDocQuery query = new HmeEquipmentStocktakeDocQuery(stocktakeId, tenantId);
        List<HmeEquipmentStocktakeDocRepresentation> list = mapper.selectRepresentationList(query);
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
            return new HmeEquipmentStocktakeDocRepresentation();
        }
        return list.get(0);
    }

    @Override
    public HmeEquipmentStocktakeDocRepresentation byDocNum(String stocktakeNum, Long tenantId) {
        HmeEquipmentStocktakeDocQuery query = new HmeEquipmentStocktakeDocQuery(tenantId, stocktakeNum);
        List<HmeEquipmentStocktakeDocRepresentation> list = mapper.selectRepresentationList(query);
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
            return new HmeEquipmentStocktakeDocRepresentation();
        }
        return list.get(0);
    }

    @Override
    @ProcessLovValue
    public Page<HmeEquipmentStocktakeDocRepresentation> page(Long tenantId, HmeEquipmentStocktakeDocQuery query, PageRequest pageRequest) {
        Page<HmeEquipmentStocktakeDocRepresentation> page = PageHelper.doPage(pageRequest, () -> mapper.selectRepresentationList(query));
        //台账类别值集
        List<LovValueDTO> tagLovList = lovAdapter.queryLovValue("HME.LEDGER_TYPE", tenantId);
        Map<String,LovValueDTO> map = new HashMap<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tagLovList)){
            map = tagLovList.stream().collect(Collectors.toMap(LovValueDTO::getValue,t->t));
        }
        Map<String, LovValueDTO> finalMap = map;
        page.getContent().forEach(e->{
            //如果是多个设备类别逗号拼接的，获取对应值
            if (StringUtils.isNotEmpty(e.getLedgerType())){
                if(e.getLedgerType().contains(",")) {
                    List<String> list = Arrays.stream(StringUtils.split(e.getLedgerType(), ",")).collect(Collectors.toList());
                    List<String> res = new ArrayList<>();
                    list.forEach(s -> {
                        LovValueDTO lovValueDTO = finalMap.get(s);
                        if (lovValueDTO != null) {
                            res.add(lovValueDTO.getMeaning());
                        }

                    });
                    e.setLedgerTypeMeaning(StringUtils.join(res,","));
                }else{
                    LovValueDTO lovValueDTO = finalMap.get(e.getLedgerType());
                    if (lovValueDTO != null) {
                        e.setLedgerTypeMeaning(lovValueDTO.getMeaning());
                    }
                }
            }
        });
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeEquipmentStocktakeExportVO> export(Long tenantId, HmeEquipmentStocktakeDocQuery query) {
        List<HmeEquipmentStocktakeExportVO> returnList = mapper.selectExportList(query);
        //台账类别值集
        List<LovValueDTO> tagLovList = lovAdapter.queryLovValue("HME.LEDGER_TYPE", tenantId);
        Map<String,LovValueDTO> map = new HashMap<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tagLovList)){
            map = tagLovList.stream().collect(Collectors.toMap(LovValueDTO::getValue,t->t));
        }
        Map<String, LovValueDTO> finalMap = map;
        returnList.forEach(e->{
            //如果是多个设备类别逗号拼接的，获取对应值
            if (e.getLedgerType() != null && e.getLedgerType().contains(",")){
                List<String> list = Arrays.stream(StringUtils.split(e.getLedgerType(),",")).collect(Collectors.toList());
                StringBuilder ledgerTypeMeaning = new StringBuilder();
                list.forEach(s->{
                    LovValueDTO lovValueDTO = finalMap.get(s);
                    if (lovValueDTO != null){
                        ledgerTypeMeaning.append(lovValueDTO.getMeaning());
                    }

                });
                e.setLedgerTypeMeaning(ledgerTypeMeaning.toString());
            }
        });
        return returnList;
    }

}
