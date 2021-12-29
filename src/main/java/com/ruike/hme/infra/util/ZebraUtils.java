package com.ruike.hme.infra.util;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;

import javax.print.*;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterName;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * description 斑马打印工具类
 *
 * @author zhenyu.zhou 2021/04/13 14:57
 */
public class ZebraUtils {


    private final MtErrorMessageRepository mtErrorMessageRepository;

    public ZebraUtils(MtErrorMessageRepository mtErrorMessageRepository) {
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }


    /**
     * 通过网络连接斑马打印机,执行ZPL命令进行打印,设置超时时间
     * Function to print code Zpl to net zebra
     *
     * @param zpl
     * @param ip
     * @param port
     * @return
     * @author shicong.li
     */

    public static void printZplByNetWorkTimeOut(String zpl, String ip, int port, int time) {
        Socket clientSocket = new Socket();
        try {
            try {
                clientSocket.connect(new InetSocketAddress(ip, port), time);//设置连接请求超时时间1 s
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(zpl);
                clientSocket.close();
            } finally {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            }
        } catch (IOException e1) {
            throw new MtException("当前工位打印机无法打印标签 : " + ip + ":" + port + "，请检查打印机。", e1);
        }
    }

    /**
     * 通过网络连接斑马打印机,执行ZPL命令进行打印
     * Function to print code Zpl to net zebra
     *
     * @param zpl
     * @param ip
     * @param port
     * @return
     * @author zhenyu.zhou
     */

    public static void printZplByNetWork(String zpl, String ip, int port) {
        Socket clientSocket = null;
        try {
            try {
                clientSocket = new Socket(ip, port);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(zpl);
                clientSocket.close();
            } finally {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            }
        } catch (IOException e1) {
            throw new MtException("当前工位打印机无法打印标签 : " + ip + ":" + port + "，请检查打印机。", e1);
        }
    }

    /**
     * 通过USB连接斑马打印机,执行ZPL命令进行打印
     * Function to print code Zpl to local zebra(usb)
     *
     * @param zpl         code Zpl to print
     * @param printerName name of the selected printer
     * @return
     * @author zhenyu.zhou
     */

    public static void printZplByUSB(String zpl, String printerName) {
        try {

            PrintService psZebra = null;
            String sPrinterName = null;
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            //查询打印机
            for (int i = 0; i < services.length; i++) {
                PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
                sPrinterName = ((PrinterName) attr).getValue();
                if (sPrinterName.toLowerCase().indexOf(printerName.toLowerCase()) >= 0) {
                    psZebra = services[i];
                    break;
                }
            }

            if (psZebra == null) {
                throw new MtException("Zebra printer not found : " + printerName);
            }
            //执行ZPL打印
            DocPrintJob job = psZebra.createPrintJob();

            byte[] by = zpl.getBytes();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(by, flavor, null);
            job.print(doc, null);

        } catch (PrintException e) {
            throw new MtException("Cannot print label on this printer : " + printerName, e);
        }
    }

}
