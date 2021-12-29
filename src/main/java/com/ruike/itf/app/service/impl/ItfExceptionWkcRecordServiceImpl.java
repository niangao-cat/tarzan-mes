package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruike.hme.domain.entity.HmeExcWkcRecord;
import com.ruike.hme.domain.entity.HmeExcWkcRecordHis;
import com.ruike.hme.domain.repository.HmeExcWkcRecordHisRepository;
import com.ruike.hme.infra.mapper.HmeExcWkcRecordHisMapper;
import com.ruike.hme.infra.mapper.HmeExcWkcRecordMapper;
import com.ruike.itf.api.dto.IftSendOAExceptionStatusDTO;
import com.ruike.itf.api.dto.ItfExceptionDTO;
import com.ruike.itf.api.dto.ItfExceptionUserInfoDTO;
import com.ruike.itf.api.dto.ItfExceptionWkcRecordDTO;
import com.ruike.itf.app.delqueue.Consumer;
import com.ruike.itf.app.delqueue.Message;
import com.ruike.itf.app.service.ItfExceptionWkcRecordService;
import com.ruike.itf.domain.vo.IftSendOAExceptionMsgVO;
import com.ruike.itf.domain.vo.IftSendWXExceptionMsgVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.itf.utils.ThreadPoolUtils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @auther:lkj
 * @Date:2020/8/4 13:24
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口传参
 */
@Slf4j
@Service
public class ItfExceptionWkcRecordServiceImpl implements ItfExceptionWkcRecordService {

    @Autowired
    private HmeExcWkcRecordMapper hmeExcWkcRecordMapper;

    @Autowired
    private HmeExcWkcRecordHisMapper hmeExcWkcRecordHisMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private SendESBConnect sendESBConnect;

    private final int MM = 60000;// 分钟为单位(upgradeTime+MM)

    @Override
    public void sendExceptionInfoWX(Long tenantId, ItfExceptionWkcRecordDTO itfBomComponentIface) throws Exception {
        /**
         *
         * 功能描述: 发送异常信息至ESB-停止使用
         *
         * @auther:lkj
         * @date:2020/8/4 下午1:45
         * @param itfBomComponentIface
         * @return:void
         *
         */

        // 判断审批人信息
        if (itfBomComponentIface.getExceptions().size() == 0) {
            log.error("<===========================>没有审批人的存在，请维护审批人:{}",itfBomComponentIface);
            return;
        }
        List<ItfExceptionDTO> itfExceptionDTOS = itfBomComponentIface.getExceptions();
        itfBomComponentIface.setExceptions(itfExceptionDTOS);
        IftSendWXExceptionMsgVO wx = new IftSendWXExceptionMsgVO(itfBomComponentIface);
        // 创建延时队列
        DelayQueue<Message> queue = new DelayQueue<>();
        for (int i = 0; i < itfExceptionDTOS.size(); i++) {
            Integer upgradeTime = itfExceptionDTOS.get(i).getUpgradeTime();
            if (i == 0) {
                wx.setApprovedBy(itfExceptionDTOS.get(i));
                Message m1 = new Message(upgradeTime, wx, 100);
                queue.offer(m1);
            } else {
                wx.setApprovedBy(itfExceptionDTOS.get(i));
                Message m1 = new Message(upgradeTime, wx, 1000 * upgradeTime);
                queue.offer(m1);
            }
        }
        // 启动消费线程 消费添加到延时队列中的消息，前提是任务到了延期时间
        ExecutorService exec = ThreadPoolUtils.getThreadPool();
        exec.execute(new Consumer(queue));
        exec.shutdown();


    }

    /**
     * @param tenantId
     * @param iftSendOAExceptionMsgVO
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/31 20:39
     */
    @Override
    public void sendExceptionInfoEsb(Long tenantId, IftSendOAExceptionMsgVO iftSendOAExceptionMsgVO) {
        try {
            List<IftSendOAExceptionMsgVO> vos = new ArrayList<>();
            vos.add(iftSendOAExceptionMsgVO);
            Map<String, Object> resultMap = sendESBConnect.sendEsb(vos, "mesExceptionInfo",
                    "ItfExceptionWkcRecordServiceImpl.sendExceptionInfoEsb", ItfConstant.InterfaceCode.ESB_OA_EXCEPTION_INFO_SYNC);
            log.info("<==== ItfExceptionWkcRecordServiceImpl.sendExceptionInfoEsbStatus:{}", resultMap);
        } catch (Exception e) {
            log.error("<==== ItfExceptionWkcRecordServiceImpl.sendExceptionInfoEsb:{}" + e.getMessage(), iftSendOAExceptionMsgVO);
        }

    }

    /**
     * OA回传异常信息工单状态接口
     *
     * @param iftSendOAExceptionStatusDTO
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/1 11:00
     */
    @Override
    public IftSendOAExceptionStatusDTO exceptionWkcRecordStatus(IftSendOAExceptionStatusDTO iftSendOAExceptionStatusDTO) throws ParseException {
        // 查询该工单的状态
        String exceptionWkcRecordId = iftSendOAExceptionStatusDTO.getExceptionWkcRecordId();
        String status = iftSendOAExceptionStatusDTO.getStatus();
        String respondedBy = iftSendOAExceptionStatusDTO.getRespondedBy();
        String respondRemark = iftSendOAExceptionStatusDTO.getRespondRemark();
        String respondTime = iftSendOAExceptionStatusDTO.getRespondTime();
        HmeExcWkcRecord hmeExcWkcRecord = hmeExcWkcRecordMapper.selectByPrimaryKey(exceptionWkcRecordId);
        if (!Objects.isNull(hmeExcWkcRecord)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hmeExcWkcRecord.setRespondedBy(respondedBy);
            hmeExcWkcRecord.setExceptionStatus(status);
            hmeExcWkcRecord.setRespondRemark(respondRemark);
            hmeExcWkcRecord.setRespondTime(format.parse(respondTime));
            hmeExcWkcRecord.setClosedBy(Long.parseLong(respondedBy));
            hmeExcWkcRecord.setCloseTime(format.parse(respondTime));
            long exceptionLevel = Objects.isNull(hmeExcWkcRecord.getExceptionLevel()) ? 0L : hmeExcWkcRecord.getExceptionLevel();
            hmeExcWkcRecord.setExceptionLevel(exceptionLevel + 1L);
            hmeExcWkcRecordMapper.updateByPrimaryKeySelective(hmeExcWkcRecord);
            HmeExcWkcRecordHis hmeExcWkcRecordHis = new HmeExcWkcRecordHis();
            BeanUtils.copyProperties(hmeExcWkcRecord, hmeExcWkcRecordHis);
            hmeExcWkcRecordHis.setExceptionWkcRecordHisId(customDbRepository.getNextKey("hme_exc_wkc_record_his_s"));
            hmeExcWkcRecordHisMapper.insertSelective(hmeExcWkcRecordHis);
            return new IftSendOAExceptionStatusDTO();
        } else {
            iftSendOAExceptionStatusDTO.setMessage("该异常工单不存在，请查找MES系统管理人员核对！");
            return iftSendOAExceptionStatusDTO;
        }


    }

    private List<ItfExceptionDTO> approvedBy(Long tenantId, List<ItfExceptionDTO> approvedBy, String keyId) throws Exception {
        /**
         *
         * 功能描述: 计算发审批人数据
         *
         * @auther:lkj
         * @date:2020/8/4 下午2:26
         * @param tenantId
         * @param approvedBy
         * @return:java.util.List<com.ruike.itf.api.dto.ItfExceptionDTO>
         *
         */
        List<ItfExceptionDTO> itfExceptionDTOS = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        // 判断岗位下是否有分配员工
        approvedBy.forEach(user -> {
            if (user.getApprovedBy().size() == 0) {
                log.error("<===========================>该岗位下面没有维护审批人:{}",approvedBy);
                return;
            }
        });

        // 判断是否有同样的异常等级
        for (int i = 0; i < approvedBy.size(); i++) {
            if (set.add(approvedBy.get(i).getExceptionLevel())) {
                itfExceptionDTOS.add(approvedBy.get(i));
            } else {
                List<ItfExceptionUserInfoDTO> approvedBy1 = approvedBy.get(i).getApprovedBy();
                List<ItfExceptionUserInfoDTO> approvedBy2 = itfExceptionDTOS.get(i - 1).getApprovedBy();
                approvedBy1.forEach(itfUserInfoDTO -> {
                    approvedBy2.add(itfUserInfoDTO);
                });
                //去重
                List<ItfExceptionUserInfoDTO> collect = approvedBy2.stream().distinct().collect(Collectors.toList());
                itfExceptionDTOS.get(i - 1).setApprovedBy(collect);
            }
        }
        return itfExceptionDTOS;
    }


}
