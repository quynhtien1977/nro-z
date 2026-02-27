/*
 * Dev By Duy
 */
package com.arriety.kygui;

import com.girlkun.consts.ConstNpc;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.npc.NpcFactory;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.NpcService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Stole By MITCHIKEN ZALO 0358689793
 */
public class ShopKyGuiService {

    private static ShopKyGuiService instance;

    public static ShopKyGuiService gI() {
        if (instance == null) {
            instance = new ShopKyGuiService();
        }
        return instance;
    }
     private static long LastTimeUpdate;
    public static void update()
    {
        if(System.currentTimeMillis() - LastTimeUpdate >= 60000)
        {
            List<ItemKyGui> list = new ArrayList<>(ShopKyGuiManager.gI().listItem);
            for(ItemKyGui item : list)
            {
                if(System.currentTimeMillis() - item.createTime >= 60000*60*24*2 && item.isBuy == 0)
                {
                    item.isBuy = 2;
                }
            }
            LastTimeUpdate = System.currentTimeMillis();
        }
    }
    private List<ItemKyGui> getItemKyGui2(Player pl, byte tab, int max) {
        List<ItemKyGui> z = new ArrayList<>(getItemKyGui2(pl, tab));
        int x = max * 20;
        x = z.size() < x ? z.size() : x;
        return z.subList(x - 20 < 0 ? 0 : x - 20, x);
    }

    private List<ItemKyGui> getItemKyGui2(Player pl, byte tab) {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        List<ItemKyGui> listSort2 = new ArrayList<>();
        List<ItemKyGui> listSort3 = new ArrayList<>();
        List<ItemKyGui> listSort4 = new ArrayList<>();
        List<ItemKyGui> listItem = new ArrayList<>( ShopKyGuiManager.gI().listItem);
       listItem.stream().filter((it) -> (it != null && it.tab == tab && it.isBuy == 0)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.createTime, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        listSort2 = new ArrayList<>(listSort);
        listSort.clear();
        listSort2.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).limit(9).forEach(i -> listSort.add(i));
        listSort3 = new ArrayList<>(listSort);
        listSort.clear();
        listSort.addAll(listSort3);
        listSort2.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.createTime, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        for (int i = 0; i < listSort.size(); i++) {
            if (!listSort4.contains(listSort.get(i))) {
                listSort4.add(listSort.get(i));
            }
        }
        return listSort4;
    }

    private List<ItemKyGui> getItemKyGui(Player pl, byte tab, byte... max) {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        List<ItemKyGui> listSort2 = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.tab == tab && it.isBuy == 0 && it.player_sell != pl.id)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        if (max.length == 2) {
            if (listSort.size() > max[1]) {
                for (int i = max[0]; i < max[1]; i++) {
                    if (listSort.get(i) != null) {
                        listSort2.add(listSort.get(i));
                    }
                }
            } else {
                for (int i = max[0]; i <= max[0]; i++) {
                    if (listSort.get(i) != null) {
                        listSort2.add(listSort.get(i));
                    }
                }
            }
            return listSort2;
        }
        if (max.length == 1 && listSort.size() > max[0]) {
            for (int i = 0; i < max[0]; i++) {
                if (listSort.get(i) != null) {
                    listSort2.add(listSort.get(i));
                }
            }
            return listSort2;
        }
        return listSort;
    }

    private List<ItemKyGui> getItemKyGui() {
        List<ItemKyGui> its = new ArrayList<>();
        List<ItemKyGui> listSort = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.isBuy == 0)).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter(i -> i != null).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(i -> listSort.add(i));
        return listSort;
    }

    private boolean isKyGui(Item item) {
        for (ItemOption option : item.itemOptions) {
            if (option.optionTemplate.id == 86) {
                return true;
            }
        }
        if (item.template.id == 1280) {
            return true;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return item.template.name.contains("Thần");
            case 27:
                switch (item.template.id) {
                    case 921:
                    case 1155:
                    case 1156:
                    case 568:
                        return item.itemOptions.size() > 1;
                    case 1066:
                    case 1067:
                    case 1068:
                    case 1069:
                    case 1070:
                    case 1280:
                        return true;
                }
                return false;
            case 21:
            case 72:
                return true;
        }

        return false;
    }

    private boolean SubThoiVang(Player pl, int quatity) {
        for (Item item : pl.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 457 && item.quantity >= quatity && !HaveOPNotTrade(item)) {
                com.girlkun.services.InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quatity);
                return true;
            }
        }
        return false;
    }

    private boolean HaveOPNotTrade(Item item) {
        for (Item.ItemOption op : item.itemOptions) {
            if (op != null && op.optionTemplate.id == 30) {
                return true;
            }
        }
        return false;
    }

    public void buyItem(Player pl, int id) {
        if (pl.nPoint.power < 40000000000L) {
            Service.gI().sendThongBao(pl, "Yêu cầu sức mạnh lớn hơn 40 tỷ");
            openShopKyGui(pl);
            return;
        }
//        if (!pl.getSession().actived) {
//            Service.gI().sendThongBao(pl, "Cần phải kích hoạt thành viên mới có thể mua vật phẩm");
//            openShopKyGui(pl);
//            return;
//        }
        ItemKyGui it = getItemBuy(id);
        if (it == null || it.isBuy == 1) {
            Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
            return;
        }
        if (it.player_sell == pl.id) {
            Service.gI().sendThongBao(pl, "Không thể mua vật phẩm bản thân đăng bán");
            openShopKyGui(pl);
            return;
        }
        boolean isBuy = false;
        if (it.goldSell > 0) {
            if (SubThoiVang(pl, it.goldSell)) {
                isBuy = true;
            } else {
                Service.gI().sendThongBao(pl, "Bạn cần đủ thỏi vàng có thể giao dịch mới có thể mua vật phẩm");
                isBuy = false;
            }
        } else if (it.gemSell > 0) {
            if (pl.inventory.ruby >= it.gemSell) {
                pl.inventory.ruby -= it.gemSell;
                isBuy = true;
            } else {
                Service.gI().sendThongBao(pl, "Bạn không đủ hồng ngọc để mua vật phẩm này!");
                isBuy = false;
            }
        }
        Service.gI().sendMoney(pl);
        if (isBuy) {
            Item item = ItemService.gI().createNewItem(it.itemId);
            item.quantity = it.quantity;
            item.itemOptions.addAll(it.options);
            it.isBuy = 1;
            if (it.isBuy == 1) {

                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + item.template.name);
                openShopKyGui(pl);
            }
        }
    }

    public ItemKyGui getItemBuy(int id) {
        for (ItemKyGui it : getItemKyGui()) {
            if (it != null && it.id == id) {
                return it;
            }
        }
        return null;
    }

    public ItemKyGui getItemBuy(Player pl, int id) {
        for (ItemKyGui it : ShopKyGuiManager.gI().listItem) {
            if (it != null && it.id == id && it.player_sell == pl.id) {
                return it;
            }
        }
        return null;
    }

    public void openShopKyGui(Player pl, byte index, int page) {
        if (page > getItemKyGui(pl, index).size()) {
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-100);
            msg.writer().writeByte(index);
            List<ItemKyGui> items = getItemKyGui2(pl, index);
            byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
            msg.writer().writeByte(tab); // max page
            msg.writer().writeByte(page);
            List<ItemKyGui> itemsSend = getItemKyGui2(pl, index, page + 1);
            msg.writer().writeByte(itemsSend.size());
            for (int j = 0; j < itemsSend.size(); j++) {
                ItemKyGui itk = itemsSend.get(j);
                Item it = ItemService.gI().createNewItem(itk.itemId);
                it.itemOptions.clear();
                if (itk.options.isEmpty()) {
                    it.itemOptions.add(new ItemOption(73, 0));
                } else {
                    it.itemOptions.addAll(itk.options);
                }
                msg.writer().writeShort(it.template.id);
                msg.writer().writeShort(itk.id);
                msg.writer().writeInt(itk.goldSell);
                msg.writer().writeInt(itk.gemSell);
                msg.writer().writeByte(0); // buy type
                if (pl.getSession().version >= 222) {
                    msg.writer().writeInt(itk.quantity);
                } else {
                    msg.writer().writeByte(itk.quantity);
                }
                msg.writer().writeByte(itk.player_sell == pl.id ? 1 : 0); // isMe
                msg.writer().writeByte(it.itemOptions.size());
                for (int a = 0; a < it.itemOptions.size(); a++) {
                    msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                    msg.writer().writeShort(it.itemOptions.get(a).param);
                }
                msg.writer().writeByte(0);
//                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            System.err.print("\nError at 7\n");
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void upItemToTop(Player pl, int id) {
        ItemKyGui it = getItemBuy(id);
        if (it == null || it.isBuy == 1) {
            Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
            return;
        }
        if (it.player_sell != pl.id) {
            Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
            openShopKyGui(pl);
            return;
        }
        pl.iDMark.setIdItemUpTop(id);
        NpcService.gI().createMenuConMeo(pl, ConstNpc.UP_TOP_ITEM, -1, "Bạn có muốn đưa vật phẩm ['" + ItemService.gI().createNewItem(it.itemId).template.name + "'] của bản thân lên trang đầu?\nYêu cầu 2 thỏi vàng.", "Đồng ý", "Từ Chối");
    }

    public void StartupItemToTop(Player pl) {
        if (!SubThoiVang(pl, 2)) {
            Service.gI().sendThongBao(pl, "Bạn cần có ít nhất 2 thỏi vàng đưa vật phẩm lên trang đầu");
            return;
        }
        for (ItemKyGui its : ShopKyGuiManager.gI().listItem) {
            if (its.id == pl.iDMark.getIdItemUpTop()) {
                its.isUpTop = 1;
                its.createTime = System.currentTimeMillis();
                Service.gI().sendThongBao(pl, "Đưa vật phẩm lên trang đầu thành công");
                break;
            }
        }
        openShopKyGui(pl);
    }

    public void claimOrDel(Player pl, byte action, int id) {
        ItemKyGui it = getItemBuy(pl, id);
        switch (action) {
            case 1: // hủy vật phẩm
                if (it == null || it.isBuy == 1) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
                    return;
                }
                if (it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    openShopKyGui(pl);
                    return;
                }
                Item item = ItemService.gI().createNewItem(it.itemId);
                item.quantity = it.quantity;
                item.itemOptions.addAll(it.options);
                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    InventoryServiceNew.gI().addItemBag(pl, item);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    Service.gI().sendMoney(pl);
                    Service.gI().sendThongBao(pl, "Hủy bán vật phẩm thành công");
                    openShopKyGui(pl);
                }
                break;
            case 2: // nhận tiền
                if (it == null || it.isBuy == 0) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc chưa được bán");
                    return;
                }
                if (it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    openShopKyGui(pl);
                    return;
                }
                if (it.goldSell > 0) {
                    Item tvAdd = ItemService.gI().createNewItem((short) 457);
                    tvAdd.quantity = (int) (it.goldSell * 0.9);
                    InventoryServiceNew.gI().addItemBag(pl, tvAdd);
                } else if (it.gemSell > 0) {
                    pl.inventory.ruby += (int) (it.gemSell * 0.9);
                }
                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    Service.gI().sendMoney(pl);
                    Service.gI().sendThongBao(pl, "Bạn đã bán vật phẩm thành công");
                    openShopKyGui(pl);
                }
                break;
        }
    }

    public List<ItemKyGui> getItemCanKiGui(Player pl) {
        List<ItemKyGui> its = new ArrayList<>();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.player_sell == pl.id)).forEachOrdered((it) -> {
            its.add(it);
        });
        pl.inventory.itemsBag.stream().filter((it) -> (it.isNotNullItem() && (isKyGui(it)))).forEachOrdered((it) -> {
            its.add(new ItemKyGui(InventoryServiceNew.gI().getIndexBag(pl, it), it.template.id, (int) pl.id, (byte) 4, -1, -1, it.quantity, (byte) -1, it.itemOptions, (byte) 0, System.currentTimeMillis()));
        });
        return its;
    }

    public int getMaxId() {
        try {
            List<Integer> id = new ArrayList<>();
            ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null)).forEachOrdered((it) -> {
                id.add(it.id);
            });

            if (id.isEmpty()) {
                return 0;
            }

            return Collections.max(id);
        } catch (Exception e) {
            System.err.print("\nError at 8\n");
            e.printStackTrace();
            return 0;
        }
    }

    public byte getTabKiGui(Item it) {
        if (it.template.type >= 0 && it.template.type <= 4) {
            return 0;
        } else if ((it.template.type == 27)) {
            switch (it.template.id) {
                case 921:
                case 1155:
                case 1156:
                case 568:
                    return 1;
                case 1066:
                case 1067:
                case 1068:
                case 1069:
                case 1070:
                    return 3;
            }
            return 3;
        } else if (it.template.type == 72) {
            return 2;
        } else {
            return 3;
        }
    }

    private boolean ManhThienSu(int id) {
        return id == 1066 || id == 1067 || id == 1068 || id == 1069 || id == 1070;
    }

    public void KiGui(Player pl, int id, int money, byte moneyType, int quantity) {
        try {
            if (!SubThoiVang(pl, 1)) {
                Service.gI().sendThongBao(pl, "Bạn cần có ít nhất 1 thỏi vàng để làm phí đăng bán");
                return;
            }
            Item it = ItemService.gI().copyItem(pl.inventory.itemsBag.get(id));
            for (Item.ItemOption daubuoi : it.itemOptions) {
                if (daubuoi.optionTemplate.id == 30) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thể kí gửi");
                    openShopKyGui(pl);
                    return;
                }
            }
            if (money <= 0 || quantity > it.quantity) {
                openShopKyGui(pl);
                return;
            }
            if (quantity > 9999) {
                Service.gI().sendThongBao(pl, "Ký gửi tối đa x9999");
                openShopKyGui(pl);
                return;
            }
            if (quantity < 50 && it.template.id == 1280) {
                Service.gI().sendThongBao(pl, "Ma thạch cần ký gửi x50 trở lên");
                openShopKyGui(pl);
                return;
            }
            if (quantity < 50 && ManhThienSu(it.template.id)) {
                Service.gI().sendThongBao(pl, "Mảnh thiên sứ cần ký gửi x50 trở lên");
                openShopKyGui(pl);
                return;
            }
            pl.inventory.gem -= 5;
            switch (moneyType) {
                case 0:// vàng
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 100000 thỏi vàng");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id, getTabKiGui(it), money, -1, quantity, (byte) 0, it.itemOptions, (byte) 0, System.currentTimeMillis()));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }
                    break;
                case 1:// hồng ngọc
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 100000 ngọc");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id, getTabKiGui(it), -1, money, quantity, (byte) 0, it.itemOptions, (byte) 0, System.currentTimeMillis()));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }

                    break;
                default:
                    Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
                    openShopKyGui(pl);
                    break;
            }
        } catch (Exception e) {
            System.err.print("\nError at 9\n");
            e.printStackTrace();
        }
    }

    public void openShopKyGui(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(2);
            msg.writer().writeByte(5);
            for (byte i = 0; i < 5; i++) {
                if (i == 4) {
                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    msg.writer().writeByte(0);
                    msg.writer().writeByte(getItemCanKiGui(pl).size());
                    for (int j = 0; j < getItemCanKiGui(pl).size(); j++) {
                        ItemKyGui itk = getItemCanKiGui(pl).get(j);
                        if (itk == null) {
                            continue;
                        }
                        Item it = ItemService.gI().createNewItem(itk.itemId);
                        it.itemOptions.clear();
                        if (itk.options.isEmpty()) {
                            it.itemOptions.add(new ItemOption(73, 0));
                        } else {
                            it.itemOptions.addAll(itk.options);
                        }
                        msg.writer().writeShort(it.template.id);
                        msg.writer().writeShort(itk.id);
                        msg.writer().writeInt(itk.goldSell);
                        msg.writer().writeInt(itk.gemSell);
                        if (getItemBuy(pl, itk.id) == null) {
                            msg.writer().writeByte(0); // buy type
                        } else if (itk.isBuy == 1) {
                            msg.writer().writeByte(2);
                        } else if (itk.isBuy == 2) {
                            msg.writer().writeByte(3);
                        } else {
                            msg.writer().writeByte(1);
                        }
                        if (pl.getSession().version >= 222) {
                            msg.writer().writeInt(itk.quantity);
                        } else {
                            msg.writer().writeByte(itk.quantity);
                        }
                        msg.writer().writeByte(1); // isMe
                        msg.writer().writeByte(it.itemOptions.size());
                        for (int a = 0; a < it.itemOptions.size(); a++) {
                            msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                            msg.writer().writeShort(it.itemOptions.get(a).param);
                        }
                        msg.writer().writeByte(0);
                        msg.writer().writeByte(0);
                    }
                } else {
                    List<ItemKyGui> items = getItemKyGui2(pl, i);
                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
                    List<ItemKyGui> itemsSend = getItemKyGui2(pl, i, 1);
                    msg.writer().writeByte(tab); // max page
                    msg.writer().writeByte(itemsSend.size());
                    for (int j = 0; j < itemsSend.size(); j++) {
                        ItemKyGui itk = itemsSend.get(j);
                        Item it = ItemService.gI().createNewItem(itk.itemId);
                        it.itemOptions.clear();
                        if (itk.options.isEmpty()) {
                            it.itemOptions.add(new ItemOption(73, 0));
                        } else {
                            it.itemOptions.addAll(itk.options);
                        }
                        msg.writer().writeShort(it.template.id);
                        msg.writer().writeShort(itk.id);
                        msg.writer().writeInt(itk.goldSell);
                        msg.writer().writeInt(itk.gemSell);
                        msg.writer().writeByte(0); // buy type
                        if (pl.getSession().version >= 222) {
                            msg.writer().writeInt(itk.quantity);
                        } else {
                            msg.writer().writeByte(itk.quantity);
                        }
                        msg.writer().writeByte(itk.player_sell == pl.id ? 1 : 0); // isMe
                        msg.writer().writeByte(it.itemOptions.size());
                        for (int a = 0; a < it.itemOptions.size(); a++) {
                            msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
                            msg.writer().writeShort(it.itemOptions.get(a).param);
                        }
                        msg.writer().writeByte(0);
                        msg.writer().writeByte(0);
                    }
                }
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            System.err.print("\nError at 10\n");
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
}
