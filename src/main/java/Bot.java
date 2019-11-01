import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    Schedule schedule = new Schedule();
    Operations operations = new Operations();
    private long chat_id;
    private long operation_id = 1;
    String lastMessage = "";
    Boolean firstInitialization = true;
    String departureStation = "";
    String arrivalStation = "";
    Date departureDate = new Date();
    Date arrivalDate = new Date();

    ArrayList keyboard = new ArrayList<>();
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    KeyboardRow keyboardSecondRow = new KeyboardRow();

    public void onUpdateReceived(Update update) {
        update.getUpdateId();

        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("Расписание") && firstInitialization == false) {
                    try {
                        execute(resetKeyboard(update.getMessage().getChatId()));
                        execute(departureStation(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("Главное меню") && firstInitialization == false) {
                    try {
                        execute(
                                new SendMessage().setText(getMessage(update.getMessage().getText()))
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("Сема") && firstInitialization == false) {
                    try {
                        execute(
                                new SendMessage().setText(getMessage(update.getMessage().getText()))
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (firstInitialization || update.getMessage().getText().equals("/start")) {
                    try {
                        execute(
                                new SendMessage().setText(firstInitialization(update.getMessage().getText()))
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 3) {
                    try {
                        execute(
                                new SendMessage().setText(chooseDepartureDate(update.getMessage().getText()))
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 4) {
                    try {
                        execute(
                                new SendMessage().setText(chooseArrivalDate(update.getMessage().getText()))
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                        chat_id = update.getMessage().getChatId();
                        execute(
                                new SendMessage().setText(finalOperation())
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        execute(
                                new SendMessage().setText("Что-то пошло не так")
                                        .setChatId(update.getMessage().getChatId())
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().contains("Departure station")) {
                departureStation = update.getCallbackQuery().getData().substring(18);
                try {
                    execute(arrivalStation(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getCallbackQuery().getData().contains("Arrival station")) {
                arrivalStation = update.getCallbackQuery().getData().substring(16);
                try {
                    execute(departureDate(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "bileti_railways_bot";
    }

    @Override
    public String getBotToken() {
        return "904009265:AAGfaobhXuzrPlpN8dwVaCUDrBz36OO4E1k";
    }

    public String firstInitialization(String msg) {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();

        keyboardFirstRow.add("Расписание");
        keyboardFirstRow.add("Помощь");
        keyboardSecondRow.add("О моем боте");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        firstInitialization = false;
        return "Здравствуйте!";

    }

    public String getMessage(String msg) {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();

        if (msg.equals("Главное меню")) {
            keyboardFirstRow.add("Расписание");
            keyboardFirstRow.add("Помощь");
            keyboardSecondRow.add("О моем боте");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            firstInitialization = false;
            return "Здравствуйте!";
        } else if (msg.equals("Сема")) {
            keyboardFirstRow.add("Расписание");
            keyboardFirstRow.add("Помощь");
            keyboardSecondRow.add("О моем боте");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            firstInitialization = false;
            return "\uD83D\uDC25";
        }

        return "";
    }

    public String finalOperation() {
        operation_id = 1;
        schedule.departureStation = departureStation;
        schedule.arrivalStation = arrivalStation;
        return getSchedule(schedule.getAllItems());
    }

    public String getSchedule(String[] text) {
        SendMessage sendMessage = new SendMessage().setChatId(chat_id);
        for (int i = 0; i < text.length; i++) {
            try {
                sendMessage.setText(text[i]);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public SendMessage resetKeyboard(long chat_id) {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();

        keyboardFirstRow.add("Главное меню");
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        firstInitialization = false;

        return new SendMessage().setChatId(chat_id).setText("Выберите параметры \u2705").setReplyMarkup(replyKeyboardMarkup);
    }

    public SendMessage departureStation(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Алматы").setCallbackData("Departure station Almaty"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Жезказган").setCallbackData("Departure station Zhezkazgan"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Кызылорда").setCallbackData("Departure station Kyzylorda"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Павлодар").setCallbackData("Departure station Pavlodar"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Астана").setCallbackData("Departure station 2708001"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Петропавл").setCallbackData("Departure station 2040500"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Шымкент").setCallbackData("Departure station Shymkent"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Актау").setCallbackData("Departure station Aktau"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return new SendMessage().setChatId(chatId).setText("Станция отправления \uD83D\uDE89").setReplyMarkup(inlineKeyboardMarkup);
    }

    public SendMessage arrivalStation(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Алматы").setCallbackData("Arrival station Almaty"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Жезказган").setCallbackData("Arrival station Zhezkazgan"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Кызылорда").setCallbackData("Arrival station Kyzylorda"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Павлодар").setCallbackData("Arrival station Pavlodar"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Астана").setCallbackData("Arrival station Astana"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Петропавл").setCallbackData("Arrival station Petropavl"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Шымкент").setCallbackData("Arrival station Shymkent"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Актау").setCallbackData("Arrival station Aktau"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return new SendMessage().setChatId(chatId).setText("Станция назначения \uD83D\uDE89").setReplyMarkup(inlineKeyboardMarkup);
    }

    public SendMessage departureDate(long chat_id) {
        operation_id = 3;

        return new SendMessage().setChatId(chat_id).setText("Напишите дату отправления в формате дд.мм.гггг").setReplyMarkup(replyKeyboardMarkup);

    }

    public String chooseDepartureDate(String msg) {
        if (msg.matches("^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$")) {
            departureDate = parseDate(msg);
        }
        operation_id = 4;

        return "Напишите дату прибытия в формате дд.мм.гггг";
    }

    public String chooseArrivalDate(String msg) {
        if (msg.matches("^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$")) {
            arrivalDate = parseDate(msg);
        }
        operation_id = 5;

        return "Расписание \uD83D\uDDD3";
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
