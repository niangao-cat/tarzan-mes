package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.vo.MtMaterialVO3;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
public interface MtMaterialLotRepository extends BaseRepository<MtMaterialLot>, AopProxy<MtMaterialLotRepository> {

    /**
     * materialLotPropertyGet-获取物料批属性
     *
     * @param tenantId
     * @param materialLotId
     * @author sen.luo
     * @date 2019/3/25
     */
    MtMaterialLot materialLotPropertyGet(Long tenantId, String materialLotId);

    /**
     * materialLotConsume-物料批消耗
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/25
     */
    void materialLotConsume(Long tenantId, MtMaterialLotVO1 dto);

    /**
     * sequenceLimitMaterialLotBatchConsume-物料批批量消耗
     *
     * @author chuang.yang
     * @date 2019/10/14
     * @param tenantId
     * @param dto
     * @return void
     */
    void sequenceLimitMaterialLotBatchConsume(Long tenantId, MtMaterialLotVO15 dto);

    /**
     * materialLotUpdate-物料批更新&新增并记录历史
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/25
     */
    MtMaterialLotVO13 materialLotUpdate(Long tenantId, MtMaterialLotVO2 dto, String fullUpdate);

    /**
     * materialLotBatchUpdate-物料批更新&新增并记录历史 2019-11-20 add params by peng.yuan
     * 
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param materialLotList
     * @param eventId
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO19>
     */
    List<MtMaterialLotVO19> materialLotBatchUpdate(Long tenantId, List<MtMaterialLotVO20> materialLotList,
                    String eventId, String fullUpdate);

    /**
     * materialLotBatchUpdate-物料批更新&新增并记录历史 预编译
     *
     * @author chaonan.hu
     * @date 2021/04/30
     * @param tenantId
     * @param materialLotList
     * @param eventId
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO19>
     */
    List<MtMaterialLotVO19> materialLotBatchUpdatePrecompile(Long tenantId, List<MtMaterialLotVO20> materialLotList,
                    String eventId);

    /**
     * materialLotPropertyBatchGet-批量获取物料批属性
     *
     * @param tenantId
     * @param materialLotIds
     * @author guichuan.li
     * @date 2019/3/26
     */
    List<MtMaterialLot> materialLotPropertyBatchGet(Long tenantId, List<String> materialLotIds);

    /**
     * materialLotPropertyBatchGetForUpdate-批量获取物料批属性
     *
     * @param tenantId
     * @param materialLotIds
     * @author penglin.sui
     * @date 2021/4/21
     */
    List<MtMaterialLot> materialLotPropertyBatchGetForUpdate(Long tenantId, List<String> materialLotIds);

    /**
     * propertyLimitMaterialLotQuery-根据属性获取物料批
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/26
     */
    List<String> propertyLimitMaterialLotQuery(Long tenantId, MtMaterialLotVO3 dto);

    /**
     * materialLotNextCodeGet-获取下一个物料批的编码
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/21
     */
    MtNumrangeVO5 materialLotNextCodeGet(Long tenantId, MtMaterialLotVO26 dto);

    /**
     * materialLotLimitMaterialQtyGet-获取指定物料批的物料数量
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/26
     */
    MtMaterialLotVO4 materialLotLimitMaterialQtyGet(Long tenantId, MtMaterialLotVO11 dto);

    /**
     * materialLotEnableValidate-物料批有效性验证
     *
     * @param tenantId
     * @param materialLotId
     * @author guichuan.li
     * @date 2019/3/26
     */
    void materialLotEnableValidate(Long tenantId, String materialLotId);

    /**
     * materialLotIdentify-物料批识别
     *
     * @param tenantId
     * @param identification
     * @author guichuan.li
     * @date 2019/3/26
     */
    List<String> materialLotIdentify(Long tenantId, String identification);

    /**
     * materialLotNextLotGet-获取下一个物料批批次
     *
     * @author sen.luo
     * @date 2019/3/27
     */
    String materialLotNextLotGet(Long tenantId);

    /**
     * materialLotLimitMaterialQtyBatchGet-批量获取指定物料批的物料数量
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return java.util.List<hmes.material_lot.view.MtMaterialLotVO5>
     */
    List<MtMaterialLotVO5> materialLotLimitMaterialQtyBatchGet(Long tenantId, List<String> materialLotIds);

    /**
     * materialLotReserveVerify-物料批预留验证
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return void
     */
    void materialLotReserveVerify(Long tenantId, MtMaterialLotVO6 dto);

    /**
     * materialLotReserve-物料批预留
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialLotReserve(Long tenantId, MtMaterialLotVO7 dto);

    /**
     * materialLotReserveCancelVerify-物料批预留取消验证
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param materialLotId
     * @return void
     */
    void materialLotReserveCancelVerify(Long tenantId, String materialLotId);

    /**
     * materialLotReserveCancel-物料批预留取消
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialLotReserveCancel(Long tenantId, MtMaterialLotVO7 dto);

    /**
     * materialLotIdentificationUpdate-更新物料批标识
     *
     * @author chuang.yang
     * @date 2019/4/5
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialLotIdentificationUpdate(Long tenantId, MtMaterialLotVO8 dto);

    /**
     * materialLotTransfer-物料批移动
     *
     * @Author lxs
     * @Date 2019/4/8
     * @param tenantId
     * @param dto
     * @Return
     */
    void materialLotTransfer(Long tenantId, MtMaterialLotVO9 dto);

    /**
     * materialLotBatchTransfer-物料批批量移动
     *
     * @author chuang.yang
     * @date 2019/10/10
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialLotBatchTransfer(Long tenantId, MtMaterialLotVO14 dto);

    /**
     * materialLotSplit-物料批拆分
     *
     * @Author lxs
     * @Date 2019/4/9
     * @Params [tenantId, dto]
     * @Return java.lang.String
     */
    String materialLotSplit(Long tenantId, MtMaterialVO3 dto);

    /**
     * materialLotMerge-物料批合并
     *
     * @Author lxs
     * @Date 2019/4/9
     * @Params [tenantId, dto]
     * @Return java.lang.String
     */
    String materialLotMerge(Long tenantId, MtMaterialLotVO10 dto);

    /**
     * 获取物料批拓展属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtExtendAttrVO> materialLotLimitAttrQuery(Long tenantId, MtMaterialLotAttrVO2 dto);

    /**
     * 新增&更新物料批拓展属性同时记录物料批拓展属性历史
     *
     * @Author lxs
     * @Params
     * @Return
     */
    void materialLotLimitAttrUpdate(Long tenantId, MtMaterialLotAttrVO3 dto);

    /**
     * 根据物料批拓展属性获取物料批
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> attrLimitMaterialLotQuery(Long tenantId, MtMaterialLotAttrVO1 dto);

    /**
     * materialLotAttrHisQuery-获取物料批拓展属性变更历史
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtMaterialLotAttrHisVO2> materialLotAttrHisQuery(Long tenantId, MtMaterialLotAttrHisVO1 dto);

    /**
     * 根据事件批量获取物料批拓展属性变更历史
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtMaterialLotAttrHisVO2> eventLimitMaterialLotAttrHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * 验证物料批是否盘点停用
     *
     * @param tenantId
     * @param materialLotId
     */
    String materialLotStocktakeVerify(Long tenantId, String materialLotId);

    /**
     * materialLotOwnerTransfer-物料批所有权移动
     *
     * @param tenantId
     * @param dto
     */
    void materialLotOwnerTransfer(Long tenantId, MtMaterialLotVO12 dto);

    /**
     * containerLimitMaterialLotLoadSequenceQuery-获取指定容器已装载的物料批
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO18>
     */
    List<MtMaterialLotVO18> containerLimitMaterialLotLoadSequenceQuery(Long tenantId, MtMaterialLotVO17 dto);

    /**
     * 根据属性获取物料批信息
     *
     * @Author peng.yuan
     * @Date 2019/10/18 16:50
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.inventory.domain.vo.MtMaterialLotVO22>
     */
    List<MtMaterialLotVO22> propertyLimitMaterialLotPropertyQuery(Long tenantId, MtMaterialLotVO21 dto);

    /**
     * materialLotInitialize-物料批初始化
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtMaterialLotVO13 materialLotInitialize(Long tenantId, MtMaterialLotVO23 dto);

    /**
     * lotLimitMaterialLotQuery-根据批次获取物料批信息
     *
     * @param tenantId
     * @param lot
     * @return
     */
    List<MtMaterialLotVO24> lotLimitMaterialLotQuery(Long tenantId, String lot);

    /**
     * 自定义根据顶层容器查询物料批Id
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11
     * @param tenantId, topContainerId]
     * @return java.util.List<java.lang.String>
     */
    List<String> selectLotIdByTopContainerId(Long tenantId, String topContainerId);

    /**
     * 物料批新增&更新扩展表属性
     *
     * @Author peng.yuan
     * @Date 2019/11/18 10:01
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void materialLotAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 根据 materialLotCode批量获取物料批
     */
    List<MtMaterialLot> materialLotByCodeBatchGet(Long tenantId, List<String> materialLotCodes);

    /**
     * materialLotBatchCodeGet-批量获取容器编码
     *
     * @author chuang.yang
     * @date 2019/12/3
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.vo.MtNumrangeVO8
     */
    MtNumrangeVO8 materialLotBatchCodeGet(Long tenantId, MtMaterialLotVO27 dto);

    /**
     * codeOrIdentificationLimitObjectGet-根据编码或标识获取目标装载对象
     *
     * @author chuang.yang
     * @date 2020/1/13
     * @param tenantId
     * @param dto
     * @return tarzan.inventory.domain.vo.MtMaterialLotVO29
     */
    MtMaterialLotVO29 codeOrIdentificationLimitObjectGet(Long tenantId, MtMaterialLotVO30 dto);

    /**
     * 根据eoId查找列表集合
     * 
     * @Author peng.yuan
     * @Date 2020/1/17 14:15
     * @param tenantId :
     * @param eoId :
     * @return java.util.List<tarzan.inventory.domain.entity.MtMaterialLot>
     */
    List<MtMaterialLot> selectListByEoId(Long tenantId, String eoId);


    /**
     * 物料批批量消耗-特定逻辑：不启用双单位，不做单位转换，同时使用现有量批量更新
     *
     * @author chuang.yang
     * @date 2021/4/22
     */
    String sequenceLimitMaterialLotBatchConsumeForNew(Long tenantId, MtMaterialLotVO31 dto);

    /**
     * materialLotAndInventoryUpdate-物料批与库存新增或更新
     *
     * @author chuang.yang
     * @date 2021/7/8
     */
    List<MtMaterialLotVO19> materialLotAccumulate(Long tenantId, MtMaterialLotVO42 dto);
}
