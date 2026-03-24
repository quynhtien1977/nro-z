package com.girlkun.models.boss;

public class BossID {

    private BossID() {
    }

    // Đệ tử Fide
    public static final int KUKU = -1;
    public static final int MAP_DAU_DINH = -2;
    public static final int RAMBO = -3;

    //Doanh Trại
   public static final int TRUNG_UY_TRANG = -4;
    public static final int TRUNG_UY_THEP = -5;
    public static final int TRUNG_UY_XANH_LO = -6;
    public static final int NINJA_AO_TIM = -7;
    public static final int ROBOT_VE_SI = -8;

    public static final int NINJA_AO_TIM_CLONE = -2_147_479_960;
    public static final int NINJA_AO_TIM_CLONE_MAX = -2_147_479_940;// max 20 con ngẫu nhiên
    public static final int XEN_CON_CLONE = -2_147_479_960;
    public static final int XEN_CON_CLONE_MAX = -2_147_479_940;
    public static final int ROBOT_VE_SI1 = -2_147_479_939;
    public static final int ROBOT_VE_SI2 = -2_147_479_938;
    public static final int ROBOT_VE_SI3 = -2_147_479_937;
    public static final int ROBOT_VE_SI4 = -2_147_479_936;

    public static boolean isBossNinjaClone(int id) {
        return (id >= NINJA_AO_TIM_CLONE && id <= NINJA_AO_TIM_CLONE_MAX);
    }
    public static boolean isBossxenclone(int id) {
        return (id >= XEN_CON_CLONE && id <= XEN_CON_CLONE_MAX);
    }
    public static boolean isBossRobotVeSi(int id) {
        return (id >= ROBOT_VE_SI1 && id <= ROBOT_VE_SI4);
    }

    public static final int NINJA_AO_TIM1 = -9;
    public static final int NINJA_AO_TIM2 = -10;
    public static final int NINJA_AO_TIM3 = -11;
    public static final int NINJA_AO_TIM4 = -12;
    public static final int XEN_SAN_DE_TU1 = -9;
    public static final int XEN_SAN_DE_TU2 = -10;
    public static final int XEN_SAN_DE_TU3 = -11;
    public static final int XEN_SAN_DE_TU4 = -12;
    
    public static final int Yadat1 = -9;
    public static final int Yadat2 = -10;
    public static final int Yadat3 = -11;
    public static final int Yadat4 = -12;
    public static final int Yadat5 = -9;
    public static final int Yadat6 = -10;
    public static final int Yadat7 = -11;
    public static final int Yadat8 = -12;    
    //Tiểu Đội Sát Thủ
    public static final int SO_4 = -13;
    public static final int SO_3 = -14;
    public static final int SO_2 = -15;
    public static final int SO_1 = -16;
    public static final int TIEU_DOI_TRUONG = -17;
    public static final int TDST = -18;

    //Fide Đại Ca
    public static final int FIDE = -19;

    public static final int COOLER = -20;
    public static final int COOLER_GOLD = -21;
    public static final int VUA_COLD = -22;
    public static final int FIDE_ROBOT = -23;

    //Android
    public static final int ANDROID_13 = -24;
    public static final int ANDROID_14 = -25;
    public static final int ANDROID_15 = -26;

    public static final int ANDROID_19 = -27;
    public static final int DR_KORE = -28;

    public static final int PIC = -29;
    public static final int POC = -30;
    public static final int KING_KONG = -31;

    public static final int XEN_BO_HUNG = -32;
    public static final int SIEU_BO_HUNG = -33;
    public static final int XEN_CON_1 = -34;
    public static final int XEN_SAN_DE_TU = -332;
    public static final int HIT = -35;
    public static final int CHILL_1 = -36;
    public static final int CHILL_2 = -37;
    public static final int HACHIYACK = -38;
    public static final int DR_LYCHEE = -39;
    public static final int BOSS_YADAT = -352;
    public static final int BOSS_YADAT1 = -353;
    public static final int BOSS_YADAT3 = -354;    
    public static final int MABU = -40;
    public static final int CUMBER = -41;
    public static final int BLACK3 = -42;
    public static final int ANTROM = -43;
    public static final int KAIDO = -3212;
    public static final int BILL = -482;
    public static final int WHIS = -436;

    public static final int BILL1 = -483;
    public static final int WHIS1 = -437;
    public static final int BILL2 = -484;
    public static final int WHIS2 = -438;

    public static final int Rong_1Sao = -224;
    public static final int Rong_2Sao = -225;
    public static final int Rong_3Sao = -226;
    public static final int Rong_4Sao = -227;
    public static final int Rong_5Sao = -228;
    public static final int Rong_6Sao = -229;
    public static final int Rong_7Sao = -230;

    //Broly
    public static final int BROLY = -44;
    public static final int BROLY_1 = -45;
    public static final int BROLY_2 = -46;
    public static final int BROLY_3 = -4699;
    public static final int BROLY_4 = -46666;
    public static final int GA_1 = -466166;

    //Super 17 saga
    public static final int SUPER_ANDROID_17 = -47;
    public static final int DR_MYUU = -48;
    public static final int DR_KORE_GT = -49;

    //Tàu Pảy Pảy
    public static final int TAU_PAY_PAY_M = -51;

    //Mabu 12 giờ
    public static final int DRABURA = -50;
    public static final int BUI_BUI = -234;
    public static final int YA_CON = -235;
    public static final int MABU_12H = -236;
    public static final int DRABURA_2 = -237;
    public static final int BUI_BUI_2 = -238;

    // Black Goku - Tương Lai
    public static final int BLACK = -203;
    public static final int BLACK1 = -241;
    public static final int BLACK2 = -242;
    public static final int ZAMASMAX = -243;
    public static final int ZAMASZIN = -244;
    public static final int detu1 = -2424;
    public static final int DETU = -2414;
    public static final int SUPER_BROLY24 = -2434;

    //bọ cánh cứng
    public static final int CON_BO_1 = -2037;
    public static final int CON_BO_2 = -2461;
    public static final int bocung1 = -2431;
    public static final int bocung2 = -2631;
    
    public static final int THAN_HUY_DIET = -2632;
    public static final int THAN_THIEN_SU = -2633;
    
    public static final int THAN_HUY_DIET1 = -2634;
    public static final int THIEN_SU_WHIS = -2635;

    //Đại Hội Võ Thuật 23
    public static final byte SOI_HEC_QUYN = -77;
    public static final byte O_DO = -78;
    public static final byte XINBATO = -79;
    public static final byte CHA_PA = -80;
    public static final byte PON_PUT = -81;
    public static final byte CHAN_XU = -82;
    public static final byte TAU_PAY_PAY = -83;
    public static final byte YAMCHA = -84;
    public static final byte JACKY_CHUN = -85;
    public static final byte THIEN_XIN_HANG = -86;
    public static final byte LIU_LIU = -87;
    public static final byte THIEN_XIN_HANG_CLONE = -88;
    public static final byte THIEN_XIN_HANG_CLONE1 = -89;
    public static final byte THIEN_XIN_HANG_CLONE2 = -90;
    public static final byte THIEN_XIN_HANG_CLONE3 = -91;

    
     public static final byte LuyenTap = -92;

    // Event bosses
    // Halloween
    public static final int BIMA = -5001;
    public static final int DOI = -5002;
    public static final int MATROI = -5003;
    // Trung Thu
    public static final int KHIDOT = -5004;
    public static final int NGUYETTHAN = -5005;
    public static final int NHATTHAN = -5006;
    // Hung Vuong
    public static final int SON_TINH = -5007;
    public static final int THUY_TINH = -5008;

    // Noel & Tet
    public static final int ONG_GIA_NOEL = -5009;
    public static final int LAN_CON = -5010;
}
