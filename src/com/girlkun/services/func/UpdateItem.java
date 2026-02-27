/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.services.func;

import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UpdateItem {

    private static final int[][] ChiSo = new int[][]{
        {3, 5, 10, 20, 30, 50, 70, 100, 230, 280, 330, 450, 750, 1150, 1350},// ao
        {20, 20, 100, 200, 500, 1000, 2000, 4000, 8000, 12000, 16000, 25000, 50000, 100000, 135000},// namec
        {5, 8, 16, 32, 64, 120, 240, 560, 1000, 2000, 2500, 3000, 4500, 9000, 13500},
        {20, 20, 100, 200, 500, 1000, 2000, 4000, 8000, 12000, 16000, 25000, 50000, 100000, 135000},
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 18, 22, 25},};
    private static final int[] Rada = new int[]{
        12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281
    };
    private static final int NhanHuyDiet = 656;
    private static final int NhanThanLinh = 561;
    private static final int[] NhanThienSu = new int[]{
        1060, 1061, 1062
    };
    private static final int[][] DoThienSu = new int[][]{
        {1048, 1051, 1054, 1057},// td
        {1049, 1052, 1055, 1058},// namec
        {1050, 1053, 1056, 1059},// xayda
    };
    private static final int[][] DoHuyDiet = new int[][]{
        {650, 651, 657, 658},// td
        {652, 653, 659, 660},// namec
        {654, 655, 661, 662},// xayda
    };
    private static final int[][] DoThanLinh = new int[][]{
        {555, 556, 562, 563},// td
        {557, 558, 564, 565},// namec
        {559, 560, 566, 557},// xayda
    };
    private static final int[][] DoTraiDat = new int[][]{
        {0, 3, 33, 34, 136, 137, 138, 139, 230, 231, 232, 233},// ao
        {6, 9, 35, 36, 140, 141, 142, 143, 242, 243, 244, 245},// quan
        {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257},// gang
        {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},// giay
    };
    private static final int[][] DoNamec = new int[][]{
        {1, 4, 41, 42, 152, 153, 154, 155, 234, 235, 236, 237},// ao
        {7, 10, 43, 44, 156, 157, 158, 159, 245, 247, 248, 249},// quan
        {22, 25, 45, 46, 160, 161, 162, 163, 258, 259, 260, 261},// gang
        {28, 31, 47, 48, 164, 165, 166, 167, 270, 271, 272, 273},// giay
    };
    private static final int[][] DoXayda = new int[][]{
        {2, 5, 49, 50, 168, 169, 170, 171, 238, 239, 240, 241},// ao
        {8, 11, 51, 52, 172, 173, 174, 175, 250, 251, 252, 253},// quan
        {23, 25, 53, 54, 176, 177, 178, 179, 262, 263, 264, 265},// gang
        {29, 32, 55, 56, 180, 181, 182, 183, 274, 275, 276, 277},// giay
    };

    public static boolean isIDThan(Item item, int cgender, int ntl) {
        int id = item.template.id;
        boolean flag = item.template.type == ntl;
        if (ntl == 4) {
            return id == 561;
        }
        return id >= 555 && id <= 567 && flag&& item.template.gender == cgender;
    }
 
    public static boolean isDNS(Item item) {
        int id = item.template.id;
        return id == 674;
    }

    public static boolean isIDThan(Item item) 
    {
        if(item == null || item.template == null)
        {
            return false;
        }
        int id = item.template.id;
        return id >= 555 && id <= 567;
    }

    public static boolean isIDHuyDiet(Item item) {
        int id = item.template.id;
        return id >= 650 && id <= 662;
    }

    public static boolean isIDThienSu(Item item) {
        int id = item.template.id;
        return id >= 1048 && id <= 1062;
    }

    public static void subDaNguSac(Player pl, int soluong) {
        try {
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 674) {
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, soluong);
                }
            }
        } catch (Exception e) {
                     
        }
    }

    public static void CreateSKH(Player player, int cgender, int type,Item itemview) 
    {
        try
        { cgender = type == 4 ? player.gender : cgender;
        int[][] SKHTable = {
            {127, 128, 129},
            {130, 131, 132},
            {133, 134, 135}
        };
        int[][] SKHTable2 = {
            {139, 140, 141},
            {142, 143, 144},
            {136, 137, 138}
        };
        int random1 = Util.nextInt(3);
        int chiso1 = SKHTable[cgender][random1];
        int chiso2 = SKHTable2[cgender][random1];
        int random = Util.nextInt(1000);
        Item item = null;
        int option = 0;
        int chiso = 0;
        int level = 0;
        if (type < 4) {
            int[][] idcreate = new int[0][0];
            switch (cgender) {
                case 0:
                    idcreate = DoTraiDat;
                    break;
                case 1:
                    idcreate = DoNamec;
                    break;
                case 2:
                    idcreate = DoXayda;
                    break;
            }
            if (random <= 935) 
            {
                level = Util.nextInt(idcreate[type].length);
                int[] rows = idcreate[type];
                item = ItemService.gI().createNewItem((short) (rows[level]));
            } else if (random <= 975) {
                level = 12;
                item = ItemService.gI().createNewItem((short) (DoThanLinh[cgender][type]));
            } else if (random <= 995) {
                level = 13;
                int rows = DoHuyDiet[cgender][type];
                item = ItemService.gI().createNewItem((short) rows);
            } else {
                level = 14;
                int rows = DoThienSu[cgender][type];
                item = ItemService.gI().createNewItem((short) rows);
            }
            switch (type) {
                case 0:
                    option = 47;
                    break;
                case 1:
                    option = 6;
                    break;
                case 2:
                    option = 0;
                    break;
                case 3:
                    option = 7;
                    break;
            }
        }
        if (type == 4) {
                level = 14;
                int rows = NhanThienSu[cgender];
                item = ItemService.gI().createNewItem((short) rows);
            option = 14;
        }
        chiso = ChiSo[type][level];
        int congthem = Util.nextInt(0, 5);
        int chisocongthem = chiso * congthem;
        chiso += chisocongthem / 100;
        if (option == 6 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(22, chiso / 1000));
        } else if (option == 7 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(23, chiso / 1000));
        } else {
            item.itemOptions.add(new Item.ItemOption(option, chiso));
        }
        item.itemOptions.add(new Item.ItemOption(chiso1, 0));
        item.itemOptions.add(new Item.ItemOption(chiso2, 0));
        item.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        CombineServiceNew.gI().sendEffectOpenItem(player, itemview.template.iconID, item.template.iconID);}
        catch(Exception e)       
        {
            Service.gI().sendThongBao(player, "Có lỗi xảy ra !");
            
        }
    }

    public static void createSKHVip(Player player, int cgender, int type,Item itemview) {
        cgender = type == 4 ? player.gender : cgender;
        int[][] SKHTable = {
            {127, 128, 129},
            {130, 131, 132},
            {133, 134, 135}
        };
        int[][] SKHTable2 = {
            {139, 140, 141},
            {142, 143, 144},
            {136, 137, 138}
        };
        int random1 = Util.nextInt(3);
        int chiso1 = SKHTable[cgender][random1];
        int chiso2 = SKHTable2[cgender][random1];
        int random = Util.nextInt(1000);
        Item item = null;
        int option = 0;
        int chiso = 0;
        int level = 0;

        if (type < 4) {
            int[][] idcreate;
            switch (cgender) {
                case 0:
                    idcreate = DoTraiDat;
                    break;
                case 1:
                    idcreate = DoNamec;
                    break;
                case 2:
                    idcreate = DoXayda;
                    break;
                default:
                    return;
            }

            if (random <= 880) {
                level = Util.nextInt(idcreate[type].length);
                item = ItemService.gI().createNewItem((short) (idcreate[type][level]));
            } else if (random <= 940) {
                level = 12;
                item = ItemService.gI().createNewItem((short) (DoThanLinh[cgender][type]));
            } else if (random <= 980) {
                level = 13;
                item = ItemService.gI().createNewItem((short) (DoHuyDiet[cgender][type]));
            } else {
                level = 14;
                item = ItemService.gI().createNewItem((short) (DoThienSu[cgender][type]));
            }

            switch (type) {
                case 0:
                    option = 47;
                    break;
                case 1:
                    option = 6;
                    break;
                case 2:
                    option = 0;
                    break;
                case 3:
                    option = 7;
                    break;
            }
        }

        if (type == 4) {
            if (random <= 880) {
                level = Util.nextInt(Rada.length);
                item = ItemService.gI().createNewItem((short) (Rada[level]));
            } else if (random <= 940) {
                level = 12;
                item = ItemService.gI().createNewItem((short) (NhanThanLinh));
            } else if (random <= 980) {
                level = 13;
                item = ItemService.gI().createNewItem((short) (NhanHuyDiet));
            } else {
                level = 14;
                item = ItemService.gI().createNewItem((short) (NhanThienSu[cgender]));
            }
            option = 14;
        }

        chiso = ChiSo[type][level];
        int congthem = Util.nextInt(5, 15);
        chiso += (chiso * congthem) / 100;
        if (option == 6 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(22, chiso / 1000));
        } else if (option == 7 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(23, chiso / 1000));
        } else {
            item.itemOptions.add(new Item.ItemOption(option, chiso));
        }

        item.itemOptions.add(new Item.ItemOption(chiso1, 0));
        item.itemOptions.add(new Item.ItemOption(chiso2, 0));
        item.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        CombineServiceNew.gI().sendEffectOpenItem(player, itemview.template.iconID, item.template.iconID);
    }

    
    
    public static void createSKHThienSu(Player player, int cgender, int type,Item itemview) {
        cgender = type == 4 ? player.gender : cgender;
        int[][] SKHTable = {
            {127, 128, 129},
            {130, 131, 132},
            {133, 134, 135}
        };
        int[][] SKHTable2 = {
            {139, 140, 141},
            {142, 143, 144},
            {136, 137, 138}
        };
        int random1 = Util.nextInt(3);
        int chiso1 = SKHTable[cgender][random1];
        int chiso2 = SKHTable2[cgender][random1];
        Item item = null;
        int option = 0;
        int chiso = 0;
        int level = 0;

        if (type < 4) {
                level = 14;
                item = ItemService.gI().createNewItem((short) (DoThienSu[cgender][type]));
            switch (type) {
                case 0:
                    option = 47;
                    break;
                case 1:
                    option = 6;
                    break;
                case 2:
                    option = 0;
                    break;
                case 3:
                    option = 7;
                    break;
            }
        }

        if (type == 4) {
                level = 14;
                item = ItemService.gI().createNewItem((short) (NhanThienSu[cgender]));
            option = 14;
        }

        chiso = ChiSo[type][level];
        int congthem = Util.nextInt(5, 15);
        chiso += (chiso * congthem) / 100;
        if (option == 6 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(22, chiso / 1000));
        } else if (option == 7 && chiso >= 100000) {
            item.itemOptions.add(new Item.ItemOption(23, chiso / 1000));
        } else {
            item.itemOptions.add(new Item.ItemOption(option, chiso));
        }

        item.itemOptions.add(new Item.ItemOption(chiso1, 0));
        item.itemOptions.add(new Item.ItemOption(chiso2, 0));
        item.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        CombineServiceNew.gI().sendEffectOpenItem(player, itemview.template.iconID, item.template.iconID);
    }
    
    public static Item findItemThan(List<Item> list, int cgender, int type) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && isIDThan(item, cgender, type)  ) {
                    return item;
                }
            }
        } catch (Exception e) 
        {
                     
        }
        return null;
    }

    public static void StartUpdate(Player player, int cgender, int type, Npc npc) {
        Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
        int soLuong = 0;
        if (dns != null) {
            soLuong = dns.quantity;
        }
        if (dns == null || soLuong < 10) {
            npc.npcChat(player, "Yêu cầu cần x10 Đá Ngũ Sắc!");
            return;
        }
        Item thanlinh = findItemThan(player.inventory.itemsBag, cgender, type);
        if (thanlinh == null) {
            npc.npcChat(player, "Yêu cầu cần 1 trang bị thần linh đúng hành tinh");
            return;
        }
        if (player.inventory.gold < 500000000) {
            npc.npcChat(player, "Yêu cầu cần thêm 500tr vàng");
            return;
        }
        player.inventory.gold -= 500000000;
        Service.gI().sendMoney(player);
        InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 10);
        InventoryServiceNew.gI().subQuantityItemsBag(player, thanlinh, 1);
        int id = -1;
        int option = 0;
        int chiso = 0;
        int[][] idTable = {
            {650, 651, 657, 658},
            {652, 653, 659, 660},
            {654, 655, 661, 662}
        };
        if (type == 4) {
            id = 656;
            option = 14;
            chiso = Util.nextInt(15, 20);
        } else if (cgender >= 0 && cgender < idTable.length) {
            int[] row = idTable[cgender];
            if (type >= 0 && type < row.length) {
                id = row[type];
            }
        }
        switch (type) {
            case 0:
                option = 47;
                chiso = Util.nextInt(1500, 2000);
                break;
            case 1:
                option = 22;
                chiso = Util.nextInt(100, 130);
                break;
            case 2:
                option = 0;
                chiso = Util.nextInt(9000, 11000);
                break;
            case 3:
                option = 23;
                chiso = Util.nextInt(90, 120);
                break;
        }
        Item item = ItemService.gI().createNewItem((short) id);
        item.itemOptions.add(new ItemOption(option, chiso));
        item.itemOptions.add(new ItemOption(30, 0));
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        npc.npcChat(player, "Chuyển hóa thành công");
    }
}
