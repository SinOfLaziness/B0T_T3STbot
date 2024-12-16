package org.bot.database;

import java.util.Arrays;
import java.util.List;

public class ConstantDB {
    public static final String USER_TABLE = "users";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String REVENUE_TABLE = "revenue";
    public static final String TABLE_CATEGORY = "Category";
    public static final String INCOMES_TABLE = "incomes";
    public static final String TABLE_INCOME = "Income";
    public static final String ACCOUNTINGS_TABLE = "accountings";
    public static final String TABLE_INCOME_ID = "idIncomes";
    public static final String TABLE_USER_ID = "idUsers";
    public static final String TABLE_CATEGORIES = "idCategories";
    public static final String TABLE_DATE = "Date";
    public static final String TABLE_AMOUNT = "Amount";
    public static final String USERS_ID = "chatID";

    public static final String KEY_HOME_AND_RENOVATION = "HomeAndRenovation";
    public static final String HOME_AND_RENOVATION = "\uD83C\uDFE1Дом и ремонт";
    public static final String KEY_TRANSPORT = "Transport";
    public static final String TRANSPORT = "\uD83D\uDE8BТранспорт";
    public static final String KEY_FOOD = "Food";
    public static final String FOOD = "\uD83C\uDF55Еда";
    public static final String KEY_ENTERTAINMENT = "Entertainment";
    public static final String ENTERTAINMENT = "\uD83C\uDF87Развлечения";
    public static final String KEY_PHARMACIES = "Pharmacies";
    public static final String PHARMACIES = "\uD83D\uDC8AАптека";
    public static final String KEY_COSMETICS = "Cosmetics";
    public static final String COSMETICS = "\uD83E\uDE77Косметика";
    public static final String KEY_ITEMS_OF_CLOTHING = "ItemsOfClothing";
    public static final String ITEMS_OF_CLOTHING = "\uD83D\uDC56Предметы одежды";
    public static final String KEY_SUPERMARKETS = "Supermarkets";
    public static final String SUPERMARKETS = "\uD83C\uDFEAСупермаркет";
    public static final String KEY_TRANSFERS = "Transfers";
    public static final String TRANSFERS = "\uD83D\uDCE8Переводы";
    public static final String KEY_ELECTRONICS = "Electronics";
    public static final String ELECTRONICS = "\uD83D\uDDA5\uFE0FЭлектроника";
    public static final String KEY_BILL_PAYMENT = "BillPayment";
    public static final String BILL_PAYMENT = "\uD83D\uDCCBОплата счетов";
    public static final String KEY_OTHER = "Other";
    public static final String OTHER = "\uD83D\uDCDCДругое";
    public static final String KEY_USERS_INCOME = "UserIncome";
    public static final String USERS_INCOME = "\uD83D\uDC64Указать свой";
    public static final String KEY_USERS_CATEGORY = "UserCategory";
    public static final String USERS_CATEGORY = "\uD83E\uDD13Указать свою";
    public static final String KEY_ANIMALS = "Animals";
    public static final String ANIMALS = "\uD83D\uDC08Животные";
    public static final String KEY_ONLINE_PAYMENTS = "OnlinePayments";
    public static final String ONLINE_PAYMENTS = "\uD83D\uDCE6Онлайн покупки";

    public static final String KEY_WAGES = "Wages";
    public static final String WAGES = "\uD83D\uDCB6Зарплата";
    public static final String KEY_BUSINESS = "Business";
    public static final String BUSINESS = "\uD83D\uDC54Бизнес";
    public static final String KEY_INVESTMENTS = "Investments";
    public static final String INVESTMENTS = "\uD83D\uDCB9Инвестиции";
    public static final String KEY_RENT = "Rent";
    public static final String RENT = "\uD83D\uDE0EРента";
    public static final String KEY_SOCIAL_BENEFITS = "SocialBenefits";
    public static final String SOCIAL_BENEFITS = "\uD83D\uDD4A\uFE0FСоциальные пособия";
    public static final String KEY_PENSION = "Pension";
    public static final String PENSION = "\uD83D\uDC74\uD83C\uDFFBПенсия";
    public static final String KEY_FREELANCING = "Freelancing";
    public static final String FREELANCING = "\uD83D\uDCBBФриланс";

    public static final String KEY_EXPENSES = "Expenses";
    public static final String KEY_INCOME = "Income";
    public static final String KEY_MONTH_EXP = "MonthExp";
    public static final String KEY_MONTH_INCOME = "MonthInc";
    public static final String KEY_MONTH_TOTAL = "MonthTot";
    public static final String MONTH = "Указать месяц";
    public static final String KEY_PERIOD_EXP = "PeriodExp";
    public static final String KEY_PERIOD_INCOME = "PeriodInc";
    public static final String KEY_PERIOD_TOTAL = "PeriodTot";
    public static final String PERIOD = "Указать период";
    public static final String KEY_REGISTRATION = "/register";
    public static final String REGISTRATION = "Зарегистрироваться";
    public static final String KEY_COMMANDS = "/help";
    public final static String COM_LIST = "Список команд";

    public static final List<String> allExpenses = Arrays.asList(
            ConstantDB.KEY_HOME_AND_RENOVATION, ConstantDB.KEY_TRANSPORT, ConstantDB.KEY_FOOD,
            ConstantDB.KEY_ENTERTAINMENT, ConstantDB.KEY_PHARMACIES, ConstantDB.KEY_COSMETICS,
            ConstantDB.KEY_ITEMS_OF_CLOTHING, ConstantDB.KEY_SUPERMARKETS, ConstantDB.KEY_TRANSFERS,
            ConstantDB.KEY_ELECTRONICS, ConstantDB.KEY_BILL_PAYMENT, ConstantDB.KEY_OTHER, ConstantDB.KEY_USERS_CATEGORY
    );

    public static final List<String> allIncomes = Arrays.asList(
            ConstantDB.KEY_WAGES, ConstantDB.KEY_BUSINESS,ConstantDB.KEY_INVESTMENTS, ConstantDB.KEY_RENT,
            ConstantDB.KEY_SOCIAL_BENEFITS,ConstantDB.KEY_PENSION,ConstantDB.KEY_FREELANCING
    );
}

