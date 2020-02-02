package hanyang.ac.kr.belieme.DataType;

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

public class ItemTypeRequest {
    public static ArrayList<ItemType> getList() throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "itemType/");

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

        ArrayList<ItemType> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new ItemType(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getString("emoji"),
                    jsonArray.getJSONObject(i).getInt("count"),
                    jsonArray.getJSONObject(i).getInt("amount"))
            );
        }
        return items;
    }

    public static ItemType getItem(int id) throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "itemType/" + id);

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

        JSONObject jsonObject = new JSONObject(output);
        System.out.println(output);

        return new ItemType(jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("emoji"),
                    jsonObject.getInt("count"),
                    jsonObject.getInt("amount")
        );
    }

    public static ItemType addItem(ItemType itemType) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", itemType.getName());
        jsonObject.put("emoji", itemType.getEmoji());
        jsonObject.put("count", itemType.getCount());
        jsonObject.put("amount", itemType.getAmount());

        java.net.URL url = new URL(Constants.serverURL + "itemType/");
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


        ItemType newItemType = new ItemType(
                newJsonObject.getInt("id"),
                newJsonObject.getString("name"),
                newJsonObject.getString("emoji"),
                newJsonObject.getInt("count"),
                newJsonObject.getInt("amount"));
        return newItemType;
    }

    public static boolean editItem(ItemType itemType) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", itemType.getId());
        jsonObject.put("name", itemType.getName());
        jsonObject.put("emoji", itemType.getEmoji());
        jsonObject.put("count", itemType.getCount());
        jsonObject.put("amount", itemType.getAmount());

        java.net.URL url = new URL(Constants.serverURL + "itemType/");
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

    public static void deleteItem(int id) throws JSONException, IOException {
        String output = "";

        java.net.URL url = new URL(Constants.serverURL + "itemType/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("DELETE");
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
    }
}
