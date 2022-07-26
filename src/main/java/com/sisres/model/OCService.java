package com.sisres.model;

import java.util.List;

public class OCService {
	public List<OC> getAllOC() throws SispagException {
		DAOOC daooc = new DAOOC();
		return daooc.getAllOC();
	}

	public OC getOC(String oc) throws SispagException {
		DAOOC daooc = new DAOOC();
		return daooc.getOC(oc);
	}
}
