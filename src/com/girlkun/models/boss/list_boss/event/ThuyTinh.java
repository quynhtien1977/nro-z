package com.girlkun.models.boss.list_boss.event;

import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.*;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.utils.Util;

public class ThuyTinh extends Boss {

    private long lastTimeMove;
    private int timeMove;

    public ThuyTinh() throws Exception {
        super(BossID.THUY_TINH, BossesData.THUY_TINH);
    }

    @Override
    public void reward(Player plKill) {
        if (this.zone != null) {
            ItemMap it = new ItemMap(this.zone, 422, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it.options.add(new Item.ItemOption(77, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(103, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(50, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(94, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(14, Util.nextInt(2, 40)));
            it.options.add(new Item.ItemOption(106, Util.nextInt(2, 40)));
            it.options.add(new Item.ItemOption(154, 0));
            if (Util.isTrue(1, 100)) {
                // Vĩnh viễn (không thêm option 93)
            } else if (Util.isTrue(5, 100)) {
                it.options.add(new Item.ItemOption(93, 30));
            } else {
                it.options.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
            }
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (!piercing && damage > 1000000) {
                damage = Util.nextInt(900000, 1000000);
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
    public void die(Player plAtt) {
        if (this.zone != null) {
            this.zone.lastTimeSonTinhThuyTinhDie = System.currentTimeMillis();
            for (Player pl : this.zone.getNotBosses()) {
                if (pl != null && !pl.isDie()) {
                    com.girlkun.services.ItemTimeService.gI().sendItemTime(pl, 4671, (int)(com.girlkun.models.map.Zone.TIME_SON_TINH_THUY_TINH_EVENT / 1000));
                    com.girlkun.services.Service.gI().sendThongBao(pl, "Thủy Tinh đã bị đánh bại! Bạn có thể farm Dưa Hấu tại khu vực này trong " + (com.girlkun.models.map.Zone.TIME_SON_TINH_THUY_TINH_EVENT / 60000) + " phút.");
                }
            }
        }
        super.die(plAtt);
    }

    @Override
    public void joinMap() {
        super.joinMap();
        Service.gI().changeFlag(this, 2);
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarger != null && (this.playerTarger.isDie() || !this.zone.equals(this.playerTarger.zone))) {
            this.playerTarger = null;
        }
        if (this.playerTarger != null && this.playerTarger.effectSkin != null && this.playerTarger.effectSkin.isVoHinh) {
            this.playerTarger = null;
        }
        if (this.playerTarger == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            Player newTarget = null;
            int count = 0;
            for (Player pl : this.zone.getNotBosses()) {
                if (pl != null && !pl.isDie() && (pl.effectSkin == null || !pl.effectSkin.isVoHinh) && pl.cFlag != 0 && pl.cFlag != 8 && pl.cFlag != this.cFlag) {
                    count++;
                    if (Util.nextInt(count) == 0) {
                        newTarget = pl;
                    }
                }
            }
            for (Player pl : this.zone.getBosses()) {
                if (pl != null && !pl.equals(this) && !pl.isDie() && pl.cFlag == 1) {
                    count++;
                    if (Util.nextInt(count) == 0) {
                        newTarget = pl;
                    }
                }
            }
            this.playerTarger = newTarget;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        return this.playerTarger;
    }

    @Override
    public void active() {
        this.attack();
    }

    @Override
    public void attack() {
        if (this.effectSkill.isCharging) {
            return;
        }
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    if (Util.canDoWithTime(lastTimeMove, timeMove)) {
                        Player plRand = super.getPlayerAttack();
                        if (plRand != null) {
                            this.moveToPlayer(plRand);
                            this.lastTimeMove = System.currentTimeMillis();
                            this.timeMove = Util.nextInt(5000, 30000);
                        }
                    }
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                int dis = Util.getDistance(this, pl);
                if (dis > 450) {
                    moveTo(pl.location.x - 24, pl.location.y);
                } else if (dis > 100) {
                    int dir = (this.location.x - pl.location.x < 0 ? 1 : -1);
                    int move = Util.nextInt(50, 100);
                    moveTo(this.location.x + (dir == 1 ? move : -move), pl.location.y);
                } else {
                    if (Util.isTrue(30, 100)) {
                        int move = Util.nextInt(50);
                        moveTo(pl.location.x + (Util.nextInt(0, 1) == 1 ? move : -move), this.location.y);
                    }
                    if (pl.isPl()) {
                        this.nPoint.dame = pl.nPoint.hpMax / 30;
                    } else {
                        this.nPoint.dame = 10000;
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                }
            } catch (Exception ex) {
            }
        }
    }
}
