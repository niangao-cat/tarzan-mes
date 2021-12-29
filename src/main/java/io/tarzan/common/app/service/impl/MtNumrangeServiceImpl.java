package io.tarzan.common.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeDTO;
import io.tarzan.common.app.service.MtNumrangeService;
import io.tarzan.common.domain.entity.MtNumrange;
import io.tarzan.common.domain.entity.MtNumrangeHis;
import io.tarzan.common.domain.entity.MtNumrangeRule;
import io.tarzan.common.domain.entity.MtNumrangeRuleHis;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeRuleVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO6;
import io.tarzan.common.domain.vo.MtNumrangeVO7;
import io.tarzan.common.infra.mapper.MtNumrangeMapper;
import io.tarzan.common.infra.mapper.MtNumrangeRuleMapper;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.repository.impl.MtEventRepositoryImpl;

/**
 * 号码段定义表应用服务默认实现
 *
 * @author yuan.yuan@hand-china.com 2019-08-16 15:32:44
 */
@Service
public class MtNumrangeServiceImpl implements MtNumrangeService {
    @Autowired
    MtEventRepositoryImpl mtEventRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtNumrangeMapper mtNumrangeMapper;
    @Autowired
    private MtNumrangeRuleMapper mtNumrangeRuleMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String numrangeSaveForUi(Long tenantId, MtNumrangeDTO dto) {
        // 校验唯一性
        MtNumrange temp = new MtNumrange();
        temp.setTenantId(tenantId);
        temp.setObjectId(dto.getObjectId());
        temp.setNumrangeGroup(dto.getNumrangeGroup());
        List<MtNumrange> numranges = mtNumrangeMapper.select(temp);
        if (CollectionUtils.isNotEmpty(numranges)) {
            if (StringUtils.isEmpty(dto.getNumrangeId())) {
                throw new MtException("MT_GENERAL_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0015", "GENERAL"));
            } else {
                if (!numranges.get(0).getNumrangeId().equals(dto.getNumrangeId())) {
                    throw new MtException("MT_GENERAL_0015", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0015", "GENERAL"));
                }
            }
        }
        // 校验外部数据值长度检验固定长度与上限仅允许输入一个
        if (CollectionUtils.isNotEmpty(dto.getRules()) && dto.getRules().stream()
                        .filter(t -> "6".equalsIgnoreCase(t.getNumRule()))
                        .anyMatch(c -> c.getIncomeValueLength() != null && c.getIncomeValueLengthLimit() != null)) {
            throw new MtException("MT_GENERAL_0064",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0064", "GENERAL"));
        }
        // 查找序列号规则为特定流水号
        Optional<MtNumrangeRuleVO2> mtNumrangeRule = dto.getRules().stream()
                        .filter(t -> t.getNumRule() != null && "2".equals(t.getNumRule())).findFirst();
        if (mtNumrangeRule.isPresent()) {
            //
            // 拆分特定对象关联框 ，判断关联规则必输 并且必须为5-标准对象或者 6-外部输入值
            // ------------------------------------------------------------------------------
            String numLevel = "SPECIFIC_OBJECT_NUM";
            if (mtNumrangeRule.get().getNumLevel().equals(numLevel)) {
                if (mtNumrangeRule.get().getNumConnectInputBox() == null
                                || mtNumrangeRule.get().getNumConnectInputBox() == 0) {
                    throw new MtException("MT_GENERAL_0042", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0042", "GENERAL"));
                }

                String numConnectInputBox = mtNumrangeRule.get().getNumConnectInputBox().toString();
                char[] stringArr = numConnectInputBox.toCharArray();
                for (char c : stringArr) {
                    String ruleIndex = String.valueOf(c);
                    if (StringUtils.isNotEmpty(ruleIndex)) {
                        int index = Integer.parseInt(ruleIndex) - 1;
                        MtNumrangeRule rule = dto.getRules().get(index);
                        boolean existed = rule == null || StringUtils.isEmpty(rule.getNumRule())
                                        || (!"5".equals(rule.getNumRule()) && !"6".equals(rule.getNumRule()));
                        if (existed) {
                            throw new MtException("MT_GENERAL_0043", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_GENERAL_0043", "GENERAL", ruleIndex));
                        }
                    }
                }
            }
        }

        // 1.生成事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("NUMRANGE_ASSIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, event);

        MtNumrange mtNumrange = new MtNumrange();
        MtNumrangeHis mtNumrangeHis = new MtNumrangeHis();
        List<String> sqlList = new ArrayList<>();
        //
        // hzero 新逻辑，前台输入框固定5个 固定传入5个规则
        // ------------------------------------------------------------------------------
        if (StringUtils.isEmpty(dto.getNumrangeId())) {
            // region 新增逻辑
            // region 1.1头信息数据
            mtNumrange.setObjectId(dto.getObjectId());
            mtNumrange.setNumrangeGroup(dto.getNumrangeGroup());
            mtNumrange.setNumDescription(dto.getNumDescription());
            mtNumrange.set_tls(dto.get_tls());
            mtNumrange.setEnableFlag(dto.getEnableFlag());
            mtNumrange.setOutsideNumFlag(dto.getOutsideNumFlag());
            mtNumrange.setNumExample(dto.getNumExample());
            mtNumrange.setNumrangeId(customDbRepository.getNextKey("mt_numrange_s"));
            mtNumrange.setCid(Long.valueOf(customDbRepository.getNextKey("mt_numrange_cid_s")));
            mtNumrange.setTenantId(tenantId);

            int count = 0;
            for (MtNumrangeRule ruleDto : dto.getRules()) {
                MtNumrangeRule rule = new MtNumrangeRule();
                BeanUtils.copyProperties(ruleDto, rule);
                count++;
                if (StringUtils.isNotEmpty(rule.getNumRule())) {
                    // region 1.2 依次记录行和行历史
                    MtNumrangeRuleHis ruleHis = new MtNumrangeRuleHis();
                    rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                    rule.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_rule_cid_s")));
                    rule.setTenantId(tenantId);
                    if (StringUtils.isEmpty(rule.getNumRadix())) {
                        rule.setNumRadix("");
                    }

                    switch (count) {
                        case 1:
                            mtNumrange.setInputBox1(rule.getNumrangeRuleId());
                            break;
                        case 2:
                            mtNumrange.setInputBox2(rule.getNumrangeRuleId());
                            break;
                        case 3:
                            mtNumrange.setInputBox3(rule.getNumrangeRuleId());
                            break;
                        case 4:
                            mtNumrange.setInputBox4(rule.getNumrangeRuleId());
                            break;
                        case 5:
                            mtNumrange.setInputBox5(rule.getNumrangeRuleId());
                            break;
                        default:
                            break;
                    }
                    BeanUtils.copyProperties(rule, ruleHis);
                    ruleHis.setEventId(eventId);
                    ruleHis.setNumrangeRuleHisId(this.customDbRepository.getNextKey("mt_numrange_rule_his_s"));
                    ruleHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_rule_his_cid_s")));
                    sqlList.addAll(customDbRepository.getInsertSql(rule));
                    sqlList.addAll(customDbRepository.getInsertSql(ruleHis));
                }
                // endregion
            }
            // region 1.3 记录头历史
            BeanUtils.copyProperties(mtNumrange, mtNumrangeHis);
            mtNumrangeHis.setEventId(eventId);
            mtNumrangeHis.setNumrangeHisId(this.customDbRepository.getNextKey("mt_numrange_his_s"));
            mtNumrangeHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_his_cid_s")));
            sqlList.addAll(customDbRepository.getInsertSql(mtNumrange));
            sqlList.addAll(customDbRepository.getInsertSql(mtNumrangeHis));
            // endregion
        } else {
            // 2.1 更新头信息
            mtNumrange.setNumrangeId(dto.getNumrangeId());
            mtNumrange.setTenantId(tenantId);
            mtNumrange = mtNumrangeMapper.selectOne(mtNumrange);
            mtNumrange.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_cid_s")));

            mtNumrange.setNumDescription(dto.getNumDescription());
            mtNumrange.set_tls(dto.get_tls());
            mtNumrange.setEnableFlag(dto.getEnableFlag());
            mtNumrange.setOutsideNumFlag(dto.getOutsideNumFlag());
            mtNumrange.setNumExample(dto.getNumExample());

            // 2.2 处理号码段规则行数据 判断行数据是否有新增,修改或者删除 给传进来的组合规则框匹配规则id
            int count = 0;
            for (MtNumrangeRuleVO2 ruleDto : dto.getRules()) {
                MtNumrangeRule rule = new MtNumrangeRule();
                BeanUtils.copyProperties(ruleDto, rule);
                String processFlag = "";
                count++;
                switch (count) {
                    case 1:
                        if (StringUtils.isEmpty(rule.getNumRule())) {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox1())) {
                                rule.setNumrangeRuleId(mtNumrange.getInputBox1());
                                rule.setTenantId(tenantId);
                                rule = mtNumrangeRuleMapper.selectOne(rule);
                                mtNumrange.setInputBox1("");
                                processFlag = "DELETE";
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox1())) {
                                processFlag = "UPDATE";
                                rule = refreshNumrangeRule(tenantId, mtNumrange.getInputBox1(), rule);
                            } else {
                                rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                                mtNumrange.setInputBox1(rule.getNumrangeRuleId());
                                processFlag = "INSERT";
                            }
                        }
                        break;
                    case 2:
                        if (StringUtils.isEmpty(rule.getNumRule())) {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox2())) {
                                rule.setNumrangeRuleId(mtNumrange.getInputBox2());
                                rule.setTenantId(tenantId);
                                rule = mtNumrangeRuleMapper.selectOne(rule);
                                mtNumrange.setInputBox2("");
                                processFlag = "DELETE";
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox2())) {
                                processFlag = "UPDATE";
                                rule = refreshNumrangeRule(tenantId, mtNumrange.getInputBox2(), rule);
                            } else {
                                rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                                mtNumrange.setInputBox2(rule.getNumrangeRuleId());
                                processFlag = "INSERT";
                            }
                        }
                        break;
                    case 3:
                        if (StringUtils.isEmpty(rule.getNumRule())) {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox3())) {
                                rule.setNumrangeRuleId(mtNumrange.getInputBox3());
                                rule.setTenantId(tenantId);
                                rule = mtNumrangeRuleMapper.selectOne(rule);
                                mtNumrange.setInputBox3("");
                                processFlag = "DELETE";
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox3())) {
                                processFlag = "UPDATE";
                                rule = refreshNumrangeRule(tenantId, mtNumrange.getInputBox3(), rule);
                            } else {
                                rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                                mtNumrange.setInputBox3(rule.getNumrangeRuleId());
                                processFlag = "INSERT";
                            }
                        }
                        break;
                    case 4:
                        if (StringUtils.isEmpty(rule.getNumRule())) {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox4())) {
                                rule.setNumrangeRuleId(mtNumrange.getInputBox4());
                                rule.setTenantId(tenantId);
                                rule = mtNumrangeRuleMapper.selectOne(rule);
                                mtNumrange.setInputBox4("");
                                processFlag = "DELETE";
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox4())) {
                                processFlag = "UPDATE";
                                rule = refreshNumrangeRule(tenantId, mtNumrange.getInputBox4(), rule);
                            } else {
                                rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                                mtNumrange.setInputBox4(rule.getNumrangeRuleId());
                                processFlag = "INSERT";
                            }
                        }
                        break;
                    case 5:
                        if (StringUtils.isEmpty(rule.getNumRule())) {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox5())) {
                                rule.setNumrangeRuleId(mtNumrange.getInputBox5());
                                rule.setTenantId(tenantId);
                                rule = mtNumrangeRuleMapper.selectOne(rule);
                                mtNumrange.setInputBox5("");
                                processFlag = "DELETE";
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtNumrange.getInputBox5())) {
                                processFlag = "UPDATE";
                                rule = refreshNumrangeRule(tenantId, mtNumrange.getInputBox5(), rule);
                            } else {
                                rule.setNumrangeRuleId(this.customDbRepository.getNextKey("mt_numrange_rule_s"));
                                mtNumrange.setInputBox5(rule.getNumrangeRuleId());
                                processFlag = "INSERT";
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (StringUtils.isNotEmpty(processFlag)) {
                    rule.setTenantId(tenantId);
                    rule.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_rule_cid_s")));
                    if (StringUtils.isEmpty(rule.getNumRadix())) {
                        rule.setNumRadix("");
                    }

                    MtNumrangeRuleHis ruleHis = new MtNumrangeRuleHis();
                    BeanUtils.copyProperties(rule, ruleHis);
                    ruleHis.setEventId(eventId);
                    ruleHis.setTenantId(tenantId);
                    ruleHis.setNumrangeRuleHisId(this.customDbRepository.getNextKey("mt_numrange_rule_his_s"));
                    ruleHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_rule_his_cid_s")));
                    ruleHis.setEventId(eventId);

                    switch (processFlag) {
                        case "UPDATE":
                            sqlList.addAll(customDbRepository.getFullUpdateSql(rule));
                            break;
                        case "INSERT":
                            sqlList.addAll(customDbRepository.getInsertSql(rule));
                            break;
                        case "DELETE":
                            sqlList.addAll(customDbRepository.getDeleteSql(rule));
                            break;
                        default:
                            break;
                    }
                    sqlList.addAll(customDbRepository.getInsertSql(ruleHis));
                }
            }

            BeanUtils.copyProperties(mtNumrange, mtNumrangeHis);
            mtNumrangeHis.setEventId(eventId);
            mtNumrangeHis.setNumrangeHisId(this.customDbRepository.getNextKey("mt_numrange_his_s"));
            mtNumrangeHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_numrange_his_cid_s")));
            mtNumrangeHis.setEventId(eventId);

            sqlList.addAll(customDbRepository.getFullUpdateSql(mtNumrange));
            sqlList.addAll(customDbRepository.getInsertSql(mtNumrangeHis));
        }

        try {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        } catch (DuplicateKeyException ex) {
            throw new MtException("MT_GENERAL_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0015", "GENERAL"));
        }

        return mtNumrange.getNumrangeId();
    }

    /**
     * 刷新号码段规则
     * 
     * @author benjamin
     * @date 2019/9/25 2:03 PM
     * @param tenantId 租户Id
     * @param originNumrangeRule MtNumrangeRule
     * @return MtNumrangeRule
     */
    private MtNumrangeRule refreshNumrangeRule(Long tenantId, String numrangeRuleId,
                                               MtNumrangeRule originNumrangeRule) {
        // 先查询对象，后更新
        MtNumrangeRule currentNumrangeRule = new MtNumrangeRule();
        currentNumrangeRule.setTenantId(tenantId);
        currentNumrangeRule.setNumrangeRuleId(numrangeRuleId);
        currentNumrangeRule = mtNumrangeRuleMapper.selectOne(currentNumrangeRule);

        currentNumrangeRule.setNumrangeRuleId(originNumrangeRule.getNumrangeRuleId());
        currentNumrangeRule.setNumRule(originNumrangeRule.getNumRule());

        currentNumrangeRule.setFixInput("");
        currentNumrangeRule.setNumLevel("");
        currentNumrangeRule.setNumConnectInputBox(null);
        currentNumrangeRule.setNumLowerLimit("");
        currentNumrangeRule.setNumUpperLimit("");
        currentNumrangeRule.setNumAlert("");
        currentNumrangeRule.setNumAlertType("");
        currentNumrangeRule.setNumRadix("");
        currentNumrangeRule.setNumIncrement(null);
        currentNumrangeRule.setNumCurrent("");
        currentNumrangeRule.setNumResetType("");
        currentNumrangeRule.setNumResetPeriod(null);
        currentNumrangeRule.setNumResetLastdate("");
        currentNumrangeRule.setDateFormat("");
        currentNumrangeRule.setTimeFormat("");
        currentNumrangeRule.setCallStandardObject("");
        currentNumrangeRule.setIncomeValueLength(null);
        currentNumrangeRule.setIncomeValueLengthLimit(null);

        switch (originNumrangeRule.getNumRule()) {
            case "1":
                currentNumrangeRule.setFixInput(originNumrangeRule.getFixInput());
                break;
            case "2":
                currentNumrangeRule.setNumLevel(originNumrangeRule.getNumLevel());
                currentNumrangeRule.setNumConnectInputBox(originNumrangeRule.getNumConnectInputBox());
                currentNumrangeRule.setNumLowerLimit(originNumrangeRule.getNumLowerLimit());
                currentNumrangeRule.setNumUpperLimit(originNumrangeRule.getNumUpperLimit());
                currentNumrangeRule.setNumAlert(originNumrangeRule.getNumAlert());
                currentNumrangeRule.setNumAlertType(originNumrangeRule.getNumAlertType());
                currentNumrangeRule.setNumRadix(originNumrangeRule.getNumRadix());
                currentNumrangeRule.setNumIncrement(originNumrangeRule.getNumIncrement());
                currentNumrangeRule.setNumCurrent(originNumrangeRule.getNumCurrent());
                currentNumrangeRule.setNumResetType(originNumrangeRule.getNumResetType());
                currentNumrangeRule.setNumResetPeriod(originNumrangeRule.getNumResetPeriod());
                currentNumrangeRule.setNumResetLastdate(originNumrangeRule.getNumResetLastdate());
                break;
            case "3":
                currentNumrangeRule.setDateFormat(originNumrangeRule.getDateFormat());
                break;
            case "4":
                currentNumrangeRule.setTimeFormat(originNumrangeRule.getTimeFormat());
                break;
            case "5":
                currentNumrangeRule.setCallStandardObject(originNumrangeRule.getCallStandardObject());
                break;
            case "6":
                currentNumrangeRule.setIncomeValueLength(originNumrangeRule.getIncomeValueLength());
                currentNumrangeRule.setIncomeValueLengthLimit(originNumrangeRule.getIncomeValueLengthLimit());
                break;
            default:
                break;
        }

        return currentNumrangeRule;
    }

    @Override
    public Page<MtNumrangeVO6> numrangeListUi(Long tenantId, String objectid, String numDescription,
                                              PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest,
                        () -> mtNumrangeMapper.queryNumrangeListForUi(tenantId, objectid, numDescription));
    }

    @Override
    public MtNumrangeDTO queryNumrangeForUi(Long tenantId, String numrangeId) {
        MtNumrangeDTO dto = new MtNumrangeDTO();
        MtNumrangeVO7 mtNumrange = mtNumrangeMapper.queryNumrangeForUi(tenantId, numrangeId);
        dto.setNumrangeId(mtNumrange.getNumrangeId());
        dto.setObjectId(mtNumrange.getObjectId());
        dto.setNumrangeGroup(mtNumrange.getNumrangeGroup());
        dto.setNumDescription(mtNumrange.getNumDescription());
        dto.setEnableFlag(mtNumrange.getEnableFlag());
        dto.setOutsideNumFlag(mtNumrange.getOutsideNumFlag());
        dto.setNumExample(mtNumrange.getNumExample());
        dto.setObjectCode(mtNumrange.getObjectCode());
        dto.setObjectName(mtNumrange.getObjectName());
        List<MtNumrangeRuleVO2> rules = new ArrayList<>();
        dto.setRules(rules);

        // 返回5个规则 没有的话返回空对象
        // 1.
        MtNumrangeRuleVO2 rule = new MtNumrangeRuleVO2();
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox1())) {
            rule = mtNumrangeRuleMapper.queryNumrangeRule(tenantId, mtNumrange.getInputBox1());
        }
        rules.add(rule);

        // 2.
        rule = new MtNumrangeRuleVO2();
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox2())) {
            rule = mtNumrangeRuleMapper.queryNumrangeRule(tenantId, mtNumrange.getInputBox2());
        }
        rules.add(rule);

        // 3.
        rule = new MtNumrangeRuleVO2();
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox3())) {
            rule = mtNumrangeRuleMapper.queryNumrangeRule(tenantId, mtNumrange.getInputBox3());
        }
        rules.add(rule);

        // 4.
        rule = new MtNumrangeRuleVO2();
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox4())) {
            rule = mtNumrangeRuleMapper.queryNumrangeRule(tenantId, mtNumrange.getInputBox4());
        }
        rules.add(rule);

        // 5.
        rule = new MtNumrangeRuleVO2();
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox5())) {
            rule = mtNumrangeRuleMapper.queryNumrangeRule(tenantId, mtNumrange.getInputBox5());
        }
        rules.add(rule);
        return dto;
    }
}
