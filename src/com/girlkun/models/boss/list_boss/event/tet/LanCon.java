package com.girlkun.models.boss.list_boss.event.tet;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

public class LanCon extends Boss {

    private long lastTimeAtt;
    private long playerId;
    private boolean afk;

    public LanCon() throws Exception {
        super(BossID.LAN_CON, BossesData.LAN_CON);
    }
    
    @Override
    public void reward(Player plKill) {
    }

    @Override
    public void leaveMap() {
        if (this.playerId != -1 && this.playerId != 0) {
            Player pl = Client.gI().getPlayer(this.playerId);
            if (pl != null) {
                pl.canReward = false;
                pl.haveReward = false;
            }
        }
        super.leaveMap();
        this.playerId = -1;
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 250)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.location == null || pl.isDie()) {
                    return;
                }
                int dis = Util.getDistance(this, pl);
                if (dis > 450) {
                    moveTo(pl.location.x - 24, pl.location.y);
                } else if (dis > 100) {
                    int dir = (this.location.x - pl.location.x < 0 ? 1 : -1);
                    int move = Util.nextInt(50, 100);
                    moveTo(this.location.x + (dir == 1 ? move : -move), pl.location.y);
                } else {
                    if (Util.canDoWithTime(lastTimeAtt, 30000) && this.nPoint.hp < this.nPoint.hpMax) {
                        if (Util.isTrue(10, 100)) {
                            pl.injured(this, pl.nPoint.hpMax + 100000, true, false);
                            Service.gI().sendThongBao(pl, "Bạn đã bị Lân con húc chết!");
                        }
                        lastTimeAtt = System.currentTimeMillis();
                    }
                }
            } catch (Exception ex) {
            }
        }
    }



    public void afk() {
        if (Util.canDoWithTime(this.lastTimeAttack, 500)) {
            this.lastTimeAttack = System.currentTimeMillis();
            Player pl = Client.gI().getPlayer(playerId);
            if (pl == null || pl.zone == null || pl.isDie()) {
                this.leaveMap();
                return;
            }
            if (pl.haveReward) {
                pl.haveReward = false;
                this.leaveMap();
                return;
            }
            if (this.zone.equals(pl.zone)) {
                int dis = Util.getDistance(this, pl);
                if (dis <= 300) {
                    if (dis > 50) {
                        int dir = (this.location.x - pl.location.x < 0 ? 1 : -1);
                        int move = Util.nextInt(50, 100);
                        moveTo(this.location.x + (dir == 1 ? move : -move), pl.location.y);
                    }
                    afk = false;
                    pl.canReward = true;
                } else {
                    afk = true;
                    pl.canReward = false;
                }
            } else if (!afk) {
                ChangeMapService.gI().changeMap(this, pl.zone, pl.location.x + Util.nextInt(-10, 10), pl.location.y);
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        try {
            if (this.bossStatus == BossStatus.AFK) {
                this.afk();
            } else if (!this.isDie()) {
                if (this.zone != null && this.zone.getPlayers() != null) {
                    for (Player pl : this.zone.getPlayers()) {
                        if (pl != null && !pl.isDie() && isPlayerChatThang(pl)) {
                            this.changeToTypeNonPK();
                            this.playerId = Math.abs(pl.id);
                            Service.gI().chat(pl, "Đi thôi lân con!");
                            this.nPoint.hp = this.nPoint.hpMax;
                            this.changeStatus(BossStatus.AFK);
                            pl.lastChatMessage = ""; 
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(100, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            
            // Limit damage to avoid it dying too fast
            if (damage > 500_000) {
                damage = 500_000;
            }

            if (damage >= this.nPoint.hp || isPlayerChatThang(plAtt)) {
                this.changeToTypeNonPK();
                this.playerId = Math.abs(plAtt.id);
                Service.gI().chat(plAtt, "Đi thôi lân con!");
                this.nPoint.hp = this.nPoint.hpMax;
                this.changeStatus(BossStatus.AFK);
                return 0;
            }
            this.nPoint.subHP(damage);
            return damage;
        } else {
            return 0;
        }
    }

    private boolean isPlayerChatThang(Player plAtt) {   
        String lastChat = plAtt.lastChatMessage;  
        return lastChat != null && (lastChat.toLowerCase().contains("thắng") || lastChat.toLowerCase().contains("thang"));
    }
}
