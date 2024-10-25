package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class ChangeMonitoredItemAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IPropertyApplication application;

	public ChangeMonitoredItemAction(IPropertyApplication application) {
		super("Monitored Item");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.changeMonitoredItemAction();
	}

}
