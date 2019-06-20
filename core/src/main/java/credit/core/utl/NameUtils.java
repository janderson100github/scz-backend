package credit.core.utl;

import credit.core.exception.CreditRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class NameUtils {

    private static final int MIN_LENGTH = 4;

    private static final int MAX_LENGTH = 128;

    private static final String[] BAD_REGEXES = {""};

    private static final String[] BAD_PREFIXES = {"ipo", "suck", "kill", "murder", "rape"};

    private static final String[] BAD_WHOLE_NAMES = {"porn", "porno", "prostitue", "bitch", "hell", "jason", "official",
                                                     "biteme", "eatme", "sucks", "god", "republican", "democrat",
                                                     "muslim", "christian", "christianity", "hindu", "buddism"};

    private static final String[] BAD_CONTAINS = {"sctrader", "socialcreditzone", "asshole", "fuck", "shit", "cunt",
                                                  "whore", "allah"};

    public static void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Min length is " + MIN_LENGTH);
        }

        String nameLower = name.toLowerCase();

        if (name.length() < MIN_LENGTH) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Min length is " + MIN_LENGTH);
        }
        if (name.length() > MAX_LENGTH) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Max length is " + MAX_LENGTH);
        }

        for (String s : BAD_PREFIXES) {
            if (nameLower.startsWith(s)) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Unavailable");
            }
        }
        for (String s : BAD_REGEXES) {
            if (nameLower.matches(s)) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Unavailable");
            }
        }
        for (String s : BAD_WHOLE_NAMES) {
            if (nameLower.equalsIgnoreCase(s)) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, s + " unavailable");
            }
        }

        for (String s : BAD_CONTAINS) {
            if (nameLower.contains(s)) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Unavailable");
            }
        }

        if (StringUtils.containsWhitespace(name)) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Cannot contain spaces");
        }

        for (String country : COUNTRIES_EN) {
            if (country.equalsIgnoreCase(nameLower)) {
                throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Unavailable");
            }
        }
    }

    private final static String[] COUNTRIES_EN = {"Afghanistan", "ÅlandIslands", "Albania", "Algeria", "America",
                                                  "AmericanSamoa", "Andorra", "Angola", "Anguilla", "Antarctica",
                                                  "AntiguaandBarbuda", "Argentina", "Armenia", "Aruba", "Australia",
                                                  "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
                                                  "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda",
                                                  "Bhutan", "Bolivia", "Bonaire", "BosniaandHerzegovina", "Botswana",
                                                  "BouvetIsland", "Brazil", "BritishIndianOceanTerritory",
                                                  "BruneiDarussalam", "Bulgaria", "BurkinaFaso", "Burundi", "Cambodia",
                                                  "Cameroon", "Canada", "CapeVerde", "CaymanIslands",
                                                  "CentralAfricanRepublic", "Chad", "Chile", "China", "ChristmasIsland",
                                                  "CocosIslands", "Colombia", "Comoros", "Congo", "Congo",
                                                  "CookIslands", "CostaRica", "CôtedIvoire", "Croatia", "Cuba",
                                                  "Curaçao", "Cyprus", "CzechRepublic", "Denmark", "Djibouti",
                                                  "Dominica", "DominicanRepublic", "Ecuador", "Egypt", "ElSalvador",
                                                  "EquatorialGuinea", "Eritrea", "Estonia", "Ethiopia",
                                                  "FalklandIslands", "FaroeIslands", "Fiji", "Finland", "France",
                                                  "FrenchGuiana", "FrenchPolynesia", "FrenchSouthernTerritories",
                                                  "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
                                                  "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
                                                  "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                                                  "HeardIslandandMcDonaldIslands", "HolySee", "Honduras", "HongKong",
                                                  "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland",
                                                  "IsleofMan", "Israel", "Italy", "Jamaica", "Japan", "Jersey",
                                                  "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea", "Korea",
                                                  "Kuwait", "Kyrgyzstan", "LaoPeoplesDemocraticRepublic", "Latvia",
                                                  "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
                                                  "Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar",
                                                  "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "MarshallIslands",
                                                  "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico",
                                                  "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
                                                  "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru",
                                                  "Nepal", "Netherlands", "NewCaledonia", "NewZealand", "Nicaragua",
                                                  "Niger", "Nigeria", "Niue", "NorfolkIsland", "NorthernMarianaIslands",
                                                  "Norway", "Oman", "Pakistan", "Palau", "PalestinianTerritory",
                                                  "Panama", "PapuaNewGuinea", "Paraguay", "Peru", "Philippines",
                                                  "Pitcairn", "Poland", "Portugal", "PuertoRico", "Qatar", "Réunion",
                                                  "Romania", "RussianFederation", "Rwanda", "SaintBarthélemy",
                                                  "SaintHelena", "SaintKittsandNevis", "SaintLucia", "SaintMartin",
                                                  "SaintPierreandMiquelon", "SaintVincentandtheGrenadines", "Samoa",
                                                  "SanMarino", "SaoTomeandPrincipe", "SaudiArabia", "Senegal", "Serbia",
                                                  "Seychelles", "SierraLeone", "Singapore", "SintMaarten", "Slovakia",
                                                  "Slovenia", "SolomonIslands", "Somalia", "SouthAfrica",
                                                  "SouthGeorgiaandtheSouthSandwichIslands", "SouthSudan", "Spain",
                                                  "SriLanka", "Sudan", "Suriname", "SvalbardandJanMayen", "Swaziland",
                                                  "Sweden", "Switzerland", "SyrianArabRepublic", "Taiwan", "Tajikistan",
                                                  "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga",
                                                  "TrinidadandTobago", "Tunisia", "Turkey", "Turkmenistan",
                                                  "TurksandCaicosIslands", "Tuvalu", "Uganda", "Ukraine",
                                                  "UnitedArabEmirates", "UnitedKingdom", "UnitedStates", "usofa",
                                                  "UnitedStatesMinorOutlyingIslands", "Uruguay", "Uzbekistan",
                                                  "Vanuatu", "Venezuela", "VietNam", "VirginIslands", "VirginIslands",
                                                  "WallisandFutuna", "WesternSahara", "Yemen", "Zambia", "Zimbabwe"};
}
