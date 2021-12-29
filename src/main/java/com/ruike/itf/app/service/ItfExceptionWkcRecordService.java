package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.IftSendOAExceptionStatusDTO;
import com.ruike.itf.api.dto.ItfExceptionWkcRecordDTO;
import com.ruike.itf.domain.vo.IftSendOAExceptionMsgVO;

import java.text.ParseException;

/**
 * @auther:lkj
 * @Date:2020/8/4 13:23
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口传参
 */
public interface ItfExceptionWkcRecordService {

    /**
     * 功能描述: 发送异常信息至ESB-该方法为测试方法-暂停使用
     *
     * @auther:lkj
     * @date:2020/8/4 下午1:45
     */
    void sendExceptionInfoWX(Long tenantId, ItfExceptionWkcRecordDTO itfBomComponentIface) throws Exception;


    /**
     * 功能描述: 发送异常信息至ESB
     *
     * @auther:lkj
     * @date:2020/8/4 下午1:45
     */
    void sendExceptionInfoEsb(Long tenantId, IftSendOAExceptionMsgVO iftSendOAExceptionMsgVO);

    /**
     * OA回传异常信息工单状态接口
     *
     * @param iftSendOAExceptionStatusDTO
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/1 11:00
     */

    IftSendOAExceptionStatusDTO exceptionWkcRecordStatus(IftSendOAExceptionStatusDTO iftSendOAExceptionStatusDTO) throws ParseException;
}
