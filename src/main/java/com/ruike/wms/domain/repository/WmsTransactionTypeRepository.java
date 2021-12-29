package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.wms.domain.entity.WmsTransactionType;

import java.util.List;
import java.util.Map;

/**
 * 事务类型表资源库
 *
 * @author yubin.huang@hand-china.com 2019-10-16 14:21:56
 */
public interface WmsTransactionTypeRepository extends BaseRepository<WmsTransactionType> {

    /**
     * 缓存初始化
     *
     * @param tenantId 租户
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:48:10
     */
    void initCache(Long tenantId);

    /**
     * 更新缓存
     *
     * @param tenantId            租户
     * @param transactionTypeCode 类型编码
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:53:06
     */
    void updateCache(Long tenantId, String transactionTypeCode);

    /**
     * 批量更新缓存
     *
     * @param tenantId 租户
     * @param list     新增或更新数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:53:40
     */
    void batchUpdateCache(Long tenantId, List<WmsTransactionTypeDTO> list);

    /**
     * 获取事务类型
     *
     * @param tenantId            租户
     * @param transactionTypeCode 类型编码
     * @return com.ruike.wms.api.dto.WmsTransactionTypeDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:54:29
     */
    WmsTransactionTypeDTO getTransactionType(Long tenantId, String transactionTypeCode);

    /**
     * 批量获取事务类型
     *
     * @param tenantId                租户
     * @param transactionTypeCodeList 类型编码列表
     * @return java.util.List<com.ruike.wms.api.dto.WmsTransactionTypeDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:55:10
     */
    List<WmsTransactionTypeDTO> batchGetTransactionType(Long tenantId, List<String> transactionTypeCodeList);

    /**
     * 获取全部事务类型
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.api.dto.WmsTransactionTypeDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 03:55:10
     */
    Map<String, WmsTransactionTypeDTO> getAllTransactionType(Long tenantId);
}
