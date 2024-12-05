package org.bot.msg;

import org.bot.functional.AttachedButtons;
import org.bot.functional.Keyboard;

public class Constants {
    private final static Keyboard keyboard = new Keyboard();
    private final static AttachedButtons attachedButtons = new AttachedButtons();

    public final static Message EXP_SUM = new Message("Впишите потраченную сумму");
    public final static Message INVALID_SUM = new Message("Некорректный формат ввода, проверьте:\n" +
            "() Положительное ли число вы вводите \n" +
            "() Учтите, что бот считает только рубли, копейки не принимаются\n" +
            "() Соответствует ли текст формату ввода:\n" +
            "положительная_сумма любой_текст_по_желанию");
    public final static Message NOT_IMPLEMENTED = new Message("Функция на этапе разработки. Пока я такое не могу делать");
    public final static Message EXP_LIST = new Message("\uD83D\uDCC9Какие расходы ты хочешь указать?", attachedButtons.createButtonsForExpenses());
    public final static Message UNK_COM = new Message("Извини, такую команду я не знаю. Напиши Список команд, чтобы увидеть полный список команд", keyboard.generateStartKeyboard());
    public final static Message HELP_COM = new Message("""
            Функционал бота\s
            
            /start - начинает работу с ботом\s
            Список команд - выводит список доступных команд
            Зарегистрироваться - регистрирует пользователя""");

    public final static Message INV_PERIOD = new Message("Период указан в неверном формате");
    public final static Message ASK_FOR_REG = new Message("Ты должен зарегистрироваться, чтобы использовать эту команду", keyboard.generateStartKeyboard());
    public final static Message ALR_REG = new Message("Ты уже зарегистрирован, можешь продолжить свою работу");
    public final static Message NOW_REG = new Message("Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!");
    public final static Message START_TEXT_TEMPL = new Message("Приветствую тебя, мой друг. Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду Зарегистрироваться", keyboard.generateStartKeyboard());
    public final static String REGISTRATION = "Зарегистрироваться";
    public final static String START = "/start";
    public final static String COM_LIST = "Список команд";
    public final static String SET_EXP = "\uD83D\uDCB8Записать расходы";
    public final static String SEND_EXP = "Вывести список расходов";
    public final static String WAIT_PERIOD = "WAITING_FOR_PERIOD";
    public final static Message ASK_PERIOD = new Message("""
    Напишите период, за который вы хотите получить статистику
    
    Он может быть представлен в виде двух дат формата
    
    <гггг-мм-дд> <гггг-мм-дд>  (пр. 2024-05-01 2024-06-13)
    
    Если вы хотите получить статистику за конкретный месяц:
    
    <гггг-мм> (пр. 2024-03)
    
    Порядок автоматически сделается хронологическим:""");

}
