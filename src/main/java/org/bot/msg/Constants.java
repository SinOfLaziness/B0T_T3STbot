package org.bot.msg;

import org.bot.database.ConstantDB;
import org.bot.functional.AttachedButtons;
import org.bot.functional.Keyboard;

public class Constants {
    private final static Keyboard keyboard = new Keyboard();
    private final static AttachedButtons attachedButtons = new AttachedButtons();

    public final static Message EXP_SUM = new Message("\uD83D\uDCB3 Впишите потраченную сумму в рублях");
    public final static Message INVALID_SUM = new Message("Некорректный формат ввода, проверьте:\n\n" +
            "\uD83D\uDFE9 Положительное ли число вы вводите \n" +
            "\uD83D\uDCDD Соответствует ли число формату ввода:\n\n❗ ПОЛОЖИТЕЛЬНОЕ ЧИСЛО МЕНЕЕ 10 ТРИЛЛИОНОВ\n❗ ЧИСЛО ЗНАКОВ ПОСЛЕ ЗАПЯТОЙ НЕ БОЛЕЕ ДВУХ");
    public final static Message UNK_COM = new Message("Извини, такую команду я не знаю\uD83E\uDD14. Напиши /help, чтобы увидеть полный список команд");
    public final static Message EMPTY_RESULT = new Message("За этот период не было записано ни одной траты\uD83E\uDD74");
    public final static Message HELP_COM = new Message("""
            Функционал бота😁\s
            
            /start - начинает работу с ботом\s
            /help - выводит список доступных команд
            /register - регистрирует пользователя
            
            Пользуйтесь КЛАВИАТУРОЙ снизу, это очень удобно❗""");

    public final static Message INV_PERIOD = new Message("Что-то пошло не так\uD83D\uDE15\nВозможно ты указал неверный формат или неправильную дату");
    public final static Message ASK_FOR_REG = new Message("\uD83D\uDE15 Ты должен зарегистрироваться, чтобы использовать эту команду",
            attachedButtons.createButtonsForRegistration());
    public final static Message ALR_REG = new Message("Ты уже зарегистрирован, можешь продолжить свою работу\uD83D\uDC40");
    public final static Message NOW_REG = new Message("\uD83E\uDD42 Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!\uD83C\uDF8A");
    public final static Message START_TEXT_TEMPL = new Message("Приветствую тебя, мой друг\uD83D\uDC4B\uD83C\uDFFB." +
            " Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду /register или нажми на кнопку снизу\uD83D\uDC47\uD83C\uDFFB",
            attachedButtons.createButtonsForRegistration());
    public final static String START = "/start";
    public final static String SET_EXP = "\uD83D\uDCB8Записать расходы";
    public final static String SEND_EXP = "\uD83D\uDCCAВывести список расходов";
    public final static Message ASK_PERIOD = new Message("""
            Укажите, каким образом вы хотите записать интервал времени📆""",attachedButtons.createButtonsForPeriodFormat());
    public final static Message MONTH_PATTERN = new Message("""
            📅Запишите месяц в формате:\n\nгггг-мм\n\n✅Пример ввода:  2024-12""");
    public final static Message PERIOD_PATTERN = new Message("""
            🗓️Укажите период, за который вы хотите получить статистику\nХронологический порядок выставится автоматически:\n\nгггг-мм-дд    гггг-мм-дд\n\n✅Пример ввода:  2024-12-03 2024-12-22""");
    public final static Message EXP_LIST = new Message("Какие расходы ты хочешь указать?⬇\uFE0F", attachedButtons.createButtonsForExpenses());
}
