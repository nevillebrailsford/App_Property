package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class ViewAllItemsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IApplication application;

	public ViewAllItemsAction(IApplication application) {
		super("All Items");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.viewAllItemsAction();
	}

}
