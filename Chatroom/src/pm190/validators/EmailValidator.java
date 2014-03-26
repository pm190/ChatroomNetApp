package pm190.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * 
 * @author Patrick Mackinder
 */
public class EmailValidator implements Validator
{
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
	{
		String password = (String) value;
		UIInput confirmComponent = (UIInput) component.getAttributes().get("confirmEmail");
		String confirm = (String) confirmComponent.getSubmittedValue();

		if(password == null || password.isEmpty() || confirm == null || confirm.isEmpty())
		{
			return;
		}

		if(!password.equals(confirm))
		{
			confirmComponent.setValid(false);
			throw new ValidatorException(new FacesMessage("Emails are not equal."));
		}
	}
}
