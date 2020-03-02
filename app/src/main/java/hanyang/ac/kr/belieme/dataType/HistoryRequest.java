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
import java.util.Date;

import hanyang.ac.kr.belieme.Constants;

public class HistoryRequest {

    public static History getHistoryById(int id) throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "history/" + id);

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

        return new History(
                jsonObject.getInt("id"),
                jsonObject.getInt("typeId"),
                jsonObject.getInt("itemNum"),
                jsonObject.getString("requesterName"),
                jsonObject.getInt("requesterId"),
                jsonObject.getString("managerName"),
                jsonObject.getInt("managerId"),
                new Date(jsonObject.getInt("requestTimeStamp")*1000),
                new Date(jsonObject.getInt("responseTimeStamp")*1000),
                new Date(jsonObject.getInt("returnedTimeStamp")*1000),
                HistoryStatus.stringToHistoryStatus(jsonObject.getString("status")),
                jsonObject.getString("typeName")
        );
    }

    public static ArrayList<History> getList() throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "history/");

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

        Log.d("jsonArray", output);
        JSONArray jsonArray = new JSONArray(output);


        ArrayList<History> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new History(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getInt("typeId"),
                    jsonArray.getJSONObject(i).getInt("itemNum"),
                    jsonArray.getJSONObject(i).getString("requesterName"),
                    jsonArray.getJSONObject(i).getInt("requesterId"),
                    jsonArray.getJSONObject(i).getString("managerName"),
                    jsonArray.getJSONObject(i).getInt("managerId"),
                    new Date(jsonArray.getJSONObject(i).getInt("requestTimeStamp")*1000),
                    new Date(jsonArray.getJSONObject(i).getInt("responseTimeStamp")*1000),
                    new Date(jsonArray.getJSONObject(i).getInt("returnedTimeStamp")*1000),
                    HistoryStatus.stringToHistoryStatus(jsonArray.getJSONObject(i).getString("status")),
                    jsonArray.getJSONObject(i).getString("typeName")
            ));
        }
        return items;
    }

    public static ArrayList<History> getListByRequesterId(int requesterId ) throws JSONException, IOException {
        String output = "";
        URL url = new URL(Constants.serverURL + "history/byRequesterId/" + requesterId);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;

            int num = 0;
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

        ArrayList<History> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new History(
                    jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getInt("typeId"),
                    jsonArray.getJSONObject(i).getInt("itemNum"),
                    jsonArray.getJSONObject(i).getString("requesterName"),
                    jsonArray.getJSONObject(i).getInt("requesterId"),
                    jsonArray.getJSONObject(i).getString("managerName"),
                    jsonArray.getJSONObject(i).getInt("managerId"),
                    new Date(jsonArray.getJSONObject(i).getInt("requestTimeStamp")*1000),
                    new Date(jsonArray.getJSONObject(i).getInt("responseTimeStamp")*1000),
                    new Date(jsonArray.getJSONObject(i).getInt("returnedTimeStamp")*1000),
                    HistoryStatus.stringToHistoryStatus(jsonArray.getJSONObject(i).getString("status")),
                    jsonArray.getJSONObject(i).getString("typeName")
            ));
        }
        return items;
    }

    public static History addItem(History item) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("typeId", item.getTypeId());
        jsonObject.put("itemNum", item.getItemNum());
        jsonObject.put("requesterName", item.getRequesterName());
        jsonObject.put("requesterId", item.getRequesterId());
        jsonObject.put("managerName", item.getManagerName());
        jsonObject.put("managerId", item.getManagerId());
        jsonObject.put("requestTimeStamp", item.getRequestTimeStamp().getTime() / 1000);
        if(item.getResponseTimeStamp() != null)
            jsonObject.put("responseTimeStamp", item.getResponseTimeStamp().getTime() / 1000);
        if(item.getReturnedTimeStamp() != null)
            jsonObject.put("returnedTimeStamp", item.getReturnedTimeStamp().getTime() / 1000);
        jsonObject.put("status", item.getStatus().toString());


        java.net.URL url = new URL(Constants.serverURL + "history/");
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


        History newItem = new History(
                newJsonObject.getInt("id"),
                newJsonObject.getInt("typeId"),
                newJsonObject.getInt("itemNum"),
                newJsonObject.getString("requesterName"),
                newJsonObject.getInt("requesterId"),
                newJsonObject.getString("managerName"),
                newJsonObject.getInt("managerId"),
                new Date(newJsonObject.getInt("requestTimeStamp") * 1000),
                new Date(newJsonObject.getInt("responseTimeStamp") * 1000),
                new Date(newJsonObject.getInt("returnedTimeStamp") * 1000),
                HistoryStatus.stringToHistoryStatus(newJsonObject.getString("status")),
                newJsonObject.getString("typeName") // It's just null
        );
        return newItem;
    }

    public static boolean editItem(History item) throws IOException, JSONException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", item.getId());
        jsonObject.put("requesterName", item.getRequesterName());
        jsonObject.put("managerId", item.getManagerId());
        jsonObject.put("managerName", item.getManagerName());
        jsonObject.put("status", item.getStatus().toString());


        java.net.URL url = new URL(Constants.serverURL + "history/");
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
        System.out.println(output);
        if(output.equals("true")) {
            return true;
        }
        else {
            return false;
        }
    }
}
