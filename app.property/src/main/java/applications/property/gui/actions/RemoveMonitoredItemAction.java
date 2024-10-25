package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class RemoveMonitoredItemAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IPropertyApplication application;

	public RemoveMonitoredItemAction(IPropertyApplication application) {
		super("Monitored Item");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.removeMonitoredItemAction();
	}

}
