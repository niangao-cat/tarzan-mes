package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.hme.infra.util.JsonUtils;
import com.ruike.itf.api.dto.FreezeDocRequestDTO;
import com.ruike.itf.api.dto.FreezeDocResponseDTO;
import com.ruike.itf.api.dto.FreezeDocSyncDTO;
import com.ruike.itf.app.service.ItfFreezeDocIfaceService;
import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import com.ruike.itf.domain.repository.ItfFreezeDocIfaceRepository;
import com.ruike.itf.utils.SendESBConnect;
import org.apache.commons.lang.StringUtils;
import org.hzero.core.base.AopProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.ruike.itf.infra.constant.ItfConstant.InterfaceCode.ESB_FREEZE_DOC_SYNC;

/**
 * 条码冻结接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
@Service
public class ItfFreezeDocIfaceServiceImpl implements ItfFreezeDocIfaceService, AopProxy<ItfFreezeDocIfaceServiceImpl> {

    private final HmeFreezeDocRepository freezeDocRepository;
    private final ItfFreezeDocIfaceRepository freezeDocIfaceRepository;
    private final SendESBConnect sendESBConnect;

    public ItfFreezeDocIfaceServiceImpl(HmeFreezeDocRepository freezeDocRepository, ItfFreezeDocIfaceRepository freezeDocIfaceRepository, SendESBConnect sendESBConnect) {
        this.freezeDocRepository = freezeDocRepository;
        this.freezeDocIfaceRepository = freezeDocIfaceRepository;
        this.sendESBConnect = sendESBConnect;
    }

    @Override
    public FreezeDocResponseDTO invoke(FreezeDocRequestDTO request) {
        FreezeDocResponseDTO response = FreezeDocResponseDTO.newInstance(request);
        // 获取错误消息，若能获取到直接返回
        FreezeDocRequestDTO.ValidResult result = request.validation(freezeDocRepository, freezeDocIfaceRepository);
        if (StringUtils.isNotBlank(result.getMessage())) {
            return response.error(result.getMessage());
        }

        try {
            self().save(request.getStatus(), result);
        } catch (Exception e) {
            return response.error(e.getMessage());
        }

        // 返回正确结果
        return response.success();
    }

    @Override
    public void send(Long tenantId, ItfFreezeDocIface iface) {
        FreezeDocSyncDTO dto = FreezeDocSyncDTO.toSync(iface, freezeDocRepository);
        Map<String, String> fieldsMap = JsonUtils.toStringMap2(dto);
        sendESBConnect.sendEsb(fieldsMap, "mesJdInfo", "ItfFreezeDocIfaceServiceImpl.send", ESB_FREEZE_DOC_SYNC);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(String status, FreezeDocRequestDTO.ValidResult result) {
        // 处理逻辑，更新接口表和业务表
        freezeDocRepository.save(result.getDoc().changeApprovalStatus(status));
        freezeDocIfaceRepository.save(result.getIface().updateStatus(status));
    }


}
