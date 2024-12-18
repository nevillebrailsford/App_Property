package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.application.IPropertyApplication;

public class PrintItemsSummaryAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IPropertyApplication application;

	public PrintItemsSummaryAction(IPropertyApplication application) {
		super("Print Items (Summary)");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.printItemsSummaryAction();
	}

}
