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
import hanyang.ac.kr.belieme.Exception.InternalServerException;

public class AdminInfoRequest {
    public static ArrayList<AdminInfo> getList() throws JSONException, IOException, InternalServerException {
        String output = "";
        URL url = new URL(Constants.serverURL + "admin/");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

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


        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<AdminInfo> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new AdminInfo(
                        body.getJSONObject(i).getInt("studentId"),
                        body.getJSONObject(i).getString("name"),
                        Permission.stringToHistoryStatus(body.getJSONObject(i).getString("permission"))
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<AdminInfo> addAdmin(AdminInfo adminInfo) throws IOException, JSONException, InternalServerException {
        String output = "";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("studentId", adminInfo.getStudentId());
        jsonObject.put("name", adminInfo.getName());
        jsonObject.put("permission", adminInfo.getPermission().toString());

        URL url = new URL(Constants.serverURL + "admin/");
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
            ArrayList<AdminInfo> items = new ArrayList<>();
            JSONArray body = outputJsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new AdminInfo(
                        body.getJSONObject(i).getInt("studentId"),
                        body.getJSONObject(i).getString("name"),
                        Permission.stringToHistoryStatus(body.getJSONObject(i).getString("permission"))
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<AdminInfo> setPermissionAdmin(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        URL url = new URL(Constants.serverURL + "admin/setPermissionAdmin/" + id);
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<AdminInfo> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new AdminInfo(
                        body.getJSONObject(i).getInt("studentId"),
                        body.getJSONObject(i).getString("name"),
                        Permission.stringToHistoryStatus(body.getJSONObject(i).getString("permission"))
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<AdminInfo> setPermissionMaster(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        URL url = new URL(Constants.serverURL + "admin/setPermissionMaster/" + id);
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<AdminInfo> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new AdminInfo(
                        body.getJSONObject(i).getInt("studentId"),
                        body.getJSONObject(i).getString("name"),
                        Permission.stringToHistoryStatus(body.getJSONObject(i).getString("permission"))
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }

    public static ArrayList<AdminInfo> deleteAdmin(int id) throws IOException, JSONException, InternalServerException {
        String output = "";

        URL url = new URL(Constants.serverURL + "admin/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection != null) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("DELETE");
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

        JSONObject jsonObject = new JSONObject(output);
        JSONObject header = jsonObject.getJSONObject("header");

        if(header.getInt("code") == InternalServerException.OK) {
            ArrayList<AdminInfo> items = new ArrayList<>();
            JSONArray body = jsonObject.getJSONArray("body");

            for (int i = 0; i < body.length(); i++) {
                items.add(new AdminInfo(
                        body.getJSONObject(i).getInt("studentId"),
                        body.getJSONObject(i).getString("name"),
                        Permission.stringToHistoryStatus(body.getJSONObject(i).getString("permission"))
                ));
            }
            return items;
        }
        else {
            throw new InternalServerException(header.getInt("code"), header.getString("message"));
        }
    }
}
