package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeEoVO3;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname HmeMtEoService
 * @Description 执行作业管理-打印
 * @Date 2020/9/22 15:35
 * @Created by yaoyapeng
 */
public interface HmeMtEoService {

    /**
     * @param tenantId
     * @param type
     * @param hmeEoVO3List
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @Created by yaoyapeng
     */
    void print(Long tenantId, String type, List<HmeEoVO3> hmeEoVO3List, HttpServletResponse response);

    /**
     * 打印检验
     *
     * @param tenantId
     * @param hmeEoVO3List
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO3>
     * @author sanfeng.zhang@hand-china.com 2021/3/10 14:13
     */
    List<HmeEoVO3> printCheck(Long tenantId, List<HmeEoVO3> hmeEoVO3List);
}
