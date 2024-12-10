package org.bot.database;

public class ConstantDB {
    public static final String USER_TABLE = "users";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String TABLE_CATEGORY = "Category";
    public static final String ACCOUNTINGS_TABLE = "accountings";
    public static final String TABLE_USER_ID = "idUsers";
    public static final String TABLE_CATEGORIES = "idCategories";
    public static final String TABLE_DATE = "Date";
    public static final String TABLE_AMOUNT = "Amount";
    public static final String USERS_ID = "chatID";
    public static final String KEY_HOME_AND_RENOVATION = "HomeAndRenovation";
    public static final String KEY_TRANSPORT = "Transport";
    public static final String KEY_FOOD = "Food";
    public static final String KEY_ENTERTAINMENT = "Entertainment";
    public static final String KEY_PHARMACIES = "Pharmacies";
    public static final String KEY_COSMETICS = "Cosmetics";
    public static final String KEY_ITEMS_OF_CLOTHING = "ItemsOfClothing";
    public static final String KEY_SUPERMARKETS = "Supermarkets";
    public static final String KEY_TRANSFERS = "Transfers";
    public static final String KEY_ELECTRONICS = "Electronics";
    public static final String KEY_BILL_PAYMENT = "BillPayment";
    public static final String KEY_REGISTRATION = "/register";
    public static final String KEY_OTHER = "Other";
    public static final String KEY_COMMANDS = "/help";
    public static final String KEY_MONTH = "Month";
    public static final String KEY_PERIOD = "Period";
    public static final String MONTH = "Указать месяц";
    public static final String PERIOD = "Указать период";
    public final static String COM_LIST = "Список команд";
    public static final String REGISTRATION = "Зарегистрироваться";
    public static final String HOME_AND_RENOVATION = "\uD83C\uDFE1Дом и ремонт";
    public static final String TRANSPORT = "\uD83D\uDE8BТранспорт";
    public static final String FOOD = "\uD83C\uDF55Еда";
    public static final String ENTERTAINMENT = "\uD83C\uDF87Развлечения";
    public static final String PHARMACIES = "\uD83D\uDC8AАптека";
    public static final String COSMETICS = "\uD83E\uDE77Косметика";
    public static final String ITEMS_OF_CLOTHING = "\uD83D\uDC56Предметы одежды";
    public static final String SUPERMARKETS = "\uD83C\uDFEAСупермаркет";
    public static final String TRANSFERS = "\uD83D\uDCE8Переводы";
    public static final String ELECTRONICS = "\uD83D\uDDA5\uFE0FЭлектроника";
    public static final String BILL_PAYMENT = "\uD83D\uDCCBОплата счетов";
    public static final String OTHER = "\uD83D\uDCDCДругое";

    public static final String[] list_type_amounts = {ConstantDB.HOME_AND_RENOVATION, ConstantDB.TRANSPORT, ConstantDB.FOOD,
            ConstantDB.ENTERTAINMENT, ConstantDB.PHARMACIES, ConstantDB.COSMETICS, ConstantDB.ITEMS_OF_CLOTHING,
            ConstantDB.SUPERMARKETS, ConstantDB.TRANSFERS, ConstantDB.ELECTRONICS, ConstantDB.BILL_PAYMENT, ConstantDB.OTHER};
    public static final String[] list_amounts = {ConstantDB.KEY_HOME_AND_RENOVATION, ConstantDB.KEY_TRANSPORT, ConstantDB.KEY_FOOD,
            ConstantDB.KEY_ENTERTAINMENT, ConstantDB.KEY_PHARMACIES, ConstantDB.KEY_COSMETICS,
            ConstantDB.KEY_ITEMS_OF_CLOTHING, ConstantDB.KEY_SUPERMARKETS, ConstantDB.KEY_TRANSFERS,
            ConstantDB.KEY_ELECTRONICS, ConstantDB.KEY_BILL_PAYMENT, ConstantDB.KEY_OTHER};
}
