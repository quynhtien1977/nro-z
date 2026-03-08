package com.girlkun.models.boss.list_boss;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Util;

/**
 * Boss hộ tống - đi theo player đến map đích, thưởng ruby khi đến nơi
 */
public class DuongTank extends Boss {

    public DuongTank(int bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
    }

    public void setPlayerTarget(Player player) {
        this.playerTarger = player;
    }

    long lasttimemove;

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 1278, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.getInstance().dropItemMap(this.zone, it);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        // Player offline hoặc disconnect
        if (this.playerTarger == null || Client.gI().getPlayer(this.playerTarger.id) == null) {
            if (this.playerTarger != null) {
                playerTarger.haveDuongTang = false;
            }
            this.leaveMap();
            return;
        }
        // Đi quá xa -> boss biến mất
        if (this.zone == this.playerTarger.zone && Util.getDistance(playerTarger, this) > 500) {
            Service.gI().sendThongBao(this.playerTarger, "Đi quá xa, Boss hộ tống đã bị lạc!");
            playerTarger.haveDuongTang = false;
            this.leaveMap();
            return;
        }
        // Đổi khu vực (cùng map) -> boss biến mất
        if (this.zone != null && this.playerTarger.zone != null && this.zone.map.mapId == this.playerTarger.zone.map.mapId && this.zone.zoneId != this.playerTarger.zone.zoneId) {
            Service.gI().sendThongBao(this.playerTarger, "Hộ tống thất bại do bạn đã chuyển khu vực!");
            playerTarger.haveDuongTang = false;
            this.leaveMap();
            return;
        }
        // Cảnh báo khoảng cách
        if (this.zone == this.playerTarger.zone && Util.getDistance(playerTarger, this) > 300) {
            Service.gI().sendThongBao(this.playerTarger, "Khoảng cách quá xa, sắp mất Boss hộ tống!");
        }
        // Di chuyển theo player
        if (this.playerTarger != null && Util.getDistance(playerTarger, this) <= 300) {
            int dir = this.location.x - this.playerTarger.location.x <= 0 ? -1 : 1;
            if (Util.canDoWithTime(lasttimemove, 1000)) {
                lasttimemove = System.currentTimeMillis();
                this.moveTo(this.playerTarger.location.x + Util.nextInt(dir == -1 ? 0 : -30, dir == -1 ? 10 : 0),
                        this.playerTarger.location.y);
            }
        }
        // Đến map đích -> thưởng
        if (this.playerTarger != null && playerTarger.haveDuongTang && this.zone.map.mapId == playerTarger.mapCongDuc) {
            playerTarger.haveDuongTang = false;
            playerTarger.inventory.ruby += 3000;
            Service.getInstance().sendMoney(playerTarger);
            Service.getInstance().sendThongBaoOK(playerTarger, "Bạn nhận được 3K hồng ngọc từ hộ tống thành công!");
            this.leaveMap();
        }
        // Follow khi đổi map
        if (this.playerTarger != null && this.zone != null && this.zone.map.mapId != this.playerTarger.zone.map.mapId) {
            ChangeMapService.gI().changeMap(this, this.playerTarger.zone, this.playerTarger.location.x,
                    this.playerTarger.location.y);
        }
        // Chat hướng dẫn
        if (Util.canDoWithTime(this.lastTimeAttack, 10000)) {
            Service.gI().chat(this, playerTarger.name + ", Hãy đưa ta đến "
                    + MapService.gI().getMapById(playerTarger.mapCongDuc).mapName);
            this.lastTimeAttack = System.currentTimeMillis();
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            Service.getInstance().sendFlagBag(this);
            this.notifyJoinMap();
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
