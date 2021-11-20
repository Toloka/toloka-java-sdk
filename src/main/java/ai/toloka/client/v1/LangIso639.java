/*
 * Copyright 2021 YANDEX LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.toloka.client.v1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Implemented not with enum because of possibility to get unknown country from server
 * <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">Language codes ISO 639-1</a>
 */
public final class LangIso639 extends FlexibleEnum<LangIso639> {

    public static final LangIso639 AA = new LangIso639("AA");
    public static final LangIso639 AB = new LangIso639("AB");
    public static final LangIso639 AE = new LangIso639("AE");
    public static final LangIso639 AF = new LangIso639("AF");
    public static final LangIso639 AK = new LangIso639("AK");
    public static final LangIso639 AM = new LangIso639("AM");
    public static final LangIso639 AN = new LangIso639("AN");
    public static final LangIso639 AR = new LangIso639("AR");
    public static final LangIso639 AS = new LangIso639("AS");
    public static final LangIso639 AV = new LangIso639("AV");
    public static final LangIso639 AY = new LangIso639("AY");
    public static final LangIso639 AZ = new LangIso639("AZ");
    public static final LangIso639 BA = new LangIso639("BA");
    public static final LangIso639 BE = new LangIso639("BE");
    public static final LangIso639 BG = new LangIso639("BG");
    public static final LangIso639 BH = new LangIso639("BH");
    public static final LangIso639 BI = new LangIso639("BI");
    public static final LangIso639 BM = new LangIso639("BM");
    public static final LangIso639 BN = new LangIso639("BN");
    public static final LangIso639 BO = new LangIso639("BO");
    public static final LangIso639 BR = new LangIso639("BR");
    public static final LangIso639 BS = new LangIso639("BS");
    public static final LangIso639 CA = new LangIso639("CA");
    public static final LangIso639 CE = new LangIso639("CE");
    public static final LangIso639 CH = new LangIso639("CH");
    public static final LangIso639 CO = new LangIso639("CO");
    public static final LangIso639 CR = new LangIso639("CR");
    public static final LangIso639 CS = new LangIso639("CS");
    public static final LangIso639 CU = new LangIso639("CU");
    public static final LangIso639 CV = new LangIso639("CV");
    public static final LangIso639 CY = new LangIso639("CY");
    public static final LangIso639 DA = new LangIso639("DA");
    public static final LangIso639 DE = new LangIso639("DE");
    public static final LangIso639 DV = new LangIso639("DV");
    public static final LangIso639 DZ = new LangIso639("DZ");
    public static final LangIso639 EE = new LangIso639("EE");
    public static final LangIso639 EL = new LangIso639("EL");
    public static final LangIso639 EN = new LangIso639("EN");
    public static final LangIso639 EO = new LangIso639("EO");
    public static final LangIso639 ES = new LangIso639("ES");
    public static final LangIso639 ET = new LangIso639("ET");
    public static final LangIso639 EU = new LangIso639("EU");
    public static final LangIso639 FA = new LangIso639("FA");
    public static final LangIso639 FF = new LangIso639("FF");
    public static final LangIso639 FI = new LangIso639("FI");
    public static final LangIso639 FJ = new LangIso639("FJ");
    public static final LangIso639 FO = new LangIso639("FO");
    public static final LangIso639 FR = new LangIso639("FR");
    public static final LangIso639 FY = new LangIso639("FY");
    public static final LangIso639 GA = new LangIso639("GA");
    public static final LangIso639 GD = new LangIso639("GD");
    public static final LangIso639 GL = new LangIso639("GL");
    public static final LangIso639 GN = new LangIso639("GN");
    public static final LangIso639 GU = new LangIso639("GU");
    public static final LangIso639 GV = new LangIso639("GV");
    public static final LangIso639 HA = new LangIso639("HA");
    public static final LangIso639 HE = new LangIso639("HE");
    public static final LangIso639 HI = new LangIso639("HI");
    public static final LangIso639 HO = new LangIso639("HO");
    public static final LangIso639 HR = new LangIso639("HR");
    public static final LangIso639 HT = new LangIso639("HT");
    public static final LangIso639 HU = new LangIso639("HU");
    public static final LangIso639 HY = new LangIso639("HY");
    public static final LangIso639 HZ = new LangIso639("HZ");
    public static final LangIso639 IA = new LangIso639("IA");
    public static final LangIso639 ID = new LangIso639("ID");
    public static final LangIso639 IE = new LangIso639("IE");
    public static final LangIso639 IG = new LangIso639("IG");
    public static final LangIso639 II = new LangIso639("II");
    public static final LangIso639 IK = new LangIso639("IK");
    public static final LangIso639 IN = new LangIso639("IN");
    public static final LangIso639 IO = new LangIso639("IO");
    public static final LangIso639 IS = new LangIso639("IS");
    public static final LangIso639 IT = new LangIso639("IT");
    public static final LangIso639 IU = new LangIso639("IU");
    public static final LangIso639 IW = new LangIso639("IW");
    public static final LangIso639 JA = new LangIso639("JA");
    public static final LangIso639 JI = new LangIso639("JI");
    public static final LangIso639 JV = new LangIso639("JV");
    public static final LangIso639 KA = new LangIso639("KA");
    public static final LangIso639 KG = new LangIso639("KG");
    public static final LangIso639 KI = new LangIso639("KI");
    public static final LangIso639 KJ = new LangIso639("KJ");
    public static final LangIso639 KK = new LangIso639("KK");
    public static final LangIso639 KL = new LangIso639("KL");
    public static final LangIso639 KM = new LangIso639("KM");
    public static final LangIso639 KN = new LangIso639("KN");
    public static final LangIso639 KO = new LangIso639("KO");
    public static final LangIso639 KR = new LangIso639("KR");
    public static final LangIso639 KS = new LangIso639("KS");
    public static final LangIso639 KU = new LangIso639("KU");
    public static final LangIso639 KV = new LangIso639("KV");
    public static final LangIso639 KW = new LangIso639("KW");
    public static final LangIso639 KY = new LangIso639("KY");
    public static final LangIso639 LA = new LangIso639("LA");
    public static final LangIso639 LB = new LangIso639("LB");
    public static final LangIso639 LG = new LangIso639("LG");
    public static final LangIso639 LI = new LangIso639("LI");
    public static final LangIso639 LN = new LangIso639("LN");
    public static final LangIso639 LO = new LangIso639("LO");
    public static final LangIso639 LT = new LangIso639("LT");
    public static final LangIso639 LU = new LangIso639("LU");
    public static final LangIso639 LV = new LangIso639("LV");
    public static final LangIso639 MG = new LangIso639("MG");
    public static final LangIso639 MH = new LangIso639("MH");
    public static final LangIso639 MI = new LangIso639("MI");
    public static final LangIso639 MK = new LangIso639("MK");
    public static final LangIso639 ML = new LangIso639("ML");
    public static final LangIso639 MN = new LangIso639("MN");
    public static final LangIso639 MO = new LangIso639("MO");
    public static final LangIso639 MR = new LangIso639("MR");
    public static final LangIso639 MS = new LangIso639("MS");
    public static final LangIso639 MT = new LangIso639("MT");
    public static final LangIso639 MY = new LangIso639("MY");
    public static final LangIso639 NA = new LangIso639("NA");
    public static final LangIso639 NB = new LangIso639("NB");
    public static final LangIso639 ND = new LangIso639("ND");
    public static final LangIso639 NE = new LangIso639("NE");
    public static final LangIso639 NG = new LangIso639("NG");
    public static final LangIso639 NL = new LangIso639("NL");
    public static final LangIso639 NN = new LangIso639("NN");
    public static final LangIso639 NO = new LangIso639("NO");
    public static final LangIso639 NR = new LangIso639("NR");
    public static final LangIso639 NV = new LangIso639("NV");
    public static final LangIso639 NY = new LangIso639("NY");
    public static final LangIso639 OC = new LangIso639("OC");
    public static final LangIso639 OJ = new LangIso639("OJ");
    public static final LangIso639 OM = new LangIso639("OM");
    public static final LangIso639 OR = new LangIso639("OR");
    public static final LangIso639 OS = new LangIso639("OS");
    public static final LangIso639 PA = new LangIso639("PA");
    public static final LangIso639 PI = new LangIso639("PI");
    public static final LangIso639 PL = new LangIso639("PL");
    public static final LangIso639 PS = new LangIso639("PS");
    public static final LangIso639 PT = new LangIso639("PT");
    public static final LangIso639 PT_BR = new LangIso639("PT-BR");
    public static final LangIso639 QU = new LangIso639("QU");
    public static final LangIso639 RM = new LangIso639("RM");
    public static final LangIso639 RN = new LangIso639("RN");
    public static final LangIso639 RO = new LangIso639("RO");
    public static final LangIso639 RU = new LangIso639("RU");
    public static final LangIso639 RW = new LangIso639("RW");
    public static final LangIso639 SA = new LangIso639("SA");
    public static final LangIso639 SC = new LangIso639("SC");
    public static final LangIso639 SD = new LangIso639("SD");
    public static final LangIso639 SE = new LangIso639("SE");
    public static final LangIso639 SG = new LangIso639("SG");
    public static final LangIso639 SI = new LangIso639("SI");
    public static final LangIso639 SK = new LangIso639("SK");
    public static final LangIso639 SL = new LangIso639("SL");
    public static final LangIso639 SM = new LangIso639("SM");
    public static final LangIso639 SN = new LangIso639("SN");
    public static final LangIso639 SO = new LangIso639("SO");
    public static final LangIso639 SQ = new LangIso639("SQ");
    public static final LangIso639 SR = new LangIso639("SR");
    public static final LangIso639 SS = new LangIso639("SS");
    public static final LangIso639 ST = new LangIso639("ST");
    public static final LangIso639 SU = new LangIso639("SU");
    public static final LangIso639 SV = new LangIso639("SV");
    public static final LangIso639 SW = new LangIso639("SW");
    public static final LangIso639 TA = new LangIso639("TA");
    public static final LangIso639 TE = new LangIso639("TE");
    public static final LangIso639 TG = new LangIso639("TG");
    public static final LangIso639 TH = new LangIso639("TH");
    public static final LangIso639 TI = new LangIso639("TI");
    public static final LangIso639 TK = new LangIso639("TK");
    public static final LangIso639 TL = new LangIso639("TL");
    public static final LangIso639 TN = new LangIso639("TN");
    public static final LangIso639 TO = new LangIso639("TO");
    public static final LangIso639 TR = new LangIso639("TR");
    public static final LangIso639 TS = new LangIso639("TS");
    public static final LangIso639 TT = new LangIso639("TT");
    public static final LangIso639 TW = new LangIso639("TW");
    public static final LangIso639 TY = new LangIso639("TY");
    public static final LangIso639 UG = new LangIso639("UG");
    public static final LangIso639 UK = new LangIso639("UK");
    public static final LangIso639 UR = new LangIso639("UR");
    public static final LangIso639 UZ = new LangIso639("UZ");
    public static final LangIso639 VE = new LangIso639("VE");
    public static final LangIso639 VI = new LangIso639("VI");
    public static final LangIso639 VO = new LangIso639("VO");
    public static final LangIso639 WA = new LangIso639("WA");
    public static final LangIso639 WO = new LangIso639("WO");
    public static final LangIso639 XH = new LangIso639("XH");
    public static final LangIso639 YI = new LangIso639("YI");
    public static final LangIso639 YO = new LangIso639("YO");
    public static final LangIso639 ZA = new LangIso639("ZA");
    public static final LangIso639 ZH = new LangIso639("ZH");
    public static final LangIso639 ZH_HANS = new LangIso639("ZH-HANS");
    public static final LangIso639 ZU = new LangIso639("ZU");

    private LangIso639(String name) {
        super(name);
    }

    private static final LangIso639[] VALUES = {
            AA, AB, AE, AF, AK, AM, AN, AR, AS, AV, AY, AZ, BA, BE, BG, BH, BI, BM, BN, BO, BR, BS, CA, CE, CH, CO,
            CR, CS, CU, CV, CY, DA, DE, DV, DZ, EE, EL, EN, EO, ES, ET, EU, FA, FF, FI, FJ, FO, FR, FY, GA, GD, GL,
            GN, GU, GV, HA, HE, HI, HO, HR, HT, HU, HY, HZ, IA, ID, IE, IG, II, IK, IN, IO, IS, IT, IU, IW, JA, JI,
            JV, KA, KG, KI, KJ, KK, KL, KM, KN, KO, KR, KS, KU, KV, KW, KY, LA, LB, LG, LI, LN, LO, LT, LU, LV, MG,
            MH, MI, MK, ML, MN, MO, MR, MS, MT, MY, NA, NB, ND, NE, NG, NL, NN, NO, NR, NV, NY, OC, OJ, OM, OR, OS,
            PA, PI, PL, PS, PT, QU, RM, RN, RO, RU, RW, SA, SC, SD, SE, SG, SI, SK, SL, SM, SN, SO, SQ, SR, SS, ST,
            SU, SV, SW, TA, TE, TG, TH, TI, TK, TL, TN, TO, TR, TS, TT, TW, TY, UG, UK, UR, UZ, VE, VI, VO, WA, WO,
            XH, YI, YO, ZA, ZH, ZH_HANS, ZU,

            PT_BR
    };
    private static final ConcurrentMap<String, LangIso639> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static LangIso639[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), LangIso639.class);
    }

    @JsonCreator
    public static LangIso639 valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new FlexibleEnum.NewEnumCreator<LangIso639>() {
            @Override public LangIso639 create(String name) {
                return new LangIso639(name);
            }
        });
    }
}
