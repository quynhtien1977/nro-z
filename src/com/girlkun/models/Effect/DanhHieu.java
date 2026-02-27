/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.Effect;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class DanhHieu {

    public static boolean FindDanhhieu(Player pl , int iddanhhieu)
    {
        for(Item item : pl.ListDanhHieu)
        {
            if(item.isNotNullItem() && item.template.id == iddanhhieu)
            {
                return true;
            }
        }
        return false;
    }
    public static void AddDanhHieu(Player pl, Item iddanhhieu) 
    {
        if (!pl.ListDanhHieu.contains(iddanhhieu)) 
        {
            pl.ListDanhHieu.add(iddanhhieu);
            if (pl.ListDanhHieu.size() > 5)
            {
                pl.ListDanhHieu.remove(0);
            }
        }
        else 
        {
            pl.ListDanhHieu.remove(iddanhhieu);
        }
        EffectService.SendEffChartoMap(pl);
    }
    public static int GetImgDanhHieu(Item item)
    {
        switch (item.template.id) {
            case 1266:
                return 21950;
            case 1267:
                return 21951;
            case 1268:
                return 21952;
            case 1269:
                return 21953;
            case 1270:
                return 21954;
            case 1271:
                return 21955;
            case 1272:
                return 21956;
            case 1273:
                return 21957;
            case 1274:
                return 21959;   
            case 1275:
                return 21960;
            case 1276:
                return 21961;
            case 1277:
                return 21962;
            case 1323:
                return 21998;
        }
        return -1;
    }
}
