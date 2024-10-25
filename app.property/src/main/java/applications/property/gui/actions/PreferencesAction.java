package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class PreferencesAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IPropertyApplication application;

	public PreferencesAction(IPropertyApplication application) {
		super("Preferences");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.preferencesAction();
	}

}
