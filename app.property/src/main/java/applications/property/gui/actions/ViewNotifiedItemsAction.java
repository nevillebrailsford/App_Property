package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class ViewNotifiedItemsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IApplication application;

	public ViewNotifiedItemsAction(IApplication application) {
		super("Notified Items");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.viewNotifiedItemsAction();
	}

}
