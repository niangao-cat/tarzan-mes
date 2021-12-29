package io.tarzan.common.infra.repository.impl;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.code.DbType;
import io.choerodon.mybatis.domain.Config;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.choerodon.mybatis.helper.MapperHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtExtendTableDescRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtFieldsHelper;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import io.tarzan.common.infra.mapper.MtExtendTableDescMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtExtendSettingsRepositoryImpl extends BaseRepositoryImpl<MtExtendSettings>
                implements MtExtendSettingsRepository {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtExtendTableDescMapper mtExtendTableDescMapper;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MapperHelper mapperHelper;

    @Autowired
    private MtExtendTableDescRepository mtExtendTableDescRepository;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public List<MtExtendSettingsVO2> customAttrQuery(Long tenantId, String attrTable, String enableFlag) {

        if (StringUtils.isEmpty(attrTable)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "attrTable", "【API:customAttrQuery】"));
        }

        MtExtendSettingsVO2 tmp = new MtExtendSettingsVO2();
        tmp.setAttrTable(attrTable);
        tmp.setEnableFlag(enableFlag);
        return mtExtendSettingMapper.customAttrQuery(tenantId, tmp);
    }

    @Override
    public List<MtExtendAttrVO> attrPropertyQuery(Long tenantId, MtExtendVO dto) {
        if (StringUtils.isEmpty(dto.getTableName())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API:attrPropertyQuery】"));
        }

        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", "【API:attrPropertyQuery】"));
        }
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(dto.getTableName());
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", dto.getTableName(), "【API:attrPropertyQuery】"));
        }

        MtExtendSettings queryAttr = new MtExtendSettings();
        queryAttr.setTenantId(tenantId);
        queryAttr.setExtendTableDescId(tableInfo.getExtendTableDescId());
        queryAttr.setAttrName(dto.getAttrName());
        queryAttr.setEnableFlag("Y");
        List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(queryAttr);
        if (CollectionUtils.isEmpty(allSettings)) {
            return Collections.emptyList();
        }

        return mtExtendSettingMapper.attrPropertyQuery(tenantId, dto.getTableName(), tableInfo.getMainTableKey(),
                        dto.getKeyId(), allSettings);
    }

    @Override
    public List<MtExtendAttrVO1> attrPropertyBatchQuery(Long tenantId, MtExtendVO1 dto) {
        if (StringUtils.isEmpty(dto.getTableName())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tableName", "【API:attrPropertyBatchQuery】"));
        }

        if (CollectionUtils.isEmpty(dto.getKeyIdList()) || dto.getKeyIdList().size() != dto.getKeyIdList().stream()
                .filter(StringUtils::isNotEmpty).count()) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "keyIdList", "【API:attrPropertyBatchQuery】"));
        }

        // 报错改成MT_GENERAL_0066：扩展属性名不可为空! ${ attrPropertyBatchQuery }
        List<MtExtendVO5> dtoAttrs = dto.getAttrs();

        if (CollectionUtils.isNotEmpty(dtoAttrs) && dtoAttrs.size() != dtoAttrs.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getAttrName())).count()) {
            throw new MtException("MT_GENERAL_0066", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0066", "GENERAL", "【API:attrPropertyBatchQuery】"));
        }

        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(dto.getTableName());
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getExtendTableDescId())) {
            throw new MtException("MT_GENERAL_0065", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0065", "GENERAL", dto.getTableName(), "【API:attrPropertyBatchQuery】"));
        }

        MtExtendSettings queryAttr = new MtExtendSettings();
        queryAttr.setTenantId(tenantId);
        queryAttr.setExtendTableDescId(tableInfo.getExtendTableDescId());
        queryAttr.setEnableFlag("Y");
        List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(queryAttr);

        if (CollectionUtils.isEmpty(allSettings)) {
            return Collections.emptyList();
        }


        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingMapper.attrPropertyBatchQuery(tenantId,
                        dto.getTableName(), tableInfo.getMainTableKey(), dto.getKeyIdList());
        if (CollectionUtils.isEmpty(mtExtendAttrVO1s)) {
            return Collections.emptyList();
        }

        List<MtExtendAttrVO1> resultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(dtoAttrs)) {
            for (MtExtendVO5 dtoAttr : dtoAttrs) {
                for (MtExtendAttrVO1 vo1 : mtExtendAttrVO1s) {
                    if (dtoAttr.getAttrName().equals(vo1.getAttrName())
                                    && (dtoAttr.getAttrValue() == null
                                                    || dtoAttr.getAttrValue().equals(vo1.getAttrValue()))
                                    && (dtoAttr.getLang() == null || dtoAttr.getLang().equals(vo1.getLang()))) {
                        resultList.add(vo1);
                    }
                }
            }
        } else {
            resultList = mtExtendAttrVO1s;
        }

        return resultList;
    }

    @Override
    public List<MtExtendAttrVO2> attrTableWithLangQuery(Long tenantId, MtExtendVO dto) {
        if (StringUtils.isEmpty(dto.getTableName())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API:attrTableWithLangQuery】"));
        }

        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", "【API:attrTableWithLangQuery】"));
        }
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(dto.getTableName());
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", dto.getTableName(), "【API:attrTableWithLangQuery】"));
        }

        MtExtendSettings queryAttr = new MtExtendSettings();
        queryAttr.setTenantId(tenantId);
        queryAttr.setExtendTableDescId(tableInfo.getExtendTableDescId());
        queryAttr.setEnableFlag("Y");
        List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(queryAttr);
        if (CollectionUtils.isEmpty(allSettings)) {
            throw new MtException("MT_GENERAL_0003", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0003", "GENERAL", dto.getTableName(), "", "【API:attrTableWithLangQuery】"));
        }

        return mtExtendSettingMapper.attrTableWithLangQuery(tenantId, dto.getTableName(), tableInfo.getMainTableKey(),
                        dto.getKeyId(), allSettings);
    }

    @Override
    public List<String> attrPropertyLimitKidQuery(Long tenantId, MtExtendVO2 dto) {
        if (StringUtils.isEmpty(dto.getTableName())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API:attrPropertyLimitKidQuery】"));
        }
        if (StringUtils.isEmpty(dto.getAttrName())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "attrName", "【API:attrPropertyLimitKidQuery】"));
        }
        if (StringUtils.isEmpty(dto.getAttrValue())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "attrValue", "【API:attrPropertyLimitKidQuery】"));
        }

        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(dto.getTableName());
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", dto.getTableName(), "【API:attrPropertyLimitKidQuery】"));
        }

        MtExtendSettings setting = new MtExtendSettings();
        setting.setTenantId(tenantId);
        setting.setExtendTableDescId(tableInfo.getExtendTableDescId());
        setting.setAttrName(dto.getAttrName());
        setting.setEnableFlag("Y");
        setting = mtExtendSettingMapper.selectOne(setting);
        if (null == setting) {
            throw new MtException("MT_GENERAL_0003",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0003", "GENERAL",
                                            dto.getTableName(), dto.getAttrName(), "【API:attrPropertyLimitKidQuery】"));
        }

        return mtExtendSettingMapper.attrPropertyLimitKidQuery(tenantId, dto.getTableName(), setting.getAttrName(),
                        dto.getAttrValue(), tableInfo.getMainTableKey(), setting.getTlFlag());
    }

    @Override
    public List<String> attrBatchPropertyLimitKidQuery(Long tenantId, String tableName, Map<String, String> dto) {
        if (StringUtils.isEmpty(tableName)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API:attrBatchPropertyLimitKidQuery】"));
        }

        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrBatchPropertyLimitKidQuery】"));
        }

        List<String> attrNames = new ArrayList<String>(dto.keySet());
        List<MtExtendSettings> settingList = mtExtendSettingMapper.selectSettingsByProperty(tenantId,
                        tableInfo.getExtendTableDescId(), "Y", attrNames);
        if (CollectionUtils.isEmpty(settingList)) {
            throw new MtException("MT_GENERAL_0003",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0003", "GENERAL",
                                            tableName, attrNames.toString(), "【API:attrBatchPropertyLimitKidQuery】"));
        }

        List<MtExtendVO3> attrList = new ArrayList<MtExtendVO3>();
        for (Map.Entry<String, String> entry : dto.entrySet()) {
            MtExtendVO3 attr = new MtExtendVO3();
            attr.setAttrName(entry.getKey());
            attr.setAttrValue(entry.getValue());
            String tlFlag = null;
            Optional<MtExtendSettings> settingsOptional =
                            settingList.stream().filter(t -> entry.getKey().equals(t.getAttrName())).findFirst();
            if (settingsOptional.isPresent()) {
                tlFlag = settingsOptional.get().getTlFlag();
            } else {
                throw new MtException("MT_GENERAL_0003",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0003", "GENERAL",
                                                tableName, entry.getKey(), "【API:attrBatchPropertyLimitKidQuery】"));
            }
            attr.setTlFlag(tlFlag);
            attrList.add(attr);
        }

        return mtExtendSettingMapper.attrBatchPropertyLimitKidQuery(tenantId, tableName, tableInfo.getMainTableKey(),
                        attrList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> attrPropertyUpdate(Long tenantId, String tableName, String keyId, String eventId,
                    List<MtExtendVO5> dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(tableName)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API：attrTableUpdate】"));
        }
        if (StringUtils.isEmpty(keyId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", "【API：attrTableUpdate】"));
        }

        // Update by Xie yi yang in 2019/11/4
        // 2. 获取扩展表定义
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc extendTableDesc = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == extendTableDesc) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrPropertyUpdate】"));
        }

        // 返回的Id列表
        List<String> resultList = new ArrayList<>();

        // 准备需要执行的SQL语句
        List<String> sqlList = new ArrayList<>();

        // 共有变量
        String nowStr = DATE_FORMAT.format(new Date());
        String dbNow = customDbRepository.getDateSerializerSql(nowStr, false);

        Long userId = DetailsHelper.getUserDetails().getUserId();
        String keyHisId = null;

        if (CollectionUtils.isEmpty(dto)) {
            // update by chuang.yang 2019-09-23
            // 新增逻辑，如果未输入更新的扩展属性，则只生成全部扩展属性历史
            if (StringUtils.isNotEmpty(extendTableDesc.getHisAttrTable())) {
                // 根据输入参数tableName和输入参数keyId查询lastestHisId
                keyHisId = mtExtendTableDescMapper.selectLatestHisIdByMainT(tenantId, extendTableDesc.getMainTable(),
                                extendTableDesc.getMainTableKey(), keyId);
                if (StringUtils.isEmpty(eventId) && StringUtils.isNotEmpty(keyHisId)) {
                    // 获取事件ID
                    eventId = mtExtendTableDescMapper.selectEventIdByLatestHisId(tenantId,
                                    extendTableDesc.getHisTable(), extendTableDesc.getHisTableKey(), keyHisId);
                    if (StringUtils.isEmpty(eventId)) {
                        throw new MtException("MT_GENERAL_0061", mtErrorMessageService.getErrorMessageWithModule(
                                        tenantId, "MT_GENERAL_0061", "GENERAL", "【API:attrPropertyUpdate】"));
                    }
                }
                if (StringUtils.isNotEmpty(keyHisId)) {
                    MtExtendSettings mtExtendSettings = new MtExtendSettings();
                    mtExtendSettings.setExtendTableDescId(extendTableDesc.getExtendTableDescId());
                    List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(mtExtendSettings);
                    if (CollectionUtils.isEmpty(allSettings)) {
                        return resultList;
                    }

                    // 获取当前扩展属性表中已有的扩展属性
                    List<MtExtendAttrVO2> attrValueList = mtExtendSettingMapper.existAttrQuery(tenantId, tableName,
                                    extendTableDesc.getMainTableKey(), keyId, allSettings.stream()
                                                    .map(MtExtendSettings::getAttrName).collect(Collectors.toList()));

                    // 拼接生成历史数据Sql
                    for (MtExtendAttrVO2 attrValue : attrValueList) {
                        sqlList.add(getInsertAttrHisSql(tenantId, eventId, extendTableDesc.getHisAttrTable(),
                                        extendTableDesc.getHisTableKey(), keyHisId, attrValue.getAttrName(),
                                        attrValue.getAttrValue(), attrValue.getLang(), dbNow, userId));
                        resultList.add(attrValue.getAttrId());
                    }
                }
            }
        } else {
            // 如果输入更新的扩展属性，则更新对应扩展属性，然后记录历史
            // 传的attrName列表不能有空值
            if (CollectionUtils.isNotEmpty(dto.stream().filter(t -> StringUtils.isEmpty(t.getAttrName()))
                            .collect(Collectors.toList()))) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "attrName", "【API：attrPropertyUpdate】"));
            }

            List<String> attrNames = dto.stream().map(MtExtendVO5::getAttrName).collect(Collectors.toList());
            List<MtExtendSettings> settingList = mtExtendSettingMapper.selectSettingsByProperty(tenantId,
                            extendTableDesc.getExtendTableDescId(), "Y", attrNames);
            if (CollectionUtils.isEmpty(settingList)) {
                throw new MtException("MT_GENERAL_0010",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0010", "GENERAL",
                                                tableName, attrNames.toString(), "【API:attrPropertyUpdate】"));
            }

            // 如果存在历史表
            if (StringUtils.isNotEmpty(extendTableDesc.getHisAttrTable())) {
                keyHisId = mtExtendTableDescMapper.selectLatestHisIdByMainT(tenantId, extendTableDesc.getMainTable(),
                                extendTableDesc.getMainTableKey(), keyId);
                if (StringUtils.isEmpty(eventId) && StringUtils.isNotEmpty(keyHisId)) {
                    // 获取事件ID
                    eventId = mtExtendTableDescMapper.selectEventIdByLatestHisId(tenantId,
                                    extendTableDesc.getHisTable(), extendTableDesc.getHisTableKey(), keyHisId);
                    if (StringUtils.isEmpty(eventId)) {
                        throw new MtException("MT_GENERAL_0061", mtErrorMessageService.getErrorMessageWithModule(
                                        tenantId, "MT_GENERAL_0061", "GENERAL", "【API:attrPropertyUpdate】"));
                    }
                }
            }

            List<MtExtendVO4> attrList = new ArrayList<MtExtendVO4>();
            for (MtExtendVO5 entry : dto) {
                MtExtendVO4 attr = new MtExtendVO4();
                attr.setAttrName(entry.getAttrName());
                attr.setAttrValue(entry.getAttrValue());
                attr.setLang(entry.getLang());
                String tlFlag = null;
                Optional<MtExtendSettings> settingsOptional = settingList.stream()
                                .filter(t -> entry.getAttrName().equals(t.getAttrName())).findFirst();
                if (settingsOptional.isPresent()) {
                    tlFlag = settingsOptional.get().getTlFlag();
                } else {
                    throw new MtException("MT_GENERAL_0010",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0010",
                                                    "GENERAL", tableName, entry.getAttrName(),
                                                    "【API:attrPropertyUpdate】"));
                }
                attr.setTlFlag(tlFlag);
                attrList.add(attr);
            }

            // 获取当前扩展属性表中已有的扩展属性
            List<MtExtendAttrVO2> attrValue = mtExtendSettingMapper.existAttrQuery(tenantId, tableName,
                            extendTableDesc.getMainTableKey(), keyId,
                            attrList.stream().map(MtExtendVO4::getAttrName).collect(Collectors.toList()));

            // 获取系统支持的所有语言环境
            List<Language> languages = LanguageHelper.languages();

            for (MtExtendVO4 ever : attrList) {
                Optional<MtExtendAttrVO2> curAttrOp = null;
                if (CollectionUtils.isNotEmpty(settingList)) {
                    // 如果是多语言字段，同时传入LANG，则更新传入LANG的数据
                    // 如果是多语言字段，同时没有传入LANG，则更新当前语言环境的数据
                    // 如果不是多语言字段，则更新LANG为空字符串的数据
                    if ("Y".equals(ever.getTlFlag()) && StringUtils.isNotEmpty(ever.getLang())) {
                        // 找到当前扩展属性是否有值,只会有一个值
                        curAttrOp = attrValue.stream().filter(t -> ever.getAttrName().equals(t.getAttrName())
                                        && ever.getLang().equals(t.getLang())).findFirst();

                    } else if ("Y".equals(ever.getTlFlag()) && StringUtils.isEmpty(ever.getLang())) {
                        curAttrOp = attrValue.stream().filter(t -> ever.getAttrName().equals(t.getAttrName())
                                        && MtLanguageHelper.language().equals(t.getLang())).findFirst();
                    } else {
                        if (StringUtils.isNotEmpty(ever.getLang())) {
                            throw new MtException("MT_GENERAL_0057",
                                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0057",
                                                            "GENERAL", tableName, ever.getAttrName(),
                                                            "【API:attrPropertyUpdate】"));
                        }
                        curAttrOp = attrValue.stream().filter(t -> ever.getAttrName().equals(t.getAttrName())
                                        && StringUtils.isEmpty(t.getLang())).findFirst();
                    }
                }

                // 判断是否找到数据，如果找到说明需要更新，没找到需要新增
                if (curAttrOp != null && curAttrOp.isPresent()) {
                    // 获取更新扩展属性和新增扩展属性历史的SQL
                    List<String> updateSqlList = this.getUpdateAttrInfoSqlList(tenantId, eventId,
                                    extendTableDesc.getHisAttrTable(), tableName, extendTableDesc.getHisTableKey(),
                                    keyHisId, curAttrOp.get(), ever.getAttrValue(), dbNow, userId);

                    resultList.add(curAttrOp.get().getAttrId());
                    if (CollectionUtils.isNotEmpty(updateSqlList)) {
                        sqlList.addAll(updateSqlList);
                    }
                } else {
                    List<String> langs = new ArrayList<String>();
                    // 如果是多语言字段，同时传入LANG，则新增传入LANG的数据
                    // 如果是多语言字段，同时没有传入LANG，则新增所有语言环境的数据
                    // 如果不是多语言字段，则新增LANG为空字符串的数据
                    if ("Y".equals(ever.getTlFlag()) && StringUtils.isNotEmpty(ever.getLang())) {
                        langs.add(ever.getLang());
                    } else if ("Y".equals(ever.getTlFlag()) && StringUtils.isEmpty(ever.getLang())) {
                        for (Language language : languages) {
                            langs.add(language.getCode());
                        }
                    } else {
                        langs.add("");
                    }
                    // 获取新增扩展属性和扩展属性历史的SQL
                    MtExtendVO7 insertAttrInfoSqlList = this.getInsertAttrInfoSqlList(tenantId, eventId,
                                    extendTableDesc.getHisAttrTable(), tableName, extendTableDesc.getMainTableKey(),
                                    keyId, extendTableDesc.getHisTableKey(), keyHisId, ever.getAttrName(), langs,
                                    ever.getAttrValue(), dbNow, userId);
                    if (insertAttrInfoSqlList != null) {
                        if (CollectionUtils.isNotEmpty(insertAttrInfoSqlList.getSqlList())) {
                            sqlList.addAll(insertAttrInfoSqlList.getSqlList());
                        }
                        if (CollectionUtils.isNotEmpty(insertAttrInfoSqlList.getKidList())) {
                            resultList.addAll(insertAttrInfoSqlList.getKidList());
                        }
                    }
                }
            }
        }

        // 批量执行sql
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> attrPropertyBatchUpdate(Long tenantId, String tableName, String eventId,
                    List<MtCommonExtendVO6> dtoList) {
        String apiName = "【API：attrPropertyBatchUpdate】";

        // 0. basic check
        if (StringUtils.isEmpty(tableName)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", apiName));
        }
        if (CollectionUtils.isEmpty(dtoList) || dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getKeyId()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", apiName));
        }

        // 1. get extend table structure, throw exception if table is disabled or table doesn't have the
        // main key
        MtExtendRpcVO table = doGetExtendTable(tenantId, tableName, apiName);

        // 2 key id -> his id & event id
        Map<String, Pair<String, String>> idPairMap = new HashMap<>();

        // 2 valid event id if exists history attr table
        if (StringUtils.isNotEmpty(table.getHisAttrTable())) {
            List<String> keyIdList =
                            dtoList.stream().map(MtCommonExtendVO6::getKeyId).distinct().collect(Collectors.toList());
            idPairMap = getHisEventByKey(tenantId, keyIdList, table.getMainTable(), table.getMainTableKey(),
                            table.getHisTable(), table.getHisTableKey());
        }

        // get param which contains attrs
        List<MtCommonExtendVO6> existAttrParamList = dtoList.stream()
                        .filter(t -> CollectionUtils.isNotEmpty(t.getAttrs())).collect(Collectors.toList());
        // return if table doesn't contain columns
        List<MtExtendColumnRpcVO> columnList = table.getExtendColumnList().stream()
                        .filter(c -> MtBaseConstants.YES.equals(c.getEnableFlag())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(columnList)) {
            return Collections.emptyList();
        }

        // 3. throw exception if param attr name list contains disabled attr
        List<String> attrNameList =
                        columnList.stream().map(MtExtendColumnRpcVO::getAttrName).collect(Collectors.toList());
        Optional<String> attrNameOpt = existAttrParamList.stream().flatMap(t -> t.getAttrs().stream()
                        .filter(x -> !attrNameList.contains(x.getAttrName())).map(MtCommonExtendVO5::getAttrName))
                        .findFirst();
        if (attrNameOpt.isPresent()) {
            throw new MtException("MT_GENERAL_0010",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0010", "GENERAL",
                                            tableName, attrNameList.toString(), "【API:attrPropertyUpdate】"));
        }

        // 4. set tl flag for each attr column
        Map<String, MtExtendColumnRpcVO> columnMap =
                        columnList.stream().collect(Collectors.toMap(MtExtendColumnRpcVO::getAttrName, t -> t));
        existAttrParamList.forEach(
                        t -> t.getAttrs().forEach(x -> x.setTlFlag(columnMap.get(x.getAttrName()).getTlFlag())));

        // 5. get origin attr properties
        List<MtExtendAttrVO4> originAttrValueList = mtExtendSettingMapper.existAttrBatchQuery(tenantId, tableName,
                        table.getMainTableKey(), existAttrParamList);
        Map<Pair<String, String>, MtExtendAttrVO4> tripleAttrMap = originAttrValueList.stream()
                        .collect(Collectors.toMap(t -> Pair.of(t.getKeyId(), t.getAttrName()), t -> t));

        // 共有变量
        String dbNow = customDbRepository.getDateSerializerSql(DATE_FORMAT.format(new Date()), false);
        Long userId = MtUserClient.getCurrentUserId();

        List<String> resultList = new ArrayList<>();

        List<String> sqlList = new ArrayList<>();

        // 6. deal with params
        for (MtCommonExtendVO6 dto : existAttrParamList) {
            String hisKeyId = null;
            String curEventId = eventId;
            if (MapUtils.isNotEmpty(idPairMap)) {
                curEventId = null == eventId ? idPairMap.get(dto.getKeyId()).getRight() : eventId;
                hisKeyId = idPairMap.get(dto.getKeyId()).getLeft();
            }

            List<String> hisKids = null;
            List<String> hisCids = null;
            if (StringUtils.isNotEmpty(table.getHisAttrTable())) {
                hisKids = customDbRepository.getNextKeys(table.getHisAttrTable() + MtBaseConstants.PK_SUFFIX,
                                dto.getAttrs().size());
                hisCids = customDbRepository.getNextKeys(table.getHisAttrTable() + MtBaseConstants.CID_SUFFIX,
                                dto.getAttrs().size());
            }

            int count = 0;
            for (MtCommonExtendVO5 ever : dto.getAttrs()) {
                String hisKid = null == hisKids ? null : hisKids.get(count);
                Long hisCid = null == hisCids ? null : Long.valueOf(hisCids.get(count));
                MtExtendAttrVO4 inputAttrData = tripleAttrMap.get(Pair.of(dto.getKeyId(), ever.getAttrName()));

                if (inputAttrData != null) {
                    sqlList.addAll(getUpdateAttrInfoSqlList(tenantId, tableName, table.getHisAttrTable(),
                                    table.getHisTableKey(), hisKeyId, hisKid, hisCid, curEventId,
                                    inputAttrData.getAttrId(), inputAttrData.getAttrName(), ever.getAttrValue(),
                                    inputAttrData.getLang(), dbNow, userId));
                    resultList.add(inputAttrData.getAttrId());

                } else {
                    String attrKid = this.customDbRepository.getNextKey(tableName + MtBaseConstants.PK_SUFFIX);
                    String cid = this.customDbRepository.getNextKey(tableName + MtBaseConstants.CID_SUFFIX);
                    sqlList.addAll(getInsertAttrInfoSqlList(tenantId, tableName, table.getMainTableKey(),
                                    table.getHisAttrTable(), table.getHisTableKey(), attrKid, Long.valueOf(cid), hisKid,
                                    hisCid, eventId, dto.getKeyId(), hisKeyId, ever.getAttrName(), ever.getAttrValue(),
                                    "", dbNow, userId));
                    resultList.add(attrKid);

                }
                count++;
            }
        }

        // 7 deal with empty-attrs params
        List<MtCommonExtendVO6> noAttrDtoList = dtoList.stream().filter(t -> CollectionUtils.isEmpty(t.getAttrs()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(noAttrDtoList) && StringUtils.isNotEmpty(table.getHisTable())) {
            originAttrValueList = mtExtendSettingMapper.existAttrBatchQuery(tenantId, tableName,
                            table.getMainTableKey(), noAttrDtoList);
            Map<String, List<MtExtendAttrVO4>> originAttrValueMap =
                            originAttrValueList.stream().collect(Collectors.groupingBy(MtExtendAttrVO4::getKeyId));
            List<String> hisKids = customDbRepository.getNextKeys(table.getHisAttrTable() + MtBaseConstants.PK_SUFFIX,
                            originAttrValueList.size());
            List<String> hisCids = customDbRepository.getNextKeys(table.getHisAttrTable() + MtBaseConstants.CID_SUFFIX,
                            originAttrValueList.size());
            int count = 0;
            for (MtCommonExtendVO6 dto : noAttrDtoList) {
                String hisKeyId = null;
                String curEventId = eventId;
                if (MapUtils.isNotEmpty(idPairMap)) {
                    if (StringUtils.isEmpty(eventId)) {
                        curEventId = null == eventId ? idPairMap.get(dto.getKeyId()).getRight() : eventId;
                    }
                    hisKeyId = idPairMap.get(dto.getKeyId()).getLeft();
                }
                List<MtExtendAttrVO4> curAttrValueList = originAttrValueMap.get(dto.getKeyId());
                // *check if exists his table
                if (CollectionUtils.isNotEmpty(curAttrValueList) && StringUtils.isNotEmpty(hisKeyId)) {
                    for (MtExtendAttrVO4 attrValue : curAttrValueList) {
                        sqlList.add(getInsertAttrHisSql(tenantId, curEventId, table.getHisAttrTable(),
                                        table.getHisTableKey(), hisKeyId, attrValue.getAttrName(),
                                        attrValue.getAttrValue(), attrValue.getLang(), dbNow, userId,
                                        hisKids.get(count), Long.valueOf(hisCids.get(count))));
                        count++;
                    }

                    resultList.addAll(curAttrValueList.stream().map(MtExtendAttrVO4::getAttrId)
                                    .collect(Collectors.toList()));
                }

            }
        }

        // 批量执行sql
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    @Override
    public List<String> attrHisBatchUpdate(Long tenantId, String tableName, Map<String, String> dto, String eventId) {
        // 准备需要执行的SQL语句
        List<String> sqlList = new ArrayList<>();
        // 共有变量
        String nowStr = DATE_FORMAT.format(new Date());
        String dbNow = customDbRepository.getDateSerializerSql(nowStr, false);

        Long userId = -1L;
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc extendTableDesc = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == extendTableDesc || StringUtils.isEmpty(extendTableDesc.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrPropertyUpdate】"));
        }
        if (StringUtils.isNotEmpty(extendTableDesc.getHisAttrTable())) {

            // 获取扩展属性
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setExtendTableDescId(extendTableDesc.getExtendTableDescId());
            List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(mtExtendSettings);
            if (CollectionUtils.isEmpty(allSettings)) {
                return sqlList;
            }

            // 获取扩展属性值
            List<String> kyeIds = new ArrayList<>(dto.keySet());
            String whereInValuesSql = StringHelper.getWhereInValuesSql(extendTableDesc.getMainTableKey(), kyeIds, 1000);
            List<MtExtendAttrVO3> attrValue = mtExtendSettingMapper.existAttrQueryForKeyId(tenantId, tableName,
                            extendTableDesc.getMainTableKey(), whereInValuesSql);

            // 拼接生成历史数据Sql
            for (MtExtendAttrVO3 attrVO3 : attrValue) {
                attrVO3.setHisTableKeyValue(dto.get(attrVO3.getMainTableKeyValue()));
                // 拼接生成历史数据Sql
                sqlList.add(getInsertAttrHisSql(tenantId, eventId, extendTableDesc.getHisAttrTable(),
                                extendTableDesc.getHisTableKey(), attrVO3.getHisTableKeyValue(), attrVO3.getAttrName(),
                                attrVO3.getAttrValue(), attrVO3.getLang(), dbNow, userId));
            }
        }
        return sqlList;
    }

    /**
     * 生成插入扩展表的SQL语句和插入历史表的SQL语句
     */
    private MtExtendVO7 getInsertAttrInfoSqlList(Long tenantId, String eventId, String hisAttrTable, String tableName,
                    String mainTableKey, String keyId, String hisTableKey, String hisKeyId, String attrName,
                    List<String> langs, String attrValue, String dbNow, Long userId) {
        List<String> sqlList = new ArrayList<String>();
        List<String> kidList = new ArrayList<String>();
        String value = StringUtils.isEmpty(attrValue) ? "" : attrValue;
        for (String lang : langs) {
            String kid = this.customDbRepository.getNextKey(tableName + "_s");
            Long cid = Long.valueOf(this.customDbRepository.getNextKey(tableName + "_cid_s"));
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(tableName).append(" ");
            sql.append("(CREATED_BY,LAST_UPDATED_BY,CID,ATTR_ID,TENANT_ID,");
            sql.append(mainTableKey);
            sql.append(",ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES (");
            sql.append(userId).append(",").append(userId).append(",");
            sql.append(cid).append(",'").append(kid).append("','").append(tenantId).append("','").append(keyId)
                            .append("','");
            sql.append(attrName).append("','").append(value).append("','").append(lang).append("',");
            sql.append(dbNow).append(",").append(dbNow);
            sql.append(")");
            // 填充扩展表的SQL
            sqlList.add(sql.toString());
            kidList.add(kid);
            // 准备拼接历史表SQL
            if (StringUtils.isNotEmpty(hisAttrTable)) {
                // 填充扩展表历史的SQL
                sqlList.add(this.getInsertAttrHisSql(tenantId, eventId, hisAttrTable, hisTableKey, hisKeyId, attrName,
                                value, lang, dbNow, userId));
            }
        }
        MtExtendVO7 result = new MtExtendVO7();
        result.setSqlList(sqlList);
        result.setKidList(kidList);
        return result;
    }

    /**
     * 生成更新扩展表的SQL语句和插入历史表的SQL语句
     */
    private List<String> getUpdateAttrInfoSqlList(Long tenantId, String eventId, String hisTable, String tableName,
                    String hisTableKey, String hisKeyId, MtExtendAttrVO2 attrs, String attrValue, String dbNow,
                    Long userId) {
        List<String> sqlList = new ArrayList<String>();
        String value = StringUtils.isEmpty(attrValue) ? "" : attrValue;

        Long cid = Long.valueOf(this.customDbRepository.getNextKey(tableName + "_cid_s"));
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET ATTR_VALUE = '").append(value).append("',");
        sql.append("CID =").append(cid).append(",");
        sql.append("LAST_UPDATE_DATE =").append(dbNow).append(",");
        sql.append("LAST_UPDATED_BY = ").append(userId).append(" ");
        sql.append(" WHERE ATTR_ID ='").append(attrs.getAttrId()).append("'");
        sql.append(" AND TENANT_ID =").append(tenantId);
        // 填充扩展表更新的SQL
        sqlList.add(sql.toString());
        if (StringUtils.isNotEmpty(hisTable)) {
            // 填充扩展表历史的SQL
            sqlList.add(this.getInsertAttrHisSql(tenantId, eventId, hisTable, hisTableKey, hisKeyId,
                            attrs.getAttrName(), value, attrs.getLang(), dbNow, userId));
        }
        return sqlList;
    }

    /**
     * 生成插入历史表的SQL语句
     */
    private String getInsertAttrHisSql(Long tenantId, String eventId, String hisAttrTable, String hisTableKey,
                    String hisKeyId, String attrName, String attrValue, String lang, String dbNow, Long userId) {
        if (StringUtils.isNotEmpty(hisKeyId)) {
            String hisKid = this.customDbRepository.getNextKey(hisAttrTable + "_s");
            Long hisCid = Long.valueOf(this.customDbRepository.getNextKey(hisAttrTable + "_cid_s"));
            StringBuilder hisSql = new StringBuilder();
            hisSql.append("INSERT INTO ").append(hisAttrTable).append(" ");
            hisSql.append("(CREATED_BY,LAST_UPDATED_BY,CID,ATTR_HIS_ID,EVENT_ID,TENANT_ID,");
            hisSql.append(hisTableKey).append(",");
            hisSql.append("ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES (");
            hisSql.append(userId).append(",").append(userId).append(",").append(hisCid).append(",'");
            hisSql.append(hisKid).append("','").append(eventId).append("','").append(tenantId).append("','");
            hisSql.append(hisKeyId).append("','");
            hisSql.append(attrName).append("','").append(attrValue).append("','").append(lang).append("',");
            hisSql.append(dbNow).append(",").append(dbNow);
            hisSql.append(")");

            return hisSql.toString();
        } else {
            return null;
        }
    }

    @Override
    public List<MtExtendAttrHisVO> attrHisBatchQuery(Long tenantId, List<String> eventIds, String tableName) {
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrHisBatchQuery】"));
        }

        if (StringUtils.isEmpty(tableInfo.getHisAttrTable())) {
            throw new MtException("MT_GENERAL_0028", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0028", "GENERAL", tableName, "【API:attrHisBatchQuery】"));
        }

        return mtExtendSettingMapper.attrHisBatchQuery(tenantId, tableInfo.getHisAttrTable(),
                        tableInfo.getMainTableKey(), eventIds);
    }

    @Override
    public List<MtExtendAttrHisVO> attrHisQuery(Long tenantId, MtExtendAttrHisVO2 dto, String tableName) {
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrHisQuery】"));
        }

        if (StringUtils.isEmpty(tableInfo.getHisAttrTable())) {
            throw new MtException("MT_GENERAL_0028", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0028", "GENERAL", tableName, "【API:attrHisQuery】"));
        }

        return mtExtendSettingMapper.attrHisQuery(tenantId, tableInfo.getHisAttrTable(), tableInfo.getMainTableKey(),
                        dto);
    }

    @Override
    public List<MtExtendAttrVO3> attrDataQuery(Long tenantId, String tableName, String mainTableKey,
                    List<String> kids) {
        return this.mtExtendSettingMapper.attrDataQuery(tenantId, tableName, mainTableKey, kids);
    }

    @Override
    public String getInsertAttrSql(Long tenantId, String tableName, String mainTableKey, String keyId, String attrName,
                    String attrValue, String lang, Date now, Long userId) {
        String nowStr = DATE_FORMAT.format(now);
        String value = StringUtils.isEmpty(attrValue) ? "" : attrValue;
        String dbNow = customDbRepository.getDateSerializerSql(nowStr, false);

        String kid = this.customDbRepository.getNextKey(tableName + "_s");
        Long cid = Long.valueOf(this.customDbRepository.getNextKey(tableName + "_cid_s"));

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" ");
        sql.append("(TENANT_ID,CREATED_BY,LAST_UPDATED_BY,CID,ATTR_ID,");
        sql.append(mainTableKey);
        sql.append(",ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES (");
        sql.append(tenantId).append(",").append(userId).append(",").append(userId).append(",").append(cid).append(",'")
                        .append(kid).append("','");
        sql.append(keyId).append("','");
        sql.append(attrName).append("','").append(value).append("','").append(lang).append("',").append(dbNow)
                        .append(",").append(dbNow);
        sql.append(")");
        return sql.toString();
    }

    @Override
    public List<MtExtendVO9> propertyLimitAttrPropertyQuery(Long tenantId, MtExtendVO8 mtExtendVO8) {
        // 获取当前语言
        String currentLanguage = MtLanguageHelper.language();

        // 1.获取指定扩展属性表已设置的扩展字段
        List<MtExtendSettingsVO2> extendSettingsVO2s =
                        self().customAttrQuery(tenantId, mtExtendVO8.getTableName(), "Y");
        if (CollectionUtils.isEmpty(extendSettingsVO2s)) {
            return Collections.emptyList();
        }

        List<MtExtendSettingsVO2> extendSettings = new ArrayList<>();
        extendSettings.addAll(extendSettingsVO2s);
        MtExtendTableDesc mtExtendTableDesc = new MtExtendTableDesc();
        mtExtendTableDesc.setTenantId(tenantId);
        mtExtendTableDesc.setAttrTable(mtExtendVO8.getTableName());
        mtExtendTableDesc = mtExtendTableDescMapper.selectOne(mtExtendTableDesc);
        if (mtExtendTableDesc == null) {
            return Collections.emptyList();
        }
        String mainKeyId = mtExtendTableDesc.getMainTableKey();
        List<MtExtendVO9> result = new ArrayList<>();

        // 2.获取扩展属性值
        if (CollectionUtils.isNotEmpty(mtExtendVO8.getAttrs())) {
            extendSettingsVO2s = extendSettingsVO2s.stream()
                            .filter(t -> mtExtendVO8.getAttrs().stream().map(MtExtendAttrVO::getAttrName)
                                            .collect(Collectors.toList()).contains(t.getAttrName()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(extendSettingsVO2s)) {
                return Collections.emptyList();
            }
            extendSettingsVO2s.stream().forEach(t -> {
                Optional<MtExtendAttrVO> first = mtExtendVO8.getAttrs().stream()
                                .filter(c -> t.getAttrName().equalsIgnoreCase(c.getAttrName())).findFirst();
                if (first.isPresent()) {
                    t.setAttrValue(first.get().getAttrValue());
                }

            });
        }
        // 针对多组属性查询
        List<String> finalKidList = new ArrayList<>();
        for (int i = 0; i < extendSettingsVO2s.size(); i++) {
            List<String> kidList = mtExtendSettingMapper.attrsFromNameLineToHeader(tenantId, extendSettingsVO2s.get(i),
                            mtExtendVO8.getTableName(), mainKeyId);

            if (CollectionUtils.isEmpty(kidList)) {
                return Collections.emptyList();
            }

            // 取交集
            if (i > 0) {
                finalKidList = finalKidList.stream().filter(k -> kidList.contains(k)).collect(Collectors.toList());
            } else {
                finalKidList.addAll(kidList);
            }
        }
        if (CollectionUtils.isEmpty(finalKidList)) {
            return Collections.emptyList();
        }
        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingMapper.attrsFromNameForKids(tenantId,
                        mtExtendVO8.getTableName(), finalKidList, extendSettings, mainKeyId);

        Map<String, List<MtExtendAttrVO1>> listMap =
                        mtExtendAttrVO1s.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));
        listMap.entrySet().stream().forEach(c -> {
            MtExtendVO9 vo9 = new MtExtendVO9();
            List<MtExtendVO5> attrsList = new ArrayList<>();
            vo9.setKeyId(c.getKey());
            c.getValue().stream().filter(
                            v -> StringUtils.isEmpty(v.getLang()) || currentLanguage.equalsIgnoreCase(v.getLang()))
                            .forEach(v -> {
                                MtExtendVO5 attrs = new MtExtendVO5();
                                attrs.setAttrName(v.getAttrName());
                                attrs.setAttrValue(v.getAttrValue());
                                attrs.setLang(v.getLang());
                                attrsList.add(attrs);
                            });
            vo9.setAttrs(attrsList);
            result.add(vo9);
        });
        return result;
    }

    @Override
    public List<String> attrPropertyUpdateForIface(Long tenantId, String tableName,
                    Map<String, List<MtExtendVO5>> dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(tableName)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", "【API：attrPropertyUpdate】"));
        }
        if (MapUtils.isEmpty(dto)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", "【API：attrPropertyUpdate】"));
        }
        // 准备需要执行的SQL语句
        List<String> sqlList = new ArrayList<>();

        // 共有变量
        Date now = new Date();
        String nowStr = DATE_FORMAT.format(now);
        String dbNow = customDbRepository.getDateSerializerSql(nowStr, false);

        Long tempUserId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            tempUserId = DetailsHelper.getUserDetails().getUserId();
        }
        final Long userId = tempUserId;

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();


        // 2. 获取扩展表定义
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc extendTableDesc = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == extendTableDesc || StringUtils.isEmpty(extendTableDesc.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrPropertyUpdate】"));
        }
        // 主表主键名
        String mainTableKey = extendTableDesc.getMainTableKey();

        // 主表名
        String mainTableName = extendTableDesc.getMainTable();

        // 历史扩展表名
        String attrHisTableName = extendTableDesc.getHisAttrTable();

        // 历史主键名
        String hisTableKey = extendTableDesc.getHisTableKey();

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ATTR_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3.获取扩展属性
        MtExtendSettings queryAttr = new MtExtendSettings();
        queryAttr.setTenantId(tenantId);
        queryAttr.setExtendTableDescId(extendTableDesc.getExtendTableDescId());
        queryAttr.setEnableFlag("Y");
        List<MtExtendSettings> allSettings = mtExtendSettingMapper.select(queryAttr);

        // 获取当前扩展属性表中已有的扩展属性
        List<String> keyIds = dto.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        String whereInValuesSql = StringHelper.getWhereInValuesSql(extendTableDesc.getMainTableKey(), keyIds, 1000);
        List<MtExtendAttrVO3> attrValue = mtExtendSettingMapper.existAttrQueryForKeyId(tenantId, tableName,
                        extendTableDesc.getMainTableKey(), whereInValuesSql);
        Map<String, List<MtExtendAttrVO3>> keyIdMap =
                        attrValue.stream().collect(Collectors.groupingBy(MtExtendAttrVO3::getMainTableKeyValue));


        // 获取最新历史ID
        Map<String, String> mtExtendVO11sMap = null;
        if (StringUtils.isNotEmpty(attrHisTableName) && CollectionUtils.isNotEmpty(keyIds)) {
            List<MtExtendVO11> mtExtendVO11s = mtExtendTableDescMapper.selectLatestHisIdsByMainT(tenantId,
                            mainTableName, mainTableKey, whereInValuesSql);
            if (CollectionUtils.isNotEmpty(mtExtendVO11s)) {
                mtExtendVO11sMap = mtExtendVO11s.stream()
                                .collect(Collectors.toMap(t -> t.getKeyId(), t -> t.getLatestHisId()));
            }
        }

        // 获取id
        // 批量获取Id、Cid
        List<String> attrId =
                        this.customDbRepository.getNextKeys(tableName + "_s", dto.size() * allSettings.size() * 2);
        List<String> attrCid =
                        this.customDbRepository.getNextKeys(tableName + "_cid_s", dto.size() * allSettings.size() * 2);

        List<String> attrHisId = this.customDbRepository.getNextKeys(attrHisTableName + "_s",
                        dto.size() * allSettings.size() * 2);
        List<String> attrHisCid = this.customDbRepository.getNextKeys(attrHisTableName + "_cid_s",
                        dto.size() * allSettings.size() * 2);
        List<MtExtendAttrVO3> updateList = new ArrayList<>(attrCid.size());
        Map<String, String> finalMtExtendVO11sMap = mtExtendVO11sMap;
        dto.entrySet().stream().forEach(k -> {
            List<MtExtendVO4> attrList = new ArrayList<MtExtendVO4>();
            for (MtExtendVO5 entry : k.getValue()) {
                MtExtendVO4 attr = new MtExtendVO4();
                attr.setAttrName(entry.getAttrName());
                attr.setAttrValue(entry.getAttrValue());
                attr.setLang(entry.getLang());
                String tlFlag = null;
                Optional<MtExtendSettings> settingsOptional = allSettings.stream()
                                .filter(t -> entry.getAttrName().equals(t.getAttrName())).findFirst();
                if (settingsOptional.isPresent()) {
                    tlFlag = settingsOptional.get().getTlFlag();
                } else {
                    throw new MtException("MT_GENERAL_0010",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_GENERAL_0010",
                                                    "GENERAL", tableName, entry.getAttrName(),
                                                    "【API:attrPropertyUpdate】"));
                }
                attr.setTlFlag(tlFlag);
                attrList.add(attr);
            }
            for (MtExtendVO4 ever : attrList) {
                Optional<MtExtendAttrVO3> curAttrOp = null;
                if (CollectionUtils.isNotEmpty(allSettings) && MapUtils.isNotEmpty(keyIdMap)
                                && CollectionUtils.isNotEmpty(keyIdMap.get(k.getKey()))) {
                    // 如果是多语言字段，同时传入LANG，则更新传入LANG的数据
                    // 如果是多语言字段，同时没有传入LANG，则更新当前语言环境的数据
                    // 如果不是多语言字段，则更新LANG为空字符串的数据
                    if ("Y".equals(ever.getTlFlag()) && StringUtils.isNotEmpty(ever.getLang())) {
                        // 找到当前扩展属性是否有值,只会有一个值
                        curAttrOp = keyIdMap.get(k.getKey()).stream()
                                        .filter(t -> ever.getAttrName().equals(t.getAttrName())
                                                        && ever.getLang().equals(t.getLang()))
                                        .findFirst();

                    } else if ("Y".equals(ever.getTlFlag()) && StringUtils.isEmpty(ever.getLang())) {
                        curAttrOp = keyIdMap.get(k.getKey()).stream()
                                        .filter(t -> ever.getAttrName().equals(t.getAttrName())
                                                        && MtLanguageHelper.language().equals(t.getLang()))
                                        .findFirst();
                    } else {
                        curAttrOp = keyIdMap.get(k.getKey()).stream()
                                        .filter(t -> ever.getAttrName().equals(t.getAttrName())
                                                        && StringUtils.isEmpty(t.getLang()))
                                        .findFirst();
                    }
                }

                // 判断是否找到数据，如果找到说明需要更新，没找到需要新增
                if (curAttrOp != null && curAttrOp.isPresent()) {
                    // 获取更新扩展属性和新增扩展属性历史的SQL
                    curAttrOp.get().setAttrValue(ever.getAttrValue());
                    curAttrOp.get().setCid(Long.valueOf(attrCid.remove(0)));
                    if (MapUtils.isNotEmpty(finalMtExtendVO11sMap)) {
                        curAttrOp.get().setHisTableKeyValue(finalMtExtendVO11sMap.get(k.getKey()));
                        curAttrOp.get().setAttrHisId(attrHisId.remove(0));
                        curAttrOp.get().setHisCid(Long.valueOf(attrHisCid.remove(0)));
                    }
                    curAttrOp.get().setObjectVersionNumber(curAttrOp.get().getObjectVersionNumber() + 1L);
                    updateList.add(curAttrOp.get());
                } else {
                    List<String> langs = new ArrayList<String>();
                    // 如果是多语言字段，同时传入LANG，则新增传入LANG的数据
                    // 如果是多语言字段，同时没有传入LANG，则新增所有语言环境的数据
                    // 如果不是多语言字段，则新增LANG为空字符串的数据
                    if ("Y".equals(ever.getTlFlag()) && StringUtils.isNotEmpty(ever.getLang())) {
                        langs.add(ever.getLang());
                    } else if ("Y".equals(ever.getTlFlag()) && StringUtils.isEmpty(ever.getLang())) {
                        for (Language language : languages) {
                            langs.add(language.getCode());
                        }
                    } else {
                        langs.add("");
                    }
                    // 获取新增扩展属性和扩展属性历史的SQL
                    for (String lang : langs) {
                        MtExtendAttrVO3 insertVo = new MtExtendAttrVO3();
                        insertVo.setAttrId(attrId.remove(0));
                        insertVo.setMainTableKeyValue(k.getKey());
                        if (MapUtils.isNotEmpty(finalMtExtendVO11sMap)) {
                            insertVo.setHisTableKeyValue(finalMtExtendVO11sMap.get(k.getKey()));
                            insertVo.setAttrHisId(attrHisId.remove(0));
                            insertVo.setHisCid(Long.valueOf(attrHisCid.remove(0)));
                        }
                        insertVo.setAttrName(ever.getAttrName());
                        insertVo.setAttrValue(ever.getAttrValue());
                        insertVo.setLang(lang);
                        insertVo.setCreateDate(now);
                        insertVo.setCreateBy(userId);
                        insertVo.setObjectVersionNumber(1L);
                        insertVo.setCid(Long.valueOf(attrCid.remove(0)));
                        insertVo.setAttrName(ever.getAttrName());
                        updateList.add(insertVo);
                    }
                }
            }
        });
        if (CollectionUtils.isNotEmpty(updateList)) {
            List<List<MtExtendAttrVO3>> splitSqlList = splitSqlList(updateList, 10000);
            for (List<MtExtendAttrVO3> mtExtendAttrVO3s : splitSqlList) {
                Config config = mapperHelper.getConfig();
                DbType dbType = config.getDbType();
                String dbTypeValue = dbType.getValue();
                if (dbTypeValue.equals("oracle")) {
                    sqlList.addAll(getAttrReplaceOracleSql(tenantId, tableName, mainTableKey, attrHisTableName,
                                    hisTableKey, eventId, mtExtendAttrVO3s, dbNow, userId));
                } else {
                    sqlList.addAll(getAttrReplaceMysqlSql(tenantId, tableName, mainTableKey, attrHisTableName,
                                    hisTableKey, eventId, mtExtendAttrVO3s, dbNow, userId));
                }
            }
        }
        return sqlList;
    }

    /**
     * 根据 旧attrId数据复制新的attrId数据
     *
     * @param attrTableName 扩展表名
     * @param newAttrId 复制出来的新attrId
     * @param oldAttrId 根据oldAttrId对应数据复制
     * @param mainKeyIdName 主表ID字段名称
     * @param mainKeyId 复制出来主表ID值
     * @param cid 复制出来数据的CID
     * @param userId 复制出来数据的创建人ID
     * @param now 处理时间格式化
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/12/30
     */
    @Override
    public String getCopyAttrSql(String attrTableName, String newAttrId, String oldAttrId, String mainKeyIdName,
                    String mainKeyId, String cid, Long userId, String now) {
        String dbNow = customDbRepository.getDateSerializerSql(now, false);
        StringBuilder insertAttrsql = new StringBuilder("INSERT INTO ").append(attrTableName)
                        .append(" (TENANT_ID, ATTR_ID, ").append(mainKeyIdName)
                        .append(", ATTR_NAME, ATTR_VALUE, LANG, CID, CREATED_BY, CREATION_DATE, LAST_UPDATED_BY, LAST_UPDATE_DATE ) ");
        insertAttrsql.append("SELECT t.TENANT_ID, '").append(newAttrId).append("','").append(mainKeyId)
                        .append("', t.ATTR_NAME,").append("t.ATTR_VALUE,").append("t.LANG,").append(cid).append(",")
                        .append(userId).append(",").append(dbNow).append(",").append(userId).append(",").append(dbNow)
                        .append("FROM").append(" ").append(attrTableName).append("  t ").append("WHERE ")
                        .append("t.ATTR_ID = '").append(oldAttrId).append("'");
        return insertAttrsql.toString();
    }

    /**
     * 更新扩展表的主表ID为新的主表ID（替换）
     *
     * @param attrTableName 扩展表名
     * @param mainKeyIdName 扩展表的主表ID名
     * @param newMainKeyId 替换目标主表ID值
     * @param oldMainKeyId 替换来源主表ID值
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/12/30
     */
    @Override
    public String getRepleaceMainKeyIdSql(String attrTableName, String mainKeyIdName, String newMainKeyId,
                    String oldMainKeyId) {
        StringBuilder updateAttrsql = new StringBuilder("update ").append(attrTableName).append("  t set t.")
                        .append(mainKeyIdName).append("='").append(newMainKeyId).append("' where t.")
                        .append(mainKeyIdName).append("='").append(oldMainKeyId).append("'");
        return updateAttrsql.toString();
    }

    /**
     * 拆分-集合列表
     *
     * @param sqlList
     * @param splitNum
     * @return
     */
    private List<List<MtExtendAttrVO3>> splitSqlList(List<MtExtendAttrVO3> sqlList, int splitNum) {

        List<List<MtExtendAttrVO3>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private List<String> getAttrReplaceOracleSql(Long tenantId, String tableName, String mainTableKey,
                    String hisTableName, String hisTableKey, String eventId, List<MtExtendAttrVO3> attrs, String dbNow,
                    Long userId) {

        List<String> sqlList = new ArrayList<String>();
        final StringBuilder sql = new StringBuilder();
        sql.append("MERGE INTO ").append(tableName);
        sql.append(" M USING");
        sql.append("(");

        final StringBuilder hisSql = new StringBuilder();
        if (StringUtils.isNotEmpty(hisTableName)) {
            hisSql.append("MERGE INTO ").append(hisTableName);
            hisSql.append(" M USING");
            hisSql.append("(");
        }

        StringBuilder tempSql = new StringBuilder();
        StringBuilder tempHisSql = new StringBuilder();

        for (MtExtendAttrVO3 c : attrs) {
            String nowStr = DATE_FORMAT.format(c.getCreateDate());
            String createDate = "to_date('" + nowStr + "','yyyy-MM-dd hh24:mi:ss')";
            String value = StringUtils.isEmpty(c.getAttrValue()) ? "" : c.getAttrValue();
            String lang = StringUtils.isEmpty(c.getLang()) ? "" : c.getLang();
            value = value.replace("'", "''");
            value = StringUtils.isEmpty(value) ? null : value;
            lang = StringUtils.isEmpty(lang) ? null : lang;
            tempSql.append(" SELECT ");
            tempSql.append(tenantId).append(" ").append("TENANT_ID,");
            tempSql.append("'").append(c.getAttrId()).append("' ATTR_ID,");
            tempSql.append("'").append(c.getAttrName()).append("' ATTR_NAME,");
            if (value != null) {
                tempSql.append("'").append(value).append("' ATTR_VALUE,");
            } else {
                tempSql.append(" null ").append(" ATTR_VALUE,");
            }
            if (lang != null) {
                tempSql.append("'").append(lang).append("' LANG,");
            } else {
                tempSql.append("null").append(" LANG,");
            }
            tempSql.append("'").append(c.getLang()).append("' LANG,");
            tempSql.append("'").append(c.getCid()).append("' CID,");
            tempSql.append(c.getCreateBy()).append(" CREATED_BY,");
            tempSql.append(createDate).append(" CREATION_DATE,");
            tempSql.append(userId).append(" LAST_UPDATED_BY,");
            tempSql.append(dbNow).append(" LAST_UPDATE_DATE,");
            tempSql.append(c.getObjectVersionNumber()).append(" OBJECT_VERSION_NUMBER,");
            tempSql.append("'").append(c.getMainTableKeyValue()).append("' ").append(mainTableKey);
            tempSql.append(" FROM DUAL UNION");
            if (StringUtils.isNotEmpty(hisTableName)) {
                tempHisSql.append(" SELECT ");
                tempHisSql.append(tenantId).append(" ").append("TENANT_ID,");
                tempHisSql.append("'").append(c.getAttrHisId()).append("' ATTR_HIS_ID,");
                tempHisSql.append("'").append(c.getAttrName()).append("' ATTR_NAME,");
                if (value != null) {
                    tempHisSql.append("'").append(value).append("' ATTR_VALUE");
                } else {
                    tempHisSql.append("null").append(" ATTR_VALUE");
                }
                if (lang != null) {
                    tempHisSql.append("'").append(lang).append("' LANG,");
                } else {
                    tempHisSql.append("null").append(" LANG,");
                }
                tempHisSql.append("'").append(eventId).append("' EVENT_ID,");
                tempHisSql.append("'").append(c.getHisCid()).append("' CID,");
                tempHisSql.append(c.getCreateBy()).append(" CREATED_BY,");
                tempHisSql.append(createDate).append(" CREATION_DATE,");
                tempHisSql.append(userId).append(" LAST_UPDATED_BY,");
                tempHisSql.append(dbNow).append(" LAST_UPDATE_DATE,");
                tempHisSql.append(1).append(" OBJECT_VERSION_NUMBER,");
                tempHisSql.append("'").append(c.getHisTableKeyValue()).append("' ").append(hisTableKey);
                tempHisSql.append(" FROM DUAL UNION");
            }
        }

        // 填充扩展表更新的SQL
        tempSql.delete(tempSql.length() - 5, tempSql.length());
        sql.append(tempSql).append(") T ").append(" ON ( M.ATTR_ID =T.ATTR_ID )");
        sql.append(" WHEN MATCHED THEN UPDATE SET ").append(
                        "TENANT_ID=T.TENANT_ID,ATTR_ID=T.ATTR_ID,ATTR_NAME=T.ATTR_NAME,ATTR_VALUE=T.ATTR_VALUE,")
                        .append("LANG= T.LANG,EVENT_ID=T.EVENT_ID,CID=T.CID,CREATED_BY=T.CREATED_BY,CREATION_DATE=T.CREATION_DATE,")
                        .append("LAST_UPDATED_BY= T.LAST_UPDATED_BY,LAST_UPDATE_DATE=T.LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER=T.OBJECT_VERSION_NUMBER,")
                        .append(mainTableKey).append("=T.").append(mainTableKey);
        sql.append(" WHEN NOT MATCHED THEN INSERT (TENANT_ID,ATTR_ID,ATTR_NAME,ATTR_VALUE,LANG,EVENT_ID,CID,CREATED_BY,")
                        .append("CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER,");
        sql.append(mainTableKey).append(") VALUES");
        sql.append(" (T.TENANT_ID,T.ATTR_ID,T.ATTR_NAME,T.ATTR_VALUE,T.LANG,T.EVENT_ID,T.CID,T.CREATED_BY,T.CREATION_DATE,")
                        .append("T.LAST_UPDATED_BY, T.LAST_UPDATE_DATE,T.OBJECT_VERSION_NUMBER,T.");
        sql.append(mainTableKey).append(")");
        sqlList.add(sql.toString());
        if (StringUtils.isNotEmpty(hisTableName)) {
            tempHisSql.delete(tempHisSql.length() - 5, tempHisSql.length());
            sql.append(tempHisSql).append(") T ").append(" ON ( M.ATTR_HIS_ID =T.ATTR_HIS_ID )");
            sql.append(" WHEN MATCHED THEN UPDATE SET ").append(
                            "TENANT_ID=T.TENANT_ID,ATTR_HIS_ID=T.ATTR_HIS_ID,ATTR_NAME=T.ATTR_NAME,ATTR_VALUE=T.ATTR_VALUE,")
                            .append("LANG= T.LANG,CID=T.CID,CREATED_BY=T.CREATED_BY,CREATION_DATE=T.CREATION_DATE,")
                            .append("LAST_UPDATED_BY=T.LAST_UPDATED_BY,LAST_UPDATE_DATE= T.LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER=T.OBJECT_VERSION_NUMBER,")
                            .append(hisTableKey).append("=T.").append(hisTableKey);
            sql.append(" WHEN NOT MATCHED THEN INSERT (TENANT_ID,ATTR_HIS_ID,ATTR_NAME,ATTR_VALUE,LANG,CID,CREATED_BY,CREATION_DATE,")
                            .append("LAST_UPDATED_BY,LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER,");
            sql.append(hisTableKey).append(") VALUES");
            sql.append(" (T.TENANT_ID,T.ATTR_HIS_ID,T.ATTR_NAME,T.ATTR_VALUE,T.LANG,T.CID,T.CREATED_BY,T.CREATION_DATE,T.LAST_UPDATED_BY,")
                            .append("T.LAST_UPDATE_DATE,T.OBJECT_VERSION_NUMBER,T.");
            sql.append(hisTableKey).append(")");

            sqlList.add(hisSql.append(tempHisSql).toString());
        }

        sqlList.add(sql.append(tempSql).toString());
        return sqlList;
    }

    private List<String> getAttrReplaceMysqlSql(Long tenantId, String tableName, String mainTableKey,
                    String hisTableName, String hisTableKey, String eventId, List<MtExtendAttrVO3> attrs, String dbNow,
                    Long userId) {

        List<String> sqlList = new ArrayList<String>();
        final StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO ").append(tableName).append(
                        "(TENANT_ID,ATTR_ID,ATTR_NAME,ATTR_VALUE,LANG,CID,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER,");
        sql.append(mainTableKey).append(") VALUES");

        StringBuilder tempSql = new StringBuilder();
        final StringBuilder hisSql = new StringBuilder();
        StringBuilder tempHisSql = new StringBuilder();

        if (StringUtils.isNotEmpty(hisTableName)) {
            hisSql.append("REPLACE INTO ").append(hisTableName).append(
                            "(TENANT_ID,ATTR_HIS_ID,ATTR_NAME,ATTR_VALUE,LANG,EVENT_ID,CID,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBJECT_VERSION_NUMBER,");
            hisSql.append(hisTableKey).append(") VALUES");
        }

        for (MtExtendAttrVO3 c : attrs) {
            String nowStr = DATE_FORMAT.format(c.getCreateDate());
            String createDate = customDbRepository.getDateSerializerSql(nowStr, false);
            String value = StringUtils.isEmpty(c.getAttrValue()) ? "" : c.getAttrValue();
            String lang = StringUtils.isEmpty(c.getLang()) ? "" : c.getLang();
            value = value.replace("'", "''");
            tempSql.append("(");
            tempSql.append(tenantId).append(",'").append(c.getAttrId()).append("','").append(c.getAttrName())
                            .append("','");
            tempSql.append(value).append("','").append(lang).append("',").append(c.getCid()).append(",")
                            .append(c.getCreateBy()).append(",").append(createDate).append(",").append(userId)
                            .append(",").append(dbNow).append(",").append(c.getObjectVersionNumber()).append(",'")
                            .append(c.getMainTableKeyValue()).append("'");
            tempSql.append("),");
            if (StringUtils.isNotEmpty(hisTableName)) {
                tempHisSql.append("(");
                tempHisSql.append(tenantId).append(",'").append(c.getAttrHisId()).append("','").append(c.getAttrName())
                                .append("','");
                tempHisSql.append(value).append("','").append(lang).append("','").append(eventId).append("',")
                                .append(c.getHisCid()).append(",").append(userId).append(",").append(dbNow).append(",")
                                .append(userId).append(",").append(dbNow).append(",").append(1).append(",'")
                                .append(c.getHisTableKeyValue()).append("'");
                tempHisSql.append("),");
            }
        }

        // 填充扩展表更新的SQL
        tempSql.deleteCharAt(tempSql.length() - 1);
        if (StringUtils.isNotEmpty(hisTableName)) {
            tempHisSql.deleteCharAt(tempHisSql.length() - 1);
            sqlList.add(hisSql.append(tempHisSql).toString());
        }

        sqlList.add(sql.append(tempSql).toString());
        return sqlList;
    }


    @Override
    public List<MtExtendSettings> selectSettingsByProperty(Long tenantId, String extendTableDescId, String enableFlag,
                    List<String> attrNames) {
        List<MtExtendSettings> result = MtExtendSettings.getSettingCaches(tenantId, extendTableDescId,
                        LanguageHelper.language(), redisHelper);
        if (CollectionUtils.isNotEmpty(result)) {
            if (StringUtils.isNotEmpty(enableFlag)) {
                result = result.stream().filter(c -> c.getEnableFlag().equals(enableFlag)).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(attrNames)) {
                result = result.stream().filter(c -> attrNames.contains(c.getAttrName())).collect(Collectors.toList());
            }
        } else {
            result = this.mtExtendSettingMapper.selectSettingsByProperty(tenantId, extendTableDescId, enableFlag,
                            attrNames);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String settingBasicPropertyUpdate(Long tenantId, MtExtendSettings dto) {
        MtExtendSettings union = new MtExtendSettings();
        union.setTenantId(tenantId);
        union.setExtendTableDescId(dto.getExtendTableDescId());
        union.setAttrName(dto.getAttrName());
        if (StringUtils.isNotEmpty(dto.getExtendId())) {
            union.setExtendId(dto.getExtendId());
        }

        Criteria criteria = new Criteria(union);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtExtendSettings.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtExtendSettings.FIELD_EXTEND_TABLE_DESC_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtExtendSettings.FIELD_ATTR_NAME, Comparison.EQUAL));
        if (StringUtils.isNotEmpty(dto.getExtendId())) {
            whereFields.add(new WhereField(MtExtendSettings.FIELD_EXTEND_ID, Comparison.NOT_EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (this.mtExtendSettingMapper.selectOptional(union, criteria).size() > 0) {
            throw new MtException("MT_GENERAL_0040", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0040", "GENERAL", "settingBasicPropertyBatchUpdate"));

        }

        String extendId = dto.getExtendId();

        if (StringUtils.isEmpty(extendId)) {
            self().insertSelective(dto);
            extendId = dto.getExtendId();

            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setTenantId(tenantId);
            mtExtendSettings.setExtendId(extendId);
            mtExtendSettings = this.mtExtendSettingMapper.selectOne(mtExtendSettings);

            if (dto.get_tls() == null || dto.get_tls().get("attrMeaning") == null) {
                for (Language language : LanguageHelper.languages()) {
                    mtExtendSettings.setLang(language.getCode());
                    MtExtendSettings.refreshCache(tenantId, language.getCode(), mtExtendSettings, redisHelper);
                }
            } else {
                Map<String, String> map = dto.get_tls().get("attrMeaning");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    mtExtendSettings.setLang(entry.getKey());
                    mtExtendSettings.setAttrMeaning(entry.getValue());
                    MtExtendSettings.refreshCache(tenantId, entry.getKey(), mtExtendSettings, redisHelper);
                }
            }
        } else {
            self().updateByPrimaryKeySelective(dto);
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setTenantId(tenantId);
            mtExtendSettings.setExtendId(extendId);
            mtExtendSettings = this.mtExtendSettingMapper.selectOne(mtExtendSettings);

            if (dto.get_tls() == null || dto.get_tls().get("attrMeaning") == null) {
                mtExtendSettings.setLang(LanguageHelper.language());
                MtExtendSettings.refreshCache(tenantId, LanguageHelper.language(), mtExtendSettings, redisHelper);
            } else {
                Map<String, String> map = dto.get_tls().get("attrMeaning");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    mtExtendSettings.setLang(entry.getKey());
                    mtExtendSettings.setAttrMeaning(entry.getValue());
                    MtExtendSettings.refreshCache(tenantId, entry.getKey(), mtExtendSettings, redisHelper);
                }
            }
        }

        return extendId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> settingBasicPropertyBatchUpdate(Long tenantId, List<MtExtendSettings> dto) {
        List<String> resultList = new ArrayList<>(dto.size());

        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dto)) {
            return resultList;
        }
        List<String> extendSettingIds = mtCustomDbRepository.getNextKeys("mt_extend_settings_s", dto.size());
        List<String> extendSettingCids = mtCustomDbRepository.getNextKeys("mt_extend_settings_cid_s", dto.size());

        // userId
        Long userId = MtUserClient.getCurrentUserId();
        // now Date
        Date date = new Date(System.currentTimeMillis());
        int index = 0;

        for (MtExtendSettings settings : dto) {
            String enableFlag = settings.getEnableFlag();

            List<MtExtendSettings> settingsList = selectSettingsByProperty(tenantId, settings.getExtendTableDescId(),
                            null, Collections.singletonList(settings.getAttrName()));

            if (StringUtils.isNotEmpty(settings.getExtendId())) {
                MtExtendSettings finalSettings = settings;
                settingsList = settingsList.stream().filter(t -> !t.getExtendId().equals(finalSettings.getExtendId()))
                                .collect(Collectors.toList());
            }

            if (settingsList.size() > 0
                            && settingsList.stream().anyMatch(t -> MtBaseConstants.YES.equals(t.getEnableFlag()))) {
                throw new MtException("MT_GENERAL_0040", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0040", "GENERAL", "settingBasicPropertyBatchUpdate"));
            }
            if (CollectionUtils.isNotEmpty(settingsList)
                            && MtBaseConstants.NO.equals(settingsList.get(0).getEnableFlag())) {
                settings = settingsList.get(0);
            }
            String extendId = settings.getExtendId();

            if (StringUtils.isNotEmpty(enableFlag)) {
                settings.setEnableFlag(enableFlag);
            }
            if (StringUtils.isEmpty(settings.getTlFlag())) {
                settings.setTlFlag(MtBaseConstants.NO);
            }
            if (StringUtils.isEmpty(extendId)) {
                settings.setTenantId(tenantId);
                settings.setExtendId(extendSettingIds.get(index));
                settings.setCid(Long.valueOf(extendSettingCids.get(index)));
                settings.setCreatedBy(userId);
                settings.setCreationDate(date);
                settings.setLastUpdatedBy(userId);
                settings.setLastUpdateDate(date);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(settings));
            } else {
                settings.setTenantId(tenantId);
                settings.setCid(Long.valueOf(extendSettingCids.get(index)));
                settings.setLastUpdatedBy(userId);
                settings.setLastUpdateDate(date);
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(settings));
            }
            if (settings.get_tls() == null || settings.get_tls().get("attrMeaning") == null) {
                for (Language language : LanguageHelper.languages()) {
                    settings.setLang(language.getCode());
                    MtExtendSettings.refreshCache(tenantId, language.getCode(), settings, redisHelper);
                }
            } else {
                Map<String, String> map = settings.get_tls().get("attrMeaning");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    settings.setLang(entry.getKey());
                    settings.setAttrMeaning(entry.getValue());
                    MtExtendSettings.refreshCache(tenantId, entry.getKey(), settings, redisHelper);
                }
            }
            resultList.add(settings.getExtendId());
            index++;
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    /**
     * 扩展属性和语言唯一性匹配类
     *
     * @author chuang.yang
     * @date 2020/3/19
     * @return
     */
    public static class AttrNameLangTuple {
        private String keyId;
        private String attrName;
        private String lang;

        public AttrNameLangTuple(String keyId, String attrName, String lang) {
            this.keyId = keyId;
            this.attrName = attrName;
            this.lang = lang;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AttrNameLangTuple that = (AttrNameLangTuple) o;
            return Objects.equals(keyId, that.keyId) && Objects.equals(attrName, that.attrName)
                            && Objects.equals(lang, that.lang);
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyId, attrName, lang);
        }
    }

    /**
     * 根据扩展表表名获取扩展表结构信息
     *
     * @param tenantId 租户Id
     * @param tableName 扩展表表名
     * @param apiName API名称
     * @return MtExtendRpcVO
     * @author benjamin
     * @date 2020/7/8 5:19 PM
     */
    private MtExtendRpcVO doGetExtendTable(Long tenantId, String tableName, String apiName) {
        MtExtendRpcVO table = mtExtendTableDescRepository.tableLimitAttrNameQuery(tenantId, tableName);
        // 找到维护的扩展属性
        if (null == table || !MtBaseConstants.YES.equals(table.getEnableFlag())
                        || StringUtils.isEmpty(table.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, apiName));
        }

        return table;
    }

    private Map<String, Pair<String, String>> getHisEventByKey(Long tenantId, List<String> keyIdList, String mainTable,
                    String mainTableKey, String hisTable, String hisTableKey) {
        Map<String, Pair<String, String>> idMap = new HashMap<>(keyIdList.size());

        // key id -> latest his id
        String whereInValuesSql = StringHelper.getWhereInValuesSql(mainTableKey, keyIdList, 1000);
        List<MtExtendVO11> lastHisIds = mtExtendSettingMapper.selectLatestHisIdsByMainT(tenantId, mainTable,
                        mainTableKey, whereInValuesSql);

        Map<String, String> keyHisIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(lastHisIds)) {
            keyHisIdMap = lastHisIds.stream()
                            .collect(Collectors.toMap(MtExtendVO11::getKeyId, MtExtendVO11::getLatestHisId));
        }

        // latest his id -> event id
        Map<String, String> lastHisEventMap = new HashMap<>();
        List<String> hisIds = new ArrayList<>(keyHisIdMap.values());
        if (CollectionUtils.isNotEmpty(hisIds)) {
            List<MtExtendVO12> lastHisEventList =
                            mtExtendSettingMapper.selectEventIdsByLatestHisId(tenantId, hisTable, hisTableKey, hisIds);
            if (CollectionUtils.isNotEmpty(lastHisEventList)) {
                lastHisEventMap = lastHisEventList.stream().collect(HashMap::new,
                                (m, v) -> m.put(v.getLatestHisId(), v.getEventId()), HashMap::putAll);
            }
        }

        for (Map.Entry<String, String> entry : keyHisIdMap.entrySet()) {
            idMap.put(entry.getKey(), Pair.of(entry.getValue(), lastHisEventMap.get(entry.getValue())));
        }

        return idMap;
    }

    /**
     * 生成新增扩展表的SQL语句和插入历史表的SQL语句
     */
    private List<String> getInsertAttrInfoSqlList(Long tenantId, String tableName, String mainTableKey,
                    String hisTableName, String hisTableKey, String attrKid, Long cid, String hisKid, Long hisCid,
                    String eventId, String keyId, String hisKeyId, String attrName, String attrValue, String lang,
                    String dbNow, Long userId) {
        List<String> sqlList = new ArrayList<>();

        String value = StringUtils.isEmpty(attrValue) ? "" : this.replaceAttrValue(attrValue);
        String insertSql = "INSERT INTO " + tableName + " " + "(CREATED_BY,LAST_UPDATED_BY,CID,ATTR_ID,TENANT_ID,"
                        + mainTableKey + ",ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES (" + userId
                        + "," + userId + "," + cid + ",'" + attrKid + "','" + tenantId + "','" + keyId + "','"
                        + attrName + "','" + value + "','" + lang + "'," + dbNow + "," + dbNow + ")";
        sqlList.add(insertSql);
        if (StringUtils.isNotEmpty(hisTableName) && StringUtils.isNotEmpty(hisKeyId)) {
            // 填充扩展表历史的SQL
            sqlList.add(getInsertAttrHisSql(tenantId, eventId, hisTableName, hisTableKey, hisKeyId, attrName, attrValue,
                            lang, dbNow, userId, hisKid, hisCid));
        }
        return sqlList;
    }

    private List<String> getUpdateAttrInfoSqlList(Long tenantId, String tableName, String hisTable, String hisTableKey,
                    String hisKeyId, String hisKid, Long hisCid, String eventId, String attrId, String attrName,
                    String attrValue, String lang, String dbNow, Long userId) {
        List<String> sqlList = new ArrayList<>();

        String value = StringUtils.isEmpty(attrValue) ? "" : this.replaceAttrValue(attrValue);

        String cid = this.customDbRepository.getNextKey(tableName + MtBaseConstants.CID_SUFFIX);
        // 填充扩展表更新的SQL
        String updateSql = "UPDATE " + tableName + " SET ATTR_VALUE = '" + value + "'," + "CID =" + Long.valueOf(cid)
                        + "," + "LAST_UPDATE_DATE =" + dbNow + "," + "LAST_UPDATED_BY = " + userId + " "
                        + " WHERE ATTR_ID ='" + attrId + "'" + " AND TENANT_ID =" + tenantId;
        sqlList.add(updateSql);
        if (StringUtils.isNotEmpty(hisTable) && StringUtils.isNotEmpty(hisKeyId)) {
            // 填充扩展表历史的SQL
            sqlList.add(getInsertAttrHisSql(tenantId, eventId, hisTable, hisTableKey, hisKeyId, attrName, value, lang,
                            dbNow, userId, hisKid, hisCid));
        }
        return sqlList;
    }

    /**
     * 生成插入历史表的SQL语句
     */
    private String getInsertAttrHisSql(Long tenantId, String eventId, String hisAttrTable, String hisTableKey,
                    String hisKeyId, String attrName, String attrValue, String lang, String dbNow, Long userId,
                    String hisKid, Long hisCid) {
        String value = StringUtils.isEmpty(attrValue) ? "" : this.replaceAttrValue(attrValue);
        return "INSERT INTO " + hisAttrTable + " " + "(CREATED_BY,LAST_UPDATED_BY,CID,ATTR_HIS_ID,EVENT_ID,TENANT_ID,"
                        + hisTableKey + "," + "ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES ("
                        + userId + "," + userId + "," + hisCid + "," + hisKid + "," + eventId + "," + tenantId + ","
                        + hisKeyId + ",'" + attrName + "','" + value + "','" + lang + "'," + dbNow + "," + dbNow + ")";
    }

    private String replaceAttrValue(String v) {
        return v.replace("'", "''").replace("\\", "\\\\");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attrPropertyBatchUpdateNew(Long tenantId, String tableName, String eventId,
                    List<MtCommonExtendVO7> dtoList) {
        String apiName = "【API：attrPropertyBatchUpdateNew】";

        // 0. basic check
        if (StringUtils.isEmpty(tableName)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tableName", apiName));
        }
        if (CollectionUtils.isEmpty(dtoList) || dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getKeyId()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", apiName));
        }

        // 1. get extend table structure, throw exception if table is disabled or table doesn't have the
        // main key
        MtExtendRpcVO table = doGetExtendTable(tenantId, tableName, apiName);
        // return if table doesn't contain columns
        List<MtExtendColumnRpcVO> columnList = table.getExtendColumnList().stream()
                        .filter(c -> MtBaseConstants.YES.equals(c.getEnableFlag())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(columnList)) {
            return;
        }

        // get param which contains attrs
        List<MtCommonExtendVO7> existAttrParamList = dtoList.stream()
                        .filter(t -> CollectionUtils.isNotEmpty(t.getAttrs())).collect(Collectors.toList());

        // 2. throw exception if param attr name list contains disabled attr
        List<String> attrNameList =
                        columnList.stream().map(MtExtendColumnRpcVO::getAttrName).collect(Collectors.toList());
        Optional<String> attrNameOpt = existAttrParamList.stream().flatMap(t -> t.getAttrs().stream()
                        .filter(x -> !attrNameList.contains(x.getAttrName())).map(MtCommonExtendVO4::getAttrName))
                        .findFirst();
        if (attrNameOpt.isPresent()) {
            throw new MtException("MT_GENERAL_0010", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0010", "GENERAL", tableName, attrNameOpt.get(), apiName));
        }

        // 3. get all keys origin attr properties
        List<String> paramKeyIdList = dtoList.stream().map(MtCommonExtendVO7::getKeyId).collect(Collectors.toList());
        List<MtExtendAttrVO3> originAttrValueList = mtExtendSettingMapper.attrDataQuery(tenantId, tableName,
                        table.getMainTableKey(), paramKeyIdList);

        // transform to map, keyId & attrName is unique
        Map<String, Map<String, MtExtendAttrVO3>> originAttrMap =
                        originAttrValueList.stream()
                                        .collect(Collectors.groupingBy(MtExtendAttrVO3::getMainTableKeyValue,
                                                        Collectors.toMap(MtExtendAttrVO3::getAttrName, t -> t,
                                                                        (oldValue, newValue) -> oldValue)));

        // 4 key id -> his id & event id
        Map<String, Pair<String, String>> idPairMap = new HashMap<>();
        // valid event id if exists history attr table
        if (StringUtils.isNotEmpty(table.getHisAttrTable())) {
            List<String> keyIdList =
                            dtoList.stream().map(MtCommonExtendVO7::getKeyId).distinct().collect(Collectors.toList());
            idPairMap = getHisEventByKey(tenantId, keyIdList, table.getMainTable(), table.getMainTableKey(),
                            table.getHisTable(), table.getHisTableKey());
        }

        // 共有变量
        LocalDateTime dbNow = LocalDateTime.now();
        Long userId = MtUserClient.getCurrentUserId();

        // 5. deal with params
        List<MtCommonExtendAttrVO4> insertAttrList = new ArrayList<>();
        List<MtCommonExtendAttrVO4> updateAttrList = new ArrayList<>();
        List<MtCommonExtendAttrHisVO6> insertHisAttrList = new ArrayList<>();

        // 获取扩展表主键
        int index = 0;
        int cidIndex = 0;
        long count = existAttrParamList.stream().map(MtCommonExtendVO7::getAttrs).flatMap(Collection::stream).count();
        List<String> idS = customDbRepository.getNextKeys(tableName + "_s", Math.toIntExact(count));
        List<String> cidS = customDbRepository.getNextKeys(tableName + "_cid_s", Math.toIntExact(count));

        for (MtCommonExtendVO7 dto : existAttrParamList) {
            String hisKeyId = null;
            String curEventId = eventId;
            if (MapUtils.isNotEmpty(idPairMap)) {
                curEventId = null == eventId ? idPairMap.get(dto.getKeyId()).getRight() : eventId;
                hisKeyId = idPairMap.get(dto.getKeyId()).getLeft();
            }

            Map<String, String> currentAttr =
                            dto.getAttrs().stream().collect(Collectors.toMap(MtCommonExtendVO4::getAttrName,
                                            t -> MtFieldsHelper.getOrDefault(t.getAttrValue(), "")));

            MtCommonExtendAttrVO4 attr;
            MtCommonExtendAttrHisVO6 hisAttr;
            if (MapUtils.isNotEmpty(originAttrMap.get(dto.getKeyId()))) {
                for (Map.Entry<String, String> entry : currentAttr.entrySet()) {
                    MtExtendAttrVO3 originExtend = originAttrMap.get(dto.getKeyId()).get(entry.getKey());
                    attr = new MtCommonExtendAttrVO4();
                    attr.setKeyId(dto.getKeyId());
                    attr.setAttrName(entry.getKey());
                    attr.setAttrValue(entry.getValue());
                    attr.setCid(cidS.get(cidIndex));
                    cidIndex++;
                    if (null == originExtend) {
                        attr.setAttrId(idS.get(index));
                        insertAttrList.add(attr);
                        index++;
                    } else {
                        attr.setAttrId(originExtend.getAttrId());
                        updateAttrList.add(attr);
                    }

                    if (StringUtils.isNotEmpty(table.getHisAttrTable())) {
                        hisAttr = new MtCommonExtendAttrHisVO6();
                        hisAttr.setHisKeyId(hisKeyId);
                        hisAttr.setAttrId(attr.getAttrId());
                        hisAttr.setEventId(curEventId);
                        hisAttr.setAttrName(entry.getKey());
                        hisAttr.setAttrValue(entry.getValue());
                        insertHisAttrList.add(hisAttr);
                    }
                }

                if (StringUtils.isNotEmpty(table.getHisAttrTable()) && StringUtils.isNotEmpty(hisKeyId)) {
                    String finalHisKeyId = hisKeyId;
                    String finalCurEventId = curEventId;
                    insertHisAttrList.addAll(originAttrMap.get(dto.getKeyId()).entrySet().stream()
                                    .filter(t -> !currentAttr.containsKey(t.getKey())).map(t -> {
                                        MtCommonExtendAttrHisVO6 lambdaHisAttr = new MtCommonExtendAttrHisVO6();
                                        lambdaHisAttr.setHisKeyId(finalHisKeyId);
                                        lambdaHisAttr.setEventId(finalCurEventId);
                                        lambdaHisAttr.setAttrId(t.getValue().getAttrId());
                                        lambdaHisAttr.setAttrName(t.getKey());
                                        lambdaHisAttr.setAttrValue(t.getValue().getAttrValue());
                                        return lambdaHisAttr;
                                    }).collect(Collectors.toList()));
                }
            } else {
                for (Map.Entry<String, String> entry : currentAttr.entrySet()) {
                    attr = new MtCommonExtendAttrVO4();
                    attr.setKeyId(dto.getKeyId());
                    attr.setAttrName(entry.getKey());
                    attr.setAttrValue(entry.getValue());
                    attr.setAttrId(idS.get(index));
                    attr.setCid(cidS.get(cidIndex));
                    insertAttrList.add(attr);

                    index++;
                    cidIndex++;

                    if (StringUtils.isNotEmpty(table.getHisAttrTable()) && StringUtils.isNotEmpty(hisKeyId)) {
                        hisAttr = new MtCommonExtendAttrHisVO6();
                        hisAttr.setHisKeyId(hisKeyId);
                        hisAttr.setAttrId(attr.getAttrId());
                        hisAttr.setEventId(curEventId);
                        hisAttr.setAttrName(entry.getKey());
                        hisAttr.setAttrValue(entry.getValue());
                        insertHisAttrList.add(hisAttr);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(insertAttrList)) {
            batchInsertAttr(tenantId, table.getAttrTable(), table.getMainTableKey(), dbNow, userId, insertAttrList);
        }
        if (CollectionUtils.isNotEmpty(updateAttrList)) {
            batchUpdateAttr(tenantId, table.getAttrTable(), table.getMainTableKey(), dbNow, userId, updateAttrList);
        }

        // 6. deal with empty-attrs params, only record history
        List<MtCommonExtendVO7> noAttrDtoList = dtoList.stream().filter(t -> CollectionUtils.isEmpty(t.getAttrs()))
                        .collect(Collectors.toList());
        for (MtCommonExtendVO7 dto : noAttrDtoList) {
            if (null == originAttrMap.get(dto.getKeyId())) {
                continue;
            }

            String hisKeyId = null;
            String curEventId = eventId;
            // get his key id & event id
            if (MapUtils.isNotEmpty(idPairMap)) {
                if (StringUtils.isEmpty(eventId)) {
                    curEventId = null == eventId ? idPairMap.get(dto.getKeyId()).getRight() : eventId;
                }
                hisKeyId = idPairMap.get(dto.getKeyId()).getLeft();
            }

            List<MtExtendAttrVO3> curAttrValueList = new ArrayList<>(originAttrMap.get(dto.getKeyId()).values());

            // check if exists his table
            if (CollectionUtils.isNotEmpty(curAttrValueList) && StringUtils.isNotEmpty(table.getHisTable())
                            && StringUtils.isNotEmpty(hisKeyId)) {
                MtCommonExtendAttrHisVO6 hisAttr;
                for (MtExtendAttrVO3 attrValue : curAttrValueList) {
                    hisAttr = new MtCommonExtendAttrHisVO6();
                    hisAttr.setHisKeyId(hisKeyId);
                    hisAttr.setAttrId(attrValue.getAttrId());
                    hisAttr.setEventId(curEventId);
                    hisAttr.setAttrName(attrValue.getAttrName());
                    hisAttr.setAttrValue(attrValue.getAttrValue());
                    insertHisAttrList.add(hisAttr);
                }
            }
        }

        // record his
        if (CollectionUtils.isNotEmpty(insertHisAttrList)) {
            AtomicInteger hisIndex = new AtomicInteger();
            List<String> hisIdS =
                            customDbRepository.getNextKeys(table.getHisAttrTable() + "_s", insertHisAttrList.size());
            List<String> hisCidS = customDbRepository.getNextKeys(table.getHisAttrTable() + "_cid_s",
                            insertHisAttrList.size());
            insertHisAttrList.stream().forEach(t -> {
                t.setAttrHisId(hisIdS.get(hisIndex.get()));
                t.setCid(hisCidS.get(hisIndex.get()));
                hisIndex.getAndIncrement();
            });
            batchInsertAttrHis(tenantId, table.getHisAttrTable(), table.getHisTableKey(), dbNow, userId,
                            insertHisAttrList);
        }
    }

    private void batchInsertAttr(Long tenantId, String tableName, String mainTableKey, LocalDateTime dbNow, Long userId,
                    List<MtCommonExtendAttrVO4> attrList) {
        String insertSql = "INSERT INTO " + tableName + " (CREATED_BY,LAST_UPDATED_BY,TENANT_ID," + mainTableKey
                        + ",ATTR_NAME,ATTR_VALUE,CREATION_DATE,LAST_UPDATE_DATE,ATTR_ID,CID) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        java.sql.Timestamp tp = java.sql.Timestamp.valueOf(dbNow);
        jdbcTemplate.batchUpdate(insertSql, attrList, 5000, (ps, argument) -> {
            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setLong(3, tenantId);
            ps.setString(4, argument.getKeyId());
            ps.setString(5, argument.getAttrName());
            ps.setString(6, StringUtils.isEmpty(argument.getAttrValue()) ? ""
                            : this.replaceAttrValue(argument.getAttrValue()));
            ps.setTimestamp(7, tp);
            ps.setTimestamp(8, tp);
            ps.setString(9, argument.getAttrId());
            ps.setString(10, argument.getCid());

        });
    }

    private void batchUpdateAttr(Long tenantId, String tableName, String mainTableKey, LocalDateTime dbNow, Long userId,
                    List<MtCommonExtendAttrVO4> attrList) {
        String updateSql = "UPDATE " + tableName + " SET ATTR_VALUE = ?, LAST_UPDATE_DATE = ?, LAST_UPDATED_BY = ? "
                        + " WHERE " + mainTableKey + " = ? AND ATTR_NAME = ? AND TENANT_ID = ? AND ATTR_ID=?";

        java.sql.Timestamp tp = java.sql.Timestamp.valueOf(dbNow);
        jdbcTemplate.batchUpdate(updateSql, attrList, 5000, (ps, argument) -> {
            ps.setString(1, StringUtils.isEmpty(argument.getAttrValue()) ? ""
                            : this.replaceAttrValue(argument.getAttrValue()));
            ps.setTimestamp(2, tp);
            ps.setLong(3, userId);
            ps.setString(4, argument.getKeyId());
            ps.setString(5, argument.getAttrName());
            ps.setLong(6, tenantId);
            ps.setString(7, argument.getAttrId());
        });
    }

    private void batchInsertAttrHis(Long tenantId, String hisAttrTable, String hisTableKey, LocalDateTime dbNow,
                    Long userId, List<MtCommonExtendAttrHisVO6> attrHisList) {
        String insertSql = "INSERT INTO " + hisAttrTable + " (CREATED_BY,LAST_UPDATED_BY,EVENT_ID,TENANT_ID,"
                        + hisTableKey
                        + ",ATTR_NAME,ATTR_VALUE,CREATION_DATE,LAST_UPDATE_DATE,ATTR_HIS_ID,ATTR_ID,CID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

        java.sql.Timestamp tp = java.sql.Timestamp.valueOf(dbNow);
        jdbcTemplate.batchUpdate(insertSql, attrHisList, 5000, (ps, argument) -> {
            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setString(3, MtFieldsHelper.getOrDefault(argument.getEventId(), ""));
            ps.setLong(4, tenantId);
            ps.setString(5, argument.getHisKeyId());
            ps.setString(6, argument.getAttrName());
            ps.setString(7, StringUtils.isEmpty(argument.getAttrValue()) ? ""
                            : this.replaceAttrValue(argument.getAttrValue()));
            ps.setTimestamp(8, tp);
            ps.setTimestamp(9, tp);
            ps.setString(10, argument.getAttrHisId());
            ps.setString(11, argument.getAttrId());
            ps.setString(12, argument.getCid());
        });
    }
}
