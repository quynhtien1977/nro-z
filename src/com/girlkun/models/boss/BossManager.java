package com.girlkun.models.boss;

import com.girlkun.models.boss.list_boss.BLACK.*;
import com.girlkun.models.boss.list_boss.Cooler.Cooler;
// import com.girlkun.models.boss.list_boss.HuyDiet.Champa;
// import com.girlkun.models.boss.list_boss.HuyDiet.ThanHuyDiet;
// import com.girlkun.models.boss.list_boss.HuyDiet.ThienSuWhis;
// import com.girlkun.models.boss.list_boss.HuyDiet.Vados;
import com.girlkun.models.boss.list_boss.NgucTu.CoolerGold;
// import com.girlkun.models.boss.list_boss.FideBack.Kingcold;
// import com.girlkun.models.boss.list_boss.SuperXen;
// import com.girlkun.models.boss.list_boss.NgucTu.Cumber;
import com.girlkun.models.boss.list_boss.cell.Xencon;
import com.girlkun.models.boss.list_boss.ginyu.TDST;
import com.girlkun.models.boss.list_boss.android.*;
import com.girlkun.models.boss.list_boss.cell.SieuBoHung;
import com.girlkun.models.boss.list_boss.cell.XenBoHung;
import com.girlkun.models.boss.list_boss.Broly.Broly;
// import com.girlkun.models.boss.list_boss.Doraemon.Doraemon;
// import com.girlkun.models.boss.list_boss.Doraemon.Nobita;
// import com.girlkun.models.boss.list_boss.Doraemon.Xeko;
// import com.girlkun.models.boss.list_boss.Doraemon.Xuka;
// import com.girlkun.models.boss.list_boss.Doraemon.Chaien;
import com.girlkun.models.boss.list_boss.FideBack.FideRobot;
import com.girlkun.models.boss.list_boss.FideBack.Kingcold;
// import com.girlkun.models.boss.list_boss.NgucTu.SongokuTaAc;
import com.girlkun.models.boss.list_boss.fide.Fide;
import com.girlkun.models.boss.list_boss.NRD.Rong1Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong2Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong3Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong4Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong5Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong6Sao;
import com.girlkun.models.boss.list_boss.NRD.Rong7Sao;
import com.girlkun.models.boss.list_boss.Mabu12h.MabuBoss;
import com.girlkun.models.boss.list_boss.Mabu12h.BuiBui;
import com.girlkun.models.boss.list_boss.Mabu12h.BuiBui2;
import com.girlkun.models.boss.list_boss.Mabu12h.Drabura;
import com.girlkun.models.boss.list_boss.Mabu12h.Drabura2;
import com.girlkun.models.boss.list_boss.Mabu12h.Yacon;
import com.girlkun.models.boss.list_boss.NgucTu.Berus;
import com.girlkun.models.boss.list_boss.NgucTu.ThanThienSu;
import com.girlkun.models.boss.list_boss.TauHuyDiet.ThanHuyDiet123;
import com.girlkun.models.boss.list_boss.TauHuyDiet.ThienSuWhis;
// import com.girlkun.models.boss.list_boss.gohanNN;
// import com.girlkun.models.boss.list_boss.kami.cumberBlack;
// import com.girlkun.models.boss.list_boss.kami.cumberYellow;
// import com.girlkun.models.boss.list_boss.kami.kamiLoc;
// import com.girlkun.models.boss.list_boss.kami.kamiRin;
// import com.girlkun.models.boss.list_boss.kami.kamiSooMe;
import com.girlkun.models.boss.list_boss.nappa.*;
//import com.girlkun.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.server.ServerManager;
import com.girlkun.services.ItemMapService;
import com.girlkun.services.MapService;
import java.util.ArrayList;
import java.util.List;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 50;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.POC);

            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.VUA_COLD);
            this.createBoss(BossID.VUA_COLD);
            this.createBoss(BossID.FIDE_ROBOT);
            this.createBoss(BossID.FIDE_ROBOT);

            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);

            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK1);
            this.createBoss(BossID.BLACK1);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.BLACK3);


            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.ZAMASMAX);

            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.FIDE);

            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.ANTROM);

            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.BROLY);

            this.createBoss(BossID.THAN_THIEN_SU);
            this.createBoss(BossID.THAN_THIEN_SU);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.THIEN_SU_WHIS);

            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_HUY_DIET1);
            this.createBoss(BossID.THAN_HUY_DIET1);

            this.createBoss(BossID.ANTROM);
            this.createBoss(BossID.ANTROM);
            this.createBoss(BossID.ANTROM);


        } catch (Exception e) {
            e.printStackTrace();

        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBossBdkb(int bossID, int dame, int hp, Zone zone) {
        try {
            switch (bossID) {
//                case BossID.TRUNG_UY_XANH_LO_BDKB:
//                    return new TrungUyXanhLoBdkb(dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {

            return null;
        }
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
                case BossID.THIEN_SU_WHIS:
                    return new ThienSuWhis();
                case BossID.THAN_HUY_DIET1:
                    return new ThanHuyDiet123();   
                case BossID.THAN_THIEN_SU:
                    return new ThanThienSu();
                case BossID.THAN_HUY_DIET:
                    return new Berus();    
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.ZAMASZIN:
                    return new ZamasKaio();
                case BossID.BLACK2:
                    return new SuperBlack2();
                case BossID.BLACK1:
                    return new BlackGokuTl();
                case BossID.BLACK:
                    return new Black();
                case BossID.BLACK3:
                    return new BlackGokuBase();
                case BossID.XEN_CON_1:
                    return new Xencon();
                case BossID.TDST:
                    return new TDST();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.BROLY:
                    return new Broly();
                case BossID.ANTROM:
                    
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0]) && !MapService.gI().isnguhs(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]) || MapService.gI().isnguhs(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    if (player.isAdmin()) {
                        msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                    } else {
                        msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu ?");
                    }
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void dobossmember(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                    //                    && !MapService.gI().isMapTienMon(boss.data[0].getMapJoin()[0])
                    //                    && !(boss instanceof MiNuong))
                    && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapKhiGas(boss.data[0].getMapJoin()[0])
                    //                    && !MapService.gI().isMapNhanBan(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        //                        || boss instanceof MiNuong 
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGas(boss.data[0].getMapJoin()[0])) //                        || MapService.gI().isMapNhanBan(boss.data[0].getMapJoin()[0]) 
                //                        || MapService.gI().isMapTienMon(boss.data[0].getMapJoin()[0]))
                {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Dịch chuyển");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {

        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
