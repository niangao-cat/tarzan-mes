package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsInspectionTimeDTO;
import com.ruike.qms.api.dto.QmsReceivedQuantutyDTO;
import com.ruike.qms.api.dto.QmsRqAndItDTO;
import com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO;
import com.ruike.qms.app.service.QmsReceivedInspectingBoardService;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsReceivedInspectingBoardMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.message.MessageClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: tarzan -mes
 * @description:
 * @author: han.zhang
 * @create: 2020/04/29 14:42
 */
@Service
public class QmsReceivedInspectingBoardServiceImpl implements QmsReceivedInspectingBoardService {
    @Autowired
    private QmsReceivedInspectingBoardMapper qmsReceivedInspectingBoardMapper;
    @Autowired
    private MessageClient messageClient;

    @Override
    public Page<QmsSelectCardDataReturnDTO> selectCardData(Long tenantId, PageRequest pageRequest) {
        Page<QmsSelectCardDataReturnDTO> qmsSelectCardDataReturnDTOS = PageHelper.doPage(pageRequest, () -> qmsReceivedInspectingBoardMapper.selectCardData(tenantId));

        messageClient.sendToAll("aaa","哈哈哈嘻嘻");
        return qmsSelectCardDataReturnDTOS;
    }

    @Override
    public List<QmsReceivedQuantutyDTO> selectReceivedQuantity(Long tenantId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConstants.Pattern.DATE);

        List<QmsReceivedQuantutyDTO> qmsReceivedQuantutyDTOS = new ArrayList<>(30);
        qmsReceivedQuantutyDTOS.addAll(qmsReceivedInspectingBoardMapper.selectReceivedQuantity(tenantId));
        //获取所有的已存在日期
        List<String> collectDays = qmsReceivedQuantutyDTOS.stream().map(QmsReceivedQuantutyDTO::getActualReceiveDate).collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-30);

        int days = 30;
        for (int i = 0; i < days; i++) {
            calendar.add(Calendar.DATE,1);

            Date date = calendar.getTime();
            String format = simpleDateFormat.format(date);
            //如果没有这一天的天数添加一个数量为0的
            if(!collectDays.contains(format)){
                QmsReceivedQuantutyDTO qmsReceivedQuantutyDTO = new QmsReceivedQuantutyDTO();
                qmsReceivedQuantutyDTO.setActualReceiveDate(format);
                qmsReceivedQuantutyDTO.setActualReceiveQty(Double.valueOf(QmsConstants.ConstantValue.ZERO));
                qmsReceivedQuantutyDTOS.add(i,qmsReceivedQuantutyDTO);
            }



        }
        return qmsReceivedQuantutyDTOS;
    }

    @Override
    public QmsRqAndItDTO selectYearRqAndIt(Long tenantId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(QmsConstants.ConstantValue.MONTH);
        //校验数量
        List<QmsReceivedQuantutyDTO> qmsReceivedQuantutyDTOS = qmsReceivedInspectingBoardMapper.selectYearReceivedQuantity(tenantId);
        List<String> quantityCollect = qmsReceivedQuantutyDTOS.stream().map(QmsReceivedQuantutyDTO::getActualReceiveDate).collect(Collectors.toList());
        //待检时长
        List<QmsInspectionTimeDTO> qmsInspectionTimeDTOS = qmsReceivedInspectingBoardMapper.selectYearInspectionTime(tenantId);
        List<String> timeCollect = qmsInspectionTimeDTOS.stream().map(QmsInspectionTimeDTO::getInspectionFinishDate).collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        int months = 12;
        for (int i = 0; i < months; i++) {
            calendar.add(Calendar.MONTH,1);

            Date date = calendar.getTime();
            String format = simpleDateFormat.format(date);
            //如果没有这一天的天数添加一个数量为0的
            if(!quantityCollect.contains(format)){
                QmsReceivedQuantutyDTO qmsReceivedQuantutyDTO = new QmsReceivedQuantutyDTO();
                qmsReceivedQuantutyDTO.setActualReceiveDate(format);
                qmsReceivedQuantutyDTO.setActualReceiveQty(Double.valueOf(QmsConstants.ConstantValue.ZERO));
                qmsReceivedQuantutyDTOS.add(i,qmsReceivedQuantutyDTO);
            }

            //如果没有这一天的天数添加一个数量为0的
            if(!timeCollect.contains(format)){
                QmsInspectionTimeDTO qmsInspectionTimeDTO = new QmsInspectionTimeDTO();
                qmsInspectionTimeDTO.setInspectionFinishDate(format);
                qmsInspectionTimeDTO.setInspectionTime(Double.valueOf(QmsConstants.ConstantValue.ZERO));
                qmsInspectionTimeDTOS.add(i,qmsInspectionTimeDTO);
            }
        }

        QmsRqAndItDTO qmsRqAndItDTO = new QmsRqAndItDTO();
        qmsRqAndItDTO.setInspectionTimeDTOList(qmsInspectionTimeDTOS);
        qmsRqAndItDTO.setReceivedQuantutyDTOList(qmsReceivedQuantutyDTOS);
        return qmsRqAndItDTO;
    }

}