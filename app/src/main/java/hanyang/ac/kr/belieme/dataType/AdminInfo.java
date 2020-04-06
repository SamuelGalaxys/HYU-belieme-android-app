package hanyang.ac.kr.belieme.dataType;

public class AdminInfo {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ERROR = 2;
    public static final int VIEW_TYPE_PROGRESS = 3;

    private int viewType;

    private int studentId;

    private String name;

    private Permission permission;

    public AdminInfo(int studentId, String name, Permission permission) {
        this.viewType = VIEW_TYPE_ITEM;
        this.studentId = studentId;
        this.name = name;
        this.permission = permission;
    }

    public AdminInfo() {

    }

    public int getViewType() {
        return viewType;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public static AdminInfo getErrorAdminInfo(String errorMessage) {
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setName(errorMessage);
        adminInfo.setViewType(VIEW_TYPE_ERROR);
        return adminInfo;
    }

    public static AdminInfo getProgressAdminInfo() {
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setViewType(VIEW_TYPE_PROGRESS);
        return adminInfo;
    }
}
