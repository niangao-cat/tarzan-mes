package io.tarzan.common.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO2;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.api.dto.MtMultiLanguageDTO;
import io.tarzan.common.api.dto.MtMultiLanguageDTO2;
import io.tarzan.common.domain.entity.MtExtendSettings;

/**
 * 应用服务
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendSettingsService extends BaseService<MtExtendSettings> {

    /**
     * 前台扩展属性查询功能
     * 
     * @param tenantId
     * @param kid
     * @param tableName
     * @return List
     */
    List<MtExtendAttrDTO> attrQuery(Long tenantId, String kid, String tableName);

    /**
     * 前台扩展属性保存功能
     * 
     * @param tenantId
     * @param tableName
     * @param keyId
     * @param eventId
     * @param dto
     */
    void attrSave(Long tenantId, String tableName, String keyId, String eventId, List<MtExtendAttrDTO3> dto);


    void attrSave(Long tenantId, String tableName, String keyId, String eventId, List<MtExtendAttrDTO3> dto, String keyHisId);
    /**
     * 前台获取多语言功能
     * 
     * @param tenantId
     * @param dto
     * @return MtExtendAttrLangDTO2
     */
    List<MtMultiLanguageDTO2> getLangValue(Long tenantId, MtMultiLanguageDTO dto);

    /**
     * 通过拓展表名查询拓展字段集合
     * 
     * @author benjamin
     * @date 2019-08-13 21:30
     * @param tenantId 租户Id
     * @param dto 表名
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtExtendAttrDTO2> queryExtendSettingsForUi(Long tenantId, MtExtendAttrDTO4 dto, PageRequest pageRequest);

    /**
     * 保存拓展字段
     * 
     * @author benjamin
     * @date 2019-08-13 21:31
     * @param tenantId 租户Id
     * @param dto MtExtendAttrDTO2
     * @return MtExtendAttrDTO2
     */
    MtExtendAttrDTO2 saveExtendSettingsForUi(Long tenantId, MtExtendAttrDTO2 dto);

    /**
     * UI查询扩展表下的扩展字段
     *
     * @Author Xie.yiyang
     * @Date 2020/4/16 14:05
     * @param tenantId
     * @param extendTableDescId
     * @return java.util.List<io.tarzan.common.api.dto.MtExtendAttrDTO2>
     */
    List<MtExtendSettings> queryExtendsLimitTableForUi(Long tenantId, String extendTableDescId);


    /**
     * UI批量保存拓展字段
     *
     * @Author Xie.yiyang
     * @Date 2020/4/16 15:40
     * @param tenantId
     * @param dto
     * @return void
     */
    void saveExtendSettingsBatchForUi(Long tenantId, List<MtExtendSettings> dto);

}
