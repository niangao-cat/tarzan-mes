package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockParamDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 记录锁定表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
public interface HmeObjectRecordLockRepository extends BaseRepository<HmeObjectRecordLock>, AopProxy<HmeObjectRecordLockRepository> {

    /**
     * UI查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/9/12 17:39
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO>
     */
    Page<HmeObjectRecordLockReturnDTO> listForUi(Long tenantId, HmeObjectRecordLockParamDTO dto, PageRequest pageRequest);

    /**
     * 锁定
     *
     * @param recordLock
     * @author jiangling.zheng@hand-china.com 2020/9/12 17:39
     * @return void
     */
    void lock(HmeObjectRecordLock recordLock);

    /**
     * 解锁
     *
     * @param billLock
     * @param releaseFlag
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/12 17:40
     */
    void releaseLock(HmeObjectRecordLock billLock, String releaseFlag);

    /**
     * 工单创建加锁（锁存在就报错）
     *
     * @param recordLock
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/7 17:32
     */
    void commonLockWo(HmeObjectRecordLock recordLock);

    /**
     * 对象批量加锁（锁存在就报错）
     *
     * @param recordLockList
     * @return void
     * @author penglin.sui@hand-china.com 2021/6/18 15:14
     */
    void batchCommonLockObject(Long tenantId,List<HmeObjectRecordLock> recordLockList);

    /**
     * 对象批量加锁（锁存在就报错 捕获唯一性约束）
     *
     * @param recordLockList
     * @return void
     * @author penglin.sui@hand-china.com 2021/6/18 15:14
     */
    void batchCommonLockObject2(Long tenantId,List<HmeObjectRecordLock> recordLockList);

    /**
     * 批量解锁
     *
     * @param tenantId
     * @param billLockList
     * @param releaseFlag
     * @return void
     * @author penglin.sui@hand-china.com 2020/9/12 17:40
     */
    void batchReleaseLock(Long tenantId , List<HmeObjectRecordLock> billLockList, String releaseFlag);
}
