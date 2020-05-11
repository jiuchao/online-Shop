package onlineShop.log;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component 
public class PaymentAction {
	//This mean logger is a bean
	//Autowired means logger is managed by spring, spring will inject this object in
	@Autowired 
	private Logger logger;

	public void pay(BigDecimal payValue) {
		logger.log("pay begin, payValue is " + payValue);
		logger.log("pay end");
	}
}
