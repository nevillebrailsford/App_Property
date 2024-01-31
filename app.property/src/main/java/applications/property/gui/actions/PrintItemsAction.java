package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class PrintItemsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IApplication application;

	public PrintItemsAction(IApplication application) {
		super("Print Items");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.printItemsAction();
	}

}
