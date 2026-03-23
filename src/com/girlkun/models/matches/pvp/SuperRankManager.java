package com.girlkun.models.matches.pvp;

import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuperRankManager implements Runnable {

    public static class WaitSuperRank {
        public long playerId;
        public long rivalId;
        public WaitSuperRank(long playerId, long rivalId) {
            this.playerId = playerId;
            this.rivalId = rivalId;
        }
    }

    private final List<WaitSuperRank> waitList;
    private final List<SuperRank> list;
    private static SuperRankManager instance;

    public static SuperRankManager gI() {
        if (instance == null) {
            instance = new SuperRankManager();
        }
        return instance;
    }

    public SuperRankManager() {
        waitList = new ArrayList<>();
        list = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!Maintenance.isRuning) {
            long startTime = System.currentTimeMillis();
            try {
                Iterator<WaitSuperRank> iterator = waitList.iterator();
                while (iterator.hasNext()) {
                    WaitSuperRank wsp = iterator.next();
                    Player wPl = Client.gI().getPlayer(wsp.playerId);
                    // 113 is map Dai Hoi Vo Thuat
                    if (wPl != null && wPl.zone != null && wPl.zone.map.mapId == 113) {
                        if (!SPRCheck(wPl.zone)) {
                            list.add(new SuperRank(wPl, wsp.rivalId, wPl.zone));
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = 500 - elapsedTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                }
            }
        }
    }

    public boolean canCompete(Player player) {
        return !currentlyCompeting(player.id) && !awaitingCompetition(player.id);
    }

    public boolean currentlyCompeting(long playerId) {
        for (int i = list.size() - 1; i >= 0; i--) {
            SuperRank spr = list.get(i);
            if (spr.getPlayerId() == playerId || spr.getRivalId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public boolean awaitingCompetition(long playerId) {
        for (int i = waitList.size() - 1; i >= 0; i--) {
            WaitSuperRank wspr = waitList.get(i);
            if (wspr.playerId == playerId || wspr.rivalId == playerId) {
                return true;
            }
        }
        return false;
    }

    public boolean awaiting(Player player) {
        for (int i = waitList.size() - 1; i >= 0; i--) {
            WaitSuperRank wspr = waitList.get(i);
            if (wspr.playerId == player.id) {
                return true;
            }
        }
        return false;
    }

    public boolean SPRCheck(Zone zone) {
        for (int i = list.size() - 1; i >= 0; i--) {
            SuperRank spr = list.get(i);
            if (spr.getZone() != null && spr.getZone().equals(zone)) {
                return true;
            }
        }
        return false;
    }

    public int ordinal(long id) {
        for (int i = 0; i < waitList.size(); i++) {
            if (waitList.get(i).playerId == id) {
                return i + 1;
            }
        }
        return -1;
    }

    public String getCompeting(long plId) {
        for (int i = list.size() - 1; i >= 0; i--) {
            SuperRank spr = list.get(i);
            if (spr.getPlayerId() == plId) {
                return "VS " + (spr.getRival() != null ? spr.getRival().name : "Đối thủ") + " kv: " + spr.getZone().zoneId;
            } else if (spr.getRivalId() == plId) {
                return "VS " + (spr.getPlayer() != null ? spr.getPlayer().name : "Đối thủ") + " kv: " + spr.getZone().zoneId;
            }
        }
        return "";
    }

    public void addSPR(SuperRank spr) {
        list.add(spr);
    }

    public void removeSPR(SuperRank spr) {
        list.remove(spr);
    }

    public void addWSPR(long playerId, long rivalId) {
        waitList.add(new WaitSuperRank(playerId, rivalId));
    }
}
