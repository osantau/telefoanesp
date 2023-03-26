package oct.soft.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class MyValidator {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	static {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	
	public static String validate(Object obj) {
		String errors = "";
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
		
		if (!constraintViolations.isEmpty()) {
			errors+="<ul>";
			for(ConstraintViolation<Object> constraintViolation : constraintViolations) {
				errors += "<li>" + constraintViolation.getMessage()
				+ "</li>";
			}
			errors+="</ul>";
		}
		
		return errors;
	}
}
