package com.girlkun.services.func;

import com.girlkun.database.GirlkunDB;
import com.girlkun.consts.ConstNpc;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.BDKB.BanDoKhoBauService;
import com.girlkun.models.map.Zone;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.npc.NpcManager;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.network.session.ISession;
import com.girlkun.server.Client;
import com.girlkun.services.Service;
import com.girlkun.services.GiftService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.NpcService;
import com.girlkun.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int CHOOSE_LEVEL_GAS = 555;
    public static final int GIVE_IT = 507;
    public static final int QUY_DOI_COIN = 508;
    public static final int QUY_DOI_NGOC_XANH = 509;
    public static final int TAI = 510;
    public static final int XIU = 511;
    public static final int QUY_DOI_HONG_NGOC = 509;
    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    public static final int UseGold = 3;
    public static final int changeCN = 4;
    public static final int changeBK = 5;
    public static final int changeBH = 6;
    public static final int changeGX = 7;
    public static final int changeAD = 8;
    private static Input intance;
    public static final int XIU_taixiu = 5164;
    public static final int TAI_taixiu = 5165;
    
    public static final int SEND_ITEM = 6000;
    public static final int SEND_ITEM_OP = 6001;
    public static final int SEND_ITEM_SKH = 6002;
    public static final int MailBox = 65675;
    private Input() {

    }
    public void createFromMailBox(Player pl) {
        createForm(pl, MailBox, "Hộp thư gửi đến người chơi",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", ANY),
                new SubInput("Chuỗi option", ANY),
                new SubInput("Số lượng", NUMERIC));
    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case MailBox:
                    if (player.isAdmin()) {
//                        int idItemBuff = Integer.parseInt(text[1]);
                        String itemIds = text[1];
                        String option = text[2];
                        int slItemBuff = Integer.parseInt(text[3]);
                        if (slItemBuff > 20000) {
                            Service.gI().sendThongBaoOK(player, "Buff vượt số lượng giới hạn vui lòng để tối đa sl 20000");
                            return;
                        }
                        String plName = text[0].trim();
                        if (plName.equals("all")) {
                            new Thread(() -> {
                                List<Player> allPlayer = GodGK.getAllPlayer();
                                for (Player pBuffItem : allPlayer) {
                                    if (pBuffItem != null) {
                                        try {
                                            String[] itemIdsArray = itemIds.split(",");
                                            for (String itemId : itemIdsArray) {
                                                int idItemBuff = Integer.parseInt(itemId);
                                                Item itembuff = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);

                                                if (option != null) {
                                                    String[] Option = option.split(",");
                                                    if (Option.length > 0) {
                                                        for (int i = 0; i < Option.length; i++) {
                                                            String[] optItem = Option[i].split("-");
                                                            int optID = Integer.parseInt(optItem[0]);
                                                            int param = Integer.parseInt(optItem[1]);
                                                            itembuff.itemOptions.add(new Item.ItemOption(optID, param));
                                                        }
                                                    }
                                                }
                                                pBuffItem.inventory.itemsMailBox.add(itembuff);
                                            }

                                            if (GodGK.updateMailBox(pBuffItem)) {
                                                Service.getInstance().sendThongBao(player, "Bạn vừa gửi mail thành công cho " + pBuffItem.name);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Service.getInstance().sendThongBao(player, "Đã có lỗi xảy ra vui lòng thực hiện lại");
                                        }

                                    } else {
                                        Service.getInstance().sendThongBao(player, "Player không tồn tại");
                                    }
                                }
                            }).start();
                        } else {
                            Player pBuffItem = GodGK.loadPlayerByName(text[0].trim());
                            if (pBuffItem != null) {
                                try {
                                    String[] itemIdsArray = itemIds.split(",");
                                    for (String itemId : itemIdsArray) {
                                        int idItemBuff = Integer.parseInt(itemId);
                                        Item itembuff = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);

                                        if (option != null) {
                                            String[] Option = option.split(",");
                                            if (Option.length > 0) {
                                                for (int i = 0; i < Option.length; i++) {
                                                    String[] optItem = Option[i].split("-");
                                                    int optID = Integer.parseInt(optItem[0]);
                                                    int param = Integer.parseInt(optItem[1]);
                                                    itembuff.itemOptions.add(new Item.ItemOption(optID, param));
                                                }
                                            }
                                        }
                                        pBuffItem.inventory.itemsMailBox.add(itembuff);
                                    }
                                    if (GodGK.updateMailBox(pBuffItem)) {
                                        Service.getInstance().sendThongBao(player, "Bạn vừa gửi mail thành công cho " + pBuffItem.name);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Service.getInstance().sendThongBao(player, "Đã có lỗi xảy ra vui lòng thực hiện lại");
                                }

                            } else {
                                Service.getInstance().sendThongBao(player, "Player không tồn tại");
                            }
                        }
                        break;

                    }
                    break;
                case SEND_ITEM:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem(((short) id));
                        item.quantity = q;
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.gI().sendThongBao(Client.gI().getPlayer(name), "Nhận " + item.template.name + " từ " + player.name);

                    } else {
                        Service.gI().sendThongBao(player, "Không online");
                    }
                    break;
                case GIFT_CODE:
                    GiftService.gI().giftCode(player, text[0]);
                    break;
                case SEND_ITEM_OP:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        String[] options = text[2].split(" ");
                        int slItemBuff = Integer.parseInt(text[3]);
                        Player admin = player;
                        Player target = Client.gI().getPlayer(text[0]);
                        if (target != null) {
                            String txtBuff = "Bạn vừa nhận được: " + target.name + ", hãy kiểm tra hành trang\n";
                            Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                            for (String option : options) {
                                String[] optionParts = option.split("-");
                                int idOptionBuff = Integer.parseInt(optionParts[0]);
                                int slOptionBuff = Integer.parseInt(optionParts[1]);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                            }
                            itemBuffTemplate.quantity = slItemBuff;
                            InventoryServiceNew.gI().addItemBag(target, itemBuffTemplate);
                            InventoryServiceNew.gI().sendItemBags(target);
                            txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\n";
                            Service.gI().sendThongBao(target, txtBuff);
                        } else {
                            Service.gI().sendThongBao(admin, "Không tìm thấy cư dân hoặc cư dân.");
                        }
                    }
                    break;
                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 1139, "Quyền Năng Thiên Sứ?",
                                new String[]{"Dịch chuyển\nđến\n" + pl.name, "Triệu hồi\n" + pl.name + "\nđến đây", "Đổi tên\n" + pl.name + "", "Khóa\n" + pl.name + "", "Đăng xuất\n" + pl.name + ""},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Cư dân " + pl.name + " hiện tại không online!!");
                    }
                    break;
//                case UseGold:
//                    int Gold = Integer.parseInt(text[0]);
//                    Item thoivangchange = null;
//                    for (Item item : player.inventory.itemsBag) {
//                        if (item.isNotNullItem() && item.template.id == 1678) {
//                            thoivangchange = item;
//                            break;
//                        }
//                    }
//                    if (thoivangchange.quantity >= Gold) {
//                        long goldsum = (long) (500000000L * (long) Gold);
//                        if (player.inventory.gold + goldsum > Inventory.LIMIT_GOLD) {
//                            Service.gI().sendThongBao(player, "Số vàng quy đổi vượt quá giới hạn 100 tỉ");
//                        } else {
//                            player.inventory.gold += (long) (500000000L * (long) Gold);
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivangchange, Gold);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendMoney(player);
//                            Service.gI().sendThongBao(player, "Đổi Thành Công");
//                        }
//                    } else {
//                        Service.gI().sendThongBao(player, "Số lượng không đủ");
//                    }
//                    break;
case UseGold:
String goldInput = text[0];
// Xóa các dấu trừ nếu có
goldInput = goldInput.replaceAll("-", "");
int gold;

try {
    gold = Integer.parseInt(goldInput);
} catch (NumberFormatException e) {
    Service.gI().sendThongBao(player, "Bug cái con cặc, bố m ban acc tự động , cố lên kakakaak !");
    return; // Thoát khỏi hàm khi không thể chuyển đổi thành số nguyên
}

Item thoivangchange = null;
for (Item item : player.inventory.itemsBag) {
    if (item.isNotNullItem() && item.template.id == 457) {
        thoivangchange = item;
        break;
    }
}


    if (thoivangchange.quantity >= gold) {
        long goldsum = (long) (500000000L * (long) gold);
        if (player.inventory.gold + goldsum > Inventory.LIMIT_GOLD) {
            Service.gI().sendThongBao(player, "Số vàng quy đổi vượt quá giới hạn 100 tỉ");
        } else {
            player.inventory.gold += (long) (500000000L * (long) gold);
            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivangchange, gold);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            Service.gI().sendThongBao(player, "Đổi Thành Công");
        }
    } else {
        Service.gI().sendThongBao(player, "Số lượng không đủ");
    }
    break;                    
                case QUY_DOI_COIN:
                    int ratioGold = 1; // tỉ lệ đổi tv
                    int coinGold = 1; // là cái loz
                    int goldTrade = Integer.parseInt(text[0]);
                    if(goldTrade<=0 || goldTrade>= 50000000)
                    {
                       Service.gI().sendThongBao(player, "giới hạn");
                    }
                    else if(player.getSession().vnd >= goldTrade*coinGold){
                        PlayerDAO.subvnd(player, goldTrade*coinGold);
                        Item thoiVang =ItemService.gI().createNewItem((short)861,goldTrade*1);// x3
                        InventoryServiceNew.gI().addItemBag(player,thoiVang);
                       InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " +goldTrade*ratioGold
                         +" " + thoiVang.template.name);
                    }else{
                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
                                + " đổi " + goldTrade + " Hồng Ngọc " + " " + "bạn cần thêm" +(player.getSession().vnd-goldTrade));
                    }
                    break;
                    case QUY_DOI_HONG_NGOC:
                    int ratioGem = 2; // tỉ lệ đổi tv
                    int coinGem = 1000; // là cái loz
                    int gemTrade = Integer.parseInt(text[0]);
                    if(gemTrade<=0 || gemTrade>= 50000000)
                    {
                       Service.gI().sendThongBao(player, "giới hạn");
                    }
                    else if(player.getSession().vnd >= gemTrade*coinGem){
                        PlayerDAO.subvnd(player, gemTrade*coinGem);
                        Item thoiVang =ItemService.gI().createNewItem((short)457,gemTrade*2);// x4
                        InventoryServiceNew.gI().addItemBag(player,thoiVang);
                       InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " +gemTrade*ratioGem
                         +" " + thoiVang.template.name);
                    }else{
                        Service.gI().sendThongBao(player, "Số tiền của bạn là " +player.getSession().vnd+ " không đủ để quy "
                                + " đổi " + gemTrade + " Thỏi Vàng" + " " + "bạn cần thêm" +(player.getSession().vnd-gemTrade));
                    }
                    break;
                        case SEND_ITEM_SKH:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionSKH = Integer.parseInt(text[2]);
                        int idOptionBuff = Integer.parseInt(text[3]);
                        int slOptionBuff = Integer.parseInt(text[4]);
                        int slItemBuff = Integer.parseInt(text[5]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += slItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionSKH, 0));
                                if (idOptionSKH == 127) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(139, 0));
                                } else if (idOptionSKH == 128) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(140, 0));
                                } else if (idOptionSKH == 129) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(141, 0));
                                } else if (idOptionSKH == 130) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(142, 0));
                                } else if (idOptionSKH == 131) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(143, 0));
                                } else if (idOptionSKH == 132) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(144, 0));
                                } else if (idOptionSKH == 133) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(136, 0));
                                } else if (idOptionSKH == 134) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(137, 0));
                                } else if (idOptionSKH == 135) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(138, 0));
                                }
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(30, 0));
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case CHOOSE_LEVEL_GAS:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 100) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.MR_POPO, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCPET_GO_TO_GAS,
                                    "Con có chắc chắn muốn tới Khí gas huỷ diệt cấp độ " + level + "?",
                                    new String[]{"Đồng ý, Let's Go", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case CHOOSE_LEVEL_BDKB:
                    int levele = Integer.parseInt(text[0]);
                    if (levele >= 1 && levele <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + levele + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, levele);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case TAI:
                    if(player != null){
                    int sohntai = Integer.valueOf(text[0]);
                    if (sohntai > 500000){
                        Service.getInstance().sendThongBao(player, "Tối đa 500000 VNĐ!!");
                        return;
                    }
                    if (sohntai <= 0){
                        Service.getInstance().sendThongBao(player, "Xanh Chín đi, Đừng bug bẩn !!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1){
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
//                    Item tv1 = null;
//                    for (Item item : player.inventory.itemsBag) {
//                        if (item.isNotNullItem() && item.template.id == 457) {
//                            tv1 = item;
//                            break;
//                        }
//                    }
                    try {
                        if (player.inventory.ruby >= sohntai) {
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, sotvtai);
                            player.inventory.ruby -= sohntai;
                            Service.gI().sendMoney(player);
                            int TimeSeconds = 10;
                            Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết kết quả");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x+y+z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " +tong+ "\nBạn đã cược : "
                                    + sohntai + " VNĐ vào Tài"  + "\nKết quả : Xỉu" + "\nBạn Thua.");
                                    return;
                                }
                             } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sohntai + " VNĐ vào Xỉu" + "\nKết quả : Tam hoa" + "\nBạn Thua.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                
                                if (player != null) {
//                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
//                                    tvthang.quantity = (int) Math.round(sotvtai * 1.8);
//                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    player.inventory.ruby += sohntai*1.8;
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z+ "\nTổng là : " + tong + "\nBạn đã cược : " + sohntai +
                                    " VNĐ vào Tài"+ "\nKết quả : Tài"+ "\n\nBạn dành chiến thắng !");
                                    return;
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng Ngọc để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }
                    }
                case XIU:
                    if (player != null){
                    int sohnxiu = Integer.valueOf(text[0]);
                    if (sohnxiu > 500000){
                        Service.getInstance().sendThongBao(player, "Tối đa 500.000 VNĐ!!");
                        return;
                    }
                    if (sohnxiu <= 0){
                        Service.getInstance().sendThongBao(player, "Xanh Chín đi, Đừng Bug bạn ơi !!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1){
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
//                    Item tv2 = null;
//                    for (Item item : player.inventory.itemsBag) {
//                        if (item.isNotNullItem() && item.template.id == 457) {
//                            tv2 = item;
//                            break;
//                        }
//                    }
                    try {
                        if (player.inventory.ruby >= sohnxiu) {
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                            player.inventory.ruby -= sohnxiu;
                            Service.gI().sendMoney(player);
                            int TimeSeconds = 10;
                            Service.getInstance().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x+y+z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
//                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
//                                    tvthang.quantity = (int) Math.round(sotvxiu * 1.8);
//                                    InventoryServiceNew.gI().addItemBag(player, tvthang);
                                    player.inventory.ruby += sohnxiu*2.3;
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z+ "\nTổng là : " + tong + "\nBạn đã cược : " + sohnxiu +
                                    " VNĐ vào Xỉu"+ "\nKết quả : Xỉu"+ "\n\nBạn dành chiến thắng");
                                    return;
                                }
                             } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sohnxiu + " VNĐ vào Xỉu" + "\nKết quả : Tam hoa" + "\nBạn thua.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.getInstance().sendThongBaoOK(player, "Kết quả"+ "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " +tong+ "\nBạn đã cược : "
                                    + sohnxiu + " Hồng Ngọc vào Xỉu"  + "\nKết quả : Tài" + "\nBạn đã thua.");
                                    return;
                                }
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ tiền để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện.");
                    }}
                 case TAI_taixiu:
                    int sotvxiu1 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu1 >= 20000 && sotvxiu1 <= 1000000000) {
                            if (player.inventory.ruby >= sotvxiu1) {
                                player.inventory.ruby -= sotvxiu1;
                                player.goldTai += sotvxiu1;
                                TaiXiu.gI().goldTai += sotvxiu1;
                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu1) + " Hồng Ngọc vào TÀI");
                                TaiXiu.gI().addPlayerTai(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                PlayerDAO.updatePlayer(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng Ngọc để chơi.");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Cược ít nhất 20.000 - nhiều nhất 1.000.000.000 VNĐ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Không Thể Thực Hiện.");
                    }
                    break;
                     case XIU_taixiu:
                    int sotvxiu2 = Integer.valueOf(text[0]);
                    try {
                        if (sotvxiu2 >= 20000 && sotvxiu2 <= 1000000000) {
                            if (player.inventory.ruby >= sotvxiu2) {
                                player.inventory.ruby -= sotvxiu2;
                                player.goldXiu += sotvxiu2;
                                TaiXiu.gI().goldXiu += sotvxiu2;
                                Service.gI().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu2) + " Hồng Ngọc vào XỈU");
                                TaiXiu.gI().addPlayerXiu(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                PlayerDAO.updatePlayer(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ Hồng Ngọc để chơi.");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Cược ít nhất 20.000 - nhiều nhất 1.000.000.000 Hồng Ngọc ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                        System.out.println("Lỗi mục tài xỉu ! ");
                    }
                    break;

                case changeCN:
                case changeBK:
                case changeBH:
                case changeGX:
                case changeAD:
                    int SoLuong = Integer.parseInt(text[0]);
                    UseItem.gI().SendItemCap2(player, player.iDMark.getTypeInput(), SoLuong);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Gift code ", new SubInput("Gift-code", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void TAI(Player pl) {
        createForm(pl, TAI, "Chọn số thỏi vàng đặt Xỉu", new SubInput("Số thỏi vàng", ANY));//????
    }

    public void XIU(Player pl) {
        createForm(pl, XIU, "Chọn số thỏi vàng đặt Tài", new SubInput("Số thỏi vàng", ANY));
    }
    public void TAI_taixiu(Player pl) {
        createForm(pl, TAI_taixiu, "Chọn số Hồng Ngọc đặt Tài", new SubInput("Số Hồng Ngọc cược", ANY));//????
    }

    public void XIU_taixiu(Player pl) {
        createForm(pl, XIU_taixiu, "Chọn số Hồng Ngọc đặt Xỉu", new SubInput("Số Hồng Ngọc cược", ANY));//????
    }
    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }
    public void createFormSenditem(Player pl) {
        createForm(pl, SEND_ITEM, "SEND ITEM",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID item", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "Quyền năng V",
                new SubInput("Name", ANY),
                new SubInput("ID Item", NUMERIC),
                new SubInput("ID Option", ANY),
                new SubInput("Quantity", NUMERIC));
    }
    public void createFormSenditem2(Player pl) {
        createForm(pl, SEND_ITEM_SKH, "Buff SKH Option V2",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option SKH 127 > 135", NUMERIC),
                new SubInput("ID Option Bonus", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormQDTV(Player pl) {
      
        createForm(pl, QUY_DOI_COIN, "Quy đổi Hồng Ngọc tỉ lệ 1-1"
                + " Chỉ x2 khi lễ, tết "
                + "\n50.000 coin = 50.000 Hồng ngọc "
                + "\nNạp tiền Tại: nrodaiviet.online "
                + "\nĐăng Nhập và Chọn Nạp Coin "
                + "\nLưu Ý : Nạp đúng nội dung nhé ! Nạp Sai là Mất ^•^ ", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }
    
    public void createFormQDHN(Player pl) {
     
        createForm(pl, QUY_DOI_HONG_NGOC, "Quy đổi Thỏi Vàng"
                + "\nNhập 10 Có nghĩa là  10.000đ"
                + "\nTỉ Lệ Quy Đổi 10.000đ = 20 Thỏi Vàng"
                + "\nNạp tiền Tại: nrodaiviet.online "
                + "\nĐăng Nhập và Chọn Nạp Coin "
                + "\nLưu Ý : Nạp đúng nội dung nhé ! Nạp Sai là Mất ^•^ ", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelGas(Player pl) {
        createForm(pl, CHOOSE_LEVEL_GAS, "Chọn cấp độ", new SubInput("Cấp độ (1-100)", NUMERIC));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormUseGold(Player pl) {
        createForm(pl, UseGold, "Nhập số lượng cần dùng", new SubInput("1 thỏi vàng dùng sẽ được 500tr vàng", NUMERIC));
    }
//        

    public void createFormItemC2(Player pl, int select) {
        if (select == 0) {
            createForm(pl, changeCN, "Nhập số lượng cần dùng", new SubInput("Dùng để nhận Cuồng Nộ cấp 2", NUMERIC));
        }
        if (select == 1) {
            createForm(pl, changeBK, "Nhập số lượng cần dùng", new SubInput("Dùng để nhận Bổ khí cấp 2", NUMERIC));
        }
        if (select == 2) {
            createForm(pl, changeBH, "Nhập số lượng cần dùng", new SubInput("Dùng để nhận Bổ huyết cấp 2", NUMERIC));
        }
        if (select == 3) {
            createForm(pl, changeGX, "Nhập số lượng cần dùng", new SubInput("Dùng để nhận Giáp xên bọ hung cấp 2", NUMERIC));
        }
        if (select == 4) {
            createForm(pl, changeAD, "Nhập số lượng cần dùng", new SubInput("Dùng để nhận Ẩn Danh cấp 2", NUMERIC));
        }

    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

}
