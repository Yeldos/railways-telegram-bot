import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Operations {
    Schedule schedule = new Schedule();
    private long chat_id;
    private long operation_id = 1;
    String lastMessage = "";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    Boolean firstInitialization = true;
    String departureStation = "";
    String arrivalStation = "";
    Date departureDate = new Date();
    Date arrivalDate = new Date();
    InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();

    public String getMessage(String msg) {
        /*ArrayList keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (msg.equals("Укажите пункт отправления")) {
            lastMessage = msg;
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("На главное меню");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            //return getSchedule(schedule.getAllItems());
            return "Выберите город";
        }

        if (lastMessage.equals("Расписание")) {
            lastMessage = msg;
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Расписание");
            keyboardSecondRow.add("На главное меню");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Расписание";
        }

        if (msg.equals("На главное меню")) {
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Расписание");
            keyboardFirstRow.add("Помощь");
            keyboardSecondRow.add("О моем боте");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            operation_id = 1;
            return "Выберите раздел";
        }*/

        return "";
    }

    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Алматы");
        inlineKeyboardButton1.setCallbackData("Выбран город Алматы");
        inlineKeyboardButton2.setText("Астана");
        inlineKeyboardButton2.setCallbackData("Выбран город Астана");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Жезказган").setCallbackData("Выбран город Жезказган"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Выберите город").setReplyMarkup(inlineKeyboardMarkup);
    }
}
