package wura.project.mini_banking_app.exception;

public class UserAlreadyExistException extends  RuntimeException{

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
