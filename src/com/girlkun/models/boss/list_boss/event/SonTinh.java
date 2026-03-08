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

public class SonTinh extends Boss {

    private long lastTimeMove;
    private int timeMove;

    public SonTinh() throws Exception {
        super(BossID.SON_TINH, BossesData.SON_TINH);
    }

    @Override
    public void reward(Player plKill) {
        if (this.zone != null) {
            ItemMap it = new ItemMap(this.zone, 421, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            it.options.add(new Item.ItemOption(77, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(103, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(50, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(94, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(95, Util.nextInt(20, 50)));
            it.options.add(new Item.ItemOption(108, Util.nextInt(2, 40)));
            if (Util.isTrue(1, 15)) {
                it.options.add(new Item.ItemOption(116, 0));
            }
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
                    com.girlkun.services.ItemTimeService.gI().sendItemTime(pl, 4671, 600);
                    com.girlkun.services.Service.gI().sendThongBao(pl, "Sơn Tinh đã bị đánh bại! Bạn có thể farm Dưa Hấu tại khu vực này trong 10 phút.");
                }
            }
        }
        super.die(plAtt);
    }

    @Override
    public void active() {
        if (this.typePk == com.girlkun.consts.ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
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
