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
 * <a href="http://www.iso.org/iso/country_codes.htm">Country codes ISO 3166</a>
 */
public final class CountryIso3166 extends FlexibleEnum<CountryIso3166> {

    public static final CountryIso3166 AB = new CountryIso3166("AB");
    public static final CountryIso3166 AD = new CountryIso3166("AD");
    public static final CountryIso3166 AE = new CountryIso3166("AE");
    public static final CountryIso3166 AF = new CountryIso3166("AF");
    public static final CountryIso3166 AG = new CountryIso3166("AG");
    public static final CountryIso3166 AI = new CountryIso3166("AI");
    public static final CountryIso3166 AL = new CountryIso3166("AL");
    public static final CountryIso3166 AM = new CountryIso3166("AM");
    public static final CountryIso3166 AN = new CountryIso3166("AN");
    public static final CountryIso3166 AO = new CountryIso3166("AO");
    public static final CountryIso3166 AQ = new CountryIso3166("AQ");
    public static final CountryIso3166 AR = new CountryIso3166("AR");
    public static final CountryIso3166 AS = new CountryIso3166("AS");
    public static final CountryIso3166 AT = new CountryIso3166("AT");
    public static final CountryIso3166 AU = new CountryIso3166("AU");
    public static final CountryIso3166 AW = new CountryIso3166("AW");
    public static final CountryIso3166 AX = new CountryIso3166("AX");
    public static final CountryIso3166 AZ = new CountryIso3166("AZ");
    public static final CountryIso3166 BA = new CountryIso3166("BA");
    public static final CountryIso3166 BB = new CountryIso3166("BB");
    public static final CountryIso3166 BD = new CountryIso3166("BD");
    public static final CountryIso3166 BE = new CountryIso3166("BE");
    public static final CountryIso3166 BF = new CountryIso3166("BF");
    public static final CountryIso3166 BG = new CountryIso3166("BG");
    public static final CountryIso3166 BH = new CountryIso3166("BH");
    public static final CountryIso3166 BI = new CountryIso3166("BI");
    public static final CountryIso3166 BJ = new CountryIso3166("BJ");
    public static final CountryIso3166 BL = new CountryIso3166("BL");
    public static final CountryIso3166 BM = new CountryIso3166("BM");
    public static final CountryIso3166 BN = new CountryIso3166("BN");
    public static final CountryIso3166 BO = new CountryIso3166("BO");
    public static final CountryIso3166 BQ = new CountryIso3166("BQ");
    public static final CountryIso3166 BR = new CountryIso3166("BR");
    public static final CountryIso3166 BS = new CountryIso3166("BS");
    public static final CountryIso3166 BT = new CountryIso3166("BT");
    public static final CountryIso3166 BV = new CountryIso3166("BV");
    public static final CountryIso3166 BW = new CountryIso3166("BW");
    public static final CountryIso3166 BY = new CountryIso3166("BY");
    public static final CountryIso3166 BZ = new CountryIso3166("BZ");
    public static final CountryIso3166 CA = new CountryIso3166("CA");
    public static final CountryIso3166 CC = new CountryIso3166("CC");
    public static final CountryIso3166 CD = new CountryIso3166("CD");
    public static final CountryIso3166 CF = new CountryIso3166("CF");
    public static final CountryIso3166 CG = new CountryIso3166("CG");
    public static final CountryIso3166 CH = new CountryIso3166("CH");
    public static final CountryIso3166 CI = new CountryIso3166("CI");
    public static final CountryIso3166 CK = new CountryIso3166("CK");
    public static final CountryIso3166 CL = new CountryIso3166("CL");
    public static final CountryIso3166 CM = new CountryIso3166("CM");
    public static final CountryIso3166 CN = new CountryIso3166("CN");
    public static final CountryIso3166 CO = new CountryIso3166("CO");
    public static final CountryIso3166 CR = new CountryIso3166("CR");
    public static final CountryIso3166 CS = new CountryIso3166("CS");
    public static final CountryIso3166 CU = new CountryIso3166("CU");
    public static final CountryIso3166 CV = new CountryIso3166("CV");
    public static final CountryIso3166 CW = new CountryIso3166("CW");
    public static final CountryIso3166 CX = new CountryIso3166("CX");
    public static final CountryIso3166 CY = new CountryIso3166("CY");
    public static final CountryIso3166 CZ = new CountryIso3166("CZ");
    public static final CountryIso3166 DE = new CountryIso3166("DE");
    public static final CountryIso3166 DJ = new CountryIso3166("DJ");
    public static final CountryIso3166 DK = new CountryIso3166("DK");
    public static final CountryIso3166 DM = new CountryIso3166("DM");
    public static final CountryIso3166 DO = new CountryIso3166("DO");
    public static final CountryIso3166 DZ = new CountryIso3166("DZ");
    public static final CountryIso3166 EC = new CountryIso3166("EC");
    public static final CountryIso3166 EE = new CountryIso3166("EE");
    public static final CountryIso3166 EG = new CountryIso3166("EG");
    public static final CountryIso3166 EH = new CountryIso3166("EH");
    public static final CountryIso3166 ER = new CountryIso3166("ER");
    public static final CountryIso3166 ES = new CountryIso3166("ES");
    public static final CountryIso3166 ET = new CountryIso3166("ET");
    public static final CountryIso3166 FI = new CountryIso3166("FI");
    public static final CountryIso3166 FJ = new CountryIso3166("FJ");
    public static final CountryIso3166 FK = new CountryIso3166("FK");
    public static final CountryIso3166 FM = new CountryIso3166("FM");
    public static final CountryIso3166 FO = new CountryIso3166("FO");
    public static final CountryIso3166 FR = new CountryIso3166("FR");
    public static final CountryIso3166 GA = new CountryIso3166("GA");
    public static final CountryIso3166 GB = new CountryIso3166("GB");
    public static final CountryIso3166 GD = new CountryIso3166("GD");
    public static final CountryIso3166 GE = new CountryIso3166("GE");
    public static final CountryIso3166 GF = new CountryIso3166("GF");
    public static final CountryIso3166 GG = new CountryIso3166("GG");
    public static final CountryIso3166 GH = new CountryIso3166("GH");
    public static final CountryIso3166 GI = new CountryIso3166("GI");
    public static final CountryIso3166 GL = new CountryIso3166("GL");
    public static final CountryIso3166 GM = new CountryIso3166("GM");
    public static final CountryIso3166 GN = new CountryIso3166("GN");
    public static final CountryIso3166 GP = new CountryIso3166("GP");
    public static final CountryIso3166 GQ = new CountryIso3166("GQ");
    public static final CountryIso3166 GR = new CountryIso3166("GR");
    public static final CountryIso3166 GS = new CountryIso3166("GS");
    public static final CountryIso3166 GT = new CountryIso3166("GT");
    public static final CountryIso3166 GU = new CountryIso3166("GU");
    public static final CountryIso3166 GW = new CountryIso3166("GW");
    public static final CountryIso3166 GY = new CountryIso3166("GY");
    public static final CountryIso3166 HK = new CountryIso3166("HK");
    public static final CountryIso3166 HM = new CountryIso3166("HM");
    public static final CountryIso3166 HN = new CountryIso3166("HN");
    public static final CountryIso3166 HR = new CountryIso3166("HR");
    public static final CountryIso3166 HT = new CountryIso3166("HT");
    public static final CountryIso3166 HU = new CountryIso3166("HU");
    public static final CountryIso3166 ID = new CountryIso3166("ID");
    public static final CountryIso3166 IE = new CountryIso3166("IE");
    public static final CountryIso3166 IL = new CountryIso3166("IL");
    public static final CountryIso3166 IM = new CountryIso3166("IM");
    public static final CountryIso3166 IN = new CountryIso3166("IN");
    public static final CountryIso3166 IO = new CountryIso3166("IO");
    public static final CountryIso3166 IQ = new CountryIso3166("IQ");
    public static final CountryIso3166 IR = new CountryIso3166("IR");
    public static final CountryIso3166 IS = new CountryIso3166("IS");
    public static final CountryIso3166 IT = new CountryIso3166("IT");
    public static final CountryIso3166 JE = new CountryIso3166("JE");
    public static final CountryIso3166 JM = new CountryIso3166("JM");
    public static final CountryIso3166 JO = new CountryIso3166("JO");
    public static final CountryIso3166 JP = new CountryIso3166("JP");
    public static final CountryIso3166 KE = new CountryIso3166("KE");
    public static final CountryIso3166 KG = new CountryIso3166("KG");
    public static final CountryIso3166 KH = new CountryIso3166("KH");
    public static final CountryIso3166 KI = new CountryIso3166("KI");
    public static final CountryIso3166 KM = new CountryIso3166("KM");
    public static final CountryIso3166 KN = new CountryIso3166("KN");
    public static final CountryIso3166 KP = new CountryIso3166("KP");
    public static final CountryIso3166 KR = new CountryIso3166("KR");
    public static final CountryIso3166 KW = new CountryIso3166("KW");
    public static final CountryIso3166 KY = new CountryIso3166("KY");
    public static final CountryIso3166 KZ = new CountryIso3166("KZ");
    public static final CountryIso3166 LA = new CountryIso3166("LA");
    public static final CountryIso3166 LB = new CountryIso3166("LB");
    public static final CountryIso3166 LC = new CountryIso3166("LC");
    public static final CountryIso3166 LI = new CountryIso3166("LI");
    public static final CountryIso3166 LK = new CountryIso3166("LK");
    public static final CountryIso3166 LR = new CountryIso3166("LR");
    public static final CountryIso3166 LS = new CountryIso3166("LS");
    public static final CountryIso3166 LT = new CountryIso3166("LT");
    public static final CountryIso3166 LU = new CountryIso3166("LU");
    public static final CountryIso3166 LV = new CountryIso3166("LV");
    public static final CountryIso3166 LY = new CountryIso3166("LY");
    public static final CountryIso3166 MA = new CountryIso3166("MA");
    public static final CountryIso3166 MC = new CountryIso3166("MC");
    public static final CountryIso3166 MD = new CountryIso3166("MD");
    public static final CountryIso3166 ME = new CountryIso3166("ME");
    public static final CountryIso3166 MF = new CountryIso3166("MF");
    public static final CountryIso3166 MG = new CountryIso3166("MG");
    public static final CountryIso3166 MH = new CountryIso3166("MH");
    public static final CountryIso3166 MK = new CountryIso3166("MK");
    public static final CountryIso3166 ML = new CountryIso3166("ML");
    public static final CountryIso3166 MM = new CountryIso3166("MM");
    public static final CountryIso3166 MN = new CountryIso3166("MN");
    public static final CountryIso3166 MO = new CountryIso3166("MO");
    public static final CountryIso3166 MP = new CountryIso3166("MP");
    public static final CountryIso3166 MQ = new CountryIso3166("MQ");
    public static final CountryIso3166 MR = new CountryIso3166("MR");
    public static final CountryIso3166 MS = new CountryIso3166("MS");
    public static final CountryIso3166 MT = new CountryIso3166("MT");
    public static final CountryIso3166 MU = new CountryIso3166("MU");
    public static final CountryIso3166 MV = new CountryIso3166("MV");
    public static final CountryIso3166 MW = new CountryIso3166("MW");
    public static final CountryIso3166 MX = new CountryIso3166("MX");
    public static final CountryIso3166 MY = new CountryIso3166("MY");
    public static final CountryIso3166 MZ = new CountryIso3166("MZ");
    public static final CountryIso3166 NA = new CountryIso3166("NA");
    public static final CountryIso3166 NC = new CountryIso3166("NC");
    public static final CountryIso3166 NE = new CountryIso3166("NE");
    public static final CountryIso3166 NF = new CountryIso3166("NF");
    public static final CountryIso3166 NG = new CountryIso3166("NG");
    public static final CountryIso3166 NI = new CountryIso3166("NI");
    public static final CountryIso3166 NL = new CountryIso3166("NL");
    public static final CountryIso3166 NO = new CountryIso3166("NO");
    public static final CountryIso3166 NP = new CountryIso3166("NP");
    public static final CountryIso3166 NR = new CountryIso3166("NR");
    public static final CountryIso3166 NU = new CountryIso3166("NU");
    public static final CountryIso3166 NZ = new CountryIso3166("NZ");
    public static final CountryIso3166 OM = new CountryIso3166("OM");
    public static final CountryIso3166 OS = new CountryIso3166("OS");
    public static final CountryIso3166 PA = new CountryIso3166("PA");
    public static final CountryIso3166 PE = new CountryIso3166("PE");
    public static final CountryIso3166 PF = new CountryIso3166("PF");
    public static final CountryIso3166 PG = new CountryIso3166("PG");
    public static final CountryIso3166 PH = new CountryIso3166("PH");
    public static final CountryIso3166 PK = new CountryIso3166("PK");
    public static final CountryIso3166 PL = new CountryIso3166("PL");
    public static final CountryIso3166 PM = new CountryIso3166("PM");
    public static final CountryIso3166 PN = new CountryIso3166("PN");
    public static final CountryIso3166 PR = new CountryIso3166("PR");
    public static final CountryIso3166 PS = new CountryIso3166("PS");
    public static final CountryIso3166 PT = new CountryIso3166("PT");
    public static final CountryIso3166 PW = new CountryIso3166("PW");
    public static final CountryIso3166 PY = new CountryIso3166("PY");
    public static final CountryIso3166 QA = new CountryIso3166("QA");
    public static final CountryIso3166 RE = new CountryIso3166("RE");
    public static final CountryIso3166 RO = new CountryIso3166("RO");
    public static final CountryIso3166 RS = new CountryIso3166("RS");
    public static final CountryIso3166 RU = new CountryIso3166("RU");
    public static final CountryIso3166 RW = new CountryIso3166("RW");
    public static final CountryIso3166 SA = new CountryIso3166("SA");
    public static final CountryIso3166 SB = new CountryIso3166("SB");
    public static final CountryIso3166 SC = new CountryIso3166("SC");
    public static final CountryIso3166 SD = new CountryIso3166("SD");
    public static final CountryIso3166 SE = new CountryIso3166("SE");
    public static final CountryIso3166 SG = new CountryIso3166("SG");
    public static final CountryIso3166 SH = new CountryIso3166("SH");
    public static final CountryIso3166 SI = new CountryIso3166("SI");
    public static final CountryIso3166 SJ = new CountryIso3166("SJ");
    public static final CountryIso3166 SK = new CountryIso3166("SK");
    public static final CountryIso3166 SL = new CountryIso3166("SL");
    public static final CountryIso3166 SM = new CountryIso3166("SM");
    public static final CountryIso3166 SN = new CountryIso3166("SN");
    public static final CountryIso3166 SO = new CountryIso3166("SO");
    public static final CountryIso3166 SR = new CountryIso3166("SR");
    public static final CountryIso3166 SS = new CountryIso3166("SS");
    public static final CountryIso3166 ST = new CountryIso3166("ST");
    public static final CountryIso3166 SV = new CountryIso3166("SV");
    public static final CountryIso3166 SX = new CountryIso3166("SX");
    public static final CountryIso3166 SY = new CountryIso3166("SY");
    public static final CountryIso3166 SZ = new CountryIso3166("SZ");
    public static final CountryIso3166 TC = new CountryIso3166("TC");
    public static final CountryIso3166 TD = new CountryIso3166("TD");
    public static final CountryIso3166 TF = new CountryIso3166("TF");
    public static final CountryIso3166 TG = new CountryIso3166("TG");
    public static final CountryIso3166 TH = new CountryIso3166("TH");
    public static final CountryIso3166 TJ = new CountryIso3166("TJ");
    public static final CountryIso3166 TK = new CountryIso3166("TK");
    public static final CountryIso3166 TL = new CountryIso3166("TL");
    public static final CountryIso3166 TM = new CountryIso3166("TM");
    public static final CountryIso3166 TN = new CountryIso3166("TN");
    public static final CountryIso3166 TO = new CountryIso3166("TO");
    public static final CountryIso3166 TR = new CountryIso3166("TR");
    public static final CountryIso3166 TT = new CountryIso3166("TT");
    public static final CountryIso3166 TV = new CountryIso3166("TV");
    public static final CountryIso3166 TW = new CountryIso3166("TW");
    public static final CountryIso3166 TZ = new CountryIso3166("TZ");
    public static final CountryIso3166 UA = new CountryIso3166("UA");
    public static final CountryIso3166 UG = new CountryIso3166("UG");
    public static final CountryIso3166 UM = new CountryIso3166("UM");
    public static final CountryIso3166 US = new CountryIso3166("US");
    public static final CountryIso3166 UY = new CountryIso3166("UY");
    public static final CountryIso3166 UZ = new CountryIso3166("UZ");
    public static final CountryIso3166 VA = new CountryIso3166("VA");
    public static final CountryIso3166 VC = new CountryIso3166("VC");
    public static final CountryIso3166 VE = new CountryIso3166("VE");
    public static final CountryIso3166 VG = new CountryIso3166("VG");
    public static final CountryIso3166 VI = new CountryIso3166("VI");
    public static final CountryIso3166 VN = new CountryIso3166("VN");
    public static final CountryIso3166 VU = new CountryIso3166("VU");
    public static final CountryIso3166 WF = new CountryIso3166("WF");
    public static final CountryIso3166 WS = new CountryIso3166("WS");
    public static final CountryIso3166 YE = new CountryIso3166("YE");
    public static final CountryIso3166 YT = new CountryIso3166("YT");
    public static final CountryIso3166 ZA = new CountryIso3166("ZA");
    public static final CountryIso3166 ZM = new CountryIso3166("ZM");
    public static final CountryIso3166 ZW = new CountryIso3166("ZW");

    private CountryIso3166(String name) {
        super(name);
    }

    private static final CountryIso3166[] VALUES = {
            AB, AD, AE, AF, AG, AI, AL, AM, AN, AO, AQ, AR, AS, AT, AU, AW, AX, AZ, BA, BB, BD, BE, BF, BG, BH, BI,
            BJ, BL, BM, BN, BO, BQ, BR, BS, BT, BV, BW, BY, BZ, CA, CC, CD, CF, CG, CH, CI, CK, CL, CM, CN, CO, CR,
            CS, CU, CV, CW, CX, CY, CZ, DE, DJ, DK, DM, DO, DZ, EC, EE, EG, EH, ER, ES, ET, FI, FJ, FK, FM, FO, FR,
            GA, GB, GD, GE, GF, GG, GH, GI, GL, GM, GN, GP, GQ, GR, GS, GT, GU, GW, GY, HK, HM, HN, HR, HT, HU, ID,
            IE, IL, IM, IN, IO, IQ, IR, IS, IT, JE, JM, JO, JP, KE, KG, KH, KI, KM, KN, KP, KR, KW, KY, KZ, LA, LB,
            LC, LI, LK, LR, LS, LT, LU, LV, LY, MA, MC, MD, ME, MF, MG, MH, MK, ML, MM, MN, MO, MP, MQ, MR, MS, MT,
            MU, MV, MW, MX, MY, MZ, NA, NC, NE, NF, NG, NI, NL, NO, NP, NR, NU, NZ, OM, OS, PA, PE, PF, PG, PH, PK,
            PL, PM, PN, PR, PS, PT, PW, PY, QA, RE, RO, RS, RU, RW, SA, SB, SC, SD, SE, SG, SH, SI, SJ, SK, SL, SM,
            SN, SO, SR, SS, ST, SV, SX, SY, SZ, TC, TD, TF, TG, TH, TJ, TK, TL, TM, TN, TO, TR, TT, TV, TW, TZ, UA,
            UG, UM, US, UY, UZ, VA, VC, VE, VG, VI, VN, VU, WF, WS, YE, YT, ZA, ZM, ZW};
    private static final ConcurrentMap<String, CountryIso3166> DISCOVERED_VALUES = new ConcurrentHashMap<>();

    public static CountryIso3166[] values() {
        return values(VALUES, DISCOVERED_VALUES.values(), CountryIso3166.class);
    }

    @JsonCreator
    public static CountryIso3166 valueOf(String name) {
        return valueOf(VALUES, DISCOVERED_VALUES, name, new FlexibleEnum.NewEnumCreator<CountryIso3166>() {
            @Override public CountryIso3166 create(String name) {
                return new CountryIso3166(name);
            }
        });
    }
}
