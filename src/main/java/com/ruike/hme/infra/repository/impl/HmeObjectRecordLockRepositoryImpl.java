package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockParamDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeObjectRecordLockMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import groovy.json.internal.Dates;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import org.opensaml.xml.signature.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 记录锁定表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
@Component
public class HmeObjectRecordLockRepositoryImpl extends BaseRepositoryImpl<HmeObjectRecordLock>
                implements HmeObjectRecordLockRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeObjectRecordLockMapper hmeObjectRecordLockMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private CustomSequence customSequence;

    @Override
    @ProcessLovValue
    public Page<HmeObjectRecordLockReturnDTO> listForUi(Long tenantId, HmeObjectRecordLockParamDTO dto,
                    PageRequest pageRequest) {
        Page<HmeObjectRecordLockReturnDTO> list = PageHelper.doPageAndSort(pageRequest,
                        () -> hmeObjectRecordLockMapper.queryLockData(tenantId, dto));
        list.forEach(lock -> {
            lock.setCreatedByName(userClient.userInfoGet(tenantId, lock.getCreatedBy()).getRealName());
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void lock(HmeObjectRecordLock recordLock) {
        // 清除过期锁
        clearExpireLock(Collections.singletonList(recordLock));
        // 查询当前锁定信息(索引查询)
        HmeObjectRecordLock lock = hmeObjectRecordLockMapper.selectOne(new HmeObjectRecordLock() {
            {
                setTenantId(recordLock.getTenantId());
                setObjectType(recordLock.getObjectType());
                setObjectRecordCode(recordLock.getObjectRecordCode());
            }
        });
        // 单据已被锁定则判断是否为当前用户锁定，未被锁定则创建
        if (!Objects.isNull(lock)) {
            // 获取当前用户信息
            Long userId = DetailsHelper.getUserDetails().getUserId();
            // 校验单据是否被其他用户锁定
            if (!lock.getCreatedBy().equals(userId)) {
                throw new MtException("HME_RECORD_LOCK_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                                recordLock.getTenantId(), "HME_RECORD_LOCK_0002", "HME",
                                recordLock.getObjectRecordCode(),
                                userClient.userInfoGet(recordLock.getTenantId(), lock.getCreatedBy()).getRealName()));
            }
        } else {
            // 默认设置失效日期为12小时后
            recordLock.setExpireDate(
                            Date.from(LocalDateTime.now().plusHours(12L).atZone(ZoneId.systemDefault()).toInstant()));
            self().insertSelective(recordLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void releaseLock(HmeObjectRecordLock recordLock, String releaseFlag) {
        // 清除过期锁
        clearExpireLock(Collections.singletonList(recordLock));
        // 查询当前用户锁定信息
        HmeObjectRecordLock lock = hmeObjectRecordLockMapper.selectOne(new HmeObjectRecordLock() {
            {
                setTenantId(recordLock.getTenantId());
                setObjectType(recordLock.getObjectType());
                setObjectRecordCode(recordLock.getObjectRecordCode());
            }
        });

        // 解除锁定
        if (!Objects.isNull(lock)) {
            // 获取当前用户信息
            Long userId = DetailsHelper.getUserDetails().getUserId();
            // 当前用户或者管理员前台解锁
            if (lock.getCreatedBy().equals(userId) || StringUtils.equals(HmeConstants.ConstantValue.YES, releaseFlag)) {
                self().deleteByPrimaryKey(lock);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void commonLockWo(HmeObjectRecordLock recordLock) {
        // 清除过期锁
        clearExpireLock(Collections.singletonList(recordLock));
        // 查询当前锁定信息(索引查询)
        HmeObjectRecordLock lock = hmeObjectRecordLockMapper.selectOne(new HmeObjectRecordLock() {
            {
                setTenantId(recordLock.getTenantId());
                setObjectType(recordLock.getObjectType());
                setObjectRecordCode(recordLock.getObjectRecordCode());
            }
        });
        // 已被锁定则直接报错
        if (!Objects.isNull(lock)) {
            throw new MtException("HME_RECORD_LOCK_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                    recordLock.getTenantId(), "HME_RECORD_LOCK_0002", "HME",
                    recordLock.getObjectRecordCode(),
                    userClient.userInfoGet(recordLock.getTenantId(), lock.getCreatedBy()).getRealName()));
        } else {
            // 默认设置失效日期为2小时后
            recordLock.setExpireDate(
                    Date.from(LocalDateTime.now().plusHours(2L).atZone(ZoneId.systemDefault()).toInstant()));
            self().insertSelective(recordLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchCommonLockObject(Long tenantId, List<HmeObjectRecordLock> recordLockList) {

        if(CollectionUtils.isEmpty(recordLockList)) {
            return;
        }

        // 清除过期锁
        clearExpireLock(recordLockList);

        // 查询当前锁定信息(索引查询)
        List<HmeObjectRecordLock> lockList = hmeObjectRecordLockMapper.batchQueryLockData(tenantId , recordLockList);

        // 已被锁定则直接报错
        if (CollectionUtils.isNotEmpty(lockList)) {

            List<Long> userIdList = new ArrayList<>();

            for (HmeObjectRecordLock lock : lockList
            ) {
                if(!userIdList.contains(lock.getCreatedBy())){
                    userIdList.add(lock.getCreatedBy());
                }
            }
            Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId ,userIdList);

            StringBuilder errorMessages = new StringBuilder();
            for (HmeObjectRecordLock lock : lockList
                 ) {
                MtUserInfo userInfo = userInfoMap.get(lock.getCreatedBy());
                if(errorMessages.length() == 0) {
                    errorMessages.append("数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                }else{
                    errorMessages.append("| 数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                }
            }

            //{1},无法进行操作!
            throw new MtException("HME_RECORD_LOCK_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_RECORD_LOCK_0003", "HME",errorMessages.toString()));
        } else {
            // 默认设置失效日期为2小时后
            recordLockList.forEach(recordLock -> {
                recordLock.setExpireDate(
                        Date.from(LocalDateTime.now().plusHours(2L).atZone(ZoneId.systemDefault()).toInstant()));
            });

            List<String> idList = customSequence.getNextKeys("hme_object_record_lock_s", recordLockList.size());
            List<String> cidList = customSequence.getNextKeys("hme_object_record_lock_cid_s", recordLockList.size());
            int index = 0;

            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //当前时间
            Date date = CommonUtils.currentTimeGet();

            for (HmeObjectRecordLock recordLock : recordLockList
            ) {
                recordLock.setLockId(idList.get(index));
                recordLock.setCid(Long.valueOf(cidList.get(index++)));
                recordLock.setObjectVersionNumber(1L);
                recordLock.setCreatedBy(userId);
                recordLock.setCreationDate(date);
                recordLock.setLastUpdatedBy(userId);
                recordLock.setLastUpdateDate(date);
            }
            //批量插入
            List<List<HmeObjectRecordLock>> splitSqlList = InterfaceUtils.splitSqlList(recordLockList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeObjectRecordLock> domains : splitSqlList) {
                hmeObjectRecordLockMapper.batchInsertLock(domains);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchCommonLockObject2(Long tenantId, List<HmeObjectRecordLock> recordLockList) {

        if(CollectionUtils.isEmpty(recordLockList)) {
            return;
        }

        // 清除过期锁
        clearExpireLock(recordLockList);

        // 查询当前锁定信息(索引查询)
        List<HmeObjectRecordLock> lockList = hmeObjectRecordLockMapper.batchQueryLockData(tenantId , recordLockList);

        // 已被锁定则直接报错
        if (CollectionUtils.isNotEmpty(lockList)) {

            List<Long> userIdList = new ArrayList<>();

            for (HmeObjectRecordLock lock : lockList
            ) {
                if(!userIdList.contains(lock.getCreatedBy())){
                    userIdList.add(lock.getCreatedBy());
                }
            }
            Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId ,userIdList);

            StringBuilder errorMessages = new StringBuilder();
            for (HmeObjectRecordLock lock : lockList
            ) {
                MtUserInfo userInfo = userInfoMap.get(lock.getCreatedBy());
                if(errorMessages.length() == 0) {
                    errorMessages.append("数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                }else{
                    errorMessages.append("| 数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                }
            }

            //{1},无法进行操作!
            throw new MtException("HME_RECORD_LOCK_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_RECORD_LOCK_0003", "HME",errorMessages.toString()));
        } else {
            // 默认设置失效日期为2小时后
            recordLockList.forEach(recordLock -> {
                recordLock.setExpireDate(
                        Date.from(LocalDateTime.now().plusHours(2L).atZone(ZoneId.systemDefault()).toInstant()));
            });

            List<String> idList = customSequence.getNextKeys("hme_object_record_lock_s", recordLockList.size());
            List<String> cidList = customSequence.getNextKeys("hme_object_record_lock_cid_s", recordLockList.size());
            int index = 0;

            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //当前时间
            Date date = CommonUtils.currentTimeGet();

            for (HmeObjectRecordLock recordLock : recordLockList
            ) {
                recordLock.setLockId(idList.get(index));
                recordLock.setCid(Long.valueOf(cidList.get(index++)));
                recordLock.setObjectVersionNumber(1L);
                recordLock.setCreatedBy(userId);
                recordLock.setCreationDate(date);
                recordLock.setLastUpdatedBy(userId);
                recordLock.setLastUpdateDate(date);
            }
            //批量插入
            try {
                List<List<HmeObjectRecordLock>> splitSqlList = InterfaceUtils.splitSqlList(recordLockList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeObjectRecordLock> domains : splitSqlList) {
                    hmeObjectRecordLockMapper.batchInsertLock(domains);
                }
            } catch (Exception e) {
                // 判断是否是唯一性索引 报错
                if (e.getMessage().contains("hme_object_record_lock_u1")) {
                    List<Long> userIdList = new ArrayList<>();
                    // 报唯一性约束 此处一定会找到数据
                    List<HmeObjectRecordLock> hmeObjectRecordLockList = hmeObjectRecordLockMapper.batchQueryLockData(tenantId , recordLockList);

                    for (HmeObjectRecordLock lock : hmeObjectRecordLockList
                    ) {
                        if(!userIdList.contains(lock.getCreatedBy())){
                            userIdList.add(lock.getCreatedBy());
                        }
                    }
                    Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId ,userIdList);

                    StringBuilder errorMessages = new StringBuilder();
                    for (HmeObjectRecordLock lock : hmeObjectRecordLockList
                    ) {
                        MtUserInfo userInfo = userInfoMap.get(lock.getCreatedBy());
                        if(errorMessages.length() == 0) {
                            errorMessages.append("数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                        }else{
                            errorMessages.append("| 数据" + lock.getObjectRecordCode() + "被用户" + userInfo.getRealName() + "锁定");
                        }
                    }
                    //{1},无法进行操作!
                    throw new MtException("HME_RECORD_LOCK_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_RECORD_LOCK_0003", "HME",errorMessages.toString()));
                } else {
                    throw new CommonException(e.getMessage());
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void batchReleaseLock(Long tenantId , List<HmeObjectRecordLock> billLockList, String releaseFlag) {
        if(CollectionUtils.isEmpty(billLockList)) {
            return;
        }
        
        // 清除过期锁
        clearExpireLock(billLockList);
        
        // 查询当前用户锁定信息
        List<HmeObjectRecordLock> lockList = hmeObjectRecordLockMapper.batchQueryLockData(tenantId , billLockList);

        // 解除锁定
        if (CollectionUtils.isNotEmpty(lockList)) {
            // 获取当前用户信息
            Long userId = DetailsHelper.getUserDetails().getUserId();
            List<HmeObjectRecordLock> deleteLockList = new ArrayList<>();
            // 当前用户或者管理员前台解锁
            for (HmeObjectRecordLock lock : lockList
                 ) {
                if (lock.getCreatedBy().equals(userId) || StringUtils.equals(HmeConstants.ConstantValue.YES, releaseFlag)) {
                    deleteLockList.add(lock);
                }
            }
            if(CollectionUtils.isNotEmpty(deleteLockList)){
                self().batchDeleteByPrimaryKey(deleteLockList);
            }
        }
    }

    /**
     * 清除过期锁
     *
     * @param recordLockList
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/12 18:05
     */

    private void clearExpireLock(List<HmeObjectRecordLock> recordLockList) {
        if(CollectionUtils.isEmpty(recordLockList)){
            return;
        }

        // 检查参数是否完整
        for (HmeObjectRecordLock recordLock : recordLockList
             ) {
            if (StringUtils.isBlank(recordLock.getDeviceCode()) || StringUtils.isBlank(recordLock.getObjectType())
                    || StringUtils.isBlank(recordLock.getObjectRecordCode())) {
                throw new MtException("HME_RECORD_LOCK_0001", mtErrorMessageRepository
                        .getErrorMessageWithModule(recordLock.getTenantId(), "HME_RECORD_LOCK_0001", "HME"));
            }
        }

        // 清除过期锁
        List<HmeObjectRecordLock> isLockList = hmeObjectRecordLockMapper.queryLock(recordLockList.get(0).getTenantId());
        if (isLockList.size() > 0) {
            self().batchDeleteByPrimaryKey(isLockList);
        }
        isLockList.clear();
    }
}
