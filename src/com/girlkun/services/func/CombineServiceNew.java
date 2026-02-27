package com.girlkun.services.func;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerNotify;
import com.girlkun.network.io.Message;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int RUBY_DAP_DO_KICH_HOAT = 10;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;

    private static final int TIME_COMBINE = 1;

    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 519;

    public static final int MO_CHI_SO_Chien_Linh = 520;
    public static final int NANG_CAP_KHI = 521;
    public static final int NANG_CAP_MEO = 2521;
    public static final int NANG_CAP_LUFFY = 5291;
    public static final int Nang_Chien_Linh = 522;
    public static final int CHE_TAO_TRANG_BI_TS = 523;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;

    private static final int GOLD_MOCS_BONG_TAI = 500_000_000;
    private static final int RUBY_MOCS_BONG_TAI = 500;
    private static final int GOLD_BONG_TAI2 = 500_000_000;
    private static final int RUBY_BONG_TAI2 = 1_000;

    private static final int GOLD_LINHTHU = 500_000_000;
    private static final int GEM_LINHTHU = 5_000;

    private static final int RATIO_NANG_CAP_ChienLinh = 50;
    private static final int GOLD_Nang_Chien_Linh = 1_000_000_000;
    private static final int RUBY_Nang_Chien_Linh = 5000;
    // private static final int RATIO_NANG_CAP = 100;
    private static final int GOLD_MOCS_Chien_Linh = 500_000_000;
    private static final int RUBY_MOCS_Chien_Linh = 1000;

    private static final int GOLD_NANG_KHI = 500_000_000;
    private static final int RUBY_NANG_KHI = 5000;
    private static final int GOLD_NANG_LUFFY = 500_000_000;
    private static final int RUBY_NANG_LUFFY = 10000;
    public static final int REN_KIEM_Z = 517;
    public static final int ChanThienTu = 524;
    public static final int ChanThienTu2 = 525;
    public static final int CTZENO = 518;
    public static final int SKH1 = 521;

    private static final int GOLD_BONG_TAI = 200_000_000;
    private static final int GOLD_KIEM_Z = 200_000_000;
    private static final int GEM_BONG_TAI = 1_000;
    private static final int GEM_KIEM_Z = 1_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 70;
    private static final int RATIO_KIEM_Z2 = 40;
    private static final int RATIO_CTZENO = 50;
    private static final int GEM_CTZENO = 1_000;
    private static final int GOLD_CTZENO = 200_000_000;
    private static final int RATIO_SKH1 = 50;
    private static final int GEM_SKH1 = 1_000;
    private static final int GOLD_SKH1 = 200_000_000;

    public static final int NANG_CAP_THIEN_TU = 555;

    public static final int NANG_CAP_DO_KICH_HOAT = 550;
    public static final int NANG_CAP_DO_KICH_HOAT_THUONG = 800;
    private final Npc baHatMit;
//    private final Npc tosukaio;
    private final Npc whis;
    private final Npc npsthiensu64;
    private final Npc khidaumoi;
    private final Npc chomeoan;
    private final Npc trunglinhthu;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
        this.khidaumoi = NpcManager.getNpc(ConstNpc.KHI_DAU_MOI);
        this.trunglinhthu = NpcManager.getNpc(ConstNpc.TRUNG_LINH_THU);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
        this.chomeoan = NpcManager.getNpc(ConstNpc.CHO_MEO_AN);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        if (player != null && player.combineNew != null && player.combineNew.itemsCombine != null) {
            player.combineNew.clearItemCombine();
        }
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_THIEN_TU:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item thientu = null, DaKhac = null;

                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id == 1279) {
                                DaKhac = item;
                            } else if (item.template.id == 1300
                                    || item.template.id == 1301
                                    || item.template.id == 1302
                                    || item.template.id == 1303
                                    || item.template.id == 1304) {
                                thientu = item;
                            }
                        }
                    }
                    int level1_1 = 0;
                    int level1_2 = 0;
                    int level1_3 = 0;
                    int level1_4 = 0;
                    Item.ItemOption optionLevel_72 = null;
                    if (thientu != null) {
                        for (Item.ItemOption io : thientu.itemOptions) {
                            if (io.optionTemplate.id == 50) {
                                level1_1 = io.param;
                                break;
                            }
                        }

                        for (Item.ItemOption io : thientu.itemOptions) {
                            if (io.optionTemplate.id == 77) {
                                level1_2 = io.param;
                                break;
                            }
                        }
                        for (Item.ItemOption io : thientu.itemOptions) {
                            if (io.optionTemplate.id == 103) {
                                level1_3 = io.param;
                                break;
                            }
                        }
                        for (Item.ItemOption io : thientu.itemOptions) {
                            if (io.optionTemplate.id == 5) {
                                level1_4 = io.param;
                                break;
                            }
                        }
                    }
                    if (thientu == null || DaKhac == null) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần chân mệnh + 10 Tinh thể", "Đóng");
                    }
                    if (player.inventory.gold <= 500_000_000) {
                        Service.getInstance().sendThongBao(player, "Chuẩn bị đủ 500 Tr Vàng hãy đến tìm ta");
                        return;
                    }

                    if (thientu.template.id == 1308) {
                        Service.gI().sendThongBaoOK(player, "Cấp trang bị thiên tử đã đạt tối đa");
                    } else if (thientu != null && DaKhac != null) {
                        int ratio = getLevelThienTu(thientu);
                        String npcSay = "|6|" + thientu.template.name + "\n";
                        for (Item.ItemOption io : thientu.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        npcSay += "Ngươi có muốn Tiến Hóa Chân mẹnh không?\n  tỉ lệ  " + ratio + "%\n";
                        player.combineNew.ratioCombine
                                = ratio;
                        if (player.inventory.gold >= 500_000_000) {
                            this.whis.createOtherMenu(player, ConstNpc.MENU_THIEN_TU, npcSay, "Tiến Hóa\n Chân Mệnh");
                        } else {
                            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Chuẩn bị đủ tiền rồi hãy gặp ta!!!");
                        }
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần chân mệnh + 10 Tinh thể", "Đóng");
                    }
                }
                break;
                
            case NANG_CAP_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);                    //if (isTrangBiGod1(item) && isTrangBiThuong(item1)) {
                    if (isTrangBiHakai(item) && isTrangBiHakai(item1) && isTrangBiHakai(item2) ) {
                        player.combineNew.goldCombine = 10000;
                        String npcSay ="Con có muốn nâng cấp trang bị :\n"+ item.template.name + item1.template.name + item2.template.name + " \n|3|Trở thành trang bị Kích Hoạt\n";
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                            if (thoivang != null && thoivang.quantity >= 20 )  {
                                npcSay += "Cần 20 Thỏi vàng \n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Không đủ 20 thỏi vàng ";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho 1 trang bị Hủy Diệt ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
            case NANG_CAP_DO_KICH_HOAT_THUONG:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item thiensu = null;
                    Item skh1 = null;
                    Item skh2 = null;
                    if (player.combineNew.itemsCombine.get(0).isDHD()) {
                        thiensu = player.combineNew.itemsCombine.get(0);
                    }
                    if (player.combineNew.itemsCombine.get(1).isDTL()) {
                        skh1 = player.combineNew.itemsCombine.get(1);
                    }
                    if (player.combineNew.itemsCombine.get(2).isDTL()) {
                        skh2 = player.combineNew.itemsCombine.get(2);
                    }
                    if (thiensu != null && skh1 != null && skh2 != null) {
                        player.combineNew.goldCombine = 500_000_000;
                        player.combineNew.ratioCombine = 100;
                        String npcSay = "\n|2| " + thiensu.template.name;
                        npcSay += "\n|2| " + skh1.template.name;
                        npcSay += "\n|2| " + skh2.template.name + "\n";
                        npcSay += "\n|7|Ta sẽ phù phép trang bị ngươi cho ta thành 1 trang bị kích hoạt có chỉ số ngẫu nhiên";
                        npcSay += "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.goldCombine + " vàng");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa cho ta 1 trang bị huy diet"
                            + "\n và 2 trang bị than linh", "Đóng");
                }

                break;
            case Nang_Chien_Linh:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item linhthu = null;
                    Item ttt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.type == 72) {
                            linhthu = item;
                        } else if (item.template.id == 2031) {
                            ttt = item;
                        }
                    }

                    if (linhthu != null && ttt != null) {

                        player.combineNew.goldCombine = GOLD_Nang_Chien_Linh;
                        player.combineNew.rubyCombine = RUBY_Nang_Chien_Linh;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP_ChienLinh;

                        String npcSay = "Pet: " + linhthu.template.name + " \n|2|";
                        for (Item.ItemOption io : linhthu.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (ttt.quantity >= 10) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    trunglinhthu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(10 - ttt.quantity) + "Thăng tinh thạch";
                            trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }

                    } else {
                        this.trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Linh Thú và x10 Thăng tinh thạch", "Đóng");
                    }
                } else {
                    this.trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Linh Thú và x10 Thăng tinh thạch", "Đóng");
                }
                break;
            case NANG_CAP_KHI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item ctkhi = null;
                    Item dns = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (checkctkhi(item)) {
                            ctkhi = item;
                        } else if (item.template.id == 2063) {
                            dns = item;
                        }
                    }

                    if (ctkhi != null && dns != null) {
                        int lvkhi = lvkhi(ctkhi);
                        int countdns = getcountdnsnangkhi(lvkhi);
                        player.combineNew.goldCombine = getGoldnangkhi(lvkhi);
                        player.combineNew.rubyCombine = getRubydnangkhi(lvkhi);
                        player.combineNew.ratioCombine = getRatioNangkhi(lvkhi);

                        String npcSay = "Cải trang Little Girl : " + lvkhi + " \n|2|";
                        for (Item.ItemOption io : ctkhi.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (dns.quantity >= countdns) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    khidaumoi.createOtherMenu(player, ConstNpc.MENU_NANG_KHI, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(countdns - dns.quantity) + " Đá little Girl";
                            khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }

                    } else {
                        this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Cải trang little Girl Cấp 1-7 và 10 + 10*lvkhi Đá little Girl", "Đóng");
                    }
                } else {
                    this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Cải trang little Girl Cấp 1-7 và 10 + 10*lvkhi Đá little Girl", "Đóng");
                }
                break;
            case NANG_CAP_LUFFY:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item ctluffy = null;
                    Item dns = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (checkctluffy(item)) {
                            ctluffy = item;
                        } else if (item.template.id == 2076) {
                            dns = item;
                        }
                    }

                    if (ctluffy != null && dns != null) {
                        int lvluffy = lvluffy(ctluffy);
                        int countdns = getcountdnsnangluffy(lvluffy);
                        player.combineNew.goldCombine = getGoldnangluffy(lvluffy);
                        player.combineNew.rubyCombine = getRubydnangluffy(lvluffy);
                        player.combineNew.ratioCombine = getRatioNangluffy(lvluffy);

                        String npcSay = "Cải trang Luffy : " + lvluffy + " \n|2|";
                        for (Item.ItemOption io : ctluffy.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: 50 " + "%" + "\n";
                        if (dns.quantity >= countdns) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    khidaumoi.createOtherMenu(player, ConstNpc.MENU_NANG_LUFFY, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(countdns - dns.quantity) + " Đá thức tỉnh";
                            khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }

                    } else {
                        this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Cải trang luffy chưa thức tỉnh Cấp 1-7 và Đá thức tỉnh", "Đóng");
                    }
                } else {
                    this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Cải trang luffy chưa thức tỉnh Cấp 1-7 và Đá thức tỉnh", "Đóng");
                }
                break;
            case NANG_CAP_MEO:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item ctmeo = null;
                    Item dns = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (checkctmeo(item)) {
                            ctmeo = item;
                        } else if (item.template.id == 1004) {
                            dns = item;
                        }
                    }

                    if (ctmeo != null && dns != null) {
                        int lvmeo = lvmeo(ctmeo);
                        int countdns = getcountdnsnangmeo(lvmeo);
                        player.combineNew.goldCombine = getGoldnangmeo(lvmeo);
                        player.combineNew.rubyCombine = getRubydnangmeo(lvmeo);
                        player.combineNew.ratioCombine = getRatioNangmeo(lvmeo);

                        String npcSay = "Thú Cưng : " + lvmeo + " \n|2|";
                        for (Item.ItemOption io : ctmeo.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: 50" + "%" + "\n";
                        if (dns.quantity >= countdns) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    chomeoan.createOtherMenu(player, ConstNpc.MENU_NANG_MEO, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    chomeoan.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                chomeoan.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(countdns - dns.quantity) + " Thức ăn cho mèo";
                            chomeoan.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }

                    } else {
                        this.chomeoan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 bé mèo và thức ăn cho mèo", "Đóng");
                    }
                } else {
                    this.chomeoan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 bé mèo và thức ăn cho mèo", "Đóng");
                }
                break;

            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 933) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X99 Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 921) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case MO_CHI_SO_Chien_Linh:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item ChienLinh = null;
                    Item damathuat = null;
                    Item honthu = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 1149 && item.template.id <= 1151) {
                            ChienLinh = item;
                        } else if (item.template.id == 2030) {
                            damathuat = item;
                        } else if (item.template.id == 2029) {
                            honthu = item;
                        }
                    }
                    if (ChienLinh != null && damathuat != null && damathuat.quantity >= 99 && honthu.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_MOCS_Chien_Linh;
                        player.combineNew.rubyCombine = RUBY_MOCS_Chien_Linh;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Chiến Linh " + "\n|2|";
                        for (Item.ItemOption io : ChienLinh.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                trunglinhthu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Chiến Linh, X99 Đá ma thuật và X99 Hồn linh thú", "Đóng");
                    }
                } else {
                    this.trunglinhthu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chiến Linh, X99 Đá ma thuật và X99 Hồn linh thú", "Đóng");
                }

                break;

            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.rubyCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.rubyCombine) + " ngọc hồng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.rubyCombine + " ngọc hồng");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case REN_KIEM_Z:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item manhKiemZ = null;
                    Item quangKiemZ = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            manhKiemZ = item;
                        } else if (item.template.id == 1995) {
                            quangKiemZ = item;
                        }
                    }
                    if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 1) {
                        player.combineNew.goldCombine = GOLD_KIEM_Z;
                        player.combineNew.gemCombine = GEM_KIEM_Z;
                        player.combineNew.ratioCombine = RATIO_KIEM_Z2;
                        String npcSay = "Kiếm Z cấp 1" + "\n|2|";
                        for (Item.ItemOption io : manhKiemZ.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Rèn Kiếm Z " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Rèn Kiếm Z\ncần " + player.combineNew.gemCombine + " Ngọc xanh");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if (manhKiemZ == null || quangKiemZ == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Số lượng quặng Kiếm Z không đủ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
                }
                break;

            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc", "Nâng Cấp Liên Tục 100 Lần");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;

            case CHE_TAO_TRANG_BI_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    // this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Tuandz", "Yes");
                    return;
                }
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() < 1) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
//                        return;
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
//                        return;
//                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
//                    if (daNC != null) {
//                        tilenew += (daNC.template.id - 1073) * 10;                     
//                    }

                    String npcSay = "|1|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|1|Mạnh hơn trang bị Hủy Diệt từ 20% đến 35% \n"
                            + "|2|Mảnh ghép " + mTS.quantity + "/999(Thất bại -99 mảnh ghép)";
                    if (daNC != null) {
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|2|Phí nâng cấp: 2 tỉ vàng";
                    if (player.inventory.gold < 2000000000) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Đồng ý", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case ChanThienTu:
            case ChanThienTu2:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 3) {
                        Item chanthientu = null;
                        Item tinhthe = null;
                        Item mathach = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.template != null) {
                                if (item.template.type == 37 || item.template.type == 38) {
                                    chanthientu = item;
                                } else if (item.template.id == 1279) {
                                    tinhthe = item;
                                } else if (item.template.id == 1280) {
                                    mathach = item;
                                }
                            }
                        }
                        if (chanthientu != null && tinhthe != null && mathach != null) {
                            if (chanthientu.template.id != 1248 && chanthientu.template.id != 1255) {
                                if (tinhthe.quantity >= com.girlkun.services.func.ChanThienTu.GetTinhTheNangCap(chanthientu.template.id)) {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                            "|2|Bạn có muốn tiến hành nâng cấp Chân Thiên Tử ?\n"
                                            + "|2|Khi Nâng Cấp Thành Công Sẽ Nhận Được Ngẫu Nhiên 1 Dòng Chỉ Số HP,KI,SD\n"
                                            + "|7|Cần có " + com.girlkun.services.func.ChanThienTu.GetTinhTheNangCap(chanthientu.template.id) + " Tinh thể và ma thạch để có thể nâng cấp\n"
                                            + "Tỉ lệ thành công : " + com.girlkun.services.func.ChanThienTu.NextTileHienThi(chanthientu.template.id) + "%", "Nâng Cấp 1 Lần", "Nâng Cấp 10 Lần", "Nâng Cấp 30 Lần", "Đóng");
                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có " + com.girlkun.services.func.ChanThienTu.GetTinhTheNangCap(chanthientu.template.id) + " Tinh thể và ma thạch để có thể nâng cấp, Hãy Quay Lại Sau", "Đóng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chân thiên tử đã đạt cấp tối đa", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không tìm thấy những vật phẩm cần thiết", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không tìm thấy những vật phẩm cần thiết", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rải vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Điểm\n"
                            + (500000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món Thần Linh và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";
                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player, int type) {
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_THIEN_TU:
                nangcapthientu(player);
                break;
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player, type);
                break;
            case CHUYEN_HOA_TRANG_BI:
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case REN_KIEM_Z:
                renKiemZ(player);
                break;
            case PHAN_RA_DO_THAN_LINH:
                phanradothanlinh(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_DO_KICH_HOAT:
                dapDoKichHoat(player);
                break;
            case NANG_CAP_DO_KICH_HOAT_THUONG:
                dapDoKichHoatthuong(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
            case NANG_CAP_KHI:
                nangCapKhi(player);
                break;
            case NANG_CAP_MEO:
                nangCapMeo(player);
                break;
            case NANG_CAP_LUFFY:
                nangCapLuffy(player);
                break;
            case MO_CHI_SO_Chien_Linh:
                moChiSoLinhThu(player);
                break;
            case Nang_Chien_Linh:
                nangCapChienLinh(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break;
            case ChanThienTu:
            case ChanThienTu2:
                ChanThienTu1(player, type);
                break;
        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();
    }

    private void nangcapthientu(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item ngocboi1 = null;
            Item DaKhac = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isNotNullItem()) {
                        if (item.template.id == 1279) {
                            DaKhac = item;
                        } else if (item.template.id == 1300
                                || item.template.id == 1301
                                || item.template.id == 1302
                                || item.template.id == 1303
                                || item.template.id == 1304) {
                            ngocboi1 = item;
                        }
                    }
                }
            }

            int level1_1 = 0;
            int level1_2 = 0;
            int level1_3 = 0;
            int level1_4 = 0;

            Item.ItemOption optionLevel_5 = null;
            Item.ItemOption optionLevel_50 = null;
            Item.ItemOption optionLevel_77 = null;
            Item.ItemOption optionLevel_103 = null;

            for (Item.ItemOption io : ngocboi1.itemOptions) {
                if (io.optionTemplate.id == 50) {
                    level1_1 = io.param;
                    optionLevel_50 = io;
                    break;
                }
            }
            for (Item.ItemOption io : ngocboi1.itemOptions) {
                if (io.optionTemplate.id == 77) {
                    level1_2 = io.param;
                    optionLevel_77 = io;
                    break;
                }
            }
            for (Item.ItemOption io : ngocboi1.itemOptions) {
                if (io.optionTemplate.id == 103) {
                    level1_3 = io.param;
                    optionLevel_103 = io;
                    break;
                }
            }
            for (Item.ItemOption io : ngocboi1.itemOptions) {
                if (io.optionTemplate.id == 5) {
                    level1_4 = io.param;
                    optionLevel_5 = io;
                    break;
                }
            }

            if (ngocboi1 == null || DaKhac == null) {
                Service.getInstance().sendThongBao(player, "Không đủ vật phẩm ");
                return;
            }
            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                sendEffectSuccessCombine(player);
                sendEffectFailCombine(player);
                {
                    // CẤP 2
                    if (ngocboi1.template.id == 1300 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1301);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(3, 6)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(3, 6)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(3, 6)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(2, 4)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 3
                    if (ngocboi1.template.id == 1301 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1302);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(6, 9)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(6, 9)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(6, 9)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(4, 6)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 4
                    if (ngocboi1.template.id == 1302 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1303);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 12)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(9, 12)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(9, 12)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(6, 8)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa  chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 5
                    if (ngocboi1.template.id == 1303 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1304);
                        item.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(8) + 12));
                        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(8) + 12));
                        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(8) + 12));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(8, 10)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");
                        sendEffectSuccessCombine(player);

                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 6
                    if (ngocboi1.template.id == 1304 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1305);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 18)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 18)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 18)));
                        // item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(25, 30)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 7
                    if (ngocboi1.template.id == 1305 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1306);
                        item.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(25) + 18));
                        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(25) + 18));
                        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(25) + 18));
                        //  item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(25, 30)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 8
                    if (ngocboi1.template.id == 1306 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1307);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 35)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 35)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 35)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(30, 35)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                    // CẤP 9
                    if (ngocboi1.template.id == 1307 && DaKhac.quantity >= 10) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        Item item = ItemService.gI().createNewItem((short) 1308);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(35, 40)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(35, 40)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(35, 40)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(35, 40)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Chân Mệnh thành công");

                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        return;
                    }
                }
            } else {
                sendEffectFailCombine(player);
                Service.getInstance().sendMoney(player);
                InventoryServiceNew.gI().subQuantityItemsBag(player, DaKhac, 10);
                player.inventory.gold -= 500_000_000;
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }
    }

    public void ChanThienTu1(Player player, int select) {

        Item chanthientu = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.type == 37 || item.template.type == 38) {
                chanthientu = item;
                break;
            }
        }
        if (chanthientu != null) {
            com.girlkun.services.func.ChanThienTu.NangCapChanThienTu(player, chanthientu, select);
        }
    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void laychisoctkhi(Player player, Item ctkhi, int lvkhi) {
        ctkhi.itemOptions.add(new ItemOption(50, 12 + 5 * lvkhi));//sd
        ctkhi.itemOptions.add(new ItemOption(77, 15 + 5 * lvkhi));//hp
        ctkhi.itemOptions.add(new ItemOption(103, 15 + 5 * lvkhi));//ki
        ctkhi.itemOptions.add(new ItemOption(14, 5 + 2 * lvkhi));//cm
        ctkhi.itemOptions.add(new ItemOption(5, 20 + 10 * lvkhi));//sd cm
        ctkhi.itemOptions.add(new ItemOption(156, 10 + 5 * lvkhi));
        ctkhi.itemOptions.add(new ItemOption(181, 5 + 5 * lvkhi));
        ctkhi.itemOptions.add(new ItemOption(106, 0));
        ctkhi.itemOptions.add(new ItemOption(34, 0));
        ctkhi.itemOptions.add(new ItemOption(30, 0));
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void laychisoctmeo(Player player, Item ctkhi, int lvkhi) {
        ctkhi.itemOptions.add(new ItemOption(50, Util.nextInt(2, 15)));//sd
        ctkhi.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));//hp
        ctkhi.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));//ki
        ctkhi.itemOptions.add(new ItemOption(162, Util.nextInt(5, 15)));//ki        
        ctkhi.itemOptions.add(new ItemOption(114, 100));
        ctkhi.itemOptions.add(new ItemOption(111, 0));
        ctkhi.itemOptions.add(new ItemOption(30, 0));
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void laychisoctluffy(Player player, Item ctluffy, int lvluffy) {
        ctluffy.itemOptions.add(new ItemOption(50, 15 + 8 * lvluffy));//sd
        ctluffy.itemOptions.add(new ItemOption(95, 15 + 5 * lvluffy));//hp
        ctluffy.itemOptions.add(new ItemOption(96, 15 + 5 * lvluffy));//ki
        ctluffy.itemOptions.add(new ItemOption(14, 5 + 1 * lvluffy));//cm
        ctluffy.itemOptions.add(new ItemOption(5, 20 + 10 * lvluffy));//sd cm
        ctluffy.itemOptions.add(new ItemOption(159, 0 + 1 * lvluffy));
        ctluffy.itemOptions.add(new ItemOption(116, 0));
        ctluffy.itemOptions.add(new ItemOption(106, 0));
        ctluffy.itemOptions.add(new ItemOption(36, 0));
        ctluffy.itemOptions.add(new ItemOption(30, 0));
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void laychiChienLinh(Player player, Item ctkhi) {
        ctkhi.itemOptions.add(new ItemOption(50, Util.nextInt(7, 15)));//sd
        ctkhi.itemOptions.add(new ItemOption(77, Util.nextInt(7, 15)));//hp
        ctkhi.itemOptions.add(new ItemOption(103, Util.nextInt(7, 15)));//ki
        ctkhi.itemOptions.add(new ItemOption(72, 2));
        ctkhi.itemOptions.add(new ItemOption(30, 0));
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new Item.ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void phanradothanlinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 500000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
            sendEffectSuccessCombine(player);
            player.inventory.coupon += couponAdd;
            this.baHatMit.npcChat(player, "Con đã nhận được " + couponAdd + " điểm");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.gI().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).findFirst().get();

        player.inventory.gold -= COST;
        sendEffectSuccessCombine(player);
        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
        InventoryServiceNew.gI().addItemBag(player, itemTS);

        InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 5);
        itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    public void openSKHVIP(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hãy chuẩn bị ít nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Cần 1 trang bị Thần linh , hủy diệt , thiên sứ cùng 1 hành tinh , 10 đá ngũ sắc và 500tr vàng");
            return;
        }
        if (player.inventory.gold < 500000000) {
            Service.gI().sendThongBao(player, "Cần 500tr vàng");
            return;
        }
        Item item1 = null;
        Item item2 = null;
        Item item3 = null;
        Item item4 = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (UpdateItem.isIDThan(item)) {
                item1 = item;
            } else if (UpdateItem.isIDHuyDiet(item)) {
                item2 = item;
            } else if (UpdateItem.isIDThienSu(item)) {
                item3 = item;
            } else if (UpdateItem.isDNS(item) && item.quantity >= 4) {
                item4 = item;
            }
        }
        if (item1 == null || item2 == null || item3 == null || item4 == null) {
            Service.gI().sendThongBao(player, "Cần 3 trang bị Thần linh , hủy diệt , thiên sứ cùng 1 hành tinh , 10 đá ngũ sắc và 500tr vàng");
            return;
        }
        if (item1.template.type != item2.template.type || item1.template.type != item3.template.type) {
            Service.gI().sendThongBao(player, "Cần 3 trang bị Thần linh , hủy diệt , thiên sứ cùng 1 loại");
            return;
        }
        if (item1.template.gender != item2.template.gender || item1.template.gender != item3.template.gender) {
            Service.gI().sendThongBao(player, "Cần 3 trang bị Thần linh , hủy diệt , thiên sứ cùng 1 hành tinh ");
            return;
        }
        UpdateItem.createSKHVip(player, item1.template.gender, item1.template.type, item1);
        player.inventory.gold -= 500000000;
        Service.gI().sendMoney(player);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item4, 10);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void dapDoKichHoat(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = 100;
            Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
            if (thoivang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            Item item1 = player.combineNew.itemsCombine.get(1);
            Item item2 = player.combineNew.itemsCombine.get(2);
            if (isTrangBiHakai(item) && isTrangBiHakai(item1) && isTrangBiHakai(item2) ) {
                int idItemKichHoatTDop[][] = {{ 136, 137, 138, 139, 230, 231, 232, 233  },
                        { 140, 141, 142, 143, 242, 243, 244, 245 },
                        { 144, 145, 146, 147, 254, 255, 256, 257 },
                        { 148, 149, 150, 151, 266, 267, 268, 269 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};
                int idItemKichHoatXDop[][] = {{ 168, 169, 170, 171, 238, 239, 240, 241 },
                        { 172, 173, 174, 175, 250, 251, 252, 253 },
                        { 176, 177, 178, 179, 262, 263, 264, 265 },
                        { 180, 181, 182, 183, 274, 275, 276, 277 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};
                int idItemKichHoatNMop[][] = {{ 152, 153, 154, 155, 234, 235, 236, 237 },
                        { 156, 157, 158, 159, 246, 247, 248, 249 },
                        { 160, 161, 162, 163, 258, 259, 260, 261 },
                        { 164, 165, 166, 167, 270, 271, 272, 273 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};

                int optionItemKichHoat[][] = {
                    {127, 128, 129, 213, 215, 139, 140, 141, 214, 216},
                    {130, 131, 132, 213, 217, 142, 143, 144, 214, 218}, 
                    {133, 134, 135, 213, 219, 136, 137, 138, 214, 220}};
                int type = item.template.type;
                int gender = item.template.gender;
                int random = Util.nextInt(0, 2);
                if (gender == 3) {
                    gender = player.gender;
                }
                int option1 = optionItemKichHoat[gender][random];
                int option2 = optionItemKichHoat[gender][random + 5];
                Item itemKichHoat = null;
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    if ( itemKichHoat.template.id == 0 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 2));
                    }
                    if ( itemKichHoat.template.id == 33){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 4));
                    }
                    if ( itemKichHoat.template.id == 3 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,8));
                    }
                    if ( itemKichHoat.template.id == 34){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 16));
                    }
                    if ( itemKichHoat.template.id == 136){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 24));
                    }
                    if ( itemKichHoat.template.id == 137){
                        itemKichHoat.itemOptions.add(new ItemOption(47,40));
                    }
                    if ( itemKichHoat.template.id ==  138){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 50));
                    }
                    if ( itemKichHoat.template.id == 139){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 90));
                    }
                    if ( itemKichHoat.template.id ==230){
                        itemKichHoat.itemOptions.add(new ItemOption(47,200));
                    }
                    if ( itemKichHoat.template.id ==231 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 250));
                    }
                    if ( itemKichHoat.template.id ==232 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 300));
                    }
                    if ( itemKichHoat.template.id == 233){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    if ( itemKichHoat.template.id ==  6){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 20));
                    }
                    if ( itemKichHoat.template.id == 35){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 150));
                    }
                    if ( itemKichHoat.template.id == 9){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 300));
                    }
                    if ( itemKichHoat.template.id == 36){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 600));
                    }
                    if ( itemKichHoat.template.id == 140){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1400));
                    }
                    if ( itemKichHoat.template.id == 141){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 3000));
                    }
                    if ( itemKichHoat.template.id ==  142){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 6000));
                    }
                    if ( itemKichHoat.template.id == 143){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 10000));
                    }
                    if ( itemKichHoat.template.id ==242){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 14000));
                    }
                    if ( itemKichHoat.template.id == 243){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 18000));
                    }
                    if ( itemKichHoat.template.id == 244){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 22000));
                    }
                    if ( itemKichHoat.template.id == 245){
                        itemKichHoat.itemOptions.add(new ItemOption(6,26000 ));
                    }
                    if ( itemKichHoat.template.id ==  21){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 4));
                    }
                    if ( itemKichHoat.template.id == 24){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 7));
                    }
                    if ( itemKichHoat.template.id ==37){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 14));
                    }
                    if ( itemKichHoat.template.id == 38){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 28));
                    }
                    if ( itemKichHoat.template.id == 144){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 55));
                    }
                    if ( itemKichHoat.template.id == 145){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 110));
                    }
                    if ( itemKichHoat.template.id == 146 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 220));
                    }
                    if ( itemKichHoat.template.id ==147 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 530));
                    }
                    if ( itemKichHoat.template.id ==254){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 680));
                    }
                    if ( itemKichHoat.template.id == 255){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 1000));
                    }
                    if ( itemKichHoat.template.id == 256){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1500) );
                    }
                    if ( itemKichHoat.template.id == 257){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2200 ));
                    }
                    if ( itemKichHoat.template.id ==  27){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10));
                    }
                    if ( itemKichHoat.template.id == 30){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25));
                    }
                    if ( itemKichHoat.template.id ==39){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 120));
                    }
                    if ( itemKichHoat.template.id == 40){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 250));
                    }
                    if ( itemKichHoat.template.id == 148){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 500));
                    }
                    if ( itemKichHoat.template.id == 149){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1200));
                    }
                    if ( itemKichHoat.template.id ==  150){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 2400));
                    }
                    if ( itemKichHoat.template.id == 151){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 5000));
                    }
                    if ( itemKichHoat.template.id ==266){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 9000));
                    }
                    if ( itemKichHoat.template.id == 267){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 14000));
                    }
                    if ( itemKichHoat.template.id == 268){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 19000));
                    }
                    if ( itemKichHoat.template.id == 269){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNMop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    if ( itemKichHoat.template.id == 1 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 2));
                    }
                    if ( itemKichHoat.template.id == 41){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 4));
                    }
                    if ( itemKichHoat.template.id == 4 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,8));
                    }
                    if ( itemKichHoat.template.id == 42){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 16));
                    }
                    if ( itemKichHoat.template.id == 152){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 24));
                    }
                    if ( itemKichHoat.template.id == 153){
                        itemKichHoat.itemOptions.add(new ItemOption(47,40));
                    }
                    if ( itemKichHoat.template.id ==  154){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 50));
                    }
                    if ( itemKichHoat.template.id == 155){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 90));
                    }
                    if ( itemKichHoat.template.id ==234){
                        itemKichHoat.itemOptions.add(new ItemOption(47,200));
                    }
                    if ( itemKichHoat.template.id ==235 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 250));
                    }
                    if ( itemKichHoat.template.id ==236 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 300));
                    }
                    if ( itemKichHoat.template.id == 237){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    if ( itemKichHoat.template.id ==  7){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 25));
                    }
                    if ( itemKichHoat.template.id == 43){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 50));
                    }
                    if ( itemKichHoat.template.id == 10){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 100));
                    }
                    if ( itemKichHoat.template.id == 44){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 250));
                    }
                    if ( itemKichHoat.template.id == 156){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 600));
                    }
                    if ( itemKichHoat.template.id == 157){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1200));
                    }
                    if ( itemKichHoat.template.id ==  158){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 2400));
                    }
                    if ( itemKichHoat.template.id == 159){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 4800));
                    }
                    if ( itemKichHoat.template.id ==246){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 13000));
                    }
                    if ( itemKichHoat.template.id == 247){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 17000));
                    }
                    if ( itemKichHoat.template.id == 248){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 21000));
                    }
                    if ( itemKichHoat.template.id == 249){
                        itemKichHoat.itemOptions.add(new ItemOption(6,25000 ));
                    }
                    //gang{22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261},
                    if ( itemKichHoat.template.id ==  22){
                        itemKichHoat.itemOptions.add(new ItemOption(0,3));
                    }
                    if ( itemKichHoat.template.id == 46){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 6));
                    }
                    if ( itemKichHoat.template.id ==25){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 12));
                    }
                    if ( itemKichHoat.template.id == 45){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 24));
                    }
                    if ( itemKichHoat.template.id == 160){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 50));
                    }
                    if ( itemKichHoat.template.id == 161){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 100));
                    }
                    if ( itemKichHoat.template.id == 162 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 200));
                    }
                    if ( itemKichHoat.template.id ==163 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 500));
                    }
                    if ( itemKichHoat.template.id ==258){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 630));
                    }
                    if ( itemKichHoat.template.id == 259){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 950));
                    }
                    if ( itemKichHoat.template.id == 260){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1450) );
                    }
                    if ( itemKichHoat.template.id == 261){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2150 ));
                    }
                    if ( itemKichHoat.template.id ==  28){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 15));
                    }
                    if ( itemKichHoat.template.id == 47){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 30));
                    }
                    if ( itemKichHoat.template.id ==31){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 150));
                    }
                    if ( itemKichHoat.template.id == 48){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 300));
                    }
                    if ( itemKichHoat.template.id == 164){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 600));
                    }
                    if ( itemKichHoat.template.id == 165){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1500));
                    }
                    if ( itemKichHoat.template.id ==  166){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 3000));
                    }
                    if ( itemKichHoat.template.id == 167){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 6000));
                    }
                    if ( itemKichHoat.template.id ==270){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10000));
                    }
                    if ( itemKichHoat.template.id == 271){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 15000));
                    }
                    if ( itemKichHoat.template.id == 272){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 20000));
                    }
                    if ( itemKichHoat.template.id == 273){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25000));
                    }
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    if ( itemKichHoat.template.id == 2 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 5));
                    }
                    if ( itemKichHoat.template.id == 49){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 5));
                    }
                    if ( itemKichHoat.template.id == 5 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,10));
                    }
                    if ( itemKichHoat.template.id == 50){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 20));
                    }
                    if ( itemKichHoat.template.id == 168){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 30));
                    }
                    if ( itemKichHoat.template.id == 169){
                        itemKichHoat.itemOptions.add(new ItemOption(47,50));
                    }
                    if ( itemKichHoat.template.id ==  170){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 70));
                    }
                    if ( itemKichHoat.template.id == 171){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 100));
                    }
                    if ( itemKichHoat.template.id ==238){
                        itemKichHoat.itemOptions.add(new ItemOption(47,230));
                    }
                    if ( itemKichHoat.template.id ==239){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 280));
                    }
                    if ( itemKichHoat.template.id ==240 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 330));
                    }
                    if ( itemKichHoat.template.id == 241){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 450));
                    }
                    if ( itemKichHoat.template.id ==  8){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 25));
                    }
                    if ( itemKichHoat.template.id == 51){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 50));
                    }
                    if ( itemKichHoat.template.id == 11){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 100));
                    }
                    if ( itemKichHoat.template.id == 52){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 200));
                    }
                    if ( itemKichHoat.template.id == 172){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 500));
                    }
                    if ( itemKichHoat.template.id == 173){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1000));
                    }
                    if ( itemKichHoat.template.id ==  174){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 2000));
                    }
                    if ( itemKichHoat.template.id == 175){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 4000));
                    }
                    if ( itemKichHoat.template.id ==250){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 12000));
                    }
                    if ( itemKichHoat.template.id == 251){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 16000));
                    }
                    if ( itemKichHoat.template.id == 252){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 20000));
                    }
                    if ( itemKichHoat.template.id == 253){
                        itemKichHoat.itemOptions.add(new ItemOption(6,24000 ));
                    }
                    if ( itemKichHoat.template.id ==  23){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 5));
                    }
                    if ( itemKichHoat.template.id == 53){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 8));
                    }
                    if ( itemKichHoat.template.id ==26){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 16));
                    }
                    if ( itemKichHoat.template.id == 54){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 32));
                    }
                    if ( itemKichHoat.template.id == 176){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 60));
                    }
                    if ( itemKichHoat.template.id == 177){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 120));
                    }
                    if ( itemKichHoat.template.id == 178 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 240));
                    }
                    if ( itemKichHoat.template.id ==179 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 560));
                    }
                    if ( itemKichHoat.template.id ==262){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 700));
                    }
                    if ( itemKichHoat.template.id == 263){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 1050));
                    }
                    if ( itemKichHoat.template.id == 264){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1550) );
                    }
                    if ( itemKichHoat.template.id == 265){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2250 ));
                    }
                    if ( itemKichHoat.template.id ==  29){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10));
                    }
                    if ( itemKichHoat.template.id == 55){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25));
                    }
                    if ( itemKichHoat.template.id ==32){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 120));
                    }
                    if ( itemKichHoat.template.id == 56){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 250));
                    }
                    if ( itemKichHoat.template.id == 180){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 500));
                    }
                    if ( itemKichHoat.template.id == 181){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1200));
                    }
                    if ( itemKichHoat.template.id ==  182){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 2400));
                    }
                    if ( itemKichHoat.template.id == 183){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 5000));
                    }
                    if ( itemKichHoat.template.id ==274){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 9000));
                    }
                    if ( itemKichHoat.template.id == 275){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 14000));
                    }
                    if ( itemKichHoat.template.id == 276){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 19000));
                    }
                    if ( itemKichHoat.template.id == 277){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                itemKichHoat.itemOptions.add(new ItemOption(option1, 0));
                itemKichHoat.itemOptions.add(new ItemOption(option2, 0));
                itemKichHoat.itemOptions.add(new ItemOption(30, 0));

                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);
                sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().removeItemBag(player, item2);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 20);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void dapDoKichHoatthuong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hãy chuẩn bị ít nhất 1 ô trống trong hành trang");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3) {
            Item thiensu = null;
            Item skh1 = null;
            Item skh2 = null;
            if (player.combineNew.itemsCombine.get(0).isDHD()) {
                thiensu = player.combineNew.itemsCombine.get(0);
            }
            if (player.combineNew.itemsCombine.get(1).isDTL()) {
                skh1 = player.combineNew.itemsCombine.get(1);
            }
            if (player.combineNew.itemsCombine.get(2).isDTL()) {
                skh2 = player.combineNew.itemsCombine.get(2);
            }
            if (thiensu != null && skh1 != null && skh2 != null) {
                UpdateItem.CreateSKH(player, thiensu.template.gender, thiensu.template.type, thiensu);
                player.inventory.gold -= 500000000;
                Service.gI().sendMoney(player);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thiensu, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, skh1, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, skh2, 1);
                InventoryServiceNew.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        } else {
            return;
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapChienLinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }

            Item linhthu = null;
            Item ttt = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.type == 72) {
                    linhthu = item;
                } else if (item.template.id == 2031) {
                    ttt = item;
                }
            }
            if (linhthu != null && ttt != null) {

                if (ttt.quantity < 10) {
                    Service.gI().sendThongBao(player, "Thăng tinh thạch");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, ttt, 10);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    short[] chienlinh = {2019, 2020, 2021, 2022, 2023, 2024, 2025};
                    linhthu.template = ItemService.gI().getTemplate(chienlinh[Util.nextInt(0, 2)]);
                    linhthu.itemOptions.clear();
                    laychiChienLinh(player, linhthu);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapKhi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }

            Item ctkhi = null;
            Item dns = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkctkhi(item)) {
                    ctkhi = item;
                } else if (item.template.id == 2063) {
                    dns = item;
                }
            }
            if (ctkhi != null && dns != null) {
                int lvkhi = lvkhi(ctkhi);
                int countdns = getcountdnsnangkhi(lvkhi);
                if (countdns > dns.quantity) {
                    Service.gI().sendThongBao(player, "Không đủ Đá little Girl");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, countdns);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    short idctkhisaunc = getidctkhisaukhilencap(lvkhi);
                    ctkhi.template = ItemService.gI().getTemplate(idctkhisaunc);
                    ctkhi.itemOptions.clear();
                    ctkhi.itemOptions.add(new Item.ItemOption(72, lvkhi + 1));
                    laychisoctkhi(player, ctkhi, lvkhi);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapMeo(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để huấn luyện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để huấn luyện");
                return;
            }

            Item ctmeo = null;
            Item dns = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkctmeo(item)) {
                    ctmeo = item;
                } else if (item.template.id == 1004) {
                    dns = item;
                }
            }
            if (ctmeo != null && dns != null) {
                int lvmeo = lvmeo(ctmeo);
                int countdns = getcountdnsnangmeo(lvmeo);
                if (countdns > dns.quantity) {
                    Service.gI().sendThongBao(player, "Không đủ Thức ăn");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, countdns);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    short idctmeosaunc = getidctmeosaukhilencap(lvmeo);
                    ctmeo.template = ItemService.gI().getTemplate(idctmeosaunc);
                    ctmeo.itemOptions.clear();
                    ctmeo.itemOptions.add(new Item.ItemOption(72, lvmeo + 1));
                    laychisoctmeo(player, ctmeo, lvmeo);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapLuffy(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }

            Item ctluffy = null;
            Item dns = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkctluffy(item)) {
                    ctluffy = item;
                } else if (item.template.id == 2076) {
                    dns = item;
                }
            }
            if (ctluffy != null && dns != null) {
                int lvluffy = lvluffy(ctluffy);
                int countdns = getcountdnsnangluffy(lvluffy);
                if (countdns > dns.quantity) {
                    Service.gI().sendThongBao(player, "Không đủ Đá thức tỉnh");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, countdns);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    short idctluffysaunc = getidctluffysaukhilencap(lvluffy);
                    ctluffy.template = ItemService.gI().getTemplate(idctluffysaunc);
                    ctluffy.itemOptions.clear();
                    ctluffy.itemOptions.add(new Item.ItemOption(72, lvluffy + 1));
                    laychisoctluffy(player, ctluffy, lvluffy);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item manhvobt = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkbongtai(item)) {
                    bongtai = item;
                } else if (item.template.id == 933) {
                    manhvobt = item;
                }
            }
            if (bongtai != null && manhvobt != null) {
                int lvbt = lvbt(bongtai);
                int countmvbt = getcountmvbtnangbt(lvbt);
                if (countmvbt > manhvobt.quantity) {
                    Service.gI().sendThongBao(player, "Không đủ Mảnh vỡ bông tai");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhvobt, countmvbt);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.template = ItemService.gI().getTemplate(getidbtsaukhilencap(lvbt));
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, lvbt + 1));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item thangtinhthach = null;
            Item thucan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    linhthu = item;
                } else if (item.template.id == 934) {
                    thangtinhthach = item;
                } else if (item.template.id == 935) {
                    thucan = item;
                }
            }
            if (linhthu != null && thangtinhthach != null && thangtinhthach.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thangtinhthach, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thucan, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    linhthu.itemOptions.clear();
                    linhthu.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 15)));
                    } else if (rdUp == 1) {
                        linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 15)));
                    } else if (rdUp == 2) {
                        linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 15)));
                    } else if (rdUp == 3) {
                        linhthu.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 15)));
                    } else if (rdUp == 4) {
                        linhthu.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        linhthu.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        linhthu.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 15)));
                    } else if (rdUp == 7) {
                        linhthu.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 15)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void moChiSoLinhThu(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item ChienLinh = null;
            Item damathuat = null;
            Item honthu = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 1149 && item.template.id <= 1151) {
                    ChienLinh = item;
                } else if (item.template.id == 2030) {
                    damathuat = item;
                } else if (item.template.id == 2029) {
                    honthu = item;
                }
            }
            if (ChienLinh != null && damathuat.quantity >= 99 && honthu.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, damathuat, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, honthu, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    ChienLinh.itemOptions.add(new Item.ItemOption(206, 0));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 1) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        ChienLinh.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isDaNangCap()) {
                    daNC = item;
                } else if (item.isDaMayMan()) {
                    daMM = item;
                } else if (item.isCongThucVip()) {
                    CtVip = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.gold < 2000000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            player.inventory.gold -= 2000000000;

            int tilemacdinh = 35;
            int tileLucky = 20;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }
            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                if (daNC != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                }
                if (daMM != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
                if (mTS != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                }
                if (CtVip != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                }
                if (daNC != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);;
                }
                if (daMM != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                }

                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);

            } else {
                sendEffectFailCombine(player);
                if (mTS != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                }
                if (CtVip != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                }
                if (daNC != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);;
                }
                if (daMM != null) {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                }

                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }

        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ ngọc hồng để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.ruby -= ruby;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player, int type) {
        if (type == 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                long gold = player.combineNew.goldCombine;
                int gem = player.combineNew.gemCombine;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                } else if (player.inventory.gem < gem) {
                    Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                    return;
                }
                Item item = player.combineNew.itemsCombine.get(0);
                if (isTrangBiPhaLeHoa(item)) {
                    int star = 0;
                    Item.ItemOption optionStar = null;
                    for (Item.ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 107) {
                            star = io.param;
                            optionStar = io;
                            break;
                        }
                    }
                    if (star < MAX_STAR_ITEM) {
                        player.inventory.gold -= gold;
                        player.inventory.gem -= gem;
                        byte ratio = (optionStar != null && optionStar.param > 6) ? (byte) 3 : 1;
                        if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                            if (optionStar == null) {
                                item.itemOptions.add(new Item.ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            sendEffectSuccessCombine(player);
//                            if (optionStar != null && optionStar.param >= 7) {
//                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
//                                        + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
//                            }
                        } else {
                            sendEffectFailCombine(player);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
        if (type == 1) {
            if (player.combineNew.itemsCombine.isEmpty()) {
                return;
            }
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (!isTrangBiPhaLeHoa(item)) {
                return;
            }
            int star = 0;
            Item.ItemOption optionStar = null;
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 107) {
                    star = io.param;
                    optionStar = io;
                    break;
                }
            }
            boolean flag = false;
            for (int i = 0; i < 100; i++) {
                gold = player.combineNew.goldCombine;
                gem = player.combineNew.gemCombine;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    break;
                } else if (player.inventory.gem < gem) {
                    Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                    break;
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 6) ? (byte) 3 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        flag = true;
                        if (optionStar != null && optionStar.param >= 7) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
//                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                        Service.gI().sendThongBao(player, "Nâng cấp thành công sau " + (i + 1) + " lần");
                        break;
                    }
                }
            }
            if (!flag) {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    private void renKiemZ(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item manhKiemZ = null;
            Item quangKiemZ = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 555 && item.template.id <= 567) {
                    manhKiemZ = item;
                } else if (item.template.id == 1995) {
                    quangKiemZ = item;
                }
            }

            if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 1) {
                //Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 1200); //Nguyên liệu
                //if (findItemBag != null) {
                //Service.gI().sendThongBao(player, "Con đã có Kiếm Z trong hành trang rồi, không thể rèn nữa.");
                //return;
                //}
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, quangKiemZ, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    manhKiemZ.template = ItemService.gI().getTemplate(1996);
                    manhKiemZ.itemOptions.clear();
                    Random rand = new Random();
                    int ratioCombine = rand.nextInt(60) + 1;
                    int level = 0;
                    if (ratioCombine <= 40) {
                        level = 1 + rand.nextInt(4);
                    } else if (ratioCombine <= 70) {
                        level = 5 + rand.nextInt(4);
                    } else if (ratioCombine <= 90) {
                        level = 9 + rand.nextInt(4);
                    } else if (ratioCombine <= 95) {
                        level = 13 + rand.nextInt(3);
                    } else {
                        level = 16;
                    }
                    manhKiemZ.itemOptions.add(new Item.ItemOption(0, level * 200 + 10000));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(49, level * 1 + 20));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(14, level));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(97, level));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(30, 0));
                    manhKiemZ.itemOptions.add(new Item.ItemOption(72, level));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
                        if (optionLevel != null && optionLevel.param >= 5) {
                            ServerNotify.gI().notify("Chúc mừng cư dân " + player.name + " vừa nâng cấp "
                                    + "thành công " + itemDo.template.name + " lên +" + optionLevel.param);
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    public void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    public void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    public void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isTrangBiHakai(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    public int getLevelThienTu(Item item) {
        try {
            if (item.isNotNullItem() == false) {
                return 0;
            }
            switch (item.template.id) {
                case 1300:
                    return 50;
                case 1301:
                    return 40;
                case 1302:
                    return 30;
                case 1303:
                    return 20;
                case 1304:
                    return 10;
                case 1305:
                    return 5;
                case 1306:
                    return 2;
                case 1307:
                    return 2;
                case 1308:
                    return 1;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
            return 0;
        }

    }

    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 20_000_000;
            case 1:
                return 140_000_000;
            case 2:
                return 160_000_000;
            case 3:
                return 180_000_000;
            case 4:
                return 200_000_000;
            case 5:
                return 220_000_000;
            case 6:
                return 400_000_000;
            case 7:
                return 550_000_000;
            case 8:
                return 600_000_000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 100f;// 5tr vang
            case 1:
                return 80f;  // 10tr
            case 2:
                return 60f; // 20tr
            case 3:
                return 40f; // 40tr
            case 4:
                return 20f; // 50tr
            case 5:
                return 10f; // 60tr
            case 6:
                return 5f; // 70tr
            case 7:
                return 2f; // 80tr
            case 8:
                return 1f;    // 100tr

        }

        return 0;
    }

    private float getRatioNangmeo(int lvmeo) { //tile nang khi chi hat mit
        switch (lvmeo) {
            case 1:
                return 20f;
//            case 2:
//                return 30f;
//            case 3:
//                return 20f;
//            case 4:
//                return 10f;
//            case 5:
//                return 10f;
//            case 6:
//                return 5f;
//            case 7:
//                return 2f;
        }

        return 0;
    }

    private float getRatioNangkhi(int lvkhi) { //tile nang khi chi hat mit
        switch (lvkhi) {
            case 1:
                return 100f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 10f;
            case 6:
                return 5f;
            case 7:
                return 2f;
        }

        return 0;
    }

    private float getRatioNangluffy(int lvluffy) { //tile nang khi chi hat mit
        switch (lvluffy) {
            case 1:
                return 70f;
            case 2:
                return 30f;
            case 3:
                return 30f;
            case 4:
                return 30f;
            case 5:
                return 30f;
            case 6:
                return 25;
            case 7:
                return 20f;
        }

        return 0;
    }

    private float getRationangbt(int lvbt) { //tile dap do chi hat mit
        switch (lvbt) {
            case 1:
                return 15f;
            case 2:
                return 10f;
            case 3:
                return 5f;

        }

        return 0;
    }

    private int getGoldnangbt(int lvbt) {
        return GOLD_BONG_TAI2 + 200000000 * lvbt;
    }

    private int getRubydnangbt(int lvbt) {
        return RUBY_BONG_TAI2 + 2000 * lvbt;
    }

    private int getcountmvbtnangbt(int lvbt) {
        return 100 + 50 * lvbt;
    }

    private boolean checkbongtai(Item item) {
        if (item.template.id == 454 || item.template.id == 921 || item.template.id == 1155) {
            return true;
        }
        return false;
    }

    private int lvbt(Item bongtai) {
        switch (bongtai.template.id) {
            case 454:
                return 1;
            case 921:
                return 2;
            case 1155:
                return 3;
        }

        return 0;

    }

    private short getidbtsaukhilencap(int lvbtcu) {
        switch (lvbtcu) {
            case 1:
                return 921;
            case 2:
                return 1155;
            case 3:
                return 1156;

        }
        return 0;
    }

    private int getGoldnangmeo(int lvmeo) {
        return GOLD_NANG_KHI + 100000000 * lvmeo;
    }

    private int getRubydnangmeo(int lvmeo) {
        return RUBY_NANG_KHI + 25000;
    }

    private int getcountdnsnangmeo(int lvmeo) {
        return 10 + 10 * lvmeo;
    }

    private boolean checkctmeo(Item item) {
        if ((item.template.id >= 1411 && item.template.id <= 1412)) {
            return true;
        }
        return false;
    }

    private int getGoldnangkhi(int lvkhi) {
        return GOLD_NANG_KHI + 100000000 * lvkhi;
    }

    private int getRubydnangkhi(int lvkhi) {
        return RUBY_NANG_KHI + 2000 * lvkhi;
    }

    private int getcountdnsnangkhi(int lvkhi) {
        return 10 + 7 * lvkhi;
    }

    private boolean checkctkhi(Item item) {
        if ((item.template.id >= 2055 && item.template.id <= 2062)) {
            return true;
        }
        return false;
    }

    private int getGoldnangluffy(int lvluffy) {
        return GOLD_NANG_KHI + 100000000 * lvluffy;
    }

    private int getRubydnangluffy(int lvluffy) {
        return RUBY_NANG_LUFFY + 40000;
    }

    private int getcountdnsnangluffy(int lvluffy) {
        return 10 + 15 * lvluffy;
    }

    private boolean checkctluffy(Item item) {
        if ((item.template.id >= 2068 && item.template.id <= 2075)) {
            return true;
        }
        return false;
    }

    private int lvkhi(Item ctkhi) {
        switch (ctkhi.template.id) {
            case 2055:
                return 1;
            case 2056:
                return 2;
            case 2057:
                return 3;
            case 2058:
                return 4;
            case 2059:
                return 5;
            case 2060:
                return 6;
            case 2061:
                return 7;
        }

        return 0;

    }

    private short getidctkhisaukhilencap(int lvkhicu) {
        switch (lvkhicu) {
            case 1:
                return 2056;
            case 2:
                return 2057;
            case 3:
                return 2058;
            case 4:
                return 2059;
            case 5:
                return 2060;
            case 6:
                return 2061;
            case 7:
                return 2062;
        }
        return 0;
    }

    private int lvmeo(Item ctmeo) {
        switch (ctmeo.template.id) {
            case 1411:
                return 1;
        }

        return 0;

    }

    private short getidctmeosaukhilencap(int lvmeocu) {
        switch (lvmeocu) {
            case 1:
                return 1412;
        }
        return 0;
    }

    private int lvluffy(Item ctluffy) {
        switch (ctluffy.template.id) {
            case 2068:
                return 1;
            case 2069:
                return 2;
            case 2070:
                return 3;
            case 2071:
                return 4;
            case 2072:
                return 5;
            case 2073:
                return 6;
            case 2074:
                return 7;
        }

        return 0;

    }

    private short getidctluffysaukhilencap(int lvluffycu) {
        switch (lvluffycu) {
            case 1:
                return 2069;
            case 2:
                return 2070;
            case 3:
                return 2071;
            case 4:
                return 2072;
            case 5:
                return 2073;
            case 6:
                return 2074;
            case 7:
                return 2075;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 90;
        }
        return 0;
    }

    private int getGemEpSao(int star) {

        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 70;
            case 1:
                return 40;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 5;
            case 5:
                return 3;
            case 6:
                return 1;
            case 7: // 7 sao
                return 0.5;
            case 8:
                return 0.3;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
            case 8:
                return 70;
            case 9:
                return 70;
            case 10:
                return 70;
            case 11:
                return 70;
            case 12:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 50000;
            case 1:
                return 100000;
            case 2:
                return 3000000;
            case 3:
                return 45000000;
            case 4:
                return 50000000;
            case 5:
                return 70000000;
            case 6:
                return 100000000;
            case 7:
                return 150000000;
            case 8:
                return 250000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {

        return item != null && item.template != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 6 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 1; // +2%giáp
            case 14:
                return 1; // +5%né đòn
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 49;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case REN_KIEM_Z:
                return "Ta sẽ rèn\ncho con thanh\nKiếm Z này";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\n Tăng một cấp";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case MO_CHI_SO_Chien_Linh:
                return "Ta sẽ phù phép\ncho Chiến Linh của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_KHI:
                return "Ta sẽ phù phép\ncho Cải trang của ngươi\nTăng một cấp!!";
            case NANG_CAP_MEO:
                return "Ta sẽ giúp ngươi cho mèo ăn\ncho mèo của ngươi\nTăng một cấp!!";
            case NANG_CAP_LUFFY:
                return "Ta sẽ Giúp \ncho Cải trang Luffy của ngươi\nthức tỉnh!!";
            case Nang_Chien_Linh:
                return "Ta sẽ biến linh thú của ngươi \nThành Chiến Linh!!!";
            case NANG_CAP_DO_KICH_HOAT:
                return "Ta sẽ phù phép\ntrang bị kích hoạt từ vải thô đến thần linh";
            case NANG_CAP_DO_KICH_HOAT_THUONG:
                return "Ta sẽ phù phép\ntrang bị kích hoạt, Random chỉ số vip ";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
            case ChanThienTu:
                return "\n\nNâng cấp Chân Thiên Tử";
            case ChanThienTu2:
                return "\n\nNâng cấp Siêu Chân Thiên Tử";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 5 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 3 trang bị Thần Linh, Hủy Diệt , Thiên Sứ cùng 1 hành tinh,cùng 1 loại\nChọn tiếp  Đá Ngũ Sắc \n "
                        + " đồ SKH VIP sẽ cùng loại \n với đồ đưa vào!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI:
                return "Tách bông tai trước khi nâng\nVào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp \nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata Cấp 2\nChọn Mảnh hồn bông tai số lượng 99 cái\nvà 2 Đá ngũ sắc để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_Chien_Linh:
                return "Vào hành trang\nChọn Chiến Linh\nChọn Đá ma thuật số lượng 99 cái\nvà x99 Hồn Thú để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_KHI:
                return "Vào hành trang\nChọn Cải Little Girl \nChọn Đá Little Girl để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_MEO:
                return "Vào hành trang\nChọn Mèo \nChọn thức ăn cho mèo để cho mèo ăn\nSau đó chọn 'cho ăn'";
            case NANG_CAP_LUFFY:
                return "Vào hành trang\nChọn Cải Luffy \nChọn Đá thức tỉnh để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case Nang_Chien_Linh:
                return "Vào hành trang\nChọn Linh Thú \nChọn x10 Thăng tinh thạch để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_KICH_HOAT:
                return "Vào hành trang\nchọn 3 trang bị thần linh\n "
                        + " và 500tr vàng\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_DO_KICH_HOAT_THUONG:
                return "Vào hành trang\nchọn 1 trang bị Hủy diệt , 2 trang bị thần linh\n "
                        + " và 500tr vàng\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case REN_KIEM_Z:
                return "VChọn Kiếm Z\nChọn Quặng Z, số lượng\n99 cái\nSau đó chọn 'Rèn Kiếm'\n Ngẫu nhiên Kiếm Z cấp 1 đến cấp 16";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)\n"
                        + "1 đá may mắn (tùy chọn)\n";
            case ChanThienTu:
                return "Chọn 1 Chân Thiên Tử chưa đạt cấp tối đa\nChọn Tinh Thể và Ma Thạch\nVà Chiếnnnnnnnnnnn";
            case ChanThienTu2:
                return "Chọn 1 Siêu Chân Thiên Tử chưa đạt cấp tối đa\nChọn Tinh Thể và Ma Thạch\nVà Chiếnnnnnnnnnnn";
            default:
                return "";
        }
    }

}
