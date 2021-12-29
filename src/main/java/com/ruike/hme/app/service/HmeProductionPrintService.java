package com.ruike.hme.app.service;

import com.ruike.hme.api.controller.v1.HmeProductionPrintDTO;
import com.ruike.hme.api.dto.HmeProductionPrintDTO2;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import tarzan.order.api.dto.MtEoDTO5;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * description 打印类
 *
 * @author wengang.qiang@hand-china 2021/09/23 18:27
 */
public interface HmeProductionPrintService {
    /**
     * 打印方法
     *
     * @param tenantId 租户
     * @param printVO  参数
     */
    void boxPrints(Long tenantId, HmeProductionPrintVO printVO);

    /**
     * 条码打印二
     *
     * @param tenantId                   租户id
     * @param hmeProductionPrintSecondVO 打印参数
     */
    void boxPrintSecond(Long tenantId, HmeProductionPrintSecondVO hmeProductionPrintSecondVO);

    /**
     * 多模板同时打印查询界面
     *
     * @param tenantId 租户ID
     * @param dto 参数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/15 10:36:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO10>
     */
    List<HmeProductionPrintVO10> multiTemplateQuery(Long tenantId, HmeProductionPrintDTO dto);

    /**
     * 打印方法
     * 多模板同时打印
     *
     * @param tenantId 租户ID
     * @param dto      参数
     * @param response 响应体
     */
    void multiTemplatePrints(Long tenantId, HmeProductionPrintDTO dto, HttpServletResponse response);

    /**
     * 执行作业打印时查询internalCode
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 11:19:55
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO3
     */
    HmeProductionPrintVO9 internalCodeQuery(Long tenantId, List<MtEoDTO5> dtoList);

    /**
     * 迭代查询整机所有投料数据
     * 
     * @param tenantId 租户ID
     * @param dtoList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 10:01:41 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO4>
     */
    List<HmeProductionPrintVO4> assembleDataQuery(Long tenantId, List<MtEoDTO5> dtoList);

    /**
     * 查询AC
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 01:50:23
     * @return java.lang.String
     */
    HmeProductionPrintVO6 acQuery(Long tenantId, List<MtEoDTO5> dtoList, List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 查询DC
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 05:23:40
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO3>
     */
    HmeProductionPrintVO6 dcQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 查询泵浦源
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 05:27:02
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO3>
     */
    HmeProductionPrintVO6 pumpQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto, List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 输出光缆查询
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 04:38:09
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 opticalCableQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                            List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 航插型号查询
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 04:38:09
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 aerPlugModelQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                            List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 上位机版本
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 04:58:33
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 hostComVerQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                          List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList);

    /**
     * 主控板型号
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 06:56:11
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 mainControlModQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                              List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 主控板程序
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 07:15:59 
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 mainControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                  List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList);

    /**
     * 光模控制板型号
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @param materialQtyInfoList 物料数量信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 03:46:46 
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 optModeControlModel(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                              List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 光模控制板程序
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @param hmeEoJobSnList
     * @param hmeEoJobDataRecordList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 03:56:59 
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 optModeControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                  List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList);

    /**
     * 合束控制板型号
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @param materialQtyInfoList 物料数量信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 04:01:48 
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 comBundleControlModelQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                              List<HmeProductionPrintVO4> materialQtyInfoList);

    /**
     * 合束控制板程序
     *
     * @param tenantId 租户ID
     * @param dtoList 执行作业信息
     * @param dto 前面查询信息
     * @param hmeEoJobSnList
     * @param hmeEoJobDataRecordList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 04:10:54 
     * @return com.ruike.hme.domain.vo.HmeProductionPrintVO6
     */
    HmeProductionPrintVO6 comBundleControlProgramQuery(Long tenantId, List<MtEoDTO5> dtoList, HmeProductionPrintVO6 dto,
                                                     List<HmeEoJobSn> hmeEoJobSnList, List<HmeEoJobDataRecord> hmeEoJobDataRecordList);

    /**
     * 执行作业铭牌打印
     *
     * @param tenantId 租户ID
     * @param dto 前台信息
     * @param response 响应体
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 07:37:38
     * @return void
     */
    void eoNameplatePrint(Long tenantId, HmeProductionPrintVO9 dto, HttpServletResponse response);

    /**
     * 执行作业铭牌打印查询
     *
     * @param tenantId 租户ID
     * @param dto 前台信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 09:25:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO8>
     */
    List<HmeProductionPrintVO8> eoNameplatePrintQuery(Long tenantId, HmeProductionPrintVO9 dto);

    /**
     * 执行作业铭牌打印查询校验
     *
     * @param tenantId 租户ID
     * @param eoList 执行作业信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 09:13:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO8>
     */
    List<HmeProductionPrintVO8> eoNameplatePrintVerify(Long tenantId, List<MtEoDTO5> eoList);
}
