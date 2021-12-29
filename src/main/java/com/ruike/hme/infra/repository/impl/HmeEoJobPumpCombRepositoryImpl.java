package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobPumpCombMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEoJobPumpComb;
import com.ruike.hme.domain.repository.HmeEoJobPumpCombRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 泵浦源组合关系表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
@Component
@Slf4j
public class HmeEoJobPumpCombRepositoryImpl extends BaseRepositoryImpl<HmeEoJobPumpComb> implements HmeEoJobPumpCombRepository {

    @Autowired
    private HmeEoJobPumpCombMapper hmeEoJobPumpCombMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public String getSnByWorkOrder(Long tenantId, MtWorkOrder mtWorkOrder, String workcellId) {
        //查询该工单在此工位是否存在在制品
        String snNum = hmeEoJobPumpCombMapper.getWipMaterialLotCodeByWo(tenantId, workcellId, mtWorkOrder.getWorkOrderId());
        if (StringUtils.isNotBlank(snNum)) {
            return snNum;
        }
        //若查不到，则取工单中EO清单中未进过本作业平台的SN
        snNum = hmeEoJobPumpCombMapper.getEoIdentificationByWo(tenantId, mtWorkOrder.getWorkOrderId(), mtWorkOrder.getSiteId());
        return snNum;
    }

    @Override
    public List<HmeEoJobPumpCombVO5> pumpFilterRuleVerify(Long tenantId, HmeEoJobSnVO3 dto) {
        List<HmeEoJobPumpCombVO5> resultList = new ArrayList<>();
        //根据物料查询有效的泵浦源筛选规则行,找不到则报错筛选规则没有维护筛选条件
        List<HmeEoJobPumpCombVO3> hmeEoJobPumpCombVO3List = hmeEoJobPumpCombMapper.qureyPumpFilterRuleLineByMaterial(tenantId, dto.getSnMaterialId());
        if (CollectionUtils.isEmpty(hmeEoJobPumpCombVO3List)) {
            throw new MtException("HME_EO_JOB_SN_225", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_225", "HME"));
        }
        List<String> tagIdList = hmeEoJobPumpCombVO3List.stream().map(HmeEoJobPumpCombVO3::getTagId).distinct().collect(Collectors.toList());

        List<String> materialLotIdList = null;
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsPumpProcess())){
            //根据出站的jobId查询泵浦源组合关系表中泵浦源物料批
            materialLotIdList = hmeEoJobPumpCombMapper.getPumpMaterialLotIdByJobId(tenantId, dto.getJobId());
        } else {
            materialLotIdList = hmeEoJobPumpCombMapper.getPumpMaterialLotIdByCombBarcodeId(tenantId, dto.getMaterialLotId());
        }
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "泵浦源物料批集合"));
        }
        //查询值集HME_PUMP_SOURCE_WKC下维护的值（即是工艺编码）,进而查询出工序下的工位
        List<LovValueDTO> processCodeLov = lovAdapter.queryLovValue("HME_PUMP_SOURCE_WKC", tenantId);
        if (CollectionUtils.isEmpty(processCodeLov)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "值集HME_PUMP_SOURCE_WKC"));
        }
        List<String> processCodeList = processCodeLov.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        List<String> workcellIdList = hmeEoJobPumpCombMapper.getWorkcellByOperation(tenantId, processCodeList);
        if (CollectionUtils.isEmpty(workcellIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "值集HME_PUMP_SOURCE_WKC下的工位"));
        }
        //根据workcell_id+泵浦源物料批查询hme_eo_job_sn的workcell_id+泵浦源物料批+jobId+工位的上层工序+最后更新时间
        Long startDate = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO4> hmeEoJobPumpCombVO4List = hmeEoJobPumpCombMapper.getJobId(tenantId, workcellIdList, materialLotIdList);
        log.info("=================================>getJobId耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobPumpCombVO4List)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> jobIdList = new ArrayList<>();
        //查询到的数据根据工序+物料批分组，每组下取更新时间最近的那笔jobId
        // 20211126 modify by sanfeng.zhang for wenxin.zhang 根据物料去取最新的 存在返修 在不同工序的数据
        Map<String, List<HmeEoJobPumpCombVO4>> jobIdMap = hmeEoJobPumpCombVO4List.stream().collect(Collectors.groupingBy((item -> {
            return item.getMaterialLotId();
        })));
        for (Map.Entry<String, List<HmeEoJobPumpCombVO4>> map : jobIdMap.entrySet()) {
            List<HmeEoJobPumpCombVO4> sortedValue = map.getValue().stream().sorted(Comparator.comparing(HmeEoJobPumpCombVO4::getLastUpdateDate).reversed()).collect(Collectors.toList());
            jobIdList.add(sortedValue.get(0).getJobId());
        }
        //根据jobId+tagId查询数据采集项
        Long startDate2 = System.currentTimeMillis();
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobPumpCombMapper.queryDataRecordByJobTag(tenantId, jobIdList, tagIdList);
        log.info("=================================>queryDataRecordByJobTag耗时："+(System.currentTimeMillis() - startDate2)+ "ms");
        if (CollectionUtils.isEmpty(hmeEoJobDataRecordList)) {
            throw new MtException("HME_EO_JOB_SN_226", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_226", "HME"));
        }
        List<String> tagIdList2 = hmeEoJobDataRecordList.stream().map(HmeEoJobDataRecord::getTagId).distinct().collect(Collectors.toList());
        //如果之前找到的筛选规则行表的tagId集合和现在找到的数据采集项表的tagId集合不完全相同则报错
        if (!tagIdList.stream().sorted().collect(Collectors.joining())
                .equals(tagIdList2.stream().sorted().collect(Collectors.joining()))) {
            throw new MtException("HME_EO_JOB_SN_227", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_227", "HME"));
        }
        //进行求和类型校验
        Long startDate3 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> sumRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "SUM".equals(item.getCalculateType())).collect(Collectors.toList());
        //tagResultMap存储的是tagId与其所对应的所有result之和的关系，并用于计算公式校验
        Map<String, BigDecimal> tagResultMap = new HashMap<>();
        //2021-10-25 17:20 edit by chaonan.hu for hui.gu 将求和类型和计算类型报错消息汇总在一起显示
        StringBuilder sumAndCalculationErrorMessage = new StringBuilder();
        for (HmeEoJobPumpCombVO3 sumRuleLine : sumRuleLineList) {
            //如果当前遍历的tagId在之前的循环当中已校验过，则跳出此次循环，不必重复校验
            if(tagResultMap.keySet().contains(sumRuleLine.getTagId())){
                continue;
            }
            //将计算类型hme_pump_filter_rule_line.【type】=SUM的【tag_id】对应的hme_eo_job_data_record.【result】求和
            List<HmeEoJobDataRecord> sumDateRecordList = hmeEoJobDataRecordList.stream().filter(item -> sumRuleLine.getTagId().equals(item.getTagId())).collect(Collectors.toList());
            List<String> sumResultList = sumDateRecordList.stream().map(HmeEoJobDataRecord::getResult).collect(Collectors.toList());
            BigDecimal result = BigDecimal.ZERO;
            try {
                for (String sumResult : sumResultList) {
                    result = result.add(new BigDecimal(sumResult));
                }
            } catch (Exception ex) {
                throw new MtException("进行求和类型校验时,数据采集项"+ sumRuleLine.getTagId()+"的结果不为数字");
            }
            //2021-10-25 11:00 edit by chaonan.hu for hui.gu 需要考虑最小值或最大值为空的情况，但两者不会同时为空
            //若小于hme_pump_filter_rule_line.【min】则报错数据项之和小于下限,不允许出站
            if(Objects.nonNull(sumRuleLine.getMinValue())){
                if (result.compareTo(sumRuleLine.getMinValue()) < 0){
                    MtTag mtTag = mtTagRepository.tagGet(tenantId, sumRuleLine.getTagId());
                    sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_215", "HME", mtTag.getTagCode(), sumRuleLine.getMinValue().toString()));
                }
            }
            //若大于hme_pump_filter_rule_line.【max】则报错数据项之和大于下限,不允许出站
            if(Objects.nonNull(sumRuleLine.getMaxValue())){
                if (result.compareTo(sumRuleLine.getMaxValue()) > 0){
                    MtTag mtTag = mtTagRepository.tagGet(tenantId, sumRuleLine.getTagId());
                    sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_216", "HME", mtTag.getTagCode(), sumRuleLine.getMaxValue().toString()));
                }
            }
            tagResultMap.put(sumRuleLine.getTagId(), result);
            //2021-09-07 10:27 add by chaonan.hu for wenxin.zhang 出站时增加记录功率和电压数据到数据采集项记录表逻辑
            if("P".equals(sumRuleLine.getParameterCode()) || "V".equals(sumRuleLine.getParameterCode())){
                String tagCode = lovAdapter.queryLovMeaning("HME.PUMP_TAG_PARAMETER", tenantId, sumRuleLine.getParameterCode());
                MtTag mtTag = mtTagRepository.selectOne(new MtTag() {{
                    setTenantId(tenantId);
                    setTagCode(tagCode);
                }});
                if(Objects.isNull(mtTag)){
                    sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_232", "HME", tagCode));
                }
                HmeEoJobPumpCombVO5 hmeEoJobPumpCombVO5 = new HmeEoJobPumpCombVO5();
                hmeEoJobPumpCombVO5.setParameterCode(sumRuleLine.getParameterCode());
                hmeEoJobPumpCombVO5.setMinValue(sumRuleLine.getMinValue());
                hmeEoJobPumpCombVO5.setMaxValue(sumRuleLine.getMaxValue());
                hmeEoJobPumpCombVO5.setTagId(mtTag.getTagId());
                hmeEoJobPumpCombVO5.setResult(result);
                resultList.add(hmeEoJobPumpCombVO5);
            }
        }
        log.info("=================================>进行求和类型校验耗时："+(System.currentTimeMillis() - startDate3)+ "ms");
        //进行单个符合类型校验
        Long startDate4 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> singleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "SINGLE".equals(item.getCalculateType())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(singleRuleLineList)){
            //将SINGLE类型的筛选规则行数据根据tagId分组，每组tagId会对应多个result,多个筛选规则行Id。
            //对于找到的所有result进行排列组合，只有这些组合中至少存在一种情况满足各个result落在每一个筛选规则行中即代表此tag通过校验
            Map<String, List<HmeEoJobPumpCombVO3>> singleRuleLineMap = singleRuleLineList.stream().collect(Collectors.groupingBy(HmeEoJobPumpCombVO3::getTagId));
            for (Map.Entry<String, List<HmeEoJobPumpCombVO3>> singleRuleLine:singleRuleLineMap.entrySet()) {
                String tagId = singleRuleLine.getKey();
                List<HmeEoJobPumpCombVO3> value = singleRuleLine.getValue();
                List<HmeEoJobDataRecord> singleDateRecordList = hmeEoJobDataRecordList.stream().filter(item -> tagId.equals(item.getTagId())).collect(Collectors.toList());
                //获取到当前tagId所对应的全部result
                List<String> singleResultList = singleDateRecordList.stream().map(HmeEoJobDataRecord::getResult).collect(Collectors.toList());
                if(value.size() != singleResultList.size()){
                    //业务说前面的逻辑能保证这两个数一定相等，不必考虑这个异常情况，但是为了保险起见，这里自己加了此校验
                    throw new MtException("进行单个符合类型校验时,数据采集项"+ tagId+"的筛选规则行数与找到的数据采集项结果的个数不一致");
                }
                //结果由字符串转BigDecimal
                List<BigDecimal> singleResultBigList = new ArrayList<>();
                try {
                    for (String singleResult:singleResultList) {
                        singleResultBigList.add(new BigDecimal(singleResult));
                    }
                }catch (Exception ex){
                    throw new MtException("进行单个符合类型校验时,数据采集项"+ tagId+"的结果不为数字");
                }
                BigDecimal[] singleResultBigArray = singleResultBigList.toArray(new BigDecimal[singleResultBigList.size()]);
                //对存放result的list进行排列组合，然后遍历排列组合后的每一种情况，至少存在一种情况满足各个result落在每一个筛选规则行中即代表此tag通过校验
                List<List<BigDecimal>> permSingleResultBigList = new ArrayList<>();
                perm(singleResultBigArray, new Stack<>(), permSingleResultBigList);
                //singleTagFlag标志当前遍历的tagId下的所有result是否满足筛选规则行
                boolean singleTagFlag = false;
                for (List<BigDecimal> singleResultBig:permSingleResultBigList) {
                    //successI代表当前遍历的排列组合情况下的每个result通过校验的次数
                    int successI = 0;
                    for (int i = 0; i < singleResultBig.size(); i++) {
                        //业务指定这里不会存在最小值、最大值同时为空的情况
                        if(Objects.nonNull(value.get(i).getMinValue()) && Objects.nonNull(value.get(i).getMaxValue())){
                            if(singleResultBig.get(i).compareTo(value.get(i).getMinValue()) >= 0
                                    && singleResultBig.get(i).compareTo(value.get(i).getMaxValue()) <= 0){
                                successI++;
                            }
                        }else if(Objects.nonNull(value.get(i).getMinValue()) && Objects.isNull(value.get(i).getMaxValue())){
                            if(singleResultBig.get(i).compareTo(value.get(i).getMinValue()) >= 0){
                                successI++;
                            }
                        }else if(Objects.isNull(value.get(i).getMinValue()) && Objects.nonNull(value.get(i).getMaxValue())){
                            if(singleResultBig.get(i).compareTo(value.get(i).getMaxValue()) <= 0){
                                successI++;
                            }
                        }
                    }
                    //如果次数最终等于result的个数，则代表通过校验
                    if(successI == singleResultBig.size()){
                        singleTagFlag = true;
                        break;
                    }
                }
                //如果最终singleTagFlag为false，则报错数据项不满足要求,不允许出站
                if(!singleTagFlag){
                    MtTag mtTag = mtTagRepository.tagGet(tenantId, tagId);
                    throw new MtException("HME_EO_JOB_SN_217", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_217", "HME", mtTag.getTagCode()));
                }
            }
        }
        log.info("=================================>进行单个符合类型校验耗时："+(System.currentTimeMillis() - startDate4)+ "ms");
        //进行计算类型校验
        Long startDate5 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> calculationRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "CALCULATION".equals(item.getCalculateType())).collect(Collectors.toList());
        for (HmeEoJobPumpCombVO3 hmeEoJobPumpCombVO3:calculationRuleLineList) {
            //获取公式中的每一个字母
            String formula = hmeEoJobPumpCombVO3.getFormula();
            if(StringUtils.isNotBlank(formula)){
                //letterResultMap存储的是每个字母与其下对应的result和
                Map<String, Double> letterResultMap = new HashMap<>();
                List<String> letterList = new ArrayList<>();
                for (int i = 0; i < formula.length(); i++) {
                    char letter = formula.charAt(i);
                    if((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')){
                        letterList.add(String.valueOf(letter));
                    }
                }
                letterList = letterList.stream().distinct().collect(Collectors.toList());
                //创建公式键值对
                Scope scope = new Scope();
                for (String letter:letterList) {
                    Double letterResultSum = null;
                    //先从map中根据字母取对应的result,取不到则根据parameter_code=字母，type=sum找到对应的唯一一条筛选规则行数据的tagId，进而在map中取出对应的result和
                    if(letterResultMap.keySet().contains(letter)){
                        letterResultSum = letterResultMap.get(letter);
                    }else {
                        List<HmeEoJobPumpCombVO3> letterFilterRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> letter.equals(item.getParameterCode()) && "SUM".equals(item.getCalculateType())).collect(Collectors.toList());
                        //报错计算公式找不到参数代码
                        if(CollectionUtils.isEmpty(letterFilterRuleLineList)){
                            sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_229", "HME", formula, letter));
                        }
                        letterResultSum = tagResultMap.get(letterFilterRuleLineList.get(0).getTagId()).doubleValue();
                        letterResultMap.put(letter, letterResultSum);
                    }
                    //添加参数键值对
                    scope.getVariable(letter).setValue(letterResultSum);
                }
                //计算公式
                Double formulaValue = null;
                try {
                    Expression expr2 = Parser.parse(formula, scope);
                    formulaValue = expr2.evaluate();
                } catch (Exception e) {
                    log.error("<===== HmeEoJobPumpCombRepositoryImpl.pumpFilterRuleVerify 公式【{}】，参数【{}】计算错误 ", formula, scope.getLocalVariables());
                    log.error("<===== HmeEoJobPumpCombRepositoryImpl.pumpFilterRuleVerify " + e);
                    throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_DATA_RECORD_003", "HME", formula));
                }
                if(Objects.nonNull(hmeEoJobPumpCombVO3.getMinValue())){
                    if(formulaValue < hmeEoJobPumpCombVO3.getMinValue().doubleValue()){
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, hmeEoJobPumpCombVO3.getTagId());
                        sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_218", "HME", mtTag.getTagCode(), hmeEoJobPumpCombVO3.getMinValue().toString()));
                    }
                }
                if(Objects.nonNull(hmeEoJobPumpCombVO3.getMaxValue())){
                    if(formulaValue > hmeEoJobPumpCombVO3.getMaxValue().doubleValue()){
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, hmeEoJobPumpCombVO3.getTagId());
                        sumAndCalculationErrorMessage.append(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_219", "HME", mtTag.getTagCode(), hmeEoJobPumpCombVO3.getMaxValue().toString()));
                    }
                }
            }
        }
        log.info("=================================>进行计算类型校验耗时："+(System.currentTimeMillis() - startDate5)+ "ms");
        if(StringUtils.isNotBlank(sumAndCalculationErrorMessage.toString())){
            throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_235", "HME", sumAndCalculationErrorMessage.toString()));
        }
        //进行多个符合类型校验  2021/09/07 09:52 add by chaonan.hu for wenxin.zhang
        Long startDate6 = System.currentTimeMillis();
        List<HmeEoJobPumpCombVO3> multipleRuleLineList = hmeEoJobPumpCombVO3List.stream().filter(item -> "MULTIPLE".equals(item.getCalculateType())).collect(Collectors.toList());
        for (HmeEoJobPumpCombVO3 multipleRuleLine:multipleRuleLineList) {
            BigDecimal minValue = multipleRuleLine.getMinValue();
            BigDecimal maxValue = multipleRuleLine.getMaxValue();
            //根据多个符合类型的筛选规则行的tagId取到所有的result，必须每个result都位于当前遍历的筛选规则行的最小值与最大值之间
            List<String> multipleResultList = hmeEoJobDataRecordList.stream()
                    .filter(item -> multipleRuleLine.getTagId().equals(item.getTagId()))
                    .map(HmeEoJobDataRecord::getResult).collect(Collectors.toList());
            List<BigDecimal> resultBigDecimalList = new ArrayList<>();
            try{
                for (String result:multipleResultList) {
                    resultBigDecimalList.add(new BigDecimal(result));
                }
            }catch (Exception ex){
                throw new MtException("进行多个符合类型校验时,数据采集项"+ multipleRuleLine.getTagId()+"的结果不为数字");
            }
            for (BigDecimal resultBigDecimal:resultBigDecimalList) {
                if(Objects.nonNull(minValue)){
                    if(resultBigDecimal.compareTo(minValue) < 0){
                        //报错规则类型为“多个符合的”数据项【${1}】,结果【${2}】小于下限【${3}】。
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_254", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_254", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), minValue.toString()));
                    }
                }
                if(Objects.nonNull(maxValue)){
                    if(resultBigDecimal.compareTo(maxValue) > 0){
                        //报错规则类型为“多个符合的”数据项【${1}】,结果【${2}】大于上限【${3}】。
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, multipleRuleLine.getTagId());
                        throw new MtException("HME_EO_JOB_SN_255", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_255", "HME", mtTag.getTagCode(), resultBigDecimal.toString(), maxValue.toString()));
                    }
                }
            }
        }
        log.info("=================================>进行多个符合类型校验耗时："+(System.currentTimeMillis() - startDate6)+ "ms");
        return resultList;
    }

    @Override
    public HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8) {
        HmeEoJobSnBatchVO14 result = new HmeEoJobSnBatchVO14();
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnBatchVO8.getHmeEoJobSn();

        List<HmeEoJobPumpComb> hmeEoJobPumpCombList = null;
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getIsRepairProcess())) {
            // 返修作业平台 根据组合物料批查询泵浦组合关系
            hmeEoJobPumpCombList = hmeEoJobPumpCombMapper.queryPumbCombByCombMaterialLot(tenantId, hmeEoJobSn.getMaterialLotId());
        } else {
            //根据jobId、组合物料批ID查询泵浦源组合关系数据
            hmeEoJobPumpCombList = hmeEoJobPumpCombMapper.queryPumbCombByJobCombMaterialLot(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getMaterialLotId());
        }

        if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombList)){
            //errorFlag为true是代表遍历完所有的子条码顺序，仍然未找到需要新增还是更新数据，此时报错扫描超出需求数,不允许扫描
            boolean errorFlag = true;
            for (long i = 1L; i <= hmeEoJobPumpCombList.get(0).getPumpReqQty(); i++) {
                long subBarcodeSeq = i;
                List<HmeEoJobPumpComb> seqEoJobPumpCombList = hmeEoJobPumpCombList.stream().filter(item -> subBarcodeSeq == item.getSubBarcodeSeq()).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(seqEoJobPumpCombList)){
                    HmeEoJobPumpComb hmeEoJobPumpComb = seqEoJobPumpCombList.get(0);
                    //如果找到，则看找到数据的material_lot_id是否为空
                    MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
                    if(StringUtils.isBlank(hmeEoJobPumpComb.getMaterialLotId())){
                        //如果为空，则更新数据，将扫描的泵浦源物料批ID、泵浦源物料ID记在material_lot_id、material_id上
                        hmeEoJobPumpComb.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        hmeEoJobPumpComb.setMaterialId(mtMaterialLot.getMaterialId());
                        hmeEoJobPumpCombMapper.updateByPrimaryKeySelective(hmeEoJobPumpComb);
                        result.setPrintFlag(HmeConstants.ConstantValue.YES);
                        result.setSubCode(hmeEoJobPumpComb.getSubBarcode());
                        errorFlag = false;
                        break;
                    } else {
                        // 20211206 add by sanfeng.zhang for wenxin.zhang 如果material_lot_id有值 则判断是否绑定工位关系（避免解绑了 组合还在的情况）
                        Long eoJobMaterialCount = hmeEoJobPumpCombMapper.queryEoJobMaterial(tenantId, hmeEoJobPumpComb.getMaterialLotId());
                        if (eoJobMaterialCount.compareTo(Long.valueOf(0)) <= 0) {
                            hmeEoJobPumpComb.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            hmeEoJobPumpComb.setMaterialId(mtMaterialLot.getMaterialId());
                            hmeEoJobPumpCombMapper.updateByPrimaryKeySelective(hmeEoJobPumpComb);
                            result.setPrintFlag(HmeConstants.ConstantValue.YES);
                            result.setSubCode(hmeEoJobPumpComb.getSubBarcode());
                            errorFlag = false;
                            break;
                        }
                    }
                }else {
                    //如果找不到，则新增一条子条码顺序为当前遍历i的数据
                    HmeEoJobPumpComb hmeEoJobPumpComb = new HmeEoJobPumpComb();
                    hmeEoJobPumpComb.setTenantId(tenantId);
                    hmeEoJobPumpComb.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobPumpComb.setWorkcellId(hmeEoJobSn.getWorkcellId());
                    hmeEoJobPumpComb.setEoId(hmeEoJobSn.getEoId());
                    hmeEoJobPumpComb.setCombMaterialLotId(hmeEoJobSn.getMaterialLotId());
                    hmeEoJobPumpComb.setCombMaterialId(hmeEoJobSnBatchVO8.getMaterialId());
                    hmeEoJobPumpComb.setPumpReqQty(hmeEoJobSnBatchVO8.getQty());
                    MtMaterialLot eoJonSnMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
                    hmeEoJobPumpComb.setSubBarcodeSeq(subBarcodeSeq);
                    hmeEoJobPumpComb.setSubBarcode(eoJonSnMaterialLot.getMaterialLotCode() + "-" + hmeEoJobPumpComb.getSubBarcodeSeq());
                    hmeEoJobPumpComb.setPrintTime(new Date());
                    MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
                    hmeEoJobPumpComb.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeEoJobPumpComb.setMaterialId(mtMaterialLot.getMaterialId());
                    this.insertSelective(hmeEoJobPumpComb);
                    result.setPrintFlag(HmeConstants.ConstantValue.YES);
                    result.setSubCode(hmeEoJobPumpComb.getSubBarcode());
                    errorFlag = false;
                    break;
                }
            }
            if(errorFlag){
                throw new MtException("HME_EO_JOB_SN_212", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_212", "HME"));
            }
        }else {
            //如果找不到，则新增一条子条码顺序为1的数据
            HmeEoJobPumpComb hmeEoJobPumpComb = new HmeEoJobPumpComb();
            hmeEoJobPumpComb.setTenantId(tenantId);
            hmeEoJobPumpComb.setJobId(hmeEoJobSn.getJobId());
            hmeEoJobPumpComb.setWorkcellId(hmeEoJobSn.getWorkcellId());
            hmeEoJobPumpComb.setEoId(hmeEoJobSn.getEoId());
            hmeEoJobPumpComb.setCombMaterialLotId(hmeEoJobSn.getMaterialLotId());
            hmeEoJobPumpComb.setCombMaterialId(hmeEoJobSnBatchVO8.getMaterialId());
            hmeEoJobPumpComb.setPumpReqQty(hmeEoJobSnBatchVO8.getQty());
            MtMaterialLot eoJonSnMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
            hmeEoJobPumpComb.setSubBarcodeSeq(1L);
            if(hmeEoJobPumpComb.getSubBarcodeSeq() > hmeEoJobSnBatchVO8.getQty()){
                throw new MtException("HME_EO_JOB_SN_212", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_212", "HME"));
            }
            hmeEoJobPumpComb.setSubBarcode(eoJonSnMaterialLot.getMaterialLotCode() + "-" + hmeEoJobPumpComb.getSubBarcodeSeq());
            hmeEoJobPumpComb.setPrintTime(new Date());
            MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
            hmeEoJobPumpComb.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobPumpComb.setMaterialId(mtMaterialLot.getMaterialId());
            this.insertSelective(hmeEoJobPumpComb);
            result.setPrintFlag(HmeConstants.ConstantValue.YES);
            result.setSubCode(hmeEoJobPumpComb.getSubBarcode());
        }
        return result;
    }

    @Override
    public void samePumpSelectionComposeVerify(Long tenantId, HmeEoJobSn hmeEoJobSn, MtMaterialLot mtMaterialLot) {
        //根据进站条码作为组合物料批，泵浦源物料批不为空在表hme_eo_job_pump_comb查找数据
        HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.getPumpCombByCombMaterialLot(tenantId, hmeEoJobSn.getMaterialLotId());
        if(Objects.isNull(hmeEoJobPumpComb)){
            //如果找不到，则代表进站的条码未绑定过泵浦源条码，然后看扫描的条码属于组合还是非组合
            //根据扫描的条码ID、状态为LOADED查询表hme_pump_pre_selection的组合物料
            String combMaterialId = hmeEoJobPumpCombMapper.getCombMaterialBySelectionDetailsMaterialLot(tenantId, mtMaterialLot.getMaterialLotId());
            if(StringUtils.isNotBlank(combMaterialId)){
                //如果找到，则代表扫描的条码属于组合，则需要校验进站条码的物料是否与组合物料相等
                MtMaterialLot siteInMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
                if(!siteInMaterialLot.getMaterialId().equals(combMaterialId)){
                    //不等则报错扫描物料批所属的组合物料【${1}】与进站SN的物料不一致。
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(combMaterialId);
                    throw new MtException("HME_EO_JOB_SN_233", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_233", "HME", mtMaterial.getMaterialCode()));
                }
            }
        }else {
            //如果找到，则代表进站的条码绑定过泵浦源条码，然后看扫描的条码属于组合还是非组合
            //根据找到数据的泵浦源物料批ID、状态为LOADED查询表hme_pump_selection_details的数据
            HmeEoJobPumpCombVO6 hmePumpSelectionDetails = hmeEoJobPumpCombMapper.getPumpSelectionDetailsByMaterialLotId(tenantId, hmeEoJobPumpComb.getMaterialLotId());
            if(Objects.nonNull(hmePumpSelectionDetails)){
                //如果找到，则代表扫描的条码属于组合，则需要再根据扫描条码ID、状态为LOADED查询数据，找到数据的头ID、筛选顺序必须相等
                HmeEoJobPumpCombVO6 siteInPumpSelectionDetails = hmeEoJobPumpCombMapper.getPumpSelectionDetailsByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
                if(Objects.isNull(siteInPumpSelectionDetails)
                        || !siteInPumpSelectionDetails.getPumpPreSelectionId().equals(hmePumpSelectionDetails.getPumpPreSelectionId())
                        || !siteInPumpSelectionDetails.getSelectionOrder().equals(hmePumpSelectionDetails.getSelectionOrder())){
                    //扫描的物料批与已扫描的不为同一个筛选组合,请检查
                    throw new MtException("HME_EO_JOB_SN_234", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_234", "HME"));
                }
            }
        }
    }

    /**
     * 对一个数组进行排列组合 如[1,2,3]得到[1,2,3] [1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]
     *
     * @param array 传入的数组
     * @param stack 栈对象
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 09:20:20
     * @return java.util.List<java.util.List<java.math.BigDecimal>>
     */
    public static void perm(BigDecimal[] array, Stack<BigDecimal> stack, List<List<BigDecimal>> permSingleResultBigList) {
        if(array.length <= 0) {
            //进入了叶子节点，输出栈中内容
            List<BigDecimal> singleResult = new LinkedList<>(stack);
            permSingleResultBigList.add(singleResult);
        } else {
            for (int i = 0; i < array.length; i++) {
                //tmepArray是一个临时数组，用于就是Ri
                //eg：1，2，3的全排列，先取出1，那么这时tempArray中就是2，3
                BigDecimal[] tempArray = new BigDecimal[array.length-1];
                System.arraycopy(array,0,tempArray,0,i);
                System.arraycopy(array,i+1,tempArray,i,array.length-i-1);
                stack.push(array[i]);
                perm(tempArray,stack, permSingleResultBigList);
                stack.pop();
            }
        }
    }
}
