///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.girlkun.models.ThanhTich;
//
//import com.girlkun.models.player.Player;
//import com.girlkun.network.io.Message;
//import com.girlkun.services.Service;
//import org.json.simple.JSONArray;
//
///**
// *
// * @author Administrator
// */
//public class OnlineHangNgay {
//
//    public static int Type = 2;
//    public static String Name = "Hoạt động hằng ngày";
//    public static String[] Info1 = new String[]{
//        "Online",
//        "Online",
//        "Online",
//        "Online",
//        "Online",
//        "Online",
//        "Hoàn Thành",
//        "Hoàn Thành",
//        "Tham Gia",
//        "Chiến thắng",
//        "Tham Gia",};
//    public static String[] Info2 = new String[]{
//        "Online Tổng 10p",
//        "Online Tổng 30p",
//        "Online Tổng 60p",
//        "Online Tổng 120p",
//        "Online Tổng 180p",
//        "Online Tổng 300p",
//        "Hoàn Thành Doanh Trại Độc Nhãn",
//        "Hoàn Thành Đảo Kho Báu",
//        "Tham gia Ngọc Rồng Sao Đen",
//        "Chiến thắng Ngọc Rồng Sao Đen",
//        "Câu Cá 10 Lần",};
//    public static int[] Money = new int[]{
//        50, 100, 200, 400, 600, 1000, 200, 200, 200, 500, 200
//    };
//    public static boolean[] isFinish = new boolean[]{
//        false, false, false, false, false, false, false, false, false, false, false
//    };
//    public static boolean[] isRecieve = new boolean[]{
//        false, false, false, false, false, false, false, false, false, false, false
//    };
//    public static String[] reward = new String[]{
//        "Hoạt động đạt 10p",
//        "Hoạt động đạt 30p",
//        "Hoạt động đạt 60p",
//        "Hoạt động đạt 120p",
//        "Hoạt động đạt 180p",
//        "Hoạt động đạt 300p",};
//
//    public static void GetRecieve(Player pl, JSONArray dataArray) {
//        for (int i = 0; i < OnlineHangNgay.isRecieve.length; i++) {
//            OnlineHangNgay.isRecieve[i] = Integer.parseInt(String.valueOf(dataArray.get(i))) == 1;
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
//            System.err.print("\nError at 88\n");
//            e.printStackTrace();
//        }
//    }
//
//    public static void GetArchivemnt(Player pl, int index) {
//        OnlineHangNgay.isFinish = CheckTaskDays.CheckTaskDay(pl);
//        if (OnlineHangNgay.isRecieve[index]) {
//            Service.gI().sendThongBao(pl, "Bạn đã nhận từ trước rồi");
//            return;
//        }
//        if (!OnlineHangNgay.isFinish[index]) {
//            Service.gI().sendThongBao(pl, "Bạn chưa hoàn thành nhiệm vụ này");
//            return;
//        }
//        if (OnlineHangNgay.isFinish[index] && !OnlineHangNgay.isRecieve[index]) {
//            OnlineHangNgay.isRecieve[index] = true;
//            int hongngoc = 0;
//            switch (index) {
//                case 0:
//                    hongngoc = 50;
//                    break;
//                case 1:
//                    hongngoc = 100;
//                    break;
//                case 3:
//                    hongngoc = 400;
//                    break;
//                case 4:
//                    hongngoc = 600;
//                    break;
//                case 5:
//                    hongngoc = 1000;
//                    break;
//                case 9:
//                    hongngoc = 500;
//                    break;
//                case 2:
//                case 6:
//                case 7:
//                case 8:
//                case 10:
//                    hongngoc = 200;
//                    break;
//            }
//            if (hongngoc != 0) {
//                pl.inventory.ruby += hongngoc;
//                Service.gI().sendMoney(pl);
//                SendGetArchivemnt(pl, index);
//                Service.gI().sendThongBao(pl, "Bạn nhận được " + hongngoc + " hồng ngọc");
//            }
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
//            msg.writer().writeUTF(OnlineHangNgay.Name);
//            msg.writer().writeByte(OnlineHangNgay.Type);
//            msg.writer().writeByte(OnlineHangNgay.Info2.length);
//            for (int i = 0; i < OnlineHangNgay.Info2.length; i++) {
//                msg.writer().writeUTF(OnlineHangNgay.Info1[i]);
//                msg.writer().writeUTF(OnlineHangNgay.Info2[i]);
//                msg.writer().writeShort(OnlineHangNgay.Money[i]);
//                msg.writer().writeBoolean(CheckTaskDays.CheckTaskDay(pl)[i]);
//                msg.writer().writeBoolean(OnlineHangNgay.isRecieve[i]);
//                msg.writer().writeUTF("Hồng ngọc");
//            }
//            pl.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception E) {
//            System.err.print("\nError at 89\n");
//            E.printStackTrace();
//        }
//    }
//}
