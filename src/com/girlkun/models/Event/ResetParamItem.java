///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.girlkun.models.Event;
//
//import com.girlkun.models.item.Item;
//
///**
// *
// * @author Administrator
// */
//public class ResetParamItem 
//{
//     public static int GetLevel(Item item) {
//        if (item == null) {
//            return 0;
//        }
//        for (Item.ItemOption op : item.itemOptions) {
//            if (op.optionTemplate.id == 72) {
//                return op.param;
//            }
//        }
//        return 0;
//    }
//    public static int[][] ChiSoQuan = new int[][]{
//        {48, 50, 52},
//        {96, 100, 104},
//        {150, 155, 160}};
//    public static int[][] ChiSoGang = new int[][]{
//        {5000, 4500, 5200},
//        {10000, 9600, 10400},
//        {13500, 13000, 14000}
//    };
//
//    public static void SetBasicChiSo(Item item) 
//    {
//        if (item == null || item.template == null) 
//        {
//            return;
//        }
//        if (item.template.type == 1 || item.template.type == 2) 
//        {
//            int optionid = item.template.type == 1 ? 22 : 0;
//            if (item.template.id >= 555 && item.template.id <= 567) {
//                int i = GetBasicChiSo(item.template.type, GetLevel(item), 0, item.template.gender);   
//                for (Item.ItemOption op : item.itemOptions) {
//                    if (op.optionTemplate.id == optionid && op.param > i) {
//                        op.param = i;
//                    }
//                }
//            }
//            if (item.template.id >= 650 && item.template.id <= 662) 
//            {
//                int i = GetBasicChiSo(item.template.type, GetLevel(item), 1, item.template.gender);
//                 for (Item.ItemOption op : item.itemOptions) {
//                    if (op.optionTemplate.id == optionid && op.param > i) {
//                        op.param = i;
//                    }
//                }
//            }
//            if (item.template.id >= 1048 && item.template.id <= 1062) {
//                int i = GetBasicChiSo(item.template.type, GetLevel(item), 2, item.template.gender);
//                 for (Item.ItemOption op : item.itemOptions) {
//                    if (op.optionTemplate.id == optionid && op.param > i) {
//                        op.param = i;
//                    }
//                }
//            }
//        }
//    }
//
//    public static int GetBasicChiSo(int type, int level, int typeitem, int gender) {
//        if (type == 1) {
//            if (level == 0) {
//                return ChiSoQuan[typeitem][gender];
//            }
//            if (level > 0) {
//                return (int) (GetBasicChiSo(type, level - 1, typeitem, gender) * 1.1);
//            }
//        }
//        if (type == 2) {
//            if (level == 0) {
//                return ChiSoGang[typeitem][gender];
//            }
//            if (level > 0) {
//                return (int) (GetBasicChiSo(type, level - 1, typeitem, gender) * 1.1);
//            }
//        }
//        return 0;
//    }
//}
