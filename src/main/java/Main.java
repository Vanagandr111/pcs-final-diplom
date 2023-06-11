import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Запущен сервер с портом " + serverSocket.getLocalPort());
            while (true) {
                try (Socket socket = serverSocket.accept();
                  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
                {
                    System.out.println("Клиент подключен");
                    out.println("connected");
                    String s;
                    while((s = in.readLine()) != null) {
                        var output = outputFromSearchResult(engine.search(s));
                        System.out.println(output);
                        out.println(output);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }

        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    }

    public static String outputFromSearchResult(List<PageEntry> searchResult) {
        var array = new JSONArray();
        if(searchResult != null) {
            searchResult.forEach(pageEntry -> {
                var pageJsonObject = new JSONObject();
                pageJsonObject.put("pdfName", pageEntry.getPdfName());
                pageJsonObject.put("page", pageEntry.getPage());
                pageJsonObject.put("count", pageEntry.getCount());
                array.add(pageJsonObject);
            });
        }

        return array.toJSONString();
    }
}