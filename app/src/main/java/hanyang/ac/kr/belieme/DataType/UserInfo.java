package hanyang.ac.kr.belieme.DataType;

public class UserInfo {
    private String studentId;
    private String name;
    private String majorCode;
    private String statusCode;

    public UserInfo(String studentId, String name, String majorCode, String statusCode) {
        this.studentId = studentId;
        this.name = name;
        this.majorCode = majorCode;
        this.statusCode = statusCode;
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
}
