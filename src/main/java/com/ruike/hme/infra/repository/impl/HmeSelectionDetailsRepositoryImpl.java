package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmePreSelectionReturnDTO8;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.repository.HmeSelectionDetailsRepository;
import com.ruike.hme.domain.vo.HmeSelectionDetailsQueryVO;
import com.ruike.hme.infra.mapper.HmeSelectionDetailsMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 预挑选明细表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-19 15:44:45
 */
@Component
public class HmeSelectionDetailsRepositoryImpl extends BaseRepositoryImpl<HmeSelectionDetails> implements HmeSelectionDetailsRepository {

    @Autowired
    private HmeSelectionDetailsMapper hmeSelectionDetailsMapper;

    @Autowired
    private MtUserClient mtUserClient;

    @Override
    public Page<HmePreSelectionReturnDTO8> selectionDetailsQuery(Long tenantId, PageRequest pageRequest, HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO) {
        Page<HmePreSelectionReturnDTO8> selectionDetailsPage = PageHelper.doPage(pageRequest, () -> hmeSelectionDetailsMapper.selectionDetailsQuery(tenantId, hmeSelectionDetailsQueryVO));
        if (CollectionUtils.isNotEmpty(selectionDetailsPage.getContent())) {
            List<String> virtualNumList = selectionDetailsPage.getContent().stream().map(HmePreSelectionReturnDTO8::getVirtualNum).distinct().collect(Collectors.toList());
            // 根据虚拟号 汇总波长和功率（存在分页 故查询汇总）
            List<HmePreSelectionReturnDTO8> hmePreSelectionReturnDTO8s = hmeSelectionDetailsMapper.summaryCosFunction(tenantId, virtualNumList);
            // 汇总数据
            Map<String, HmePreSelectionReturnDTO8> preSelectionReturnMap = new HashMap<>();
            Map<String, List<HmePreSelectionReturnDTO8>> hmePreSelectionReturnMap = hmePreSelectionReturnDTO8s.stream().collect(Collectors.groupingBy(HmePreSelectionReturnDTO8::getVirtualNum));
            for (Map.Entry<String, List<HmePreSelectionReturnDTO8>> hmePreSelectionReturnEntry : hmePreSelectionReturnMap.entrySet()) {
                List<HmePreSelectionReturnDTO8> valueList = hmePreSelectionReturnEntry.getValue();
                // 根据cosFunctionId去重汇总
                List<HmePreSelectionReturnDTO8> distinctValueList = valueList.stream().collect(Collectors
                        .collectingAndThen(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(o -> o.getCosFunctionId()))), ArrayList::new));
                HmePreSelectionReturnDTO8 returnDTO8 = new HmePreSelectionReturnDTO8();
                returnDTO8.setVirtualNum(hmePreSelectionReturnEntry.getKey());
                BigDecimal sunaO2 = distinctValueList.stream().map(HmePreSelectionReturnDTO8::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal sunaO4 = distinctValueList.stream().map(HmePreSelectionReturnDTO8::getA04).reduce(BigDecimal.ZERO, BigDecimal::add);
                returnDTO8.setAvga04(sunaO4);
                returnDTO8.setSuna02(sunaO2);
                returnDTO8.setDivisorNum(BigDecimal.valueOf(distinctValueList.size()));
                preSelectionReturnMap.put(hmePreSelectionReturnEntry.getKey(), returnDTO8);
            }

            List<Long> createdByList = selectionDetailsPage.getContent().stream().map(HmePreSelectionReturnDTO8::getCreatedBy).distinct().collect(Collectors.toList());
            Map<Long, MtUserInfo> userMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(createdByList)){
                userMap = mtUserClient.userInfoBatchGet(tenantId, createdByList);
            }
            for (HmePreSelectionReturnDTO8 temp :
                    selectionDetailsPage) {
                //用户
                temp.setUserName(userMap.getOrDefault(temp.getCreatedBy(), new MtUserInfo()).getRealName());
                temp.setStatus("合格");

                //显示位置
                if (StringUtils.isNotBlank(temp.getOldLoad())) {
                    String[] split = temp.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }
                //序列号
                HmePreSelectionReturnDTO8 hmePreSelectionReturnDTO8 = preSelectionReturnMap.get(temp.getVirtualNum());
                //平均值
                temp.setAvga04(BigDecimal.ZERO);
                //求和
                temp.setSuna02(BigDecimal.ZERO);
                if (hmePreSelectionReturnDTO8 != null) {
                    BigDecimal divide = BigDecimal.ZERO;
                    if (hmePreSelectionReturnDTO8.getDivisorNum().compareTo(BigDecimal.ZERO) != 0) {
                        divide = hmePreSelectionReturnDTO8.getAvga04().divide(hmePreSelectionReturnDTO8.getDivisorNum(), 4, RoundingMode.HALF_UP);
                    }
                    //平均值
                    temp.setAvga04(divide);
                    //求和
                    temp.setSuna02(hmePreSelectionReturnDTO8.getSuna02());
                }
            }
        }
        return selectionDetailsPage;
    }

    @Override
    public List<HmePreSelectionReturnDTO8> selectionDetailsExport(Long tenantId, HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO) {
        List<HmePreSelectionReturnDTO8> hmePreSelectionReturnDTO8s = hmeSelectionDetailsMapper.selectionDetailsQuery(tenantId, hmeSelectionDetailsQueryVO);
        if (CollectionUtils.isNotEmpty(hmePreSelectionReturnDTO8s)) {
            String virtualNum = hmePreSelectionReturnDTO8s.get(0).getVirtualNum();
            String finalVirtualNum = virtualNum;
            List<HmePreSelectionReturnDTO8> collect = hmePreSelectionReturnDTO8s.stream().filter(t -> t.getVirtualNum().equals(finalVirtualNum)).collect(Collectors.toList());
            BigDecimal reduce = collect.stream().map(HmePreSelectionReturnDTO8::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal divide = collect.stream().map(HmePreSelectionReturnDTO8::getA04).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(collect.size()), 4, RoundingMode.HALF_UP);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Long> createdByList = hmePreSelectionReturnDTO8s.stream().map(HmePreSelectionReturnDTO8::getCreatedBy).distinct().collect(Collectors.toList());
            Map<Long, MtUserInfo> userMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(createdByList)){
                userMap = mtUserClient.userInfoBatchGet(tenantId, createdByList);
            }
            for (HmePreSelectionReturnDTO8 temp :
                    hmePreSelectionReturnDTO8s) {
                //用户
                temp.setUserName(userMap.getOrDefault(temp.getCreatedBy(), new MtUserInfo()).getRealName());
                temp.setStatus("合格");
                //显示位置
                if (StringUtils.isNotBlank(temp.getOldLoad())) {
                    String[] split = temp.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }

                //序列号
                if (!virtualNum.equals(temp.getVirtualNum())) {
                    virtualNum = temp.getVirtualNum();
                    String finalVirtualNum2 = virtualNum;
                    List<HmePreSelectionReturnDTO8> HmePreSelectionReturnDTO8Temp = hmePreSelectionReturnDTO8s.stream().filter(t -> t.getVirtualNum().equals(finalVirtualNum2)).collect(Collectors.toList());
                    reduce = HmePreSelectionReturnDTO8Temp.stream().map(HmePreSelectionReturnDTO8::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                    divide = HmePreSelectionReturnDTO8Temp.stream().map(HmePreSelectionReturnDTO8::getA04).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(collect.size()), 4, RoundingMode.HALF_UP);
                }
                //平均值
                temp.setAvga04(divide);
                //求和
                temp.setSuna02(reduce);
                // 装配时间
                if (temp.getAssemblyTime() != null) {
                    temp.setAssemblyTimeStr(sdf.format(temp.getAssemblyTime()));
                }
            }
        }
        return hmePreSelectionReturnDTO8s;
    }
}
