package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class RemoveInventoryItemAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private IApplication application;

	public RemoveInventoryItemAction(IApplication application) {
		super("Inventory Item");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.removeInventoryItemAction();
	}

}
