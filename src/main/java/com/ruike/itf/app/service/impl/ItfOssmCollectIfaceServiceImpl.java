package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.OssmCollectItfDTO;
import com.ruike.itf.app.service.ItfOssmCollectIfaceService;
import com.ruike.itf.domain.entity.ItfOssmCollectIface;
import com.ruike.itf.domain.repository.ItfOssmCollectIfaceRepository;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.mybatis.service.BaseServiceImpl;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 示波器数据采集接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2020-07-17 07:46:07
 */
@Service
public class ItfOssmCollectIfaceServiceImpl extends BaseServiceImpl<ItfOssmCollectIface> implements ItfOssmCollectIfaceService {

    @Autowired
    private ItfOssmCollectIfaceRepository itfOssmCollectIfaceRepository;

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    /**
     * 接口数据验证
     *
     * @param itfList  接口数据
     * @param tenantId 租户
     * @return boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/17 3:56 下午
     */
    private boolean validate(Long tenantId, List<ItfOssmCollectIface> itfList) {
        boolean validFlag = true;
        for (ItfOssmCollectIface itf : itfList) {
            String processMessage = "";
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getEquipmentCategory()), processMessage, "ITF_DATA_COLLECT_0002");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");
            HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(itf.getAssetEncoding());
            }});
            if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
                processMessage = InterfaceUtils.processErrorMessage(tenantId, ObjectUtil.isEmpty(hmeEquipmentFirst), processMessage, "ITF_DATA_COLLECT_0011");
            } else {
                processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(hmeEquipmentFirst.getEquipmentCategory()), processMessage, "ITF_DATA_COLLECT_0010");
                itf.setEquipmentCategory(hmeEquipmentFirst.getEquipmentCategory());
            }
            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfOssmCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<OssmCollectItfDTO> collectList) {
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }

        // 插入接口表
        List<ItfOssmCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(OssmCollectItfDTO.class, ItfOssmCollectIface.class, false);
        Date nowDate = new Date();
        for (OssmCollectItfDTO data : collectList) {
            ItfOssmCollectIface itf = new ItfOssmCollectIface();
            copier.copy(data, itf, null);
            itf.setProcessDate(nowDate);
            itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_NEW);
            list.add(itf);
        }
        list.forEach(this::insertSelective);

        // 验证接口表
        boolean validFlag = this.validate(tenantId, list);

        if (validFlag) {
            // 执行插入业务表逻辑，如果有必要
            //for (ItfOssmCollectIface data : list) {
            //    messageClient.sendToAll(data.getEquipmentCategory(), JSON.toJSONString(data));
            //}
        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

}
