package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfMaterialLotIface;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO1;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据资源库
 *
 * @author yapeng.yao@hand-china.com 2020-09-01 09:32:35
 */
public interface ItfMaterialLotIfaceRepository extends BaseRepository<ItfMaterialLotIface> {

}
