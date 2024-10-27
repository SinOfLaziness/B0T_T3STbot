package org.bot.msg;

import org.bot.functional.AttachedButtons;
import org.bot.functional.Keyboard;

public class Constants {
    private final static Keyboard keyboard = new Keyboard();
    private final static AttachedButtons attachedButtons = new AttachedButtons();
    public final static Message EXP_SUM = new Message("Впишите потраченную сумму");
    public final static Message INVALID_SUM = new Message("Пожалуйста, введите числовое значение");
    public final static Message NOT_IMPLEMENTED = new Message("Функция на этапе разработки. Пока я такое не могу делать");
    public final static Message EXP_LIST = new Message("Какие расходы ты хочешь указать?", attachedButtons.createButtonsForExpenses());
    public final static Message UNK_COM = new Message("Извини, такую команду я не знаю. Напиши Список команд, чтобы увидеть полный список команд", keyboard.generateStartKeyboard());
    public final static Message HELP_COM = new Message("""
            Функционал бота\s
            
            /start - начинает работу с ботом\s
            Список команд - выводит список доступных команд
            Зарегистрироваться - регистрирует пользователя""");
    public final static Message ASK_FOR_REG = new Message("Ты должен зарегистрироваться, чтобы использовать эту команду", keyboard.generateStartKeyboard());
    public final static Message ALR_REG = new Message("Ты уже зарегистрирован, можешь продолжить свою работу");
    public final static Message NOW_REG = new Message("Поздравляю! Теперь, ты можешь пользоваться всеми моими полезными штуками!");
    public final static String START_TEXT_TEMPL = "Приветствую тебя, %s. Перед началом пользования прошу тебя зарегистрироваться. Для этого напиши команду Зарегистрироваться";
    public final static String REGISTRATION = "Зарегистрироваться";
    public final static String START = "/start";
    public final static String COM_LIST = "Список команд";
    public final static String SET_EXP = "Записать расходы";
    public final static String SEND_EXP = "Вывести список расходов";
}