package applications.property.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import applications.property.gui.IApplication;

public class UndoAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private IApplication application;

	public UndoAction(IApplication application) {
		super("Undo");
		this.application = application;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		application.undoAction();
	}

}
