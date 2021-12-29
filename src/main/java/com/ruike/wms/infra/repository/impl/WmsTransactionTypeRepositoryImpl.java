package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.infra.mapper.WmsTransactionTypeMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事务类型表 资源库实现
 *
 * @author yubin.huang@hand-china.com 2019-10-16 14:21:56
 */
@Component
public class WmsTransactionTypeRepositoryImpl extends BaseRepositoryImpl<WmsTransactionType> implements WmsTransactionTypeRepository {
    private final WmsTransactionTypeMapper wmsTransactionTypeMapper;
    private final RedisHelper redisHelper;

    public WmsTransactionTypeRepositoryImpl(WmsTransactionTypeMapper wmsTransactionTypeMapper, RedisHelper redisHelper) {
        this.wmsTransactionTypeMapper = wmsTransactionTypeMapper;
        this.redisHelper = redisHelper;
    }

    private static String generateCacheKey(Long tenantId) {
        return "tarzan:wms-trx-type:" + tenantId;
    }

    @Override
    public void initCache(Long tenantId) {
        List<WmsTransactionTypeDTO> list = wmsTransactionTypeMapper.queryList(tenantId, null, null);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String cacheKey = generateCacheKey(tenantId);
        Map<String, String> map = list.stream().collect(Collectors
                .toMap(WmsTransactionTypeDTO::getTransactionTypeCode, redisHelper::toJson));
        redisHelper.hshPutAll(cacheKey, map);
    }

    @Override
    public void updateCache(Long tenantId, String transactionTypeCode) {
        String cacheKey = generateCacheKey(tenantId);
        List<WmsTransactionTypeDTO> list = wmsTransactionTypeMapper.queryList(tenantId, transactionTypeCode, null);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String updateValue = redisHelper.toJson(list.get(0));
        redisHelper.hshPut(cacheKey, transactionTypeCode, updateValue);
    }

    @Override
    public void batchUpdateCache(Long tenantId, List<WmsTransactionTypeDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String cacheKey = generateCacheKey(tenantId);
        Map<String, String> map = list.stream().collect(Collectors
                .toMap(WmsTransactionTypeDTO::getTransactionTypeCode, redisHelper::toJson));
        redisHelper.hshPutAll(cacheKey, map);
    }

    @Override
    public WmsTransactionTypeDTO getTransactionType(Long tenantId, String transactionTypeCode) {
        String cacheKey = generateCacheKey(tenantId);
        Map<String, String> typeHashMap = redisHelper.hshGetAll(cacheKey);
        if (typeHashMap.isEmpty()) {
            initCache(tenantId);
        }
        String trxTypeRecord = redisHelper.hshGet(cacheKey, transactionTypeCode);
        if (StringUtils.isBlank(trxTypeRecord)) {
            updateCache(tenantId, transactionTypeCode);
        }
        return redisHelper.fromJson(trxTypeRecord, WmsTransactionTypeDTO.class);
    }

    @Override
    public List<WmsTransactionTypeDTO> batchGetTransactionType(Long tenantId, List<String> transactionTypeCodeList) {
        String cacheKey = generateCacheKey(tenantId);
        Map<String, String> typeHashMap = redisHelper.hshGetAll(cacheKey);
        if (typeHashMap.isEmpty()) {
            initCache(tenantId);
        }
        List<WmsTransactionTypeDTO> list = new ArrayList<>();
        transactionTypeCodeList.forEach(transactionTypeCode -> {
            String trxTypeRecord = redisHelper.hshGet(cacheKey, transactionTypeCode);
            if (StringUtils.isBlank(trxTypeRecord)) {
                updateCache(tenantId, transactionTypeCode);
                list.add(redisHelper.fromJson(trxTypeRecord, WmsTransactionTypeDTO.class));
            }
        });
        return list;
    }

    @Override
    public Map<String, WmsTransactionTypeDTO> getAllTransactionType(Long tenantId) {
        String cacheKey = generateCacheKey(tenantId);
        Map<String, String> typeHashMap = redisHelper.hshGetAll(cacheKey);
        if (typeHashMap.isEmpty()) {
            initCache(tenantId);
        }
        Map<String, WmsTransactionTypeDTO> map = new HashMap<>();
        typeHashMap.forEach((transactionTypeCode, trxTypeRecord) -> {
            map.put(transactionTypeCode, redisHelper.fromJson(trxTypeRecord, WmsTransactionTypeDTO.class));
        });
        return map;
    }

    @Override
    public int updateByPrimaryKey(WmsTransactionType obj) {
        return wmsTransactionTypeMapper.updateByPrimaryKey(obj);
    }

    @Override
    public List<WmsTransactionType> batchUpdateByPrimaryKey(List<WmsTransactionType> list) {
        list.forEach(this::updateByPrimaryKey);
        return list;
    }
}
