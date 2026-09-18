package wura.project.mini_banking_app.exception;

public class ResourceNotFoundException  extends  RuntimeException{
    public ResourceNotFoundException (String message) {
        super(message);
    }
}
