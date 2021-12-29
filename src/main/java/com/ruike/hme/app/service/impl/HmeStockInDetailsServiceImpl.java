package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeStockInDetailsDTO;
import com.ruike.hme.app.service.HmeStockInDetailsService;
import com.ruike.hme.domain.vo.HmeStockInDetailsVO;
import com.ruike.hme.infra.mapper.HmeStockInDetailsMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import static java.util.stream.Collectors.toList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库明细查询报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 12:22
 */
@Service
@Slf4j
public class HmeStockInDetailsServiceImpl implements HmeStockInDetailsService {

    @Autowired
    private HmeStockInDetailsMapper hmeStockInDetailsMapper;

    @Override
    @ProcessLovValue
    public Page<HmeStockInDetailsVO> queryList(Long tenantId, HmeStockInDetailsDTO dto, PageRequest pageRequest) {
        List<HmeStockInDetailsVO> hmeStockInDetailsVOList = hmeStockInDetailsMapper.queryList(tenantId, dto);
        List<HmeStockInDetailsVO> result = new ArrayList<>();
        Map<String, List<HmeStockInDetailsVO>> listMap = hmeStockInDetailsVOList.stream().collect(Collectors.groupingBy(HmeStockInDetailsVO::getWorkOrderNum));
        for (Map.Entry<String, List<HmeStockInDetailsVO>> entry : listMap.entrySet()) {
            List<HmeStockInDetailsVO> list = entry.getValue();
            double sum = list.stream().map(HmeStockInDetailsVO::getActualQty).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            for (HmeStockInDetailsVO vo : list) {
                vo.setActualQtySum(BigDecimal.valueOf(sum));
                NumberFormat percent = NumberFormat.getPercentInstance();
                percent.setMaximumFractionDigits(1);
                vo.setRate(percent.format((BigDecimal.valueOf(sum).divide(vo.getQty(), 20, BigDecimal.ROUND_HALF_UP).doubleValue())));
                result.add(vo);
            }
        }
        Page<HmeStockInDetailsVO> resultList = new Page<>();
        if (Objects.nonNull(dto.getRate())) {
            result = new ArrayList<>();
            for (HmeStockInDetailsVO vo : hmeStockInDetailsVOList) {
                if(Objects.nonNull(vo.getActualQty()) && Objects.nonNull(vo.getQty())) {
                    if (vo.getActualQtySum().divide(vo.getQty(), 20, BigDecimal.ROUND_HALF_UP).compareTo(dto.getRate().divide(BigDecimal.valueOf(100), 20, BigDecimal.ROUND_HALF_UP)) >= 0) {
                        result.add(vo);
                    }
                }
            }
        }
        //计算获取子串开始结束位置
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), result.size());

        resultList.setTotalPages((int) result.size() / pageRequest.getSize() + 1);
        resultList.setTotalElements(result.size());
        resultList.setNumberOfElements(toIndex - fromIndex);
        resultList.setSize(pageRequest.getSize());
        resultList.setNumber(pageRequest.getPage());
        resultList.setContent(result);
        return resultList;
    }
}
