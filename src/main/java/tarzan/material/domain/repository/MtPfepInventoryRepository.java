package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepInventoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryVO6;
import tarzan.material.domain.vo.MtPfepInventoryVO7;
import tarzan.material.domain.vo.MtPfepInventoryVO8;

import java.util.List;

/**
 * 物料存储属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryRepository
        extends BaseRepository<MtPfepInventory>, AopProxy<MtPfepInventoryRepository> {

    /**
     * pfepInventoryGet-获取物料存储属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventory pfepInventoryGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepDefaultManufacturingLocationGet-获取物料默认完工和默认发料位置
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventory pfepDefaultManufacturingLocationGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * materialIdentifyTypeValidate-验证物料标识类型是否满足管控要求
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/7/23
     */
    void materialIdentifyTypeValidate(Long tenantId, MtPfepInventoryVO3 dto);

    /**
     * materialPfepInventoryUpdate-物料存储属性新增&更新
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/9/19
     */
    String materialPfepInventoryUpdate(Long tenantId, MtPfepInventoryVO6 dto, String fullUpdate);

    /**
     * 获取物料pfep存储属性主键
     * @Author peng.yuan
     * @Date 2019/10/11 15:29
     * @param tenantId :
     * @param dto :
     * @return tarzan.material.domain.vo.MtPfepInventoryVO8
     */
    MtPfepInventoryVO8 pfepInventoryKidGet(Long tenantId, MtPfepInventoryVO7 dto);

    /**
     * 物料库存PFEP属性新增&更新扩展表属性
     * @Author peng.yuan
     * @Date 2019/11/19 11:12
     * @param tenantId : 
     * @param mtExtendVO10 :
     * @return void
     */
    void pfepInventoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * pfepInventoryGet-批量获取物料存储属性
     *
     * @param tenantId
     * @param voList
     * @return
     */
    List<MtPfepInventoryVO3> pfepInventoryBatchGet(Long tenantId, List<MtPfepInventoryVO> voList, List<String> fields);
}
