package com.girlkun.models.npc;

//import com.arriety.MaQuaTang.MaQuaTangManager;
import com.arriety.kygui.ShopKyGuiService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.boss.list_boss.DuongTank;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.Zone;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.girlkun.models.map.doanhtrai.DoanhTrai;
import com.girlkun.models.map.doanhtrai.DoanhTraiService;
//import com.arriety.models.map.khigas.KhiGas;
//import com.arriety.models.map.khigas.KhiGasService;
import com.girlkun.jdbc.daos.PlayerDAO;
//import com.girlkun.models.ThanhTich.OnlineHangNgay;
//import com.girlkun.models.ThanhTich.QuaNapHangNgay;
//import com.girlkun.models.ThanhTich.ThanhTichPlayer;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import com.girlkun.models.map.BDKB.BanDoKhoBauService;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.map.nguhanhson.nguhs;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.matches.PVPService;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
//import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Archivement;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.*;
import com.girlkun.services.func.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.girlkun.services.func.SummonDragon.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    // playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static int getRandomValue(int min, int max, Random random) {
        return random.nextInt(max - min + 1) + min;
    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
                                        System.err.print("\nError at 209\n");
                                        e.printStackTrace();
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }

                                case 1:

                                    break;
                                case 2:

                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.Nang_Chien_Linh:
                                case CombineServiceNew.MO_CHI_SO_Chien_Linh:
                                    if (select == 0) {

                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0,
                            "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
                            "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (pl.nPoint.power < 50000000000L) {
                    Service.gI().sendThongBao(pl, "Yêu cầu sức mạnh lớn hơn 50 tỷ");
                    return;
                }
                // if (mapId == 0) {
                // if (player.nPoint.power < 400000000000L || player.nPoint.power >=
                // 2000000000000L) {
                // this.npcChat(player, "yeu cau 40 ti sm!");
                // return;
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar,
                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            ShopKyGuiService.gI().openShopKyGui(pl);
                            break;

                    }
                }
            }
        };
    }

    /////////////////////////////////////////// NPC Quy Lão
    /////////////////////////////////////////// Kame///////////////////////////////////////////
    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                Item ruacon = InventoryServiceNew.gI().findItemBag(player, 874);
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (ruacon != null && ruacon.quantity >= 1) {
                            this.createOtherMenu(player, 12, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Giao\nRùa con", "Nói chuyện");
                        } else {
                            this.createOtherMenu(player, 13, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Nói chuyện");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 12) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5,
                                        "Cảm ơn cậu đã cứu con rùa của ta\n Để cảm ơn ta sẽ tặng cậu món quà.",
                                        "Nhận quà", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 6,
                                        "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                        "Về khu\nvực bang", "Giải tán\nBang hội", "Kho Báu\ndưới biển");
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 12361) {
                        switch (select) {
                            case 0:
                                if (player.inventory.itemsMailBox.size() <= 0) {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hông có quà đâu mà nhận",
                                            "Nhận quà", "Đóng");
                                    return;
                                } else {
                                    int emptyBagCount = InventoryServiceNew.gI().getCountEmptyBag(player);
                                    if (emptyBagCount >= player.inventory.itemsMailBox.size()) {
                                        List<Item> temp = new ArrayList<>(player.inventory.itemsMailBox);
                                        player.inventory.itemsMailBox.clear();
                                        if (GodGK.updateMailBox(player)) {
                                            for (Item it : temp) {
                                                InventoryServiceNew.gI().addItemBag(player, it);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn vừa nhận được x" + it.quantity + " " + it.template.name);
                                            }
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        }
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bạn không đủ ô trống trong hành trang", "", "Đóng");
                                        return;
                                    }
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                try {
                                    Item RuaCon = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 874);
                                    if (RuaCon != null) {
                                        if (RuaCon.quantity >= 1
                                                && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            int randomItem = Util.nextInt(6); // Random giữa 0 và 1
                                            if (randomItem == 0) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z",
                                                        "Ok");
                                            } else if (randomItem == 1) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z",
                                                        "Ok");
                                            } else if (randomItem == 2) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 12));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 30));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z",
                                                        "Ok");
                                            } else if (randomItem == 3) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else if (randomItem == 4) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else if (randomItem == 5) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else {
                                                Item VatPham = ItemService.gI().createNewItem((short) 16);
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ta tặng cậu Ngọc rồng 3 sao", "Ok");
                                            }
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, RuaCon, 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.err.print("\nError at 211\n");
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 1:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1,
                                                "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Đồng ý", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                                break;
                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.BanDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                        + player.clan.BanDoKhoBau.level
                                                        + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                        Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
                if (player.iDMark.getIndexMenu() == 13) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 7,
                                    "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Về khu\nvực bang", "Giải tán\nBang hội", "Kho Báu\ndưới biển");
                            break;
                        case 1:
                            player.inventory.itemsMailBox.clear();
                            player.inventory.itemsMailBox.addAll(GodGK.getMailBox(player));
                            StringBuilder npcSayBuilder = new StringBuilder(
                                    "Bạn đang có " + player.inventory.itemsMailBox.size() + " phần quà\n");
                            for (Item it : player.inventory.itemsMailBox) {
                                npcSayBuilder.append("x ").append(it.quantity).append(" ").append(it.template.name)
                                        .append(" \n");
                            }
                            String npcSay = npcSayBuilder.toString();

                            if (player.inventory.itemsMailBox.size() > 0) {
                                this.createOtherMenu(player, 12361, npcSay, "Nhận quà", "Đóng");
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == 7) {
                    switch (select) {
                        case 0:
                            if (player.getSession().player.nPoint.power >= 80000000000L) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                            }
                            break;
                        case 1:
                            Clan clan = player.clan;
                            if (clan != null) {
                                ClanMember cm = clan.getClanMember((int) player.id);
                                if (cm != null) {
                                    if (clan.members.size() > 1) {
                                        Service.gI().sendThongBao(player, "Bang phải còn một người");
                                        break;
                                    }
                                    if (!clan.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                        break;
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1,
                                            "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                            "Đồng ý", "Từ chối!");
                                }
                                break;
                            }
                            Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                            break;
                        case 2:
                            if (player.clan != null) {
                                if (player.clan.BanDoKhoBau != null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                    + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                            "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                            "Chọn\ncấp độ", "Từ chối");
                                }
                            } else {
                                this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                ChangeMapService.gI().goToDBKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                Input.gI().createFormChooseLevelBDKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                    switch (select) {
                        case 0:
                            BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                    Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                            break;

                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Thành công đến từ sự cố gắng và chăm chỉ\nTruy Cập nrodreamland.io.vn Để xem giftcode và nạp thẻ"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru"
                                                        : "Vua Vegeta"),
                                "Đổi password", "Nhận ngọc xanh", "Nhận đệ tử", "Giftcode", "Quy đổi thỏi vàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormChangePassword(player);
                                break;
                            case 1:
                                if (player.inventory.gem == 200000000) {
                                    this.npcChat(player, "Bú ít thôi con");
                                    break;
                                }
                                player.inventory.gem = 2000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 2M ngọc xanh");
                                break;
                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat(player, "Cháu đã có đệ tử rồi");
                                }
                                break;
                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 4:
                                this.createOtherMenu(player, ConstNpc.QUY_DOI,
                                        "|7|Số tiền của bạn còn : " + player.getSession().vnd + "\n"
                                                + "Muốn quy đổi không",
                                        "Quy Đổi\n10.000\n 20 Thỏi Vàng", "Quy Đổi\n20.000\n 40 Thỏi Vàng",
                                        "Quy Đổi\n30.000\n 60 Thỏi Vàng", "Quy Đổi\n50.000\n 100 Thỏi Vàng",
                                        "Quy Đổi\n100.000 \n200 Thỏi Vàng", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                        PreparedStatement ps = null;
                        try (Connection con = GirlkunDB.getConnection();) {
                            switch (select) {
                                case 0:
                                    Item thoivang = ItemService.gI().createNewItem((short) (457));
                                    thoivang.quantity += 19;
                                    if (player.getSession().vnd < 10000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ 10k coin");
                                        return;
                                    }
                                    player.getSession().vnd -= 10000;
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    Service.gI().sendThongBao(player,
                                            "Bạn Nhận Được 20 " + thoivang.template.name + " Nhớ out game vô lại");
                                    break;
                                case 1:
                                    Item thoivangg = ItemService.gI().createNewItem((short) (457));
                                    thoivangg.quantity += 39;
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ 20k coin");
                                        return;
                                    }
                                    player.getSession().vnd -= 20000;
                                    InventoryServiceNew.gI().addItemBag(player, thoivangg);
                                    Service.gI().sendThongBao(player,
                                            "Bạn Nhận Được 40 " + thoivangg.template.name + " Nhớ out game vô lại");
                                    break;
                                case 2:
                                    Item thoivanggg = ItemService.gI().createNewItem((short) (457));
                                    thoivanggg.quantity += 59;
                                    if (player.getSession().vnd < 30000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ 30k coin");
                                        return;
                                    }
                                    player.getSession().vnd -= 30000;
                                    InventoryServiceNew.gI().addItemBag(player, thoivanggg);
                                    Service.gI().sendThongBao(player,
                                            "Bạn Nhận Được 60 " + thoivanggg.template.name + " Nhớ out game vô lại");
                                    break;
                                case 3:
                                    Item thoivangggg = ItemService.gI().createNewItem((short) (457));
                                    thoivangggg.quantity += 99;
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ 50k coin");
                                        return;
                                    }
                                    player.getSession().vnd -= 50000;
                                    InventoryServiceNew.gI().addItemBag(player, thoivangggg);
                                    Service.gI().sendThongBao(player,
                                            "Bạn Nhận Được 1000 " + thoivangggg.template.name + " Nhớ out game vô lại");
                                    break;
                                case 4:
                                    Item thoivanggggg = ItemService.gI().createNewItem((short) (457));
                                    thoivanggggg.quantity += 199;
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ 100k coin");
                                        return;
                                    }
                                    player.getSession().vnd -= 100000;
                                    InventoryServiceNew.gI().addItemBag(player, thoivanggggg);
                                    Service.gI().sendThongBao(player,
                                            "Bạn Nhận Được 200 " + thoivanggggg.template.name + " Nhớ out game vô lại");
                                    break;
                            }

                            ps = con.prepareStatement("update account set coin = ? where id = ?");
                            ps.setInt(1, player.getSession().vnd);
                            ps.setInt(2, player.getSession().userId);
                            ps.executeUpdate();
                            ps.close();

                        } catch (Exception e) {
                            Logger.logException(NpcFactory.class, e, "Lỗi update coin " + player.name);
                        } finally {
                            try {
                                if (ps != null) {
                                    ps.close();
                                }
                            } catch (SQLException ex) {
                                System.out.println("Lỗi khi update coin");
                            }
                        }
                    }
                }

            }

        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1,
                                        "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước",
                                        "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                            // }
                            // } else if (player.iDMark.getIndexMenu() == 1) {
                            //
                            // if (player.clan == null) {
                            // Service.gI().sendThongBao(player, "Không có bang hội");
                            // return;
                            // }
                            // if (player.idNRNM != 353) {
                            // Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            // return;
                            // }
                            //
                            // byte numChar = 0;
                            // for (Player pl : player.zone.getPlayers()) {
                            // if (pl.clan.id == player.clan.id && pl.id != player.id) {
                            // if (pl.idNRNM != -1) {
                            // numChar++;
                            // }
                            // }
                            // }
                            // if (numChar < 6) {
                            // Service.gI().sendThongBao(player, "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck
                            // đi");
                            // return;
                            // }
                            //
                            // if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            // if (player.idNRNM == 353) {
                            //// NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() +
                            // 86400000;
                            //// NgocRongNamecService.gI().firstNrNamec = true;
                            //// NgocRongNamecService.gI().timeNrNamec = 0;
                            //// NgocRongNamecService.gI().doneDragonNamec();
                            //// NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                            //// NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                            // SummonDragon.gI().summonNamec(player);
                            // } else {
                            // Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                        }
                    }
                }
            }
            // }
            // };
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi",
                                            "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                        : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(player);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                        + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                                this.createOtherMenu(player, 2,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            } else {
                                this.createOtherMenu(player, 3,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.getIndexMenu() == 2) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                            }
                        }
                    }
                }
                if (this.mapId == 19) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.playerTask.taskMain.id >= 24) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Hãy hoàn thành những nhiệm vụ trước đó");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS && boss.zone != null
                                            && boss.zone.map != null) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                if (boss != null && !boss.isDie()) {
                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                        if (player != null && boss != null && boss.zone != null) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;

                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
                                Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                if (boss != null && !boss.isDie()) {
                                    if (player != null && boss.zone != null
                                            && player.inventory.gold >= COST_FIND_BOSS) {
                                        Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                boss.zone.zoneId);
                                        if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                            player.inventory.gold -= COST_FIND_BOSS;
                                            ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                    boss.location.y);
                                            Service.gI().sendMoney(player);
                                        } else {
                                            Service.gI().sendThongBao(player, "Khu vực đang full.");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chết rồi ba...");
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào ta có bán 1 số sản phẩm con có mua không ",
                            "Cửa hàng", "Quy đổi capsul\n Hồng Ngọc", "không");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 5,
                                            "Giá Capsule hồng ngọc tại Cửa hàng Santa: 500Tr vàng\n"
                                                    + "Ta hỗ trợ mua với số lượng lớn quy định như sau:\n"
                                                    + "10 Capsule hồng ngọc = 10 thỏi vàng\n"
                                                    + "105 Capsule hồng ngọc = 100 thỏi vàng\n"
                                                    + "1100 Capsule hồng ngọc = 1000 thỏi vàng\n"
                                                    + "Ngươi muốn mua số lượng bao nhiêu?",
                                            "Mua 10\nCapsule\nhồng ngọc", "Mua 105\nCapsule\nhồng ngọc",
                                            "Mua 1100\nCapsule\nhồng ngọc");
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == 5) {
                            switch (select) {
                                case 0:
                                    Item xudaiviet = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    if (xudaiviet != null) {
                                        if (xudaiviet.quantity >= 10
                                                && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item yardart = ItemService.gI().createNewItem((short) 984, 10);
                                            InventoryServiceNew.gI().addItemBag(player, yardart);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet, 10);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn vừa nhận được capsule hồng ngọc");
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn không đủ 10 thỏi vàng ");
                                        }
                                    }
                                    break;
                                case 1:
                                    Item xudaiviet1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    if (xudaiviet1 != null) {
                                        if (xudaiviet1.quantity >= 100
                                                && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item yardart = ItemService.gI().createNewItem((short) 984, 105);
                                            InventoryServiceNew.gI().addItemBag(player, yardart);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet1, 100);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn vừa nhận được capsule hồng ngọc");
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn không đủ 100 thỏi vàng");
                                        }
                                    }
                                    break;
                                case 2:
                                    Item xudaiviet2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                    if (xudaiviet2 != null) {
                                        if (xudaiviet2.quantity >= 1000
                                                && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item yardart = ItemService.gI().createNewItem((short) 984, 1100);
                                            InventoryServiceNew.gI().addItemBag(player, yardart);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet2, 1000);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn vừa nhận được capsule ");
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn không đủ 1000 thỏi vàng");
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ADMIN(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào ta hỗ trợ đổi vnd sang ",
                            "Đổi Thỏi Vàng", "Đổi Xu\n", "Nhận Qùa\nMốc Nạp", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    this.createOtherMenu(player, 333,
                                            "\b|1|Muốn đổi thỏi vàng à?\n \b|7|Bạn đang có :" + player.getSession().vnd
                                                    + " VNĐ",
                                            "10k\n1000 thỏi vàng", "20k\n2000 thỏi vàng", "50k\n5500 thỏi vàng",
                                            "100k\n12000 thỏi vàng");
                                    break;
                                case 1: // shop
                                    this.createOtherMenu(player, 334,
                                            "\b|1|Muốn đổi Xu  à?\n \b|7|Bạn đang có :" + player.getSession().vnd
                                                    + " VNĐ",
                                            "10k\n10 Xu", "20k\n20 Xu", "50k\n55 Xu", "100k\n120 Xu");
                                    break;
                                case 2:
                                    if (player.getSession() != null) {
                                        this.createOtherMenu(player, 9899, "Nạp đạt mốc nhận quà he :3",
                                                "Xem quà mốc nạp", "Nhận quà mốc nạp", "Đóng");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 9899) {
                            switch (select) {
                                case 0:
                                    if (player.getSession() != null) {
                                        Service.gI().sendThongBaoOK(player, Arrays.toString(Archivement.Textmocnap));
                                    }
                                    break;
                                case 1:
                                    // if (player.getSession().actived) {
                                    Archivement.gI().getAchievement(player);
                                    // } else {
                                    // Service.gI().sendThongBao(player, "Chưa nạp mà đòi xem à con");
                                    // }
                                    break;
                                case 2:
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == 333) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 10000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 10k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 10000)) {

                                        Item i = ItemService.gI().createNewItem((short) 457, (short) 1000);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 1k thỏi vàng");
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 20k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 20000)) {

                                        Item i = ItemService.gI().createNewItem((short) 457, (short) 2000);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 2k thỏi vàng");
                                    }
                                    break;
                                case 2:
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 50k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 50000)) {

                                        Item i = ItemService.gI().createNewItem((short) 457, (short) 5500);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 5k5 thỏi vàng");
                                    }
                                    break;
                                case 3:
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 100k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 100000)) {

                                        Item i = ItemService.gI().createNewItem((short) 457, (short) 12000);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 12k thỏi vàng");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 334) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd < 10000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 10k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 10000)) {

                                        Item i = ItemService.gI().createNewItem((short) 1335, (short) 10);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 10 Xu ");
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().vnd < 20000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 20k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 20000)) {

                                        Item i = ItemService.gI().createNewItem((short) 1335, (short) 20);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 20 Xu ");
                                    }
                                    break;
                                case 2:
                                    if (player.getSession().vnd < 50000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 50k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 50000)) {

                                        Item i = ItemService.gI().createNewItem((short) 1335, (short) 55);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 55 Xu ");
                                    }
                                    break;
                                case 3:
                                    if (player.getSession().vnd < 100000) {
                                        Service.gI().sendThongBaoOK(player, "Bạn không đủ 100k VND");
                                        return;
                                    }

                                    if (PlayerDAO.subvnd(player, 100000)) {

                                        Item i = ItemService.gI().createNewItem((short) 1335, (short) 120);
                                        InventoryServiceNew.gI().addItemBag(player, i);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBaoOK(player, "Bạn đã nhận được 120 Xu ");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc trongtai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào cậu muốn ta giúp gì",
                            "giải đấu bang hội", "Cửa Hàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 13) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendThongBaoOK(player, "Chức năng đang bảo trì!!!");
                                    break;
                                case 1: // shop
                                    ShopServiceNew.gI().opendShop(player, "SHOP_DCM", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bunma(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào ta hỗ trợ cậu bán đồ vip nhất game ",
                            "Cửa Hàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "SHOP_ĐẠI_VIỆT", false);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private Random random;
            private int quantity;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "SỰ KIỆN TẾT NRO : x20 Lì Xì = 10 thỏi vàng.\n"
                                    + "x50 Lì Xì = 1 Cải trang tết .\n"
                                    + "x2 thiệp chúc để chúc tết NPC và nhận được món quà bất ngờ .\n"
                                    + "Ngọc Rồng Green Chúc Anh Em 1 Năm Mới An Lành.\n"
                                    + "Vạn Sự Như Ý, Làm ăn phát đạt, Có tiền nạp lần đầu !!!.\n",
                            "Nhận Thỏi Vàng Free", "Đổi Cải Trang", "Chúc Tết NPC");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {

                                    Item botmi = null;

                                    try {

                                        botmi = InventoryServiceNew.gI().findItemBag(player, 717);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (botmi == null || botmi.quantity < 20) {
                                        this.npcChat(player, "Hãy Đưa Ta 20 Lì Xì");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, botmi, 20);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, conga, 99);
                                        // player.inventory.ruby -= 1000;
                                        Service.getInstance().sendMoney(player);
                                        // Random random = new Random(); // quantity = 100
                                        // Item caitrang = ItemService.gI().createNewItem((short) 1264);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 457, quantity = 10);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 10 Thỏi Vàng từ bao lì xì ");
                                    } // caitrang.itemOptions.add(new Item.ItemOption(50, getRandomValue(10, 30,
                                      // random)));
                                    break;
                                }
                                // case 1: {
                                // Item botmi = null;
                                // Item dauxanh = null;
                                // Item trung = null;
                                //
                                // try {
                                //
                                // botmi = InventoryServiceNew.gI().findItemBag(player, 888);
                                // dauxanh = InventoryServiceNew.gI().findItemBag(player, 889);
                                // trung = InventoryServiceNew.gI().findItemBag(player, 886);
                                //
                                // } catch (Exception e) {
                                //// throw new RuntimeException(e);
                                // }
                                // if (botmi == null || botmi.quantity < 99 || dauxanh == null ||
                                // dauxanh.quantity < 99 || trung == null || trung.quantity < 99) {
                                // this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                //
                                // } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                // this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                // } else {
                                //
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, botmi, 99);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 99);
                                //
                                // Service.getInstance().sendMoney(player);
                                // player.inventory.ruby -= 1000;
                                // Item trungLinhThu = ItemService.gI().createNewItem((short) 466);
                                // InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                // InventoryServiceNew.gI().sendItemBags(player);
                                // this.npcChat(player, "Bạn nhận được 1 bánh trung thu 2 nhân");
                                // }
                                // break;
                                // }
                                // case 2: {
                                // Item botmi = null;
                                // Item dauxanh = null;
                                // Item trung = null;
                                //
                                // try {
                                //
                                // botmi = InventoryServiceNew.gI().findItemBag(player, 888);
                                // dauxanh = InventoryServiceNew.gI().findItemBag(player, 889);
                                // trung = InventoryServiceNew.gI().findItemBag(player, 886);
                                //
                                // } catch (Exception e) {
                                //// throw new RuntimeException(e);
                                // }
                                // if (botmi == null || botmi.quantity < 99 || dauxanh == null ||
                                // dauxanh.quantity < 99 || trung == null || trung.quantity < 99) {
                                // this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                //
                                // } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                // this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                // } else {
                                //
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, botmi, 99);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 99);
                                //
                                // Service.getInstance().sendMoney(player);
                                // player.inventory.ruby -= 1000;
                                // Item trungLinhThu = ItemService.gI().createNewItem((short) 472);
                                // InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                // InventoryServiceNew.gI().sendItemBags(player);
                                // this.npcChat(player, "Bạn nhận được banh trung thu đặc biệt");
                                // }
                                // break;
                                // }
                                // case 3: {
                                // Item banh1nhan = null;
                                // Item banh2nhan = null;
                                // Item banhdb = null;
                                //
                                // try {
                                //
                                // banh1nhan = InventoryServiceNew.gI().findItemBag(player, 465);
                                // banh2nhan = InventoryServiceNew.gI().findItemBag(player, 466);
                                // banhdb = InventoryServiceNew.gI().findItemBag(player, 472);
                                //
                                // } catch (Exception e) {
                                //// throw new RuntimeException(e);
                                // }
                                // if (banh1nhan == null || banh1nhan.quantity < 24 || banh2nhan == null ||
                                // banh2nhan.quantity < 24 || banhdb == null || banhdb.quantity < 24) {
                                // this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                //
                                // } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                // this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                // } else {
                                //
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, banh1nhan, 25);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, banh2nhan, 25);
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, banhdb, 25);
                                //
                                // Service.getInstance().sendMoney(player);
                                // player.inventory.ruby -= 10000;
                                // Item trungLinhThu = ItemService.gI().createNewItem((short) 473);
                                // InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                // InventoryServiceNew.gI().sendItemBags(player);
                                // this.npcChat(player, "Bạn nhận được banh trung thu đặc biệt");
                                // }
                                // break;
                                // }
                                // case 4: {
                                // Item banh1nhan = null;
                                // Item banh2nhan = null;
                                // Item banhdb = null;
                                //
                                // try {
                                //
                                // banhdb = InventoryServiceNew.gI().findItemBag(player, 473);
                                //
                                // } catch (Exception e) {
                                //// throw new RuntimeException(e);
                                // }
                                // if (banhdb == null || banhdb.quantity < 0) {
                                // this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                //
                                // } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                // this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                // } else {
                                //
                                // InventoryServiceNew.gI().subQuantityItemsBag(player, banhdb, 1);
                                //
                                // Service.getInstance().sendMoney(player);
                                // player.inventory.ruby -= 10000;
                                // Item trungLinhThu = ItemService.gI().createNewItem((short) 457, quantity =
                                // 100);
                                // InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                // InventoryServiceNew.gI().sendItemBags(player);
                                // this.npcChat(player, "Bạn nhận được 100 thỏi vàng");
                                // }
                                // break;
                                // }
                                case 1: {
                                    Item banh1nhan = null;
                                    // Item banh2nhan = null;

                                    try {

                                        banh1nhan = InventoryServiceNew.gI().findItemBag(player, 717);
                                        // banh2nhan = InventoryServiceNew.gI().findItemBag(player, 649);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (banh1nhan == null || banh1nhan.quantity < 50) {
                                        this.npcChat(player, "Hãy đưa ta 50 bao lì xì");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, banh1nhan, 50);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, banh2nhan, 99);
                                        // player.inventory.ruby -= 10000;
                                        Random random = new Random();
                                        Item caitrang = ItemService.gI().createNewItem((short) 2064);
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(50, getRandomValue(10, 30, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(77, getRandomValue(10, 35, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(103, getRandomValue(10, 35, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(95, getRandomValue(2, 20, random)));
                                        caitrang.itemOptions
                                                .add(new Item.ItemOption(96, getRandomValue(2, 20, random)));
                                        caitrang.itemOptions.add(new Item.ItemOption(106, 0));
                                        if (Util.isTrue(99, 100)) {
                                            caitrang.itemOptions
                                                    .add(new Item.ItemOption(93, getRandomValue(1, 20, random)));
                                        }

                                        caitrang.itemOptions.add(new Item.ItemOption(30, 0));

                                        InventoryServiceNew.gI().addItemBag(player, caitrang);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                        this.npcChat(player, "Bạn Nhận Được cải trang sự kiện !");
                                    }
                                    break;

                                }
                                case 2: {
                                    Item botmi = null;
                                    // Item dauxanh = null;
                                    // Item trung = null;

                                    try {

                                        botmi = InventoryServiceNew.gI().findItemBag(player, 399);
                                        // dauxanh = InventoryServiceNew.gI().findItemBag(player, 889);
                                        // trung = InventoryServiceNew.gI().findItemBag(player, 886);

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (botmi == null || botmi.quantity < 2) {
                                        this.npcChat(player, "Không đủ 2 thiệp để chúc tết");

                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {

                                        InventoryServiceNew.gI().subQuantityItemsBag(player, botmi, 2);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                        // InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 99);

                                        Service.getInstance().sendMoney(player);
                                        // player.inventory.ruby -= 1000;
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 397);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 hộp quà tết");
                                    }
                                    break;
                                    // }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thoren(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "\b|7|Bạn cần đổi gì?\b|7|", "Đổi đồ\nHủy Diệt\nTrái Đất", "Đổi đồ\nHuy Diệt\nNamek",
                            "Đổi Đồ\nHủy Diệt\nxayda");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, 1,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nTrái đất cùng loại , 500tr vàng và x10 đá ngũ sắc \n|6|Để đổi lấy",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, 2,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nNamek cùng loại  , 500tr vàngvà x10 đá ngũ sắc \n|6|Để đổi lấy",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;
                                    case 2:
                                        this.createOtherMenu(player, 3,
                                                "\b|7|Bạn muốn đổi 1 món đồ thần linh \nXayda cùng loại  , 500tr vàngvà x10 đá ngũ sắc \n|6|Để đổi lấy ",
                                                "Áo\nHúy Diệt", "Quần\nHúy Diệt", "Găng\nDúy Diệt", "Giày\nHúy Diệt",
                                                "Nhẫn\nHúy Diệt", "Thôi Khỏi");
                                        break;

                                }
                                break;
                            case 1:
                            case 2:
                            case 3:
                                com.girlkun.services.func.UpdateItem.StartUpdate(player,
                                        player.iDMark.getIndexMenu() - 1, select, this);
                        }
                    }
                }
            }

        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }
    //
    // public static Npc gohannn(int mapId, int status, int cx, int cy, int tempId,
    // int avartar) {
    // return new Npc(mapId, status, cx, cy, tempId, avartar) {
    //
    // @Override
    // public void openBaseMenu(Player player) {
    // if (canOpenNpc(player)) {
    // if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
    // this.createOtherMenu(player, 0, "Tiến vào map hỗ trợ tân thủ\nNơi up set kích
    // hoạt và nhiều phần quà hấp dẫn\nChỉ dành cho người chơi từ 2k đến 60 tỷ sức
    // mạnh!", "Đến\nRừng Aurura", "Từ chối");
    //
    // } else {
    // this.createOtherMenu(player, 0, "Ngươi muốn quay về?", "Quay về", "Từ chối");
    // }
    // }
    //
    // }
    //
    // @Override
    // public void confirmMenu(Player player, int select) {
    // if (canOpenNpc(player)) {
    //
    // switch (select) {
    // case 0:
    // if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
    // if (!player.getSession().actived) {
    // Service.gI().sendThongBaoFromAdmin(player,
    // "|5|Vui lòng kích hoạt thành viên để sử dụng tính năng này!");
    // break;
    // } else {
    // ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 295);
    // break;
    // }
    // } else {
    // ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1,
    // 295);
    // }
    // case 1:
    //
    // break;
    // }
    //
    // }
    // }
    // };
    // }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 182) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Admin chưa giao việc cho ta, Hãy quay lại sau khi ta được admin update chức năng mới nhé !?",
                                "Đóng");
                    } else if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Nâng cấp\nSet Kích Hoạt");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {

                if (canOpenNpc(player)) {
                    if (this.mapId == 182) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Chức năng đang được nghiên cứu và sẽ update trong tương lai gần "
                                                    + "Sau khi update, các bạn sẽ có thêm chức năng mới tại đây !",
                                            "Đóng");
                                    break;
                            }
                        }
                        if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.ChanThienTu:
                                case CombineServiceNew.ChanThienTu2:
                                    CombineServiceNew.gI().startCombine(player, select);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.NANG_CAP_DO_KICH_HOAT);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_DO_KICH_HOAT:
                                case CombineServiceNew.NANG_CAP_DO_KICH_HOAT_THUONG:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.ChanThienTu:
                                case CombineServiceNew.ChanThienTu2:
                                    CombineServiceNew.gI().startCombine(player, select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT_THUONG) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_MEO) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        }

                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                    + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 tháng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: // nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: // làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player, 0);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc onggianoel(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta là Ngộ Không, đang trên đường đi thỉnh kinh nhưng ta bị mắc ỉa. Ngươi có thể hộ tống sư phụ dùm ta được không?\nNgươi sẽ nhận phần thưởng xứng đáng!",
                            "Hướng dẫn\n Hộ Tống", "Hộ Tống", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.getInstance().sendThongBaoFromAdmin(player,
                                        "Gặp NPC, chọn Hộ Tống rồi dắt boss đến vị trí được chỉ định.\n"
                                                + "Phần quà: 3K Hồng Ngọc. Phí dắt: 1K Hồng Ngọc.");
                                break;
                            case 1:
                                Boss oldDuongTank = BossManager.gI()
                                        .getBossById(Util.createIdDuongTank((int) player.id));
                                if (oldDuongTank != null) {
                                    this.npcChat(player, "Boss đang được hộ tống, khu " + oldDuongTank.zone.zoneId);
                                } else if (player.inventory.ruby < 1000) {
                                    this.npcChat(player, "Nhà ngươi không đủ 1K Hồng Ngọc");
                                } else {
                                    BossData bossDataClone = new BossData(
                                            "Đường Tăng do " + player.name + " hộ tống",
                                            (byte) 2,
                                            new short[] { 467, 468, 469, -1, -1, -1 },
                                            100000,
                                            new int[] { (int) (player.nPoint.hpMax / 2) },
                                            new int[] { 103 },
                                            new int[][] { { Skill.TAI_TAO_NANG_LUONG, 7, 15000 } },
                                            new String[] {},
                                            new String[] {},
                                            new String[] {},
                                            60);
                                    try {
                                        DuongTank dt = new DuongTank(
                                                Util.createIdDuongTank((int) player.id),
                                                bossDataClone,
                                                player.zone,
                                                player.location.x - 20,
                                                player.location.y);
                                        dt.setPlayerTarget(player);
                                        int[] map = { 6, 29, 30, 4, 5, 27, 28 };
                                        player.mapCongDuc = map[Util.nextInt(map.length)];
                                        player.haveDuongTang = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    player.inventory.ruby -= 1000;
                                    Service.getInstance().sendMoney(player);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        nguhs.gI().setTimeJoinnguhs();
                        long now = System.currentTimeMillis();
                        if (now > nguhs.TIME_OPEN_NHS && now < nguhs.TIME_CLOSE_NHS) {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x2 TNSM\b|1|Từ 1tỷ5 SM dến 100 Tỷ SM?", "Vào",
                                    "Từ chối");
                        } else {
                            Service.gI().sendThongBao(player, "Chưa tới giờ hoặc đã hết hạn vô Ngũ hành sơn");
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x2 TNSM\b|1|Từ 1tỷ5 SM dến 100 Tỷ SM?", "Vào",
                                    "Từ chối");
                        }
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0,
                                "A mi khò khò, ở Ngũ hành sơn có lũ khỉ đã ăn trộm Hồng Đào\b Thí chủ có thể giúp ta lấy lại Hồng Đào từ chúng\bTa sẽ đổi 1 ít đồ để đổi lấy Hồng Đào.",
                                "Cửa hàng\nHồng Đào", "Về\nLàng Aru", "Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: "
                                + player.NguHanhSonPoint
                                + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang không?\n 10000 điểm : Cải trang x4 chưởng\n 5000 điểm : Cải trang x3 chưởng\n 2000 điểm : Cải trang ngẫu nhiên HSD hoặc vĩnh viễn",
                                "Cải trang x4 chưởng", "Cải trang x3 chưởng", "Cải trang ngẫu nhiên",
                                "Top Ngu Hanh Son", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (mapId == 0) {
                        if (player.nPoint.power < 1500000000) {
                            Service.gI().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                                break;
                            case 1:
                                break;

                        }
                    }
                    if (mapId == 123) {
                        if (select == 0) {
                            ShopServiceNew.gI().opendShop(player, "TAYDUKY", true);
                        }
                        if (select == 1) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                        }
                    }
                    if (mapId == 122) {
                        if (select == 0) {
                            if (player.NguHanhSonPoint >= 10000) {
                                player.NguHanhSonPoint -= 10000;
                                Item item = ItemService.gI().createNewItem((short) (711));
                                item.itemOptions.add(new Item.ItemOption(49, 23));// SD
                                item.itemOptions.add(new Item.ItemOption(77, 21));// HP
                                item.itemOptions.add(new Item.ItemOption(103, 21));// KI
                                item.itemOptions.add(new Item.ItemOption(160, 50));// TNSM
                                item.itemOptions.add(new Item.ItemOption(159, 4));// X4
                                item.itemOptions.add(new Item.ItemOption(207, 0));// CT EVENT
                                item.itemOptions.add(new Item.ItemOption(33, 0));// DICH CHUYEN
                                item.itemOptions.add(new Item.ItemOption(21, 40));// YEU CAU SM
                                item.itemOptions.add(new Item.ItemOption(154, 0));// KHONG THE BAN
                                item.itemOptions.add(new Item.ItemOption(174, 2021)); // SK NAM
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.getInstance().sendThongBao(player,
                                        "Chúc Mừng Bạn Đổi " + item.Name() + " Thành Công !");
                                InventoryServiceNew.gI().sendItemBags(player);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Không đủ điểm, bạn còn " + (10000 - player.NguHanhSonPoint) + " điểm nữa");
                            }
                        }
                        if (select == 1) {
                            if (player.NguHanhSonPoint >= 5000) {
                                player.NguHanhSonPoint -= 5000;
                                Item item = ItemService.gI().createNewItem((short) (710));
                                item.itemOptions.add(new Item.ItemOption(49, 22));
                                item.itemOptions.add(new Item.ItemOption(77, 20));
                                item.itemOptions.add(new Item.ItemOption(103, 20));
                                item.itemOptions.add(new Item.ItemOption(160, 35));
                                item.itemOptions.add(new Item.ItemOption(159, 3));
                                item.itemOptions.add(new Item.ItemOption(207, 0));
                                item.itemOptions.add(new Item.ItemOption(21, 40));
                                item.itemOptions.add(new Item.ItemOption(154, 0));
                                item.itemOptions.add(new Item.ItemOption(174, 2021));
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.getInstance().sendThongBao(player,
                                        "Chúc Mừng Bạn Đổi " + item.Name() + "Thành Công !");
                                InventoryServiceNew.gI().sendItemBags(player);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Không đủ điểm, bạn còn " + (5000 - player.NguHanhSonPoint) + " điểm nữa");
                            }
                        }
                        if (select == 2) {
                            if (player.NguHanhSonPoint >= 2000) {
                                player.NguHanhSonPoint -= 2000;
                                Item item = ItemService.gI().createNewItem((short) (1997));
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.getInstance().sendThongBao(player,
                                        "Chúc Mừng Bạn Đổi " + item.Name() + "Thành Công !");
                                InventoryServiceNew.gI().sendItemBags(player);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Không đủ điểm, bạn còn " + (2000 - player.NguHanhSonPoint) + " điểm nữa");
                            }
                        }
                        if (select == 3) {
                            Service.getInstance().showListTop(player, Manager.topNHS);
                        }
                    }

                }
            }
        };
    }

    public static Npc hoanguc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "bạn muốn vào hành tinh hoả?.", "Đồng ý", "Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Thí chủ muốn quay về làng Aru?", "Đồng ý", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0,
                                "A mi khò khò, ở Ngũ hành sơn có lũ khỉ đã ăn trộm Hồng Đào\b Thí chủ có thể giúp ta lấy lại Hồng Đào từ chúng\bTa sẽ đổi 1 ít đồ để đổi lấy Hồng Đào.",
                                "Cửa hàng\nHồng Đào", "Về\nLàng Aru", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 0) {
                                if (player.nPoint.power < 400000000000L || player.nPoint.power >= 2000000000000L) {
                                    this.npcChat(player, "yeu cau 40 ti sm!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapBySpaceShip(player, 173, -1, 96);
                            }
                            if (mapId == 122) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                            }
                            if (mapId == 124) {
                                if (select == 0) {
                                    ShopServiceNew.gI().opendShop(player, "TAYDUKY", true);
                                    break;
                                }
                                if (select == 1) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 936);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            // kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            // về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        // kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        if (player.playerTask.taskMain.id >= 21) {
                            ChangeMapService.gI().goToTuongLai(player);
                        } else {
                            Service.gI().sendThongBao(player, "Hãy hoàn thành những nhiệm vụ trước đó");
                        }
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay",
                                "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }
            // if (player.getSession().player.playerTask.taskMain.id >= 24) {
            // ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
            // } else {
            // this.npcChat(player, "Hãy hoàn thành những nhiệm vụ trước đó");
            // }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.playerTask.taskMain.id >= 22) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Hãy hoàn thành xong nhiệm vụ fide");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (!player.getSession().actived) {
                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 10.000 hồng ngọc không?",
                                    "Gọi Boss\nNhân bản", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (player.nPoint.power < 50000000000L) {
                    // if (player.getSession().player.playerTask.taskMain.id >= 22) {
                    // if (!player.getSession().actived) {
                    Service.gI().sendThongBao(player, "Yêu cầu sức mạnh là 50 tỉ !");
                } else if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI()
                                            .getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player,
                                                "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu "
                                                        + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.ruby < 10000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 10.000 Hồng ngọc ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[] { player.getHead(), player.getBody(), player.getLeg(),
                                                        player.getFlagBag(), player.idAura },
                                                player.nPoint.dame,
                                                new int[] { player.nPoint.hpMax },
                                                new int[] { 140 },
                                                skillTemp,
                                                new String[] { "|-2|Boss nhân bản đã xuất hiện rồi" }, // text chat 1
                                                new String[] { "|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!" }, // text
                                                                                                                   // chat
                                                                                                                   // 2
                                                new String[] { "|-1|Lần khác ta sẽ xử đẹp ngươi" }, // text chat 3
                                                60);

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone,
                                                    player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        // trừ vàng khi gọi boss
                                        player.inventory.ruby -= 10000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "\b|7|Bạn cần đổi random gì?\b|7|", "Đổi pet\nBằng thỏi vàng",
                            "Đổi thú cưỡi\nBằng thỏi vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "\b|7|Bạn có mún đổi 100 thỏi vàng random Danh hiệu thiên tử, có tỉ lệ vĩnh viễn?",
                                            "Úm ba la ta ra được nịt", "Từ Chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, 2,
                                            "\b|7|Bạn có mún đổi 200 thỏi vàng random thú cưỡi vip có tỉ lệ vĩnh viễn ?",
                                            "úm ba la ta ra được nịt");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0:
                                    Item vy1 = null;
                                    try {
                                        vy1 = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (vy1 == null || vy1.quantity < 100) {
                                        this.npcChat(player, "Bạn cần có x100 thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 100;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vy1, 100);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1323);
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10) + 5));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10) + 10));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10) + 10));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1) + 6));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, Util.nextInt(10) + 5));
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Vui lòng kiểm tra hành trang !!!");
                                    }
                                    break;
                                case 1: // canel
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 2) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                    Item hoa = null;
                                    try {
                                        hoa = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    if (hoa == null || hoa.quantity < 200) {
                                        this.npcChat(player, "Bạn cần có 200 TV");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, hoa, 200);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 746);
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10) + 12));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10) + 3));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10) + 7));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1) + 25));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, Util.nextInt(10) + 8));
                                        trungLinhThu.itemOptions.add(new Item.ItemOption(217, Util.nextInt(10) + 8));
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Vui lòng kiểm tra hành trang !!! ");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                    // if (this.mapId == 0) {
                    // this.createOtherMenu(player, 0,
                    // "Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " điểm PvP Point",
                    // "Đến DHVT", "Đổi Cải trang sự kiên", "Top PVP");
                    // }
                    if (this.mapId == 129 || this.mapId == 141) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay về");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    // if (this.mapId == 0) {
                    // if (player.iDMark.getIndexMenu() == 0) { //
                    // switch (select) {
                    // case 0:
                    // ChangeMapService.gI().changeMapBySpaceShip(player, 129, -1, 354);
                    // Service.getInstance().changeFlag(player, Util.nextInt(8));
                    // break; // qua dhvt
                    // case 1: //
                    // this.createOtherMenu(player, 1,
                    // "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ
                    // số là 80%\n ", "Ok", "Tu choi");
                    // // bat menu doi item
                    // break;
                    //
                    // case 2: //
                    // Util.showListTop(player, (byte) 3);
                    // // mo top pvp
                    // break;
                    //
                    // }
                    // }
                    // if (player.iDMark.getIndexMenu() == 1) { // action doi item
                    // switch (select) {
                    // case 0: // trade
                    // if (player.pointPvp >= 500) {
                    // player.pointPvp -= 500;
                    // Item item = ItemService.gI().createNewItem((short) (1104));
                    // item.itemOptions.add(new Item.ItemOption(49, 80));
                    // item.itemOptions.add(new Item.ItemOption(77, 80));
                    // item.itemOptions.add(new Item.ItemOption(103, 50));
                    // item.itemOptions.add(new Item.ItemOption(207, 0));
                    // item.itemOptions.add(new Item.ItemOption(33, 0));
                    ////
                    // InventoryServiceNew.gI().addItemBag(player, item);
                    // Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành
                    // Công !");
                    // } else {
                    // Service.getInstance().sendThongBao(player, "Không đủ điểm bạn còn " + (500 -
                    // player.pointPvp) + " Điểm nữa");
                    // }
                    // break;
                    // }
                    // }
                    // }
                    if (this.mapId == 129) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 141) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                                    + (player.inventory.itemsBoxCrackBall.size()
                                                            - InventoryServiceNew.gI().getCountEmptyListItem(
                                                                    player.inventory.itemsBoxCrackBall))
                                                    + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                    + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48 || this.mapId == 0) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48 || this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                            "Con đường\n rắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMap(player, 141, -1, 318, 336);// con đường rắn độc
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 80 tỉ sức mạnh!\n"
                                + "Nếu đã săn sàng rồi ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 80 tỉ sức mạnh!\n"
                                + "Nếu đã săn sàng rồi ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn úp mảnh thiên sứ ? \n"
                                + "Vậy thì hãy đạt 80 tỉ sức mạnh!\n"
                                + "Nếu đã săn sàng rồi ta sẽ dịch chuyển ngươi qua map up mảnh thiên sứ siêu vip !!!",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception e) {
                            System.err.print("\nError at 212\n");
                            Logger.error("Lỗi mở menu osin");
                            e.printStackTrace();
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (player.nPoint.power < 80_000_000_000L) {
                                        Service.getInstance().sendThongBao(player, "Sức mạnh cần 80 tỉ để qua map!");
                                    } else {
                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                        ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                        return;
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    // if (!player.getSession().actived) {
                                    // Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng
                                    // chức năng này");
                                    // } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1,
                                        this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.lastTimeOpenDoanhTrai = (System.currentTimeMillis() - 300000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        // player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    // if (player.clan != null && player.name.contains("trumticker")) {
                    // createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                    // "NQMP",
                    // "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                    // return;
                    // }
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                        + "Thời gian còn lại là "
                                        + (TimeUtil.getSecondLeft(player.clan.lastTimeOpenDoanhTrai,
                                                DoanhTrai.TIME_DOANH_TRAI / 1000)) / 60
                                        + "p. Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP
                                        + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                        + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                        + "Hahaha.",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.LastDoanhTrai <= 1 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi đã đi hôm nay rồi \n Thời gian chờ còn lại để có thể vào : "
                                        + (((player.LastDoanhTrai + (1 * 24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + " s",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.createTimeLong <= 2 * 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi chỉ vừa tạo.Hãy chờ đủ 2 ngày để có thể vào doanh trại \n Thời gian chờ còn lại để có thể vào : "
                                        + (((player.clan.createTimeLong + (2 * 24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + " s",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (System.currentTimeMillis() - player.clan.lastTimeOpenDoanhTrai <= 24 * 60 * 60 * 1000) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi doanh trại gần đây .Hãy chờ"
                                        + (((player.clan.lastTimeOpenDoanhTrai + (24 * 60 * 60 * 1000))
                                                - System.currentTimeMillis()) / 1000)
                                        + "s để có thể tham gia doanh trại lần nữa. Hẹn ngươi quay lại vào ngày mai",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }

                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override

            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?",
                                "OK", "Từ Chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi DesTroy Gas cấp độ "
                                                        + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
                                                        + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau1(player,
                                        Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc meothantai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0,
                        "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu!Hồng Ngọc trên 1tr5 có thể quy đổi sang ATM, ae có thể rút tiền mặt"
                                + "\n kết quả từ của tổng 3 xúc sắc từ 3 -> 10 (Xỉu) Lớn hơn hoặc bằng 11 sẽ là (Tài), bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                                + "\n\n|7|(Điều kiện tham gia : Có đủ số dư tối thiểu là 250.000 Hồng Ngọc)\n\n|2|Đặt tối thiểu: 20.000 VNĐ\n Tối đa: 1.000.000.000.000 VNĐ \n "
                                + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng",
                        "Hướng Dẫn Chơi", "Tham gia");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU,
                                    "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                            + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                            + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                            + "\n\n|7|Lưu ý: Số Hồng Ngọc nhận được sẽ bị nhà cái lụm đi 20%. Thắng sẽ x2,3 số hồng ngọc. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY",
                                    "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false) {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU  ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu",
                                            "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu",
                                            "Đóng");
                                } else {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu",
                                            "Đóng");
                                }
                            } else {
                                if (pl.goldTai == 0 && pl.goldXiu == 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu",
                                            "Đóng");
                                } else if (pl.goldTai > 0) {
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đặt Tài", "Đặt Xỉu",
                                            "Đóng");
                                } else {
                                    createOtherMenu(pl, 1,
                                            "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : "
                                                    + TaiXiu.gI().y + " : " + TaiXiu.gI().z + "\n\n|6|Tổng nhà TÀI: "
                                                    + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                                    + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu)
                                                    + " Hồng Ngọc\n\n|5|Thời gian còn lại: " + time
                                                    + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu)
                                                    + " Hồng Ngọc" + "\n\n|7|Hệ thống sắp bảo trì",
                                            "Cập nhập", "Đóng");
                                }
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai == 0
                                && pl.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Đặt Xỉu",
                                            "Đóng");
                                    break;
                                case 1:
                                    if (pl.inventory.ruby >= 250000) {
                                        Input.gI().TAI_taixiu(pl);
                                    } else {
                                        Service.gI().sendThongBao(pl,
                                                "Số dư của bạn cần ít nhất 250.000 để tham gia đánh tài Tài");
                                    }
                                    break;
                                case 2:
                                    if (pl.inventory.ruby >= 250000) {
                                        Input.gI().XIU_taixiu(pl);
                                    } else {
                                        Service.gI().sendThongBao(pl,
                                                "Số dư của bạn cần ít nhất 250.000 để tham gia đánh tài xỉu");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0
                                && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0
                                && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU Ngọc Rồng ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0
                                && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU Ngọc Rồng ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0
                                && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU Ngọc Rồng ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");

                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0
                                && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---TÀI XỈU Ngọc Rồng ---\n\n|3|Kết quả kì trước:  "
                                            + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt TÀI: " + TaiXiu.gI().PlayersTai.size() + " người"
                                            + "\n\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng Ngọc"
                                            + "\n|1|Tổng người đặt XỈU: " + TaiXiu.gI().PlayersXiu.size() + " người"
                                            + "\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU",
                                            "Đóng");

                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng",
                                    "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng",
                                    "Đóng");
                        }
                    }
                    if (this.mapId == 154) {
                        if (player.billEgg != null) {
                            player.billEgg.sendBillEgg();
                            if (player.billEgg.getSecondDone() != 0) {
                                this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                        "Hủy bỏ\ntrứng",
                                        "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                            } else {
                                this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở",
                                        "Hủy bỏ\ntrứng", "Đóng");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                        + Util.numberToMoney(
                                                                (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                        + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                        + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                        + Util.numberToMoney(
                                                                (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                        + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                        + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
                                                "Đệ Bill\nTrái Đất", "Đệ Bill\nNamếc", "Đệ Bill\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                    + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n"
                                                    + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                    + " vàng",
                                            "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                        + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(
                                                        OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng",
                                                "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                // giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                            - player.inventory.gold))
                                                    + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                        - player.inventory.gold))
                                                + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng",
                                    "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng",
                                "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                        "Đường đến với ngọc rồng sao đen đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1
                                                ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " "
                                                : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                            "Ngươi có một vài phần thưởng ngọc "
                                                    + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception e) {
                            System.err.print("\nError at 213\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                // if (!player.getSession().actived) {
                                // Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng
                                // chức năng này");
                                //
                                // } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ",
                                "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream()
                                        .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP", "Từ Chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
            }

            // if (player.inventory.gold < 500000000) {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo
            // ít thôi con", "Đóng");
            // return;
            // }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đói bụng quá.. ngươi mang cho ta 99 phần đồ ăn,\nta sẽ cho một món đồ Hủy Diệt.\n Nếu tâm trạng ta vui ngươi có thể nhận được trang bị\ntăng đến 15%!",
                            "OK", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        boolean dotl = PlayerService.gI().isFullSetThanLinh(player);
                                        Item pudding = InventoryServiceNew.gI().findItemBag(player, 663);
                                        Item xucxich = InventoryServiceNew.gI().findItemBag(player, 664);
                                        Item kemdau = InventoryServiceNew.gI().findItemBag(player, 665);
                                        Item mily = InventoryServiceNew.gI().findItemBag(player, 666);
                                        Item sushi = InventoryServiceNew.gI().findItemBag(player, 667);
                                        if (!dotl) {
                                            this.npcChat(player, "Bạn Cần Mặc Full Set Thần linh Để Đổi");
                                        } else {
                                            if (pudding != null && pudding.quantity >= 99
                                                    || xucxich != null && xucxich.quantity >= 99
                                                    || kemdau != null && kemdau.quantity >= 99
                                                    || mily != null && mily.quantity >= 99
                                                    || sushi != null && sushi.quantity >= 99) {
                                                ShopServiceNew.gI().opendShop(player, "HUY_DIET", true);
                                                break;
                                            } else {
                                                this.npcChat(player,
                                                        "Mặc full sét thần linh, x99 thức ăn đến cho ta !!");
                                                break;
                                            }
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Công Thức", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ",
                                        "Cửa hàng", "Chế tạo", "Đóng");
                                break;
                            case 1:
                                this.createOtherMenu(player, 111,
                                        "ahhaha ta vừa có 1 số mảnh công thức con có mua không\n chỉ với 1 thỏi vàng ?",
                                        "Công Thức\nTrái Đất", "Công Thức\nNamec", "Công Thức\nXayda", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 111) {
                        switch (select) {
                            case 0:
                                Item xudaiviet = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                if (xudaiviet != null) {
                                    if (xudaiviet.quantity >= 10
                                            && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) 1084);
                                        InventoryServiceNew.gI().addItemBag(player, yardart);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet, 10);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được công thức");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 1 thỏi vàng ");
                                    }
                                }
                                break;
                            case 1:
                                Item xudaiviet1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
                                if (xudaiviet1 != null) {
                                    if (xudaiviet1.quantity >= 10
                                            && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) 1085);
                                        InventoryServiceNew.gI().addItemBag(player, yardart);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet1, 10);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được công thức");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 1 thỏi vàng");
                                    }
                                }
                                break;
                            case 2:
                                Item xudaiviet2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1335);
                                if (xudaiviet2 != null) {
                                    if (xudaiviet2.quantity >= 10
                                            && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) 1086);
                                        InventoryServiceNew.gI().addItemBag(player, yardart);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, xudaiviet2, 10);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được công thức");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ 1 thỏi vàng");
                                    }
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_DA", false);
                                break;
                            case 1:
                                boolean dotl = PlayerService.gI().isFullSetHuyDiet(player);
                                if (!dotl) {
                                    this.npcChat(player, "Bạn Cần Mặc Full Set Hủy Diệt");
                                } else {
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1320);
                                if (sach != null && sach.quantity >= 9999 && player.inventory.gold >= 10000000
                                        && player.inventory.gem > 99 && player.nPoint.power >= 1000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 9999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 1000000000L) {
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 9999) {
                                    int sosach = 9999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player,
                                            "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 10000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 99) {
                                    Service.getInstance().sendThongBao(player,
                                            "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                // break;
                        }
                    }
                }
            }

        };
    }

    public static Npc ngudan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 178) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 178) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 178, "Ta sẽ giúp ngươi Câu Cá", "Cửa Hàng", "Về Đảo",
                                        "Quà Top", "Đóng");
                                break;
                            case 1:

                        }
                    } else if (player.iDMark.getIndexMenu() == 178) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_CAN", false);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 295);
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "TOP_CAN", false);
                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc thangod(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 5) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi", "Quà Top", "Đi Câu Cá", "Đóng");
                                break;
                            case 1:

                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "SHOP_TOP", false);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 178, -1, 295);
                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 47 || this.mapId == 84) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cậu muốn giúp đỡ tôi à\b tôi sẽ giao cho cậu vài nhiệm vụ của hôm nay.",
                                    "Nhiệm vụ\nhàng ngày", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName()
                                                + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess()
                                                + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                        + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    // System.out.println(".confirmMenu() 1");
                                    // ThanhTichPlayer.SendThanhTich(player);
                                    break;
                                // case 2:
                                //// if (false) {
                                //// Service.gI().sendThongBao(player, "Chức năng hiện tại đang bảo trì");
                                //// return;
                                //// }
                                // OnlineHangNgay.SendThanhTich(player);
                                // break;
                                case 3:
                                    // if (true) {
                                    // Service.gI().sendThongBao(player, "Chức năng hiện tại đang bảo trì");
                                    // return;
                                    // }
                                    // QuaNapHangNgay.SendThanhTich(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Ta là thần mèo Karin"
                                    + "\n|2| Nro green comback", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng",
                                "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "KARIN", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Làm Gì Đó Đi ?",
                                "Tới Khu Vực Thiên Tử", "Đóng");
                    }
                    if (this.mapId == 167) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Đây là khu vực thiên tử , bạn muốn làm gì ?",
                                "Nâng Cấp\nChân Mệnh", "Cửa hàng", "Thông tin chi tiết", "Về Đảo Kame");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 167:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        CombineServiceNew.gI().openTabCombine(player,
                                                CombineServiceNew.NANG_CAP_THIEN_TU);
                                        break;
                                    }
                                    if (select == 1) {
                                        ShopServiceNew.gI().opendShop(player, "SHOP_VTT", false);
                                        break;
                                    }
                                    if (select == 2) {
                                        createOtherMenu(player, ConstNpc.BASE_MENU + 3,
                                                "|2|Hạ gục quái tại map Thiên Tử sẽ có tỉ lệ rơi Ma Thạch\n",
                                                "Đóng");
                                        break;
                                    }
                                    if (select == 4) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, 2, 210);
                                        break;
                                    }
                                    break;
                                case ConstNpc.BASE_MENU + 1:
                                    if (select == 0) {
                                        ChanThienTu.DoiChanThienTu(player, 0);
                                    }
                                    if (select == 1) {
                                        ChanThienTu.DoiChanThienTu(player, 2);
                                    }
                                    break;
                                case ConstNpc.BASE_MENU + 2:
                                    if (select == 0) {
                                        ChanThienTu.DoiChanThienTu(player, 1);
                                    }
                                    if (select == 1) {
                                        ChanThienTu.DoiChanThienTu(player, 3);
                                    }
                                    break;
                            }
                            break;
                        case 19:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        if (player.nPoint.power < 80_000_000_000L) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Sức mạnh cần 80 tỉ để qua map!");
                                        } else {
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            ChangeMapService.gI().changeMap(player, 167, -1, 133, 456);
                                            return;
                                        }
                                        break;
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 80) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 870);
                                }
                            } else if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc KAIDO(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi không có cửa thắng được đa đâu ",
                                "Tới đảo\nKaido", "Từ chối");
                    } else if (this.mapId == 173) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Sợ rồi sao ha ha ha", "Bỏ chạy", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 5) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 173, -1, 870);
                                }
                            } else if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 770);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Đến tây thánh địa", "từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                // đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vào các khung giờ chẵn trong ngày\n"
                                + "Khi luyện tập với Mộc nhân với chế độ bật Cờ sẽ đánh rơi Bí kíp\n"
                                + "Hãy cố găng tập luyện thu thập 9999 bí kíp rồi quay lại gặp ta nhé", "Nhận\nthưởng",
                                "OK");

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (select == 0) {
                            if (biKiep != null) {
                                if (biKiep.quantity >= 9999 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                    yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                    yardart.itemOptions.add(new Item.ItemOption(33, 0));
                                    InventoryServiceNew.gI().addItemBag(player, yardart);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 9999);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                } else if (biKiep.quantity < 9999) {
                                    Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ 9999 bí kíp");
                                }
                            }
                        } else {
                            return;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta đang nắm giữ bí kíp giúp ngươi mạnh lên, Ngươi có muốn thử ?", "Nâng cấp\nVip", "Shop",
                            "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 14) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 1,
                                        "|7|Ngươi muốn thức tỉnh cải trang hay nâng cấp Lite Girl ?\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm đá little Girl",
                                        "Nâng cấp lite girl",
                                        "Thức tỉnh Luffy",
                                        "Từ chối");
                                break;
                            case 1: // Shop
                                ShopServiceNew.gI().opendShop(player, "KHI", false);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_KHI);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_LUFFY);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_KHI) {
                        if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_KHI && select == 0) {
                            CombineServiceNew.gI().startCombine(player, 0);
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_LUFFY) {
                        if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_LUFFY && select == 0) {
                            CombineServiceNew.gI().startCombine(player, 2);
                        }
                    }
                }
            }
        };
    }

    public static Npc chomeoan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                        "Ai đó hãy giúp tôi với ... Làm ơn huhu",
                        "Hãy giúp tôi tìm lại bé mèo huhu",
                        "Thí chủ, Xin dừng bước",
                        "Hãy giúp tôi tìm lại bé mèo ..."
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }

            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta đang đánh mất một bé mèo đen đuôi vàng, hãy giúp ta tìm lại nó...", "Cho mèo ăn",
                            "Shop", "Không quan tâm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cho mèo ăn để mèo có chỉ số random từ 5 -> 30% Chỉ số \b|2| Mèo có chỉ số vĩnh viễn sẽ không thể cho ăn !",
                                            "Cho Mèo ăn",
                                            "Để mèo đói");
                                    break;
                                case 1: // shop
                                    ShopServiceNew.gI().opendShop(player, "MEOMEO", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_MEO);
                                    break;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_MEO) {
                            if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_MEO && select == 0) {
                                CombineServiceNew.gI().startCombine(player, 0);
                                // }
                                // break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[] {};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                "Đại Hội\nVõ Thuật\nLần thứ\n23", "Từ chối");
                    } else if (this.mapId == 129) {
                        // int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[] { "Thi đấu\n" + 200 + " Hồng ngọc", "Về\nĐại Hội\nVõ Thuật" };
                        } else {
                            menuselect = new String[] { "Thi đấu\n" + 200 + " Hồng ngọc",
                                    "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật" };
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        // int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.ruby >= 200) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.ruby -= 200;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(200 - player.inventory.ruby) + " Hồng ngọc");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gem >= 200) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gem -= (200);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 200;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(200 - player.inventory.gem) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài Unknow");
                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame",
                                "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1: //
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Goku SSJ3\n với chỉ số random từ 20 > 30% \n ",
                                            "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2: //
                                    Service.gI().showListTop(player, Manager.topRUBY);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1227)); // 49
                                        item.itemOptions.add(new Item.ItemOption(49, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 20)));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
                                        //
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Sắp trung thu rồi .. ngươi mang cho ta item bất kì up tại ngũ hành sơn ,\nta sẽ cho một vật phẩm cực vip.\n Nếu tâm trạng ta vui ngươi có thể mua vật phẩm\nfree!",
                            "OK", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 0:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        if (!player.getSession().actived) {
                                            if (player.getSession().player.nPoint.power >= 20000000000L) {
                                                ShopServiceNew.gI().opendShop(player, "TAYDUKI", true);
                                                break;
                                            } else {
                                                this.npcChat(player, "Ngươi Chưa Đủ 20 tỉ sm !!");

                                            }
                                            break;
                                        }
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc hungVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào con, ta là Hùng Vương.\nGiặc Ân đang lăm le bờ cõi. Nước non đang cần người tài.\nSơn Tinh Thủy Tinh cũng mang sính lễ đến để kén rể.\nCon có Dưa Hấu không, ta sẽ đổi quà cho con!\n Con có thể kiếm dưa hấu từ những nơi mà Sơn Tinh và Thủy tinh xuất hiện!",
                            "Đổi 99 Dưa Hấu\nlấy Búa Sơn Tinh", "Đổi 99 Dưa Hấu\nlấy Bút Thủy Tinh", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: { // 99 Dưa Hấu -> Búa Sơn Tinh (1772)
                                Item duaHau = InventoryServiceNew.gI().findItemBag(player, 569);
                                if (duaHau == null || duaHau.quantity < 99) {
                                    this.npcChat(player, "Con không đủ 99 Dưa Hấu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của con không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, duaHau, 99);
                                    Item itemDrop = ItemService.gI().createNewItem((short) 1772);
                                    itemDrop.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 20))); // HP %
                                    itemDrop.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 20))); // KI %
                                    itemDrop.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 15)));  // SD %
                                    itemDrop.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 5)));   // Crit %
                                    InventoryServiceNew.gI().addItemBag(player, itemDrop);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Con nhận được 1 Búa Sơn Tinh");
                                }
                                break;
                            }
                            case 1: { // 99 Dưa Hấu -> Bút Thủy Tinh (1773)
                                Item duaHau = InventoryServiceNew.gI().findItemBag(player, 569);
                                if (duaHau == null || duaHau.quantity < 99) {
                                    this.npcChat(player, "Con không đủ 99 Dưa Hấu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của con không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, duaHau, 99);
                                    Item itemDrop = ItemService.gI().createNewItem((short) 1773);
                                    itemDrop.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 20))); // HP %
                                    itemDrop.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 20))); // KI %
                                    itemDrop.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 15)));  // SD %
                                    itemDrop.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 5)));   // Crit %
                                    InventoryServiceNew.gI().addItemBag(player, itemDrop);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Con nhận được 1 Bút Thủy Tinh");
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc billbingo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào con, ta là Bill.\nLễ Halloween sắp đến rồi, hãy mang Bí Ngô đến đây ta sẽ đổi lấy quà cho con nhé!",
                            "Đổi 20 Bí Ngô\nlấy Capsule\nCải Trang", "Đổi 99 Bí Ngô\nlấy Capsule\nVàng", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: { // 20 Bí Ngô -> Capsule Cải Trang (818)
                                Item biNgo = InventoryServiceNew.gI().findItemBag(player, 585);
                                if (biNgo == null || biNgo.quantity < 20) {
                                    this.npcChat(player, "Con không đủ 20 Bí Ngô");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của con không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biNgo, 20);
                                    Item capsule = ItemService.gI().createNewItem((short) 818);
                                    InventoryServiceNew.gI().addItemBag(player, capsule);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Con nhận được 1 Capsule Cải Trang");
                                }
                                break;
                            }
                            case 1: { // 99 Bí Ngô -> Capsule Vàng (574)
                                Item biNgo = InventoryServiceNew.gI().findItemBag(player, 585);
                                if (biNgo == null || biNgo.quantity < 99) {
                                    this.npcChat(player, "Con không đủ 99 Bí Ngô");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của con không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biNgo, 99);
                                    Item capsule = ItemService.gI().createNewItem((short) 574);
                                    InventoryServiceNew.gI().addItemBag(player, capsule);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Con nhận được 1 Capsule Vàng");
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi Đang Đua Top Sao?, Ta Có Vài Món Quà Cho Ngươi Này",
                            "Xem Ngay", "Từ Chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", false);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.ADMIN1:
                    return ADMIN(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA1:
                    return bunma(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THOREN:
                    return thoren(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHO_MEO_AN:
                    return chomeoan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NGUDAN:
                    return ngudan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return trongtai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL_BI_NGO:
                    return billbingo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_GOD:
                    return thangod(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MEO_THAN_TAI:
                    return meothantai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KAIDO:
                    return KAIDO(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HOANGUC:
                    return hoanguc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GIA_NOEL:
                    return onggianoel(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HUNG_VUONG:
                    return hungVuong(mapId, status, cx, cy, tempId, avatar);
                // case ConstNpc.GOHAN_NHAT_NGUYET:
                // return gohannn(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            System.err.print("\nError at 216\n");
            e.printStackTrace();
            return null;
        }
    }

    // girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        if (player.getSession().actived) {
                            if (Maintenance.isRuning) {
                                break;
                            }
                            PVPService.gI().sendInvitePVP(player, (byte) select);
                            break;
                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, 1105);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player, 1105);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player, 1105);
                        }
                        break;
                    case 1980:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, 19800);
                        } else if (select == 1) {
                            IntrinsicService.gI().sattd(player, 19801);
                        } else if (select == 2) {
                            IntrinsicService.gI().sattd(player, 19802);
                        }
                        break;

                    case 1985:
                    case 1986:
                    case 1987:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player, player.iDMark.getIndexMenu());
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player, player.iDMark.getIndexMenu());
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player, player.iDMark.getIndexMenu());
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                            ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 216\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 217\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            System.err.print("\nError at 218\n");
                            e.printStackTrace();
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case 1278:
                        Input.gI().createFormItemC2(player, select);
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        ShopKyGuiService.gI().StartupItemToTop(player);
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        // PetService.gI().changeUubPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    Maintenance.gI().start(15);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                BossManager.gI().showListBoss(player);
                                break;
                            case 5:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 6:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 7:
                                Input.gI().createFormSenditem2(player);
                                break;
                            case 8:
                                Input.gI().createFormGiftCode(player);
                                break;
                        }
                        break;
                    case 19850:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 219\n");
                                    e.printStackTrace();

                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 220\n");

                                    e.printStackTrace();

                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 221\n");
                                    e.printStackTrace();

                                }
                                break;
                        }
                        break;
                    case 19851:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().setgodki19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 222\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 223\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon19(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 224\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19852:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().setgodgalick16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 225\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 226\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp16(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 227\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19860:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14taiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 228\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set14genki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 229\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set14kamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 230\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19861:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14godki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 231\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set14goddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 232\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set14summon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 233\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19862:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set14godgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 234\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey14(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 235\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp14(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 236\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19870:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set1taiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 237\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set1genki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 238\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set1kamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 239\n");
                                    e.printStackTrace();

                                }
                                break;
                        }
                        break;
                    case 19871:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set2godki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 240\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().set1goddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 241\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().set1summon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 242\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case 19872:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().set1godgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 243\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey1(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 244\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp1(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 245\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {

                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 246\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 247\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 248\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 249\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 250\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 251\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 252\n");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                    System.err.print("\nError at 253\n");
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case ConstNpc.XU_HRZ:
                        try {
                            if (select == 0) {
                                NapVangService.ChonGiaTien(20, player);
                            } else if (select == 1) {
                                NapVangService.ChonGiaTien(50, player);
                            } else if (select == 2) {
                                NapVangService.ChonGiaTien(100, player);
                            } else if (select == 3) {
                                NapVangService.ChonGiaTien(500, player);

                            } else {

                                break;
                            }
                            break;
                        } catch (Exception e) {
                            System.err.print("\nError at 254\n");
                            e.printStackTrace();
                            break;

                        }

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                        Service.gI().sendThongBao(player,
                                                "Đại thiên sứ đã được chuyển tức thời đến vị trí: " + p.name + " !");
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(p, player.zone, player.location.x,
                                                player.location.y);

                                        Service.gI().sendThongBao(player, "Cư dân " + p.name
                                                + " đã được Đại thiên sứ dịch chuyển tức thời đến đây!");
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[] { "Hủy diệt", "Tha" };
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Đại thiên sứ có muốn hủy diệt cư dân: " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player,
                                            "Đại thiên sứ đã Logout cư dân " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    // case ConstNpc.CONFIRM_TELE_NAMEC:
                    // if (select == 0) {
                    // NgocRongNamecService.gI().teleportToNrNamec(player);
                    // player.inventory.subGemAndRubyAndTien(50);
                    // Service.gI().sendMoney(player);
                    // }
                    // break;
                    case ConstNpc.RUONG_GO:
                        if (select == 0) { // Người chơi chọn "OK"
                            player.currentMessageIndex++;
                            UseItem.gI().showNextReward(player);
                        }
                        break;
                }
            }
        };
    }

}
