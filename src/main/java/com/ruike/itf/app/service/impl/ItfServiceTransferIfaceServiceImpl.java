package com.ruike.itf.app.service.impl;

import com.ruike.itf.app.assembler.ServiceTransferIfaceAssembler;
import com.ruike.itf.app.service.ItfServiceTransferIfaceService;
import com.ruike.itf.domain.entity.ItfServiceTransferIface;
import com.ruike.itf.domain.repository.ItfServiceTransferIfaceRepository;
import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;
import com.ruike.itf.domain.vo.ServiceTransferIfaceResponseVO;
import com.ruike.itf.utils.SendESBConnect;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.itf.infra.constant.ItfConstant.InterfaceCode.ESB_SERVICE_TRANSFER;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.ERROR;

/**
 * 售后大仓回调应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2021-04-01 14:05:32
 */
@Service
@Slf4j
public class ItfServiceTransferIfaceServiceImpl implements ItfServiceTransferIfaceService {
    public final static String DATA_NAME = "ZITEM";
    public final static String RES_NAME = "RETURN";

    private final ServiceTransferIfaceAssembler assembler;
    private final ItfServiceTransferIfaceRepository repository;
    private final SendESBConnect sendESBConnect;

    public ItfServiceTransferIfaceServiceImpl(ServiceTransferIfaceAssembler assembler, ItfServiceTransferIfaceRepository repository, SendESBConnect sendESBConnect) {
        this.assembler = assembler;
        this.repository = repository;
        this.sendESBConnect = sendESBConnect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invoke(List<ServiceTransferIfaceInvokeVO> list) {
        //没有传入数据，直接返回
        if(CollectionUtils.isEmpty(list)){
            return;
        }

        // 初始化数据
        List<ItfServiceTransferIface> entities = assembler.invokeVoToEntityBatch(list);

        //新增接口表数据-独立事务
        repository.batchInsertRecord(entities);
        
        List<Map<String, Object>> fieldsMap = entities.stream().map(assembler::entityToFieldMap).collect(Collectors.toList());

        log.info("=======向SAP发送数据========>:{}", fieldsMap);

        // 推送数据
        Map<String, Object> resultMap = sendESBConnect.sendEsb(fieldsMap, DATA_NAME, "ItfServiceTransferIfaceServiceImpl.send", ESB_SERVICE_TRANSFER);

        // 解析结果
        List<ServiceTransferIfaceResponseVO> responses = ((List<Map<String, Object>>) resultMap.get(RES_NAME)).stream().map(ServiceTransferIfaceResponseVO::new).collect(Collectors.toList());
        log.info("=======SAP返回结果解析========>:{}", responses);
        Map<String, ServiceTransferIfaceResponseVO> responseMap = responses.stream().collect(Collectors.toMap(ServiceTransferIfaceResponseVO::getInterfaceId, a -> a, (a, b) -> a));
        // 回写接口表
        List<ItfServiceTransferIface> updateEntities = new ArrayList<>(entities.size());
        Exception exception = null;
        for (ItfServiceTransferIface rec:entities
             ) {
            ItfServiceTransferIface updateEntity = new ItfServiceTransferIface();
            try {
                updateEntity.setInterfaceId(rec.getInterfaceId());
                if (responseMap.containsKey(rec.getInterfaceId())) {
                    log.info("=======SAP返回数据在MES中存在========>");
                    ServiceTransferIfaceResponseVO res = responseMap.get(rec.getInterfaceId());
                    if (!(StringUtils.isBlank(res.getInterfaceId()) || StringUtils.isBlank(res.getProcessStatus()))) {
                        updateEntity.setProcessStatus(res.getProcessStatus());
                        updateEntity.setProcessMessage(res.getProcessMessage());
                        updateEntity.setDocument(res.getDocument());
                    } else {
                        updateEntity.setProcessStatus(ERROR);
                        updateEntity.setProcessMessage("未获取到处理信息");
                    }
                } else {
                    log.info("=======SAP返回数据在MES中不存在========>");
                    updateEntity.setProcessStatus(ERROR);
                    updateEntity.setProcessMessage("未获取到处理信息");
                }
            }catch (Exception e){
                updateEntity.setProcessStatus(ERROR);
                updateEntity.setProcessMessage(e.getMessage());
                exception = e;
            }
            updateEntities.add(updateEntity);
//            repository.insertRecord(rec);
        }

        //回写接口表状态-独立事务
        repository.batchUpdateRecord(updateEntities);

        if(Objects.nonNull(exception)){
            throw new MtException(exception.getMessage());
        }
    }

}
