package application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FieldValidation {

	public FieldValidation() {}
	
	
	public boolean emailValidation(String email) {
		Pattern patern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@ [a-zA-Z0-9]+([.][a-zA-Z+])+");
		Matcher matcher = patern.matcher(email);
		if(matcher.find() && matcher.group().equals(email)) {
			System.out.println("Corect");
			return true;
			
		}else {
			System.out.println("False");
			return false;
		}
	}
	
}
