package com.girlkun.models.shop;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.services.func.TransactionService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.util.Set;
import java.util.HashSet;
import java.util.List;


public class ShopServiceNew {

    private static final byte COST_GOLD = 0;
    private static final byte COST_GEM = 1;
    private static final byte COST_ITEM_SPEC = 2;
    private static final byte COST_RUBY = 3;
    private static final byte COST_COUPON = 4;

    private static final byte NORMAL_SHOP = 0;
    private static final byte SPEC_SHOP = 3;
    private static final byte BOX = 4;

    private static ShopServiceNew I;

    public static ShopServiceNew gI() {
        if (ShopServiceNew.I == null) {
            ShopServiceNew.I = new ShopServiceNew();
        }
        return ShopServiceNew.I;
    }

    public void opendShop(Player player, String tagName, boolean allGender) {//Zalo: 0358124452//Name: EMTI 
        if(tagName.equals("ITEMS_MAIL_BOX")){
             openShopType4(player, tagName, player.inventory.itemsMailBox);
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {//Zalo: 0358124452//Name: EMTI 
            openShopType4(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {//Zalo: 0358124452//Name: EMTI 
            player.getSession().initItemsReward();
            return;
        }
        try {//Zalo: 0358124452//Name: EMTI 
            Shop shop = this.getShop(tagName);
            shop = this.resolveShop(player, shop, allGender);
            switch (shop.typeShop) {//Zalo: 0358124452//Name: EMTI 
                case NORMAL_SHOP:
                    openShopType0(player, shop);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case SPEC_SHOP:
                    openShopType3(player, shop);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
            }
        } catch (Exception ex) {//Zalo: 0358124452//Name: EMTI 
            ex.printStackTrace();
            Service.gI().sendThongBao(player, ex.getMessage());
        }
    }

    private Shop getShop(String tagName) throws Exception {
        for (Shop s : Manager.SHOPS) {
            if (s.tagName != null && s.tagName.equals(tagName)) {
                return s;
            }
        }
        throw new Exception("Shop " + tagName + " không tồn tại!");
    }

    private void _________________Xử_lý_cửa_hàng_trước_khi_gửi_______________() {
        //**********************************************************************
    }

     private Shop resolveShop(Player player, Shop shop, boolean allGender) {//Zalo: 0358124452//Name: EMTI 
        if (shop.tagName != null && (shop.tagName.equals("BUA_1H")
                || shop.tagName.equals("BUA_8H") || shop.tagName.equals("BUA_1M"))) {//Zalo: 0358124452//Name: EMTI 
            return this.resolveShopBua(player, new Shop(shop));
        }
        return allGender ? new Shop(shop) : new Shop(shop, player.gender);
    }

    private Shop resolveShopBua(Player player, Shop s) {
        for (TabShop tabShop : s.tabShops) {
            for (ItemShop item : tabShop.itemShops) {
                long min = 0;
                switch (item.temp.id) {
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;

                        break;
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;
                }
                if (min > 0) {
                    item.options.clear();
                    if (min >= 1440) {
                        item.options.add(new Item.ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {
                        item.options.add(new Item.ItemOption(64, (int) min / 60));
                    } else {
                        item.options.add(new Item.ItemOption(65, (int) min));
                    }
                }
            }
        }
        return s;
    }

    private void _________________Gửi_cửa_hàng_cho_người_chơi________________() {
        //**********************************************************************
    }

    private void openShopType0(Player player, Shop shop) {//Zalo: 0358124452//Name: EMTI 
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {//Zalo: 0358124452//Name: EMTI 
            Message msg;
            try {//Zalo: 0358124452//Name: EMTI 
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {//Zalo: 0358124452//Name: EMTI 
                        msg.writer().writeShort(itemShop.temp.id);
                        if (itemShop.typeSell == COST_GOLD) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(itemShop.cost);
                            msg.writer().writeInt(0);
                        } else if (itemShop.typeSell == COST_GEM) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_RUBY) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_COUPON) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        }
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

     private void openShopType3(Player player, Shop shop) {//Zalo: 0358124452//Name: EMTI 
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {//Zalo: 0358124452//Name: EMTI 
            Message msg;
            try {//Zalo: 0358124452//Name: EMTI 
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {//Zalo: 0358124452//Name: EMTI 
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.cost);
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType4(Player player, String tagName, List<Item> items) {//Zalo: 0358124452//Name: EMTI 
        if (items == null) {//Zalo: 0358124452//Name: EMTI 
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {//Zalo: 0358124452//Name: EMTI 
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(items.size());
            for (Item item : items) {//Zalo: 0358124452//Name: EMTI 
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|Ngọc Rồng Zair0u");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                //số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 

            e.printStackTrace();
        }
    }

    private void _________________Mua_vật_phẩm______________________________() {
        //**********************************************************************
    }

    public void takeItem(Player player, byte type, int tempId, int quantity) {//Zalo: 0358124452//Name: EMTI 
        String tagName = player.iDMark.getTagNameShop();
        if (tagName == null || tagName.length() <= 0) {//Zalo: 0358124452//Name: EMTI 
            return;
        }
        
        if (tagName.equals("ITEMS_MAIL_BOX")) {//Zalo: 0358124452//Name: EMTI 
            getItemSideBoxLuckyRound(player, player.inventory.itemsMailBox, type, tempId);
            return;
        } 

        if (tagName.equals("ITEMS_LUCKY_ROUND")) {//Zalo: 0358124452//Name: EMTI 
            getItemSideBoxLuckyRound(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {//Zalo: 0358124452//Name: EMTI 
            return;
        }

        if (player.iDMark.getShopOpen() == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (tagName.equals("BUA_1H") || tagName.equals("BUA_8H") || tagName.equals("BUA_1M")) {//Zalo: 0358124452//Name: EMTI 
            buyItemBua(player, tempId);
        } else {//Zalo: 0358124452//Name: EMTI 
            buyItem(player, tempId);
        }
        Service.gI().sendMoney(player);
    }

    private boolean subMoneyByItemShop(Player player, ItemShop is) {
        int gold = 0;
        int gem = 0;
        int ruby = 0;
        int coupon = 0;
        switch (is.typeSell) {
            case COST_GOLD:
                gold = is.cost;
                break;
            case COST_GEM:
                gem = is.cost;
                break;
            case COST_RUBY:
                ruby = is.cost;
                break;
            case COST_COUPON:
                coupon = is.cost;
                break;

        }
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Bạn không có đủ vàng");
            return false;
        } else if (player.inventory.gem < gem) {
            Service.gI().sendThongBao(player, "Bạn không có đủ ngọc");
            return false;
        } else if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc");
            return false;
        } else if (player.inventory.coupon < coupon) {
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm");
            return false;
        }
        player.inventory.gold -= is.temp.gold;
        player.inventory.gem -= is.temp.gem;
        player.inventory.ruby -= ruby;
        player.inventory.coupon -= coupon;
        return true;
    }

    /**
     * Mua bùa
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemBua(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (!subMoneyByItemShop(player, is)) {
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
        InventoryServiceNew.gI().sendItemBags(player);
        opendShop(player, shop.tagName, true);
    }

    /**
     * Mua vật phẩm trong cửa hàng
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    public void buyItem(Player player, int itemTempId) {
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }

        // Kiểm tra nếu là vật phẩm hủy diệt và không đủ thức ăn
        if (isHuyDietItem(is) && !hasEnoughThucAn(player)) {
            Service.gI().sendThongBao(player, "Không đủ thức ăn để mua đồ!");
            return;
        }

        if (shop.typeShop == ShopServiceNew.NORMAL_SHOP) {
            if (!subMoneyByItemShop(player, is)) {
                return;
            }
        } else if (shop.typeShop == ShopServiceNew.SPEC_SHOP) {
            if (!this.subIemByItemShop(player, is)) {
                return;
            }
        }

        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
    }

// Kiểm tra nếu là vật phẩm hủy diệt
    private boolean isHuyDietItem(ItemShop itemShop) {
        int itemId = itemShop.temp.id;
        return itemId >= 650 && itemId <= 662;
    }

// Kiểm tra nếu có đủ thức ăn
    private boolean hasEnoughThucAn(Player player) {
        return player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.isThucAn() && item.quantity >= 99)
                .findFirst().isPresent();
    }

    private void _________________Bán_vật_phẩm______________________________() {
        //**********************************************************************
    }

    private boolean subIemByItemShop(Player pl, ItemShop itemShop) {
        boolean isBuy = false;
        short itSpec = ItemService.gI().getItemIdByIcon((short) itemShop.iconSpec);
        int buySpec = itemShop.cost;
        Item itS = ItemService.gI().createNewItem(itSpec);
        switch (itS.template.id) {
            case 861:
            case 188:
            case 189:
            case 190:
                if (pl.inventory.ruby >= buySpec) {
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Vàng Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            case 76:
                if (pl.inventory.ruby >= buySpec) {
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Hồng Ngọc Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;
            default:
                if (InventoryServiceNew.gI().findItemBag(pl, itSpec) == null || !InventoryServiceNew.gI().findItemBag(pl, itSpec).isNotNullItem()) {
                    Service.gI().sendThongBao(pl, "Không tìm thấy " + itS.template.name);
                    isBuy = false;
                } else if (InventoryServiceNew.gI().findItemBag(pl, itSpec).quantity < buySpec) {
                    Service.gI().sendThongBao(pl, "Bạn không có đủ " + buySpec + " " + itS.template.name);
                    isBuy = false;
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, itSpec), buySpec);
                    isBuy = true;
                }
                break;
        }
        return isBuy;
    }

    public void showConfirmSellItem(Player pl, int where, int index) {

        TransactionService.gI().cancelTrade(pl);
        if (index < 0) {
            return;
        }
        Item item = null;
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            if (pl.getSession().version < 220) {
                index -= (pl.inventory.itemsBody.size() - 7);
            }
            if (index >= 0) {
                item = pl.inventory.itemsBag.get(index);
            } else {
                item = pl.inventory.itemsBag.get(0);
            }
        }
        if (item != null && item.isNotNullItem()) {
            int quantity = item.quantity;
            // int cost = item.template.id == 457 ? item.template.gold : 1;
            int cost = item.template.gold;
            if (item.template.id == 457) {
                quantity = 1;
            } else if(isContains(thanItems, item.template.id) != -1 || isContains(huydietItem, item.template.id) != -1 || isContains(thienSuItem, item.template.id) != -1){
                // không chia giá     
            }
            else{
                cost /= 4;
            }
            if (cost == 0) {
                cost = 1;
            }
            cost *= quantity;

            String text = "Bạn có muốn bán\nx" + quantity
                    + " " + item.template.name + "\nvới giá là " + Util.numberToMoney(cost) + " vàng?";
            Message msg = new Message(7);
            try {
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer().writeUTF(text);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                   
            }
        }
    }

    private int isContains(int[][] arr, int id){
        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[row].length; col++) {
                if (arr[row][col] == id) {
                    return col; 
                }
            }
        }
        return -1;
    }

    public void sellItem(Player pl, int where, int index) {
        Item item = null;
        if (index < 0) {
            return;
        }
        if (where == 0) {
            item = pl.inventory.itemsBody.get(index);
        } else {
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.template.id != 921 && item.template.id != 454 && item.template.id != 194) { // Thêm điều kiện kiểm tra id của vật phẩm khác với 921
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {
                quantity = 1;
            } else {
                cost /= 4;
            }
            if(isContains(thanItems, item.template.id) != -1 ){
                switch (isContains(thanItems, item.template.id)) {
                    case 0:
                        cost = 150000000;
                        break;
                    case 1:
                        cost = 145000000;
                        break;
                    case 2:
                        cost = 200000000;
                        break;
                    case 3:
                        cost = 125000000;
                        break;
                    case 4:
                        cost = 220000000;
                        break;
                }
            }
            else if(isContains(huydietItem, item.template.id) != -1 ){
                switch (isContains(huydietItem, item.template.id)) {
                    case 0:
                        cost = 170000000;
                        break;
                    case 1:
                        cost = 160000000;
                        break;
                    case 2:
                        cost = 250000000;
                        break;
                    case 3:
                        cost = 145000000;
                        break;
                    case 4:
                        cost = 300000000;
                        break;
                }
            }
            else if(isContains(thienSuItem, item.template.id) != -1 ){
                switch (isContains(thienSuItem, item.template.id)) {
                    case 0:
                        cost = 1000000000;
                        break;
                    case 1:
                        cost = 1500000000;
                        break;
                    case 2:
                        cost = 2000000000;
                        break;
                    case 3:
                        cost = 1400000000;
                        break;
                    case 4:
                        cost = 2000000000;
                        break;
                }
            }

            if (cost == 0) {
                cost = 1;
            }
            cost *= quantity;
            if (pl.inventory.gold + cost > Inventory.LIMIT_GOLD) {
                Service.gI().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn");
                return;
            }
            pl.inventory.gold += cost;
            Service.gI().sendMoney(pl);
            Service.gI().sendThongBao(pl, "Đã bán " + item.template.name
                    + " thu được " + Util.numberToMoney(cost) + " vàng");
            if (where == 0) {
                InventoryServiceNew.gI().subQuantityItemsBody(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBody(pl);
                Service.gI().Send_Caitrang(pl);
            } else {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            Service.gI().sendThongBao(pl, "Không thể bán " + item.template.name + " này được");
        }
    }

    private void _________________Nhận_vật_phẩm_từ_rương_đặc_biệt___________() {
        //**********************************************************************
    }

    private void getItemSideBoxLuckyRound(Player player, List<Item> items, byte type, int index) {//Zalo: 0358124452//Name: EMTI 
        if (items == null || items.isEmpty() || index >= items.size()) {//Zalo: 0358124452//Name: EMTI 
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }

        Item item = items.get(index);
        switch (type) {//Zalo: 0358124452//Name: EMTI 
            case 0: // nhận
                if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {//Zalo: 0358124452//Name: EMTI 
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        InventoryServiceNew.gI().sendItemBags(player);
                        items.remove(index);
                    } else {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {//Zalo: 0358124452//Name: EMTI 
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case 1: // xóa
                items.remove(index);
                Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case 2: // nhận hết
                for (int i = items.size() - 1; i >= 0; i--) {//Zalo: 0358124452//Name: EMTI 
                    item = items.get(i);
                    if (InventoryServiceNew.gI().addItemBag(player, item)) {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                        items.remove(i);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                break;                                //Zalo: 0358124452                                //Name: EMTI 
        }
        openShopType4(player, player.iDMark.getTagNameShop(), items);
    }
    private final int[][] thanItems = {
        {555, 556, 562, 563, 561}, // Trái đất
        {557, 558, 564, 565, 561}, // Namek
        {559, 560, 566, 567, 561}  // Xayda
    };

    private final int[][] huydietItem = {
        {650, 651, 657, 658, 656}, // Trái đất
        {652, 653, 659, 660, 656}, // Namek
        {654, 655, 661, 662, 656}  // Xayda
    };

    private final int[][] thienSuItem = {
        {1048, 1051, 1054, 1057, 1060}, // Trái đất
        {1049, 1052, 1055, 1058, 1061}, // Namek
        {1050, 1053, 1056, 1059, 1062}  // Xayda
    };
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girl Béo
 */
