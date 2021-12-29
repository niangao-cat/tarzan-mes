package io.tarzan.common.app.service;


import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtGenTypeDTO;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO4;
import io.tarzan.common.domain.vo.MtGenTypeVO5;

/**
 * 类型应用服务
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenTypeService {

    /**
     * 下拉框查询(前台)
     * @author xiao.tang02@hand-china.com 2019年8月13日上午11:23:46
     * @param tenantId
     * @param condition
     * @return
     * @return List<MtGenType>
     */
    List<MtGenType> comboBoxUi(Long tenantId, MtGenTypeVO2 condition);
    
    /**
     * 获取类型列表
     * @author xiao.tang02@hand-china.com 2019年8月13日上午11:22:15
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return List<MtGenType>
     */
    Page<MtGenTypeVO4> listGenTypeForUi(Long tenantId, MtGenTypeVO3 condition, PageRequest pageRequest);

    /**
     * 新增&更新类型
     * @author xiao.tang02@hand-china.com 2019年8月13日上午11:22:29
     * @param tenantId
     * @param dto
     * @return void
     */
    String saveGenTypeForUi(Long tenantId, MtGenTypeDTO dto);

    /**
     * 删除类型
     * @author xiao.tang02@hand-china.com 2019年8月13日上午11:22:29
     * @param tenantId
     * @param list
     * @return void
     */
    void removeGenTypeForUi(Long tenantId, List<MtGenType> list);


    /**
     * 根据类型组获取服务包
     * @author xiao.tang02@hand-china.com 2019年8月15日下午4:44:12
     * @param tenantId
     * @param typeGroup
     * @return
     * @return List<MtGenTypeVO5>
     */
    List<MtGenTypeVO5> getModuleByTypeGroupForUi(Long tenantId, String typeGroup);
}
