package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class ViewOverdueItemsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IPropertyApplication application;

	public ViewOverdueItemsAction(IPropertyApplication application) {
		super("Overdue Items");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.viewOverdueItemsAction();
	}

}
