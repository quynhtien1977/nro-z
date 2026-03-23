package com.girlkun.models.matches.pvp;

import com.girlkun.jdbc.daos.SuperRankDAO;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.list_boss.super_rank.Rival;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Maintenance;
import com.girlkun.server.ServerNotify;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

public final class SuperRank implements Runnable {

    private Zone zone;
    private boolean isCompeting;
    private long playerId;
    private long rivalId;
    private Player player;
    private Boss rival;
    public int timeUp;
    public int timeDown;
    public int rankWin;
    public int rankLose;
    public boolean win;
    public int error;

    public long getPlayerId() {
        return playerId;
    }

    public long getRivalId() {
        return rivalId;
    }

    public Player getPlayer() {
        return player;
    }

    public Boss getRival() {
        return rival;
    }

    public Zone getZone() {
        return zone;
    }

    public SuperRank(Player player, long rivalId, Zone zone) {
        try {
            this.playerId = player.id;
            this.rivalId = rivalId;
            this.zone = zone;
            this.player = player;
            this.player.isPKDHVT = true;
            this.rankLose = player.superRank;
            Player riv = SuperRankService.gI().loadPlayer((int) rivalId);
            this.rankWin = riv.superRank;
            riv.nPoint.calPoint();
            this.rival = new Rival(player, riv);
            this.zone.isCompeting = true;
            this.zone.rank1 = player.superRank;
            this.zone.rank2 = riv.superRank;
            this.zone.rankName1 = player.name;
            this.zone.rankName2 = riv.name;
            init();
        } catch (Exception e) {
            e.printStackTrace();
            dispose();
        }
    }

    public void init() {
        timeUp = 0;
        timeDown = 180;
        rankLose = player.superRank;
        isCompeting = true;
        win = false;
        if (player.zone.zoneId != zone.zoneId) {
            ChangeMapService.gI().changeZone(player, zone.zoneId);
        }
        new Thread(this, "Super Rank").start();
    }

    @Override
    public void run() {
        while (!Maintenance.isRuning && isCompeting) {
            long startTime = System.currentTimeMillis();
            try {
                update();
            } catch (Exception e) {
                if (error < 5) {
                    error++;
                    System.err.println(e);
                }
            }
            long sleepTime = Math.max(1000 - (System.currentTimeMillis() - startTime), 10);
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {}
        }
    }

    public void update() {
        if (win) {
            return;
        }
        if (timeUp < 5) {
            switch (timeUp) {
                case 0:
                    Service.gI().sendThongBao(player, "Trận đấu bắt đầu");
                    Service.gI().setPos(player, 334, 264);
                    Service.gI().setPos(rival, 434, 264);
                    break;
                case 2:
                    Service.gI().chat(rival, SuperRankService.TEXT_SAN_SANG_CHUA);
                    break;
                case 3:
                    Service.gI().chat(player, SuperRankService.TEXT_SAN_SANG);
                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                    PlayerService.gI().changeAndSendTypePK(rival, ConstPlayer.PK_ALL);
                    break;
                case 4:
                    rival.changeStatus(BossStatus.ACTIVE);
                    break;
            }
            timeUp++;
            return;
        }
        if (timeDown > 0) {
            timeDown--;
            if (player != null && player.isPKDHVT && !player.isDie() && player.location != null && player.zone != null && player.zone.equals(zone)) {
                if (player.location.y > 264) {
                    lose();
                } else if (rival == null || rival.zone == null || rival.isDie() || rival.location.y > 264) {
                    win();
                }
            } else {
                lose();
            }
        } else {
            lose();
        }
    }

    public void win() {
        win = true;
        try {
            finish();
            Player plWin = SuperRankService.gI().loadPlayer((int) playerId);
            Player plLose = SuperRankService.gI().loadPlayer((int) rivalId);
            plWin.superRankWins++;
            plLose.superRankLoses++;
            if (plWin.superRankTicket == 0 && plWin.inventory.gem > 0) {
                plWin.inventory.gem -= 2;
            }
            plWin.superRank = rankWin;
            plWin.recordSuperRankHistory("Hạ " + plLose.name + "[" + rankLose + "]", System.currentTimeMillis());
            SuperRankDAO.updateSuperRank(plWin);
            
            plLose.superRank = rankLose;
            plLose.recordSuperRankHistory("Thua " + plWin.name + "[" + rankWin + "]", System.currentTimeMillis());
            SuperRankDAO.updateSuperRank(plLose);
            
            if (rankWin <= 10) {
                ServerNotify.gI().notify(SuperRankService.TEXT_TOP_10.replaceAll("%1", plWin.name).replaceAll("%2", rankWin + ""));
            }
            
            if (player != null && player.zone != null) {
                player.superRankWins++;
                if (player.superRankTicket == 0 && player.inventory.gem > 0) {
                    player.inventory.gem -= 2;
                    Service.gI().sendMoney(player);
                }
                player.superRank = rankWin;
                player.recordSuperRankHistory("Hạ " + plLose.name + "[" + rankLose + "]", System.currentTimeMillis());
                Service.gI().chat(player, SuperRankService.TEXT_THANG.replaceAll("%1", rankWin + ""));
            }
            
            Player rv = SuperRankService.gI().getPlayer((int) rivalId);
            if (rv != null && rv.zone != null) {
                rv.superRankLoses++;
                rv.superRank = rankLose;
                rv.recordSuperRankHistory("Thua " + plWin.name + "[" + rankWin + "]", System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }

    public void lose() {
        try {
            finish();
            Player plWin = SuperRankService.gI().loadPlayer((int) rivalId);
            Player plLose = SuperRankService.gI().loadPlayer((int) playerId);
            plWin.superRankWins++;
            plLose.superRankLoses++;
            if (plLose.superRankTicket > 0) {
                plLose.superRankTicket--;
            } else if (plLose.inventory.gem > 0) {
                plLose.inventory.gem -= 3;
            }
            plWin.superRank = rankWin;
            plWin.recordSuperRankHistory("Hạ " + plLose.name + "[" + rankLose + "]", System.currentTimeMillis());
            SuperRankDAO.updateSuperRank(plWin);
            
            plLose.superRank = rankLose;
            plLose.recordSuperRankHistory("Thua " + plWin.name + "[" + rankWin + "]", System.currentTimeMillis());
            SuperRankDAO.updateSuperRank(plLose);
            
            if (player != null && player.zone != null) {
                player.superRankLoses++;
                if (player.superRankTicket > 0) {
                    player.superRankTicket--;
                } else if (player.inventory.gem > 0) {
                    player.inventory.gem -= 3;
                    Service.gI().sendMoney(player);
                }
                player.superRank = rankLose;
                player.recordSuperRankHistory("Thua " + plWin.name + "[" + rankWin + "]", System.currentTimeMillis());
                Service.gI().chat(player, SuperRankService.TEXT_THUA);
            }
            
            Player rv = SuperRankService.gI().getPlayer((int) rivalId);
            if (rv != null && rv.zone != null) {
                rv.superRankWins++;
                rv.superRank = rankWin;
                rv.recordSuperRankHistory("Hạ " + plLose.name + "[" + rankLose + "]", System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }

    public void finish() {
        if (rival != null) {
            rival.leaveMap();
        }
        if (player != null && player.zone != null && player.zone.equals(zone)) {
            if (player.isDie()) {
                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        }
    }

    public void dispose() {
        if (player != null && player.location != null) {
            Service.gI().setPos(player, Util.nextInt(250, 450), 360);
        }
        if (this.zone != null) {
            this.zone.isCompeting = false;
            this.zone.rank1 = -1;
            this.zone.rank2 = -1;
            this.zone.rankName1 = null;
            this.zone.rankName2 = null;
        }
        isCompeting = false;
        if (player != null) {
            player.isPKDHVT = false; // need to add isPKDHVT to Player class
            player = null;
        }
        if (rival != null) {
            rival.dispose();
        }
        rival = null;
        zone = null;
        playerId = -1;
        rivalId = -1;
        rankWin = -1;
        rankLose = -1;
        SuperRankManager.gI().removeSPR(this);
    }

}
