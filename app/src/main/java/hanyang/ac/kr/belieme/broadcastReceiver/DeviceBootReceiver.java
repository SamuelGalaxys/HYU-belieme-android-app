package hanyang.ac.kr.belieme.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Objects;

import hanyang.ac.kr.belieme.manager.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;


//TODO 아몰라 다시해 목표 : Alarm array를 string으로 변환하고 다시 추출까지 가능하게 하여 PreferenceManager 이용해서 기기 내부에 저장
public class DeviceBootReceiver extends BroadcastReceiver {
    public class Alarm {
        private int historyId;
        private String type;
        private long timeMillis;

        public Alarm() {
        }

        public void setHistoryId(int historyId) {
            this.historyId = historyId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTimeMillis(long timeMillis) {
            this.timeMillis = timeMillis;
        }

        @Override
        public String toString() {
            return "{" +
                    "historyId=" + historyId +
                    ", type='" + type + '\'' +
                    ", timeMillis=" + timeMillis +
                    '}';
        }
    }

    private Alarm stringToAlarm(String string) {
        String onlyInfo =  string.substring(string.indexOf("{"), string.indexOf("}"));
        Alarm result = new Alarm();
        while(onlyInfo.indexOf(", ") != -1) {
            String tmp;
            tmp = onlyInfo.substring(2);
            int lastIndex = tmp.indexOf(", ") - 1;
            tmp = onlyInfo.substring(0, lastIndex + 2);

            if (tmp.substring(2, tmp.indexOf("=")).equals("historyId")) {
                result.setHistoryId(Integer.getInteger(tmp.substring(onlyInfo.indexOf("="))));
            } else if(tmp.substring(2, tmp.indexOf("=")).equals("type")) {
                result.setType(tmp.substring(tmp.indexOf("=") + 2, tmp.lastIndexOf("'") - 1));
            } else if(tmp.substring(2, tmp.indexOf("=")).equals("timeMillis")) {
                result.setTimeMillis(Long.getLong(tmp.substring(tmp.indexOf("="))));
            }
            onlyInfo = onlyInfo.substring(onlyInfo.indexOf(", "));
        }
        return result;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
//
//            // on device boot complete, reset the alarm
//            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) (System.currentTimeMillis() / 1000), alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//
//            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////
//
//            int remainNotificationCount = PreferenceManager.getInt(context, "remainNotificationCount");
//            for(int i = 0; i < remainNotificationCount; i++) {
//
//
//            Calendar current_calendar = Calendar.getInstance();
//            Calendar nextNotifyTime = Calendar.getInstance();
//            nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("nextNotifyTime", millis));
//
//            if (current_calendar.after(nextNotifyTime)) {
//                nextNotifyTime.add(Calendar.DATE, 1);
//            }
//
//            Date currentDateTime = nextNotifyTime.getTime();
//            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
//            Toast.makeText(context.getApplicationContext(),"[재부팅후] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
//
//
//            if (manager != null) {
//                manager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
//                        AlarmManager.INTERVAL_DAY, pendingIntent);
//            }
//        }
    }
}
