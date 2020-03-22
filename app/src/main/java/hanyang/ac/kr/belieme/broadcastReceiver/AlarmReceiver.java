package hanyang.ac.kr.belieme.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import hanyang.ac.kr.belieme.dataType.ExceptionAdder;
import hanyang.ac.kr.belieme.dataType.History;
import hanyang.ac.kr.belieme.dataType.HistoryRequest;
import hanyang.ac.kr.belieme.dataType.HistoryStatus;
import hanyang.ac.kr.belieme.manager.PreferenceManager;
import hanyang.ac.kr.belieme.notification.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int historyId = intent.getIntExtra("historyId", -1);
        HistoryGetTask historyGetTask = new HistoryGetTask(context, intent);
        historyGetTask.execute(historyId);

    }

    public class HistoryGetTask extends AsyncTask<Integer, Void, ExceptionAdder<History>> {

        private Intent intent;
        private Context context;

        public HistoryGetTask(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        @Override
        protected ExceptionAdder<History> doInBackground(Integer... integers) {
            History result = new History();
            try {
                return new ExceptionAdder<>(HistoryRequest.getHistoryById(integers[0]));
            } catch (Exception e) {
                e.printStackTrace();
                return new ExceptionAdder<>(e);
            }
        }

        @Override
        protected void onPostExecute(ExceptionAdder<History> result) {
            if(result.getException() == null) {
                String type = intent.getStringExtra("type");
                History history = result.getBody();
                if (type.equals("forDelayed")) {
                    if (history.getRequesterId() == PreferenceManager.getInt(context, "gaeinNo") && history.getStatus() == HistoryStatus.DELAYED) {
                        System.out.println("연체됨");
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(history.getNotificationTitle(), history.getNotificationMessage());
                        notificationHelper.getManager().notify((int) System.currentTimeMillis() / 1000, nb.build());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date(System.currentTimeMillis()));
                        calendar.add(Calendar.DATE, 1);

                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        alarmIntent.putExtra("historyId", history.getId());
                        alarmIntent.putExtra("type", "forDelayed");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() / 1000, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    } else if (history.getRequesterId() == PreferenceManager.getInt(context, "gaeinNo") && history.getStatus() == HistoryStatus.USING) {
                        System.out.println("사용중");
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(history.getNotificationTitle(), history.getNotificationMessage());
                        notificationHelper.getManager().notify((int) System.currentTimeMillis() / 1000, nb.build());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(history.getDueDate());
                        calendar.add(Calendar.SECOND, 1);

                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        alarmIntent.putExtra("historyId", history.getId());
                        alarmIntent.putExtra("type", "forDelayed");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() / 1000, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                } else if (type.equals("forExpired")) {
                    if (history.getRequesterId() == Integer.parseInt(PreferenceManager.getString(context, "gaeinNo")) && history.getStatus() == HistoryStatus.EXPIRED) {
                        System.out.println("취소됨");
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(history.getNotificationTitle(), history.getNotificationMessage());
                        notificationHelper.getManager().notify((int) System.currentTimeMillis() / 1000, nb.build());
                    } else if (history.getRequesterId() == Integer.parseInt(PreferenceManager.getString(context, "gaeinNo")) && history.getStatus() == HistoryStatus.USING) {
                        // 반납 기한 아침에 보내기
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(history.getDueDate());
                        calendar.set(Calendar.HOUR_OF_DAY, 5);

                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        alarmIntent.putExtra("historyId", history.getId());
                        alarmIntent.putExtra("type", "forDelayed");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() / 1000, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                }
            }
            else {
                Toast.makeText(context, result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}