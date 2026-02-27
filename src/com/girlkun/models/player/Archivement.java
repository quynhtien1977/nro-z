package com.girlkun.models.player;

import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.models.item.Item;
import com.girlkun.network.io.Message;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Archivement {

    public String info1;
    public String info2;
    public short money;
    public boolean isFinish;
    public boolean isRecieve;
    public static Archivement gI = null;

    public static Archivement gI() {
        if (gI == null) {
            return new Archivement();
        }
        return gI;
    }

    public Archivement() {
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public short getMoney() {
        return money;
    }

    public void setMoney(short money) {
        this.money = money;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isRecieve() {
        return isRecieve;
    }

    public void setRecieve(boolean recieve) {
        isRecieve = recieve;
    }

    public Archivement(String info1, String info2, short money, boolean isFinish, boolean isRecieve) {
        this.info1 = info1;
        this.info2 = info2;
        this.money = money;
        this.isFinish = isFinish;
        this.isRecieve = isRecieve;
    }

    public void Show(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(0); // action
            msg.writer().writeByte(pl.archivementList.size());
            for (int i = 0; i < pl.archivementList.size(); i++) {
                Archivement archivement = pl.archivementList.get(i);
                msg.writer().writeUTF(archivement.getInfo1());
                msg.writer().writeUTF(archivement.getInfo2());
                msg.writer().writeShort(archivement.getMoney()); //money
                msg.writer().writeBoolean(archivement.isFinish);
                msg.writer().writeBoolean(archivement.isRecieve);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

            e.getStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public boolean checktongnap(Player pl, int index) {
        if (index == 0 && pl.getSession().TongNap > 50000) {
            return true;
        }
        if (index == 1 && pl.getSession().TongNap > 100000) {
            return true;
        }
        if (index == 2 && pl.getSession().TongNap > 300000) {
            return true;
        }
        if (index == 3 && pl.getSession().TongNap > 500000) {
            return true;
        }
        if (index == 4 && pl.getSession().TongNap > 1000000) {
            return true;
        }
        if (index == 5 && pl.getSession().TongNap > 1500000) {
            return true;
        }
        if (index == 6 && pl.getSession().TongNap > 2000000) {
            return true;
        }
        if (index == 7 && pl.getSession().TongNap > 3000000) {
            return true;
        }
        if (index == 8 && pl.getSession().TongNap > 5000000) {
            return true;
        }
        return false;
    }

    public void receiveGem(int index, Player pl) {
        Archivement temp = pl.archivementList.get(index);
        if (temp.isRecieve) {
            Service.gI().sendThongBao(pl, "Nhận rồi đừng nhận nữua");
            return;
        }
        if (temp != null) {
            Message msg = null;
            try {
                msg = new Message(-76);
                msg.writer().writeByte(1); // action
                msg.writer().writeByte(index); // index
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (IOException e) {
                Logger.logException(this.getClass(), e);
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }

            pl.archivementList.get(index).setRecieve(true);
            try {
                JSONArray dataArray = new JSONArray();

                for (Archivement arr : pl.archivementList) {
                    dataArray.add(arr.isRecieve ? "1" : "0");
                }
                String inventory = dataArray.toJSONString();
                dataArray.clear();
                GirlkunDB.executeUpdate("update player set Achievement = ? where name = ?", inventory, pl.name);
                nhanQua(pl, index);
                System.out.println("Player " + pl.name + " Nhận quà thành công");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Service.getInstance().sendThongBao(pl, "Nhận thành công, vui lòng kiểm tra hòm thư ");
        } else {
            Service.getInstance().sendThongBao(pl, "Không có phần thưởng");
        }
    }

    public static final String[] Textmocnap = new String[]{
        "\bMốc 50K: Nhận 10 Xu \n"
        + "\bMốc 100k: Nhận 20 Xu \n"
        + "\bMốc 300K: Nhận 30 Xu \n"
        + "\bMốc 500K: Nhận 50 Xu \n"
        + "\bMốc 1000K: Nhận 100 Xu  , 1 VPDL 30% HP KI SD\n"
        + "\bMốc 1500K: Nhận 150 Xu  , 1 PET 30% HP KI SD\n"
        + "\bMốc 2000K: Nhận 200 Xu  , 1 Hộp Thần Linh , 1 Ván Bay 40% HP KI SD\n"
        + "\bMốc 3000K: Nhận 300 Xu  , 2 Hộp Thần Linh, 1 LINH THÚ 40% HP KI SD\n"
        + "\bMốc 5000K: Nhận CẢI TRANG 50% HP KI SD , 4 Hộp Thần Linh, 1 DANH HIỆU ĐẠI GIA\n"
    };

    private void nhanQua(Player pl, int index) {

        int ql = 0;
        int ql1 = 0;
        int ql2 = 0;
        int ql3 = 0;
        int ql4 = 0;
        com.girlkun.models.item.Item item = null;
        com.girlkun.models.item.Item item1 = null;
        com.girlkun.models.item.Item item2 = null;
        com.girlkun.models.item.Item item3 = null;
        com.girlkun.models.item.Item item4 = null;

        switch (index) {
            case 0:
                item = ItemService.gI().createNewItem((short) 1335);
                ql = 10;
                item.quantity = ql;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 1:
                item = ItemService.gI().createNewItem((short) 1335);
                ql = 20;
                item.quantity = ql;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 2://30000
                item = ItemService.gI().createNewItem((short) 1335);
                ql = 30;
                item.quantity = ql;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 3://50000
                item = ItemService.gI().createNewItem((short) 1335);
                ql = 50;
                item.quantity = ql;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;

            case 4://70000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1425);
                ql = 1;
                item.quantity = ql;
                item.itemOptions.add(new Item.ItemOption(50, 30));
                item.itemOptions.add(new Item.ItemOption(77, 30));
                item.itemOptions.add(new Item.ItemOption(103, 30));
                item1 = ItemService.gI().createNewItem((short) 1335);
                ql1 = 100;
                item1.quantity = ql1;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().addItemBag(pl, item1);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 5://100000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1426);
                ql = 1;
                item.quantity = ql;
                item.itemOptions.add(new Item.ItemOption(50, 30));
                item.itemOptions.add(new Item.ItemOption(77, 30));
                item.itemOptions.add(new Item.ItemOption(103, 30));
                item1 = ItemService.gI().createNewItem((short) 1335);
                ql2 = 150;
                item1.quantity = ql2;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().addItemBag(pl, item1);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 6:
                item = ItemService.gI().createNewItem((short) 1433);
                ql = 1;
                item.quantity = ql;
                item.itemOptions.add(new Item.ItemOption(50, 40));
                item.itemOptions.add(new Item.ItemOption(77, 40));
                item.itemOptions.add(new Item.ItemOption(103, 40));
                item2 = ItemService.gI().createNewItem((short) 1335);
                ql2 = 200;
                item2.quantity = ql2;
                item3 = ItemService.gI().createNewItem((short) 1986);
                ql3 = 2;
                item3.quantity = ql3;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().addItemBag(pl, item2);
                InventoryServiceNew.gI().addItemBag(pl, item3);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 7:
                item = ItemService.gI().createNewItem((short) 1428);
                ql = 1;
                item.quantity = ql;
                item.itemOptions.add(new Item.ItemOption(50, 40));
                item.itemOptions.add(new Item.ItemOption(77, 40));
                item.itemOptions.add(new Item.ItemOption(103, 40));
                item2 = ItemService.gI().createNewItem((short) 1335);
                ql2 = 300;
                item2.quantity = ql2;
                item3 = ItemService.gI().createNewItem((short) 1986);
                ql3 = 2;
                item3.quantity = ql3;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().addItemBag(pl, item2);
                InventoryServiceNew.gI().addItemBag(pl, item3);
                InventoryServiceNew.gI().sendItemBags(pl);
                break;
            case 8:
                item = ItemService.gI().createNewItem((short) (1429 + pl.gender));
                ql = 1;
                item.quantity = ql;
                item.itemOptions.add(new Item.ItemOption(50, 50));
                item.itemOptions.add(new Item.ItemOption(77, 50));
                item.itemOptions.add(new Item.ItemOption(103, 50));
                item2 = ItemService.gI().createNewItem((short) 1432);
                ql2 = 1;
                item2.quantity = ql2;
                item2.itemOptions.add(new Item.ItemOption(50, 30));
                item2.itemOptions.add(new Item.ItemOption(77, 30));
                item2.itemOptions.add(new Item.ItemOption(103, 30));
                item3 = ItemService.gI().createNewItem((short) 1986);
                ql3 = 4;
                item3.quantity = ql3;
                InventoryServiceNew.gI().addItemBag(pl, item);
                InventoryServiceNew.gI().addItemBag(pl, item2);
                InventoryServiceNew.gI().addItemBag(pl, item3);
                InventoryServiceNew.gI().sendItemBags(pl);

        }

        InventoryServiceNew.gI()
                .sendItemBags(pl);
        Service.gI()
                .sendMoney(pl);
    }

    public void getAchievement(Player player) {
        try {
            if (player.getSession() == null) {
                return;
            }

            Connection con = null;
            PreparedStatement ps = null;
            JSONValue jv = new JSONValue();
            JSONArray dataArray = null;
            JSONArray dataArrayTemp = null;
            con = GirlkunDB.getConnection();
            ps = con.prepareStatement("SELECT `Achievement` FROM `player` WHERE name = ? LIMIT 1");
            ps.setString(1, player.name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String achievementData = rs.getString(1);
                    try {
                        dataArray = (JSONArray) jv.parse(achievementData);
                        if (dataArray != null && dataArray.size() != 10) {
                            if (dataArray.size() < 10) {
                                for (int j = dataArray.size(); j < 10; j++) {
                                    dataArray.add(0);
                                }
                            }

                            while (dataArray.size() > 10) {

                                dataArray.remove(10);

                            }

                        }
                        player.archivementList.clear();
                        if (dataArray != null) {
                            for (int i = 0; i < dataArray.size(); i++) {
                                try {
                                    Archivement achievement = new Archivement();
                                    achievement.setInfo1("Mốc Nạp " + getNhiemVu(i));
                                    achievement.setInfo2("Cần Nạp " + getNhiemVu(i));
                                    achievement.setFinish(checktongnap(player, i));
                                    achievement.setMoney((short) getRuby(i));
                                    achievement.setRecieve(Integer.parseInt(String.valueOf(dataArray.get(i))) != 0);
                                    player.archivementList.add(achievement);
                                } catch (Exception ee) {
                                    return;
                                }
                            }

                        }
                        dataArray.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Ok");
                Show(player);
                rs.close();
                ps.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNhiemVu(int index) {
        switch (index) {
            case 0:
                return "50k";
            case 1:
                return "100k";
            case 2:
                return "300k";
            case 3:
                return "500k";
            case 4:
                return "1000k";
            case 5:
                return "1500k";
            case 6:
                return "2000k";
            case 7:
                return "3000k";
            case 8:
                return "5000k";
            default:
                return "ĐÃ HẾT";
        }
    }

    public int getRuby(int index) {
        switch (index) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return 0;
            case 6:
                return 0;
            case 7:
                return 0;
            case 8:
                return 0;
            default:
                return -1;
        }
    }
}
