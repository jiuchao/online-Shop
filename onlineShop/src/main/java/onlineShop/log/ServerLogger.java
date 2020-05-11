package onlineShop.log;

import org.springframework.stereotype.Component;

//Tell spring it is a bean, and its name is serverLogger
@Component(value = "serverLogger") //Bean id
public class ServerLogger implements Logger {
    public void log(String info) {
        System.out.println("server log = " + info);
    }
}
