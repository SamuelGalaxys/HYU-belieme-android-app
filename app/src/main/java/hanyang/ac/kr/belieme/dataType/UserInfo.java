package hanyang.ac.kr.belieme.dataType;

public class UserInfo {
    private String studentId;
    private String name;
    private String majorCode;
    private String statusCode;
    private String daehakName;
    private String majorName;
    private String status;

    public UserInfo(String studentId, String name, String majorCode, String statusCode, String daehakName, String majorName, String status) {
        this.studentId = studentId;
        this.name = name;
        this.majorCode = majorCode;
        this.statusCode = statusCode;
        this.daehakName = daehakName;
        this.majorName = majorName;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getDaehakName() {
        return daehakName;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getStatus() {
        return status;
    }
}
