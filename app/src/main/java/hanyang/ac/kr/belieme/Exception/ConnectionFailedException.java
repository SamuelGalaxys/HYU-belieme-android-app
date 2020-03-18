package hanyang.ac.kr.belieme.Exception;

public class ConnectionFailedException extends Exception {
    public ConnectionFailedException() {
    }

    public ConnectionFailedException(String message) {
        super(message);
    }
}
