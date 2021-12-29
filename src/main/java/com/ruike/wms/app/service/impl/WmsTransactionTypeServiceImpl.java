package com.ruike.wms.app.service.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsTransactionTypeService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsTransactionTypeMapper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import liquibase.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @Classname TransactionTypeServiceImpl
 * @Description 事务类型维护
 * @Date 2019/10/18 10:00
 * @Author by {HuangYuBin}
 */
@Service
@Slf4j
public class WmsTransactionTypeServiceImpl extends BaseServiceImpl<WmsTransactionType> implements WmsTransactionTypeService {
    @Autowired
    WmsTransactionTypeRepository transactionTypeRepository;
    @Autowired
    CustomSequence customSequence;
    @Autowired
    WmsTransactionTypeMapper mapper;
    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long tenantId, List<WmsTransactionTypeDTO> dtoList) {
        List<WmsTransactionType> insertList = new ArrayList<>();
        List<WmsTransactionType> updateList = new ArrayList<>();
        List<WmsTransactionTypeDTO> cacheList = new ArrayList<>();
        BeanCopier dtoCopier = BeanCopier.create(WmsTransactionTypeDTO.class, WmsTransactionType.class, false);
        BeanCopier copier = BeanCopier.create(WmsTransactionType.class, WmsTransactionTypeDTO.class, false);
        for (WmsTransactionTypeDTO dto : dtoList) {
            WmsTransactionType transactionType = new WmsTransactionType();
            //如果没有ID，默认为新建
            if (StringUtils.isEmpty(dto.getTransactionTypeId())) {
                dtoCopier.copy(dto, transactionType, null);
                BeanUtils.copyProperties(dto, transactionType);
                transactionType.setCid(Long.valueOf(customSequence.getNextKey("wms_transaction_type_cid_s")));
                transactionType.setTransactionTypeId(customSequence.getNextKey("wms_transaction_type_s"));
                transactionType.setTenantId(tenantId);
                insertList.add(transactionType);
            } else {
                //如果有ID，为更新
                dtoCopier.copy(dto, transactionType, null);
                transactionType.setTenantId(tenantId);
                updateList.add(transactionType);
            }
        }
        try {
            // 批量插入
            transactionTypeRepository.batchInsert(insertList);
            // 批量更新
            transactionTypeRepository.batchUpdateByPrimaryKeySelective(updateList);
            // 更新缓存
            insertList.forEach(rec -> {
                WmsTransactionTypeDTO dto = new WmsTransactionTypeDTO();
                copier.copy(rec, dto, null);
                cacheList.add(dto);
            });
            updateList.forEach(rec -> {
                WmsTransactionTypeDTO dto = new WmsTransactionTypeDTO();
                copier.copy(rec, dto, null);
                cacheList.add(dto);
            });

            transactionTypeRepository.batchUpdateCache(tenantId, cacheList);

        } catch (Exception e) {
            // 异常处理
            if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                MySQLIntegrityConstraintViolationException mySqlIntegrityConstraintViolationException = (MySQLIntegrityConstraintViolationException) e.getCause();
                if (SQLSTATE.equals(mySqlIntegrityConstraintViolationException.getSQLState())) {
                    throw new MtException("Z_TARANSACTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "Z_TARANSACTION_0001", "GENERAL"));
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertOrUpdate(Long tenantId, WmsTransactionTypeDTO dto) {
        List<WmsTransactionType> list = transactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class).andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, dto.getTransactionTypeCode())
                .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                .andNotEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_ID, dto.getTransactionTypeId(), true)).build());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new MtException("Z_TARANSACTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "Z_TARANSACTION_0001", "GENERAL"));
        }
        WmsTransactionType transactionType = new WmsTransactionType();
        BeanCopier dtoCopier = BeanCopier.create(WmsTransactionTypeDTO.class, WmsTransactionType.class, false);
        dtoCopier.copy(dto, transactionType, null);
        transactionType.setTenantId(tenantId);
        // 根据ID判断是否插入或更新
        if (StringUtils.isEmpty(dto.getTransactionTypeId())) {
            this.insertSelective(transactionType);
            dto.setTransactionTypeId(transactionType.getTransactionTypeId());
        } else {
            transactionTypeRepository.updateByPrimaryKey(transactionType);
        }
        // 更新缓存
        transactionTypeRepository.batchUpdateCache(tenantId, Collections.singletonList(dto));
    }

    @Override
    public Page<WmsTransactionTypeDTO> queryList(Long tenantId, PageRequest pageRequest, String transactionTypeCode, String description) {
        Page<WmsTransactionTypeDTO> response = PageHelper
                .doPageAndSort(pageRequest, () -> mapper.queryList(tenantId, transactionTypeCode, description));
        return response;
    }

}
