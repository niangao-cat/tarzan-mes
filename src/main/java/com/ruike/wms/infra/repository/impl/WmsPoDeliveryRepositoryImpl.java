package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.repository.WmsPoDeliveryRepository;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO2;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO3;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO4;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 送货单行与采购订单行关系表 资源库实现
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
@Component
public class WmsPoDeliveryRepositoryImpl extends BaseRepositoryImpl<WmsPoDeliveryRel> implements WmsPoDeliveryRepository {

    @Autowired
    private WmsPoDeliveryRelMapper mtPoDeliveryMapper;

    @Override
    public List<WmsPoDeliveryVO3> propertyLimitPoDeliveryQuery(Long tenantId, WmsPoDeliveryVO dto) {
        List<WmsPoDeliveryVO2> mtPoDeliveryVO2List = mtPoDeliveryMapper.selectPoDeliveryByCondition(tenantId, dto);
        // 根据指令头ID筛选出送货单头
        List<WmsPoDeliveryVO2> mtPoDeliveryHeaderList = mtPoDeliveryVO2List.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WmsPoDeliveryVO2::getInstructionDocId))), ArrayList::new));

        // 循环头行进行拆分
        List<WmsPoDeliveryVO3> wmsPoDeliveryVO3List = new ArrayList<>();
        for (WmsPoDeliveryVO2 header : mtPoDeliveryHeaderList) {
             List<WmsPoDeliveryVO2> mtPoDeliveryLineList = mtPoDeliveryVO2List.stream()
                    .filter((WmsPoDeliveryVO2 mtPoDeliveryVO2) -> header.getInstructionDocId().equals(mtPoDeliveryVO2.getInstructionDocId()))
                    .collect(Collectors.toList());
            List<WmsPoDeliveryVO4> wmsPoDeliveryVO4List = new ArrayList<>();
            for (WmsPoDeliveryVO2 line : mtPoDeliveryLineList) {
                WmsPoDeliveryVO4 poDeliveryLineVO = new WmsPoDeliveryVO4();
                BeanUtils.copyProperties(line, poDeliveryLineVO);
                wmsPoDeliveryVO4List.add(poDeliveryLineVO);
            }

            WmsPoDeliveryVO3 wmsPoDeliveryVO3 = new WmsPoDeliveryVO3();
            BeanUtils.copyProperties(header, wmsPoDeliveryVO3);
            wmsPoDeliveryVO3.setWmsPoDeliveryVo4List(wmsPoDeliveryVO4List);
            wmsPoDeliveryVO3List.add(wmsPoDeliveryVO3);
        }

        return wmsPoDeliveryVO3List;
    }

    @Override
    public int updateByPrimaryKey(WmsPoDeliveryRel wmsPoDeliveryRel) {
        return mtPoDeliveryMapper.updateByPrimaryKey(wmsPoDeliveryRel);
    }
}
