///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.girlkun.models.Event;
//
//import com.girlkun.database.GirlkunDB;
//import com.girlkun.models.item.Item;
//import com.girlkun.models.player.Player;
//import com.girlkun.services.InventoryServiceNew;
//import com.girlkun.services.ItemService;
//import com.girlkun.services.ItemTimeService;
//import com.girlkun.services.Service;
//import com.girlkun.utils.Util;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// *
// * @author Administrator
// */
//public class CauCa {
//
//    public Player player;
//    public int PointCauCa = 0;
//    public boolean IsCauCa;
//    public long LastCauCa;
//    public long LastSendTime;
//
//    public CauCa(Player pl) {
//        this.player = pl;
//    }
//
//    public void StartCauCa() {
//        if (player.zone != null && player.zone.map != null && player.zone.map.mapId == 178) {
//            if (!IsCauCa) {
//                if (player.inventory.gold >= 199000000) 
//                {      
//                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) 
//                    {
//                        SendTime();
//                        IsCauCa = true;
//                        LastCauCa = System.currentTimeMillis();
//                        player.inventory.gold -= 199000000;
//                        Service.gI().sendMoney(player);
//                    } else {
//                        Service.gI().sendThongBao(player, "Cần 1 ô trống trong hành trang");
//                    }
//                }
//                else 
//                {
//                     Service.gI().sendThongBao(player, "Cần có 199tr vàng để có thể sử dụng cần câu");
//                }
//            } else {
//                Service.gI().sendThongBao(player, "Hãy chờ cá cắn câu rồi hãy tiếp tục");
//            }
//        } else {
//            Service.gI().sendThongBao(player, "Chỉ có thể sử dụng ở map câu cá");
//        }
//    }
//
//    public void Done() {
//        PointCauCa++;
//        int random = Util.nextInt(100);
//        if (random <= 20)
//        {
//            random = Util.nextInt(100);
//            if (random <= 35)
//            {
//                Item item = ItemService.gI().createNewItem((short) 1103, 1);
//                InventoryServiceNew.gI().addItemBag(player, item);
//                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//            }
//            else if (random <= 70) 
//            {
//                Item item = ItemService.gI().createNewItem((short)1102, 1);
//                InventoryServiceNew.gI().addItemBag(player, item);
//                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//            }
//            else if (random <= 20) 
//            {
//                int[] itc2 = new int[]{1099, 1100, 1101};
//                Item item = ItemService.gI().createNewItem((short) itc2[Util.nextInt(0, 2)], 1);
//                InventoryServiceNew.gI().addItemBag(player, item);
//                Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//            }
//        } else if (random <= 30) {
//            Item item = ItemService.gI().createNewItem((short) 16, 1);
//            InventoryServiceNew.gI().addItemBag(player, item);
//            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//        } else if (random <= 40) {
//            Item item = ItemService.gI().createNewItem((short) 17, 1);
//            InventoryServiceNew.gI().addItemBag(player, item);
//            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//        } else if (random <= 70) {
//            int gold = Util.nextInt(50, 199);
//            player.inventory.gold += gold * 1000000;
//            Service.gI().sendThongBao(player, "Bạn nhận được " + gold + "tr vàng");
//        } else if (random <= 96) {
//            Item item = ItemService.gI().createNewItem((short) 1978, 1);
//            item.itemOptions.add(new Item.ItemOption(30, 1));
//            InventoryServiceNew.gI().addItemBag(player, item);
//            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//        } else if (random <= 97) {
//            Item item = ItemService.gI().createNewItem((short) 1999, 1);
//            item.itemOptions.add(new Item.ItemOption(30, 1));
//            InventoryServiceNew.gI().addItemBag(player, item);
//            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//        } else {
//            Item item = ItemService.gI().createNewItem((short) 1998, 1);
//            item.itemOptions.add(new Item.ItemOption(30, 1));
//            InventoryServiceNew.gI().addItemBag(player, item);
//            Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
//        }
//        Service.gI().sendMoney(player);
//        InventoryServiceNew.gI().sendItemBags(player);
//    }
//
//    public void SendTime() {
//        ItemTimeService.gI().sendTextTime(player, (byte) 0, "Câu cá : ", 10);
//    }
//
//    public void Update() {
//        if (IsCauCa) {
//            if (System.currentTimeMillis() - LastCauCa >= 10000) {
//                Done();
//                IsCauCa = false;
//            }
//        }
//    }
//}
