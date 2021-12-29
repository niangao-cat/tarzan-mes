package com.ruike.itf.domain.repository;

import java.util.List;

import com.ruike.itf.domain.entity.ItfServiceTransferIface;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 售后大仓回调资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-04-01 14:05:32
 */
public interface ItfServiceTransferIfaceRepository extends BaseRepository<ItfServiceTransferIface> {

    /**
     * 新增数据记录
     *
     * @param record 记录
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 04:22:03
     */
    int insertRecord(ItfServiceTransferIface record);

    /**
     * 批量新增数据记录
     *
     * @param recordList 记录集合
     * @return void
     * @author penglin.sui@hand-china.com 2021/4/5 19:32:03
     */
    void batchInsertRecord(List<ItfServiceTransferIface> recordList);

    /**
     * 批量更新数据记录
     *
     * @param recordList 记录集合
     * @return void
     * @author penglin.sui@hand-china.com 2021/4/5 19:45
     */
    void batchUpdateRecord(List<ItfServiceTransferIface> recordList);
}
