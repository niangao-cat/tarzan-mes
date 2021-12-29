package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeMaterialLotNcRecord;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.method.domain.entity.MtNcCode;

import java.util.*;

/**
 * 不良明细表资源库
 *
 * @author yuchao.wang@hand-china.com 2020-08-19 14:50:28
 */
public interface HmeMaterialLotNcRecordRepository extends BaseRepository<HmeMaterialLotNcRecord> {

    /**
     *
     * @Description 根据工艺ID查询可选的不良代码
     *
     * @author yuchao.wang
     * @date 2020/8/21 10:36
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return tarzan.method.domain.entity.MtNcCode
     *
     */
    List<MtNcCode> queryNcCodeByOperationId(Long tenantId, String operationId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(List<HmeMaterialLotNcRecord> insertList);

    /**
     *
     * @Description 根据条码ID删除不良明细表数据
     *
     * @author yuchao.wang
     * @date 2020/10/9 0:08
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return int
     *
     */
    int deleteNcRecordByMaterialLotId(Long tenantId, String materialLotId);
}
