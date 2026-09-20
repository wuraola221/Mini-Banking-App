package wura.project.mini_banking_app.exception;

public class AmountMustBeGreaterThanZeroException extends  RuntimeException{

    public AmountMustBeGreaterThanZeroException(String message) {
        super(message);
    }
}
