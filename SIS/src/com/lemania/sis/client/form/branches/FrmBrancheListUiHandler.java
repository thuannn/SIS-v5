package com.lemania.sis.client.form.branches;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.BrancheProxy;

public interface FrmBrancheListUiHandler extends UiHandlers {
	//
	public void updateBranche(BrancheProxy branche, String brancheName, String brancheCoef, Boolean isActive);
}