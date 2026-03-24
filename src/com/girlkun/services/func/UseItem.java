package com.girlkun.services.func;

import com.arriety.card.Card;
import com.arriety.card.RadarCard;
import com.arriety.card.RadarService;
import com.girlkun.consts.ConstMap;
import com.girlkun.models.item.Item;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.Effect.DanhHieu;
import com.girlkun.models.Effect.EffectChar;
import com.girlkun.models.Effect.EffectService;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.item.Item.ItemOption;
import com.girlkun.models.map.Zone;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.player.Inventory;
import com.girlkun.services.NpcService;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.server.Manager;
import com.girlkun.utils.SkillUtil;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import com.girlkun.server.io.MySession;
import com.girlkun.services.ItemService;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.PetService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.TaskService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
//import com.girlkun.services.NgocRongNamecService;
import com.girlkun.services.RewardService;
import com.girlkun.services.SkillService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void doItem(Player player, Message _msg) {

        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học "
                                            + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        if (item != null && item.template != null) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:

                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 33:
                    UseCard(pl, item);
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag
                case 36:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    if (item.template.id == 1241) {
                        pl.ListEffect.add(new EffectChar(83, 0, -1, 10, 1));
                        EffectService.SendEffChartoMap(pl);
                    } else {
                        EffectService.RemoveEff(pl, 83);
                        EffectService.SendEffChartoMap(pl);
                    }
                    Service.gI().sendFlagBag(pl);
                    break;
                case 72:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                case 37:
                case 74:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFoot(pl, item.template.id);
                    break;
                case 39:
                    Service.gI().removeTitle(pl);
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 21:
                    if (pl.newpet != null) {
                        ChangeMapService.gI().exitMap(pl.newpet);
                        pl.newpet.dispose();
                        pl.newpet = null;
                    }
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);

                    PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg, item.template.name);
                    Service.getInstance().point(pl);
                    break;
                default:
                    switch (item.template.id) {
                        case 1110:
                            if (InventoryServiceNew.gI().findItem(pl.inventory.itemsBag, 1110).isNotNullItem()) {
                                pl.tickxanh = true;
                                Service.gI().point(pl);
                                Service.gI().sendMoney(pl);
                            }
                            break;
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 5;
                            Service.gI().Transport(pl);
                            break;
//                        case 1999:
//                            pl.type = 2;
//                            pl.maxTime = 5;
//                            Service.gI().Transport(pl);
//                            break;
                        case 361:
                            maydoboss(pl);
                            break;
                        case 293:
                            openGoiDau1(pl, item);
                            break;
                        case 294:
                            openGoiDau2(pl, item);
                            break;
                        case 295:
                            openGoiDau3(pl, item);
                            break;
                        case 296:
                            openGoiDau4(pl, item);
                            break;
                        case 297:
                            openGoiDau5(pl, item);
                            break;
                        case 298:
                            openGoiDau6(pl, item);
                            break;
                        case 299:
                            openGoiDau7(pl, item);
                            break;
                        case 596:
                            openGoiDau8(pl, item);
                            break;
                        case 597:
                            openGoiDau9(pl, item);
                            break;
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                        case 1980://hop qua skh, item 2002 xd
                            UseItem.gI().Hophdt(pl, item);
                            break;
//                        case 1979://hop qua skh, item 2002 xd
//                            pl.cauca.StartCauCa();
//                            break;
                        case 1987://hop qua skh, item 2002 xd
                            UseItem.gI().Hophd(pl, item);
                            break;
                        case 1986://hop qua skh, item 2002 xd
                            UseItem.gI().Hoptl(pl, item);
                            break;
                        case 1985://hop qua skh, item 2002 xd
                            UseItem.gI().Hoptst(pl, item);
                            break;
                        case 1376:
                            changePetDaiViet(pl, item);
                            break;
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 5) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.gI().sendThongBao(pl, "Đặt ít vệ tinh thôi");
                            }
                            break;
                        case 457:
                            Input.gI().createFormUseGold(pl);
                            break;
                        case 568: //quả trứng
                            if (pl.mabuEgg == null) {
                                MabuEgg.createMabuEgg(pl);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                if (pl.zone.map.mapId == (21 + pl.gender)) {
                                    if (pl.mabuEgg != null) {
                                        pl.mabuEgg.sendMabuEgg();
                                    }
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Bạn đã có quả trứng nên không thể sử dụng");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 574: //cskb
                            openCSKBDB(pl, item);
                            break;
                        case 397: //cskb
                            openHOPQUATET(pl, item);
                            break;
                        case 628:
                            openPhieuCaiTrangHaiTac(pl, item);
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                        case 2037: //máy dò cosmos
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                        case 1978:
                        case 1233:
                        case 1234:
                        case 1235:
                        case 752: // bánh tét
                        case 753: //bánh chưng
                        case 1045: //Đuôi khỉ
                            useItemTime(pl, item);
                            break;
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 1278:
                            RuongItemCap2(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1423: //đổi đệ tử
                            changePetGoku(pl, item);
                            break;    
//                        case 1108: //đổi đệ tử
//                            changeUubPet(pl, item);
//                            break;
                        case 722: //đổi đệ tử
                            changePetPic(pl, item);
                            break;
                              case 984: //Capsule hồng ngọc
                            int radomsoluonghongngoc = Util.nextInt(50, 135);
                            pl.inventory.ruby += radomsoluonghongngoc;
                            Service.gI().sendMoney(pl);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + radomsoluonghongngoc + " hồng ngọc");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;
                        case 2065: //đổi đệ tử
                            changeKaido(pl, item);
                            break;
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 921: //bông tai c2
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1405:
                        case 1155:
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1156:
                        case 1406:
                            UseItem.gI().usePorata4(pl);
                            break;

                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;
                        //  case 1105://hop qua skh, item 2002 xd
                        //  UseItem.gI().Hopts(pl, item);
                        //      break;
                        case 1997://hop qua skh, item 2002 xd
                        case 818: // Capsule Halloween
                            Openhopct(pl, item);
                            break;
                        case 1998://hop qua skh, item 2002 xd
                            Openhopflagbag(pl, item);
                            break;
                        case 1999://hop qua skh, item 2002 xd
                            Openhoppet(pl, item);
                            break;

                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:
                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 987:
                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 1098:
                            useItemHopQuaTanThu(pl, item);
                            break;
                        case 2078:
                            UseItem.gI().hopquagiangsinh(pl);
                            break;
                        case 648: // Hộp quà giáng sinh (Noel)
                            UseItem.gI().NoelItemBox(pl);
                            break;
                        case 1128:
                            openDaBaoVe(pl, item);
                            break;
                        case 1129:
                            openSPL(pl, item);
                            break;
                        case 1130:
                            openDaNangCap(pl, item);
                            break;
                        case 1417:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(0).skillId != -1) {
                                pl.pet.openSkill1();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 1 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 1 chứ!");
                            }
                            break;        
                        case 1418:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                pl.pet.openSkill2();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;    
                        case 1419:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                            }
                            break; 
                        case 1420:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(3).skillId != -1) {
                                pl.pet.openSkill4();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 4 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");
                            }
                            break;    
                        case 1131:
//                            openManhTS(pl, item);
                            if (pl.pet != null) {
                                if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                                    pl.pet.openSkill2();
                                    if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                        pl.pet.openSkill3();
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                } else {
                                    Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                    return;
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                return;
                            }
                            break;
                        case 1132:

                            SkillService.gI().learSkillSpecial(pl, Skill.SUPER_KAME);
                            break;
                        case 1133:
                            SkillService.gI().learSkillSpecial(pl, Skill.MA_PHONG_BA);
                            break;
                        case 1134:
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryServiceNew.gI().sendItemBags(pl);
                            SkillService.gI().learSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
                            break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 999:
                            if (pl.pet == null) {
                                Service.gI().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
                                break;
                            }

                            if (pl.pet.playerSkill.skills.get(1).skillId != -1 && pl.pet.playerSkill.skills.get(2).skillId != -1) {
                                pl.pet.openSkill2();
                                pl.pet.openSkill3();
                                InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Đã đổi thành công chiêu 2 3 đệ tử");
                            } else {
                                Service.gI().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                            }
                            break;
                        case 1379:
                            // int itemId3 = 224;
                            // Item PhanQua3 = ItemService.gI().createNewItem(((short) itemId3));
                            // PhanQua3.quantity = 9999;
                            // int itemId4 = 223;
                            // Item PhanQua4 = ItemService.gI().createNewItem(((short) itemId4));
                            // PhanQua4.quantity = 9999;
                            // int itemId5 = 222;
                            // Item PhanQua5 = ItemService.gI().createNewItem(((short) itemId5));
                            // PhanQua5.quantity = 9999;
                            // int itemId6 = 221;
                            // Item PhanQua6 = ItemService.gI().createNewItem(((short) itemId6));
                            // PhanQua6.quantity = 9999;
                            // int itemId7 = 220;
                            // Item PhanQua7 = ItemService.gI().createNewItem(((short) itemId7));
                            // PhanQua7.quantity = 9999;
                            // int itemId8 = 441;
                            // Item PhanQua8 = ItemService.gI().createNewItem(((short) itemId8));
                            // PhanQua8.itemOptions.add(new Item.ItemOption(95, 5));
                            // PhanQua8.quantity = 99;
                            // int itemId9 = 442;
                            // Item PhanQua9 = ItemService.gI().createNewItem(((short) itemId9));
                            // PhanQua9.itemOptions.add(new Item.ItemOption(96, 5));
                            // PhanQua9.quantity = 99;
                            // int itemId10 = 443;
                            // Item PhanQua10 = ItemService.gI().createNewItem(((short) itemId10));
                            // PhanQua10.itemOptions.add(new Item.ItemOption(97, 5));
                            // PhanQua10.quantity = 99;
                            // int itemId11 = 444;
                            // Item PhanQua11 = ItemService.gI().createNewItem(((short) itemId11));
                            // PhanQua11.itemOptions.add(new Item.ItemOption(98, 3));
                            // PhanQua11.quantity = 99;
                            // int itemId12 = 445;
                            // Item PhanQua12 = ItemService.gI().createNewItem(((short) itemId12));
                            // PhanQua12.itemOptions.add(new Item.ItemOption(99, 3));
                            // PhanQua12.quantity = 99;
                            // int itemId13 = 446;
                            // Item PhanQua13 = ItemService.gI().createNewItem(((short) itemId13));
                            // PhanQua13.itemOptions.add(new Item.ItemOption(100, 5));
                            // PhanQua13.quantity = 99;
                            // int itemId14 = 447;
                            // Item PhanQua14 = ItemService.gI().createNewItem(((short) itemId14));
                            // PhanQua14.itemOptions.add(new Item.ItemOption(101, 5));
                            // PhanQua14.quantity = 99;
                            // int itemId15 = 457;
                            // Item PhanQua15 = ItemService.gI().createNewItem(((short) itemId15));
                            // PhanQua15.itemOptions.add(new Item.ItemOption(30, 0));
                            // PhanQua15.quantity = 1000;
                            // if (pl.gender == 0) {
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua3.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua4.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua6.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua11.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua12.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua13.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua14.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua15.Name());

                                
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua3);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua4);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua6);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua11);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua12);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua13);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua14);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua15);
                            //     InventoryServiceNew.gI().sendItemBags(pl);
                            // }
                            // if (pl.gender == 1) {
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua3.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua4.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua6.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua11.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua12.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua13.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua14.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua15.Name());

                                
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua3);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua4);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua6);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua11);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua12);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua13);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua14);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua15);
                            //     InventoryServiceNew.gI().sendItemBags(pl);
                            // }
                            // if (pl.gender == 2) {
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua3.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua4.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua5.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua6.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua7.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua8.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua9.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua10.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua11.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua12.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua13.Name());
                            //     Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua14.Name());
                                // Service.gI().sendThongBao(pl, "Bạn nhận được " + PhanQua15.Name());

                               
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua3);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua4);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua5);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua6);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua7);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua8);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua9);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua10);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua11);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua12);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua13);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua14);
                            //     InventoryServiceNew.gI().addItemBag(pl, PhanQua15);
                            //     InventoryServiceNew.gI().sendItemBags(pl);
                            // }
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            }
                            else{
                                // áo, quần, rada, găng, giày
                                int[][] listClothesReward = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
                                for (int idCloth : listClothesReward[pl.gender])
                                {
                                    Item clotheReward = ItemService.gI().createNewItem((short) idCloth);
                                    RewardService.gI().initBaseOptionClothes(clotheReward.template.id, clotheReward.template.type, clotheReward.itemOptions);
                                    // ép 3 sao
                                    clotheReward.itemOptions.add(new Item.ItemOption(107, 3));
                                    InventoryServiceNew.gI().addItemBag(pl, clotheReward);
                                }
                                Item itemReward = ItemService.gI().createNewItem((short) 447);
                                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                                itemReward.quantity = 15;
                                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                                Service.gI().sendThongBao(pl, "Bạn nhận được 1 set đồ 3 sao tnsm!");
                                InventoryServiceNew.gI().sendItemBags(pl);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            }
                            break;
                        case 2027:
                        case 2028: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(2, 10)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(2, 5)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(2, 5)));
                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(1, 3)));
                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(1, 3)));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                            }
                            break;

                        }
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void NoelItemBox(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 0) {
                Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống");
                return;
            }
            Item item = InventoryServiceNew.gI().findItemBag(player, 648);
            if (item != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                int randomOption = Util.nextInt(1, 100);
                if (randomOption <= 20) { 
                    Item saoPhaLe = ItemService.gI().createNewItem((short) Util.nextInt(441, 447)); // Sao pha le
                    saoPhaLe.quantity = 1;
                    InventoryServiceNew.gI().addItemBag(player, saoPhaLe);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + saoPhaLe.template.name);
                } else if (randomOption <= 40) { 
                    Item daNangCap = ItemService.gI().createNewItem((short) Util.nextInt(220, 224)); // Đá nâng cấp
                    daNangCap.quantity = 1;
                    InventoryServiceNew.gI().addItemBag(player, daNangCap);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + daNangCap.template.name);
                } else if (randomOption <= 50) { 
                    Item ngocRong = ItemService.gI().createNewItem((short) Util.nextInt(17, 20)); // Ngọc rồng 4-7 sao
                    ngocRong.quantity = 1;
                    InventoryServiceNew.gI().addItemBag(player, ngocRong);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + ngocRong.template.name);
                } else if (randomOption <= 60) { 
                    Item caiTrang = ItemService.gI().createNewItem((short) Util.nextInt(740, 741)); // Cải trang noel mũ đỏ/xanh
                    caiTrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20))); // Sức đánh
                    caiTrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20))); // HP
                    caiTrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20))); // KI
                    caiTrang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3))); // HSD 1-3 ngay
                    InventoryServiceNew.gI().addItemBag(player, caiTrang);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + caiTrang.template.name);
                } else if (randomOption <= 80) { 
                    Item xeTruotTuyet = ItemService.gI().createNewItem((short) 742); // Thú cưỡi tuần lộc/xe trượt tuyết
                    xeTruotTuyet.itemOptions.add(new ItemOption(50, 10)); // Sức đánh
                    xeTruotTuyet.itemOptions.add(new ItemOption(77, 10)); // HP
                    xeTruotTuyet.itemOptions.add(new ItemOption(103, 10)); // KI
                    xeTruotTuyet.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7))); // HSD 1-7 ngay
                    InventoryServiceNew.gI().addItemBag(player, xeTruotTuyet);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + xeTruotTuyet.template.name);
                } else { 
                    int ruby = Util.nextInt(100, 500);
                    player.inventory.ruby += ruby;
                    Service.gI().sendMoney(player);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + ruby + " Hồng ngọc");
                }
                InventoryServiceNew.gI().sendItemBags(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fixPetBTH(Player pl) {
        if (pl.newpet != null) {
            ChangeMapService.gI().exitMap(pl.newpet);
            pl.newpet.dispose();
            pl.newpet = null;
        }
    }

    private void Openhopct(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            boolean vinhvien = Manager.TotalCaiTrang >= 500;
            int[] rdct = new int[]{1290, 1291, 1281, 1302, 1282, 1296, 1297, 1298, 1295, 1301, 1300, 1307, 1306, 1308, 1309, 1310};
            int[] rdop = new int[]{5, 14, 94, 108, 97};
            int randomct = new Random().nextInt(rdct.length);
            int randomop = new Random().nextInt(rdop.length);
            Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 27)));
                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
                Manager.TotalCaiTrang += 1;
            } else {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 27)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                Manager.TotalCaiTrang = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhopflagbag(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            boolean vinhvien = Manager.TotalFlag >= 500;
            int[] rdfl = new int[]{1157, 1203, 1204, 1205, 1206, 1207, 954, 955, 1220, 1221, 966, 1222, 1226, 1228,
                1229, 467, 468, 469, 470, 982, 471, 983, 994, 995, 740, 996, 741, 997, 998, 999, 1000, 745,
                1001, 1007, 2035, 1013, 1021, 766, 1022, 767, 1023};
            int[] rdop = new int[]{50, 77, 103};
            int[] daysrandom = new int[]{3, 7, 15, 30};
            int randomfl = new Random().nextInt(rdfl.length);
            int randomop = new Random().nextInt(rdop.length);
            Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
                fl.itemOptions.add(new Item.ItemOption(93, daysrandom[Util.nextInt(daysrandom.length)]));
                Manager.TotalFlag += 1;
            } else {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 10)));
                Manager.TotalFlag = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, fl);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhoppet(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int[] rdpet = new int[]{1311, 1312, 1313};
            int[] rdop = new int[]{50, 77, 103};
            int[] daysrandom = new int[]{3, 7, 15, 30};
            boolean vinhvien = Manager.TotalPet >= 500;
            int randompet = new Random().nextInt(rdpet.length);
            int randomop = new Random().nextInt(rdop.length);
            Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
            Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16, 16));
            if (!vinhvien) {
                pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(8, 13)));
                pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 12)));
                pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 14)));
                pet.itemOptions.add(new Item.ItemOption(93, daysrandom[Util.nextInt(daysrandom.length)]));
                Manager.TotalPet += 1;
            } else {
                pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(8, 13)));
                pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 12)));
                pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 14)));
                Manager.TotalPet = 0;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, pet);
            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name + " và " + vt.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id).findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ " + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1202: //Hào quang
                if (!player.effectFlagBag.useHaoQuang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHaoQuang = !player.effectFlagBag.useHaoQuang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện cho đệ tử !");
        }
    }
    private void changePetGoku(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện cho đệ tử !");
        }
    }

//    private void changeUubPet(Player player, Item item) {
//        if (player.pet != null) {
//            int gender = player.pet.gender;
//            PetService.gI().changeUubPet(player, gender);
//            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//        } else {
//            Service.gI().sendThongBao(player, "Không thể thực hiện");
//        }
//    }
    private void changePetPic(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeXencon(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changePetDaiViet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void changeKaido(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeKaidoPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void openDaBaoVe(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {987, 987};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openSPL(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {441, 442, 443, 444, 445, 446, 447};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openDaNangCap(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {220, 221, 222, 223, 224};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openManhTS(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {1066, 1067, 1068, 1069, 1070};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau1(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {13, 13};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau2(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {60, 60};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau3(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {61, 61};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau4(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {62, 62};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau5(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {63, 63};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau6(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {64, 64};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau7(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {65, 65};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau8(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {352, 352};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau9(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {523, 523};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = (short) Util.nextInt(99, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCSKBDB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {861, 861, 861, 861, 381, 382, 383, 384, 385, 457, 457, 818, 729, 730, 731, 732, 742, 806, 819, 862, 863, 864, 883, 884, 898, 904, 905, 906, 907, 911, 985};
            int[][] gold = {{15000, 150000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.ruby += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.ruby > Inventory.LIMIT_GOLD) {
                    pl.inventory.ruby = (int) Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 7743;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0)); // khoa gd
                if (it.template.id == 457) {
                    it.quantity = Util.nextInt(2, 20);
                } else if (it.template.type == 5) { // cai trang
                    it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 32))); // sd
                    it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 45))); // hp
                    it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 45))); // ki
                    if (Util.isTrue(8, 10)) { // 80% 7 ngay
                        it.itemOptions.add(new ItemOption(93, 7));
                    }
                }
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openHOPQUATET(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {574, 399, 574, 717, 574, 717, 574, 717, 399, 574, 717, 574, 18, 717, 399, 717, 574, 574};
            int[][] gold = {{1000000, 20000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openThoiVang(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            pl.inventory.gold += 500000000;
            Service.gI().sendThongBao(pl, "Bạn vừa dùng thỏi vàng và nhận được 500tr vàng");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {383, 385, 385, 385, 16, 190, 382, 383, 385, 385, 385, 382, 382, 382, 381, 382, 381, 384, 381, 383, 385, 385, 385};
            int[][] gold = {{100000, 200000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public boolean maydoboss(Player pl) {
        try {
            BossManager.gI().dobossmember(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private void hopquagiangsinh(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 2) {
                Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            Item hopquagiangsinh = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2078) {
                    hopquagiangsinh = item;
                    break;
                }
            }
            if (hopquagiangsinh != null) {
                Item gaudau = ItemService.gI().createNewItem((short) 2077);
                gaudau.itemOptions.add(new ItemOption(147, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 30)));
                gaudau.itemOptions.add(new Item.ItemOption(101, Util.nextInt(2, 25)));
                gaudau.itemOptions.add(new Item.ItemOption(211, 0));
                gaudau.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquagiangsinh, 1);
                InventoryServiceNew.gI().addItemBag(pl, gaudau);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + gaudau.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void useItemHopQuaTanThu(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 18, 19, 20};
            int[][] gold = {{10000, 50000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 1233: //x3EXP
                pl.itemTime.lastX3EXP = System.currentTimeMillis();
                pl.itemTime.isX3EXP = true;
                break;
            case 1234: //x5EXP
                pl.itemTime.lastX5EXP = System.currentTimeMillis();
                pl.itemTime.isX5EXP = true;
                break;
            case 1235: //x7EXP
                pl.itemTime.lastX7EXP = System.currentTimeMillis();
                pl.itemTime.isX7EXP = true;
                break;
            case 1978: //x2EXP
                pl.itemTime.lastX2EXP = System.currentTimeMillis();
                pl.itemTime.isX2EXP = true;
                break;
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyet2 == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ huyết 2");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhi2 == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ khí 2");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNo2 == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng cuồng nộ 2");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1099:// cn
                if (pl.itemTime.isUseCuongNo == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng cuồng nộ");
                    return;
                }
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                Service.gI().point(pl);
                break;
            case 1100:// bo huyet
                if (pl.itemTime.isUseBoHuyet == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ huyết");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                break;
            case 1101://bo khi
                if (pl.itemTime.isUseBoKhi == true) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng bổ khí");
                    return;
                }
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                break;
            case 1102://xbh
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                break;
            case 1103://an danh
                pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh2 = true;
                break;
            case 638://Bình chứa comeson
                pl.itemTime.lastdaiviet = System.currentTimeMillis();
                pl.itemTime.isdaiviet = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 2037: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
            case 752: //bánh tét
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet= true;
                break;
            case 753: //bánh chưng
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung= true;
                break;
            case 1045: //Đuôi Khỉ
                if (pl.itemTime.isUseDK) {
                    Service.getInstance().sendThongBao(pl, "Bạn đang sử dụng đuôi khỉ");
                    return;
                }
                pl.itemTime.lastTimeUseDK = System.currentTimeMillis();
                pl.itemTime.isUseDK = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                case SummonDragon.NGOC_RONG_2_SAO:
                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.gI().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8
                || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8
                || pl.fusion.typeFusion == 10) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index >= 0 && index < pl.mapCapsule.size()) {
            // if (index >= pl.mapCapsule.size()) {
            //     Service.gI().sendThongBao(pl, "Có lỗi xãy ra!");
            //     return;
            // }
            Zone zoneChose = pl.mapCapsule.get(index);
            //Kiểm tra số lượng người trong khu

            if (zoneChose.getNumOfPlayers() > 25
                    || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                    || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                    || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                    || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
                Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
                return;
            }
            if (index != 0 || zoneChose.map.mapId == 21
                    || zoneChose.map.mapId == 22
                    || zoneChose.map.mapId == 23) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
                pl.mapBeforeCapsule = null;
            }
            ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 10000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp((long) player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp((long) player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp((long) (player.pet.nPoint.hp + hpKiHoiPhuc));
                player.pet.nPoint.setMp((long) (player.pet.nPoint.mp + hpKiHoiPhuc));
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.gI().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;

            }

        } catch (Exception e) {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
            e.printStackTrace();
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//hop qua do thien su
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hoptst(Player pl, Item item) {//hop qua do thien su thuong
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hophdt(Player pl, Item item) {//hop qua do huy diet top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hophd(Player pl, Item item) {//hop qua do huy diet top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hoptl(Player pl, Item item) {//hop qua do tl top
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    // private void GetRubyFormWoodChest(Player pl, Item item) {
    //     int level = 0;
    //     for (ItemOption op : item.itemOptions) {
    //         if (op.optionTemplate.id == 72) {
    //             level = op.param;
    //             break;
    //         }
    //     }
    //     int HongNgoc = 0;
    //     switch (level) {
    //         case 1:
    //         case 2:
    //         case 3:
    //         case 4:
    //             HongNgoc = Util.nextInt(1, 1000);
    //             break;
    //         case 5:
    //         case 6:
    //             HongNgoc = Util.nextInt(500, 1000);
    //             break;
    //         case 7:
    //             HongNgoc = Util.nextInt(1000, 1500);
    //             break;
    //         case 8:
    //             HongNgoc = Util.nextInt(2000, 15000);
    //             break;
    //         case 9:
    //             HongNgoc = Util.nextInt(2000, 15000);
    //             break;
    //         case 10:
    //             HongNgoc = Util.nextInt(2000, 15000);
    //             break;
    //         case 11:
    //             HongNgoc = Util.nextInt(2000, 15000);
    //             break;
    //     }
    //     pl.inventory.ruby += HongNgoc;
    //     Service.gI().sendMoney(pl);
    //     Service.getInstance().sendThongBao(pl, "Bạn nhận được " + HongNgoc + " hồng ngọc\b");
    // }

    private int GetRubyFormWoodChest(Player pl, Item item) {
        int level = 0;
        for (ItemOption op : item.itemOptions) {
            if (op.optionTemplate.id == 72) {
                level = op.param;
                break;
            }
        }
        int HongNgoc = 0;
        switch (level) {
            case 1:
            case 2:
            case 3:
            case 4:
                HongNgoc = Util.nextInt(1, 100);
                break;
            case 5:
            case 6:
                HongNgoc = Util.nextInt(1, 1000);
                break;
            case 7:
                HongNgoc = Util.nextInt(100, 1000);
                break;
            case 8:
                HongNgoc = Util.nextInt(200, 1000);
                break;
            case 9:
                HongNgoc = Util.nextInt(200, 1500);
                break;
            case 10:
                HongNgoc = Util.nextInt(200, 2000);
                break;
            case 11:
                HongNgoc = Util.nextInt(500, 2000);
                break;
        }
        pl.inventory.ruby += HongNgoc;
        Service.gI().sendMoney(pl);
        // Service.getInstance().sendThongBao(pl, "Bạn nhận được " + HongNgoc + " hồng ngọc\b");
        return HongNgoc;
    }

    private void openWoodChest(Player pl, Item item) {
        List<String> infoItem = new ArrayList<>();
        // int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        int time  = 1;
        if (time != 0) {
            int ruby = GetRubyFormWoodChest(pl, item);
            //Thêm quà cho rương
            Item itemReward = null;
            // int param = item.itemOptions.size();
            int param = item.itemOptions.get(0).param;
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            // String text = " ";
            if (param < 8) {
                gold = 100000000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 150000000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param+1)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 2000000000;
                listClothesReward = new int[]{randClothes(param), randClothes(param+1), randClothes(param+2)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
            }
            for (int i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                //code cũ
                // RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                //code 1
                // RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{
                //     new RewardService.RatioStar((byte) 1, 1, 2),
                //     new RewardService.RatioStar((byte) 2, 1, 3),
                //     new RewardService.RatioStar((byte) 3, 1, 4),
                //     new RewardService.RatioStar((byte) 4, 1, 5),
                //     new RewardService.RatioStar((byte) 5, 1, 6),
                //     new RewardService.RatioStar((byte) 6, 1, 7),
                // });
                //code 2
                List<RewardService.RatioStar> ratios = new ArrayList<>();
                    ratios.add(new RewardService.RatioStar((byte) 1, 1, 2));
                    ratios.add(new RewardService.RatioStar((byte) 2, 1, 3));
                    ratios.add(new RewardService.RatioStar((byte) 3, 1, 4));
                if (param > 9) {
                    ratios.add(new RewardService.RatioStar((byte) 7, 1, 8));
                }
                else
                {
                    ratios.add(new RewardService.RatioStar((byte) 4, 1, 5));
                    ratios.add(new RewardService.RatioStar((byte) 5, 1, 6));
                    ratios.add(new RewardService.RatioStar((byte) 6, 1, 7));  
                }
                RewardService.gI().initStarOption(itemReward, ratios.toArray(new RewardService.RatioStar[0]));
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                // pl.textRuongGo.add(text + itemReward.Name());
                infoItem.add(itemReward.getInfoItem());
            }
            for (int i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 20);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                // pl.textRuongGo.add(text + itemReward.Name());
                infoItem.add(itemReward.getInfoItem());
            }
            if (param == 11) {
                // itemReward = ItemService.gI().createNewItem((short) 0);
                // itemReward.quantity = Util.nextInt(1, 7);
                // InventoryServiceNew.gI().addItemBag(pl, itemReward);
                for (int i = 14; i <= 20; i++) {
                    itemReward = ItemService.gI().createNewItem((short) i);
                    InventoryServiceNew.gI().addItemBag(pl, itemReward);
                }
                String text = "|1| + 1 bộ ngọc rồng 1 sao\n|2| Dùng để ước điều mong muốn từ rồng thần hoặc làm nguyên liệu ép sao pha lê";
                infoItem.add(text);
            }
            
            // NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng và +"+ruby+" hồng ngọc" + (text.equalsIgnoreCase(" ") ? "\n|2|" : "\n" + text) + pl.textRuongGo, "OK" );
            // Service.getInstance().sendThongBao(pl, "Bạn nhận được " + Util.numberToMoney(gold) + "và "+pl.textRuongGo + "\b]");
            // NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK["+(infoItem.size()+2)+"]");
            // NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + ruby + " hồng ngọc", "OK["+(infoItem.size()+1)+"]");
            // for (int i = 0; i < infoItem.size();i++)
            // {
            //     NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n" + infoItem.get(i), "OK["+(infoItem.size()-i)+"]" );
            // }
            pl.rewardMessages = new ArrayList<>();
            pl.rewardMessages.add("Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng");
            pl.rewardMessages.add("Bạn nhận được\n|1|+" + ruby + " hồng ngọc");
            for (String items : infoItem) {
                pl.rewardMessages.add("Bạn nhận được\n" + items);
            }            
            showNextReward(pl);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private void RuongItemCap2(Player pl, Item item) {
        NpcService.gI().createMenuConMeo(pl, 1278, -1, "Bạn muốn chọn gì ?", "Cuồng nộ", "Bổ khí", "Bổ huyết", "Giáp Xên bọ hung", "Ẩn danh");
    }

    public void SendItemCap2(Player pl, int type, int SoLuong) {
        short tempId = 0;
        switch (type) {
            case 4:
                tempId = 1099;
                break;
            case 5:
                tempId = 1101;
                break;
            case 6:
                tempId = 1100;
                break;
            case 7:
                tempId = 1102;
                break;
            case 8:
                tempId = 1103;
                break;
        }
        Item item = InventoryServiceNew.gI().findItem(pl.inventory.itemsBag, 1278);
        if (item.quantity >= SoLuong) {
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, SoLuong);
            Item itemsend = ItemService.gI().createNewItem(tempId, SoLuong);
            InventoryServiceNew.gI().addItemBag(pl, itemsend);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn nhận được x" + SoLuong + " " + itemsend.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Số lượng rương không đủ");
        }
    }

    public void showNextReward(Player pl) {
        if (pl.rewardMessages != null && pl.currentMessageIndex < pl.rewardMessages.size()) {
            String msg = pl.rewardMessages.get(pl.currentMessageIndex);
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, msg, "OK["+(pl.rewardMessages.size()-pl.currentMessageIndex)+"]");
        } else {
            // Hết thông điệp
            pl.rewardMessages = null;
            pl.currentMessageIndex = 0;
        }
    }
    

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    //list quần áo cho quà rương g
    public static final int[][][] LIST_ITEM_CLOTHES = {
        // áo , quần , găng ,giày,rada
        //td -> nm -> xd
        {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };
}
