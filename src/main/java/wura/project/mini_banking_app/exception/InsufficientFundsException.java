package wura.project.mini_banking_app.exception;

public class InsufficientFundsException extends  RuntimeException{
    public InsufficientFundsException (String message){
        super((message));
    }

}
