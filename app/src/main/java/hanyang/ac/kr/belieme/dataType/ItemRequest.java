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
import java.util.Date;

import hanyang.ac.kr.belieme.Constants;
import hanyang.ac.kr.belieme.Exception.InternalServerException;

public class ItemRequest {
    public static ArrayList<Item> getList() throws JSONException, IOException, InternalServerException {
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



        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<Item> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            History lastHistory;

            for(int i = 0; i < body.length(); i++) {
                if (body.getJSONObject(i).getInt("lastHistoryId") == -1) {
                    lastHistory = null;
                } else {
                    lastHistory = new History(body.getJSONObject(i).getInt("id"),
                            body.getJSONObject(i).getInt("typeId"),
                            body.getJSONObject(i).getInt("num"),
                            body.getJSONObject(i).getString("requesterName"),
                            body.getJSONObject(i).getInt("requesterId"),
                            body.getJSONObject(i).getString("responseManagerName"),
                            body.getJSONObject(i).getInt("responseManagerId"),
                            body.getJSONObject(i).getString("returnManagerName"),
                            body.getJSONObject(i).getInt("returnManagerId"),
                            new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000),
                            HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("lastHistoryStatus"))
                    );
                }
                items.add(new Item(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("num"),
                        ItemStatus.stringToItemStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getInt("lastHistoryId"),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji"),
                        lastHistory
                ));
            }
            return items;
        } else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<Item> getListByTypeId(int typeId) throws JSONException, IOException, InternalServerException {
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<Item> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            History lastHistory;

            for (int i = 0; i < body.length(); i++) {
                if (body.getJSONObject(i).getInt("lastHistoryId") == -1) {
                    lastHistory = null;
                } else {
                    lastHistory = new History(body.getJSONObject(i).getInt("id"),
                            body.getJSONObject(i).getInt("typeId"),
                            body.getJSONObject(i).getInt("num"),
                            body.getJSONObject(i).getString("requesterName"),
                            body.getJSONObject(i).getInt("requesterId"),
                            body.getJSONObject(i).getString("responseManagerName"),
                            body.getJSONObject(i).getInt("responseManagerId"),
                            body.getJSONObject(i).getString("returnManagerName"),
                            body.getJSONObject(i).getInt("returnManagerId"),
                            new Date(body.getJSONObject(i).getLong("requestTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("responseTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("returnTimeStamp") * 1000),
                            new Date(body.getJSONObject(i).getLong("cancelTimeStamp") * 1000),
                            HistoryStatus.stringToHistoryStatus(body.getJSONObject(i).getString("lastHistoryStatus"))
                    );
                }
                items.add(new Item(
                        body.getJSONObject(i).getInt("id"),
                        body.getJSONObject(i).getInt("typeId"),
                        body.getJSONObject(i).getInt("num"),
                        ItemStatus.stringToItemStatus(body.getJSONObject(i).getString("status")),
                        body.getJSONObject(i).getInt("lastHistoryId"),
                        body.getJSONObject(i).getString("typeName"),
                        body.getJSONObject(i).getString("typeEmoji"),
                        lastHistory
                ));
            }
            return items;
        }else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static Item getItem(int id) throws IOException, JSONException, InternalServerException {
        String output = "";
        URL url = new URL(Constants.serverURL + "item/" + id);

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

            History lastHistory;

            if (body.getInt("lastHistoryId") == -1) {
                lastHistory = null;
            } else {
                lastHistory = new History(body.getInt("id"),
                        body.getInt("typeId"),
                        body.getInt("num"),
                        body.getString("requesterName"),
                        body.getInt("requesterId"),
                        body.getString("responseManagerName"),
                        body.getInt("responseManagerId"),
                        body.getString("returnManagerName"),
                        body.getInt("returnManagerId"),
                        new Date(body.getLong("requestTimeStamp") * 1000),
                        new Date(body.getLong("responseTimeStamp") * 1000),
                        new Date(body.getLong("returnTimeStamp") * 1000),
                        new Date(body.getLong("cancelTimeStamp") * 1000),
                        HistoryStatus.stringToHistoryStatus(body.getString("lastHistoryStatus"))
                );
            }
            return new Item(
                    body.getInt("id"),
                    body.getInt("typeId"),
                    body.getInt("num"),
                    ItemStatus.stringToItemStatus(body.getString("status")),
                    body.getInt("lastHistoryId"),
                    body.getString("typeName"),
                    body.getString("typeEmoji"),
                    lastHistory
            );
        } else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static Item addItem(Item item) throws IOException, JSONException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("typeId", item.getTypeId());

        URL url = new URL(Constants.serverURL + "item/");
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
            JSONObject body = outputJsonObject.getJSONObject("body");

            History lastHistory;

            if (body.getInt("lastHistoryId") == -1) {
                lastHistory = null;
            } else {
                lastHistory = new History(body.getInt("id"),
                        body.getInt("typeId"),
                        body.getInt("num"),
                        body.getString("requesterName"),
                        body.getInt("requesterId"),
                        body.getString("responseManagerName"),
                        body.getInt("responseManagerId"),
                        body.getString("returnManagerName"),
                        body.getInt("returnManagerId"),
                        new Date(body.getLong("requestTimeStamp") * 1000),
                        new Date(body.getLong("responseTimeStamp") * 1000),
                        new Date(body.getLong("returnTimeStamp") * 1000),
                        new Date(body.getLong("cancelTimeStamp") * 1000),
                        HistoryStatus.stringToHistoryStatus(body.getString("lastHistoryStatus"))
                );
            }

            Item newItem = new Item(
                    body.getInt("id"),
                    body.getInt("typeId"),
                    body.getInt("num"),
                    ItemStatus.stringToItemStatus(body.getString("status")),
                    body.getInt("lastHistoryId"),
                    body.getString("typeName"),
                    body.getString("typeEmoji"),
                    lastHistory
            );
            return newItem;
        } else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static void activateItem(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        java.net.URL url = new URL(Constants.serverURL + "item/activate/"+id);
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
        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") != InternalServerException.OK) {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static void deactivateItem(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        java.net.URL url = new URL(Constants.serverURL + "item/deactivate/"+id);
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
        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") != InternalServerException.OK) {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }
}