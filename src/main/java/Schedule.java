import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Schedule {
    private Document document;
    String departureStation = "";
    String arrivalStation = "";
    String departureDate = "";
    String arrivalDate = "";

    public String[] getAllItems() {
        try {
            departureDate = departureDate.replace(", ", "%2C+");
            document = Jsoup.connect("https://bilet.railways.kz/sale/default/route/search?route_search_form%5BdepartureStation%5D=" + departureStation +
                    "&route_search_form%5BarrivalStation%5D=" + arrivalStation +
                    "&route_search_form%5BforwardDepartureDate%5D=" + departureDate +
                    "&route_search_form%5BbackwardDepartureDate%5D=").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements error = document.getElementsByClass("ui error flash message");
        Elements items = document.getElementsByClass("train item");
        String[] info;

        if (error.size() == 1 && items.size() == 0) {
            info = new String[1];
            info[0] = "В указанную дату поезд не ходит";
        } else if (error.size() == 0 && items.size() == 0) {
            info = new String[1];
            info[0] = "Билетов нет";
        } else {
            info = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                if (i < 20) {
                    Elements train = items.get(i).getElementsByClass("ui header mobile hidden train-information");
                    Elements dep = items.get(i).getElementsByClass("ui header mobile hidden departure-time");
                    Elements arr = items.get(i).getElementsByClass("ui header mobile hidden arrival-time");

                    Elements elements = items.get(i).select("table[class=ui two column equal width very basic table mobile hidden]");
                    Elements rows = elements.get(0).select("tr");
                    String seatNumbers = "";
                    for (int j = 0; j < rows.size()-1; j++) {
                        seatNumbers += rows.get(j).getElementsByClass("left aligned").text() + " - " + rows.get(j).getElementsByClass("right aligned").text() + "\n";
                    }

                    info[i] = "№ Поезда: " + train.attr("data-title") + " \uD83D\uDE86" + "\n"
                            + "Отправление: " + dep.text().substring(0, 21) + "\n"
                            + "Прибытие: " + arr.text().substring(0, 21) + "\n\n"
                            + seatNumbers;
                }
            }
        }

        return info;
    }

    public String getDepartureStation() {
        Elements elements = document.getElementsByClass("ui header mobile hidden departure-time");
        return elements.text();
    }

    public String getArrivalStation() {
        Elements elements = document.getElementsByClass("ui header mobile hidden arrival-time");
        return elements.text();
    }
}
