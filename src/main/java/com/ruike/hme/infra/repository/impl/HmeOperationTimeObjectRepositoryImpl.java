package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import com.ruike.hme.domain.entity.HmeOperationTime;
import com.ruike.hme.domain.repository.HmeOperationTimeRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeOperationTimeObjectMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;
import com.ruike.hme.domain.repository.HmeOperationTimeObjectRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 时效要求关联对象表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:08
 */
@Component
@Slf4j
public class HmeOperationTimeObjectRepositoryImpl extends BaseRepositoryImpl<HmeOperationTimeObject> implements HmeOperationTimeObjectRepository {

    @Autowired
    private HmeOperationTimeObjectMapper hmeOperationTimeObjectMapper;
    @Autowired
    private HmeOperationTimeRepository hmeOperationTimeRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Override
    public BigDecimal StandardReqdTimeInProcessGet(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeOperationTimeObjectRepositoryImpl.StandardReqdTimeInProcessGet.Start dto={}", dto);
        BigDecimal standardReqdTimeInProcess = new BigDecimal(0);
        //1.基于工位查找hme_operation_time表
        int wkcRecordCount = hmeOperationTimeRepository.selectCount(new HmeOperationTime(){{
                                setTenantId(tenantId);
                                setEnableFlag(HmeConstants.ConstantValue.YES);
                                setWorkcellId(dto.getWorkcellId());
                            }});
        if(wkcRecordCount > 0){
            //1.1
            standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcSn(tenantId , dto , HmeConstants.OperationTimeAction.WKC);
            if(Objects.isNull(standardReqdTimeInProcess)){
                //1.2
                standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcWo(tenantId , dto , HmeConstants.OperationTimeAction.WKC);
                if(Objects.isNull(standardReqdTimeInProcess)){
                    //1.3
                    standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcMaterialVersion(tenantId , dto , HmeConstants.OperationTimeAction.WKC);
                    if(Objects.isNull(standardReqdTimeInProcess)){
                        //1.4
                        standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcMaterial(tenantId , dto , HmeConstants.OperationTimeAction.WKC);
                        if(Objects.isNull(standardReqdTimeInProcess)){
                            //1.5
                            standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcOrOperation(tenantId , dto , HmeConstants.OperationTimeAction.WKC);
                            if(Objects.isNull(standardReqdTimeInProcess)) {
                                //当前SN【${1}】 未维护时效要求
                                throw new MtException("HME_EO_JOB_SN_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_057", "HME", dto.getSnNum()));
                            }
                        }
                    }
                }
            }
        }else{
            //2.基于工艺查找hme_operation_time表
            int operationRecordCount = hmeOperationTimeRepository.selectCountByCondition(Condition
                    .builder(HmeOperationTime.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeOperationTime.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeOperationTime.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                            .andEqualTo(HmeOperationTime.FIELD_OPERATION_ID, dto.getOperationId()))
                    .build());
            if(operationRecordCount == 0){
                //当前SN【${1}】 未维护时效要求
                throw new MtException("HME_EO_JOB_SN_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_057", "HME",dto.getSnNum()));
            }

            //2.1
            standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcSn(tenantId , dto , HmeConstants.OperationTimeAction.OPERATION);
            if(Objects.isNull(standardReqdTimeInProcess)){
                //2.2
                standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcWo(tenantId , dto , HmeConstants.OperationTimeAction.OPERATION);
                if(Objects.isNull(standardReqdTimeInProcess)){
                    //2.3
                    standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcMaterialVersion(tenantId , dto , HmeConstants.OperationTimeAction.OPERATION);
                    if(Objects.isNull(standardReqdTimeInProcess)){
                        //2.4
                        standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcMaterial(tenantId , dto , HmeConstants.OperationTimeAction.OPERATION);
                        if(Objects.isNull(standardReqdTimeInProcess)){
                            //2.5
                            standardReqdTimeInProcess = hmeOperationTimeObjectMapper.queryStandardReqdTimeInProcessOfWkcOrOperation(tenantId , dto , HmeConstants.OperationTimeAction.OPERATION);
                            if(Objects.isNull(standardReqdTimeInProcess)) {
                                //当前SN【${1}】 未维护时效要求
                                throw new MtException("HME_EO_JOB_SN_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_057", "HME", dto.getSnNum()));
                            }
                        }
                    }
                }
            }
        }
        log.info("<====== HmeOperationTimeObjectRepositoryImpl.StandardReqdTimeInProcessGet.End");
        return standardReqdTimeInProcess;
    }

    /**
     *
     * @Description 批量查询所有SN时效时长
     *
     * @author yuchao.wang
     * @date 2020/10/21 20:39
     * @param tenantId 租户ID
     * @param dtos 参数
     * @return java.util.Map<java.lang.String,java.math.BigDecimal>
     *
     */
    @Override
    public Map<String, BigDecimal> StandardReqdTimeInProcessBatchGet(Long tenantId, List<HmeEoJobSnVO3> dtos) {
        log.info("<====== HmeOperationTimeObjectRepositoryImpl.StandardReqdTimeInProcessBatchGet.Start dtos={}", dtos);
        String operationTimeAction = HmeConstants.OperationTimeAction.WKC;
        String workcellId = dtos.get(0).getWorkcellId();
        String operationId = dtos.get(0).getOperationId();

        //1.基于工位查找hme_operation_time表
        int wkcRecordCount = hmeOperationTimeRepository.selectCount(new HmeOperationTime(){{
            setTenantId(tenantId);
            setEnableFlag(HmeConstants.ConstantValue.YES);
            setWorkcellId(workcellId);
        }});
        if(wkcRecordCount <= 0){
            //2.基于工艺查找hme_operation_time表
            int operationRecordCount = hmeOperationTimeRepository.selectCountByCondition(Condition
                    .builder(HmeOperationTime.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeOperationTime.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeOperationTime.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                            .andEqualTo(HmeOperationTime.FIELD_OPERATION_ID, operationId))
                    .build());
            if(operationRecordCount <= 0){
                //当前SN【${1}】 未维护时效要求
                throw new MtException("HME_EO_JOB_SN_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_057", "HME", dtos.get(0).getSnNum()));
            }
            operationTimeAction = HmeConstants.OperationTimeAction.OPERATION;
        }

        //1.1 基于EO批量查询所有数据
        List<String> eoIdList = dtos.stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        //hmeOperationTimeObjectMapper.batchQueryStandardReqdTimeInProcessOfWkcSn(tenantId, operationTimeAction, workcellId, operationId, eoIdList);
        //TODO 筛选没有找到时效的，再进行下一种方式查找

        log.info("<====== HmeOperationTimeObjectRepositoryImpl.StandardReqdTimeInProcessBatchGet.End");
        return null;
    }
}
