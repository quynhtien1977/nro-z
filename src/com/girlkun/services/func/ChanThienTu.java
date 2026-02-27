/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.services.func;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author Administrator
 */
public class ChanThienTu {

    public static int GetTinhTheNangCap(int tempID) {
        switch (tempID) {
            case 1962:
            case 1963:
            case 1964:
            case 1242:
            case 1249:
                return 1;
            case 1243:
            case 1250:
                return 2;
            case 1244:
            case 1251:
                return 4;
            case 1245:
            case 1252:
                return 6;
            case 1246:
            case 1253:
                return 8;
            case 1247:
            case 1254:
                return 10;
        }
        return 1;
    }

    private static short NextVongChan(int tempID) {

        return (short) (tempID + 1);
    }

    public static short NextTileHienThi(int tempID) {
        switch (tempID) {
            case 1962:
            case 1963:
            case 1964:
            case 1242:
            case 1249:
                return 50;
            case 1243:
            case 1250:
                return 40;
            case 1244:
            case 1251:
                return 30;
            case 1245:
            case 1252:
                return 20;
            case 1246:
            case 1253:
                return 10;
            case 1247:
            case 1254:
                return 5;
        }
        return 1;
    }

    private static float NextTile(int tempID) {
        switch (tempID) {
            case 1962:
            case 1963:
            case 1964:
            case 1249:
            case 1242:
                return 30f;
            case 1250:
            case 1243:
                return 20f;
            case 1251:
            case 1244:
                return 10f;
            case 1252:
            case 1245:
                return 2;
            case 1246:
            case 1253:
                return 0.5f;
            case 1247:
            case 1254:
                return 0.2f;
        }
        return 0.1f;
    }

    private static int RandomChiso1(int optionid) {
        switch (optionid) {
            case 0:
                return Util.nextInt(100, 2000);
            case 7:
                return Util.nextInt(10000, 20000);
            case 6:
                return Util.nextInt(10000, 20000);
        }
        return Util.nextInt(10000, 12000);
    }

    private static int RandomChiso1(int type, int type2) {
        if (type2 == 1) {
            if (type == 1) {
                int x = Util.nextInt(100);
                if (x < 30) {
                    return Util.nextInt(10000, 12000);
                } else if (x < 70) {
                    return Util.nextInt(12000, 15000);
                } else if (x < 90) {
                    return Util.nextInt(15000, 17500);
                }
                return Util.nextInt(17500, 20000);
            }
            if (type == 2) {
                int x = Util.nextInt(100);
                if (x < 30) {
                    return Util.nextInt(200, 400);
                } else if (x < 70) {
                    return Util.nextInt(400, 800);
                } else if (x < 90) {
                    return Util.nextInt(800, 1200);
                }
                return Util.nextInt(1200, 2000);
            }
        }
        if (type2 == 2) {
            int x = Util.nextInt(100);
            if (x <= 40) {
                return Util.nextInt(1, 3);
            } else if (x <= 70) {
                return Util.nextInt(3, 5);
            } else if (x <= 90) {
                return Util.nextInt(5, 7);
            }
            return Util.nextInt(7, 9);
        }
        return 0;
    }

    private static int RandomChiso2(int cgender, int type) {
        if (type == 1) {
            if (cgender == 0) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 7;
                } else if (x < 66) {
                    return 6;
                } else {
                    return 0;
                }
            }
            if (cgender == 1) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 6;
                } else if (x < 66) {
                    return 0;
                } else {
                    return 7;
                }
            }
            if (cgender == 2) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 7;
                } else if (x < 66) {
                    return 6;
                } else {
                    return 0;
                }
            }
        }
        if (type == 2) {
            if (cgender == 0) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 77;
                } else if (x < 66) {
                    return 103;
                } else {
                    return 50;
                }
            }
            if (cgender == 1) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 77;
                } else if (x < 66) {
                    return 103;
                } else {
                    return 50;
                }
            }
            if (cgender == 2) {
                int x = Util.nextInt(100);
                if (x < 33) {
                    return 77;
                } else if (x < 66) {
                    return 103;
                } else {
                    return 50;
                }
            }
        }
        return -1;
    }

    public static void DoiChanThienTu(Player player, int type) {
        short tempId = 1300;
        short nguyenlieu = 0;
        int thoivang = 0;
        int soluong = -1;
        boolean flag = false;
        if (type == 0) {
            nguyenlieu = 1279;
            soluong = 9;
            thoivang = 10;
        }
        if (type == 1) {
            nguyenlieu = 1279;
            soluong = 99;
            thoivang = 20;
            flag = true;
        }
        if (type == 2) {
            nguyenlieu = 1280;
            soluong = 99;
            thoivang = 10;
        }
        if (type == 3) {
            nguyenlieu = 1280;
            soluong = 999;
            thoivang = 20;
            flag = true;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang không đủ ô trống");
            return;
        }
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, nguyenlieu);
        if (item == null || item.quantity < soluong) {
            Service.gI().sendThongBao(player, "Thiếu vật phẩm để đổi");
            return;
        }
        Item item2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
        if (item2 == null || item2.quantity < thoivang) {
            Service.gI().sendThongBao(player, "Thiếu thỏi vàng để đổi");
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player, item, soluong);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, thoivang);
        Item vongChan = ItemService.gI().createNewItem(tempId, 1);
        int cgender = player.gender;
        int id = RandomChiso2(cgender, 1);
        vongChan.itemOptions.add(new Item.ItemOption(id, RandomChiso1(id)));
        vongChan.itemOptions.add(new Item.ItemOption(RandomChiso2(cgender, 2), RandomChiso1(1, 2)));
        vongChan.itemOptions.add(new Item.ItemOption(30, 0));
        if (!flag) {
            vongChan.itemOptions.add(new Item.ItemOption(93, 7));
        }
        InventoryServiceNew.gI().addItemBag(player, vongChan);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Bạn nhận được " + vongChan.template.name);
    }

    private static int GetParamThienTu2(short tempId, int idoptions) {
        int[][] mapping = {
                {1250, 77, 103, 2, 1, 1243, 50},
                {1251, 77, 103, 3, 2, 1244, 50},
                {1252, 77, 103, 5, 3, 1245, 50},
                {1253, 77, 103, 7, 4, 1246, 50},
                {1254, 77, 103, 9, 5, 1247, 50},
                {1255, 77, 103, 12, 8, 1248, 50}
        };
        for (int[] map : mapping) {
            if ((tempId == map[0] || tempId == map[5]) && (idoptions == map[1] || idoptions == map[2])) {
                return map[3];
            } else if ((tempId == map[0] || tempId == map[5]) && idoptions == map[6]) {
                return map[4];
            }
        }
        return -1;
    }

    private static Item AddOptionItem(Item item, Item.ItemOption opadd) {
        boolean flag = false;
        for (int i = item.itemOptions.size() - 1; i >= 0; i--) {
            Item.ItemOption op = item.itemOptions.get(i);
            if (op.optionTemplate.id == 30) {
                item.itemOptions.remove(op);
            }
            if (op.optionTemplate.id == opadd.optionTemplate.id) {
                flag = true;
            }
        }
        if (flag) {
            for (Item.ItemOption op : item.itemOptions) {
                if (op.optionTemplate.id == opadd.optionTemplate.id) {
                    op.param += opadd.param;
                }
            }
        } else {
            item.itemOptions.add(opadd);
        }
        item.itemOptions.add(new Item.ItemOption(30, 0));
        return item;
    }

    public static void NangCapChanThienTu(Player player, Item chanthientu, int type) {
        try {
            int solan = 1;
            switch (type) {
                case 0:
                    solan = 1;
                    break;
                case 1:
                    solan = 10;
                    break;
                case 2:
                    solan = 30;
                    break;
            }

            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                Service.gI().sendThongBao(player, "Hành trang không đủ ô trống");
                return;
            }
            if ((chanthientu.template.type == 38 || chanthientu.template.type == 37) && chanthientu.template.id != 1255 && chanthientu.template.id != 1248) {
                boolean flag = false;
                int solandadap = 1;
                for (int i = 0; i < solan; i++) {
                    solandadap++;
                    Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1279);
                    if (item == null || item.quantity < GetTinhTheNangCap(chanthientu.template.id)) {
                        Service.gI().sendThongBao(player, "Thiếu vật phẩm Tinh Thể");
                        break;
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, GetTinhTheNangCap(chanthientu.template.id));
                    Item item2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1280);
                    if (item2 == null || item2.quantity < GetTinhTheNangCap(chanthientu.template.id)) {
                        Service.gI().sendThongBao(player, "Thiếu vật phẩm Ma thạch");
                        break;
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item2, GetTinhTheNangCap(chanthientu.template.id));
                    if (Util.isTrue(NextTile(chanthientu.template.id), 100)) {
                        Item vongChan = ItemService.gI().createNewItem(NextVongChan(chanthientu.template.id), 1);
                        vongChan.itemOptions.clear();
                        for (Item.ItemOption o : chanthientu.itemOptions) {
                            vongChan.itemOptions.add(new Item.ItemOption(o.optionTemplate.id, o.param));
                        }
                        int cgender = player.gender;
                        int idoptions = RandomChiso2(cgender, 2);
                        int param = GetParamThienTu2(vongChan.template.id, idoptions);
                        AddOptionItem(vongChan, new Item.ItemOption(idoptions, param));
                        InventoryServiceNew.gI().subQuantityItemsBag(player, chanthientu, 1);
                        InventoryServiceNew.gI().addItemBag(player, vongChan);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    Service.gI().sendThongBao(player, "Nâng cấp thành công sau " + solandadap + " lần thực hiện");
                    CombineServiceNew.gI().sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    CombineServiceNew.gI().reOpenItemCombine(player);
                } else {

                    CombineServiceNew.gI().sendEffectFailCombine(player);
                    CombineServiceNew.gI().reOpenItemCombine(player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
