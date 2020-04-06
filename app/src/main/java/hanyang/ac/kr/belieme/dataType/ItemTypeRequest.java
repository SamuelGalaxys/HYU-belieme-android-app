package hanyang.ac.kr.belieme.dataType;

import android.util.Log;

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
import hanyang.ac.kr.belieme.Exception.InternalServerException;

public class ItemTypeRequest {
    public static ArrayList<ItemType> getList() throws JSONException, IOException, InternalServerException {
        String output = "";
        URL url = new URL(Constants.serverURL + "itemType/");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            int HttpResult = connection.getResponseCode();
            Log.d("httpResult", String.valueOf(HttpResult));
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


        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<ItemType> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new ItemType(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getString("name"),
                        body.getJSONObject(i).getString("emoji"),
                        body.getJSONObject(i).getInt("amount"),
                        body.getJSONObject(i).getInt("count"),
                        ItemStatus.stringToItemStatus(body.getJSONObject(i).getString("status")))
                );
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<ItemType> addItem(ItemType itemType) throws IOException, JSONException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", itemType.getName());
        jsonObject.put("emoji", itemType.getEmoji());
        jsonObject.put("amount", itemType.getAmount());
        jsonObject.put("count", itemType.getCount());

        URL url = new URL(Constants.serverURL + "itemType/");
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

        JSONObject outputJsonObject = new JSONObject(output);
        JSONObject header = outputJsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            JSONArray body = outputJsonObject.getJSONArray("body");
            ArrayList<ItemType> result = new ArrayList<>();
            for(int i = 0; i < body.length(); i++) {
                result.add(new ItemType(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getString("name"),
                        body.getJSONObject(i).getString("emoji"),
                        body.getJSONObject(i).getInt("amount"),
                        body.getJSONObject(i).getInt("count"),
                        ItemStatus.stringToItemStatus(body.getJSONObject(i).getString("status"))
                ));
            }
            return result;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<ItemType> editItem(ItemType itemType) throws IOException, JSONException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", itemType.getId());
        jsonObject.put("name", itemType.getName());
        jsonObject.put("emoji", itemType.getEmoji());

        URL url = new URL(Constants.serverURL + "itemType/");
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
        JSONObject outputJsonObject = new JSONObject(output);
        JSONObject header = outputJsonObject.getJSONObject("header");

        if(header.getInt("code") != InternalServerException.OK) {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        } else {
            JSONArray body = outputJsonObject.getJSONArray("body");
            ArrayList<ItemType> result = new ArrayList<>();
            for(int i = 0; i < body.length(); i++) {
                result.add(new ItemType(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getString("name"),
                        body.getJSONObject(i).getString("emoji"),
                        body.getJSONObject(i).getInt("amount"),
                        body.getJSONObject(i).getInt("count"),
                        ItemStatus.stringToItemStatus(body.getJSONObject(i).getString("status"))
                ));
            }
            return result;
        }
    }

    public static void deactivateItem(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        URL url = new URL(Constants.serverURL + "itemType/deactivate/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("PUT");
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
        JSONObject outputJsonObject = new JSONObject(output);
        JSONObject header = outputJsonObject.getJSONObject("header");

        if(header.getInt("code") != InternalServerException.OK) {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }
}
