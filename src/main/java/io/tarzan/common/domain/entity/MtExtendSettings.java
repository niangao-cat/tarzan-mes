package io.tarzan.common.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@ApiModel("扩展表设置")
@ModifyAudit
@MultiLanguage
@Table(name = "mt_extend_settings")
@CustomPrimary
public class MtExtendSettings extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EXTEND_ID = "extendId";
    public static final String FIELD_EXTEND_TABLE_DESC_ID = "extendTableDescId";
    public static final String FIELD_ATTR_NAME = "attrName";
    public static final String FIELD_ATTR_MEANING = "attrMeaning";
    public static final String FIELD_TL_FLAG = "tlFlag";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3616387598695801431L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("拓展字段主键")
    @Id
    @Where
    private String extendId;
    @ApiModelProperty(value = "拓展表描述主键", required = true)
    @NotBlank
    @Where
    @OrderBy
    private String extendTableDescId;
    @ApiModelProperty(value = "拓展字段", required = true)
    @NotBlank
    @Where
    private String attrName;
    @ApiModelProperty(value = "拓展字段描述", required = true)
    @NotBlank
    @Where
    @MultiLanguageField
    private String attrMeaning;
    @ApiModelProperty(value = "多语言标识", required = true)
    @NotBlank
    @Where
    private String tlFlag;
    @ApiModelProperty(value = "有效性", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @Cid
    @Where
    private Long cid;

    @Transient
    @ApiModelProperty(hidden = true)
    private transient String lang;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 拓展字段主键
     */
    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    /**
     * @return 拓展表
     */
    public String getExtendTableDescId() {
        return extendTableDescId;
    }

    public void setExtendTableDescId(String extendTableDescId) {
        this.extendTableDescId = extendTableDescId;
    }

    /**
     * @return 拓展字段
     */
    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * @return 拓展字段描述
     */
    public String getAttrMeaning() {
        return attrMeaning;
    }

    public void setAttrMeaning(String attrMeaning) {
        this.attrMeaning = attrMeaning;
    }

    /**
     * @return 多语言标识
     */
    public String getTlFlag() {
        return tlFlag;
    }

    public void setTlFlag(String tlFlag) {
        this.tlFlag = tlFlag;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getLang() {
        return lang;
    }

    @JsonIgnore
    public void setLang(String lang) {
        this.lang = lang;
    }

    // cacheKey=tarzan:ext-set:tenantId:extendTableDescId
    private static String generateCacheKey(Long tenantId, String extendTableDescId) {
        StringBuilder sb = new StringBuilder("tarzan:ext-set:");
        sb = sb.append(tenantId).append(":").append(extendTableDescId);
        return sb.toString();
    }

    // hashKey=extendId:lang
    private static String generateHashKey(String extendId, String lang) {
        StringBuilder sb = new StringBuilder();
        sb = sb.append(extendId).append(":").append(lang);
        return sb.toString();
    }

    public static void initCache(List<MtExtendSettings> extendSettingList, RedisHelper redisHelper) {
        Map<String, List<MtExtendSettings>> settingMap = extendSettingList.stream().collect(
                Collectors.groupingBy(c -> generateCacheKey(c.getTenantId(), c.getExtendTableDescId())));
        for (Map.Entry<String, List<MtExtendSettings>> entry : settingMap.entrySet()) {
            Map<String, String> map = entry.getValue().stream().collect(Collectors
                    .toMap(c -> generateHashKey(c.getExtendId(), c.getLang()), c -> redisHelper.toJson(c)));
            redisHelper.hshPutAll(entry.getKey(), map);
        }
    }

    public static boolean existSettingCache(Long tenantId, String extendTableDescId, String extendId, String lang,
                                            RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId, extendTableDescId);
        String hashKey = generateHashKey(extendId, lang);
        return redisHelper.hasKey(cacheKey) && redisHelper.hshHasKey(cacheKey, hashKey);
    }

    public static boolean existSettingCaches(Long tenantId, String extendTableDescId, String lang,
                                             RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId, extendTableDescId);
        return redisHelper.hasKey(cacheKey);
    }

    public static MtExtendSettings getSettingCache(Long tenantId, String extendTableDescId, String extendId, String lang,
                                                   RedisHelper redisHelper) {
        MtExtendSettings result = null;
        String cacheKey = generateCacheKey(tenantId, extendTableDescId);
        String hashKey = generateHashKey(extendId, lang);
        String json = redisHelper.hshGet(cacheKey, hashKey);
        if (StringUtils.isNotEmpty(json)) {
            result = redisHelper.fromJson(json, MtExtendSettings.class);
        }
        return result;
    }

    public static List<MtExtendSettings> getSettingCaches(Long tenantId, String extendTableDescId, String lang,
                                                          RedisHelper redisHelper) {
        List<MtExtendSettings> result = new ArrayList<>();
        String cacheKey = generateCacheKey(tenantId, extendTableDescId);
        if (redisHelper.hasKey(cacheKey)) {
            Map<String, String> settingMap = redisHelper.hshGetAll(cacheKey);
            settingMap = settingMap.entrySet().stream().filter(e -> e.getKey().contains(lang))
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            if (MapUtils.isNotEmpty(settingMap)) {
                settingMap.values().forEach(json -> {
                    result.add(redisHelper.fromJson(json, MtExtendSettings.class));
                });
            }
        }
        return result;
    }

    public static void refreshCache(Long tenantId, String lang, MtExtendSettings extendSetting,
                                    RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId, extendSetting.getExtendTableDescId());
        String hashKey = generateHashKey(extendSetting.getExtendId(), lang);
        redisHelper.hshPut(cacheKey, hashKey, redisHelper.toJson(extendSetting));
    }

}
