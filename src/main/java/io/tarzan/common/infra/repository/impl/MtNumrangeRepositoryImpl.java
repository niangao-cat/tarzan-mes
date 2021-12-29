package io.tarzan.common.infra.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.entity.MtNumrange;
import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectNum;
import io.tarzan.common.domain.entity.MtNumrangeRule;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectNumRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.repository.MtNumrangeRuleRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtNumrangeRuleVO;
import io.tarzan.common.domain.vo.MtNumrangeVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO11;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO3;
import io.tarzan.common.domain.vo.MtNumrangeVO4;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import io.tarzan.common.infra.mapper.MtNumrangeAssignMapper;
import io.tarzan.common.infra.mapper.MtNumrangeMapper;
import io.tarzan.common.infra.mapper.MtNumrangeObjectNumMapper;
import io.tarzan.common.infra.mapper.MtNumrangeRuleMapper;

/**
 * 号码段定义表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Component
public class MtNumrangeRepositoryImpl extends BaseRepositoryImpl<MtNumrange> implements MtNumrangeRepository {

    private Logger logger = LoggerFactory.getLogger(MtNumrangeRepositoryImpl.class);

    @Autowired
    private MtNumrangeMapper mtNumrangeMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeRuleMapper mtNumrangeRuleMapper;

    @Autowired
    private MtNumrangeRuleRepository mtNumrangeRuleRepository;

    @Autowired
    private MtNumrangeAssignMapper mtNumrangeAssignMapper;

    @Autowired
    private MtNumrangeObjectNumMapper mtNumrangeObjectNumMapper;

    @Autowired
    private MtNumrangeObjectNumRepository mtNumrangeObjectNumRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    private Long decimalConversion(String str, String type) {
        Long result;
        switch (type) {
            case "HEXADECIMAL":
                result = Long.valueOf(str, 36);
                break;
            case "HEX":
                result = Long.valueOf(str, 16);
                break;
            case "OCTAL":
                result = Long.valueOf(str, 8);
                break;
            case "BINARY":
                result = Long.valueOf(str, 2);
                break;
            case "DECIMAL":
                result = Long.valueOf(str);
                break;
            default:
                result = Long.valueOf(0);
        }
        return result;
    };

    private String decimalConversionTo(Long str, String type) {
        String result;
        switch (type) {
            case "HEXADECIMAL":
                result = decimalToThirtySix(str.intValue()).toUpperCase();
                break;
            case "HEX":
                result = Long.toHexString(str).toUpperCase();
                break;
            case "OCTAL":
                result = Long.toOctalString(str).toUpperCase();
                break;
            case "BINARY":
                result = Long.toBinaryString(str).toUpperCase();
                break;
            case "DECIMAL":
                result = Long.toString(str).toUpperCase();
                break;
            default:
                result = null;
        }
        return result;
    }

    // 定义36进制数字
    private static final String X36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // private static HashMap<Character, Integer> thirysixToTen = createMapThirtysixToTen();
    // 拿到10进制转换36进制的值键对
    private static HashMap<Integer, Character> tenToThirtysix = createMapTenToThirtysix();
    // 定义静态进制数
    private static int BASE = 36;

    private static HashMap<Integer, Character> createMapTenToThirtysix() {
        HashMap<Integer, Character> map = new HashMap<Integer, Character>(0);
        for (int i = 0; i < X36.length(); i++) {
            // 0--0,... ..., 35 -- Z的对应存放进去
            map.put(i, X36.charAt(i));
        }
        return map;
    }

    /**
     * 用递归来实现10 to 36
     *
     * @param iSrc
     * @return
     */
    public static String decimalToThirtySix(int iSrc) {
        String result = "";
        int key;
        int value;

        key = iSrc / BASE;
        value = iSrc - key * BASE;
        if (key != 0) {
            result = result + decimalToThirtySix(key);
        }

        result = result + tenToThirtysix.get(value).toString();
        return result;
    }

    private static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                // 左补0
                sb.append("0").append(str);
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public MtNumrangeVO5 numrangeGenerate(Long tenantId, MtNumrangeVO2 dto) {
        // 1.参数合规性校验
        if (StringUtils.isEmpty(dto.getObjectCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "objectCode", "【API:numrangeGenerate】"));

        }

        // 2.将objectCode转换为objectId
        MtNumrangeObject mtNumrangeObject = new MtNumrangeObject();
        mtNumrangeObject.setObjectCode(dto.getObjectCode());
        // 仅会获取到一条数据
        List<String> list = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, mtNumrangeObject);
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("MT_GENERAL_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0021", "GENERAL", dto.getObjectCode(), "【API:numrangeGenerate】"));
        }

        String objectId = list.get(0);

        // 3.匹配号码段生成编码
        MtNumrangeAssign mtNumrangeAssign = new MtNumrangeAssign();
        mtNumrangeAssign.setSiteId(dto.getSiteId() == null ? "" : dto.getSiteId());
        mtNumrangeAssign.setObjectId(objectId);
        mtNumrangeAssign.setObjectTypeCode(dto.getObjectTypeCode() == null ? "" : dto.getObjectTypeCode());
        Optional<String> optional = mtNumrangeAssignMapper.selectForEmptyString(tenantId, mtNumrangeAssign).stream()
                        .map(MtNumrangeAssign::getNumrangeId).findFirst();

        logger.info("objectId:{}",objectId);
        if (!optional.isPresent()) {
            mtNumrangeAssign.setSiteId("");
            optional = mtNumrangeAssignMapper.selectForEmptyString(tenantId, mtNumrangeAssign).stream()
                            .map(MtNumrangeAssign::getNumrangeId).findFirst();
        }
        if (!optional.isPresent()) {
            mtNumrangeAssign.setObjectTypeCode("");
            mtNumrangeAssign.setSiteId(dto.getSiteId() == null ? "" : dto.getSiteId());
            optional = mtNumrangeAssignMapper.selectForEmptyString(tenantId, mtNumrangeAssign).stream()
                            .map(MtNumrangeAssign::getNumrangeId).findFirst();
        }
        if (!optional.isPresent()) {
            mtNumrangeAssign.setSiteId("");
            optional = mtNumrangeAssignMapper.selectForEmptyString(tenantId, mtNumrangeAssign).stream()
                            .map(MtNumrangeAssign::getNumrangeId).findFirst();
        }
        if (!optional.isPresent()) {
            throw new MtException("MT_GENERAL_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0023", "GENERAL", dto.getObjectCode(), "【API:numrangeGenerate】"));
        }

        MtNumrange mtNumrange1 = new MtNumrange();
        mtNumrange1.setNumrangeId(optional.get());
        mtNumrange1.setTenantId(tenantId);
        mtNumrange1 = mtNumrangeMapper.selectOne(mtNumrange1);

        if (mtNumrange1 == null) {
            throw new MtException("MT_GENERAL_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0023", "GENERAL", dto.getObjectCode(), "【API:numrangeGenerate】"));
        }
        if (!"Y".equals(mtNumrange1.getEnableFlag())) {
            throw new MtException("MT_GENERAL_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0017", "GENERAL", mtNumrange1.getNumrangeGroup(), "【API:numrangeGenerate】"));
        }

        MtNumrangeVO5 mtNumrangeVO5 = new MtNumrangeVO5();
        if ("Y".equals(mtNumrange1.getOutsideNumFlag())) {
            if (StringUtils.isEmpty(dto.getOutsideNum())) {
                throw new MtException("MT_GENERAL_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0035", "GENERAL", "outsideNum", "【API:numrangeGenerate】"));
            }
            mtNumrangeVO5.setNumber(dto.getOutsideNum());
            return mtNumrangeVO5;
        }

        StringBuffer result = new StringBuffer();
        Map<String, String> inputValue = new HashMap<String, String>(0);
        if (StringUtils.isNotEmpty(mtNumrange1.getInputBox1())) {
            inputValue.put("1", mtNumrange1.getInputBox1());
        }
        if (StringUtils.isNotEmpty(mtNumrange1.getInputBox2())) {
            inputValue.put("2", mtNumrange1.getInputBox2());
        }
        if (StringUtils.isNotEmpty(mtNumrange1.getInputBox3())) {
            inputValue.put("3", mtNumrange1.getInputBox3());
        }
        if (StringUtils.isNotEmpty(mtNumrange1.getInputBox4())) {
            inputValue.put("4", mtNumrange1.getInputBox4());
        }
        if (StringUtils.isNotEmpty(mtNumrange1.getInputBox5())) {
            inputValue.put("5", mtNumrange1.getInputBox5());
        }
        if (MapUtils.isEmpty(inputValue)) {
            return mtNumrangeVO5;
        }

        List<String> numrangeRuleIds =
                        inputValue.entrySet().stream().map(t -> t.getValue()).distinct().collect(Collectors.toList());
        List<MtNumrangeRule> numrangeRules =
                        this.mtNumrangeRuleMapper.queryNumrangeRuleByCondition(tenantId, numrangeRuleIds);
        if (CollectionUtils.isEmpty(numrangeRules)) {
            return mtNumrangeVO5;
        }

        Map<String, MtNumrangeRule> inputMap = new HashMap<String, MtNumrangeRule>(0);
        for (Entry<String, String> entry : inputValue.entrySet()) {
            Optional<MtNumrangeRule> numRangeOptional = numrangeRules.stream()
                            .filter(t -> t.getNumrangeRuleId().equals(entry.getValue())).findFirst();
            if (numRangeOptional.isPresent()) {
                inputMap.put(entry.getKey(), numRangeOptional.get());
            } else {
                inputMap.put(entry.getKey(), null);
            }
        }

        // 定义NumRule类型6的个数
        int numRule = 0;
        for (MtNumrangeRule mtNumrangeRule : numrangeRules) {
            if (null == mtNumrangeRule) {
                continue;
            }
            switch (mtNumrangeRule.getNumRule()) {
                case "1":
                    result.append(rule1(mtNumrangeRule));
                    break;
                case "2":
                    String numCurrent = null;
                    Integer numCurrentLength = 0;
                    String objectCombination = null;
                    String numrangeObjectNumId = null;
                    String numResetLastdate = null;
                    if ("OVERALL_SERIAL_NUM".equals(mtNumrangeRule.getNumLevel())) {
                        numCurrentLength = mtNumrangeRule.getNumCurrent().length();
                        numCurrent = currentIncr(tenantId, mtNumrange1.getNumrangeGroup(),
                                        mtNumrangeRule.getNumCurrent(), mtNumrangeRule.getNumRadix(),
                                        mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                        numCurrentLength, "【API:numrangeGenerate】");
                        numResetLastdate = mtNumrangeRule.getNumResetLastdate();
                    } else {
                        /**
                         * NumConnectInputBox至少有1个输入框的编号
                         */
                        char[] inputBoxIndexs = mtNumrangeRule.getNumConnectInputBox().toString().toCharArray();
                        List<MtNumrangeRule> mtNumrangeRules = new ArrayList<MtNumrangeRule>(inputBoxIndexs.length);
                        for (char i : inputBoxIndexs) {
                            mtNumrangeRules.add(inputMap.getOrDefault(String.valueOf(i), null));
                        }

                        objectCombination = specialIncr(tenantId, mtNumrangeRules, dto);

                        MtNumrangeObjectNum mtNumrangeObjectNum = new MtNumrangeObjectNum();
                        mtNumrangeObjectNum.setNumrangeId(mtNumrange1.getNumrangeId());
                        mtNumrangeObjectNum.setObjectCombination(objectCombination);
                        mtNumrangeObjectNum = this.mtNumrangeObjectNumMapper.selectOne(mtNumrangeObjectNum);

                        if (null == mtNumrangeObjectNum) {
                            numCurrent = currentIncr(tenantId, mtNumrange1.getNumrangeGroup(),
                                            mtNumrangeRule.getNumLowerLimit(), mtNumrangeRule.getNumRadix(),
                                            mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                            mtNumrangeRule.getNumLowerLimit().length(), "【API:numrangeGenerate】");
                            numCurrentLength = mtNumrangeRule.getNumLowerLimit().length();
                            numrangeObjectNumId = null;
                            numResetLastdate = null;
                        } else {
                            numCurrent = currentIncr(tenantId, mtNumrange1.getNumrangeGroup(),
                                            mtNumrangeObjectNum.getNumCurrent(), mtNumrangeRule.getNumRadix(),
                                            mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                            mtNumrangeObjectNum.getNumCurrent().length(), "【API:numrangeGenerate】");
                            numCurrentLength = mtNumrangeObjectNum.getNumCurrent().length();
                            numrangeObjectNumId = mtNumrangeObjectNum.getNumrangeObjectNumId();
                            numResetLastdate = mtNumrangeObjectNum.getNumResetLastdate();
                        }
                    }


                    if (!"NO-RESET".equals(mtNumrangeRule.getNumResetType())) {
                        Long num = mtNumrangeRule.getNumResetPeriod();
                        if(num == null){
                            throw new MtException("WMS_CREATE_BATCH_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_CREATE_BATCH_001", "WMS", dto.getObjectCode()));
                        }
                        Date now = new Date();
                        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
                        // 时间重置为第一天
                        Calendar cale = Calendar.getInstance();
                        if ("YEAR".equals(mtNumrangeRule.getNumResetType())) {
                            cale.add(Calendar.YEAR, 0);
                            cale.set(Calendar.DAY_OF_YEAR, 1);
                        }
                        if ("MONTH".equals(mtNumrangeRule.getNumResetType())) {
                            cale.add(Calendar.MONTH, 0);
                            cale.set(Calendar.DAY_OF_MONTH, 1);
                        }
                        if (StringUtils.isEmpty(numResetLastdate)) {
                            numResetLastdate = f.format(cale.getTime());
                        } else {
                            Date date1 = null;
                            try {
                                date1 = f.parse(numResetLastdate);
                            } catch (ParseException px) {
                                // 当查询数据的时间格式不正确时
                                throw new MtException("MT_GENERAL_0027",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_GENERAL_0027", "GENERAL", numResetLastdate,
                                                                "【API:numrangeGenerate】"));
                            }

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date1);
                            if ("YEAR".equals(mtNumrangeRule.getNumResetType())) {
                                int intervals = calendar.get(Calendar.YEAR) - cale.get(Calendar.YEAR);
                                if (intervals < 0) {
                                    calendar.setTime(cale.getTime());
                                    calendar.set(Calendar.YEAR, intervals);
                                    mtNumrangeRule.setNumResetLastdate(f.format(calendar.getTime()));
                                }
                                calendar.add(Calendar.YEAR, num.intValue());
                            }
                            if ("MONTH".equals(mtNumrangeRule.getNumResetType())) {
                                int intervals = calendar.get(Calendar.MONTH) - cale.get(Calendar.MONTH);
                                if (intervals < 0) {
                                    calendar.setTime(cale.getTime());
                                    calendar.set(Calendar.MONTH, intervals);
                                    mtNumrangeRule.setNumResetLastdate(f.format(calendar.getTime()));
                                }
                                calendar.add(Calendar.MONTH, num.intValue());
                            }
                            if ("WEEK-MON".equals(mtNumrangeRule.getNumResetType())) {
                                calendar.add(Calendar.WEEK_OF_MONTH, num.intValue());
                            }
                            if ("DAY".equals(mtNumrangeRule.getNumResetType())) {
                                calendar.add(Calendar.DAY_OF_MONTH, num.intValue());
                            }
                            Date resetTime = calendar.getTime();
                            if (now.after(resetTime)) {
                                // 重置时取下限数据为当前值
                                Long lowerLimitCurrent = decimalConversion(mtNumrangeRule.getNumLowerLimit(),
                                                mtNumrangeRule.getNumRadix());
                                String currentValue =
                                                decimalConversionTo(lowerLimitCurrent, mtNumrangeRule.getNumRadix());

                                numCurrent = addZeroForNum(currentValue, numCurrentLength);
                                numResetLastdate = f.format(cale.getTime());
                            }
                        }
                    }

                    // 预警类型进行号段剩余检查
                    String message = null;
                    Long numCurrentConvert = decimalConversion(numCurrent, mtNumrangeRule.getNumRadix());
                    Long upperLimit =
                                    decimalConversion(mtNumrangeRule.getNumUpperLimit(), mtNumrangeRule.getNumRadix());
                    Long alert = 0L;
                    if ("NUMBER".equals(mtNumrangeRule.getNumAlertType())) {
                        alert = decimalConversion(mtNumrangeRule.getNumAlert(), mtNumrangeRule.getNumRadix());
                    } else {
                        alert = Long.valueOf(mtNumrangeRule.getNumAlert());
                    }

                    if ("NUMBER".equals(mtNumrangeRule.getNumAlertType()) && numCurrentConvert.compareTo(alert) == 1) {
                        message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0018",
                                        "GENERAL", dto.getObjectTypeCode(), "【API:numrangeGenerate】");
                    }
                    if ("PERCENTAGE".equals(mtNumrangeRule.getNumAlertType()) && BigDecimal.valueOf(numCurrentConvert)
                                    .compareTo(new BigDecimal(alert).multiply(new BigDecimal(upperLimit)).divide(
                                                    new BigDecimal(100 + ""), 6, BigDecimal.ROUND_HALF_DOWN)) == 1) {
                        message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0018",
                                        "GENERAL", dto.getObjectTypeCode(), "【API:numrangeGenerate】");
                    }

                    if ("OVERALL_SERIAL_NUM".equals(mtNumrangeRule.getNumLevel())) {
                        mtNumrangeRule.setTenantId(tenantId);
                        mtNumrangeRule.setNumCurrent(numCurrent);
                        mtNumrangeRule.setNumResetLastdate(numResetLastdate);
                        mtNumrangeRuleRepository.updateByPrimaryKeySelective(mtNumrangeRule);
                    } else {
                        MtNumrangeObjectNum newMtNumrangeObjectNum = new MtNumrangeObjectNum();
                        newMtNumrangeObjectNum.setTenantId(tenantId);
                        newMtNumrangeObjectNum.setNumResetLastdate(numResetLastdate);
                        if (StringUtils.isEmpty(numrangeObjectNumId)) {
                            newMtNumrangeObjectNum.setNumrangeId(mtNumrange1.getNumrangeId());
                            newMtNumrangeObjectNum.setObjectCombination(objectCombination);
                            newMtNumrangeObjectNum.setNumCurrent(numCurrent);
                            this.mtNumrangeObjectNumRepository.insertSelective(newMtNumrangeObjectNum);
                        } else {
                            newMtNumrangeObjectNum.setNumrangeObjectNumId(numrangeObjectNumId);
                            newMtNumrangeObjectNum.setNumCurrent(numCurrent);
                            this.mtNumrangeObjectNumRepository.updateByPrimaryKeySelective(newMtNumrangeObjectNum);
                        }
                    }

                    mtNumrangeVO5.setWarningMessage(message);
                    result.append(numCurrent);
                    break;
                case "3":
                    result.append(rule3(mtNumrangeRule));
                    break;
                case "4":
                    result.append(rule4(mtNumrangeRule));
                    break;
                case "5":
                    result.append(rule5(tenantId, dto.getCallObjectCodeList(), mtNumrangeRule));
                    break;
                case "6":
                    result.append(rule6(tenantId, numRule, dto.getIncomingValueList(), mtNumrangeRule));
                    numRule += 1;
                    break;
                default:
                    break;
            }
        }

        mtNumrangeVO5.setNumber(result.toString());
        return mtNumrangeVO5;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.READ_COMMITTED)
    public MtNumrangeVO8 numrangeBatchGenerate(Long tenantId, MtNumrangeVO9 dto) {
        // 1.参数合规性校验
        if (StringUtils.isEmpty(dto.getObjectCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "objectCode", "【API:numrangeBatchGenerate】"));

        }
        if (!MtBaseConstants.YES_NO.contains(dto.getObjectNumFlag())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "objectNumFlag", "【API:numrangeBatchGenerate】"));
        }
        if (dto.getNumQty() == null) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "numQty", "【API:numrangeBatchGenerate】"));

        }
        if (dto.getNumQty() <= 0) {
            throw new MtException("MT_GENERAL_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0058", "GENERAL", "【API:numrangeBatchGenerate】"));

        }

        // 2.将objectCode转换为objectId
        MtNumrangeObject mtNumrangeObject = new MtNumrangeObject();
        mtNumrangeObject.setObjectCode(dto.getObjectCode());
        mtNumrangeObject.setTenantId(tenantId);
        mtNumrangeObject = mtNumrangeObjectRepository.selectOne(mtNumrangeObject);
        if (mtNumrangeObject == null) {
            throw new MtException("MT_GENERAL_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0021", "GENERAL", dto.getObjectCode(), "【API:numrangeBatchGenerate】"));
        }
        String objectId = mtNumrangeObject.getObjectId();

        // 3.匹配号码段生成编码
        dto.setSiteId(dto.getSiteId() == null ? "" : dto.getSiteId());
        dto.setObjectTypeCode(dto.getObjectTypeCode() == null ? "" : dto.getObjectTypeCode());

        MtNumrangeAssign mtNumrangeAssign = new MtNumrangeAssign();
        mtNumrangeAssign.setTenantId(tenantId);
        mtNumrangeAssign.setObjectId(objectId);
        List<MtNumrangeAssign> numrangeAssigns = mtNumrangeAssignMapper.select(mtNumrangeAssign);

        // a、先按对象类型与站点ID都有值的组合进行匹配
        Optional<MtNumrangeAssign> optional =
                numrangeAssigns.stream()
                        .filter(t -> dto.getSiteId().equalsIgnoreCase(t.getSiteId()) && dto
                                .getObjectTypeCode().equalsIgnoreCase(t.getObjectTypeCode()))
                        .findFirst();
        // b.其次按对象类型有值站点ID为空匹配
        if (!optional.isPresent()) {
            optional = numrangeAssigns.stream()
                    .filter(t -> "".equalsIgnoreCase(t.getSiteId())
                            && dto.getObjectTypeCode().equalsIgnoreCase(t.getObjectTypeCode()))
                    .findFirst();
        }
        // c.再按对象类型为空站点ID有值匹配
        if (!optional.isPresent()) {
            optional = numrangeAssigns.stream().filter(t -> dto.getSiteId().equalsIgnoreCase(t.getSiteId())
                    && "".equalsIgnoreCase(t.getObjectTypeCode())).findFirst();
        }
        // d.最后按对象类型与站点ID为空进行匹配。
        if (!optional.isPresent()) {
            optional = numrangeAssigns.stream().filter(
                    t -> "".equalsIgnoreCase(t.getSiteId()) && "".equalsIgnoreCase(t.getObjectTypeCode()))
                    .findFirst();
        }
        if (!optional.isPresent()) {
            throw new MtException("MT_GENERAL_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0023", "GENERAL", dto.getObjectCode(), "【API:numrangeBatchGenerate】"));
        }

        MtNumrange mtNumrange = new MtNumrange();
        mtNumrange.setNumrangeId(optional.get().getNumrangeId());
        mtNumrange.setTenantId(tenantId);
        mtNumrange = mtNumrangeMapper.selectOne(mtNumrange);
        if (mtNumrange == null) {
            throw new MtException("MT_GENERAL_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0023", "GENERAL", dto.getObjectCode(), "【API:numrangeBatchGenerate】"));
        }
        if (!MtBaseConstants.YES.equals(mtNumrange.getEnableFlag())) {
            throw new MtException("MT_GENERAL_0017",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0017", "GENERAL",
                            mtNumrange.getNumrangeGroup(), "【API:numrangeBatchGenerate】"));
        }
        // 外部输入参数返回列表
        MtNumrangeVO8 mtNumrangeVO8 = new MtNumrangeVO8();
        if (MtBaseConstants.YES.equals(mtNumrange.getOutsideNumFlag())) {
            if (CollectionUtils.isEmpty(dto.getOutsideNumList())) {
                throw new MtException("MT_GENERAL_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0035", "GENERAL", "outsideNum", "【API:numrangeBatchGenerate】"));
            }
            if (MtBaseConstants.NO.equalsIgnoreCase(dto.getObjectNumFlag())
                    && dto.getOutsideNumList().size() < dto.getNumQty()) {
                throw new MtException("MT_GENERAL_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0060", "GENERAL", "【API:numrangeBatchGenerate】"));
            }
            List<String> numberList = new ArrayList<>(dto.getNumQty().intValue());
            for (int i = 0; i < dto.getNumQty(); i++) {
                if (MtBaseConstants.NO.equalsIgnoreCase(dto.getObjectNumFlag())) {
                    numberList.addAll(dto.getOutsideNumList());
                    break;
                } else {
                    numberList.add(dto.getOutsideNumList().get(0));
                }
            }
            mtNumrangeVO8.setNumberList(numberList);
            return mtNumrangeVO8;
        }

        Map<String, String> inputValue = new HashMap<String, String>(0);
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox1())) {
            inputValue.put("1", mtNumrange.getInputBox1());
        }
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox2())) {
            inputValue.put("2", mtNumrange.getInputBox2());
        }
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox3())) {
            inputValue.put("3", mtNumrange.getInputBox3());
        }
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox4())) {
            inputValue.put("4", mtNumrange.getInputBox4());
        }
        if (StringUtils.isNotEmpty(mtNumrange.getInputBox5())) {
            inputValue.put("5", mtNumrange.getInputBox5());
        }
        if (MapUtils.isEmpty(inputValue)) {
            return mtNumrangeVO8;
        }

        List<String> numrangeRuleIds =
                inputValue.entrySet().stream().map(t -> t.getValue()).distinct().collect(Collectors.toList());
        List<MtNumrangeRule> numrangeRules =
                this.mtNumrangeRuleMapper.queryNumrangeRuleByCondition(tenantId, numrangeRuleIds);
        if (CollectionUtils.isEmpty(numrangeRules)) {
            return mtNumrangeVO8;
        }
        Map<String, List<MtNumrangeRule>> numRules =
                numrangeRules.stream().collect(Collectors.groupingBy(MtNumrangeRule::getNumRule));
        // 判断是否满足批量生成的规则
        List<String> ruleType = Arrays.asList("2", "5", "6");
        if (numRules.entrySet().stream().allMatch(c -> !ruleType.contains(c.getKey())) && dto.getNumQty() > 1) {
            throw new MtException("MT_GENERAL_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0055", "GENERAL", dto.getObjectCode(), "【API:numrangeBatchGenerate】"));
        }
        Boolean objectNumFlag = MtBaseConstants.YES.equalsIgnoreCase(dto.getObjectNumFlag());

        // 判断类型类型5,6存在时,传入的参数列表是否满足条件
        for (Entry<String, List<MtNumrangeRule>> c : numRules.entrySet()) {
            if ("5".equalsIgnoreCase(c.getKey()) && !objectNumFlag
                    && dto.getNumQty() > dto.getCallObjectCodeList().size()) {
                throw new MtException("MT_GENERAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0054", "GENERAL", "【API:numrangeBatchGenerate】"));
            } else if ("5".equalsIgnoreCase(c.getKey()) && objectNumFlag
                    && CollectionUtils.isEmpty(dto.getCallObjectCodeList())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0001", "GENERAL", "callObjectCodeList", "【API:numrangeBatchGenerate】"));
            }
            if ("6".equalsIgnoreCase(c.getKey()) && !objectNumFlag) {
                if (dto.getNumQty() > dto.getIncomingValueList().size()) {
                    throw new MtException("MT_GENERAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_GENERAL_0054", "GENERAL", "【API:numrangeBatchGenerate】"));
                }
                // 每一组中类型为6的输入参数列表是否满足条件
                if (dto.getIncomingValueList().stream()
                        .anyMatch(k -> c.getValue().size() > k.getIncomingValue().size())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "incomingValue", "【API:numrangeBatchGenerate】"));
                }
            } else if ("6".equalsIgnoreCase(c.getKey()) && objectNumFlag) {
                if (CollectionUtils.isEmpty(dto.getIncomingValueList())
                        || dto.getIncomingValueList().get(0).getIncomingValue().size() < c.getValue().size()) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "incomingValue", "【API:numrangeBatchGenerate】"));
                }
            }
        }

        Map<String, MtNumrangeRule> inputMap = new HashMap<String, MtNumrangeRule>(0);
        for (Entry<String, String> entry : inputValue.entrySet()) {
            Optional<MtNumrangeRule> numRangeOptional = numrangeRules.stream()
                    .filter(t -> t.getNumrangeRuleId().equals(entry.getValue())).findFirst();
            if (numRangeOptional.isPresent()) {
                inputMap.put(entry.getKey(), numRangeOptional.get());
            } else {
                inputMap.put(entry.getKey(), null);
            }
        }

        // 筛选出需要更新的numberRule
        List<MtNumrangeRule> forUpdateList = numrangeRules.stream().filter(c -> "2".equalsIgnoreCase(c.getNumRule()))
                .collect(Collectors.toList());
        // 添加行锁
        Map<String, MtNumrangeRule> numrangeRuleMap = new HashMap<>();
        List<MtNumrangeRule> ruleForUpdateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(forUpdateList)) {
            List<String> forUpdateIds =
                    forUpdateList.stream().map(MtNumrangeRule::getNumrangeRuleId).collect(Collectors.toList());
            ruleForUpdateList = this.mtNumrangeRuleMapper.selectForUpdates(tenantId, forUpdateIds);
            numrangeRuleMap = ruleForUpdateList.stream()
                    .collect(Collectors.toMap(MtNumrangeRule::getNumrangeRuleId, c -> c));
        }

        // 获取特殊流水号的object_num
        MtNumrangeObjectNum temp = new MtNumrangeObjectNum();
        temp.setTenantId(tenantId);
        temp.setNumrangeId(mtNumrange.getNumrangeId());
        List<MtNumrangeObjectNum> mtNumrangeObjectNums = this.mtNumrangeObjectNumMapper.select(temp);
        Map<String, MtNumrangeObjectNum> mtNumrangeObjectNumsMap = null;
        if (CollectionUtils.isNotEmpty(mtNumrangeObjectNums)) {
            mtNumrangeObjectNumsMap = mtNumrangeObjectNums.stream()
                    .collect(Collectors.toMap(t -> t.getObjectCombination(), t -> t));
        }
        // 初始化
        if (MapUtils.isEmpty(mtNumrangeObjectNumsMap)) {
            mtNumrangeObjectNumsMap = new HashMap<>(0);
        }

        // 批量获取Id、Cid
        List<String> mtNumrangeRuleCids =
                this.customDbRepository.getNextKeys("mt_numrange_rule_cid_s", ruleForUpdateList.size());
        List<String> mtNumrangeObjectNumIds =
                this.customDbRepository.getNextKeys("mt_numrange_object_num_s", dto.getNumQty().intValue());
        List<String> mtNumrangeObjectNumCids =
                this.customDbRepository.getNextKeys("mt_numrange_object_num_cid_s", dto.getNumQty().intValue());


        // 按照Sequence顺序进行排序取值
        List<MtNumrangeVO10> orderCallList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getCallObjectCodeList())) {
            orderCallList.addAll(dto.getCallObjectCodeList().stream().sorted(Comparator.comparing(t -> t.getSequence()))
                    .collect(Collectors.toList()));
        }

        List<MtNumrangeVO11> orderIncomingList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getIncomingValueList())) {
            orderIncomingList.addAll(dto.getIncomingValueList().stream()
                    .sorted(Comparator.comparing(t -> t.getSequence())).collect(Collectors.toList()));
        }

        // 共有变量
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date nowDate = new Date(System.currentTimeMillis());

        List<String> numberList = new ArrayList<>(dto.getNumQty().intValue());
        for (int i = 0; i < dto.getNumQty(); i++) {
            StringBuilder number = new StringBuilder();
            int numRule = 0;
            for (MtNumrangeRule mtNumrangeRule : numrangeRules) {
                if (null == mtNumrangeRule) {
                    continue;
                }
                MtNumrangeObjectNum numrangeObjectNum = null;
                switch (mtNumrangeRule.getNumRule()) {
                    case "1":
                        number.append(rule1(mtNumrangeRule));
                        break;
                    case "2":
                        String numCurrent = null;
                        Integer numCurrentLength = 0;
                        String objectCombination = null;
                        String numrangeObjectNumId = null;
                        String numResetLastdate = null;
                        mtNumrangeRule = numrangeRuleMap.get(mtNumrangeRule.getNumrangeRuleId());
                        if ("OVERALL_SERIAL_NUM".equals(mtNumrangeRule.getNumLevel())) {
                            numCurrentLength = mtNumrangeRule.getNumCurrent().length();
                            numCurrent = currentIncr(tenantId, mtNumrange.getNumrangeGroup(),
                                    mtNumrangeRule.getNumCurrent(), mtNumrangeRule.getNumRadix(),
                                    mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                    numCurrentLength, "【API:numrangeBatchGenerate】");
                            numResetLastdate = mtNumrangeRule.getNumResetLastdate();
                        } else {
                            /**
                             * NumConnectInputBox至少有1个输入框的编号
                             */
                            if (MapUtils.isNotEmpty(mtNumrangeObjectNumsMap)) {
                                char[] inputBoxIndexs = mtNumrangeRule.getNumConnectInputBox().toString().toCharArray();
                                List<MtNumrangeRule> mtNumrangeRules =
                                        new ArrayList<MtNumrangeRule>(inputBoxIndexs.length);
                                for (char k : inputBoxIndexs) {
                                    mtNumrangeRules.add(inputMap.getOrDefault(String.valueOf(k), null));
                                }
                                objectCombination = specialBatchIncr(tenantId, mtNumrangeRules, orderCallList,
                                        orderIncomingList, objectNumFlag, i);


                                if (mtNumrangeObjectNumsMap.get(objectCombination) == null) {
                                    // 取下限作为起始值
                                    Long aLong = decimalConversion(mtNumrangeRule.getNumLowerLimit(),
                                            mtNumrangeRule.getNumRadix());
                                    numCurrent = addZeroForNum(decimalConversionTo(aLong, mtNumrangeRule.getNumRadix()),
                                            mtNumrangeRule.getNumLowerLimit().length());
                                    numCurrentLength = mtNumrangeRule.getNumLowerLimit().length();
                                    numrangeObjectNumId = null;
                                    numResetLastdate = null;
                                } else {
                                    numrangeObjectNum = mtNumrangeObjectNumsMap.get(objectCombination);
                                    numCurrent = currentBatchIncr(tenantId, mtNumrange.getNumrangeGroup(),
                                            numrangeObjectNum.getNumCurrent(), mtNumrangeRule.getNumRadix(),
                                            mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                            numrangeObjectNum.getNumCurrent().length());
                                    numCurrentLength = numrangeObjectNum.getNumCurrent().length();
                                    numrangeObjectNumId = numrangeObjectNum.getNumrangeObjectNumId();
                                    numResetLastdate = numrangeObjectNum.getNumResetLastdate();
                                }
                            } else {
                                numCurrent = currentBatchIncr(tenantId, mtNumrange.getNumrangeGroup(),
                                        mtNumrangeRule.getNumLowerLimit(), mtNumrangeRule.getNumRadix(),
                                        mtNumrangeRule.getNumIncrement(), mtNumrangeRule.getNumUpperLimit(),
                                        mtNumrangeRule.getNumLowerLimit().length());
                                numCurrentLength = mtNumrangeRule.getNumLowerLimit().length();
                                numrangeObjectNumId = null;
                                numResetLastdate = null;
                            }
                        }
                        if (!"NO-RESET".equals(mtNumrangeRule.getNumResetType())) {
                            Long num = mtNumrangeRule.getNumResetPeriod();
                            Date now = new Date();
                            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");

                            // 时间重置为第一天
                            Calendar cale = Calendar.getInstance();
                            if ("YEAR".equals(mtNumrangeRule.getNumResetType())) {
                                cale.add(Calendar.YEAR, 0);
                                cale.set(Calendar.DAY_OF_YEAR, 1);
                            }
                            if ("MONTH".equals(mtNumrangeRule.getNumResetType())) {
                                cale.add(Calendar.MONTH, 0);
                                cale.set(Calendar.DAY_OF_MONTH, 1);
                            }
                            if (StringUtils.isEmpty(numResetLastdate)) {
                                numResetLastdate = f.format(cale.getTime());
                            } else {
                                Date date1 = null;
                                try {
                                    date1 = f.parse(numResetLastdate);
                                } catch (ParseException px) {
                                    // 当查询数据的时间格式不正确时
                                    throw new MtException("MT_GENERAL_0027",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                    "MT_GENERAL_0027", "GENERAL", numResetLastdate,
                                                    "【API:numrangeBatchGenerate】"));
                                }

                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(date1);
                                if ("YEAR".equals(mtNumrangeRule.getNumResetType())) {
                                    int intervals = calendar.get(Calendar.YEAR) - cale.get(Calendar.YEAR);
                                    if (intervals < 0) {
                                        calendar.setTime(cale.getTime());
                                        calendar.set(Calendar.YEAR, intervals);
                                        mtNumrangeRule.setNumResetLastdate(f.format(calendar.getTime()));
                                    }
                                    calendar.add(Calendar.YEAR, num.intValue());
                                }
                                if ("MONTH".equals(mtNumrangeRule.getNumResetType())) {
                                    int intervals = calendar.get(Calendar.MONTH) - cale.get(Calendar.MONTH);
                                    if (intervals < 0) {
                                        calendar.setTime(cale.getTime());
                                        calendar.set(Calendar.MONTH, intervals);
                                        mtNumrangeRule.setNumResetLastdate(f.format(calendar.getTime()));
                                    }
                                    calendar.add(Calendar.MONTH, num.intValue());
                                }
                                if ("WEEK-MON".equals(mtNumrangeRule.getNumResetType())) {
                                    calendar.add(Calendar.WEEK_OF_MONTH, num.intValue());
                                }
                                if ("DAY".equals(mtNumrangeRule.getNumResetType())) {
                                    calendar.add(Calendar.DAY_OF_MONTH, num.intValue());
                                }
                                Date resetTime = calendar.getTime();
                                if (now.after(resetTime)) {
                                    // 重置时取下限数据为当前值
                                    Long lowerLimitCurrent = decimalConversion(mtNumrangeRule.getNumLowerLimit(),
                                            mtNumrangeRule.getNumRadix());
                                    String currentValue = decimalConversionTo(lowerLimitCurrent,
                                            mtNumrangeRule.getNumRadix());

                                    numCurrent = addZeroForNum(currentValue, numCurrentLength);
                                    numResetLastdate = f.format(cale.getTime());
                                }
                            }
                        }

                        // 预警类型进行号段剩余检查
                        String message = null;
                        Long numCurrentConvert = decimalConversion(numCurrent, mtNumrangeRule.getNumRadix());
                        Long upperLimit = decimalConversion(mtNumrangeRule.getNumUpperLimit(),
                                mtNumrangeRule.getNumRadix());
                        Long alert = 0L;
                        if ("NUMBER".equals(mtNumrangeRule.getNumAlertType())) {
                            alert = decimalConversion(mtNumrangeRule.getNumAlert(), mtNumrangeRule.getNumRadix());
                        } else {
                            alert = Long.valueOf(mtNumrangeRule.getNumAlert());
                        }

                        if ("NUMBER".equals(mtNumrangeRule.getNumAlertType())
                                && numCurrentConvert.compareTo(alert) == 1) {
                            message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0018",
                                    "GENERAL", dto.getObjectTypeCode(), "【API:numrangeBatchGenerate】");
                        }
                        if ("PERCENTAGE".equals(mtNumrangeRule.getNumAlertType()) && BigDecimal
                                .valueOf(numCurrentConvert)
                                .compareTo(new BigDecimal(alert).multiply(new BigDecimal(upperLimit)).divide(
                                        new BigDecimal(100 + ""), 6,
                                        BigDecimal.ROUND_HALF_DOWN)) == 1) {
                            message = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0018",
                                    "GENERAL", dto.getObjectTypeCode(), "【API:numrangeBatchGenerate】");
                        }

                        if ("OVERALL_SERIAL_NUM".equals(mtNumrangeRule.getNumLevel())) {
                            mtNumrangeRule.setTenantId(tenantId);
                            mtNumrangeRule.setLastUpdateDate(nowDate);
                            mtNumrangeRule.setLastUpdatedBy(userId);
                            mtNumrangeRule.setNumCurrent(numCurrent);
                            mtNumrangeRule.setNumResetLastdate(numResetLastdate);
                        } else {
                            MtNumrangeObjectNum newMtNumrangeObjectNum = new MtNumrangeObjectNum();
                            newMtNumrangeObjectNum.setTenantId(tenantId);
                            newMtNumrangeObjectNum.setNumResetLastdate(numResetLastdate);
                            if (StringUtils.isEmpty(numrangeObjectNumId)) {
                                newMtNumrangeObjectNum.setNumrangeId(mtNumrange.getNumrangeId());
                                newMtNumrangeObjectNum.setNumrangeObjectNumId(mtNumrangeObjectNumIds.get(i));
                                newMtNumrangeObjectNum.setObjectCombination(objectCombination);
                                newMtNumrangeObjectNum.setObjectVersionNumber(1L);
                                newMtNumrangeObjectNum.setCreationDate(nowDate);
                                newMtNumrangeObjectNum.setCreatedBy(userId);
                                newMtNumrangeObjectNum.setNumCurrent(numCurrent);
                                newMtNumrangeObjectNum.setLastUpdateDate(nowDate);
                                newMtNumrangeObjectNum.setLastUpdatedBy(userId);
                                newMtNumrangeObjectNum.setCid(Long.valueOf(mtNumrangeObjectNumCids.get(i)));
                                mtNumrangeObjectNumsMap.put(objectCombination, newMtNumrangeObjectNum);
                            } else {
                                numrangeObjectNum.setTenantId(tenantId);
                                numrangeObjectNum.setNumrangeObjectNumId(numrangeObjectNumId);
                                numrangeObjectNum.setNumCurrent(numCurrent);
                                numrangeObjectNum.setNumResetLastdate(numResetLastdate);
                                numrangeObjectNum.setLastUpdateDate(nowDate);
                                numrangeObjectNum.setLastUpdatedBy(userId);
                                numrangeObjectNum.setObjectVersionNumber(
                                        numrangeObjectNum.getObjectVersionNumber() + 1L);
                                numrangeObjectNum.setCid(Long.valueOf(mtNumrangeObjectNumCids.get(i)));
                            }
                        }
                        mtNumrangeVO8.setWarningMessage(message);
                        number.append(numCurrent);
                        break;
                    case "3":
                        number.append(rule3(mtNumrangeRule));
                        break;
                    case "4":
                        number.append(rule4(mtNumrangeRule));
                        break;
                    case "5":
                        number.append(ruleBatch5(tenantId, orderCallList, mtNumrangeRule, objectNumFlag, i));
                        break;
                    case "6":
                        number.append(ruleBatch6(tenantId, numRule, orderIncomingList, mtNumrangeRule, objectNumFlag,
                                i));
                        numRule += 1;
                        break;
                    default:
                        break;
                }
            }
            numberList.add(number.toString());
        }
        mtNumrangeVO8.setNumberList(numberList);

        // 数据更新
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> mtNumrangeRuleList = new ArrayList<>(ruleForUpdateList.size());

        for (int i = 0; i < ruleForUpdateList.size(); i++) {
            ruleForUpdateList.get(i).setCid(Long.valueOf(mtNumrangeRuleCids.get(i)));
        }

        mtNumrangeRuleList.addAll(ruleForUpdateList);

        if (MapUtils.isNotEmpty(mtNumrangeObjectNumsMap)) {
            List<AuditDomain> mtNumrangeObjectNumList = new ArrayList<>(mtNumrangeObjectNumsMap.size());
            mtNumrangeObjectNumList.addAll(mtNumrangeObjectNumsMap.entrySet().stream().map(t -> t.getValue())
                    .collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(mtNumrangeObjectNumList)) {
                sqlList.addAll(customDbRepository.getReplaceSql(mtNumrangeObjectNumList));
            }
        }
        if (CollectionUtils.isNotEmpty(mtNumrangeRuleList)) {
            sqlList.addAll(customDbRepository.getReplaceSql(mtNumrangeRuleList));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return mtNumrangeVO8;
    }

    private String rule1(MtNumrangeRule mtNumrangeRule) {
        return mtNumrangeRule.getFixInput();
    }

    private String rule3(MtNumrangeRule mtNumrangeRule) {
        SimpleDateFormat format = new SimpleDateFormat(mtNumrangeRule.getDateFormat());
        return format.format(new Date());
    }

    private String rule4(MtNumrangeRule mtNumrangeRule) {
        SimpleDateFormat format = new SimpleDateFormat(mtNumrangeRule.getTimeFormat());
        return format.format(new Date());
    }

    private String rule5(Long tenantId, Map<String, String> callObjectCodeList, MtNumrangeRule mtNumrangeRule) {
        if (MapUtils.isEmpty(callObjectCodeList)) {
            throw new MtException("MT_GENERAL_0030",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0030", "GENERAL",
                                            mtNumrangeRule.getCallStandardObject(), "【API:numrangeGenerate】"));
        }

        String codeValue = callObjectCodeList.get(mtNumrangeRule.getCallStandardObject());
        if (StringUtils.isEmpty(codeValue)) {
            throw new MtException("MT_GENERAL_0030",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0030", "GENERAL",
                                            mtNumrangeRule.getCallStandardObject(), "【API:numrangeGenerate】"));
        }
        return codeValue;
    }

    private String ruleBatch5(Long tenantId, List<MtNumrangeVO10> callObjectCodeList, MtNumrangeRule mtNumrangeRule,
                              Boolean objectNumFlag, Integer index) {
        if (CollectionUtils.isEmpty(callObjectCodeList)) {
            throw new MtException("MT_GENERAL_0030",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0030", "GENERAL",
                                            mtNumrangeRule.getCallStandardObject(), "【API:numrangeBatchGenerate】"));
        }
        Map<String, String> callObjectCode = null;
        if (objectNumFlag) {
            callObjectCode = callObjectCodeList.get(0).getCallObjectCode();
        } else {
            callObjectCode = callObjectCodeList.get(index).getCallObjectCode();
        }
        if (MapUtils.isEmpty(callObjectCode)
                        || StringUtils.isEmpty(callObjectCode.get(mtNumrangeRule.getCallStandardObject()))) {
            throw new MtException("MT_GENERAL_0030",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0030", "GENERAL",
                                            mtNumrangeRule.getCallStandardObject(), "【API:numrangeBatchGenerate】"));
        }
        return callObjectCode.get(mtNumrangeRule.getCallStandardObject());
    }

    private String rule6(Long tenantId, int numRule, List<String> incomingValueList, MtNumrangeRule mtNumrangeRule) {
        if (CollectionUtils.isEmpty(incomingValueList) || numRule >= incomingValueList.size()
                        || StringUtils.isEmpty(incomingValueList.get(numRule))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "incomingValue", "【API:numrangeGenerate】"));
        }

        if (mtNumrangeRule.getIncomeValueLengthLimit() != null
                        && incomingValueList.get(numRule).length() > mtNumrangeRule.getIncomeValueLengthLimit()) {
            throw new MtException("MT_GENERAL_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0024", "GENERAL", "【API:numrangeGenerate】"));

        }

        if (mtNumrangeRule.getIncomeValueLength() != null
                        && incomingValueList.get(numRule).length() != mtNumrangeRule.getIncomeValueLength()) {
            throw new MtException("MT_GENERAL_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0063", "GENERAL", "【API:numrangeGenerate】"));

        }
        return incomingValueList.get(numRule);
    }

    private String ruleBatch6(Long tenantId, int numRule, List<MtNumrangeVO11> incomingValueList,
                              MtNumrangeRule mtNumrangeRule, Boolean objectNumFlag, Integer index) {
        if (CollectionUtils.isEmpty(incomingValueList)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "incomingValue", "【API:numrangeBatchGenerate】"));
        }
        List<String> incomingValue = null;
        if (objectNumFlag) {
            incomingValue = incomingValueList.get(0).getIncomingValue();
        } else {
            incomingValue = incomingValueList.get(index).getIncomingValue();
        }

        if (CollectionUtils.isEmpty(incomingValue) || numRule >= incomingValue.size()
                        || StringUtils.isEmpty(incomingValue.get(numRule))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "incomingValue", "【API:numrangeBatchGenerate】"));
        }
        if (mtNumrangeRule.getIncomeValueLengthLimit() != null
                        && incomingValue.get(numRule).length() > mtNumrangeRule.getIncomeValueLengthLimit()) {
            throw new MtException("MT_GENERAL_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0024", "GENERAL", "【API:numrangeBatchGenerate】"));

        }

        if (mtNumrangeRule.getIncomeValueLength() != null
                        && incomingValue.get(numRule).length() != mtNumrangeRule.getIncomeValueLength()) {
            throw new MtException("MT_GENERAL_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0063", "GENERAL", "【API:numrangeBatchGenerate】"));

        }
        return incomingValue.get(numRule);
    }

    private String currentIncr(Long tenantId, String numrangeGroup, String numCurrent, String numRadix,
                    Long numIncrement, String numUpperLimit, Integer length, String apiName) {
        Long temp = decimalConversion(numCurrent, numRadix);
        Long newCurrent = numIncrement == null ? Long.valueOf(0) : numIncrement + temp;
        Long upperLimit = decimalConversion(numUpperLimit, numRadix);

        // 序列号上限检查
        if (upperLimit < newCurrent) {
            throw new MtException("MT_GENERAL_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0036", "GENERAL", numrangeGroup, apiName));
        }

        // 获取转换进制之后值
        return addZeroForNum(decimalConversionTo(newCurrent, numRadix), length);
    }

    private String currentBatchIncr(Long tenantId, String numrangeGroup, String numCurrent, String numRadix,
                    Long numIncrement, String numUpperLimit, Integer length) {
        Long temp = decimalConversion(numCurrent, numRadix);
        Long newCurrent = numIncrement == null ? Long.valueOf(0) : numIncrement + temp;
        Long upperLimit = decimalConversion(numUpperLimit, numRadix);

        // 序列号上限检查
        if (upperLimit < newCurrent) {
            throw new MtException("MT_GENERAL_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0056", "GENERAL", numrangeGroup, "【API:numrangeBatchGenerate】"));
        }
        // 获取转换进制之后值
        return addZeroForNum(decimalConversionTo(newCurrent, numRadix), length);
    }

    private String specialIncr(Long tenantId, List<MtNumrangeRule> mtNumrangeRules, MtNumrangeVO2 dto) {
        StringBuffer result = new StringBuffer();
        int numRule = 0;
        for (MtNumrangeRule mtNumrangeRule : mtNumrangeRules) {
            switch (mtNumrangeRule.getNumRule()) {
                case "5":
                    result.append(rule5(tenantId, dto.getCallObjectCodeList(), mtNumrangeRule));
                    break;
                case "6":
                    result.append(rule6(tenantId, numRule, dto.getIncomingValueList(), mtNumrangeRule));
                    numRule += 1;
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    private String specialBatchIncr(Long tenantId, List<MtNumrangeRule> mtNumrangeRules,
                                    List<MtNumrangeVO10> callObjectCodeList, List<MtNumrangeVO11> incomingValueList,
                                    Boolean objectNumFlag, Integer index) {
        StringBuffer result = new StringBuffer();
        int numRule = 0;

        for (MtNumrangeRule mtNumrangeRule : mtNumrangeRules) {
            switch (mtNumrangeRule.getNumRule()) {
                case "5":
                    result.append(ruleBatch5(tenantId, callObjectCodeList, mtNumrangeRule, objectNumFlag, index));
                    break;
                case "6":
                    result.append(ruleBatch6(tenantId, numRule, incomingValueList, mtNumrangeRule, objectNumFlag,
                                    index));
                    numRule += 1;
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    @Override
    public MtNumrangeVO3 numrangePropertyQuery(Long tenantId, String numrangeId) {
        // 1.参数合规性校验
        if (StringUtils.isEmpty(numrangeId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "numrangeId", "【API:numrangePropertyQuery】"));
        }

        MtNumrangeVO3 mtNumrangeVO3 = new MtNumrangeVO3();

        MtNumrange mtNumrange = new MtNumrange();
        mtNumrange.setTenantId(tenantId);
        mtNumrange.setNumrangeId(numrangeId);
        mtNumrange = mtNumrangeMapper.selectOne(mtNumrange);
        if (mtNumrange != null) {
            // 2.获取查询得到对象代码和描述
            MtNumrangeObject mtNumrangeObject =
                            mtNumrangeObjectRepository.numrangeObjectGet(tenantId, mtNumrange.getObjectId());
            if (mtNumrangeObject != null) {
                mtNumrangeVO3.setObjectCode(mtNumrangeObject.getObjectCode());
                mtNumrangeVO3.setObjectDescription(mtNumrangeObject.getDescription());
            }

            mtNumrangeVO3.setObjectId(mtNumrange.getObjectId());
            mtNumrangeVO3.setNumrangeGroup(mtNumrange.getNumrangeGroup());
            mtNumrangeVO3.setNumDescription(mtNumrange.getNumDescription());
            mtNumrangeVO3.setNumExample(mtNumrange.getNumExample());
            mtNumrangeVO3.setOutsideNumFlag(mtNumrange.getOutsideNumFlag());
            mtNumrangeVO3.setEnableFlag(mtNumrange.getEnableFlag());

            List<String> ruleId = new ArrayList<>();
            ruleId.add(mtNumrange.getInputBox1());
            ruleId.add(mtNumrange.getInputBox2());
            ruleId.add(mtNumrange.getInputBox3());
            ruleId.add(mtNumrange.getInputBox4());
            ruleId.add(mtNumrange.getInputBox5());

            if (CollectionUtils.isNotEmpty(ruleId)) {
                List<MtNumrangeRule> mtNumrangeRules =
                                mtNumrangeRuleMapper.queryNumrangeRuleByCondition(tenantId, ruleId);
                if (CollectionUtils.isNotEmpty(mtNumrangeRules)) {
                    List<MtNumrangeRuleVO> lineList = new ArrayList<MtNumrangeRuleVO>();
                    MtNumrangeRuleVO mtNumrangeRuleVO = null;
                    for (MtNumrangeRule mtNumrangeRule : mtNumrangeRules) {
                        mtNumrangeRuleVO = new MtNumrangeRuleVO();
                        BeanUtils.copyProperties(mtNumrangeRule, mtNumrangeRuleVO);
                        lineList.add(mtNumrangeRuleVO);
                    }
                    mtNumrangeVO3.setLineList(lineList);
                }
            }
        }
        return mtNumrangeVO3;
    }

    @Override
    public List<String> numrangeIdQuery(Long tenantId, MtNumrangeVO4 dto) {
        MtNumrange mtNumrange = new MtNumrange();
        BeanUtils.copyProperties(dto, mtNumrange);
        if (StringUtils.isEmpty(dto.getObjectId()) && StringUtils.isNotEmpty(dto.getObjectCode())) {
            MtNumrangeObject mtNumrangeObject = new MtNumrangeObject();
            mtNumrangeObject.setObjectCode(dto.getObjectCode());
            List<String> list = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, mtNumrangeObject);
            if (CollectionUtils.isNotEmpty(list)) {
                // 仅会获取到一条数据
                mtNumrange.setObjectId(list.get(0));
            }
        }
        mtNumrange.setTenantId(tenantId);
        Criteria criteria = new Criteria(mtNumrange);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtNumrange.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNumrange.FIELD_OBJECT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNumrange.FIELD_NUMRANGE_GROUP, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNumrange.FIELD_NUM_DESCRIPTION, Comparison.LIKE));
        whereFields.add(new WhereField(MtNumrange.FIELD_OUTSIDE_NUM_FLAG, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNumrange.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        return mtNumrangeMapper.selectOptional(mtNumrange, criteria).stream().map(MtNumrange::getNumrangeId)
                        .collect(Collectors.toList());
    }
}
