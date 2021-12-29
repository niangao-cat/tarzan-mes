package io.tarzan.common.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.vo.*;

/**
 * 资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendSettingsRepository
                extends BaseRepository<MtExtendSettings>, AopProxy<MtExtendSettingsRepository> {

    /**
     * 拓展属性查询
     *
     * @param attrTable
     * @return List<MtExtendSettings>
     */
    List<MtExtendSettingsVO2> customAttrQuery(Long tenantId, String attrTable, String enableFlag);

    /**
     * 拓展表属性查询
     *
     * @param dto
     * @return List<MtExtendAttrVO>
     */
    List<MtExtendAttrVO> attrPropertyQuery(Long tenantId, MtExtendVO dto);

    /**
     * 拓展表属性批量查询
     *
     * @param dto
     * @return List<MtExtendAttrVO1>
     */
    List<MtExtendAttrVO1> attrPropertyBatchQuery(Long tenantId, MtExtendVO1 dto);

    /**
     * 拓展表属性查询(带多语言)
     *
     * @param tenantId
     * @param dto
     * @return List<MtExtendAttrVO>
     */
    List<MtExtendAttrVO2> attrTableWithLangQuery(Long tenantId, MtExtendVO dto);

    /**
     * 根据拓展属性获取主键
     *
     * @param dto
     * @return
     */
    List<String> attrPropertyLimitKidQuery(Long tenantId, MtExtendVO2 dto);

    /**
     * 根据多个拓展属性获取主键(目前非文档自带的API)
     *
     * @param dto
     * @return
     */
    List<String> attrBatchPropertyLimitKidQuery(Long tenantId, String tableName, Map<String, String> dto);

    /**
     * attrPropertyUpdate-新增&更新拓展表拓展属性
     *
     * @author chuang.yang
     * @date 2019/6/14
     * @param dto
     * @return void
     */
    List<String> attrPropertyUpdate(Long tenantId, String tableName, String keyId, String eventId,
                    List<MtExtendVO5> dto);

    /**
     * attrPropertyBatchUpdate-批量新增&更新拓展表拓展属性
     *
     * @Author Xie.yiyang
     * @Date 2019/12/31 18:05
     * @param tenantId
     * @param tableName
     * @param eventId
     * @param attrPropertyList
     * @return java.util.List<java.lang.String>
     */
    List<String> attrPropertyBatchUpdate(Long tenantId, String tableName, String eventId,
                    List<MtCommonExtendVO6> attrPropertyList);

    /**
     * 批量记录历史扩展表数据
     */
    List<String> attrHisBatchUpdate(Long tenantId, String tableName, Map<String, String> dto, String eventId);

    /**
     * 批量根据事件ID获取操作的扩展属性历史
     *
     * @param eventIds
     * @return
     */
    List<MtExtendAttrHisVO> attrHisBatchQuery(Long tenantId, List<String> eventIds, String tableName);

    /**
     * 根据属性获取操作的扩展属性历史
     *
     * @param dto
     * @return
     */
    List<MtExtendAttrHisVO> attrHisQuery(Long tenantId, MtExtendAttrHisVO2 dto, String tableName);

    List<MtExtendAttrVO3> attrDataQuery(Long tenantId, String tableName, String mainTableKey, List<String> kids);

    String getInsertAttrSql(Long tenantId, String tableName, String mainTableKey, String keyId, String attrName,
                    String attrValue, String lang, Date now, Long userId);

    /**
     * propertyLimitAttrPropertyQuery-根据拓展属性获取拓展属性信息
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/9/30
     */
    List<MtExtendVO9> propertyLimitAttrPropertyQuery(Long tenantId, MtExtendVO8 mtExtendVO8);

    /**
     * attrPropertyUpdateForIface-新增&更新拓展表拓展属性-接口专用
     *
     * @author chuang.yang
     * @date 2019/6/14
     * @param dto
     * @return void
     */
    List<String> attrPropertyUpdateForIface(Long tenantId, String tableName, Map<String, List<MtExtendVO5>> dto);

    /**
     * 根据 旧attrId数据复制新的attrId数据
     *
     * @author chuang.yang
     * @date 2019/12/30
     * @param attrTableName 扩展表名
     * @param newAttrId 复制出来的新attrId
     * @param oldAttrId 根据oldAttrId对应数据复制
     * @param mainKeyIdName 主表ID字段名称
     * @param mainKeyId 复制出来主表ID值
     * @param cid 复制出来数据的CID
     * @param userId 复制出来数据的创建人ID
     * @param now 处理时间格式化
     * @return java.lang.String
     */
    String getCopyAttrSql(String attrTableName, String newAttrId, String oldAttrId, String mainKeyIdName,
                    String mainKeyId, String cid, Long userId, String now);

    /**
     * 更新扩展表的主表ID为新的主表ID（替换）
     *
     * @author chuang.yang
     * @date 2019/12/30
     * @param attrTableName 扩展表名
     * @param mainKeyIdName 扩展表的主表ID名
     * @param newMainKeyId 替换目标主表ID值
     * @param oldMainKeyId 替换来源主表ID值
     * @return java.lang.String
     */
    String getRepleaceMainKeyIdSql(String attrTableName, String mainKeyIdName, String newMainKeyId,
                    String oldMainKeyId);

    List<MtExtendSettings> selectSettingsByProperty(Long tenantId, String extendTableDescId, String enableFlag,
                    List<String> attrNames);

    String settingBasicPropertyUpdate(Long tenantId, MtExtendSettings dto);

    /**
     * 批量保存扩展字段
     *
     * @Author Xie.yiyang
     * @Date 2020/4/16 15:58
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> settingBasicPropertyBatchUpdate(Long tenantId, List<MtExtendSettings> dto);

    /**
     * 批量保存扩展字段-无多语言处理
     * 
     * @param tenantId
     * @param tableName
     * @param eventId
     * @param dtoList
     * @author guichuan.li
     */
    void attrPropertyBatchUpdateNew(Long tenantId, String tableName, String eventId, List<MtCommonExtendVO7> dtoList);
}
