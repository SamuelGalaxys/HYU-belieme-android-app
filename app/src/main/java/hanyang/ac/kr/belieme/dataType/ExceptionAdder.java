package hanyang.ac.kr.belieme.dataType;

public class ExceptionAdder<T> {
    private T body;
    private Exception exception;

    public ExceptionAdder() {
        body = null;
        exception = null;
    }

    public ExceptionAdder(T body, Exception exception) {
        this.body = body;
        this.exception = exception;
    }

    public ExceptionAdder(T body) {
        this.body = body;
        exception = null;
    }

    public ExceptionAdder(Exception exception) {
        body = null;
        this.exception = exception;
    }

    public T getBody() {
        return body;
    }

    public Exception getException() {
        return exception;
    }
}
