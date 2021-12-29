package io.tarzan.common.domain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
 * 扩展说明表
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@ApiModel("扩展说明表")
@ModifyAudit
@MultiLanguage
@Table(name = "mt_extend_table_desc")
@CustomPrimary
public class MtExtendTableDesc extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EXTEND_TABLE_DESC_ID = "extendTableDescId";
    public static final String FIELD_ATTR_TABLE = "attrTable";
    public static final String FIELD_ATTR_TABLE_DESC = "attrTableDesc";
    public static final String FIELD_SERVICE_PACKAGE = "servicePackage";
    public static final String FIELD_MAIN_TABLE = "mainTable";
    public static final String FIELD_MAIN_TABLE_KEY = "mainTableKey";
    public static final String FIELD_HIS_TABLE = "hisTable";
    public static final String FIELD_HIS_TABLE_KEY = "hisTableKey";
    public static final String FIELD_HIS_ATTR_TABLE = "hisAttrTable";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_INITIAL_FLAG = "initialFlag";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6414582277273175997L;

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
    @ApiModelProperty("拓展表描述主键")
    @Id
    @Where
    private String extendTableDescId;
    @ApiModelProperty(value = "拓展表", required = true)
    @NotBlank
    @Where
    private String attrTable;
    @ApiModelProperty(value = "拓展表描述", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String attrTableDesc;
    @ApiModelProperty(value = "服务包", required = true)
    @NotBlank
    @Where
    private String servicePackage;
    @ApiModelProperty(value = "主表", required = true)
    @NotBlank
    @Where
    private String mainTable;
    @ApiModelProperty(value = "主表主键", required = true)
    @NotBlank
    @Where
    private String mainTableKey;
    @ApiModelProperty(value = "历史表")
    @Where
    private String hisTable;
    @ApiModelProperty(value = "历史表主键")
    @Where
    private String hisTableKey;
    @ApiModelProperty(value = "历史表扩展表")
    @Where
    private String hisAttrTable;
    @ApiModelProperty(value = "有效性", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "初始化标识")
    @Where
    private String initialFlag;
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
     * @return 拓展表描述主键
     */
    public String getExtendTableDescId() {
        return extendTableDescId;
    }

    public void setExtendTableDescId(String extendTableDescId) {
        this.extendTableDescId = extendTableDescId;
    }

    /**
     * @return 拓展表
     */
    public String getAttrTable() {
        return attrTable;
    }

    public void setAttrTable(String attrTable) {
        this.attrTable = attrTable;
    }

    /**
     * @return 拓展表描述
     */
    public String getAttrTableDesc() {
        return attrTableDesc;
    }

    public void setAttrTableDesc(String attrTableDesc) {
        this.attrTableDesc = attrTableDesc;
    }

    /**
     * @return 服务包
     */
    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public String getMainTableKey() {
        return mainTableKey;
    }

    public void setMainTableKey(String mainTableKey) {
        this.mainTableKey = mainTableKey;
    }

    public String getHisTable() {
        return hisTable;
    }

    public void setHisTable(String hisTable) {
        this.hisTable = hisTable;
    }

    public String getHisAttrTable() {
        return hisAttrTable;
    }

    public void setHisAttrTable(String hisAttrTable) {
        this.hisAttrTable = hisAttrTable;
    }

    /**
     * @return 历史表主键
     */
    public String getHisTableKey() {
        return hisTableKey;
    }

    public void setHisTableKey(String hisTableKey) {
        this.hisTableKey = hisTableKey;
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
     * @return 初始化标识
     */
    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
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

    // cacheKey=tarzan:ext-tab:tenantId
    private static String generateCacheKey(Long tenantId) {
        StringBuilder sb = new StringBuilder("tarzan:ext-tab:");
        sb = sb.append(tenantId);
        return sb.toString();
    }

    // hashKey=attrTable:lang
    private static String generateHashKey(String attrTable, String lang) {
        StringBuilder sb = new StringBuilder(attrTable);
        sb = sb.append(":").append(lang);
        return sb.toString();
    }

    public static void initCache(List<MtExtendTableDesc> extTables, RedisHelper redisHelper) {
        Map<Long, List<MtExtendTableDesc>> tableMap =
                extTables.stream().collect(Collectors.groupingBy(MtExtendTableDesc::getTenantId));
        for (Map.Entry<Long, List<MtExtendTableDesc>> entry : tableMap.entrySet()) {
            String cacheKey = generateCacheKey(entry.getKey());
            Map<String, String> map = entry.getValue().stream().collect(Collectors
                    .toMap(c -> generateHashKey(c.getAttrTable(), c.getLang()), c -> redisHelper.toJson(c)));
            redisHelper.hshPutAll(cacheKey, map);
        }
    }

    public static boolean existExtendTableDescCache(Long tenantId, String attrTable, String lang,
                                                    RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId);
        String hashKey = generateHashKey(attrTable, lang);
        return redisHelper.hasKey(cacheKey) && redisHelper.hshHasKey(cacheKey, hashKey);
    }

    public static MtExtendTableDesc getExtendTableDescCache(Long tenantId, String attrTable, String lang,
                                                            RedisHelper redisHelper) {
        MtExtendTableDesc result = null;
        String cacheKey = generateCacheKey(tenantId);
        String hashKey = generateHashKey(attrTable, lang);
        String json = redisHelper.hshGet(cacheKey, hashKey);
        if (StringUtils.isNotEmpty(json)) {
            result = redisHelper.fromJson(json, MtExtendTableDesc.class);
        }
        return result;
    }

    public static void refreshCache(Long tenantId, String lang, MtExtendTableDesc dto, RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId);
        String hashKey = generateHashKey(dto.getAttrTable(), lang);
        redisHelper.hshPut(cacheKey, hashKey, redisHelper.toJson(dto));
    }

    public static void deleteCache(Long tenantId, String attrTable, String lang, RedisHelper redisHelper) {
        String cacheKey = generateCacheKey(tenantId);
        String hashKey = generateHashKey(attrTable, lang);
        redisHelper.hshDelete(cacheKey, hashKey);
    }
}
