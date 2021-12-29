package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualRepository;
import com.ruike.hme.domain.vo.HmeWipStocktakeActualVO;
import com.ruike.hme.domain.vo.HmeWipStocktakeActualVO2;
import com.ruike.hme.infra.mapper.HmeWipStocktakeActualMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在制盘点实际 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Component
public class HmeWipStocktakeActualRepositoryImpl extends BaseRepositoryImpl<HmeWipStocktakeActual> implements HmeWipStocktakeActualRepository {

    private final HmeWipStocktakeActualMapper mapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public HmeWipStocktakeActualRepositoryImpl(HmeWipStocktakeActualMapper mapper, MtErrorMessageRepository mtErrorMessageRepository) {
        this.mapper = mapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    public int save(HmeWipStocktakeActual record) {
        if (StringUtils.isBlank(record.getStocktakeActualId())) {
            return this.insert(record);
        } else {
            return mapper.updateByPrimaryKeySelective(record);
        }
    }

    @Override
    public HmeWipStocktakeActualVO2 queryWipStocktakeActualByRepairMaterialLotId(Long tenantId, List<String> repairMaterialLotIdList, String stocktakeId) {
        HmeWipStocktakeActualVO2 result = new HmeWipStocktakeActualVO2();
        //根据返修SN查询在表hme_eo_rel下的eoId
        List<HmeWipStocktakeActualVO> hmeWipStocktakeActualVOList = mapper.queryEoByRepairMaterialLot(tenantId, repairMaterialLotIdList);
        //repairMaterialLotEoList存储的是每个返修SN与其对应的eoId
        List<HmeWipStocktakeActualVO> repairMaterialLotEoList = new ArrayList<>();
        List<String> errorMaterialLotIdList = new ArrayList<>();
        for (String repairMaterialLotId:repairMaterialLotIdList) {
            String eoId = null;
            //这个要确保每个返修SN都能找到eoId不等于topEoId最近一条数据的top_eo_id
            List<HmeWipStocktakeActualVO> singleWipStocktakeActualVOList = hmeWipStocktakeActualVOList.stream().filter(item ->
                    repairMaterialLotId.equals(item.getMaterialLotId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(singleWipStocktakeActualVOList)){
                //先获取当前遍历SN的eoId即为topEoId
                String topEoId = singleWipStocktakeActualVOList.get(0).getTopEoId();
                //筛选topId不等与eoId最近一条数据的top_eo_id
                List<HmeWipStocktakeActualVO> singleFinalActualVOList = singleWipStocktakeActualVOList.stream().filter(item -> !topEoId.equals(item.getEoId()))
                        .sorted(Comparator.comparing(HmeWipStocktakeActualVO::getLastUpdateDate).reversed())
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleFinalActualVOList)){
                    eoId = singleFinalActualVOList.get(0).getTopEoId();
                }
            }
            //如果最终找不到eoId则弹窗提示
            if(StringUtils.isBlank(eoId)){
                errorMaterialLotIdList.add(repairMaterialLotId);
            }else {
                HmeWipStocktakeActualVO repairMaterialLotEo = new HmeWipStocktakeActualVO();
                repairMaterialLotEo.setMaterialLotId(repairMaterialLotId);
                repairMaterialLotEo.setEoId(eoId);
                repairMaterialLotEoList.add(repairMaterialLotEo);
            }
        }
        if(CollectionUtils.isNotEmpty(errorMaterialLotIdList)){
            result.setErrorMaterialLotIdList(errorMaterialLotIdList);
            return result;
        }
        //根据eo、盘点单ID查询盘点实绩。确保每个返修SN都能找到盘点实绩
        List<String> eoIdList = repairMaterialLotEoList.stream().map(HmeWipStocktakeActualVO::getEoId).collect(Collectors.toList());
        List<HmeWipStocktakeActual> hmeWipStocktakeActualList = mapper.queryWipStocktakeActualByEo(tenantId, eoIdList, stocktakeId);
        result.setHmeWipStocktakeActualList(hmeWipStocktakeActualList);
        for (String repairMaterialLotId:repairMaterialLotIdList) {
            List<HmeWipStocktakeActual> singleWipStocktakeActualList = hmeWipStocktakeActualList.stream().filter(item -> repairMaterialLotId.equals(item.getAttribute1())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(singleWipStocktakeActualList)){
                errorMaterialLotIdList.add(repairMaterialLotId);
            }
        }
        if(CollectionUtils.isNotEmpty(errorMaterialLotIdList)){
            result.setErrorMaterialLotIdList(errorMaterialLotIdList);
            return result;
        }
        return result;
    }
}
