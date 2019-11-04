import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

public class Operations {
    Schedule schedule = new Schedule();
    Map<String, String> stations = new HashMap<>() {{
        put("АБАЙ", "2700137");
        put("НУР-СУЛТАН", "2708001");
        put("АЛМАТЫ", "2700000");
        put("АЛМАТЫ 2", "2700001");
        put("АКТОБЕ-1", "2704600");
        put("НУР-СУЛТАН-НУРЛЫ ЖОЛ", "2700152");
        put("ШЫМКЕНТ", "2700770");
        put("ЖЕЗКАЗГАН", "2708930");
        put("КАРАГАНДЫ", "2708952");
        put("АТЫРАУ", "2704830");
        put("АЛМАТЫ 1", "2700002");
        put("СЕМЕЙ", "2700900");
        put("МАНГИСТАУ", "2704807");
        put("ШУ", "2700780");
        put("ТАРАЗ", "2700710");
        put("КЫЗЫЛОРДА", "2704999");
        put("КОСТАНАЙ", "2708700");
        put("УРАЛЬСК", "2704810");
        put("КАРАГАНДЫ ПАСС", "2708950");
        put("ПАВЛОДАР", "2708900");
        put("ПЕТРОПАВЛОВСК", "2040500");
        put("КЫЗЫЛОРДА", "2704999");
        put("ТАЛДЫКОРГАН", "2700108");
        put("УРАЛЬСК", "2704810");
        put("ОСКЕМЕН-1", "2700730");
    }};
}
