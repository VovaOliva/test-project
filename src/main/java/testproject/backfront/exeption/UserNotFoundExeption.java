package testproject.backfront.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundExeption extends RuntimeException {

public UserNotFoundExeption(String message){
    super(message);
}

}
