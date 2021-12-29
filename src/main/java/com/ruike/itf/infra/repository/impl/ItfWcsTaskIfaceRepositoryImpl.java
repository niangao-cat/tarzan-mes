package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO1;
import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO2;
import com.ruike.itf.domain.entity.ItfWcsTaskIface;
import com.ruike.itf.domain.repository.ItfWcsTaskIfaceRepository;
import com.ruike.itf.infra.mapper.ItfWcsTaskIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.sys.MtException;
import liquibase.util.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 出库任务状态回传接口  API
 *
 * @author taowen.wang@hand-china.com 2021/7/2 16:31
 */
@Component
public class ItfWcsTaskIfaceRepositoryImpl extends BaseRepositoryImpl<ItfWcsTaskIface> implements ItfWcsTaskIfaceRepository {
    @Autowired
    private ItfWcsTaskIfaceMapper itfWcsTaskIfaceMapper;

    @Override
    public ItfWcsTaskIfaceDTO2 ItfWcsTaskIfaceUpdate(ItfWcsTaskIfaceDTO1 itfWcsTaskIfaceDTO1) {
        ItfWcsTaskIfaceDTO2 itfCommonReturnDTO = new ItfWcsTaskIfaceDTO2();
        if(StringUtils.isEmpty(itfWcsTaskIfaceDTO1.getTaskNum()) || StringUtils.isEmpty(itfWcsTaskIfaceDTO1.getTaskStatus())){
            itfCommonReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            itfCommonReturnDTO.setProcessMessage("参数传输错误");
            return itfCommonReturnDTO;
        }
        try {
            String taskNumBoolean = itfWcsTaskIfaceMapper.selectByTaskNum(itfWcsTaskIfaceDTO1.getTaskNum());
            if (StringUtils.isEmpty(taskNumBoolean)){
                throw new MtException("任务号"+itfWcsTaskIfaceDTO1.getTaskNum()+"不存在!");
            }
            itfWcsTaskIfaceMapper.updateByTaskNum(itfWcsTaskIfaceDTO1);
            itfCommonReturnDTO.setTaskNum(itfWcsTaskIfaceDTO1.getTaskNum());
            itfCommonReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            itfCommonReturnDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            return itfCommonReturnDTO;
        }catch (Exception e){
            itfCommonReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            itfCommonReturnDTO.setProcessMessage(e.getMessage());
            return itfCommonReturnDTO;
        }
    }
}
