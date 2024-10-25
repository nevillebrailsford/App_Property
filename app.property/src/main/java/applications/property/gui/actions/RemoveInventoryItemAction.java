package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class RemoveInventoryItemAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private IPropertyApplication application;

	public RemoveInventoryItemAction(IPropertyApplication application) {
		super("Inventory Item");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.removeInventoryItemAction();
	}

}
