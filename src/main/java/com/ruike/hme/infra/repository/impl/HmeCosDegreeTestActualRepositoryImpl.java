package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeCosDegreeTestActualHis;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosDegreeTestActualMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.hme.infra.util.CommonUtils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActual;
import com.ruike.hme.domain.repository.HmeCosDegreeTestActualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 偏振度和发散角测试结果 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
@Component
public class HmeCosDegreeTestActualRepositoryImpl extends BaseRepositoryImpl<HmeCosDegreeTestActual> implements HmeCosDegreeTestActualRepository {

    @Autowired
    private HmeCosDegreeTestActualMapper hmeCosDegreeTestActualMapper;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Date getLastJobDate(Long tenantId) {
        //获取上次job的执行时间
        HmeCosDegreeTestActual hmeCosDegreeTestActual = hmeCosDegreeTestActualMapper.getLastJobDate(tenantId);
        Date nowDate = new Date();
        Date lastJobDate = null;
        if(Objects.isNull(hmeCosDegreeTestActual)){
            //如果为空，则插入一条JOB数据，用来记录job的执行时间
            hmeCosDegreeTestActual = new HmeCosDegreeTestActual();
            hmeCosDegreeTestActual.setTenantId(tenantId);
            hmeCosDegreeTestActual.setCosType("JOB");
            hmeCosDegreeTestActual.setWafer("JOB");
            hmeCosDegreeTestActual.setTestObject("JOB");
            hmeCosDegreeTestActual.setTestQty(0L);
            hmeCosDegreeTestActual.setTestStatus("JOB");
            this.insertSelective(hmeCosDegreeTestActual);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 2);
            lastJobDate = calendar.getTime();
        }else {
            //如果找到，则取最后更新时间即为上次job的执行时间，同时更新最后更新时间为当前时间
            lastJobDate = hmeCosDegreeTestActual.getLastUpdateDate();
            hmeCosDegreeTestActual.setLastUpdateDate(nowDate);
            hmeCosDegreeTestActualMapper.updateJobDate(tenantId, hmeCosDegreeTestActual);
        }
        return lastJobDate;
    }

    @Override
    public List<HmeCosDegreeTestActual> getCosDegreeTestActualData(Long tenantId, Date lastJobDate) {
        //updateDopCosDegreeTestActualList 存储的是需要回写的数据
        List<HmeCosDegreeTestActual> updateDopCosDegreeTestActualList = new ArrayList<>();
        //查询芯片性能表数据
        List<HmeCosFunction> hmeCosFunctionList = hmeCosDegreeTestActualMapper.codFunctionDataQuery(tenantId, lastJobDate);
        if (CollectionUtils.isNotEmpty(hmeCosFunctionList)) {
            List<String> loadSequenceList = hmeCosFunctionList.stream().map(HmeCosFunction::getLoadSequence).distinct().collect(Collectors.toList());
            //根据loadSequence分批查询装载表中的cosType、wafer，得到不同的cosType、wafer组合
            // attribute17 or attribute18=Y的数据
            List<List<String>> splitLoadSequenceList = CommonUtils.splitSqlList(loadSequenceList, 1000);
            List<HmeMaterialLotLoad> cosTypeWaferList = new ArrayList<>();
            for (List<String> splitLoadSequence : splitLoadSequenceList) {
                cosTypeWaferList.addAll(hmeCosDegreeTestActualMapper.cosTypeWaferQuery(tenantId, splitLoadSequence));
            }
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
            List<HmeCosDegreeTestActual> cosDegreeTestActualQueryList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(cosTypeWaferList)){
                //装载表数据根据cosType和wafer分组,然后根据cosType和wafer再次查询装载表数据
                Map<String, List<HmeMaterialLotLoad>> materialLotLoadMap = cosTypeWaferList.stream().collect(Collectors.groupingBy((item -> {
                    return item.getAttribute1() + "#" + item.getAttribute2();
                })));
                for (Map.Entry<String, List<HmeMaterialLotLoad>> entry : materialLotLoadMap.entrySet()) {
                    String[] split = entry.getKey().split("#");
                    HmeCosDegreeTestActual hmeCosDegreeTestActual = new HmeCosDegreeTestActual();
                    hmeCosDegreeTestActual.setCosType(split[0]);
                    hmeCosDegreeTestActual.setWafer(split[1]);
                    cosDegreeTestActualQueryList.add(hmeCosDegreeTestActual);
                }
                if(CollectionUtils.isNotEmpty(cosDegreeTestActualQueryList)){
                    //根据cosType和wafer查询装载表中attribute17 or attribute18=Y的数据
                    hmeMaterialLotLoadList.addAll(hmeCosDegreeTestActualMapper.materialLotLoadQuery(tenantId, cosDegreeTestActualQueryList));
                }
            }
            if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                //装载表数据根据cosType和wafer分组,然后查询hme_cos_degree_test_actual数据
                Map<String, List<HmeMaterialLotLoad>> materialLotLoadMap = hmeMaterialLotLoadList.stream().collect(Collectors.groupingBy((item -> {
                    return item.getAttribute1() + "#" + item.getAttribute2();
                })));
                List<HmeCosDegreeTestActual> hmeCosDegreeTestActualList = hmeCosDegreeTestActualMapper.cosDegreeTestActualQuery(tenantId, cosDegreeTestActualQueryList);
                //装载表数据取出不同的cosType,然后查询hme_tag_pass_rate_header表数据
                List<String> cosTypeList = hmeMaterialLotLoadList.stream().map(HmeMaterialLotLoad::getAttribute1).distinct().collect(Collectors.toList());
                List<HmeCosDegreeTestActualVO> hmeCosDegreeTestActualVOList = hmeCosDegreeTestActualMapper.tagPassRateHeaderQuery(tenantId, cosTypeList);
                //根据headerId查询行数据
                List<String> headIdList = hmeCosDegreeTestActualVOList.stream().map(HmeCosDegreeTestActualVO::getHeaderId).distinct().collect(Collectors.toList());
                List<HmeCosDegreeTestActualVO2> hmeCosDegreeTestActualVO2List = hmeCosDegreeTestActualMapper.tagPassRateLineQuery(tenantId, headIdList);

                for (Map.Entry<String, List<HmeMaterialLotLoad>> entry : materialLotLoadMap.entrySet()) {
                    String[] split = entry.getKey().split("#");
                    String cosType = split[0];
                    String wafer = split[1];
                    List<HmeMaterialLotLoad> singleMaterialLotLoadList = entry.getValue();
                    //汇总装载表中attribute17=Y的数据，loadSequence的个数为全部数量，即为计算偏振度良率的分母
                    List<String> dopAllLoadSequenceList = singleMaterialLotLoadList.stream()
                            .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getAttribute17()))
                            .map(HmeMaterialLotLoad::getLoadSequence)
                            .collect(Collectors.toList());
                    //汇总装载表中attribute18=Y的数据，loadSequence的个数为全部数量，即为计算发散角良率的分母
                    List<String> divergenceAllLoadSequenceList = singleMaterialLotLoadList.stream()
                            .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getAttribute18()))
                            .map(HmeMaterialLotLoad::getLoadSequence)
                            .collect(Collectors.toList());
                    //根据cosType取出hme_tag_pass_rate_header表
                    List<HmeCosDegreeTestActualVO> hmeCosDegreeTestActualVOS = hmeCosDegreeTestActualVOList.stream().filter(item -> cosType.equals(item.getCosType())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(dopAllLoadSequenceList) || CollectionUtils.isEmpty(divergenceAllLoadSequenceList)
                            || CollectionUtils.isEmpty(hmeCosDegreeTestActualVOS)) {
                        continue;
                    }
                    //根据cosType和wafer查询表hme_cos_degree_test_actual
                    List<HmeCosDegreeTestActual> singleHmeCosDegreeTestActualList = hmeCosDegreeTestActualList.stream()
                            .filter(item -> cosType.equals(item.getCosType()) && wafer.equals(item.getWafer()))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(singleHmeCosDegreeTestActualList)){
                        continue;
                    }
                    HmeCosDegreeTestActual hmeCosDegreeTestActual = singleHmeCosDegreeTestActualList.stream()
                            .filter(item -> "POLARIZATION".equals(item.getTestObject()))
                            .collect(Collectors.toList()).get(0);
                    //如果偏振度的实绩表中测试状态为RELEASE或TestQty大于attribute17=Y的数据个数，则此次循环结束
                    if("RELEASE".equals(hmeCosDegreeTestActual.getTestStatus()) || hmeCosDegreeTestActual.getTestQty() > dopAllLoadSequenceList.size()){
                        continue;
                    }
                    HmeCosDegreeTestActual hmeCosDegreeTestActualDivergence = singleHmeCosDegreeTestActualList.stream()
                            .filter(item -> "VOLATILIZATION".equals(item.getTestObject()))
                            .collect(Collectors.toList()).get(0);
                    //如果发散角的实绩表中测试状态为RELEASE或TestQty大于attribute18=Y的数据个数，则此次循环结束
                    if("RELEASE".equals(hmeCosDegreeTestActualDivergence.getTestStatus()) || hmeCosDegreeTestActualDivergence.getTestQty() > divergenceAllLoadSequenceList.size()){
                        continue;
                    }
                    //计算偏振度良率 从上一步获取到的loadSequence中筛选出A24=E-B05-PZDBF或E-B05-PZDFSJBF的loadSequence个数，两者相减即为分子
                    long dopLoadSequenceSize = hmeCosFunctionList.stream()
                            .filter(item -> dopAllLoadSequenceList.contains(item.getLoadSequence())
                                    && ("E-B05-PZDBF".equals(item.getA24()) || "E-B05-PZDFSJBF".equals(item.getA24())))
                            .count();
                    int dopSize = dopAllLoadSequenceList.size();
                    long dopMolecule = dopSize - dopLoadSequenceSize;
                    BigDecimal dopYield = BigDecimal.valueOf(dopMolecule).divide(BigDecimal.valueOf(dopSize), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                    //计算发散角良率 从上一步获取到的loadSequence中筛选出A24=E-B05-FSJBF的loadSequence个数，两者相减即为分子
                    long divergenceLoadSequenceSize = hmeCosFunctionList.stream()
                            .filter(item -> divergenceAllLoadSequenceList.contains(item.getLoadSequence())
                                    && "E-B05-FSJBF".equals(item.getA24()))
                            .count();
                    int divergenceSize = divergenceAllLoadSequenceList.size();
                    long divergenceMolecule = divergenceSize - divergenceLoadSequenceSize;
                    BigDecimal divergenceYield = BigDecimal.valueOf(divergenceMolecule).divide(BigDecimal.valueOf(divergenceSize), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                    hmeCosDegreeTestActualDivergence.setTestRate(divergenceYield);
                    hmeCosDegreeTestActualDivergence.setTestStatus("COMPLETE");
                    updateDopCosDegreeTestActualList.add(hmeCosDegreeTestActualDivergence);
                    if(Objects.isNull(hmeCosDegreeTestActual.getPriority())){
                        //如果Priority优先级为空，则取出表hme_tag_pass_rate_header中数据
                        HmeCosDegreeTestActualVO hmeCosDegreeTestActualVO = hmeCosDegreeTestActualVOS.get(0);
                        if(dopYield.compareTo(hmeCosDegreeTestActualVO.getPassRate()) >= 0){
                            //如果计算出来的偏振度良率 >= PassRate,则回写test_rate
                            hmeCosDegreeTestActual.setTestRate(dopYield);
                            hmeCosDegreeTestActual.setTestStatus("COMPLETE");
                        }else {
                            //否则，根据headerId查询Priority=1的行，回写结果
                            HmeCosDegreeTestActualVO2 priorityEqualOneLine = hmeCosDegreeTestActualVO2List.stream()
                                    .filter(item -> hmeCosDegreeTestActualVO.getHeaderId().equals(item.getHeaderId())
                                            && item.getPriority() == 1)
                                    .collect(Collectors.toList()).get(0);
                            //hmeCosDegreeTestActual.setTargetQty(priorityEqualOneLine.getTestSumQty() - hmeCosDegreeTestActual.getTestQty());
                            hmeCosDegreeTestActual.setTestQty(priorityEqualOneLine.getTestSumQty());
                            hmeCosDegreeTestActual.setTestRate(dopYield);
                            hmeCosDegreeTestActual.setPriority(priorityEqualOneLine.getPriority());
                            hmeCosDegreeTestActual.setTestStatus("TEST");
                        }
                    }else {
                        //如果Priority优先级不为空，则取出表hme_tag_pass_rate_header中数据
                        HmeCosDegreeTestActualVO hmeCosDegreeTestActualVO = hmeCosDegreeTestActualVOS.get(0);
                        long actualPriority = hmeCosDegreeTestActual.getPriority().longValue();
                        if(actualPriority != 0){
                            //如果actualPriority不等于0，则根据头ID、Priority=hmeCosDegreeTestActual的Priority查询行
                            HmeCosDegreeTestActualVO2 priorityEqualActualLine = hmeCosDegreeTestActualVO2List.stream()
                                    .filter(item -> hmeCosDegreeTestActualVO.getHeaderId().equals(item.getHeaderId())
                                            && item.getPriority() == actualPriority)
                                    .collect(Collectors.toList()).get(0);
                            if(dopYield.compareTo(priorityEqualActualLine.getAddPassRate()) >= 0){
                                //如果计算出来的偏振度良率 >= AddPassRate,则回写test_rate、test_status
                                hmeCosDegreeTestActual.setTestRate(dopYield);
                                hmeCosDegreeTestActual.setTestStatus("COMPLETE");
                            }else {
                                //如果计算出来的偏振度良率 < AddPassRate且当前Priority!=0，根据headerId查询Priority=actual表Priority+1的行，回写结果
                                long actualAddPriority = actualPriority + 1;
                                List<HmeCosDegreeTestActualVO2> priorityEqualActualAddlineList = hmeCosDegreeTestActualVO2List.stream()
                                        .filter(item -> hmeCosDegreeTestActualVO.getHeaderId().equals(item.getHeaderId())
                                                && item.getPriority() == actualAddPriority)
                                        .collect(Collectors.toList());
                                if(CollectionUtils.isEmpty(priorityEqualActualAddlineList)){
                                    //如果找不到，Priority+1的行则只回写Priority为0和良率
                                    hmeCosDegreeTestActual.setPriority(0L);
                                    hmeCosDegreeTestActual.setTestRate(dopYield);
                                    hmeCosDegreeTestActual.setTestStatus("TEST");
                                }else {
                                    //如果找到，则回写结果
                                    HmeCosDegreeTestActualVO2 priorityEqualActualAddline = priorityEqualActualAddlineList.get(0);
                                    //hmeCosDegreeTestActual.setTargetQty(priorityEqualActualAddline.getTestSumQty() - hmeCosDegreeTestActual.getTestQty());
                                    hmeCosDegreeTestActual.setTestQty(priorityEqualActualAddline.getTestSumQty());
                                    hmeCosDegreeTestActual.setTestRate(dopYield);
                                    hmeCosDegreeTestActual.setPriority(priorityEqualActualAddline.getPriority());
                                    hmeCosDegreeTestActual.setTestStatus("TEST");
                                }
                            }
                        }else {
                            //如果actualPriority等于0，则直接回写结果
                            hmeCosDegreeTestActual.setTestRate(dopYield);
                            hmeCosDegreeTestActual.setTestStatus("TEST");
//                            HmeCosDegreeTestActualVO2 priorityEqualActualLine = hmeCosDegreeTestActualVO2List.stream()
//                                    .filter(item -> hmeCosDegreeTestActualVO.getHeaderId().equals(item.getHeaderId()))
//                                    .sorted(Comparator.comparing(HmeCosDegreeTestActualVO2::getPriority).reversed())
//                                    .collect(Collectors.toList()).get(0);
//                            if(dopYield.compareTo(priorityEqualActualLine.getAddPassRate()) >= 0){
//                                //如果计算出来的偏振度良率 >= AddPassRate,则回写test_rate、test_status
//                                hmeCosDegreeTestActual.setTestRate(dopYield);
//                                hmeCosDegreeTestActual.setTestStatus("COMPLETE");
//                            }else {
//                                //如果计算出来的偏振度良率 < AddPassRate,则回写结果
//                                hmeCosDegreeTestActual.setTestQty(0L);
//                                hmeCosDegreeTestActual.setTestRate(dopYield);
//                                hmeCosDegreeTestActual.setTestStatus("COMPLETE");
//                            }
                        }
                    }
                    updateDopCosDegreeTestActualList.add(hmeCosDegreeTestActual);
                }
            }
        }
        return updateDopCosDegreeTestActualList;
    }

    @Override
    public void updateCosDegreeTestActualData(Long tenantId, List<HmeCosDegreeTestActual> hmeCosDegreeTestActualList) {
        List<String> ids = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_his_s", hmeCosDegreeTestActualList.size());
        List<String> hisCids = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_his_cid_s", hmeCosDegreeTestActualList.size());
        int i = 0;
        Date nowDate = new Date();
        List<String> sqlList = new ArrayList<>();
        for (HmeCosDegreeTestActual hmeCosDegreeTestActual:hmeCosDegreeTestActualList) {
            hmeCosDegreeTestActual.setObjectVersionNumber(hmeCosDegreeTestActual.getObjectVersionNumber() + 1);
            hmeCosDegreeTestActual.setLastUpdatedBy(-1L);
            hmeCosDegreeTestActual.setLastUpdateDate(nowDate);
            sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeCosDegreeTestActual));
            HmeCosDegreeTestActualHis hmeCosDegreeTestActualHis = new HmeCosDegreeTestActualHis();
            BeanCopierUtil.copy(hmeCosDegreeTestActual, hmeCosDegreeTestActualHis);
            hmeCosDegreeTestActualHis.setDegreeTestHisId(ids.get(i));
            hmeCosDegreeTestActualHis.setCid(Long.valueOf(hisCids.get(i)));
            hmeCosDegreeTestActualHis.setObjectVersionNumber(1L);
            hmeCosDegreeTestActualHis.setCreatedBy(-1L);
            hmeCosDegreeTestActualHis.setCreationDate(nowDate);
            hmeCosDegreeTestActualHis.setLastUpdatedBy(-1L);
            hmeCosDegreeTestActualHis.setLastUpdateDate(nowDate);
            sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeCosDegreeTestActualHis));
            i++;
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }
}
