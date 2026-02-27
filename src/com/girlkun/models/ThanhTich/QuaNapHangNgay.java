///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.girlkun.models.ThanhTich;
//
//import com.girlkun.models.item.Item;
//import com.girlkun.models.player.Player;
//import com.girlkun.network.io.Message;
//import com.girlkun.services.InventoryServiceNew;
//import com.girlkun.services.ItemService;
//import com.girlkun.services.Service;
//import org.json.simple.JSONArray;
//
///**
// *
// * @author Administrator
// */
//public class QuaNapHangNgay {
//
//    public static int Type = 3;
//    public static String Name = "Quy Đổi Hằng Ngày";
//    public static String Info1 = "Quy Đổi Hằng Ngày";
//    public static String[] Info2 = new String[]{
//        "Quy Đổi ngày đạt 10.000Đ",
//        "Quy Đổi ngày đạt 20.000Đ",
//        "Quy Đổi ngày đạt 50.000Đ",
//        "Quy Đổi ngày đạt 100.000Đ",
//        "Quy Đổi ngày đạt 200.000Đ",
//        "Quy Đổi ngày đạt 500.000Đ",
//        "Quy Đổi ngày đạt 1.000.000Đ",};
//    public static int[] Money = new int[]{
//        10, 20, 5, 10, 10, 20, 50
//    };
//    public static boolean[] isFinish = new boolean[]{
//        false, false, false, false, false, false, false
//    };
//    public static boolean[] isRecieve = new boolean[]{
//        false, false, false, false, false, false, false
//    };
//    public static String[] Reward = new String[]{
//        "Đá bảo vệ",
//        "Đá bảo vệ",
//        "Item\nCấp 2",
//        "Item\nCấp 2",
//        "Đá ngũ sắc",
//        "Đá ngũ sắc",
//        "Đá ngũ sắc",};
//
//    public static void GetRecieve(Player pl, JSONArray dataArray) {
//        for (int i = 0; i < QuaNapHangNgay.isRecieve.length; i++) {
//            QuaNapHangNgay.isRecieve[i] = Integer.parseInt(String.valueOf(dataArray.get(i))) == 1;
//        }
//    }
//
//    public static void GetArchivemnt(Player pl, int index) {
//        QuaNapHangNgay.isFinish = CheckTaskDays.CheckNapTheDay(pl);
//        if (QuaNapHangNgay.isRecieve[index]) 
//        {
//            Service.gI().sendThongBao(pl, "Bạn đã nhận từ trước rồi");
//            return;
//        }
//        if (!QuaNapHangNgay.isFinish[index]) {
//            Service.gI().sendThongBao(pl, "Bạn chưa hoàn thành nhiệm vụ này");
//            return;
//        }
//        if (QuaNapHangNgay.isFinish[index] && !QuaNapHangNgay.isRecieve[index]) 
//        {
//            short tempID = 0;
//            int soluong = 0;
//            switch (index) {
//                case 0:
//                    tempID = 987;
//                    soluong = 10;
//                    break;
//                case 1:
//                    tempID = 987;
//                    soluong = 20;
//                    break;
//                case 2:
//                    tempID = 1278;
//                    soluong = 10;
//                    break;
//                case 3:
//                    tempID = 1278;
//                    soluong = 20;
//                    break;
//                case 4:
//                    tempID = 674;
//                    soluong = 10;
//                    break;
//                case 5:
//                    tempID = 674;
//                    soluong = 20;
//                    break;
//                case 6:
//                    tempID = 674;
//                    soluong = 50;
//                    break;
//            }
//            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
//                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//            } else {
//                QuaNapHangNgay.isRecieve[index] = true;
//                Item item = ItemService.gI().createNewItem(tempID, soluong);
//                InventoryServiceNew.gI().addItemBag(pl, item);
//                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn nhận được x" + soluong + " " + item.template.name);
//                QuaNapHangNgay.SendGetArchivemnt(pl, index);
//            }
//        }
//    }
//
//    public static void SendGetArchivemnt(Player pl, int index) {
//        Message msg = null;
//        try {
//            msg = new Message(-76);
//            msg.writer().writeByte(1);
//            msg.writer().writeByte(index);
//            pl.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            System.err.print("\nError at 90\n");
//            e.printStackTrace();
//        }
//    }
//
//    public static void SendThanhTich(Player pl) {
//        if (pl == null) {
//            return;
//        }
//        Message msg = null;
//        try {
//            msg = new Message(-76);
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF(QuaNapHangNgay.Name);
//            msg.writer().writeByte(QuaNapHangNgay.Type);
//            msg.writer().writeByte(QuaNapHangNgay.Info2.length);
//            for (int i = 0; i < QuaNapHangNgay.Info2.length; i++) {
//                msg.writer().writeUTF(QuaNapHangNgay.Info1);
//                msg.writer().writeUTF(QuaNapHangNgay.Info2[i]);
//                msg.writer().writeShort(QuaNapHangNgay.Money[i]);
//                msg.writer().writeBoolean(CheckTaskDays.CheckNapTheDay(pl)[i]);
//                msg.writer().writeBoolean(QuaNapHangNgay.isRecieve[i]);
//                msg.writer().writeUTF(QuaNapHangNgay.Reward[i]);
//            }
//            pl.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception E) {
//            System.err.print("\nError at 91\n");
//            E.printStackTrace();
//        }
//
//    }
//}
