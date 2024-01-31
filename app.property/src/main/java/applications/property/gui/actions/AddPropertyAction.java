package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class AddPropertyAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IApplication application;

	public AddPropertyAction(IApplication application) {
		super("Property");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.addPropertyAction();
	}

}
