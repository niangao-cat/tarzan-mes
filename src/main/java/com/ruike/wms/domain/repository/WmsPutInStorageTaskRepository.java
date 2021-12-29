package com.ruike.wms.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.wms.api.dto.WmsMaterialLotLineDetailDTO;
import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO2;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsInstructionVO;
import com.ruike.wms.domain.vo.WmsInstructionVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 入库上架任务表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-04-06 20:58:44
 */
public interface WmsPutInStorageTaskRepository extends BaseRepository<WmsPutInStorageTask>, AopProxy<WmsPutInStorageTaskRepository> {

    /**
     * 获取单据行下条码行明细
     *
     * @param tenantId
     * @param instructionNum
     * @param pageRequest
     * @return
     */
    Page<WmsMaterialLotLineDetailDTO> queryDetail(Long tenantId, String instructionNum, PageRequest pageRequest);

    /**
     * 根据物料批Id获取单据行
     *
     * @param tenantId
     * @param strings
     * @return
     */
    List<WmsInstructionLineVO> queryInstructionLineByLotIds(Long tenantId, List<String> strings);

    /**
     * 根据单据指令Id获取单据行
     *
     * @param tenantId
     * @param instructionDocId
     * @return
     */
    List<WmsInstructionLineVO> queryInstructionLine(Long tenantId, String instructionDocId);

    /**
     * 对单据行数据进行处理
     *
     * @param tenantId
     * @param instructionDocNum
     * @param instructionVO
     * @param lineList
     * @param enableDocFlag
     * @param putInFlag
     * @return
     */
    WmsInstructionVO handleData(Long tenantId, String instructionDocNum, WmsInstructionVO instructionVO,
                                List<WmsInstructionLineVO> lineList, String enableDocFlag, String putInFlag);

    /**
     * 获取库位
     * @param tenantId
     * @param dto
     * @return
     */
    WmsLocatorPutInStorageDTO getLocator(Long tenantId, WmsPutInStorageDTO2 dto);

    /**
     * 入库上架
     *
     * @param tenantId 租户Id
     * @param dto 物料行明细
     * @return WmsInstructionVO
     */
    WmsInstructionVO putInStorage(Long tenantId, WmsInstructionVO3 dto);
}
