package pm190.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validates room names
 * @author Patrick Mackinder
 */
@FacesValidator("roomNameValidator")
public class RoomNameValidator implements Validator
{
	/**
	 * Validates room name, only alphanumeric and no spaces
	 */
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
	{
		String roomName = (String) value;
		System.out.println("Attempt add room " + roomName);
		Pattern p = Pattern.compile("\\w*");
		Matcher matcher = p.matcher(roomName);
		if(matcher.matches())
		{
			System.out.println("Valid roomanem");
			return;
		}
		throw new ValidatorException(new FacesMessage("Invalid roomname."));
	}
}
