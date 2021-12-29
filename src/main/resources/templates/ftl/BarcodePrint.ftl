<!DOCTYPE html
        PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>条码打印</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<style>
    table {
        border-spacing: 2px;
    }

    td {
        font-family: Microsoft Yahei;
        font-size: 10px;
        padding-top: 5px;
        padding-left: 20px;
        font-weight: bold;
    }
</style>

<body>
<table style="margin-left: 10px;">
    <tr>
        <td colspan="1">
            <img src="${imagePath!}"
                 style="float: left;padding: 15px 0 0 15px;height: 30px ;width: 90px;"></img>
        </td>
        <td colspan="3">
            <img src="${barCodePath!}"
                 style="float: right;padding: 15px 0 0 15px;height: 40px ;width: 120px;margin: 0 0 0 30px;"></img>
        </td>
    </tr>
    <tr>
        <td colspan="3">供应商：${wmsMaterialLotPrintVO.supplierName!}</td>
    </tr>
    <tr>
        <td colspan="3">条码号：${wmsMaterialLotPrintVO.materialLotCode!}</td>
    </tr>
    <tr>
        <td colspan="1">物料：${wmsMaterialLotPrintVO.materialCode!}</td>
        <td colspan="1">版本：${wmsMaterialLotPrintVO.materialVersion!}</td>
    </tr>
    <tr>
        <td colspan="2">物料描述：${wmsMaterialLotPrintVO.materialName!}</td>
        <td rowspan="4" colspan="2">
            <img src="${qrCodePath!}"
                 style="float: right;height: 72px ;width: 72px;"></img></td>
    </tr>
    <tr>
        <td colspan="1">数量：${wmsMaterialLotPrintVO.qty!}</td>
        <td colspan="1">单位：${wmsMaterialLotPrintVO.uomName!}</td>
    </tr>
    <tr>
        <td colspan="2">接收批次：${wmsMaterialLotPrintVO.lot!}</td>
    </tr>
    <tr>
        <td colspan="2">供应商批次：${wmsMaterialLotPrintVO.supplierLot!}</td>
    </tr>
</table>
</body>
</html>