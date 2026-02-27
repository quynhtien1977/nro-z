/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.Effect;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;

/**
 *
 * @author Administrator
 */
public class VongChan {

    public static void CheckVongChan(Player pl, Item item, int z) {
        if (pl == null || item == null) {
            return;
        }
        switch (item.template.id) {
            case 1962:
            case 1963:
            case 1964:
            case 1242:
                pl.idVongChan = 21912;
                break;
            case 1243:
                pl.idVongChan = 21913;
                break;
            case 1244:
                pl.idVongChan = 21914;
                break;
            case 1245:
                pl.idVongChan = 21915;
                break;
            case 1246:
                pl.idVongChan = 21916;
                break;
            case 1247:
                pl.idVongChan = 21917;
                break;
            case 1248:
                pl.idVongChan = 21918;
                break;
        }
        EffectService.SendEffChartoMap(pl);
    }

    public static void CheckVongChan2(Player pl, Item item, int z) {
        if (pl == null || item == null) {
            return;
        }
        switch (item.template.id) {
            case 1249:
                pl.idVongChan2 = 21912;
                break;
            case 1250:
                pl.idVongChan2 = 21913;
                break;
            case 1251:
                pl.idVongChan2 = 21914;
                break;
            case 1252:
                pl.idVongChan2 = 21915;
                break;
            case 1253:
                pl.idVongChan2 = 21916;
                break;
            case 1254:
                pl.idVongChan2 = 21917;
                break;
            case 1255:
                pl.idVongChan2 = 21918;
                break;
        }
        EffectService.SendEffChartoMap(pl);
    }
}
