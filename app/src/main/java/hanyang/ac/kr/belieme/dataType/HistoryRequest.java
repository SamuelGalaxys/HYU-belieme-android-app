package hanyang.ac.kr.belieme.dataType;

import android.util.Pair;

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
import hanyang.ac.kr.belieme.Exception.ConnectionFailedException;
import hanyang.ac.kr.belieme.Exception.HttpRequestException;
import hanyang.ac.kr.belieme.Exception.InternalServerException;

public class HistoryRequest {

    public static History getHistoryById(int id) throws JSONException, IOException, InternalServerException {
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
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            JSONObject body = jsonObject.getJSONObject("body");

            return new History(
                    body.getInt("id"),
                    body.getInt("typeId"),
                    body.getInt("itemNum"),
                    body.getString("requesterName"),
                    body.getInt("requesterId"),
                    body.getString("responseManagerName"),
                    body.getInt("responseManagerId"),
                    body.getString("returnManagerName"),
                    body.getInt("returnManagerId"),
                    new Date(((long) body.getInt("requestTimeStamp")) * 1000L),
                    new Date(((long) body.getInt("responseTimeStamp")) * 1000L),
                    new Date(((long) body.getInt("returnTimeStamp")) * 1000L),
                    new Date(((long) body.getInt("cancelTimeStamp")) * 1000L),
                    HistoryStatus.stringToHistoryStatus(body.getString("status")),
                    body.getString("typeName"),
                    body.getString("typeEmoji")
            );
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<History> getList() throws JSONException, IOException, InternalServerException {
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");


        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<History> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new History(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("itemNum"),
                        body.getJSONObject(i).getString("requesterName"),
                        body.getJSONObject(i).getInt("requesterId"),
                        body.getJSONObject(i).getString("responseManagerName"),
                        body.getJSONObject(i).getInt("responseManagerId"),
                        body.getJSONObject(i).getString("returnManagerName"),
                        body.getJSONObject(i).getInt("returnManagerId"),
                        new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000L),
                        HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji")
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<History> getListByRequesterId(int requesterId ) throws JSONException, IOException, InternalServerException {
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            JSONArray body = jsonObject.getJSONArray("body");
            ArrayList<History> items = new ArrayList<>();

            for (int i = 0; i < body.length(); i++) {
                items.add(new History(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("itemNum"),
                        body.getJSONObject(i).getString("requesterName"),
                        body.getJSONObject(i).getInt("requesterId"),
                        body.getJSONObject(i).getString("responseManagerName"),
                        body.getJSONObject(i).getInt("responseManagerId"),
                        body.getJSONObject(i).getString("returnManagerName"),
                        body.getJSONObject(i).getInt("returnManagerId"),
                        new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000L),
                        HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji")
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static Pair<History, ArrayList<ItemType>> addItem(History item) throws IOException, JSONException, HttpRequestException, ConnectionFailedException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("typeId", item.getTypeId());
        jsonObject.put("requesterName", item.getRequesterName());
        jsonObject.put("requesterId", item.getRequesterId());

        URL url = new URL(Constants.serverURL + "history/");
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
                connection.disconnect();
            }
            else {
                String responseMessage = connection.getResponseMessage();
                connection.disconnect();
                throw new HttpRequestException(HttpResult + " " + responseMessage);
            }
        }
        else {
            throw new ConnectionFailedException();
        }

        JSONObject outputJsonObject = new JSONObject(output);
        JSONObject header = outputJsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            JSONObject body = outputJsonObject.getJSONObject("body");

            JSONObject historyJsonObject = body.getJSONObject("history");

            History historyResult = new History(
                    historyJsonObject.getInt("id"),
                    historyJsonObject.getInt("typeId"),
                    historyJsonObject.getInt("itemNum"),
                    historyJsonObject.getString("requesterName"),
                    historyJsonObject.getInt("requesterId"),
                    historyJsonObject.getString("responseManagerName"),
                    historyJsonObject.getInt("responseManagerId"),
                    historyJsonObject.getString("returnManagerName"),
                    historyJsonObject.getInt("returnManagerId"),
                    new Date(((long) historyJsonObject.getInt("requestTimeStamp")) * 1000L),
                    new Date(((long) historyJsonObject.getInt("responseTimeStamp")) * 1000L),
                    new Date(((long) historyJsonObject.getInt("returnTimeStamp")) * 1000L),
                    new Date(((long) historyJsonObject.getInt("cancelTimeStamp")) * 1000L),
                    HistoryStatus.stringToHistoryStatus(historyJsonObject.getString("status")),
                    historyJsonObject.getString("typeName"),
                    historyJsonObject.getString("typeEmoji")
            );

            JSONArray itemTypeListJsonObject = body.getJSONArray("itemTypeList");
            ArrayList<ItemType> itemTypeListResult = new ArrayList<>();

            for (int i = 0; i < itemTypeListJsonObject.length(); i++) {
                itemTypeListResult.add(new ItemType(
                        itemTypeListJsonObject.getJSONObject(i).getInt("id"),
                        itemTypeListJsonObject.getJSONObject(i).getString("name"),
                        itemTypeListJsonObject.getJSONObject(i).getString("emoji"),
                        itemTypeListJsonObject.getJSONObject(i).getInt("amount"),
                        itemTypeListJsonObject.getJSONObject(i).getInt("count"),
                        ItemStatus.stringToItemStatus(itemTypeListJsonObject.getJSONObject(i).getString("status")))
                );
            }
            return new Pair<>(historyResult, itemTypeListResult);
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<History> cancelItem(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        URL url = new URL(Constants.serverURL + "history/cancel/" + id);
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
            ArrayList<History> items = new ArrayList<>();

            for (int i = 0; i < body.length(); i++) {
                items.add(new History(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("itemNum"),
                        body.getJSONObject(i).getString("requesterName"),
                        body.getJSONObject(i).getInt("requesterId"),
                        body.getJSONObject(i).getString("responseManagerName"),
                        body.getJSONObject(i).getInt("responseManagerId"),
                        body.getJSONObject(i).getString("returnManagerName"),
                        body.getJSONObject(i).getInt("returnManagerId"),
                        new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000L),
                        HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji")
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<History> returnItem(History history) throws IOException, JSONException, InternalServerException {
        String output = "";

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("returnManagerId", history.getResponseManagerId());
        jsonObject.put("returnManagerName", history.getResponseManagerName());

        URL url = new URL(Constants.serverURL + "history/return/" + history.getId());
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

        if(header.getInt("code") == InternalServerException.OK) {
            JSONArray body = outputJsonObject.getJSONArray("body");
            ArrayList<History> items = new ArrayList<>();

            for (int i = 0; i < body.length(); i++) {
                items.add(new History(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("itemNum"),
                        body.getJSONObject(i).getString("requesterName"),
                        body.getJSONObject(i).getInt("requesterId"),
                        body.getJSONObject(i).getString("responseManagerName"),
                        body.getJSONObject(i).getInt("responseManagerId"),
                        body.getJSONObject(i).getString("returnManagerName"),
                        body.getJSONObject(i).getInt("returnManagerId"),
                        new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000L),
                        HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji")
                ));
            }
            return items;
        } else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<History> responseItem(History history) throws IOException, JSONException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("responseManagerId", history.getResponseManagerId());
        jsonObject.put("responseManagerName", history.getResponseManagerName());

        URL url = new URL(Constants.serverURL + "history/response/" + history.getId());
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

        if(header.getInt("code") == InternalServerException.OK) {
            JSONArray body = outputJsonObject.getJSONArray("body");
            ArrayList<History> items = new ArrayList<>();

            for (int i = 0; i < body.length(); i++) {
                items.add(new History(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("itemNum"),
                        body.getJSONObject(i).getString("requesterName"),
                        body.getJSONObject(i).getInt("requesterId"),
                        body.getJSONObject(i).getString("responseManagerName"),
                        body.getJSONObject(i).getInt("responseManagerId"),
                        body.getJSONObject(i).getString("returnManagerName"),
                        body.getJSONObject(i).getInt("returnManagerId"),
                        new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000L),
                        new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000L),
                        HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji")
                ));
            }
            return items;
        } else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }
}
