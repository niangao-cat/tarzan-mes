package io.tarzan.common.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.api.dto.MtExtendAttrDTO2;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendSettingsMapper extends BaseMapper<MtExtendSettings> {

	List<MtExtendSettingsVO2> customAttrQuery(@Param(value = "tenantId") Long tenantId,
	                                          @Param(value = "dto") MtExtendSettingsVO2 dto);

	List<MtExtendAttrVO> attrPropertyQuery(@Param(value = "tenantId") Long tenantId,
	                                       @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                       @Param(value = "keyId") String keyId, @Param(value = "attrList") List<MtExtendSettings> attrList);

	List<MtExtendAttrVO1> attrPropertyBatchQuery(@Param(value = "tenantId") Long tenantId,
	                                             @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                             @Param(value = "keyIds") List<String> keyIds);

	List<MtExtendAttrVO2> attrTableWithLangQuery(@Param(value = "tenantId") Long tenantId,
	                                             @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                             @Param(value = "keyId") String keyId,
	                                             @Param(value = "attrList") List<MtExtendSettings> allSettings);

	List<String> attrPropertyLimitKidQuery(@Param(value = "tenantId") Long tenantId,
	                                       @Param(value = "tableName") String tableName, @Param(value = "attrName") String attrName,
	                                       @Param(value = "attrValue") String attrValue, @Param(value = "mainTableKey") String mainTableKey,
	                                       @Param(value = "tlFlag") String tlFlag);

	List<MtExtendSettings> selectSettingsByProperty(@Param(value = "tenantId") Long tenantId,
	                                                @Param(value = "extendTableDescId") String extendTableDescId,
	                                                @Param(value = "enableFlag") String enableFlag, @Param(value = "attrNames") List<String> attrNames);

	List<String> attrBatchPropertyLimitKidQuery(@Param(value = "tenantId") Long tenantId,
	                                            @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                            @Param(value = "attrList") List<MtExtendVO3> attrList);

	List<MtExtendAttrVO2> existAttrQuery(@Param(value = "tenantId") Long tenantId,
	                                     @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                     @Param(value = "keyId") String keyId, @Param(value = "attrNameList") List<String> attrNameList);

	List<MtExtendAttrHisVO> attrHisBatchQuery(@Param(value = "tenantId") Long tenantId,
	                                          @Param(value = "hisTable") String hisTable, @Param(value = "mainTableKey") String mainTableKey,
	                                          @Param(value = "eventIds") List<String> eventIds);

	List<MtExtendAttrHisVO> attrHisQuery(@Param(value = "tenantId") Long tenantId,
	                                     @Param(value = "hisTable") String hisTable, @Param(value = "mainTableKey") String mainTableKey,
	                                     @Param(value = "dto") MtExtendAttrHisVO2 dto);

	List<MtExtendAttrVO2> getLangValue(@Param(value = "tenantId") Long tenantId,
	                                   @Param(value = "tableName") String attrTable, @Param(value = "mainTableKey") String mainTableKey,
	                                   @Param(value = "keyId") String keyId, @Param(value = "field") String field);

	List<MtExtendAttrDTO2> queryExtendAttrForUi(@Param(value = "tenantId") Long tenantId,
	                                            @Param(value = "table") MtExtendAttrDTO4 table);

	List<MtExtendAttrVO3> attrDataQuery(@Param(value = "tenantId") Long tenantId,
	                                    @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                    @Param(value = "kids") List<String> kids);

	List<MtExtendAttrVO1> attrsFromName(@Param(value = "tenantId") Long tenantId,
	                                    @Param(value = "tableName") String tableName,
	                                    @Param(value = "exAttrs") List<MtExtendSettingsVO2> exAttrs,
	                                    @Param(value = "mainTableKey") String mainTableKey);

	List<String> attrsFromNameLineToHeader(@Param(value = "tenantId") Long tenantId,
	                                       @Param(value = "dto") MtExtendSettingsVO2 dto, @Param(value = "tableName") String tableName,
	                                       @Param(value = "mainTableKey") String mainTableKey);


	List<MtExtendAttrVO1> attrsFromNameForKids(@Param(value = "tenantId") Long tenantId,
	                                           @Param(value = "tableName") String tableName, @Param(value = "kids") List<String> kids,
	                                           @Param(value = "extend") List<MtExtendSettingsVO2> extend,
	                                           @Param(value = "mainTableKey") String mainTableKey);

	List<MtExtendAttrVO3> existAttrQueryForKeyId(@Param(value = "tenantId") Long tenantId,
	                                             @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                             @Param(value = "keyId") String keyId);

	List<MtExtendAttrVO4> existAttrBatchQuery(@Param(value = "tenantId") Long tenantId,
	                                          @Param(value = "tableName") String tableName, @Param(value = "mainTableKey") String mainTableKey,
	                                          @Param(value = "attrPropertyList") List<MtCommonExtendVO6> attrPropertyList);

	List<MtExtendVO11> selectLatestHisIdsByMainT(@Param(value = "tenantId") Long tenantId,
	                                             @Param(value = "mainTableName") String mainTableName,
	                                             @Param(value = "mainTableKey") String mainTableKey, @Param(value = "keyIds") String keyIds);

	List<MtExtendVO12> selectEventIdsByLatestHisId(@Param(value = "tenantId") Long tenantId,
	                                               @Param(value = "hisTable") String hisTable, @Param(value = "hisTableKey") String hisTableKey,
	                                               @Param(value = "hisIds") List<String> hisIds);
}
