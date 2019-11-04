import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    Schedule schedule = new Schedule();
    Operations operations = new Operations();
    private long chat_id;
    long operation_id = 0;
    Boolean firstInitialization = true;
    String searchStation = "";
    String departureStation = "";
    String arrivalStation = "";
    String departureDate = "";
    String arrivalDate = "";


    ArrayList keyboard = new ArrayList<>();
    KeyboardRow keyboardFirstRow = new KeyboardRow();
    KeyboardRow keyboardSecondRow = new KeyboardRow();

    boolean sendMeStations = false;
    boolean dateChosen = false;

    public void onUpdateReceived(Update update) {
        update.getUpdateId();

        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                chat_id = update.getMessage().getChatId();
                if (update.getMessage().getText().equals("Расписание") && firstInitialization == false) {
                    try {
                        execute(prepareDepStation(chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("Главное меню") && firstInitialization == false) {
                    try {
                        execute(getMessage(update.getMessage().getText(), chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("Сема") && firstInitialization == false) {
                    try {
                        execute(getMessage(update.getMessage().getText(), chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (firstInitialization || update.getMessage().getText().equals("/start")) {
                    try {
                        execute(
                                new SendMessage().setText(firstInitialization(update.getMessage().getText()))
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 1 && sendMeStations) {
                    try {
                        searchStation = update.getMessage().getText();
                        execute(getMessage(update.getMessage().getText(), chat_id));
                        // execute(resetKeyboard2(chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 2 && sendMeStations) {
                    try {
                        searchStation = update.getMessage().getText();
                        execute(getMessage(update.getMessage().getText(), chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 3 && !dateChosen) {
                    try {
                        execute(
                                new SendMessage().setText(chooseDepartureDate(update.getMessage().getText()))
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                        execute(
                                new SendMessage().setText(finalOperation())
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (operation_id == 3 && dateChosen) {
                    try {
                        execute(
                                new SendMessage().setText(finalOperation())
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("Помощь")) {
                    try {
                        execute(getMessage(update.getMessage().getText(), chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (update.getMessage().getText().equals("О моем боте")) {
                    try {
                        execute(getMessage(update.getMessage().getText(), chat_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                /*else if (operation_id == 4) {
                    try {
                        execute(
                                new SendMessage().setText(chooseArrivalDate(update.getMessage().getText()))
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                        execute(
                                new SendMessage().setText(finalOperation())
                                        .setChatId(chat_id)
                                        .setReplyMarkup(replyKeyboardMarkup)
                        );
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }*/
                else {
                    try {
                        execute(
                                new SendMessage().setText("Что-то пошло не так")
                                        .setChatId(chat_id)
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
                    execute(prepareArrStation(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getCallbackQuery().getData().contains("Arrival station")) {
                arrivalStation = update.getCallbackQuery().getData().substring(16);
                try {
                    searchStation = "";
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

    public SendMessage getMessage(String msg, long chat_id) {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardSecondRow.clear();

        boolean foundStation = false;
        Map<String, String> stations = new HashMap<>();

        for (Map.Entry<String, String> e : operations.stations.entrySet()) {
            if (e.getKey().regionMatches(true, 0, searchStation, 0, searchStation.length())) {
                foundStation = true;
                stations.put(e.getKey(), e.getValue());
            }
        }

        if (msg.equals("Главное меню")) {
            keyboardFirstRow.add("Расписание");
            keyboardFirstRow.add("Помощь");
            keyboardSecondRow.add("О моем боте");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            firstInitialization = false;
            operation_id = 0;
            return new SendMessage().setChatId(chat_id).setText("Здравствуйте!").setReplyMarkup(replyKeyboardMarkup);
        } else if (msg.equals("Сема")) {
            keyboardFirstRow.add("Расписание");
            keyboardFirstRow.add("Помощь");
            keyboardSecondRow.add("О моем боте");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            firstInitialization = false;
            operation_id = 0;
            return new SendMessage().setChatId(chat_id).setText("\uD83D\uDC25").setReplyMarkup(replyKeyboardMarkup);
        } else if (msg.equals("Помощь")) {
            operation_id = 0;
            return new SendMessage().setChatId(chat_id).setText("Для того чтобы получить расписание поездов, выберите пункт меню «Расписание». Далее следуйте указаниям").setReplyMarkup(replyKeyboardMarkup);
        } else if (msg.equals("О моем боте")) {
            operation_id = 0;
            return new SendMessage().setChatId(chat_id).setText("С помощью этого бота вы можете получить расписание поездов").setReplyMarkup(replyKeyboardMarkup);
        } else if (operation_id == 1 && foundStation) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

            for (Map.Entry<String, String> e : stations.entrySet()) {
                if (e.getKey().regionMatches(true, 0, searchStation, 0, searchStation.length())) {
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText(e.getKey()).setCallbackData("Departure station "+e.getValue()));
                }
            }

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            inlineKeyboardMarkup.setKeyboard(rowList);

            sendMeStations = false;

            return new SendMessage().setChatId(chat_id).setText("Выберите станцию отправления \uD83D\uDE89").setReplyMarkup(inlineKeyboardMarkup);
        } else if (operation_id == 2 && foundStation) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

            for (Map.Entry<String, String> e : stations.entrySet()) {
                if (e.getKey().regionMatches(true, 0, searchStation, 0, searchStation.length())) {
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText(e.getKey()).setCallbackData("Arrival station "+e.getValue()));
                }
            }

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            inlineKeyboardMarkup.setKeyboard(rowList);

            sendMeStations = false;

            return new SendMessage().setChatId(chat_id).setText("Выберите станцию назначения \uD83D\uDE89").setReplyMarkup(inlineKeyboardMarkup);
        }

        return new SendMessage().setChatId(chat_id).setText("Станция не найдена. Попробуйте еще раз");
    }

    public String finalOperation() {
        schedule.departureStation = departureStation;
        schedule.arrivalStation = arrivalStation;
        schedule.departureDate = departureDate;
        String result = getSchedule(schedule.getAllItems());

        if (result.contains("В указанную дату поезд не ходит")) {
            dateChosen = true;
        } else if (result.contains("Билетов нет")) {
            dateChosen = true;
        }

        return result;
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

    public SendMessage prepareDepStation(long chat_id) {
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
        operation_id = 1;
        sendMeStations = true;

        return new SendMessage().setChatId(chat_id).setText("Введите название станции отправления. Минимум 3 буквы \u270F").setReplyMarkup(replyKeyboardMarkup);
    }

    public SendMessage prepareArrStation(long chat_id) {
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
        sendMeStations = true;
        operation_id = 2;

        return new SendMessage().setChatId(chat_id).setText("Введите название станции назначения. Минимум 3 буквы \u270F").setReplyMarkup(replyKeyboardMarkup);
    }

    public SendMessage departureDate(long chat_id) {
        operation_id = 3;

        return new SendMessage().setChatId(chat_id).setText("Введите дату в формате дд.мм.гггг").setReplyMarkup(replyKeyboardMarkup);

    }

    public String chooseDepartureDate(String msg) {
        if (msg.matches("^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$")) {
            long milliseconds = 0;

            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date d = f.parse(msg);
                milliseconds = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            departureDate = formatDate(milliseconds, "dd-MM-yyyy, EEE");
        }


        //return "Напишите дату прибытия в формате дд.мм.гггг";
        return "Расписание \uD83D\uDDD3";
    }

    /*public String chooseArrivalDate(String msg) {
        if (msg.matches("^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$")) {
            long milliseconds = 0;

            SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date d = f.parse(msg);
                milliseconds = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            arrivalDate = formatDate(milliseconds, "dd-MM-yyyy, EEE");
        }
        operation_id = 5;

        return "Расписание \uD83D\uDDD3";
    }*/

    public static String formatDate(long date, String format) {
        Locale locale = new Locale("ru");
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
        String[] months = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        String[] shortMonths = {
                "янв", "фев", "мар", "апр", "май", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек"};
        dfs.setMonths(months);
        dfs.setShortMonths(shortMonths);
        String[] weekdays = {"", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
        String[] shortWeekdays = {"", "вск", "пнд", "втр", "срд", "чтв", "птн", "сбт"};
        dfs.setWeekdays(weekdays);
        dfs.setShortWeekdays(shortWeekdays);

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        sdf.setDateFormatSymbols(dfs);
        return sdf.format(date);
    }
}
