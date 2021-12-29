package com.ruike.itf.utils;

import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ItfObjectTransactionUtil {

    /**
     * 明细表未合并261、262事务明细数据按照 工单+物料+仓库+批次+预留项目号+预留项目行号+销售订单号+销售订单行号进行合并，
     * 合并结果为261事务数量-262事务数量，若相同维度下无261事务明细，262事务明细按照原逻辑汇总
     * 两者相减后 正数事物类型为 261，负数为262，如果为零状态默认S发送成功
     *
     * @param list
     * @param transactionList
     */
    public static void count261And262(List<ItfObjectTransactionIface> list, List<WmsObjectTransaction> transactionList) {
        // 提取261和262的数据
        List<ItfObjectTransactionIface> ifaceList261 =
                list.stream().filter(face -> "261".equals(face.getMoveType())).collect(Collectors.toList());
        List<ItfObjectTransactionIface> ifaceList262 =
                list.stream().filter(face -> "262".equals(face.getMoveType())).collect(Collectors.toList());
        // 若261和262任意一个事物不存在，则不修改汇总维度
        if (CollectionUtils.isEmpty(ifaceList261) || CollectionUtils.isEmpty(ifaceList262)) {
            return;
        }
        // 汇总在一起
        List<ItfObjectTransactionIface> falceListAll = new ArrayList<>(ifaceList261.size() + ifaceList262.size());
        falceListAll.addAll(ifaceList261);
        falceListAll.addAll(ifaceList262);

        List<WmsObjectTransaction> transaction261 =
                transactionList.stream().filter(transaction -> "261".equals(transaction.getMoveType())).collect(Collectors.toList());
        List<WmsObjectTransaction> transaction262 =
                transactionList.stream().filter(transaction -> "262".equals(transaction.getMoveType())).collect(Collectors.toList());

        List<WmsObjectTransaction> transactionListAll = new ArrayList<>(transaction261.size() + transaction262.size());
        transactionListAll.addAll(transaction261);
        transactionListAll.addAll(transaction262);
        // 删除261和262的数据
        list.removeIf(iface -> "261".equals(iface.getMoveType()) || "262".equals(iface.getMoveType()));
        transactionList.removeIf(transaction -> "261".equals(transaction.getMoveType()) || "262".equals(transaction.getMoveType()));
        // list转map进行数量汇总，分组条件 工单+物料+仓库+批次+预留项目号+预留项目行号+销售订单号+销售订单行号进行合并，合并后修改合并ID
        Map<String, List<WmsObjectTransaction>> mergeIdGroup = transactionListAll.stream().collect(Collectors.groupingBy(WmsObjectTransaction::getMergeId));
        String[] fields = {"workOrderNum", "materialCode", "warehouseCode", "lotNumber",
                "bomReserveNum", "bomReserveLineNum", "soNum", "soLineNum"};
        Map<String, List<ItfObjectTransactionIface>> faceGroup =
                falceListAll.stream().collect(Collectors.groupingBy(dto -> Utils.objectValue(dto, fields)));


        faceGroup.forEach((k, v) -> {
            ItfObjectTransactionIface iface = v.get(0);
            String pMergeId = iface.getMergeId();

            // 修改明细表的mergeId，都以第一个为主
            List<String> mergeIds = v.stream().map(ItfObjectTransactionIface::getMergeId).distinct().collect(Collectors.toList());
            mergeIds.forEach(mergeId -> {
                List<WmsObjectTransaction> wmsObjectTransactions = mergeIdGroup.get(mergeId);
                wmsObjectTransactions.forEach(transaction -> {
                    transaction.setMergeId(pMergeId);
                });
                transactionList.addAll(wmsObjectTransactions);
            });
            // 汇总数量，合并结果为261事务数量-262事务数量，若相同维度下无261事务明细，262事务明细按照原逻辑汇总
            BigDecimal qty261 = v.stream().filter(v261 -> "261".equals(v261.getMoveType())).map(ItfObjectTransactionIface::getTransactionQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal qty262 = v.stream().filter(v262 -> "262".equals(v262.getMoveType())).map(ItfObjectTransactionIface::getTransactionQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            qty261 = Objects.isNull(qty261) ? BigDecimal.ZERO : qty261;
            qty262 = Objects.isNull(qty262) ? BigDecimal.ZERO : qty262;
            BigDecimal subtract = qty261.subtract(qty262);
            // 两者相减后 正数事物类型为 261，负数为262，如果为零状态默认S发送成功
            // num是 -1, 0, 1，分别表示 负数、零、正数
            int num = subtract.signum();
            switch (num) {
                case -1:
                    iface.setMoveType("262");
                    iface.setProcessStatus("N");
                    iface.setRemark("261和262两者相减后，正数事物类型为261，负数为262");
                    subtract = subtract.abs();
                    break;
                case 1:
                    iface.setMoveType("261");
                    iface.setProcessStatus("N");
                    iface.setRemark("261和262两者相减后，正数事物类型为261，负数为262");
                    break;
                case 0:
                    iface.setProcessMessage("261和262计算后数量为0，不发送SAP，状态默认成功");
                    iface.setRemark("261和262计算后数量为0，不发送SAP，状态默认成功");
                    iface.setProcessStatus("S");
                    break;
                default:
                    break;
            }
            iface.setTransactionQty(subtract);
            list.add(iface);
        });


    }


    /**
     * 不需要返回值，直接修改引用
     *
     * @param list
     * @param transactionList
     * @param type
     */
    public static void sumSNObjectTransaction(List<ItfObjectTransactionIface> list, List<WmsObjectTransaction> transactionList, String type) {
        List<ItfObjectTransactionIface> snList = list.stream().filter(a -> Strings.isNotEmpty(a.getSnNum())).collect(Collectors.toList());
        // 删除SN数据，汇总后重新插入
        list.removeIf(a -> Strings.isNotEmpty(a.getSnNum()));
        // 分组SN数据,形成MAP
        Map<String, List<ItfObjectTransactionIface>> snMapList = new HashMap<>(snList.size());
        String[] mapKeys = {"MOVE_TYPE", "MOVE_REAS", "PDATE", "PLANT", "MATERIAL", "BATCH", "ENTRY_QNT", "ENTRY_UOM", "STGE_LOC",
                "MOVE_PLANT", "MOVE_STLOC", "COSTCENTER", "LIFNR", "CUSTOMER", "HTEXT", "ITEM_TEXT", "ORDERID", "RESERV_NO",
                "RES_ITEM", "NO_MORE_GR", "SALES_ORD", "S_ORD_ITEM", "PO_NUMBER", "PO_ITEM", "SPEC_STOCK", "GMCODE", "MOVE_BATCH",
                "VAL_SALES_ORD", "XSTOB"};
        for (int i = 0; i < snList.size(); i++) {
            Map<String, Object> snMap = materialMoveMap(snList.get(i), type, 0L, 0);
            String mapKey = Utils.getMapKey(snMap, mapKeys);
            List<ItfObjectTransactionIface> ifaces = snMapList.get(mapKey);
            if (CollectionUtils.isEmpty(ifaces)) {
                ifaces = new ArrayList<>();
                ifaces.add(snList.get(i));
            } else {
                ifaces.add(snList.get(i));
                snMapList.remove(mapKey);
            }
            snMapList.put(mapKey, ifaces);
        }
        // 合并数量，并且修改mergeId
        List<WmsObjectTransaction> snTransactions = transactionList.stream().filter(a -> Strings.isNotEmpty(a.getSnNum())).collect(Collectors.toList());
        // 删除明细中的SN数据，更新后重新插入
        transactionList.removeIf(a -> Strings.isNotEmpty(a.getSnNum()));
        for (String mapKey : snMapList.keySet()) {
            // 合并SN和事物数量
            List<String> snNums = new ArrayList<>();
            BigDecimal countQty = new BigDecimal("0");
            List<ItfObjectTransactionIface> ifaces = snMapList.get(mapKey);
            ItfObjectTransactionIface countIfaces = ifaces.get(0);
            for (ItfObjectTransactionIface iface : ifaces) {
                // 合并SN和事物数量
                snNums.add(iface.getSnNum());
                countQty = countQty.add(iface.getTransactionQty());
                // 更新MergeId
                for (int i = 0; i < snTransactions.size(); i++) {
                    if (snTransactions.get(i).getMergeId().equals(iface.getMergeId())) {
                        snTransactions.get(i).setMergeId(countIfaces.getMergeId());
                        transactionList.add(snTransactions.get(i));
                    }
                }
            }
            // 更新数量和SN号
            countIfaces.setSnNum(StringUtils.join(snNums, ","));
            countIfaces.setTransactionQty(countQty);
            list.add(countIfaces);
        }
    }

    /**
     * 物料移动回传ERP 组装接口数据,然后放入到MAP中
     *
     * @param iface
     * @param type
     * @param zbatch
     * @param itemId
     * @return
     */
    public static Map<String, Object> materialMoveMap(ItfObjectTransactionIface iface, String type, Long zbatch, int itemId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        Map<String, Object> map = new HashMap<>(100);
        map.put("ZITEM", itemId);
        map.put("ZBATCH", "Y".equals(type) ? zbatch : null);
        map.put("ZID", iface.getIfaceId());
        map.put("MOVE_TYPE", iface.getMoveType());
        map.put("MOVE_REAS", iface.getMoveReason());
        map.put("PDATE", format.format(iface.getAccountDate()));
        map.put("PLANT", iface.getPlantCode());
        //临时解决。先不使用TransactionTime，这里自己new Date使用
//        map.put("DDATE", format.format(iface.getTransactionTime()));
        map.put("DDATE", format.format(now));
        map.put("MATERIAL", iface.getMaterialCode());
        map.put("ENTRY_QNT", iface.getTransactionQty());
        map.put("BATCH", iface.getLotNumber());
        map.put("ENTRY_UOM", iface.getTransactionUom());
        map.put("STGE_LOC", iface.getWarehouseCode());
        map.put("MOVE_PLANT", iface.getTransferPlantCode());
        map.put("MOVE_STLOC", iface.getTransferWarehouseCode());
        map.put("COSTCENTER", iface.getCostcenterCode());
        map.put("LIFNR", iface.getSupplierCode());
        map.put("CUSTOMER", iface.getCustomerCode());
        //map.put("GDF_HTEXT", iface.getSourceDocNum());
        map.put("HTEXT", iface.getSourceDocNum());
        map.put("ITEM_TEXT", iface.getSourceDocLineNum());
        map.put("ORDERID", iface.getWorkOrderNum());
        map.put("RESERV_NO", iface.getBomReserveNum());
        map.put("RES_ITEM", iface.getBomReserveLineNum());
        map.put("NO_MORE_GR", iface.getCompleteFlag());
        map.put("SALES_ORD", iface.getSoNum());
        map.put("S_ORD_ITEM", iface.getSoLineNum());
        map.put("SERIALNO", iface.getSnNum());
        map.put("PO_NUMBER", iface.getPoNum());
        map.put("PO_ITEM", iface.getPoLineNum());
        map.put("SPEC_STOCK", iface.getSpecStockFlag());
        map.put("GMCODE", iface.getGmcode());
        map.put("MOVE_BATCH", iface.getTransferLotNumber());
        map.put("VAL_SALES_ORD", iface.getTransferSoNum());
        map.put("XSTOB", iface.getBackFlag());
        map.put("UNLOAD_PT", iface.getRemark());
        map.put("VAL_S_ORD_ITEM", iface.getTransferSoLineNum());
        map.put("ORDER_ITNO", iface.getAttribute30());
        return map;
    }


    /**
     * 组装生产报功数据
     *
     * @param workReport
     * @return
     */
    public static List<Map<String, Object>> productionData(List<ItfObjectTransactionIface> workReport) {
        // 组装数据
        List<Map<String, Object>> erpDate = new ArrayList<>();
        for (ItfObjectTransactionIface iface : workReport) {
            Map<String, Object> map = new HashMap<>(100);
            map.put("ZID", iface.getIfaceId());
            map.put("POSTG_DATE", iface.getAccountDate());
            map.put("YIELD", iface.getTransactionQty());
            map.put("CONF_QUAN_UNIT", iface.getTransactionUom());
            map.put("ORDERID", iface.getWorkOrderNum());
            map.put("OPERATION", iface.getOperationSequence());
            map.put("SALES_ORD", iface.getTransferSoNum());
            map.put("S_ORD_ITEM", iface.getTransferSoLineNum());
            map.put("ILE01", iface.getLineAttribute1());
            map.put("ISM01", iface.getLineAttribute2());
            map.put("ILE02", iface.getLineAttribute3());
            map.put("ISM02", iface.getLineAttribute4());
            map.put("ILE03", iface.getLineAttribute5());
            map.put("ISM03", iface.getLineAttribute6());
            map.put("ILE04", iface.getLineAttribute7());
            map.put("ISM04", iface.getLineAttribute8());
            map.put("ILE05", iface.getLineAttribute9());
            map.put("ISM05", iface.getLineAttribute10());
            map.put("ILE06", iface.getLineAttribute11());
            map.put("ISM06", iface.getLineAttribute12());
            erpDate.add(map);
        }
        return erpDate;
    }

}
