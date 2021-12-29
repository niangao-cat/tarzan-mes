package io.tarzan.common.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO3;
import io.tarzan.common.domain.vo.MtNumrangeObjectColumnVO;

/**
 * 编码对象属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectColumnService {


    /**
     * 获取编码对象属性列表
     * 
     * @author xiao.tang02@hand-china.com 2019年8月13日下午6:44:14
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     * @return List<MtNumrangeObjectColumn>
     */
    Page<MtNumrangeObjectColumnVO> listNumrangeObjectColumnForUi(Long tenantId, MtNumrangeObjectColumnDTO2 dto,
                                                                 PageRequest pageRequest);

    /**
     * 批量保存编码对象属性
     * 
     * @author xiao.tang02@hand-china.com 2019年8月12日下午5:06:42
     * @param tenantId
     * @param dto
     * @return void
     */
    String saveNumrangeObjectColumnForUi(Long tenantId, MtNumrangeObjectColumnDTO3 dto);

    /**
     * 批量保存编码对象属性
     * 
     * @author xiao.tang02@hand-china.com 2019年8月12日下午5:06:42
     * @param tenantId
     * @param list
     * @return void
     */
    void batchSaveNumrangeObjectColumnForUi(Long tenantId, List<MtNumrangeObjectColumnDTO3> list);

    /**
     * 获取类型组不为空的数据
     */
    String numrangeObjectColumnModuleForUi(Long tenantId, String objectId);
}
