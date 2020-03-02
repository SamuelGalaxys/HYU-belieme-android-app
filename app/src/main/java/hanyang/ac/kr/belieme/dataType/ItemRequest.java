package hanyang.ac.kr.belieme.dataType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hanyang.ac.kr.belieme.Constants;

public class ItemRequest {
    public static ArrayList<Item> getList() throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "item/");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output += (line + "\n");
            }
            reader.close();
            connection.disconnect();
        }

        JSONArray jsonArray = new JSONArray(output);

        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new Item(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getInt("typeId"),
                    jsonArray.getJSONObject(i).getInt("num"),
                    ItemStatus.stringToItemStatus(jsonArray.getJSONObject(i).getString("status")),
                    jsonArray.getJSONObject(i).getInt("lastHistoryId")
            ));
        }
        return items;
    }

    public static ArrayList<Item> getListByTypeId(int typeId) throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "item/byTypeId/" + typeId);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output += (line + "\n");
            }
            reader.close();
            connection.disconnect();
        }

        JSONArray jsonArray = new JSONArray(output);

        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new Item(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getInt("typeId"),
                    jsonArray.getJSONObject(i).getInt("num"),
                    ItemStatus.stringToItemStatus(jsonArray.getJSONObject(i).getString("status")),
                    jsonArray.getJSONObject(i).getInt("lastHistoryId")
            ));
        }
        return items;
    }

    public static Item addItem(Item item) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("typeId", item.getTypeId());

        java.net.URL url = new URL(Constants.serverURL + "item/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            OutputStream os = connection.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = null;

                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output += (line + "\n");
                }
                reader.close();
            }
            connection.disconnect();
        }
        JSONObject newJsonObject = new JSONObject(output);

        Item newItem = new Item(
                newJsonObject.getInt("id"),
                newJsonObject.getInt("typeId"),
                newJsonObject.getInt("num"),
                ItemStatus.stringToItemStatus(newJsonObject.getString("status")),
                newJsonObject.getInt("lastHistoryId")
        );
        return newItem;
    }

    public static boolean editItem(Item item) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", item.getId());
        jsonObject.put("typeId", item.getTypeId());
        jsonObject.put("num", item.getNum());
        jsonObject.put("status", item.getStatus());
        jsonObject.put("lastHistoryId", item.getLastHistoryId());

        java.net.URL url = new URL(Constants.serverURL + "item/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            OutputStream os = connection.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();

            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = null;

                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output += (line + "\n");
                }
                reader.close();
            }
            connection.disconnect();
        }
        if(output.equals("true")) {
            return true;
        }
        else {
            return false;
        }
    }

//    public static void deleteItem(String name) throws JSONException, IOException {
//        String output = "";
//        JSONObject jsonObject = new JSONObject();
//
//        jsonObject.put("name", name);
//
//        java.net.URL url = new URL(ServerUrl.URL + "item/");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//
//        if (connection != null) {
//            connection.setConnectTimeout(10000);
//            connection.setRequestMethod("DELETE");
//
//            OutputStream os = connection.getOutputStream();
//            os.write(jsonObject.toString().getBytes());
//            os.flush();
//
//            connection.disconnect();
//        }
//    }
}