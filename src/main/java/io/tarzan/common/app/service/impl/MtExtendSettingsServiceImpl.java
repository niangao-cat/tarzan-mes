package io.tarzan.common.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.helper.LanguageHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO2;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.api.dto.MtMultiLanguageDTO;
import io.tarzan.common.api.dto.MtMultiLanguageDTO2;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO2;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import io.tarzan.common.infra.mapper.MtExtendTableDescMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

/**
 * 应用服务默认实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Service
public class MtExtendSettingsServiceImpl extends BaseServiceImpl<MtExtendSettings> implements MtExtendSettingsService {

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendTableDescMapper mtExtendTableDescMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtExtendAttrDTO> attrQuery(Long tenantId, String kid, String tableName) {
        // 找到维护的扩展属性
        MtExtendTableDesc queryTable = new MtExtendTableDesc();
        queryTable.setTenantId(tenantId);
        queryTable.setAttrTable(tableName);
        queryTable.setEnableFlag("Y");
        MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
        if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
            throw new MtException("MT_GENERAL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0002", "GENERAL", tableName, "【API:attrPropertyQuery】"));
        }

        MtExtendSettings setting = new MtExtendSettings();
        setting.setTenantId(tenantId);
        setting.setExtendTableDescId(tableInfo.getExtendTableDescId());
        setting.setEnableFlag("Y");
        List<MtExtendSettings> settingsList = mtExtendSettingMapper.select(setting);
        List<MtExtendAttrDTO> result = new ArrayList<MtExtendAttrDTO>();
        if (CollectionUtils.isNotEmpty(settingsList)) {
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName(tableName);
            extendVO.setKeyId(kid);
            // 找到实际存在的属性值
            List<MtExtendAttrVO> attrValueList;
            if (StringUtils.isNotEmpty(kid)) {
                attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            } else {
                attrValueList = new ArrayList<MtExtendAttrVO>();
            }
            settingsList.stream().forEach(ever -> {
                MtExtendAttrDTO attrUiVO = new MtExtendAttrDTO();
                // 记录主表主键，为了展示多语言字段
                attrUiVO.setKid(kid);
                attrUiVO.setAttrName(ever.getAttrName());
                attrUiVO.setAttrMeaning(ever.getAttrMeaning());
                attrUiVO.setTlFlag(ever.getTlFlag());
                Optional<MtExtendAttrVO> attrVoOp = attrValueList.stream()
                                .filter(t -> ever.getAttrName().equals(t.getAttrName())).findFirst();
                if (attrVoOp.isPresent()) {
                    attrUiVO.setAttrValue(attrVoOp.get().getAttrValue());
                } else {
                    attrUiVO.setAttrValue("");
                }
                result.add(attrUiVO);
            });
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attrSave(Long tenantId, String tableName, String keyId, String eventId, List<MtExtendAttrDTO3> dto) {
        List<MtExtendVO5> list = new ArrayList<MtExtendVO5>();
        for (MtExtendAttrDTO3 ever : dto) {
            if (ever.get_tls() != null && !ever.get_tls().isEmpty()) {
                for (Map.Entry<String, Map<String, String>> entryFiled : ever.get_tls().entrySet()) {
                    for (Map.Entry<String, String> entryLang : entryFiled.getValue().entrySet()) {
                        MtExtendVO5 info = new MtExtendVO5();
                        info.setAttrName(entryFiled.getKey());
                        info.setAttrValue(entryLang.getValue());
                        info.setLang(entryLang.getKey());
                        list.add(info);
                    }
                }
            } else {
                MtExtendVO5 info = new MtExtendVO5();
                info.setAttrName(ever.getAttrName());
                info.setAttrValue(ever.getAttrValue());
                list.add(info);
            }
        }

        if(StringUtils.isEmpty(eventId)){
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("ATTR_UPDATE");
            eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        }

        if (CollectionUtils.isNotEmpty(list)) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, tableName, keyId, eventId, list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attrSave(Long tenantId, String tableName, String keyId, String eventId, List<MtExtendAttrDTO3> dto,
                    String keyHisId) {
        List<MtExtendVO5> list = new ArrayList<MtExtendVO5>();
        for (MtExtendAttrDTO3 ever : dto) {
            if (ever.get_tls() != null && !ever.get_tls().isEmpty()) {
                for (Map.Entry<String, Map<String, String>> entryFiled : ever.get_tls().entrySet()) {
                    for (Map.Entry<String, String> entryLang : entryFiled.getValue().entrySet()) {
                        MtExtendVO5 info = new MtExtendVO5();
                        info.setAttrName(entryFiled.getKey());
                        info.setAttrValue(entryLang.getValue());
                        info.setLang(entryLang.getKey());
                        list.add(info);
                    }
                }
            } else {
                MtExtendVO5 info = new MtExtendVO5();
                info.setAttrName(ever.getAttrName());
                info.setAttrValue(ever.getAttrValue());
                list.add(info);
            }

        }
        if (CollectionUtils.isNotEmpty(list)) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, tableName, keyId, eventId, list);
        }
    }

    @Override
    public List<MtMultiLanguageDTO2> getLangValue(Long tenantId, MtMultiLanguageDTO dto) {
        List<Language> languages = LanguageHelper.languages();

        List<MtMultiLanguageDTO2> result = new ArrayList<MtMultiLanguageDTO2>();
        if (StringUtils.isEmpty(dto.getKeyId())) {
            for (Language language : languages) {
                MtMultiLanguageDTO2 langBase = new MtMultiLanguageDTO2();
                langBase.setCode(language.getCode());
                langBase.setName(language.getName());
                langBase.setValue("");
                result.add(langBase);
            }
        } else {
            // 根据主键获取所有扩展属性
            MtExtendTableDesc queryTable = new MtExtendTableDesc();
            queryTable.setAttrTable(dto.getTableName());
            queryTable.setEnableFlag("Y");
            MtExtendTableDesc tableInfo = mtExtendTableDescMapper.selectOne(queryTable);
            if (null == tableInfo || StringUtils.isEmpty(tableInfo.getMainTableKey())) {
                throw new MtException("MT_GENERAL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0002", "GENERAL", dto.getTableName(), "【API:getLangValue】"));
            }
            List<MtExtendAttrVO2> attrValueList = mtExtendSettingMapper.getLangValue(tenantId, tableInfo.getAttrTable(),
                            tableInfo.getMainTableKey(), dto.getKeyId(), dto.getFieldName());
            for (Language language : languages) {
                MtMultiLanguageDTO2 langBase = new MtMultiLanguageDTO2();
                langBase.setCode(language.getCode());
                langBase.setName(language.getName());
                Optional<MtExtendAttrVO2> attrVoOp =
                                attrValueList.stream().filter(t -> language.getCode().equals(t.getLang())).findFirst();
                if (attrVoOp.isPresent()) {
                    langBase.setValue(attrVoOp.get().getAttrValue());
                } else {
                    langBase.setValue("");
                }
                result.add(langBase);
            }
        }
        return result;
    }

    @Override
    public List<MtExtendAttrDTO2> queryExtendSettingsForUi(Long tenantId, MtExtendAttrDTO4 dto,
                                                           PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtExtendSettingMapper.queryExtendAttrForUi(tenantId, dto));
    }

    @Override
    public MtExtendAttrDTO2 saveExtendSettingsForUi(Long tenantId, MtExtendAttrDTO2 dto) {
        if (dto == null) {
            return null;
        }
        MtExtendSettings mtExtendSettings = new MtExtendSettings();
        BeanUtils.copyProperties(dto, mtExtendSettings);
        mtExtendSettings.setTenantId(tenantId);
        String extendId = this.mtExtendSettingsRepository.settingBasicPropertyUpdate(tenantId, mtExtendSettings);

        MtExtendSettings newSettings = new MtExtendSettings();
        newSettings.setTenantId(tenantId);
        newSettings.setExtendId(extendId);
        newSettings = this.mtExtendSettingMapper.selectOne(newSettings);

        BeanUtils.copyProperties(newSettings, dto);
        return dto;
    }

    @Override
    public List<MtExtendSettings> queryExtendsLimitTableForUi(Long tenantId, String extendTableDescId) {

        if (Objects.isNull(extendTableDescId)) {
            return Collections.emptyList();
        }

        List<MtExtendSettings> mtExtendSettings = mtExtendSettingsRepository.selectSettingsByProperty(tenantId,
                extendTableDescId, MtBaseConstants.YES, null);

        if (CollectionUtils.isNotEmpty(mtExtendSettings)) {
            mtExtendSettings.sort(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()));
        }
        return mtExtendSettings;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExtendSettingsBatchForUi(Long tenantId, List<MtExtendSettings> dto) {
        if (CollectionUtils.isEmpty(dto)) {
            return;
        }
        if (dto.stream().anyMatch(
                t -> StringUtils.isEmpty(t.getAttrName()) || StringUtils.isEmpty(t.getAttrMeaning()))) {
            throw new MtException();
        }
        // 批量保存扩展字段
        mtExtendSettingsRepository.settingBasicPropertyBatchUpdate(tenantId, dto);
    }
}
