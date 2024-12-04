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
    public static final String USERS_HOME_AND_RENOVATION = "HomeAndRenovation";
    public static final String USERS_TRANSPORT = "Transport";
    public static final String USERS_FOOD = "Food";
    public static final String USERS_ENTERTAINMENT = "Entertainment";
    public static final String USERS_PHARMACIES = "Pharmacies";
    public static final String USERS_COSMETICS = "Cosmetics";
    public static final String USERS_ITEMS_OF_CLOTHING = "ItemsOfClothing";
    public static final String USERS_SUPERMARKETS = "Supermarkets";
    public static final String USERS_TRANSFERS = "Transfers";
    public static final String USERS_ELECTRONICS_AND_TECHNOLOGY = "ElectronicsAndTechnology";
    public static final String USERS_BOOKS = "Books";
    public static final String USERS_CONDITION = "User_Condition";
    public static final String USERS_OTHER = "Other";
    public static final String HOME_AND_RENOVATION = "Дом и ремонт";
    public static final String TRANSPORT = "Транспорт";
    public static final String FOOD = "Еда";
    public static final String ENTERTAINMENT = "Развлечения";
    public static final String PHARMACIES = "Аптека";
    public static final String COSMETICS = "Косметика";
    public static final String ITEMS_OF_CLOTHING = "Предметы одежды";
    public static final String SUPERMARKETS = "Супермаркет";
    public static final String TRANSFERS = "Переводы";
    public static final String ELECTRONICS_AND_TECHNOLOGY = "Электроника и технологии";
    public static final String BOOKS = "Книги";
    public static final String OTHER = "Другое";

    public static final String[] list_type_amounts = {ConstantDB.HOME_AND_RENOVATION, ConstantDB.TRANSPORT, ConstantDB.FOOD,
            ConstantDB.ENTERTAINMENT, ConstantDB.PHARMACIES, ConstantDB.COSMETICS, ConstantDB.ITEMS_OF_CLOTHING,
            ConstantDB.SUPERMARKETS, ConstantDB.TRANSFERS, ConstantDB.ELECTRONICS_AND_TECHNOLOGY, ConstantDB.BOOKS, ConstantDB.OTHER};
    public static final String[] list_amounts = {ConstantDB.USERS_HOME_AND_RENOVATION, ConstantDB.USERS_TRANSPORT, ConstantDB.USERS_FOOD,
            ConstantDB.USERS_ENTERTAINMENT, ConstantDB.USERS_PHARMACIES, ConstantDB.USERS_COSMETICS,
            ConstantDB.USERS_ITEMS_OF_CLOTHING, ConstantDB.USERS_SUPERMARKETS, ConstantDB.USERS_TRANSFERS,
            ConstantDB.USERS_ELECTRONICS_AND_TECHNOLOGY, ConstantDB.USERS_BOOKS, ConstantDB.USERS_OTHER};
}
