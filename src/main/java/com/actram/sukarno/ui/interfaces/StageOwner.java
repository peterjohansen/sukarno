package com.actram.sukarno.ui.interfaces;

import com.actram.sukarno.ui.Sukarno;

import javafx.stage.Stage;

/**
 *
 *
 * @author Peter André Johansen
 */
public interface StageOwner {
	public Stage getStage();

	public void setStage(Sukarno program, Stage stage);
}