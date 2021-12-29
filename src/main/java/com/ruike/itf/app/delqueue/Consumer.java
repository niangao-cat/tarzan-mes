package com.ruike.itf.app.delqueue;

import com.alibaba.fastjson.JSON;
import com.ruike.itf.api.dto.ItfExceptionUserInfoDTO;
import com.ruike.itf.app.weixin.WeiXinNotify;
import com.ruike.itf.app.weixin.vo.TaskCardP;
import com.ruike.itf.domain.vo.IftSendOAExceptionMsgVO;
import com.ruike.itf.domain.vo.IftSendWXExceptionMsgVO;

import java.util.List;
import java.util.concurrent.DelayQueue;

public class Consumer implements Runnable {
    // 延时队列 ,消费者从其中获取消息进行消费
    private DelayQueue<Message> queue;

    public Consumer(DelayQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message take = queue.take();
                int taskNum = queue.size();

                IftSendWXExceptionMsgVO vo = (IftSendWXExceptionMsgVO) take.getBody();
                ItfExceptionUserInfoDTO initiator = vo.getApplicant();// 发起人
                List<ItfExceptionUserInfoDTO> approvedBy = vo.getApprovedBy().getApprovedBy();// 通知人
                String msgContent = "\n";// 发送内容
                msgContent += "类　型：" + vo.getExceptionType() + "\n";
                msgContent += "名　称：" + vo.getExceptionName() + "\n";
                msgContent += "描　述：" + vo.getExceptionRemark() + "\n";
                msgContent += "时　间：" + vo.getCurrentTime() + "\n";
                msgContent += "发起人：" + initiator.getRealName() + "\n";
                msgContent += "电　话：" + initiator.getMobile() + "\n";
                String user = "";// 接收人
                for (int i = 0; i < approvedBy.size(); i++) {
                    user += approvedBy.get(i).getMobile() + "|";
                }
                String toUser = user.substring(0, user.length() - 1);
                TaskCardP taskCardP = new TaskCardP();
                taskCardP.setTouser(toUser);
                taskCardP.getTaskcard().setDescription(msgContent);
                System.out.println(JSON.toJSONString(taskCardP));
                WeiXinNotify.sendTextMessageToUser(taskCardP);
                if (taskNum == 0) {
                    queue.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
