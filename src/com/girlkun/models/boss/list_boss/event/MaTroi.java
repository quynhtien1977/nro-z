package com.girlkun.models.boss.list_boss.event;

import com.girlkun.models.boss.*;
import com.girlkun.models.skill.Skill;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

public class MaTroi extends Boss {

    public MaTroi() throws Exception {
        super(BossID.MATROI, BossesData.MA_TROI);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 585, Util.nextInt(1, 5), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }

    //halloween - apply outfit khi boss tan cong player
    private void halloween(Player pl) {
        if (pl != null && !pl.isDie()) {
            EffectSkillService.gI().setIsHalloween(pl, 2, 1800000); //outfit 2, 30 phut
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(10, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 3);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (plAtt != null && plAtt.playerSkill.skillSelect != null
                    && plAtt.playerSkill.skillSelect.template.id != Skill.TU_SAT
                    && plAtt.playerSkill.skillSelect.template.id != Skill.MA_PHONG_BA
                    && plAtt.playerSkill.skillSelect.template.id != Skill.LIEN_HOAN_CHUONG
                    && plAtt.playerSkill.skillSelect.template.id != Skill.SUPER_KAME
                    && plAtt.playerSkill.skillSelect.template.id != Skill.KAIOKEN) {
                if (damage > this.nPoint.hpMax / 20) {
                    damage = this.nPoint.hpMax / 20;
                }
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
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 500) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.nPoint.dame = pl.nPoint.hpMax / Util.nextInt(80, 120);
                this.playerSkill.skillSelect = this.playerSkill.skills
                        .get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    halloween(pl);
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void joinMap() {
        this.name = "Ma trơi " + Util.nextInt(10, 100);
        super.joinMap();
    }
}
